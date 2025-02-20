import { render, screen } from '@testing-library/react';
import App from '../../App';

describe('App', () => {
  it('renders the dashboard title', () => {
    render(<App />);
    expect(screen.getByText('Newsletter Analytics Dashboard')).toBeInTheDocument();
  });

  it('renders filter controls', () => {
    render(<App />);
    
    // Newsletter selector
    expect(screen.getByRole('combobox', { name: /weekly tech digest/i })).toBeInTheDocument();
    
    // Partner selector
    expect(screen.getByRole('combobox', { name: /all partners/i })).toBeInTheDocument();
    
    // Date picker
    expect(screen.getByRole('textbox', { name: /date range/i })).toBeInTheDocument();
  });

  it('renders all metric cards', () => {
    render(<App />);
    
    // Check for all metrics
    expect(screen.getByText('Total Subscribers')).toBeInTheDocument();
    expect(screen.getByText('Open Rate')).toBeInTheDocument();
    expect(screen.getByText('Click Rate')).toBeInTheDocument();
    expect(screen.getByText('Bounce Rate')).toBeInTheDocument();
    
    // Check for metric values
    expect(screen.getByText('24,892')).toBeInTheDocument();
    expect(screen.getByText('68.7%')).toBeInTheDocument();
    expect(screen.getByText('42.3%')).toBeInTheDocument();
    expect(screen.getByText('2.4%')).toBeInTheDocument();
  });
});
