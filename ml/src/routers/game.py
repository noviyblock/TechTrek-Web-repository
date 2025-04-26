"""src/routers/game.py
~~~~~~~~~~~~~~~~~~~~
FastAPI‑роутер: базовые методы игрового цикла TechTrek.

* POST `/game/new`               — создать новую партию
* POST `/game/generate_crisis`   — сформировать кризис / возможность
* POST `/game/evaluate_decision` — оценить решение игрока до броска кубиков
* POST `/game/resolve`           — применить бросок кубиков и обновить ресурсы
"""
from __future__ import annotations

import logging
from uuid import UUID

from fastapi import APIRouter, Body, HTTPException
from pydantic import BaseModel, Field

from src.models import (
    GameState,
    CrisisResponse,
    EvaluateDecisionRequest,
    EvaluateDecisionResult,
    ResolveRequest,
    Zone,
)
from src.services import llm_service, state_store

logger = logging.getLogger("routers.game")

router = APIRouter(prefix="/game", tags=["game"])

# ---------------------------------------------------------------------------
# 0. create new game
# ---------------------------------------------------------------------------

class NewGameRequest(BaseModel):
    money: int = Field(100_000, ge=0)
    tech: int = Field(0, ge=0, le=100, alias="technicReadiness")
    product: int = Field(0, ge=0, le=100, alias="productReadiness")
    motivation: int = Field(100, ge=0, le=100)

    class Config:
        populate_by_name = True


@router.post("/new", response_model=GameState)
async def new_game(cfg: NewGameRequest = Body(NewGameRequest())):
    state = state_store.create_initial_state(
        money=cfg.money,
        tech=cfg.tech,
        product=cfg.product,
        motivation=cfg.motivation,
    )
    return state

# ---------------------------------------------------------------------------
# 1. generate_crisis
# ---------------------------------------------------------------------------

@router.post("/generate_crisis", response_model=CrisisResponse)
async def generate_crisis(game_id: UUID = Body(..., embed=True)):
    state = state_store.load_state(game_id)
    if state is None:
        raise HTTPException(404, "Игровая сессия не найдена")
    try:
        crisis = llm_service.generate_crisis(state)
        state_store.push_history(game_id, crisis.title)
        return crisis
    except Exception as exc:
        raise HTTPException(500, f"Ошибка генерации кризиса: {exc}") from exc

# ---------------------------------------------------------------------------
# 2. evaluate_decision
# ---------------------------------------------------------------------------

@router.post("/evaluate_decision", response_model=EvaluateDecisionResult)
async def evaluate_decision(req: EvaluateDecisionRequest):
    """Запрашивает у LLM вероятность "Yes" и возвращает EvaluateDecisionResult."""
    state = state_store.load_state(req.game_id)
    if state is None:
        raise HTTPException(404, "Игровая сессия не найдена")

    # --- синхронизация ресурсов
    if req.money is not None:
        state.resources.money = req.money
    if req.tech is not None:
        state.resources.tech = req.tech
    if req.product is not None:
        state.resources.product = req.product
    if req.motivation is not None:
        state.resources.motivation = req.motivation

    # --- синхронизация штата (если передано)
    if any(v is not None for v in (req.juniors, req.middles, req.seniors, req.c_levels)):
        state_store.update_staff(
            req.game_id,
            juniors=req.juniors,
            middles=req.middles,
            seniors=req.seniors,
            c_levels=req.c_levels,
        )
        state = state_store.load_state(req.game_id)

    state_store.save_state(state)

    try:
        score = llm_service.evaluate_decision(req)
        result = EvaluateDecisionResult(
            resource_delta={},  # пока не считаем
            applied_mods={},    # пока не считаем
            text_to_player="Предварительная оценка сохранена.",
            quality_score=score,
        )
        return result
    except Exception as exc:
        raise HTTPException(500, f"Ошибка оценки решения: {exc}") from exc

# ---------------------------------------------------------------------------
# 3. resolve (decision + dice)
# ---------------------------------------------------------------------------

@router.post("/resolve", response_model=GameState)
async def resolve(req: ResolveRequest):
    if not 2 <= req.dice_total <= 12:
        raise HTTPException(400, "dice_total должен быть 2‑12")

    zone_map = {
        range(2, 5): Zone.CRITICAL_FAIL,
        range(5, 7): Zone.FAIL,
        range(7, 10): Zone.NEUTRAL,
        range(10, 12): Zone.SUCCESS,
        range(12, 13): Zone.CRITICAL_SUCCESS,
    }
    expected_zone = next((z for rng, z in zone_map.items() if req.dice_total in rng), None)
    if expected_zone != req.zone:
        raise HTTPException(400, "zone не соответствует dice_total")

    pre_roll = req.pre_roll  # получаем напрямую из запроса

    state = state_store.load_state(req.state.game_id)
    if state is None:
        raise HTTPException(404, "Игровая сессия не найдена")
    try:
        updated_state = llm_service.apply_dice_result(state, pre_roll, req)
        state_store.save_state(updated_state)
        return updated_state
    except Exception as exc:
        raise HTTPException(500, f"Ошибка при применении броска: {exc}") from exc
