# TechTrek Web — ML API

> **Dev base URL:** `http://localhost:8000`

## 1. Генерация миссии
`POST game/generate_missions`
```jsonc
{
    "sphere": "E-commerce"
}
```
**200 OK**
```jsonc
{
    "first": "Разработать функцию лояльности клиентов, которая предложит скидки на будущие покупки на основе анализа поведения пользователя в течение первого месяца после покупки. Это повысит возврат клиентов и увеличит средний чек.",
    "second": "Интегрировать систему рекомендаций товаров на основе поведения пользователей и их исторических покупок. Это увеличит конверсию и удержание клиентов, привлекая их к повторным покупкам.",
    "third": "Провести маркетинговую кампанию в социальных сетях с использованием пользовательского контента и инфлюенсеров для увеличения узнаваемости бренда и привлечения новых клиентов из целевой аудитории."
}
```
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
  "res": {
    "money": 100000,
    "technicReadiness": 0,
    "productReadiness": 0,
    "motivation": 100,
    "months_passed": 0
  },
  "staffs": {
    "juniors": 0,
    "middles": 0,
    "seniors": 3,
    "superEmployees": ["CTO"]
  },
  "game_id": "499ababf-98eb-4d86-8095-23559e25bd4c"
}
```
**200 OK**
```jsonc
{
    "title": "",
    "description": "Ваш стартап Navi столкнулся с проблемой: ваши текущие ресурсы и временные ограничения привели к тому, что вам необходимо быстро достичь значимого прогресса в кодировании прототипа Dota-игры, чтобы привлечь внимание инвесторов. К сожалению, у вас недостаточно time (0 месяцев), что означает, что вам нужно будет что-то сделать для продвижения проекта в кратчайшие сроки. Вместе с этим возникает возможность: вы можете заключить контракт с профессиональной командой профессиональных игроков Dota, которые помогут вам протестировать и улучшить вашу игру, при условии, что вы предоставите им доступ к вашему коду и позволите им быть частью вашего сообщества разработчиков. Это может",
    "danger_level": 2,
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
    "game_id": "499ababf-98eb-4d86-8095-23559e25bd4c",
    "money": 100000,
    "technicReadiness": 0,
    "productReadiness": 0,
    "motivation": 100,
    "juniors": 0,
    "middles" : 0,
    "seniors": 3,
    "c_levels": ["CTO"],
    "decision": "Нанял 3 синьоров, нанял СТО, начали разработку по скраму"
}
```
**200 OK** — `EvaluateDecisionResult`
```jsonc
{
    "text_to_player": "Предварительная оценка сохранена.",
    "quality_score": 3.5762774786007867e-07
}
```
`quality_score` — вероятность того, что ответ LLM будет **Yes** (0 < p ≤ 1).

---

---

## 6. Схема `GameState` (сокращённо)
```jsonc
{
    "game_id": "499ababf-98eb-4d86-8095-23559e25bd4c",
    "stage": "pre_mvp",
    "sphere": "Dota",
    "mission": "Win",
    "startup_name": "Navi",
    "resources": {
        "money": 100000,
        "technicReadiness": 0,
        "productReadiness": 0,
        "motivation": 100,
        "months_passed": 0
    },
    "staff": [
        {
            "role": "CTO",
            "level": null,
            "stamina": 0,
            "salary": 0,
            "modifier": 1
        },
        {
            "role": "Dev",
            "level": "senior",
            "stamina": 0,
            "salary": 0,
            "modifier": 0
        },
        {
            "role": "Dev",
            "level": "senior",
            "stamina": 0,
            "salary": 0,
            "modifier": 0
        },
        {
            "role": "Dev",
            "level": "senior",
            "stamina": 0,
            "salary": 0,
            "modifier": 0
        }
    ],
    "inventory": [],
    "history": [
        {
            "role": "Assistant",
            "content": "Ваш стартап Navi столкнулся с проблемой: ваши текущие ресурсы и временные ограничения привели к тому, что вам необходимо быстро достичь значимого прогресса в кодировании прототипа Dota-игры, чтобы привлечь внимание инвесторов. К сожалению, у вас недостаточно time (0 месяцев), что означает, что вам нужно будет что-то сделать для продвижения проекта в кратчайшие сроки. Вместе с этим возникает возможность: вы можете заключить контракт с профессиональной командой профессиональных игроков Dota, которые помогут вам протестировать и улучшить вашу игру, при условии, что вы предоставите им доступ к вашему коду и позволите им быть частью вашего сообщества разработчиков. Это может"
        },
        {
            "role": "User",
            "content": "Нанял 3 синьоров, нанял СТО, начали разработку по скраму"
        }
    ]
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






Done. 1. Имею память чатов, режем по 6 ласт [{'role': role, 'content': content}]
Done. 2. Нужен метод для генерации 3х миссий -> json {1: 'first mission', 2: 'second mission', 3: 'third mission'}
Done. 3. Нужен метод для обновления истории, нужно добавить историю в evaluate_desicion
Done. 4. Добавить сервисный метод для возвращения истории по id
Done. 5. Обновить evaluate_desicion так чтобы четко выделялся кризис easy to done