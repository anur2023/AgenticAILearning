import { useState } from 'react';

const PRIORITY_CLASS = {
  Critical: 'priority-critical',
  High: 'priority-high',
  Medium: 'priority-medium',
  Low: 'priority-low',
};

function EmailComposer({ onSubmit, submitting, lastPrediction }) {
  const [emailText, setEmailText] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmed = emailText.trim();
    if (!trimmed) {
      return;
    }
    await onSubmit(trimmed);
    setEmailText('');
  };

  return (
    <section className="composer-card">
      <form onSubmit={handleSubmit}>
        <label htmlFor="email-input" className="composer-label">
          New Customer Email
        </label>
        <textarea
          id="email-input"
          value={emailText}
          onChange={(event) => setEmailText(event.target.value)}
          placeholder="Type a customer email here. Example: My payment failed and money was deducted from my account."
          rows={5}
        />
        <div className="composer-actions">
          <button type="submit" disabled={submitting || !emailText.trim()}>
            {submitting ? 'Classifying...' : 'Classify and Route Email'}
          </button>
          {lastPrediction && (
            <div className="prediction-chip">
              Routed to <strong>{lastPrediction.category}</strong>
              <span>
                {(lastPrediction.confidence * 100).toFixed(1)}% category
              </span>
              {lastPrediction.priority && (
                <span className={`priority-badge ${PRIORITY_CLASS[lastPrediction.priority] || 'priority-medium'}`}>
                  {lastPrediction.priority} priority
                </span>
              )}
            </div>
          )}
        </div>
      </form>
    </section>
  );
}

export default EmailComposer;
