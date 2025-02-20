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

  it('renders all loss items with labels and values', () => {
    render(<DataEnrichmentLoss {...defaultProps} />);
    
    defaultProps.items.forEach(item => {
      expect(screen.getByText(item.label)).toBeInTheDocument();
      expect(screen.getByText(item.value.toString())).toBeInTheDocument();
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
});
