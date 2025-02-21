import { render, screen } from '@testing-library/react';
import { MetricCard } from '../../../components/MetricCard/index';
import { MetricCardProps } from '../../../components/MetricCard/types';

describe('MetricCard', () => {
  const defaultProps: MetricCardProps = {
    title: 'Open Rate',
    value: '68.7%',
    icon: 'open-rate' as const,
    change: 3.2
  };

  it('renders with correct title and value', () => {
    render(<MetricCard {...defaultProps} />);
    
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('68.7%')).toBeInTheDocument();
  });

  it('renders with the correct icon', () => {
    const { container } = render(<MetricCard {...defaultProps} />);
    expect(container.querySelector('.icon-wrapper')).toBeInTheDocument();
  });

  it('renders positive trend indicator correctly', () => {
    render(<MetricCard {...defaultProps} />);
    
    const trendIndicator = screen.getByText('3.2%');
    expect(trendIndicator).toBeInTheDocument();
    expect(trendIndicator.parentElement).toHaveClass('text-success', 'bg-success-subtle');
  });

  it('renders negative trend indicator correctly', () => {
    render(<MetricCard {...defaultProps} change={-2.5} />);
    
    const trendIndicator = screen.getByText('2.5%');
    expect(trendIndicator).toBeInTheDocument();
    expect(trendIndicator.parentElement).toHaveClass('text-danger', 'bg-danger-subtle');
  });

  it('does not render trend indicator when change is undefined', () => {
    const { change, ...propsWithoutChange } = defaultProps;
    render(<MetricCard {...propsWithoutChange} />);
    
    expect(screen.queryByText('%')).not.toBeInTheDocument();
  });
});
