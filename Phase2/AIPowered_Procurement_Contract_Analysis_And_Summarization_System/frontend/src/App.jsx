import { useEffect, useState } from 'react';
import { analyzeContract, deleteContract, getContracts } from './api';
import ContractCard from './components/ContractCard';
import ContractUpload from './components/ContractUpload';
import ResultsPanel from './components/ResultsPanel';
import './index.css';

function App() {
  const [contracts, setContracts] = useState([]);
  const [selectedContract, setSelectedContract] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const loadContracts = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await getContracts();
      setContracts(data);
      if (data.length > 0 && !selectedContract) {
        setSelectedContract(data[0]);
      }
    } catch (err) {
      setError('Unable to load contracts. Make sure the backend is running on port 8082.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadContracts();
  }, []);

  const handleAnalyze = async (file) => {
    setSubmitting(true);
    setError('');
    try {
      const result = await analyzeContract(file);
      setSelectedContract(result);
      await loadContracts();
    } catch (err) {
      setError('Analysis failed. Ensure the ML service is running on port 8000.');
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteContract(id);
      if (selectedContract?.id === id) {
        setSelectedContract(null);
      }
      await loadContracts();
    } catch (err) {
      setError('Failed to delete contract.');
      console.error(err);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <div>
          <p className="eyebrow">AI-Powered Procurement Contract Analyzer</p>
          <h1>Contract Intelligence Dashboard</h1>
          <p className="subtitle">
            Upload procurement contracts to automatically extract supplier details, pricing,
            delivery schedules, payment terms, and generate executive summaries.
          </p>
        </div>
        <div className="stats-card">
          <span className="stats-label">Contracts Analyzed</span>
          <strong className="stats-value">{contracts.length}</strong>
        </div>
      </header>

      <div className="main-grid">
        <div className="left-column">
          <ContractUpload
            onSubmit={handleAnalyze}
            submitting={submitting}
            lastResult={selectedContract}
          />

          <section className="history-card">
            <h2>Analysis History</h2>
            {loading ? (
              <p className="loading-state">Loading contracts...</p>
            ) : contracts.length === 0 ? (
              <p className="empty-state">No contracts analyzed yet.</p>
            ) : (
              <div className="contract-list">
                {contracts.map((contract) => (
                  <ContractCard
                    key={contract.id}
                    contract={contract}
                    selected={selectedContract?.id === contract.id}
                    onSelect={setSelectedContract}
                    onDelete={handleDelete}
                  />
                ))}
              </div>
            )}
          </section>
        </div>

        <ResultsPanel contract={selectedContract} />
      </div>

      {error && <div className="error-banner">{error}</div>}
    </div>
  );
}

export default App;
