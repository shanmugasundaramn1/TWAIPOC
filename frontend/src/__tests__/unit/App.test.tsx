import { render, screen } from '@testing-library/react';
import App from '../../App';

// Mock react-datepicker
jest.mock('react-datepicker', () => {
  return function DatePicker(props: any) {
    return (
      <input
        type="text"
        id="date"
        aria-labelledby="date-label"
        onChange={e => props.onChange(new Date(e.target.value))}
        placeholder={props.placeholderText}
        value={props.selected ? props.selected.toLocaleDateString() : ''}
        className={props.className}
      />
    );
  };
});

// Mock the recharts components
jest.mock('recharts', () => ({
  ResponsiveContainer: ({ children }: any) => children,
  LineChart: ({ children }: any) => <div data-testid="line-chart">{children}</div>,
  Line: () => <div data-testid="line" />,
  XAxis: () => <div data-testid="x-axis" />,
  YAxis: () => <div data-testid="y-axis" />,
  CartesianGrid: () => <div data-testid="cartesian-grid" />,
  Tooltip: () => <div data-testid="tooltip" />
}));

// Mock EngagementByTime component
jest.mock('../../components/EngagementByTime', () => ({
  EngagementByTime: () => <div data-testid="engagement-by-time" />
}));

describe('App', () => {
  it('renders the dashboard title', () => {
    render(<App />);
    expect(screen.getByText('Newsletter Analytics Dashboard')).toBeInTheDocument();
  });

  it('renders filter controls', () => {
    render(<App />);
    
    // Newsletter selector
    expect(screen.getByLabelText('Select Newsletter')).toBeInTheDocument();
    expect(screen.getByText('Weekly Tech Digest')).toBeInTheDocument();
    
    // Partner selector
    expect(screen.getByLabelText('Select Partner')).toBeInTheDocument();
    expect(screen.getByText('All Partners')).toBeInTheDocument();
    
    // Date picker
    const datePicker = screen.getByPlaceholderText('Select date');
    expect(datePicker).toBeInTheDocument();
    expect(screen.getByText('Date')).toBeInTheDocument();
  });

  it('renders all metric cards', () => {
    render(<App />);
    
    // Check for all metrics
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('Click Rate')).toBeInTheDocument();
    expect(screen.getByText('Bounce Rate')).toBeInTheDocument();
    
    // Check for metric values
    expect(screen.getByText('68.7%')).toBeInTheDocument();
    expect(screen.getByText('42.3%')).toBeInTheDocument();
    expect(screen.getByText('2.4%')).toBeInTheDocument();
  });

  it('renders the newsletter funnel', () => {
    render(<App />);
    
    expect(screen.getByText('Total Targeted')).toBeInTheDocument();
    expect(screen.getByText('Data Enriched')).toBeInTheDocument();
    expect(screen.getByText('Delivered')).toBeInTheDocument();
    expect(screen.getByText('Opened')).toBeInTheDocument();
    
    expect(screen.getByText('30,000')).toBeInTheDocument();
    expect(screen.getByText('27,500')).toBeInTheDocument();
    expect(screen.getByText('24,892')).toBeInTheDocument();
    expect(screen.getByText('17,100')).toBeInTheDocument();
  });

  it('renders data enrichment loss section', () => {
    render(<App />);
    
    expect(screen.getByText('Data Enrichment Loss')).toBeInTheDocument();
    
    // Check for items and their values
    const items = [
      { label: 'Invalid Emails', value: 1250 },
      { label: 'Missing Information', value: 858 },
      { label: 'Duplicate Records', value: 450 },
      { label: 'Format Errors', value: 225 }
    ];
    
    items.forEach(item => {
      expect(screen.getByText(item.label)).toBeInTheDocument();
      expect(screen.getByText(item.value.toLocaleString())).toBeInTheDocument();
    });
  });

  it('renders engagement by time section', () => {
    render(<App />);
    expect(screen.getByTestId('engagement-by-time')).toBeInTheDocument();
  });
});
