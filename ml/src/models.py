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
        "Ты — AI‑ведущий настольно‑ролевой бизнес‑симуляции ‘TechTrek Web’. Правила игры:\n"
        "1) Ресурсы: деньги ($), техническая готовность, продуктовая готовность, мотивация и время (месяцы). Диапазон 0‑100, деньги — целые. Стартовые значения: $100000, 2 разработчика, 1 PM, CEO, офис и прототип продукта.\n"
        "2) Команда включает C‑level (CEO, CTO, CMO) и сотрудников (PM, Dev junior/middle/senior). Использование C‑level в кризисе даёт +1…+2 к броску 2d6; повторное применение подряд увеличивает цену.\n"
        "3) Каждый ход начинается с кризиса или возможности. Игрок за ограниченное время описывает решение, затем бросается 2d6 и ресурсы изменяются. Зоны: 2‑4 критический провал, 5‑6 провал, 7‑9 нейтрально, 10‑11 успех, 12 критический успех.\n"
        "4) Техническая готовность — стабильность и масштабируемость кода; продуктовая готовность — соответствие рынку и рост аудитории. Высокая мотивация ускоряет прогресс, низкая ведёт к задержкам и выгоранию.\n"
        "5) Цель — провести стартап через этапы pre_mvp, post_mvp и scale, балансируя ресурсы."
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

class Staffs(BaseModel):
    juniors: int | None = None
    middles: int | None = None
    seniors: int | None = None
    c_levels: List[str] | None = Field(None, alias="superEmployees")

# ---------------------------------------------------------------------------
# Game state
# ---------------------------------------------------------------------------

class GameState(BaseModel):
    game_id: UUID = Field(default_factory=uuid4)
    stage: Stage
    sphere: str
    mission: str
    startup_name: Optional[str] = None
    resources: Resources
    staff: List[Specialist]
    inventory: List[str]
    history: List[str]        # краткие логи

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
    months_passed: int = Field(0, ge=0)

    # Штат
    juniors: int | None = None
    middles: int | None = None
    seniors: int | None = None
    c_levels: List[str] | None = Field(None, alias="superEmployees")

    shop_actions: List[str] | None = None

    class Config:
        populate_by_name = True


class EvaluateDecisionResult(BaseModel):
    text_to_player: str
    quality_score: float              # 0.0 < score ≤ 1.0 — P("Yes")


class ResolveRequest(BaseModel):
    state: GameState
    pre_roll: EvaluateDecisionResult
    dice_total: int                   # 2‑12
    zone: Zone
