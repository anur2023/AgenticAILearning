function severityClass(severity) {
  if (severity === 'HIGH') return 'badge-high';
  if (severity === 'MEDIUM') return 'badge-medium';
  return 'badge-low';
}

function sentimentClass(sentiment) {
  return sentiment === 'POSITIVE' ? 'sentiment-positive' : 'sentiment-negative';
}

function firstEntity(entities, key) {
  const list = entities?.[key];
  return list?.length ? list[0].name : 'Not found';
}

function ResultsPanel({ result }) {
  if (!result) {
    return (
      <section className="panel results-panel">
        <div className="panel-header">
          <h2>Risk Analysis</h2>
          <p>Results will appear here after analysis.</p>
        </div>
        <div className="results-empty">
          <div className="empty-icon">📊</div>
          <p>No analysis yet. Paste news text or try a sample article.</p>
        </div>
      </section>
    );
  }

  const entities = result.entities || {};

  return (
    <section className="panel results-panel">
      <div className="panel-header">
        <h2>Risk Analysis</h2>
        <p className="result-summary">{result.summary}</p>
      </div>

      <div className="result-badges">
        <span className={`badge ${severityClass(result.severity)}`}>{result.severity}</span>
        <span className="badge badge-risk">{result.riskType}</span>
        <span className={`badge ${sentimentClass(result.sentiment)}`}>{result.sentiment}</span>
      </div>

      <div className="metrics-grid">
        <div className="metric-card">
          <span className="metric-label">Risk Score</span>
          <strong className="metric-value">{result.riskScore?.toFixed(4)}</strong>
        </div>
        <div className="metric-card">
          <span className="metric-label">Sentiment Score</span>
          <strong className="metric-value">{result.sentimentScore?.toFixed(4)}</strong>
        </div>
        <div className="metric-card">
          <span className="metric-label">Country</span>
          <strong className="metric-value metric-text">{firstEntity(entities, 'countries')}</strong>
        </div>
        <div className="metric-card">
          <span className="metric-label">Commodity</span>
          <strong className="metric-value metric-text">{firstEntity(entities, 'commodities')}</strong>
        </div>
      </div>

      {result.allScores && (
        <div className="scores-section">
          <h3>All Risk Scores</h3>
          <div className="score-bars">
            {Object.entries(result.allScores).map(([label, score]) => (
              <div key={label} className="score-row">
                <span className="score-label">{label}</span>
                <div className="score-bar-track">
                  <div
                    className="score-bar-fill"
                    style={{ width: `${Math.min(score * 100, 100)}%` }}
                  />
                </div>
                <span className="score-value">{score.toFixed(4)}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="entities-section">
        <h3>Extracted Entities</h3>
        <div className="entity-groups">
          {['countries', 'commodities', 'organizations', 'dates'].map((key) => (
            <div key={key} className="entity-group">
              <span className="entity-group-label">{key}</span>
              <div className="entity-tags">
                {(entities[key] || []).length ? (
                  entities[key].map((item) => (
                    <span key={`${key}-${item.name}-${item.start}`} className="entity-tag">
                      {item.name}
                    </span>
                  ))
                ) : (
                  <span className="entity-empty">None detected</span>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="cleaned-text-section">
        <h3>Cleaned Text</h3>
        <p className="cleaned-text">{result.cleanedText}</p>
      </div>
    </section>
  );
}

export default ResultsPanel;
