import { render, screen } from '@testing-library/react';
import { DataEnrichmentLoss } from '../../../components/DataEnrichmentLoss';

describe('DataEnrichmentLoss', () => {
  const defaultProps = {
    items: [
      { label: 'Invalid Emails', value: 1250 },
      { label: 'Missing Information', value: 858 },
      { label: 'Duplicate Records', value: 450 },
      { label: 'Format Errors', value: 225 }
    ]
  };

  it('renders the section title', () => {
    render(<DataEnrichmentLoss {...defaultProps} />);
    expect(screen.getByText('Data Enrichment Loss')).toBeInTheDocument();
  });

  it('renders all loss items with labels and formatted values', () => {
    render(<DataEnrichmentLoss {...defaultProps} />);
    
    defaultProps.items.forEach(item => {
      expect(screen.getByText(item.label)).toBeInTheDocument();
      expect(screen.getByText(item.value.toLocaleString())).toBeInTheDocument();
    });
  });

  it('renders progress bars with different colors', () => {
    render(<DataEnrichmentLoss {...defaultProps} />);
    
    const progressBars = screen.getAllByRole('progressbar');
    expect(progressBars).toHaveLength(4);
    
    // Each progress bar should have a unique color class
    const colors = ['bg-danger', 'bg-warning', 'bg-orange', 'bg-brown'];
    progressBars.forEach((bar, index) => {
      expect(bar).toHaveClass(colors[index]);
    });
  });

  it('renders loading state correctly', () => {
    render(<DataEnrichmentLoss isLoading={true} items={[]} />);
    const progressBars = screen.queryAllByRole('progressbar');
    expect(progressBars).toHaveLength(0);
  });

  it('renders error state correctly', () => {
    render(<DataEnrichmentLoss error="Test error" items={[]} />);
    expect(screen.getByText('Error Loading Enrichment Failure Widget')).toBeInTheDocument();
  });

  it('renders empty state correctly', () => {
    render(<DataEnrichmentLoss isLoading={false} items={[]} />);
    expect(screen.getByText('No Enrichment Failures')).toBeInTheDocument();
  });

  it('calculates progress bar widths correctly', () => {
    const items = [
      { label: 'High Value', value: 100 },
      { label: 'Low Value', value: 50 }
    ];
    render(<DataEnrichmentLoss items={items} />);
    
    const progressBars = screen.getAllByRole('progressbar');
    const firstBar = progressBars[0].getAttribute('style');
    const secondBar = progressBars[1].getAttribute('style');
    
    expect(firstBar).toContain('width: 100%');
    expect(secondBar).toContain('width: 50%');
  });

  it('handles zero values correctly', () => {
    const items = [
      { label: 'Zero Value', value: 0 },
      { label: 'Some Value', value: 50 }
    ];
    render(<DataEnrichmentLoss items={items} />);
    
    const progressBars = screen.getAllByRole('progressbar');
    const zeroBar = progressBars[0].getAttribute('style');
    const valueBar = progressBars[1].getAttribute('style');
    
    expect(zeroBar).toContain('width: 0%');
    expect(valueBar).toContain('width: 100%');
  });
});
