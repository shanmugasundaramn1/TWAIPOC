import { render, screen } from '@testing-library/react';
import { MetricCard } from '../../../components/MetricCard';

describe('MetricCard', () => {
  const defaultProps = {
    title: 'Open Rate',
    value: '68.7%',
    icon: 'open-rate' as const
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
});
