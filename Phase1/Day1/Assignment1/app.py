from flask import Flask, request, jsonify
from flask_cors import CORS
from google import genai

app = Flask(__name__)
CORS(app)

# --- Put your Gemini API key here ---
API_KEY = "AIzaSyDM3LvPcoCQjAyuTgw0rtrJG3vooRQC9x0"

client = genai.Client(api_key=API_KEY)

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json()
    user_message = data.get("message", "").strip()

    if not user_message:
        return jsonify({"error": "No message provided"}), 400

    try:
        response = client.models.generate_content(
            model="gemini-2.5-flash",
            contents=user_message,
        )
        return jsonify({"reply": response.text})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/")
def index():
    return "Gemini Backend is running!"

if __name__ == "__main__":
    app.run(debug=True, port=5000)