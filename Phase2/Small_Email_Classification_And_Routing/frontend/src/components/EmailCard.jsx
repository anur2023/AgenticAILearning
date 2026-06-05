function EmailCard({ email, onDelete }) {
  const createdAt = email.createdAt
    ? new Date(email.createdAt).toLocaleString()
    : 'Recently added';

  return (
    <div className="email-card">
      <p className="email-text">{email.emailText}</p>
      <div className="email-meta">
        <span>{createdAt}</span>
        {email.confidence != null && (
          <span>{(email.confidence * 100).toFixed(0)}%</span>
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
