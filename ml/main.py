from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import pipeline, AutoModelForCausalLM, AutoTokenizer
import torch
import torch.nn.functional as F

app = FastAPI(title="Tech Startup Adventure API")

class GenerateCompaniesRequest(BaseModel):
    category: str
    options_count: int = 3
    additional_context: str = None

class GenerateStartupDataRequest(BaseModel):
    selected_company: str
    startup_name: str
    game_stage: str = "От старта к MVP"
    additional_context: str = None

class EvaluateDecisionRequest(BaseModel):
    decision: str
    additional_context: str = None

class GameIntroductionRequest(BaseModel):
    selected_category: str = None
    additional_context: str = None

generator = pipeline("text-generation", model="Qwen2.5-1.5B-Instruct")

@app.post("/generate_companies")
async def generate_companies(request: GenerateCompaniesRequest):
    prompt = (
        f"Ты выступаешь в роли GM (игрового мастера) в игре \"Приключение технологического стартапа\". "
        f"На основе выбранной категории стартапа: \"{request.category}\", сгенерируй {request.options_count} "
        "уникальных идеи компаний. Для каждой идеи предоставь краткое описание, включающее миссию компании, "
        "основную идею и уникальное торговое предложение. Ответ оформляй в виде списка, где каждая строка содержит "
        "номер варианта, краткое название (если возможно) и описание компании. Соблюдай стиль игры – креативный, "
        "динамичный и ориентированный на инновации."
    )
    if request.additional_context:
        prompt += " Дополнительные указания: " + request.additional_context

    try:
        generated = generator(prompt, max_length=300, do_sample=True, temperature=0.8)
        result = generated[0]['generated_text']
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ошибка генерации: {e}")

    return {"prompt": prompt, "result": result}

@app.post("/generate_startup_data")
async def generate_startup_data(request: GenerateStartupDataRequest):
    prompt = (
        f"Ты GM в игре \"Приключение технологического стартапа\". Игрок выбрал вариант компании: "
        f"\"{request.selected_company}\" и придумал название для стартапа: \"{request.startup_name}\". "
        "Сгенерируй вводные данные для стартапа, которые должны включать стартовый капитал, состав команды, "
        "ключевые технические и маркетинговые ресурсы, а также краткую стратегию на ближайший период. "
        "Ответ должен быть кратким, информативным и оформленным в стиле игры."
    )
    if request.additional_context:
        prompt += " Дополнительные указания: " + request.additional_context

    try:
        generated = generator(prompt, max_length=300, do_sample=True, temperature=0.8)
        result = generated[0]['generated_text']
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ошибка генерации: {e}")

    return {"prompt": prompt, "result": result}


@app.post("/evaluate_decision")
async def evaluate_decision(request: EvaluateDecisionRequest):
    prompt = (
        "Оцени следующее решение по следующим критериям, взятым из правил игры 'Приключение технологического стартапа':\n\n"
        "1. Деньги (20 баллов): Стартап должен сохранить более 50% бюджета (более $50,000) для максимального балла, "
        "либо соответствовать промежуточным значениям (20%-50% или менее 20%).\n"
        "2. Техническая готовность (25 баллов): Продукт должен быть стабильным, технически проработанным и легко масштабируемым, "
        "с минимальным количеством багов.\n"
        "3. Продуктовая готовность (25 баллов): Продукт должен идеально соответствовать рынку, иметь сильную пользовательскую базу "
        "и быть готовым к масштабированию.\n"
        "4. Мотивация (15 баллов): Команда должна демонстрировать высокий боевой дух и продуктивность на протяжении всей игры.\n"
        "5. Управление временем (15 баллов): Стартап должен завершить игру досрочно (например, за 30 месяцев) или уложиться в дедлайн (36 месяцев), "
        "а не опаздывать (например, 38 месяцев и больше).\n\n"
        f"Решение: {request.decision}\n\n"
        "Ответь одним словом: 'Yes' если решение соответствует всем вышеперечисленным критериям, или 'No' если нет."
    )
    if request.additional_context:
        prompt += " " + request.additional_context

    try:
        inputs = generator.tokenizer(prompt, return_tensors="pt")
        with torch.no_grad():
            outputs = generator.model(**inputs)
        logits = outputs.logits[:, -1, :]
        probs = torch.nn.functional.softmax(logits, dim=-1)

        yes_variants = ["Yes", "yes", "YES"]
        yes_token_ids = []
        for variant in yes_variants:
            token_ids = generator.tokenizer.encode(variant, add_special_tokens=False)
            if token_ids:
                yes_token_ids.append(token_ids[0])

        score = probs[0, yes_token_ids].sum().item()
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ошибка оценки: {e}")


    return {"prompt": prompt, "evaluation_score": score}


@app.post("/game_introduction")
async def game_introduction(request: GameIntroductionRequest):
    prompt = (
        "Ты GM в игре 'Приключение технологического стартапа'. "
        "Предоставь вводные данные для игры, которые зададут тон и помогут игрокам понять начальную ситуацию. "
        "В твоем вступлении должны присутствовать следующие элементы:\n"
        "1. Общее описание ситуации: миссия стартапа, ключевые вызовы и возможности.\n"
        "2. Перечень доступных категорий стартапов (например, технологический стартап, fintech, e-commerce, medtech и т.д.).\n"
        "3. Определение видения и миссии стартапа: краткое описание целей и задач.\n"
        "4. Первичные шаги для начала игры: основные действия, которые предстоит выполнить.\n"
    )
    if request.selected_category:
        prompt += f"Игрок выбрал категорию: {request.selected_category}. "
    
    if request.additional_context:
        prompt += "Дополнительные указания: " + request.additional_context
    
    prompt += "Ответь кратко, информативно и в игровом стиле."

    try:
        generated = generator(prompt, max_length=400, do_sample=True, temperature=0.8)
        result = generated[0]['generated_text']
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ошибка генерации: {e}")

    return {"prompt": prompt, "result": result}


# uvicorn main:app --reload
