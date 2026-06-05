const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || 'Request failed');
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function getCategories() {
  return request('/categories');
}

export function getGroupedEmails() {
  return request('/emails/grouped');
}

export function classifyEmail(emailText) {
  return request('/emails/classify', {
    method: 'POST',
    body: JSON.stringify({ emailText }),
  });
}

export function deleteEmail(id) {
  return request(`/emails/${id}`, { method: 'DELETE' });
}
