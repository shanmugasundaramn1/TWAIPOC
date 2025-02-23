import React from 'react';
import { DataEnrichmentLossProps } from './types';
import './styles.css';

const COLORS = ['bg-danger', 'bg-warning', 'bg-orange', 'bg-brown', 'bg-purple', 'bg-blue', 'bg-green', 'bg-teal'];

export const DataEnrichmentLoss: React.FC<DataEnrichmentLossProps> = ({ items = [], isLoading = false, error = null }) => {
  if (error) {
    return (
      <div className="card border-0 shadow-sm loss-card">
        <div className="loss-container">
          <h3 className="card-title h6 mb-4">Data Enrichment Loss</h3>
          <div className="text-danger">Error Loading Enrichment Failure Widget</div>
        </div>
      </div>
    );
  }

  if (!isLoading && items.length === 0) {
    return (
      <div className="card border-0 shadow-sm loss-card">
        <div className="loss-container">
          <h3 className="card-title h6 mb-4">Data Enrichment Loss</h3>
          <div className="text-muted">No Enrichment Failures</div>
        </div>
      </div>
    );
  }

  // Calculate max value for percentage calculation
  const maxValue = items.length > 0 ? Math.max(...items.map(item => item.value)) : 0;

  return (
    <div className="card border-0 shadow-sm loss-card">
      <div className="loss-container">
        <h3 className="card-title h6 mb-4">Data Enrichment Loss</h3>
        <div className="d-flex flex-column">
          {items.map((item, index) => (
            <div key={item.label} className="loss-item">
              <div className="d-flex justify-content-between align-items-center mb-2">
                <div className="d-flex align-items-center">
                  <div className={`color-indicator ${COLORS[index % COLORS.length]}`} />
                  <span className="loss-label">{item.label}</span>
                </div>
                <span className="loss-value">{item.value.toLocaleString()}</span>
              </div>
              <div className="progress">
                <div
                  className={`progress-bar ${COLORS[index]}`}
                  role="progressbar"
                  style={{ 
                    width: `${(item.value / maxValue) * 100}%`,
                    transition: 'width 0.6s ease'
                  }}
                  aria-valuenow={item.value}
                  aria-valuemin={0}
                  aria-valuemax={maxValue}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
