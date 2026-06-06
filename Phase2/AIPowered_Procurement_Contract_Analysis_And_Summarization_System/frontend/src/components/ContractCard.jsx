function ContractCard({ contract, onDelete, selected, onSelect }) {
  return (
    <article
      className={`contract-card ${selected ? 'contract-card-selected' : ''}`}
      onClick={() => onSelect(contract)}
    >
      <div className="contract-card-top">
        <strong>{contract.fileName}</strong>
        <button
          type="button"
          className="delete-btn"
          onClick={(e) => {
            e.stopPropagation();
            onDelete(contract.id);
          }}
        >
          Delete
        </button>
      </div>
      <p className="contract-meta">
        {contract.supplier || 'Unknown supplier'} · {contract.product || 'Unknown product'}
      </p>
      <p className="contract-date">{new Date(contract.createdAt).toLocaleString()}</p>
    </article>
  );
}

export default ContractCard;
