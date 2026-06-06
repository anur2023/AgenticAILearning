import { useEffect, useMemo, useState } from 'react';
import {
  classifyEmail,
  deleteEmail,
  getCategories,
  getGroupedEmails,
} from './api';
import CategoryColumn from './components/CategoryColumn';
import EmailComposer from './components/EmailComposer';
import './index.css';

const CATEGORY_COLORS = {
  Technical: '#3b82f6',
  Billing: '#f59e0b',
  Account: '#8b5cf6',
  'General Inquiry': '#06b6d4',
  Complaint: '#ef4444',
  Refund: '#10b981',
  Feedback: '#ec4899',
};

function App() {
  const [categories, setCategories] = useState([]);
  const [groupedEmails, setGroupedEmails] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [highlightedCategory, setHighlightedCategory] = useState('');
  const [lastPrediction, setLastPrediction] = useState(null);

  const loadDashboard = async () => {
    setLoading(true);
    setError('');
    try {
      const [categoryList, grouped] = await Promise.all([
        getCategories(),
        getGroupedEmails(),
      ]);
      setCategories(categoryList);
      setGroupedEmails(grouped);
    } catch (err) {
      setError('Unable to load dashboard. Make sure backend and ML service are running.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  const totalEmails = useMemo(
    () => Object.values(groupedEmails).reduce((sum, list) => sum + list.length, 0),
    [groupedEmails]
  );

  const handleClassify = async (emailText) => {
    setSubmitting(true);
    setError('');
    try {
      const result = await classifyEmail(emailText);
      setLastPrediction(result);
      setHighlightedCategory(result.category);
      await loadDashboard();

      setTimeout(() => setHighlightedCategory(''), 2500);
    } catch (err) {
      setError('Classification failed. Check that the ML service is running on port 8000.');
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteEmail(id);
      await loadDashboard();
    } catch (err) {
      setError('Failed to delete email.');
      console.error(err);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <div>
          <p className="eyebrow">Smart Email Classification and Routing</p>
          <h1>Mail Routing Dashboard</h1>
          <p className="subtitle">
            Write a customer email, classify it with the NLP models, and route it to the right team column sorted by priority.
          </p>
        </div>
        <div className="stats-card">
          <span className="stats-label">Total Emails</span>
          <strong className="stats-value">{totalEmails}</strong>
        </div>
      </header>

      <EmailComposer
        onSubmit={handleClassify}
        submitting={submitting}
        lastPrediction={lastPrediction}
      />

      {error && <div className="error-banner">{error}</div>}

      {loading ? (
        <div className="loading-state">Loading category board...</div>
      ) : (
        <section className="board">
          {categories.map((category) => (
            <CategoryColumn
              key={category}
              category={category}
              emails={groupedEmails[category] || []}
              color={CATEGORY_COLORS[category] || '#64748b'}
              highlighted={highlightedCategory === category}
              onDelete={handleDelete}
            />
          ))}
        </section>
      )}
    </div>
  );
}

export default App;
