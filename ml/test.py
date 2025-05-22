from openai import OpenAI

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key="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",
)

completion = client.chat.completions.create(
  model="qwen/qwen-2.5-7b-instruct:free",
  messages=[
    {
      "role": "user",
      "content": "What is the meaning of life?"
    }
  ],
  max_tokens=1,
  logprobs=True,
  top_logprobs=5
)
print(completion.choices[0])