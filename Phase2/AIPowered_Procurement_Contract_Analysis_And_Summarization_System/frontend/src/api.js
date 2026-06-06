const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8082/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, options);

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || 'Request failed');
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function getContracts() {
  return request('/contracts');
}

export function analyzeContract(file) {
  const formData = new FormData();
  formData.append('file', file);

  return request('/contracts/analyze', {
    method: 'POST',
    body: formData,
  });
}

export function deleteContract(id) {
  return request(`/contracts/${id}`, {
    method: 'DELETE',
  });
}
