const FIELDS = [
  { key: 'supplier', label: 'Supplier' },
  { key: 'product', label: 'Product' },
  { key: 'quantity', label: 'Quantity' },
  { key: 'price', label: 'Price' },
  { key: 'deliveryDate', label: 'Delivery Date' },
  { key: 'paymentTerms', label: 'Payment Terms' },
];

function ResultsPanel({ contract }) {
  if (!contract) {
    return (
      <section className="results-card results-empty">
        <h2>Extracted Details</h2>
        <p>Upload a contract to see extracted fields and summary here.</p>
      </section>
    );
  }

  return (
    <section className="results-card">
      <div className="results-header">
        <h2>Extracted Details</h2>
        <span className="file-badge">{contract.fileName}</span>
      </div>

      <div className="fields-grid">
        {FIELDS.map(({ key, label }) => (
          <div key={key} className="field-item">
            <span className="field-label">{label}</span>
            <span className="field-value">{contract[key] || '—'}</span>
          </div>
        ))}
      </div>

      <div className="summary-block">
        <span className="field-label">Summary</span>
        <p className="summary-text">{contract.summary || 'No summary generated.'}</p>
      </div>
    </section>
  );
}

export default ResultsPanel;
