import React from 'react';
import { MetricsDashboard } from './components/MetricsDashboard';
import { NewsletterFunnel } from './components/NewsletterFunnel';
import { DataEnrichmentLoss } from './components/DataEnrichmentLoss';

function App() {
  const metrics = [
    {
      title: 'Total Subscribers',
      value: '24,892',
      change: 12.5,
      icon: 'subscribers' as const,
    },
    {
      title: 'Open Rate',
      value: '68.7%',
      change: 3.2,
      icon: 'open-rate' as const,
    },
    {
      title: 'Click Rate',
      value: '42.3%',
      change: -1.8,
      icon: 'click-rate' as const,
    },
    {
      title: 'Bounce Rate',
      value: '2.4%',
      change: -0.5,
      icon: 'bounce-rate' as const,
    },
  ];

  return (
    <div className="min-vh-100 bg-light">
      <div className="container-fluid px-4 py-4" style={{ maxWidth: '1600px' }}>
        <header className="mb-4">
          <h1 className="h3 mb-4">Newsletter Analytics Dashboard</h1>
          <div className="row g-3">
            <div className="col-md-4">
              <label htmlFor="newsletter" className="form-label">Select Newsletter</label>
              <select id="newsletter" className="form-select" defaultValue="Weekly Tech Digest">
                <option>Weekly Tech Digest</option>
              </select>
            </div>
            <div className="col-md-4">
              <label htmlFor="partner" className="form-label">Select Partner</label>
              <select id="partner" className="form-select" defaultValue="All Partners">
                <option>All Partners</option>
              </select>
            </div>
            <div className="col-md-4">
              <label htmlFor="date" className="form-label">Date Range</label>
              <input 
                id="date"
                type="date" 
                className="form-control"
              />
            </div>
          </div>
        </header>
        <main className="mb-5">
          <MetricsDashboard metrics={metrics} />
        </main>

        <section className="row g-4 mb-5">
          <div className="col-lg-12">
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
          </div>
        </section>

        <section className="row g-4">
          <div className="col-lg-12">
            <DataEnrichmentLoss
              items={[
                { label: 'Invalid Emails', value: 1250 },
                { label: 'Missing Information', value: 858 },
                { label: 'Duplicate Records', value: 450 },
                { label: 'Format Errors', value: 225 }
              ]}
            />
          </div>
        </section>
      </div>
    </div>
  );
}

export default App;
