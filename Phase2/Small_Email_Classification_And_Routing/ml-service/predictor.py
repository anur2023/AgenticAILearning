from pathlib import Path

import joblib

from preprocess import preprocess_email

BASE_DIR = Path(__file__).resolve().parent
MODEL_DIR = BASE_DIR / "models"


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
                "Model files not found. Run `python train_model.py` first."
            )

        self.model = joblib.load(model_path)
        self.vectorizer = joblib.load(vectorizer_path)
        if categories_path.exists():
            self.categories = joblib.load(categories_path)

    def predict(self, email_text):
        if self.model is None or self.vectorizer is None:
            self.load()

        processed = preprocess_email(email_text)
        vector = self.vectorizer.transform([processed])
        category = self.model.predict(vector)[0]
        probabilities = self.model.predict_proba(vector)[0]
        confidence = float(max(probabilities))

        return {
            "category": category,
            "confidence": round(confidence, 4),
            "processed_text": processed,
        }


classifier = EmailClassifier()
