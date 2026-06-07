import { SAMPLE_NEWS } from '../sampleNews';

function NewsInput({ text, onChange, onSubmit, submitting, onSampleSelect }) {
  const charCount = text.length;
  const canSubmit = text.trim().length >= 20 && !submitting;

  return (
    <section className="panel input-panel">
      <div className="panel-header">
        <h2>Analyze News</h2>
        <p>Paste a commodity-related news paragraph to extract trade risk signals.</p>
      </div>

      <textarea
        className="news-textarea"
        value={text}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Paste news text here... (minimum 20 characters)"
        rows={8}
      />

      <div className="input-footer">
        <span className={`char-count ${charCount < 20 ? 'char-count-warn' : ''}`}>
          {charCount} characters {charCount < 20 ? '(min 20)' : ''}
        </span>
        <button
          type="button"
          className="btn-primary"
          onClick={onSubmit}
          disabled={!canSubmit}
        >
          {submitting ? 'Analyzing...' : 'Extract Risk Signals'}
        </button>
      </div>

      <div className="samples-section">
        <p className="samples-label">Quick demo samples</p>
        <div className="sample-buttons">
          {SAMPLE_NEWS.map((sample) => (
            <button
              key={sample.id}
              type="button"
              className="btn-sample"
              onClick={() => onSampleSelect(sample.text)}
              disabled={submitting}
            >
              {sample.label}
            </button>
          ))}
        </div>
      </div>
    </section>
  );
}

export default NewsInput;
