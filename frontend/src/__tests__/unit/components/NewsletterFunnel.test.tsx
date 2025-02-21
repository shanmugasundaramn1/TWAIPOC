import { render, screen } from '@testing-library/react';
import { NewsletterFunnel } from '../../../components/NewsletterFunnel/index';
import { NewsletterFunnelProps } from '../../../components/NewsletterFunnel/types';

describe('NewsletterFunnel', () => {
  const defaultProps: NewsletterFunnelProps = {
    steps: [
      {
        label: 'Total Targeted',
        value: '0',
        color: 'blue'
      },
      {
        label: 'Data Enriched',
        value: '27,500',
        color: 'green'
      },
      {
        label: 'Delivered',
        value: '24,892',
        color: 'yellow'
      },
      {
        label: 'Opened',
        value: '17,100',
        color: 'purple'
      }
    ]
  };

  it('renders all funnel steps', () => {
    render(<NewsletterFunnel {...defaultProps} />);
    
    defaultProps.steps.forEach(step => {
      expect(screen.getByText(step.label)).toBeInTheDocument();
      expect(screen.getByText(step.value)).toBeInTheDocument();
    });
  });

  it('renders arrows between steps', () => {
    render(<NewsletterFunnel {...defaultProps} />);
    
    // Should have 3 arrows (number of steps - 1)
    expect(screen.getAllByTestId('step-arrow')).toHaveLength(3);
  });

  it('applies correct color classes to steps', () => {
    render(<NewsletterFunnel {...defaultProps} />);
    
    defaultProps.steps.forEach(step => {
      const stepElement = screen.getByTestId(`funnel-step-${step.label}`);
      expect(stepElement).toHaveClass(`bg-${step.color}-subtle`);
    });
  });

  it('renders the funnel title', () => {
    render(<NewsletterFunnel {...defaultProps} />);
    expect(screen.getByText('Newsletter Success Funnel')).toBeInTheDocument();
  });
});
