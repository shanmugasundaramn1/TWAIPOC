import React from 'react';
import { NewsletterFunnelProps } from './types';
import './styles.css';

const StepArrow: React.FC = () => (
  <div 
    data-testid="step-arrow"
    className="d-flex align-items-center px-3"
  >
    <svg 
      width="24" 
      height="24" 
      viewBox="0 0 24 24"
      className="text-secondary opacity-50"
    >
      <path 
        fill="currentColor" 
        d="M8.59 16.59L13.17 12L8.59 7.41L10 6l6 6l-6 6l-1.41-1.41z"
      />
    </svg>
  </div>
);

export const NewsletterFunnel: React.FC<NewsletterFunnelProps> = ({ steps }) => {
  return (
    <div className="card border-0 shadow-sm">
      <div className="card-body p-4">
        <h3 className="card-title h6 mb-4">Newsletter Success Funnel</h3>
        <div className="d-flex align-items-stretch gap-3">
          {steps.map((step, index) => (
            <React.Fragment key={step.label}>
              <div 
                data-testid={`funnel-step-${step.label}`}
                className={`funnel-step bg-${step.color}-subtle rounded p-4 text-center flex-grow-1`}
                style={{ minWidth: '200px' }}
              >
                <div className="mb-3 small fw-medium">
                  {step.label}
                </div>
                <div className="h3 mb-0 fw-semibold">
                  {step.value ?? 0}
                </div>
              </div>
              {index < steps.length - 1 && <StepArrow />}
            </React.Fragment>
          ))}
        </div>
      </div>
    </div>
  );
};
