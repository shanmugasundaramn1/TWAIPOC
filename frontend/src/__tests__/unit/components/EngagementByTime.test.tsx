import { render, screen } from '@testing-library/react';
import { EngagementByTime } from '../../../components/EngagementByTime';

jest.mock('recharts', () => ({
  ResponsiveContainer: ({ children }: any) => children,
  LineChart: ({ children }: any) => <div data-testid="line-chart">{children}</div>,
  Line: () => <div data-testid="line" />,
  XAxis: () => <div data-testid="x-axis" />,
  YAxis: () => <div data-testid="y-axis" />,
  CartesianGrid: () => <div data-testid="cartesian-grid" />,
  Tooltip: () => <div data-testid="tooltip" />
}));

describe('EngagementByTime', () => {
  const mockData = [
    { timestamp: '2024-03-01T09:00:00', value: 15000 },
    { timestamp: '2024-03-01T12:00:00', value: 17500 },
    { timestamp: '2024-03-01T15:00:00', value: 16800 }
  ];

  it('renders the component with correct title', () => {
    render(<EngagementByTime data={mockData} />);
    expect(screen.getByText('Engagement by Time')).toBeInTheDocument();
  });

  it('renders with the correct class names', () => {
    const { container } = render(<EngagementByTime data={mockData} />);
    expect(container.querySelector('.engagement-card')).toBeInTheDocument();
    expect(container.querySelector('.engagement-container')).toBeInTheDocument();
    expect(container.querySelector('.engagement-content')).toBeInTheDocument();
  });

  it('renders all chart components', () => {
    render(<EngagementByTime data={mockData} />);
    expect(screen.getByTestId('line-chart')).toBeInTheDocument();
    expect(screen.getByTestId('line')).toBeInTheDocument();
    expect(screen.getByTestId('x-axis')).toBeInTheDocument();
    expect(screen.getByTestId('y-axis')).toBeInTheDocument();
    expect(screen.getByTestId('cartesian-grid')).toBeInTheDocument();
    expect(screen.getByTestId('tooltip')).toBeInTheDocument();
  });

  it('formats time correctly', () => {
    render(<EngagementByTime data={mockData} />);
    const formatTime = (timestamp: string) => {
      const date = new Date(timestamp);
      return date.toLocaleString('en-US', { 
        hour: 'numeric',
        minute: '2-digit',
        hour12: true 
      });
    };
    
    expect(formatTime(mockData[0].timestamp)).toBe('9:00 AM');
    expect(formatTime(mockData[1].timestamp)).toBe('12:00 PM');
    expect(formatTime(mockData[2].timestamp)).toBe('3:00 PM');
  });
}); 