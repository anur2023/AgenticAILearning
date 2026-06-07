const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8083/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    let message = 'Request failed';
    try {
      const body = await response.json();
      message = body.error || body.detail || message;
    } catch {
      message = await response.text() || message;
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function getAnalyses() {
  return request('/analyses');
}

export function analyzeNews(text) {
  return request('/analyze', {
    method: 'POST',
    body: JSON.stringify({ text }),
  });
}

export function deleteAnalysis(id) {
  return request(`/analyses/${id}`, {
    method: 'DELETE',
  });
}
