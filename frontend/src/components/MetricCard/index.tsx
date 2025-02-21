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
  const isPositive = change && change >= 0;
  const trendIcon = isPositive ? TrendIcons.up : TrendIcons.down;
  const trendClass = isPositive ? 'text-success bg-success-subtle' : 'text-danger bg-danger-subtle';
  
  return (
    <div className="card border-0 shadow-sm metric-card">
      <div className="card-body">
        <div className="d-flex align-items-center mb-3">
          <div className="icon-wrapper me-2">
            {MetricIcons[icon]}
          </div>
          <h3 className="card-title h6 mb-0">{title}</h3>
        </div>
        <div className="d-flex align-items-baseline gap-2">
          <p className="h3 mb-0">{value}</p>
          {change !== undefined && (
            <div className={`${trendClass} rounded-pill px-2 py-1 d-flex align-items-center gap-1`}>
              {trendIcon}
              <span className="small">{Math.abs(change)}%</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
