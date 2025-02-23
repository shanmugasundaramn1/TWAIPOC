import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import { MetricsDashboard } from './components/MetricsDashboard';
import { NewsletterFunnel } from './components/NewsletterFunnel';
import { DataEnrichmentLoss } from './components/DataEnrichmentLoss';
import { fetchNewsletters, fetchPartners, fetchEnrichmentFailures, EnrichmentFailure } from './services/api';
import 'react-datepicker/dist/react-datepicker.css';
import './App.css';

interface TotalTargetedResponse {
  total_targeted: number;
  total_delivered: number;
  total_opened: number;
  data_enriched: number;
  total_coupon_clicked: number;
  total_bounced: number;
}

interface FunnelStep {
  label: string;
  value: number;
  color: string;
}

const formatDateToYYYYMMDD = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

function App() {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [newsletters, setNewsletters] = useState<string[]>([]);
  const [partners, setPartners] = useState<string[]>([]);
  const [selectedNewsletter, setSelectedNewsletter] = useState<string>('');
  const [selectedPartner, setSelectedPartner] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [metricsLoading, setMetricsLoading] = useState(false);
  const [metricsError, setMetricsError] = useState<string | null>(null);
  const [steps, setSteps] = useState<FunnelStep[]>([
    { label: 'Total Targeted', value: 0, color: 'blue' },
    { label: 'Data Enriched', value: 0, color: 'green' },
    { label: 'Delivered', value: 0, color: 'yellow' },
    { label: 'Opened', value: 0, color: 'purple' }
  ]);

  const [enrichmentData, setEnrichmentData] = useState<EnrichmentFailure[]>([]);
  const [enrichmentError, setEnrichmentError] = useState<string | null>(null);
  const [enrichmentLoading, setEnrichmentLoading] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const [newslettersList, partnersList] = await Promise.all([
        fetchNewsletters(),
        fetchPartners()
      ]);
      setNewsletters(newslettersList);
      setPartners(partnersList);
    } catch (err) {
      setError('Failed to fetch data. Please try again later.');
      console.error('Error fetching data:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setIsSubmitted(true);
    setMetricsError(null);
    setEnrichmentError(null);
    setMetricsLoading(true);
    setEnrichmentLoading(true);

    const fetchMetricsData = async () => {
      try {
        const formattedDate = formatDateToYYYYMMDD(selectedDate!);

        // Create URL with properly encoded parameters
        const url = new URL('http://localhost:8080/api/selected-audience-status/total-targeted');
        url.searchParams.append('newsletterName', selectedNewsletter);
        url.searchParams.append('date', formattedDate);
        url.searchParams.append('partnerName', selectedPartner);

        const response = await fetch(url.toString());

        if (!response.ok) {
          throw new Error('Failed to fetch metrics data');
        }

        const data: TotalTargetedResponse = await response.json();
        setSteps([
          { label: 'Total Targeted', value: data.total_targeted, color: 'blue' },
          { label: 'Data Enriched', value: data.data_enriched, color: 'green' },
          { label: 'Delivered', value: data.total_delivered, color: 'yellow' },
          { label: 'Opened', value: data.total_opened, color: 'purple' }
        ]);

        // Calculate and update metrics
        const calculatePercentage = (numerator: number, denominator: number): string => {
          if (denominator === 0) return '0%';
          // Treat negative values as 0
          const safeNumerator = numerator < 0 ? 0 : numerator;
          const safeDenominator = denominator < 0 ? 0 : denominator;
          return `${((safeNumerator / safeDenominator) * 100).toFixed(1)}%`;
        };

        setMetrics([
          {
            title: 'Total Subscribers',
            value: data.total_delivered.toLocaleString(),
            icon: 'subscribers' as const,
          },
          {
            title: 'Open Rate',
            value: calculatePercentage(data.total_opened, data.total_delivered),
            icon: 'open-rate' as const,
          },
          {
            title: 'Click Rate',
            value: calculatePercentage(data.total_coupon_clicked, data.total_delivered),
            icon: 'click-rate' as const,
          },
          {
            title: 'Bounce Rate',
            value: calculatePercentage(data.total_bounced, data.data_enriched),
            icon: 'bounce-rate' as const,
          },
        ]);
      } catch (error) {
        setMetricsError('Failed to fetch metrics data');
        console.error('Error fetching metrics:', error);
      } finally {
        setMetricsLoading(false);
      }
    };

    const fetchEnrichmentData = async () => {
      try {
        const data = await fetchEnrichmentFailures(
          selectedNewsletter,
          selectedPartner,
          selectedDate
        );
        setEnrichmentData(data);
      } catch (error) {
        setEnrichmentError('Failed to fetch enrichment failures');
        console.error('Error fetching enrichment failures:', error);
      } finally {
        setEnrichmentLoading(false);
      }
    };

    // Execute both API calls in parallel
    await Promise.all([
      fetchMetricsData(),
      fetchEnrichmentData()
    ]);
  };

  const [metrics, setMetrics] = useState([
    {
      title: 'Total Subscribers',
      value: '0',
      icon: 'subscribers' as const,
    },
    {
      title: 'Open Rate',
      value: '0%',
      icon: 'open-rate' as const,
    },
    {
      title: 'Click Rate',
      value: '0%',
      icon: 'click-rate' as const,
    },
    {
      title: 'Bounce Rate',
      value: '0%',
      icon: 'bounce-rate' as const,
    },
  ]);

  const renderMetricsSection = () => {
    if (!isSubmitted) return null;

    if (metricsLoading) {
      return (
        <div className="card shadow-sm mb-4">
          <div className="card-body text-center p-5">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading metrics...</span>
            </div>
          </div>
        </div>
      );
    }

    if (metricsError) {
      return (
        <div className="alert alert-danger" role="alert">
          {metricsError}
        </div>
      );
    }

    return (
      <>
        <MetricsDashboard metrics={metrics} />
        <NewsletterFunnel steps={steps} />
      </>
    );
  };

  return (
    <div className="min-vh-100 bg-light">
      <div className="container-fluid dashboard-container">
        <div className="row">
          <div className="col-12">
            <header className="mb-4">
              <h1 className="h3 mb-4">Newsletter Analytics Dashboard</h1>
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}
              <div className="row g-3">
              <div className="col-md-4">
                  <label htmlFor="partner" className="form-label">Select Partner</label>
                  <select 
                    id="partner" 
                    className="form-select"
                    value={selectedPartner}
                    onChange={(e) => setSelectedPartner(e.target.value)}
                    disabled={isLoading}
                  >
                    <option value="">Select a partner</option>
                    {partners.map(name => (
                      <option key={name} value={name}>{name}</option>
                    ))}
                  </select>
                </div>
                <div className="col-md-4">
                  <label htmlFor="newsletter" className="form-label">Select Newsletter</label>
                  <select 
                    id="newsletter" 
                    className="form-select"
                    value={selectedNewsletter}
                    onChange={(e) => setSelectedNewsletter(e.target.value)}
                    disabled={isLoading}
                  >
                    <option value="">Select a newsletter</option>
                    {newsletters.map(name => (
                      <option key={name} value={name}>{name}</option>
                    ))}
                  </select>
                </div>
                <div className="col-md-4">
                  <label htmlFor="date" className="form-label">Date</label>
                  <DatePicker
                    selected={selectedDate}
                    onChange={(date) => setSelectedDate(date)}
                    className="form-control"
                    placeholderText="Select date"
                    isClearable={true}
                    dateFormat="MMM d, yyyy"
                    disabled={isLoading}
                  />
                </div>
              </div>
              <div className="row mt-3">
                <div className="col-12">
                  <button 
                    className="btn btn-primary"
                    onClick={handleSubmit}
                    disabled={isLoading || !selectedNewsletter || !selectedPartner || !selectedDate}
                  >
                    {isLoading ? 'Loading...' : 'Submit'}
                  </button>
                </div>
              </div>
            </header>
            
            <main>
              <section className="mb-5">
                {renderMetricsSection()}
              </section>

              {isSubmitted && (
                <section className="row g-3">
                  <div className="col-md-12">
                    <DataEnrichmentLoss 
                      items={enrichmentData}
                      isLoading={enrichmentLoading}
                      error={enrichmentError}
                    />
                  </div>
                </section>
              )}
            </main>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
