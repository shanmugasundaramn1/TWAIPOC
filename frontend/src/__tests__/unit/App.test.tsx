import { render, screen, fireEvent } from '@testing-library/react';
import App from '../../App';

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
    expect(screen.getByLabelText('Date')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Select date')).toBeInTheDocument();
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
    expect(screen.getByText('Invalid Emails')).toBeInTheDocument();
    expect(screen.getByText('Missing Information')).toBeInTheDocument();
    expect(screen.getByText('Duplicate Records')).toBeInTheDocument();
    expect(screen.getByText('Format Errors')).toBeInTheDocument();
  });

  it('renders engagement by time section', () => {
    render(<App />);
    
    expect(screen.getByText('Engagement by Time')).toBeInTheDocument();
    expect(screen.getByTestId('line-chart')).toBeInTheDocument();
  });

  it('handles date selection', () => {
    render(<App />);
    const datePicker = screen.getByPlaceholderText('Select date');
    
    // Test date picker interaction
    fireEvent.change(datePicker, { target: { value: '2024-03-15' } });
    expect(datePicker).toHaveValue('2024-03-15');
    
    // Test date clear functionality
    fireEvent.change(datePicker, { target: { value: '' } });
    expect(datePicker).toHaveValue('');
  });
});
