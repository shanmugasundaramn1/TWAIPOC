import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import { MetricsDashboard } from './components/MetricsDashboard';
import { NewsletterFunnel } from './components/NewsletterFunnel';
import { DataEnrichmentLoss } from './components/DataEnrichmentLoss';
import { EngagementByTime } from './components/EngagementByTime';
import { fetchNewsletters, fetchPartners } from './services/api';
import 'react-datepicker/dist/react-datepicker.css';
import './App.css';

function App() {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [newsletters, setNewsletters] = useState<string[]>([]);
  const [partners, setPartners] = useState<string[]>([]);
  const [selectedNewsletter, setSelectedNewsletter] = useState<string>('');
  const [selectedPartner, setSelectedPartner] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

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

  const handleSubmit = () => {
    // TODO: Implement submit logic
    console.log('Submit clicked', {
      selectedNewsletter,
      selectedPartner,
      selectedDate
    });
  };

  const metrics = [
    {
      title: 'Open Rate',
      value: '68.7%',
      icon: 'open-rate' as const,
    },
    {
      title: 'Click Rate',
      value: '42.3%',
      icon: 'click-rate' as const,
    },
    {
      title: 'Bounce Rate',
      value: '2.4%',
      icon: 'bounce-rate' as const,
    },
  ];

  const enrichmentData = [
    { label: 'Invalid Emails', value: 1250 },
    { label: 'Missing Information', value: 858 },
    { label: 'Duplicate Records', value: 450 },
    { label: 'Format Errors', value: 225 }
  ];

  const engagementData = [
    { timestamp: '2024-03-01T09:00:00', value: 15000 },
    { timestamp: '2024-03-01T12:00:00', value: 17500 },
    { timestamp: '2024-03-01T15:00:00', value: 16800 },
    { timestamp: '2024-03-01T18:00:00', value: 18200 },
    { timestamp: '2024-03-01T21:00:00', value: 19500 }
  ];

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
                <MetricsDashboard 
                  metrics={metrics}
                />
              </section>

              <section className="mb-5">
                <NewsletterFunnel
                  steps={[
                    {
                      label: 'Total Targeted',
                      value: '30,000',
                      color: 'blue'
                    },
                    {
                      label: 'Data Enriched',
                      value: '27,500',
                      color: 'green'
                    },
                    {
                      label: 'Delivered',
                      value: '24,892',
                      color: 'yellow'
                    },
                    {
                      label: 'Opened',
                      value: '17,100',
                      color: 'purple'
                    }
                  ]}
                />
              </section>

              <section className="row g-4">
                <div className="col-md-6">
                  <DataEnrichmentLoss items={enrichmentData} />
                </div>
                <div className="col-md-6">
                  <EngagementByTime data={engagementData} />
                </div>
              </section>
            </main>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
