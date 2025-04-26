"""src/main.py
~~~~~~~~~~~~~~~~
Точка входа FastAPI‑приложения **TechTrek Web ML‑API**.

Запуск:
    uvicorn src.main:app --reload --port 8000
"""
from __future__ import annotations

import logging
from typing import Any

from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from src.services import llm_service
from src.routers import game

# ---------------------------------------------------------------------------
# Логирование
# ---------------------------------------------------------------------------
logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] %(levelname)s %(name)s: %(message)s",
)
logger = logging.getLogger("techtrek.api")

# ---------------------------------------------------------------------------
# Приложение
# ---------------------------------------------------------------------------
app = FastAPI(
    title="TechTrek Web — ML API",
    version="0.1.0",
    description="Backend‑сервис для настольно‑ролевой игры‑симулятора \"TechTrek\".",
)
llm_service.warmup()
# CORS — для локальной разработки разрешаем всё
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ---------------------------------------------------------------------------
# Роуты
# ---------------------------------------------------------------------------
app.include_router(game.router)


# ---------------------------------------------------------------------------
# Глобальные обработчики
# ---------------------------------------------------------------------------
@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    logger.exception("Unhandled error: %s", exc)
    return JSONResponse(
        status_code=500,
        content={"detail": "Internal Server Error"},
    )


# ---------------------------------------------------------------------------
# Health‑check
# ---------------------------------------------------------------------------
@app.get("/health", tags=["meta"])
async def health() -> dict[str, str]:
    return {"status": "ok"}
