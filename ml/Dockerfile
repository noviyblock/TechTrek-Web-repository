
FROM python:3.13-slim
WORKDIR /usr/src/app

COPY requirements.txt .
RUN pip install --upgrade pip \
 && pip install --no-cache-dir -r requirements.txt

COPY ./src ./src

ENV PYTHONPATH=/usr/src/app/src \
    PYTHONUNBUFFERED=1

EXPOSE 8000

CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8000"]
