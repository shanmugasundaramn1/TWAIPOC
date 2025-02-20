import React from 'react';
import { MetricsDashboardProps } from './types';
import { MetricCard } from '../MetricCard';

export const MetricsDashboard: React.FC<MetricsDashboardProps> = ({ metrics }) => {
  return (
    <div className="row g-4">
      {metrics.map((metric, index) => (
        <div key={`${metric.title}-${index}`} className="col-sm-6 col-lg-3">
          <MetricCard
            title={metric.title}
            value={metric.value}
            change={metric.change}
            icon={metric.icon}
          />
        </div>
      ))}
    </div>
  );
};
