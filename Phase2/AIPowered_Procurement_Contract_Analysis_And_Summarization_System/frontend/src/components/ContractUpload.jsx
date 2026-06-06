import { useRef, useState } from 'react';

function ContractUpload({ onSubmit, submitting, lastResult }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const [dragActive, setDragActive] = useState(false);
  const inputRef = useRef(null);

  const handleFile = (file) => {
    if (!file) return;
    const name = file.name.toLowerCase();
    if (!name.endsWith('.pdf') && !name.endsWith('.docx')) {
      return;
    }
    setSelectedFile(file);
  };

  const handleDrop = (event) => {
    event.preventDefault();
    setDragActive(false);
    handleFile(event.dataTransfer.files[0]);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (selectedFile) {
      onSubmit(selectedFile);
    }
  };

  return (
    <section className="upload-card">
      <div className="upload-header">
        <h2>Upload Contract</h2>
        <p>Upload a PDF or DOCX procurement contract to extract supplier, pricing, delivery, and payment details.</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div
          className={`dropzone ${dragActive ? 'dropzone-active' : ''}`}
          onDragEnter={(e) => {
            e.preventDefault();
            setDragActive(true);
          }}
          onDragOver={(e) => e.preventDefault()}
          onDragLeave={(e) => {
            e.preventDefault();
            setDragActive(false);
          }}
          onDrop={handleDrop}
          onClick={() => inputRef.current?.click()}
        >
          <input
            ref={inputRef}
            type="file"
            accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            hidden
            onChange={(e) => handleFile(e.target.files[0])}
          />
          <div className="dropzone-icon">📄</div>
          <p className="dropzone-title">
            {selectedFile ? selectedFile.name : 'Drag & drop your contract here'}
          </p>
          <p className="dropzone-hint">or click to browse — PDF or DOCX up to 20 MB</p>
        </div>

        <div className="upload-actions">
          <button type="submit" className="primary-btn" disabled={!selectedFile || submitting}>
            {submitting ? 'Analyzing contract...' : 'Extract Contract Details'}
          </button>
          {selectedFile && (
            <button
              type="button"
              className="ghost-btn"
              onClick={() => setSelectedFile(null)}
              disabled={submitting}
            >
              Clear
            </button>
          )}
        </div>
      </form>

      {lastResult && (
        <div className="latest-result">
          <span className="latest-label">Latest analysis</span>
          <strong>{lastResult.fileName}</strong>
        </div>
      )}
    </section>
  );
}

export default ContractUpload;
