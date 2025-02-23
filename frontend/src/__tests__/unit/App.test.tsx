import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from '../../App';
import { fetchNewsletters, fetchPartners, fetchEnrichmentFailures } from '../../services/api';

// Mock the API functions
jest.mock('../../services/api', () => ({
  fetchNewsletters: jest.fn(),
  fetchPartners: jest.fn(),
  fetchEnrichmentFailures: jest.fn()
}));

// Mock fetch
const mockFetch = jest.fn();
global.fetch = mockFetch;

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
        data-testid="date-picker"
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
    (fetchEnrichmentFailures as jest.Mock).mockReset();
    mockFetch.mockReset();
  });

  it('renders loading state', async () => {
    (fetchNewsletters as jest.Mock).mockImplementation(() => new Promise(() => {}));
    (fetchPartners as jest.Mock).mockImplementation(() => new Promise(() => {}));

    render(<App />);
    
    expect(screen.getByRole('button')).toBeDisabled();
    expect(screen.getByRole('combobox', { name: /newsletter/i })).toBeDisabled();
    expect(screen.getByRole('combobox', { name: /partner/i })).toBeDisabled();
    expect(screen.getByTestId('date-picker')).toBeDisabled();
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

    expect(screen.getByRole('button')).toBeDisabled();
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
    const datePicker = screen.getByTestId('date-picker');
    fireEvent.change(datePicker, {
      target: { value: '2024-03-15' }
    });

    expect(screen.getByRole('button')).not.toBeDisabled();
  });

  describe('Form submission', () => {
    beforeEach(async () => {
      (fetchNewsletters as jest.Mock).mockResolvedValue(mockNewsletters);
      (fetchPartners as jest.Mock).mockResolvedValue(mockPartners);
      (fetchEnrichmentFailures as jest.Mock).mockResolvedValue([]);

      render(<App />);

      await waitFor(() => {
        expect(screen.getByText(mockNewsletters[0])).toBeInTheDocument();
      });

      // Fill form
      fireEvent.change(screen.getByRole('combobox', { name: /newsletter/i }), {
        target: { value: mockNewsletters[0] }
      });
      fireEvent.change(screen.getByRole('combobox', { name: /partner/i }), {
        target: { value: mockPartners[0] }
      });
      fireEvent.change(screen.getByTestId('date-picker'), {
        target: { value: '2024-03-15' }
      });
    });

    it('should make parallel API calls and show loading states', async () => {
      // Mock APIs with delay
      mockFetch.mockImplementation((url) => {
        if (url.includes('total-targeted')) {
          return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({
              total_targeted: 5000,
              total_delivered: 4800,
              total_opened: 3500,
              data_enriched: 4500,
              total_coupon_clicked: 2000,
              total_bounced: 500
            })
          });
        }
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ failureReasonCounts: {} })
        });
      });

      // Submit form
      fireEvent.click(screen.getByRole('button'));

      // Check for loading states
      await waitFor(() => {
        const loadingSpinner = screen.getByRole('status');
        expect(loadingSpinner).toBeInTheDocument();
        expect(loadingSpinner).toHaveClass('spinner-border');
      });

      // Wait for data to load
      await waitFor(() => {
        expect(screen.getByText('4,800')).toBeInTheDocument(); // Total Subscribers
        expect(screen.getByText('No Enrichment Failures')).toBeInTheDocument();
      });

      // Verify both API calls were made
      expect(mockFetch).toHaveBeenCalledWith(expect.stringContaining('total-targeted'));
      expect(fetchEnrichmentFailures).toHaveBeenCalled();
    });

    it('should handle zero values correctly in percentage calculations', async () => {
      // Mock API response with zero values
      mockFetch.mockImplementation((url) => {
        if (url.includes('total-targeted')) {
          return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({
              total_targeted: 1000,
              total_delivered: 0,
              total_opened: 0,
              data_enriched: 0,
              total_coupon_clicked: 0,
              total_bounced: 0
            })
          });
        }
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ failureReasonCounts: {} })
        });
      });

      // Submit form
      fireEvent.click(screen.getByRole('button'));

      // Wait for data to load and verify percentages are 0%
      await waitFor(() => {
        const zeroPercentages = screen.getAllByText('0%');
        expect(zeroPercentages).toHaveLength(3); // All rates should be 0%
      });
    });

    it('should handle negative values as zero in percentage calculations', async () => {
      // Mock API response with negative values
      mockFetch.mockImplementation((url) => {
        if (url.includes('total-targeted')) {
          return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({
              total_targeted: 1000,
              total_delivered: 50,  // negative should be treated as 0
              total_opened: 25,
              data_enriched: 100,
              total_coupon_clicked: -10,  // negative should be treated as 0
              total_bounced: 20
            })
          });
        }
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ failureReasonCounts: {} })
        });
      });

      // Submit form
      fireEvent.click(screen.getByRole('button'));

      // Wait for data to load and verify percentages treat negatives as 0
      await waitFor(() => {
        // Click Rate should be 0% since coupon_clicked is negative
        expect(screen.getByText('0.0%')).toBeInTheDocument();
        // Bounce Rate should be 20% (20/100)
        expect(screen.getByText('20.0%')).toBeInTheDocument();
      });
    });

    it('should handle API errors independently', async () => {
      // Mock metrics API success, enrichment API failure
      mockFetch.mockImplementation((url) => {
        if (url.includes('total-targeted')) {
          return Promise.resolve({
            ok: true,
            json: () => Promise.resolve({
              total_targeted: 5000,
              total_delivered: 4800,
              total_opened: 3500,
              data_enriched: 4500,
              total_coupon_clicked: 2000,
              total_bounced: 500
            })
          });
        }
        return Promise.resolve({ ok: false });
      });

      (fetchEnrichmentFailures as jest.Mock).mockRejectedValue(new Error('API Error'));

      // Submit form
      fireEvent.click(screen.getByRole('button'));

      // Wait for APIs to resolve
      await waitFor(() => {
        // Metrics should show data
        expect(screen.getByText('4,800')).toBeInTheDocument();
        // Enrichment should show error
        expect(screen.getByText('Error Loading Enrichment Failure Widget')).toBeInTheDocument();
      });
    });
  });
});
