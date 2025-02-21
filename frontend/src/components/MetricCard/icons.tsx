import React from 'react';

export const MetricIcons = {
  subscribers: (
    <svg data-testid="metric-icon" data-icon="subscribers" viewBox="0 0 24 24" width="24" height="24">
      <path fill="currentColor" d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
    </svg>
  ),
  'open-rate': (
    <svg data-testid="metric-icon" data-icon="open-rate" viewBox="0 0 24 24" width="24" height="24">
      <path fill="currentColor" d="M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM17 11h-4v4h-2v-4H7V9h4V5h2v4h4v2z"/>
    </svg>
  ),
  'click-rate': (
    <svg data-testid="metric-icon" data-icon="click-rate" viewBox="0 0 24 24" width="24" height="24">
      <path fill="currentColor" d="M13.1,14.9l1.5,1.5c0.2,0.2,0.5,0.2,0.7,0l4.9-4.9c0.2-0.2,0.2-0.5,0-0.7l-1.5-1.5c-0.2-0.2-0.5-0.2-0.7,0 l-4.9,4.9C12.9,14.4,12.9,14.7,13.1,14.9z M10,20.4l3-3c0.2-0.2,0.2-0.5,0-0.7L4.6,8.3c-0.2-0.2-0.5-0.2-0.7,0l-1.5,1.5 c-0.2,0.2-0.2,0.5,0,0.7l8.4,8.4C11,19.1,11.3,19.1,10,20.4z"/>
    </svg>
  ),
  'bounce-rate': (
    <svg data-testid="metric-icon" data-icon="bounce-rate" viewBox="0 0 24 24" width="24" height="24">
      <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
    </svg>
  ),
};

export const TrendIcons = {
  up: (
    <svg data-testid="trend-icon-up" viewBox="0 0 24 24" width="16" height="16">
      <path fill="currentColor" d="M7 14l5-5 5 5H7z"/>
    </svg>
  ),
  down: (
    <svg data-testid="trend-icon-down" viewBox="0 0 24 24" width="16" height="16">
      <path fill="currentColor" d="M7 10l5 5 5-5H7z"/>
    </svg>
  ),
};
