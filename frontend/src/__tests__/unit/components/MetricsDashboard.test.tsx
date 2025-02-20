import { render, screen } from '@testing-library/react';
import { MetricsDashboard } from '../../../components/MetricsDashboard';

describe('MetricsDashboard', () => {
  const mockMetrics = [
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

  const mockEnrichmentData = [
    { label: 'Invalid Emails', value: 150 },
    { label: 'Bounced', value: 75 },
  ];

  const mockEngagementData = [
    { timestamp: '2024-03-01', value: 150 },
    { timestamp: '2024-03-02', value: 200 },
  ];

  it('renders all components with correct data', () => {
    render(
      <MetricsDashboard
        metrics={mockMetrics}
        enrichmentData={mockEnrichmentData}
        engagementData={mockEngagementData}
      />
    );

    // Verify metrics cards are rendered
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

    // Verify Data Enrichment Loss component
    expect(screen.getByText('Data Enrichment Loss')).toBeInTheDocument();
    expect(screen.getByText('Invalid Emails')).toBeInTheDocument();
    expect(screen.getByText('Bounced')).toBeInTheDocument();

    // Verify Engagement by Time component
    expect(screen.getByText('Engagement by Time')).toBeInTheDocument();
  });

  it('renders in a grid layout with correct column sizes', () => {
    const { container } = render(
      <MetricsDashboard
        metrics={mockMetrics}
        enrichmentData={mockEnrichmentData}
        engagementData={mockEngagementData}
      />
    );

    // Check metric cards grid
    const metricCards = container.querySelectorAll('.col-sm-6.col-lg-3');
    expect(metricCards).toHaveLength(4);

    // Check bottom row components grid
    const bottomRowColumns = container.querySelectorAll('.col-md-6');
    expect(bottomRowColumns).toHaveLength(2);
  });
});
