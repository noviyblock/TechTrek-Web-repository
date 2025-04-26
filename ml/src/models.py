"""src/models.py
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Domain & API models for TechTrek Web ML‑API.

* Pydantic data‑classes for **game state** и **HTTP‑схемы**.
* Enum‑helpers.
* `SYSTEM_PROMPT_BASE` — базовый system‑промпт (RU) c ресурсом «мотивация».
"""
from __future__ import annotations

from enum import Enum
from typing import Optional, List, Dict
from uuid import UUID, uuid4

from pydantic import BaseModel, Field

# ---------------------------------------------------------------------------
# System prompt (RU)
# ---------------------------------------------------------------------------

SYSTEM_PROMPT_BASE = (
    "Ты — GM настольно‑ролевой бизнес‑симуляции «TechTrek Web». Следуй правилам:\n"
    "1) Игровые ресурсы: деньги ($), техническая готовность, продуктовая готовность, мотивация, время в месяцах; диапазон 0‑100, деньги — целые.\n"
    "2) Команда: C‑level (CEO, CTO, CMO) и сотрудники (PM, Dev junior/middle/senior). C‑level дают модификатор +1…+2 к броску 2d6 при использовании; подряд‑использование повышает стоимость."
)

# ---------------------------------------------------------------------------
# Enums
# ---------------------------------------------------------------------------

class Stage(str, Enum):
    PRE_MVP = "pre_mvp"
    POST_MVP = "post_mvp"
    SCALE = "scale"

class Zone(str, Enum):
    CRITICAL_FAIL = "critical_fail"
    FAIL = "fail"
    NEUTRAL = "neutral"
    SUCCESS = "success"
    CRITICAL_SUCCESS = "critical_success"

class SpecialistRole(str, Enum):
    CEO = "CEO"
    CTO = "CTO"
    CMO = "CMO"
    PM = "PM"
    DEV = "Dev"

class Level(str, Enum):
    JUNIOR = "junior"
    MIDDLE = "middle"
    SENIOR = "senior"

# ---------------------------------------------------------------------------
# Staff & Resources models
# ---------------------------------------------------------------------------

class Specialist(BaseModel):
    role: SpecialistRole
    level: Optional[Level] = None       # C‑level → None
    stamina: int = 0                   # ходов подряд использования
    salary: int = 0                    # PM / Dev
    modifier: int = 0                  # +k к 2d6

class Resources(BaseModel):
    """Игровые метрики (0‑100), деньги — любое целое."""

    money: int = Field(100_000, ge=0)
    tech: int = Field(0, ge=0, le=100, alias="technicReadiness")
    product: int = Field(0, ge=0, le=100, alias="productReadiness")
    motivation: int = Field(100, ge=0, le=100)
    months_passed: int = Field(0, ge=0)

    class Config:
        populate_by_name = True

# ---------------------------------------------------------------------------
# Game state
# ---------------------------------------------------------------------------

class GameState(BaseModel):
    game_id: UUID = Field(default_factory=uuid4)
    stage: Stage
    resources: Resources
    staff: List[Specialist] = []
    inventory: List[str] = []
    history: List[str] = []            # краткие логи

# ---------------------------------------------------------------------------
# HTTP‑schemas
# ---------------------------------------------------------------------------

class CrisisResponse(BaseModel):
    title: str
    description: str
    danger_level: int                # 1‑3
    recommended_roles: List[str]
    forbidden_roles: Optional[List[str]] = None
    resource_targets: List[str]


class EvaluateDecisionRequest(BaseModel):
    """Запрос от фронта на оценку решения игрока."""

    game_id: UUID
    decision: str = Field(..., min_length=3)

    # Текущие ресурсы (опционально)
    money: int | None = Field(None, ge=0)
    tech: int | None = Field(None, ge=0, le=100, alias="technicReadiness")
    product: int | None = Field(None, ge=0, le=100, alias="productReadiness")
    motivation: int | None = Field(None, ge=0, le=100)

    # Штат
    juniors: int | None = None
    middles: int | None = None
    seniors: int | None = None
    c_levels: List[str] | None = Field(None, alias="superEmployees")

    shop_actions: List[str] | None = None

    class Config:
        populate_by_name = True


class EvaluateDecisionResult(BaseModel):
    resource_delta: Dict[str, int]
    applied_mods: Dict[str, int]
    text_to_player: str
    quality_score: float              # 0.0 < score ≤ 1.0 — P("Yes")


class ResolveRequest(BaseModel):
    state: GameState
    pre_roll: EvaluateDecisionResult
    dice_total: int                   # 2‑12
    zone: Zone
