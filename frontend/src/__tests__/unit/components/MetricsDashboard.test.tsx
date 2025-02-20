import { render, screen } from '@testing-library/react';
import { MetricsDashboard } from '../../../components/MetricsDashboard';

describe('MetricsDashboard', () => {
  it('renders all metric cards with correct data', () => {
    const metrics = [
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

    render(<MetricsDashboard metrics={metrics} />);

    // Verify all metrics are rendered
    expect(screen.getByText('Total Subscribers')).toBeInTheDocument();
    expect(screen.getByText('24,892')).toBeInTheDocument();
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('68.7%')).toBeInTheDocument();
    expect(screen.getByText('Click Rate')).toBeInTheDocument();
    expect(screen.getByText('42.3%')).toBeInTheDocument();
    expect(screen.getByText('Bounce Rate')).toBeInTheDocument();
    expect(screen.getByText('2.4%')).toBeInTheDocument();

    // Verify trend indicators
    const positiveChanges = screen.getAllByTestId('trend-icon-up');
    const negativeChanges = screen.getAllByTestId('trend-icon-down');
    expect(positiveChanges).toHaveLength(2); // Subscribers and Open Rate
    expect(negativeChanges).toHaveLength(2); // Click Rate and Bounce Rate
  });
});
