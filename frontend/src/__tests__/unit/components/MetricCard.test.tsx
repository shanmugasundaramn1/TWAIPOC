import { render, screen } from '@testing-library/react';
import { MetricCard } from '../../../components/MetricCard';

describe('MetricCard', () => {
  const defaultProps = {
    title: 'Total Subscribers',
    value: '24,892',
    change: 12.5,
    icon: 'subscribers',
  };

  it('renders the metric card with title and value', () => {
    render(<MetricCard {...defaultProps} />);
    
    expect(screen.getByText('Total Subscribers')).toBeInTheDocument();
    expect(screen.getByText('24,892')).toBeInTheDocument();
  });

  it('displays positive change with green color and up arrow', () => {
    render(<MetricCard {...defaultProps} />);
    
    const changeElement = screen.getByText('12.5%');
    expect(changeElement).toHaveClass('text-success');
    expect(screen.getByTestId('trend-icon-up')).toBeInTheDocument();
  });

  it('displays negative change with red color and down arrow', () => {
    render(<MetricCard {...defaultProps} change={-1.8} />);
    
    const changeElement = screen.getByText('-1.8%');
    expect(changeElement).toHaveClass('text-danger');
    expect(screen.getByTestId('trend-icon-down')).toBeInTheDocument();
  });

  it('renders the correct metric icon', () => {
    render(<MetricCard {...defaultProps} />);
    expect(screen.getByTestId('metric-icon')).toHaveAttribute('data-icon', 'subscribers');
  });
});
