import React from 'react';
import { MetricCardProps } from './types';
import { MetricIcons, TrendIcons } from './icons';
import './styles.css';

export const MetricCard: React.FC<MetricCardProps> = ({ 
  title,
  value,
  change,
  icon
}) => {
  const isPositive = change >= 0;
  const trendIcon = isPositive ? TrendIcons.up : TrendIcons.down;
  const trendClass = isPositive ? 'text-success bg-success-subtle' : 'text-danger bg-danger-subtle';
  
  return (
    <div className="card h-100 border-0" style={{ boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
      <div className="card-body p-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3 className="card-subtitle text-secondary mb-0 fs-6 fw-normal">{title}</h3>
          <div className={`metric-icon ${icon}`}>
            {MetricIcons[icon]}
          </div>
        </div>
        
        <div className="d-flex align-items-baseline gap-2">
          <div className="h2 mb-0 fw-semibold">{value}</div>
          <div className={`${trendClass} rounded-pill px-3 py-1 d-flex align-items-center gap-1`}>
            {trendIcon}
            <span className="fs-7">{Math.abs(change)}%</span>
          </div>
        </div>
      </div>
    </div>
  );
};
