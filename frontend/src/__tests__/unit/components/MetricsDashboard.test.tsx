import { render, screen } from '@testing-library/react';
import { MetricsDashboard } from '../../../components/MetricsDashboard/index';
import { MetricsDashboardProps } from '../../../components/MetricsDashboard/types';

describe('MetricsDashboard', () => {
  const mockMetrics: MetricsDashboardProps['metrics'] = [
    {
      title: 'Total Subscribers',
      value: '24,892',
      change: 12.5,
      icon: 'subscribers' as const,
    },
    {
      title: 'Open Rate',
      value: '68.7%',
      change: 3.2,
      icon: 'open-rate' as const,
    },
    {
      title: 'Click Rate',
      value: '42.3%',
      change: -1.8,
      icon: 'click-rate' as const,
    },
    {
      title: 'Bounce Rate',
      value: '2.4%',
      change: -0.5,
      icon: 'bounce-rate' as const,
    },
  ];

  it('renders all metric cards with correct data', () => {
    render(<MetricsDashboard metrics={mockMetrics} />);

    // Verify metrics cards are rendered
    expect(screen.getByText('Total Subscribers')).toBeInTheDocument();
    expect(screen.getByText('24,892')).toBeInTheDocument();
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('68.7%')).toBeInTheDocument();
    expect(screen.getByText('Click Rate')).toBeInTheDocument();
    expect(screen.getByText('42.3%')).toBeInTheDocument();
    expect(screen.getByText('Bounce Rate')).toBeInTheDocument();
    expect(screen.getByText('2.4%')).toBeInTheDocument();
  });

  it('renders in a grid layout with correct column sizes', () => {
    const { container } = render(<MetricsDashboard metrics={mockMetrics} />);

    // Check metric cards grid
    const metricCards = container.querySelectorAll('.col-sm-6.col-lg-3');
    expect(metricCards).toHaveLength(4);
  });
});
