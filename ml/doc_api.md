# TechTrek Web — ML API

> **Dev base URL:** `http://localhost:8000`


## 2. Создание новой партии
`POST /game/new`
```jsonc
{
  "money": 100000,
  "technicReadiness": 0,
  "productReadiness": 0,
  "motivation": 100
}
```
<small>*Все поля опциональны — если пропущены, берутся дефолты.*</small>

**200 OK** — полный `GameState`:
```jsonc
{
  "game_id": "22eca5ae-332a-400e-9a8f-6a34b10ddc7f",
  "stage": "pre_mvp",
  "resources": {
    "money": 100000,
    "technicReadiness": 0,
    "productReadiness": 0,
    "motivation": 100,
    "months_passed": 0
  },
  "staff": [],
  "inventory": [],
  "history": []
}
```

---

## 3. Генерация кризиса / возможности
`POST /game/generate_crisis`
```json
{ "game_id": "<uuid>" }
```
**200 OK**
```jsonc
{
  "title": "Критический баг в платёжном модуле",
  "description": "На проде обнаружена утечка…",
  "danger_level": 3,
  "recommended_roles": ["CTO", "Dev-senior"],
  "forbidden_roles": [],
  "resource_targets": ["money", "tech", "product", "motivation"]
}
```

Ошибки: `404` — нет такой игры · `500` — сбой LLM.

---

## 4. Предварительная оценка решения
`POST /game/evaluate_decision`
```jsonc
{
  "game_id": "22eca5ae-…",
  "money": 100000,
  "technicReadiness": 0,
  "productReadiness": 0,
  "motivation": 100,
  "juniors": 0,
  "middles": 0,
  "seniors": 3,
  "c_levels": ["CTO"],
  "decision": "Нанял 3 синьоров, нанял СТО, начали разработку по скраму"
}
```
**200 OK** — `EvaluateDecisionResult`
```jsonc
{
  "resource_delta": {},
  "applied_mods": {},
  "text_to_player": "Предварительная оценка сохранена.",
  "quality_score": 0.0001136747
}
```
`quality_score` — вероятность того, что ответ LLM будет **Yes** (0 < p ≤ 1).

---

## 5. Итог хода (решение + бросок)
`POST /game/resolve`
```jsonc
{
  "state": { … GameState … },
  "pre_roll": { … EvaluateDecisionResult … },
  "dice_total": 10,
  "zone": "success"
}
```
**200 OK** — обновлённый `GameState`.

Ошибки: `422` — диапазон dice/zone · `404` — нет игры.

---

## 6. Схема `GameState` (сокращённо)
```jsonc
{
  "game_id": "uuid",
  "stage": "pre_mvp | post_mvp | scale",
  "resources": {
    "money": 100000,
    "technicReadiness": 75,
    "productReadiness": 60,
    "motivation": 90,
    "months_passed": 3
  },
  "staff": [
    { "role": "CEO", "level": null,  "stamina": 0, "salary": 0, "modifier": 1 },
    { "role": "Dev", "level": "senior", "stamina": 0, "salary": 0, "modifier": 0 }
  ],
  "inventory": ["office", "prototype"],
  "history": ["[T1] …"]
}
```
*Поля `technicReadiness` / `productReadiness` имеют алиасы `tech` / `product` во внутренних моделях.*

---

## 7. Зоны броска 2d6
| Сумма | `zone`             |
|-------|--------------------|
| 2‑4   | `critical_fail`    |
| 5‑6   | `fail`             |
| 7‑9   | `neutral`          |
| 10‑11 | `success`          |
| 12+   | `critical_success` |

---

## 8. Типовой ход
1. `POST /game/new` → получаем `game_id`.
2. `POST /game/generate_crisis` — текст ситуации.
3. Игрок вводит решение → `evaluate_decision` возвращает `quality_score`.
4. Фронт бросает 2d6 (+ моды) → `dice_total`, `zone`.
5. `POST /game/resolve` — сервер отдаёт новый `GameState`.
6. UI показывает изменения, лог → следующий ход.

