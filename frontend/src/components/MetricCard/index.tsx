import React from 'react';
import { MetricCardProps } from './types';
import { MetricIcons, TrendIcons } from './icons';
import './styles.css';

export const MetricCard: React.FC<MetricCardProps> = ({ title, value, icon }) => {
  return (
    <div className="card border-0 shadow-sm metric-card">
      <div className="card-body">
        <div className="d-flex align-items-center mb-3">
          <div className="icon-wrapper me-2">
            {MetricIcons[icon]}
          </div>
          <h3 className="card-title h6 mb-0">{title}</h3>
        </div>
        <p className="h3 mb-0">{value}</p>
      </div>
    </div>
  );
};
