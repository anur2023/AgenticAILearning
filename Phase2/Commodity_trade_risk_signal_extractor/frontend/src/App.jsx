import { useCallback, useEffect, useState } from 'react';
import { analyzeNews, deleteAnalysis, getAnalyses } from './api';
import HistoryPanel from './components/HistoryPanel';
import NewsInput from './components/NewsInput';
import ResultsPanel from './components/ResultsPanel';
import './index.css';

function App() {
  const [text, setText] = useState('');
  const [history, setHistory] = useState([]);
  const [selectedResult, setSelectedResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const loadHistory = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const data = await getAnalyses();
      setHistory(data);
    } catch (err) {
      setError('Unable to load history. Make sure the backend is running on port 8083.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadHistory();
  }, [loadHistory]);

  const handleAnalyze = async () => {
    setSubmitting(true);
    setError('');
    try {
      const result = await analyzeNews(text);
      setSelectedResult(result);
      await loadHistory();
    } catch (err) {
      setError(err.message || 'Analysis failed. Ensure the ML service is running on port 8000.');
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteAnalysis(id);
      if (selectedResult?.id === id) {
        setSelectedResult(null);
      }
      await loadHistory();
    } catch (err) {
      setError('Failed to delete analysis.');
      console.error(err);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <div>
          <p className="eyebrow">Commodity Trade Risk Signal Extractor</p>
          <h1>Trade Risk Intelligence</h1>
          <p className="subtitle">
            Paste commodity news to automatically detect country, commodity, risk type,
            severity level, and sentiment using spaCy, BART, and DistilBERT.
          </p>
        </div>
        <div className="stats-card">
          <span className="stats-label">Analyses Stored</span>
          <strong className="stats-value">{history.length}</strong>
        </div>
      </header>

      <div className="main-grid">
        <div className="left-column">
          <NewsInput
            text={text}
            onChange={setText}
            onSubmit={handleAnalyze}
            submitting={submitting}
            onSampleSelect={setText}
          />
          <HistoryPanel
            history={history}
            selectedId={selectedResult?.id}
            onSelect={setSelectedResult}
            onDelete={handleDelete}
            loading={loading}
          />
        </div>
        <ResultsPanel result={selectedResult} />
      </div>

      {error && <div className="error-banner">{error}</div>}
    </div>
  );
}

export default App;
