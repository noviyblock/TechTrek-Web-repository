"""src/services/llm_service.py
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Обёртка над HuggingFace `pipeline` для модели **Qwen/Qwen2.5‑0.5B‑Instruct**.

Публичные функции
-----------------
* `warmup()` – форсированная загрузка модели при старте API.
* `evaluate_decision(req)` – вернуть P("Yes" | решение игрока) ∈ (0, 1].

Алгоритм оценки:
1. Склеиваем `SYSTEM_PROMPT_BASE` + сводку ресурсов/штата.
2. Добавляем текст решения.
3. Генерируем ровно **один** токен без сэмплинга (`max_new_tokens=1`).
4. Считаем softmax‑вероятность токенов "Yes" / "yes".
"""
from __future__ import annotations

import logging
from functools import lru_cache
from typing import List
import random

import torch
from transformers import pipeline, Pipeline

from src.models import SYSTEM_PROMPT_BASE, EvaluateDecisionRequest, GameState, CrisisResponse

logger = logging.getLogger("services.llm")

# ---------------------------------------------------------------------------
# LLM initialisation (lazy singleton)
# ---------------------------------------------------------------------------

_MODEL_NAME = "Qwen/Qwen2.5-0.5B-Instruct"


@lru_cache(maxsize=1)
def _get_generator() -> Pipeline:
    """Загрузить text‑generation pipeline один раз за процесс."""
    logger.info("Loading LLM: %s", _MODEL_NAME)
    return pipeline(
        "text-generation",
        model=_MODEL_NAME,
        device_map="auto",
        torch_dtype=torch.float16 if torch.cuda.is_available() else torch.float32,
    )


def warmup() -> None:
    """Форсированная инициализация LLM при старте FastAPI."""
    _get_generator()


# ---------------------------------------------------------------------------
# Internal helpers
# ---------------------------------------------------------------------------

def _yes_probability(prompt: str, pipe: Pipeline) -> float:
    tok = pipe.tokenizer
    yes_ids: List[int] = [tok.encode(v, add_special_tokens=False)[0] for v in ("Yes", "yes", "Да", "да")]
    inputs = tok(prompt, return_tensors="pt")
    with torch.no_grad():
        inputs = pipe.tokenizer(prompt, return_tensors="pt").to(pipe.model.device)
        out = pipe.model.generate(
            **inputs,
            max_new_tokens=1,
            do_sample=False,
            return_dict_in_generate=True,
            output_scores=True,          # ← фикс
        )

    logits = out.scores[0]                       # (1, vocab)
    probs  = torch.softmax(logits, dim=-1)[0]
    prob_yes = float(probs[yes_ids].sum()) if yes_ids else 0.0
    return prob_yes



# ---------------------------------------------------------------------------
# Public API
# ---------------------------------------------------------------------------

def evaluate_decision(req: EvaluateDecisionRequest) -> float:
    """Вероятность того, что решение удовлетворяет **всем** критериям."""
    pipe = _get_generator()

    resources_str = (
        f"$={req.money}, TECH={req.tech}, PROD={req.product}, MOT={req.motivation} | "
        f"J={req.juniors}, M={req.middles}, S={req.seniors}, C={','.join(req.c_levels or [])}"
    )

    prompt = (
        f"{SYSTEM_PROMPT_BASE}\n\n"
        "Оцени решение игрока по следующим **детальным** критериям (из правил игры):\n"
        "1. **Деньги** – после хода должно остаться > 50 % бюджета.\n"
        "2. **Техническая готовность** – продукт стабилен, масштабируем, почти без багов.\n"
        "3. **Продуктовая готовность** – подтверждён market‑fit и растёт пользовательская база.\n"
        "4. **Управление временем** – команда укладывается в дедлайн без задержек.\n"
        "5. **Мотивация** – мораль команды остаётся высокой.\n"
        "6. **Рациональное использование C‑level** – ключевые специалисты не злоупотребляются подряд.\n\n"
        "Ответь *одним* словом — **Yes** если решение удовлетворяет **всем** критериям, иначе **No**.\n\n"
        f"Контекст стартапа: Ресурсы: {resources_str}.\n"
        f"Решение игрока: {req.decision}\n\n"
        "Ответ: "
    )

    score = _yes_probability(prompt, pipe)
    logger.info("P(Yes)=%.3f for game %s", score, req.game_id)
    return score

# ---------------------------------------------------------------------------
# Public: generate crisis / opportunity
# ---------------------------------------------------------------------------

def _staff_summary(state: GameState) -> str:
    dev_levels = {"junior": 0, "middle": 0, "senior": 0}
    c_levels: List[str] = []
    for s in state.staff:
        if s.role.value in ("CEO", "CTO", "CMO"):
            c_levels.append(s.role.value)
        elif s.role.value == "Dev" and s.level:
            dev_levels[s.level.value] += 1
    return (
        f"J={dev_levels['junior']} M={dev_levels['middle']} S={dev_levels['senior']} | "
        f"C={','.join(c_levels) if c_levels else '-'}"
    )


def generate_crisis(state: GameState) -> CrisisResponse:
    """Сгенерировать текст кризисной ситуации и danger_level (1‑3)."""
    pipe = _get_generator()
    res = state.resources
    summary = (
        f"$={res.money}, TECH={res.tech}, PROD={res.product}, MOT={res.motivation} | "
        f"{_staff_summary(state)}"
    )

    prompt = (
        "Ты — GM игры о стартапе. Придумай кризисную ситуацию или возможность "
        "(2–3 предложения, связный текст) для стартапа на стадии "
        f"{state.stage}.\n\nКонтекст стартапа: {summary}\n\nОписание:"
    )

    description = (
        pipe(prompt, max_new_tokens=120, do_sample=True, temperature=0.9)[0]["generated_text"].split("Описание:")[-1].strip()
    )

    return CrisisResponse(
        title="",
        description=description,
        danger_level=random.randint(1, 3),
        recommended_roles=[],
        forbidden_roles=[],
        resource_targets=[],
    )