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

# import torch
# from transformers import pipeline, Pipeline

import math
from openai import OpenAI
import os

from src.services import state_store
from src.models import SYSTEM_PROMPT_BASE, EvaluateDecisionRequest, GameState, CrisisResponse

logger = logging.getLogger("services.llm")

# ---------------------------------------------------------------------------
# LLM initialisation (lazy singleton)
# ---------------------------------------------------------------------------

_API_URL = "https://openrouter.ai/api/v1"
_MODEL_NAME = "qwen/qwen-2.5-7b-instruct:free"
# api_key = "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"
api_key = "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"

@lru_cache(maxsize=1)
def _get_client() -> OpenAI:
    if not api_key:
        raise RuntimeError("OPENROUTER_API_KEY is not set")
    return OpenAI(base_url=_API_URL, api_key=api_key)

def warmup() -> None:
    """Проверить наличие API-ключа."""
    _get_client()


# ---------------------------------------------------------------------------
# Internal helpers
# ---------------------------------------------------------------------------


def _chat(messages: List[dict], max_tokens: int = 1, temperature: float = 0.0, logprobs: bool = True) -> dict:
    client = _get_client()
    response = client.chat.completions.create(
        model=_MODEL_NAME,
        messages=messages,
        max_tokens=max_tokens,
        temperature=temperature,
        logprobs=True,
        top_logprobs=20
    )
    return response


def _yes_probability(prompt: str) -> float:
    data = _chat([
        {"role": "system", "content": SYSTEM_PROMPT_BASE},
        {"role": "user",   "content": prompt},
    ])
    choice = data.choices[0]
    lp     = choice.logprobs

    if lp and lp.content:
        first_info = lp.content[0]
        probs = { first_info.token.strip(): math.exp(first_info.logprob) }

        for alt in first_info.top_logprobs:
            probs[alt.token.strip()] = math.exp(alt.logprob)

        yes = sum(probs.get(t, 0.0) for t in ("Yes", "yes", "Да", "да"))
        no  = sum(probs.get(t, 0.0) for t in ("No",  "no",  "Нет", "нет"))
        
        print(yes, no)
        if yes == 0.0:
            yes = 1.0 - no

        return yes

    content = choice.message.content.strip().lower()
    return 1.0 if content.startswith(("yes", "да")) else 0.0



# ---------------------------------------------------------------------------
# Public API
# ---------------------------------------------------------------------------

def evaluate_decision(req: EvaluateDecisionRequest) -> float:
    """Вероятность того, что решение удовлетворяет **всем** критериям."""
    resources_str = (
        f"$={req.money}, TECH={req.tech}, PROD={req.product}, MOT={req.motivation} | "
        f"J={req.juniors}, M={req.middles}, S={req.seniors}, C={','.join(req.c_levels or [])}"
    )
    history = state_store.get_history(req.game_id)
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
        f"Кризис: {history[-1]}"
        f"Решение игрока: {req.decision}\n\n"
        "Ответ: "
    )

    score = _yes_probability(prompt)
    if score != 0:
        state_store.push_history(req.game_id, "User", req.decision)
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
    history = state_store.get_history(state.game_id)
    res = state.resources
    summary = (
        f"$={res.money}, TECH={res.tech}, PROD={res.product}, MOT={res.motivation} | "
        f"{_staff_summary(state)}"
    )

    prompt = (
        f"История чата: {history}"
        f"Ты — GM игры о технологическом стартапе {state.startup_name}. "
        f"Сфера: {state.sphere}. {state.mission} "
        f"Стадия: {state.stage}. "
        f"\n\nКонтекст стартапа: {summary}"
        "Сгенерируй одну кризисную ситуацию или возможность (2–3 предложения).\n\n"
        "Описание:"
    )

    data = _chat([
        {"role": "system", "content": SYSTEM_PROMPT_BASE},
        {"role": "user", "content": prompt},
    ], max_tokens=200, temperature=0.9, logprobs=False)
    description = data.choices[0].message.content.split("Описание:")[-1].strip()

    return CrisisResponse(
        title="",
        description=description,
        danger_level=random.randint(1, 3),
        recommended_roles=[],
        forbidden_roles=[],
        resource_targets=[],
    )

import json, re

def generate_missions(sphere: str) -> dict[int, str]:
    """Сгенерировать три миссии для стартапа в указанной сфере.
    Возвращает словарь с ключами 1,2,3 и описаниями миссий.
    Использует строгую инструкцию для гарантированного JSON без инструментов.
    """

    prompt = (
        f"Сфера: {sphere} "
        "Придумай три уникальные миссии для стартапа в указанной сфере. "
        "ОТВЕТ: строго JSON-объект, где ключи — строки \"1\", \"2\", \"3\" и значения — описания миссий. "
        "Без дополнительного текста, только валидный JSON."
        )
    # Запрос к модели
    response = _chat(
        [
            {"role": "system", "content": SYSTEM_PROMPT_BASE},
            {"role": "user",   "content": prompt},
        ],
        max_tokens=512,
        temperature=0.8,
        logprobs=False
    )
    content = response.choices[0].message.content.strip()
    try:
        missions = json.loads(content)
        return {int(k): v for k, v in missions.items()}
    except json.JSONDecodeError:
        # Логируем некорректный JSON
        logger.error("Expected JSON but got: %s", content)

        # Фоллбек: берём первые предложения из каждого пункта
        result: dict[int, str] = {}
        # Ищем строки вида "1. ...", "2. ..." и т.п.
        for match in re.finditer(r'(\d+)\.\s*([^\.]+\.?)', content):
            num = int(match.group(1))
            sentence = match.group(2).strip()
            # Если предложение не заканчивается точкой — добавляем её
            if not sentence.endswith('.'):
                sentence += '.'
            result[num] = sentence
        return result

