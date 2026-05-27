                                                Day 1

🤖 Gemini Chat App
A simple full-stack AI chatbot built with Flask (Python backend) and Vanilla HTML/CSS/JS (frontend), powered by Google Gemini 2.5 Flash.

🛠️ Tech Stack
LayerTechnologyFrontendHTML, CSS, JavaScriptBackendPython (Flask)AI ModelGoogle Gemini 2.5 Flash (google-genai SDK)API CommunicationREST (fetch + JSON)

📚 What I Learned
1. Flask Backend Development

Setting up a REST API endpoint (/chat) using Flask
Handling POST requests and parsing JSON payloads with request.get_json()
Returning structured JSON responses with jsonify()
Enabling CORS using flask-cors so the frontend can communicate with the backend across origins

2. Google Gemini API Integration

Using the google-genai Python SDK to connect to Gemini
Sending user messages to gemini-2.5-flash and receiving AI-generated responses
Basic error handling for API failures

3. Frontend ↔ Backend Communication

Making async API calls from the browser using the Fetch API
Sending and receiving JSON between a static HTML page and a Python server
Handling loading states (typing animation) and error states gracefully in the UI

4. UI/UX with Pure CSS

Building a clean, dark-themed chat interface without any framework
Auto-resizing textarea, animated typing dots, and smooth scroll behavior
Responsive layout using Flexbox
