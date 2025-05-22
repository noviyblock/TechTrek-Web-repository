"""src/services/state_store.py
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
In‑memory стораж для `GameState` (MVP).
Переопределить на Postgres / Redis‑JSON в продакшене.
"""
from __future__ import annotations

from typing import Dict, List
from uuid import UUID, uuid4

from src.models import GameState, Resources, Stage, Specialist, SpecialistRole, Level

# ---------------------------------------------------------------------------
# Internal registry (UUID ➜ GameState)
# ---------------------------------------------------------------------------
_store: Dict[UUID, GameState] = {}

# ---------------------------------------------------------------------------
# API — синхронный (FastAPI сам может вызывать sync‑функции).
# ---------------------------------------------------------------------------

def create_initial_state(
    sphere: str,
    mission: str,
    startup_name: str | None = None,
    money: int = 100_000,
    tech: int = 0,
    product: int = 0,
    motivation: int = 100,
    months_passed: int = 0,
) -> GameState:
    """Создать новую игровую сессию с базовыми ресурсами."""
    gs = GameState(
        game_id=uuid4(),
        stage=Stage.PRE_MVP,
        sphere=sphere,
        mission=mission,
        startup_name=startup_name,
        resources=Resources(
            money=money,
            tech=tech,
            product=product,
            motivation=motivation,
            months_passed=months_passed,
        ),
        staff=[],
        inventory=[],
        history=[],
    )
    _store[gs.game_id] = gs.model_copy(deep=True)
    return gs


# --- базовые геттер / сеттер ----------------------------------------------

def load_state(game_id: UUID) -> GameState:
    """Вернуть копию `GameState` или кинуть `KeyError`."""
    try:
        return _store[game_id].model_copy(deep=True)
    except KeyError as exc:
        raise KeyError(f"Game {game_id} not found") from exc


def save_state(state: GameState) -> None:
    """Перезаписать состояние под тем же `game_id`."""
    _store[state.game_id] = state.model_copy(deep=True)


def delete_state(game_id: UUID) -> None:
    _store.pop(game_id, None)

# ---------------------------------------------------------------------------
# Staff helpers
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Low‑level util: set exact N developers of given level
# ---------------------------------------------------------------------------

def _set_dev_count(
    staff: List[Specialist],
    level: Level,
    count: int,
) -> List[Specialist]:
    """Вернуть новый список `staff` с ровно `count` Dev‑сотрудниками `level`."""
    # Удаляем всех Dev этого уровня
    filtered = [s for s in staff if not (s.role == SpecialistRole.DEV and s.level == level)]
    # Добавляем нужное количество
    for _ in range(count):
        filtered.append(Specialist(role=SpecialistRole.DEV, level=level))
    return filtered

# ---------------------------------------------------------------------------

def update_staff(
    game_id: UUID,
    *,
    juniors: int | None = None,
    middles: int | None = None,
    seniors: int | None = None,
    c_levels: List[str] | None = None,
) -> GameState:
    """Обновить численность команды и вернуть актуальный `GameState`."""
    state = load_state(game_id)

    if juniors is not None:
        state.staff = _set_dev_count(state.staff, Level.JUNIOR, juniors)
    if middles is not None:
        state.staff = _set_dev_count(state.staff, Level.MIDDLE, middles)
    if seniors is not None:
        state.staff = _set_dev_count(state.staff, Level.SENIOR, seniors)

    if c_levels is not None:
        keep = {role.strip().upper() for role in c_levels}
        state.staff = [
            s
            for s in state.staff
            if not (
                s.role in {SpecialistRole.CEO, SpecialistRole.CTO, SpecialistRole.CMO}
                and s.role.value not in keep
            )
        ]
        for role_name in keep:
            role_enum = SpecialistRole(role_name)
            if not any(s.role == role_enum for s in state.staff):
                state.staff.append(Specialist(role=role_enum, modifier=1))

    save_state(state)
    return state

# ---------------------------------------------------------------------------
# History helper
# ---------------------------------------------------------------------------


def push_history(
    game_id: UUID,
    role: str,
    content: str,
) -> None:
    """Добавить запись в историю в виде словаря {'role': role, 'content': content} и сохранить состояние."""
    state = load_state(game_id)
    state.history.append({'role': role, 'content': content})
    save_state(state)

def get_history(
    game_id: UUID,
) -> List[Dict[str, str]]:
    """Получить историю хода в виде списка словарей {'role': role, 'content': content}."""
    state = load_state(game_id)
    if len(state.history) > 20:
        return state.history[:-20]
    return state.history

