import { render, screen } from '@testing-library/react';
import { NewsletterFunnel } from '../../../components/NewsletterFunnel';

describe('NewsletterFunnel', () => {
  const defaultProps = {
    steps: [
      {
        label: 'Total Targeted',
        value: '30,000',
        color: 'primary'
      },
      {
        label: 'Data Enriched',
        value: '27,500',
        color: 'success'
      },
      {
        label: 'Delivered',
        value: '24,892',
        color: 'warning'
      },
      {
        label: 'Opened',
        value: '17,100',
        color: 'info'
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
});
