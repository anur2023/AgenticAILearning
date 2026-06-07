import re

import spacy
from transformers import pipeline

nlp = spacy.load("en_core_web_sm")

risk_classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
sentiment_analyzer = pipeline(
    "sentiment-analysis",
    model="distilbert-base-uncased-finetuned-sst-2-english",
)

RISK_LABELS = [
    "weather disaster",
    "political sanction",
    "logistics disruption",
    "price surge",
    "supply shortage",
    "no risk",
]

COMMODITY_FALLBACK = {
    "paddy": "rice",
    "rice": "rice",
    "wheat": "wheat",
    "groundnut": "peanut oil",
    "guar": "guar gum",
    "maize": "corn",
    "crude": "crude oil",
    "soybean": "soybean",
    "palm": "palm oil",
    "sugar": "sugar",
    "cotton": "cotton",
    "coffee": "coffee",
    "copper": "copper",
    "fertilizer": "fertilizer",
}


def preprocess_text(text: str) -> str:
    text = text.strip()
    text = re.sub(r"<[^>]+>", "", text)
    text = re.sub(r"http\S+|www\S+", "", text)
    text = re.sub(r"\s+", " ", text)
    text = re.sub(r"[^a-zA-Z0-9\s.,!?\'\"%-]", "", text)

    if len(text) > 512:
        text = text[:512]

    return text.strip()


def extract_entities(text: str) -> dict:
    doc = nlp(text)

    countries = []
    commodities = []
    organizations = []
    dates = []

    for ent in doc.ents:
        if ent.label_ == "GPE":
            countries.append({"name": ent.text, "start": ent.start_char, "end": ent.end_char})
        elif ent.label_ == "PRODUCT":
            commodities.append({"name": ent.text, "start": ent.start_char, "end": ent.end_char})
        elif ent.label_ == "ORG":
            organizations.append({"name": ent.text, "start": ent.start_char, "end": ent.end_char})
        elif ent.label_ == "DATE":
            dates.append({"name": ent.text, "start": ent.start_char, "end": ent.end_char})

    for token in doc:
        word = token.text.lower()
        if word in COMMODITY_FALLBACK:
            canonical = COMMODITY_FALLBACK[word]
            already_found = any(c["name"].lower() == canonical.lower() for c in commodities)
            if not already_found:
                commodities.append(
                    {
                        "name": canonical,
                        "start": token.idx,
                        "end": token.idx + len(token.text),
                    }
                )

    return {
        "countries": countries,
        "commodities": commodities,
        "organizations": organizations,
        "dates": dates,
    }


def classify_risk(text: str) -> dict:
    result = risk_classifier(text, candidate_labels=RISK_LABELS)

    top_label = result["labels"][0]
    top_score = round(result["scores"][0], 4)

    if top_score > 0.75:
        severity = "HIGH"
    elif top_score > 0.50:
        severity = "MEDIUM"
    else:
        severity = "LOW"

    all_scores = {
        label: round(score, 4) for label, score in zip(result["labels"], result["scores"])
    }

    return {
        "risk_type": top_label,
        "risk_score": top_score,
        "severity": severity,
        "all_scores": all_scores,
    }


def analyze_sentiment(text: str) -> dict:
    result = sentiment_analyzer(text[:512])
    return {
        "sentiment": result[0]["label"],
        "sentiment_score": round(result[0]["score"], 4),
    }


def analyze_news(raw_text: str) -> dict:
    cleaned_text = preprocess_text(raw_text)

    if len(cleaned_text) < 20:
        return {"error": "Text is too short. Please provide at least 20 characters."}

    entities = extract_entities(cleaned_text)
    risk_result = classify_risk(cleaned_text)
    sentiment_result = analyze_sentiment(cleaned_text)

    country = entities["countries"][0]["name"] if entities["countries"] else "Not found"
    commodity = entities["commodities"][0]["name"] if entities["commodities"] else "Not found"

    if country != "Not found" and commodity != "Not found":
        summary = (
            f"{risk_result['severity']} {risk_result['risk_type']} "
            f"detected for {commodity} in {country}"
        )
    elif commodity != "Not found":
        summary = f"{risk_result['severity']} {risk_result['risk_type']} detected for {commodity}"
    elif country != "Not found":
        summary = f"{risk_result['severity']} {risk_result['risk_type']} detected in {country}"
    else:
        summary = f"{risk_result['severity']} {risk_result['risk_type']} detected"

    return {
        "cleaned_text": cleaned_text,
        "entities": entities,
        "risk_type": risk_result["risk_type"],
        "risk_score": risk_result["risk_score"],
        "severity": risk_result["severity"],
        "all_scores": risk_result["all_scores"],
        "sentiment": sentiment_result["sentiment"],
        "sentiment_score": sentiment_result["sentiment_score"],
        "summary": summary,
    }
