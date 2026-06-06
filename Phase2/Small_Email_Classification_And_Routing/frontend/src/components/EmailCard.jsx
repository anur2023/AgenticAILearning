const PRIORITY_CLASS = {
  Critical: 'priority-critical',
  High: 'priority-high',
  Medium: 'priority-medium',
  Low: 'priority-low',
};

function EmailCard({ email, onDelete }) {
  const createdAt = email.createdAt
    ? new Date(email.createdAt).toLocaleString()
    : 'Recently added';

  const priorityClass = PRIORITY_CLASS[email.priority] || 'priority-medium';

  return (
    <div className="email-card">
      <div className="email-card-header">
        {email.priority && (
          <span className={`priority-badge ${priorityClass}`}>{email.priority}</span>
        )}
        {email.priorityConfidence != null && (
          <span className="priority-confidence">
            {(email.priorityConfidence * 100).toFixed(0)}%
          </span>
        )}
      </div>
      <p className="email-text">{email.emailText}</p>
      <div className="email-meta">
        <span>{createdAt}</span>
        {email.confidence != null && (
          <span>{(email.confidence * 100).toFixed(0)}% category</span>
        )}
      </div>
      <div className="email-footer">
        <span className={`source-badge ${email.source}`}>{email.source}</span>
        <button type="button" onClick={() => onDelete(email.id)}>
          Remove
        </button>
      </div>
    </div>
  );
}

export default EmailCard;
