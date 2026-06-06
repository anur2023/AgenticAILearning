import ast
import csv
from pathlib import Path

import joblib
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB

from preprocess import preprocess_email

BASE_DIR = Path(__file__).resolve().parent
PROJECT_DIR = BASE_DIR.parent
MODEL_DIR = BASE_DIR / "models"
DATA_FILE = PROJECT_DIR / "email_keywords_extracted.csv"

PRIORITY_ORDER = ["Critical", "High", "Medium", "Low"]


def build_processed_text(row):
    tokens = row.get("lemmatized_tokens", "")
    if tokens:
        try:
            parsed = ast.literal_eval(tokens)
            if parsed:
                return " ".join(parsed)
        except (ValueError, SyntaxError):
            pass
    return preprocess_email(row["email_text"])


def load_dataset():
    rows = []
    with DATA_FILE.open(encoding="utf-8", newline="") as handle:
        reader = csv.DictReader(handle)
        for row in reader:
            rows.append(
                {
                    "email_text": row["email_text"],
                    "priority": row["priority"],
                    "processed_text": build_processed_text(row),
                }
            )
    return rows


def main():
    MODEL_DIR.mkdir(parents=True, exist_ok=True)

    rows = load_dataset()
    texts = [row["processed_text"] for row in rows]
    labels = [row["priority"] for row in rows]

    indices = list(range(len(rows)))
    train_idx, test_idx = train_test_split(
        indices, test_size=0.2, random_state=42, stratify=labels
    )

    train_texts = [rows[i]["processed_text"] for i in train_idx]
    test_texts = [rows[i]["processed_text"] for i in test_idx]
    y_train = [rows[i]["priority"] for i in train_idx]
    y_test = [rows[i]["priority"] for i in test_idx]

    vectorizer = TfidfVectorizer(max_features=5000)
    X_train = vectorizer.fit_transform(train_texts)
    X_test = vectorizer.transform(test_texts)

    model = MultinomialNB()
    model.fit(X_train, y_train)

    accuracy = model.score(X_test, y_test)
    print(f"Priority model trained with test accuracy: {accuracy:.4f}")

    priorities = [p for p in PRIORITY_ORDER if p in set(labels)]

    joblib.dump(model, MODEL_DIR / "priority_naive_bayes_model.pkl")
    joblib.dump(vectorizer, MODEL_DIR / "priority_tfidf_vectorizer.pkl")
    joblib.dump(priorities, MODEL_DIR / "priorities.pkl")
    print(f"Saved priority model artifacts to {MODEL_DIR}")
    print(f"Priorities: {priorities}")


if __name__ == "__main__":
    main()
