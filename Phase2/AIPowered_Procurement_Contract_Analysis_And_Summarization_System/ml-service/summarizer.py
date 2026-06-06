from transformers import T5ForConditionalGeneration, T5Tokenizer

_tokenizer = None
_model = None


def _load_model():
    global _tokenizer, _model
    if _tokenizer is None:
        _tokenizer = T5Tokenizer.from_pretrained("t5-small")
        _model = T5ForConditionalGeneration.from_pretrained("t5-small")
    return _tokenizer, _model


def generate_summary(text: str) -> str:
    if not text.strip():
        return ""

    tokenizer, model = _load_model()
    input_text = "summarize: " + text
    inputs = tokenizer.encode(
        input_text,
        return_tensors="pt",
        max_length=512,
        truncation=True,
    )
    summary_ids = model.generate(
        inputs,
        max_length=100,
        min_length=20,
        num_beams=4,
        early_stopping=True,
    )
    return tokenizer.decode(summary_ids[0], skip_special_tokens=True)
