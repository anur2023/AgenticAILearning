from pathlib import Path

import joblib

from preprocess import preprocess_email

BASE_DIR = Path(__file__).resolve().parent
MODEL_DIR = BASE_DIR / "models"

PRIORITY_RANK = {
    "Critical": 4,
    "High": 3,
    "Medium": 2,
    "Low": 1,
}


class EmailClassifier:
    def __init__(self):
        self.model = None
        self.vectorizer = None
        self.categories = []

    def load(self):
        model_path = MODEL_DIR / "naive_bayes_model.pkl"
        vectorizer_path = MODEL_DIR / "tfidf_vectorizer.pkl"
        categories_path = MODEL_DIR / "categories.pkl"

        if not model_path.exists() or not vectorizer_path.exists():
            raise FileNotFoundError(
                "Category model files not found. Run `python train_model.py` first."
            )

        self.model = joblib.load(model_path)
        self.vectorizer = joblib.load(vectorizer_path)
        if categories_path.exists():
            self.categories = joblib.load(categories_path)

    def predict(self, processed_text):
        if self.model is None or self.vectorizer is None:
            self.load()

        vector = self.vectorizer.transform([processed_text])
        category = self.model.predict(vector)[0]
        probabilities = self.model.predict_proba(vector)[0]
        confidence = float(max(probabilities))

        return category, round(confidence, 4)


class EmailPriorityClassifier:
    def __init__(self):
        self.model = None
        self.vectorizer = None
        self.priorities = []

    def load(self):
        model_path = MODEL_DIR / "priority_naive_bayes_model.pkl"
        vectorizer_path = MODEL_DIR / "priority_tfidf_vectorizer.pkl"
        priorities_path = MODEL_DIR / "priorities.pkl"

        if not model_path.exists() or not vectorizer_path.exists():
            raise FileNotFoundError(
                "Priority model files not found. Run `python train_priority_model.py` first."
            )

        self.model = joblib.load(model_path)
        self.vectorizer = joblib.load(vectorizer_path)
        if priorities_path.exists():
            self.priorities = joblib.load(priorities_path)

    def predict(self, processed_text):
        if self.model is None or self.vectorizer is None:
            self.load()

        vector = self.vectorizer.transform([processed_text])
        priority = self.model.predict(vector)[0]
        probabilities = self.model.predict_proba(vector)[0]
        confidence = float(max(probabilities))

        return priority, round(confidence, 4)


classifier = EmailClassifier()
priority_classifier = EmailPriorityClassifier()


def predict_email(email_text):
    processed = preprocess_email(email_text)

    category, category_confidence = classifier.predict(processed)
    priority, priority_confidence = priority_classifier.predict(processed)

    return {
        "category": category,
        "confidence": category_confidence,
        "priority": priority,
        "priority_confidence": priority_confidence,
        "processed_text": processed,
    }
