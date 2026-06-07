function formatDate(isoString) {
  if (!isoString) return '';
  return new Date(isoString).toLocaleString();
}

function severityClass(severity) {
  if (severity === 'HIGH') return 'badge-high';
  if (severity === 'MEDIUM') return 'badge-medium';
  return 'badge-low';
}

function HistoryPanel({ history, selectedId, onSelect, onDelete, loading }) {
  return (
    <section className="panel history-panel">
      <div className="panel-header">
        <h2>Recent Analyses</h2>
        <p>Last 3 analyses are stored locally in JSON.</p>
      </div>

      {loading ? (
        <p className="loading-state">Loading history...</p>
      ) : history.length === 0 ? (
        <p className="empty-state">No analyses yet.</p>
      ) : (
        <div className="history-list">
          {history.map((item) => (
            <article
              key={item.id}
              className={`history-item ${selectedId === item.id ? 'history-item-selected' : ''}`}
            >
              <button
                type="button"
                className="history-select"
                onClick={() => onSelect(item)}
              >
                <div className="history-top">
                  <span className={`badge badge-sm ${severityClass(item.severity)}`}>
                    {item.severity}
                  </span>
                  <span className="history-date">{formatDate(item.createdAt)}</span>
                </div>
                <p className="history-summary">{item.summary}</p>
                <p className="history-preview">{item.rawText}</p>
              </button>
              <button
                type="button"
                className="btn-delete"
                onClick={() => onDelete(item.id)}
                aria-label="Delete analysis"
              >
                ×
              </button>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default HistoryPanel;
