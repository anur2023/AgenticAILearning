import EmailCard from './EmailCard';

function CategoryColumn({ category, emails, color, highlighted, onDelete }) {
  return (
    <article
      className={`category-column ${highlighted ? 'highlighted' : ''}`}
      style={{ '--accent': color }}
    >
      <header className="column-header">
        <div>
          <h2>{category}</h2>
          <p>{emails.length} email{emails.length === 1 ? '' : 's'}</p>
        </div>
        <span className="category-dot" />
      </header>

      <div className="column-body">
        {emails.length === 0 ? (
          <div className="empty-state">No emails routed here yet.</div>
        ) : (
          emails.map((email) => (
            <EmailCard key={email.id} email={email} onDelete={onDelete} />
          ))
        )}
      </div>
    </article>
  );
}

export default CategoryColumn;
