# TechTrek Web — ML API

> **Dev base URL:** `http://localhost:8000`

## 1. Генерация миссии

## 2. Создание новой партии
`POST /game/new`
```jsonc
{
  "sphere": "Dota",
  "mission": "Победить",
  "startup_name": "Navi",
  "money": 100000,
  "technicReadiness": 0,
  "productReadiness": 0,
  "motivation": 100
}
```
<small>*Все поля опциональны кроме `sphere` и `mission` — если пропущены, берутся дефолты.*</small>

**200 OK** — полный `GameState`:
```jsonc
{
  "game_id": "22eca5ae-332a-400e-9a8f-6a34b10ddc7f",
  "stage": "pre_mvp",
  "sphere": "Dota",
  "mission": "Победить",
  "startup_name": "Navi",
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
{ 
  "game_id": "<uuid>",
  "money": 100000,
  "technicReadiness": 0,
  "productReadiness": 0,
  "motivation": 100,
  "months_passed": 0,
  "juniors": 0,
  "middles": 0,
  "seniors": 3,
  "c_levels": ["CTO"],
  }
```
**200 OK**
```jsonc
{
  "title": "", -> тут ничего не генерится 
  "description": "На проде обнаружена утечка…",
  "danger_level": 3,
  "recommended_roles": [],
  "forbidden_roles": [],
  "resource_targets": []
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
  "months_passed": 0,
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
  "text_to_player": "Предварительная оценка сохранена.",
  "quality_score": 0.0001136747
}
```
`quality_score` — вероятность того, что ответ LLM будет **Yes** (0 < p ≤ 1).

---

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

---

## 8. Типовой ход
1. `POST /game/new` → получаем `game_id`.
2. `POST /game/generate_crisis` — текст ситуации.
3. Игрок вводит решение → `evaluate_decision` возвращает `quality_score`.
4. Фронт бросает 2d6 (+ моды) → `dice_total`, `zone`.
5. `POST /game/resolve` — сервер отдаёт новый `GameState`.
6. UI показывает изменения, лог → следующий ход.






1. Имею память чатов, режем по 6 ласт [{'role': role, 'content': content}]
2. Нужен метод для генерации 3х миссий -> json {1: 'first mission', 2: 'second mission', 3: 'third mission'}
3. Нужен метод для обновления истории, нужно добавить историю в evaluate_desicion
4. Добавить сервисный метод для возвращения истории по id
5. Обновить evaluate_desicion так чтобы четко выделялся кризис