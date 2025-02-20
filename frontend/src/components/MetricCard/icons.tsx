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
      <path fill="currentColor" d="M17 12c0 .59-.04 1.17-.12 1.74L19.5 15l-2.54 4.37c-.44.75-1.39 1.02-2.17.63l-1.9-1.1c-.51.41-1.07.74-1.69 1l-.38 2.65C10.68 23.36 9.89 24 9 24H5c-.89 0-1.68-.64-1.82-1.45l-.38-2.65c-.62-.25-1.18-.59-1.69-1l-1.9 1.1c-.78.39-1.73.12-2.17-.63L0 18h2.5c0-1.93.78-3.68 2.04-4.95l-.02-.37c0-.59.04-1.17.12-1.74L1.5 9l2.54-4.37c.44-.75 1.39-1.02 2.17-.63l1.9 1.1c.51-.41 1.07-.74 1.69-1l.38-2.65C10.32.64 11.11 0 12 0h4c.89 0 1.68.64 1.82 1.45l.38 2.65c.62.25 1.18.59 1.69 1l1.9-1.1c.78-.39 1.73-.12 2.17.63L22 6h-2.5c0 1.93-.78 3.68-2.04 4.95l.02.37z"/>
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
