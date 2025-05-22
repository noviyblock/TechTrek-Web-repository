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
    Staffs,
    Resources
)
from src.services import llm_service, state_store

logger = logging.getLogger("routers.game")

router = APIRouter(prefix="/game", tags=["game"])
# ---------------------------------------------------------------------------
# -1. generate missions
# ---------------------------------------------------------------------------



# ---------------------------------------------------------------------------
# 0. create new game
# ---------------------------------------------------------------------------

class NewGameRequest(BaseModel):
    sphere: str
    mission: str
    startup_name: str
    money: int = Field(100_000, ge=0)
    tech: int = Field(0, ge=0, le=100, alias="technicReadiness")
    product: int = Field(0, ge=0, le=100, alias="productReadiness")
    motivation: int = Field(100, ge=0, le=100)


    class Config:
        populate_by_name = True


@router.post("/new", response_model=GameState)
async def new_game(cfg: NewGameRequest = Body(...)):
    state = state_store.create_initial_state(
        sphere=cfg.sphere,
        mission=cfg.mission,
        startup_name=cfg.startup_name,
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
async def generate_crisis(res: Resources, staffs: Staffs, game_id: UUID = Body(..., embed=True)):
    state = state_store.load_state(game_id)

    # --- синхронизация ресурсов
    if res.money is not None:
        state.resources.money = res.money
    if res.tech is not None:
        state.resources.tech = res.tech
    if res.product is not None:
        state.resources.product = res.product
    if res.motivation is not None:
        state.resources.motivation = res.motivation
    if res.months_passed is not None:
        state.resources.months_passed = res.months_passed

    state_store.save_state(state)

    if any(v is not None for v in (staffs.juniors, staffs.middles, staffs.seniors, staffs.c_levels)):
        state_store.update_staff(
            staffs.game_id,
            juniors=staffs.juniors,
            middles=staffs.middles,
            seniors=staffs.seniors,
            c_levels=staffs.c_levels,
        )
        state = state_store.load_state(game_id)

    if state is None:
        raise HTTPException(404, "Игровая сессия не найдена")
    try:
        crisis = llm_service.generate_crisis(state)
        state_store.push_history(game_id, "Assistant", crisis.description)
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
    if req.months_passed is not None:
        state.resources.months_passed = req.months_passed

    state_store.save_state(state)

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

    

    try:
        score = llm_service.evaluate_decision(req)
        state_store.push_history(req.game_id, "User", req.decision)
        result = EvaluateDecisionResult(
            resource_delta={},
            applied_mods={},
            text_to_player="Предварительная оценка сохранена.",
            quality_score=score,
        )
        return result
    except Exception as exc:
        raise HTTPException(500, f"Ошибка оценки решения: {exc}") from exc

