import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from '../../App';
import { fetchNewsletters, fetchPartners } from '../../services/api';

// Mock the API functions
jest.mock('../../services/api', () => ({
  fetchNewsletters: jest.fn(),
  fetchPartners: jest.fn()
}));

// Mock react-datepicker
jest.mock('react-datepicker', () => {
  return function DatePicker(props: any) {
    return (
      <input
        type="text"
        onChange={e => props.onChange(new Date(e.target.value))}
        placeholder={props.placeholderText}
        value={props.selected ? props.selected.toLocaleDateString() : ''}
        disabled={props.disabled}
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
  const mockNewsletters = ['Newsletter 1', 'Newsletter 2'];
  const mockPartners = ['Partner 1', 'Partner 2'];

  beforeEach(() => {
    (fetchNewsletters as jest.Mock).mockReset();
    (fetchPartners as jest.Mock).mockReset();
  });

  it('renders loading state', async () => {
    (fetchNewsletters as jest.Mock).mockImplementation(() => new Promise(() => {}));
    (fetchPartners as jest.Mock).mockImplementation(() => new Promise(() => {}));

    render(<App />);
    
    expect(screen.getByRole('button')).toBeDisabled();
    expect(screen.getByRole('combobox', { name: /newsletter/i })).toBeDisabled();
    expect(screen.getByRole('combobox', { name: /partner/i })).toBeDisabled();
  });

  it('renders error state', async () => {
    const errorMessage = 'Failed to fetch data. Please try again later.';
    (fetchNewsletters as jest.Mock).mockRejectedValue(new Error('API Error'));
    (fetchPartners as jest.Mock).mockRejectedValue(new Error('API Error'));

    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });

  it('renders successful data fetch', async () => {
    (fetchNewsletters as jest.Mock).mockResolvedValue(mockNewsletters);
    (fetchPartners as jest.Mock).mockResolvedValue(mockPartners);

    render(<App />);
    
    await waitFor(() => {
      mockNewsletters.forEach(newsletter => {
        expect(screen.getByText(newsletter)).toBeInTheDocument();
      });
      mockPartners.forEach(partner => {
        expect(screen.getByText(partner)).toBeInTheDocument();
      });
    });

    expect(screen.getByRole('button')).toBeDisabled(); // Submit button should be disabled initially
  });

  it('enables submit button when all fields are filled', async () => {
    (fetchNewsletters as jest.Mock).mockResolvedValue(mockNewsletters);
    (fetchPartners as jest.Mock).mockResolvedValue(mockPartners);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(mockNewsletters[0])).toBeInTheDocument();
    });

    // Select newsletter
    fireEvent.change(screen.getByRole('combobox', { name: /newsletter/i }), {
      target: { value: mockNewsletters[0] }
    });

    // Select partner
    fireEvent.change(screen.getByRole('combobox', { name: /partner/i }), {
      target: { value: mockPartners[0] }
    });

    // Select date
    fireEvent.change(screen.getByPlaceholderText('Select date'), {
      target: { value: '2024-03-15' }
    });

    expect(screen.getByRole('button')).not.toBeDisabled();
  });

  it('renders all dashboard components', async () => {
    (fetchNewsletters as jest.Mock).mockResolvedValue(mockNewsletters);
    (fetchPartners as jest.Mock).mockResolvedValue(mockPartners);

    render(<App />);

    // Check for metrics
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('Click Rate')).toBeInTheDocument();
    expect(screen.getByText('Bounce Rate')).toBeInTheDocument();

    // Check for funnel steps
    expect(screen.getByText('Total Targeted')).toBeInTheDocument();
    expect(screen.getByText('Data Enriched')).toBeInTheDocument();
    expect(screen.getByText('Delivered')).toBeInTheDocument();
    expect(screen.getByText('Opened')).toBeInTheDocument();

    // Check for data enrichment loss
    expect(screen.getByText('Data Enrichment Loss')).toBeInTheDocument();
    expect(screen.getByText('Invalid Emails')).toBeInTheDocument();
    expect(screen.getByText('Missing Information')).toBeInTheDocument();
    expect(screen.getByText('Duplicate Records')).toBeInTheDocument();
    expect(screen.getByText('Format Errors')).toBeInTheDocument();

    // Check for engagement by time
    expect(screen.getByTestId('engagement-by-time')).toBeInTheDocument();
  });
});
