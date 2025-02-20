export interface FunnelStep {
  label: string;
  value: string;
  color: string;
}

export interface NewsletterFunnelProps {
  steps: FunnelStep[];
}
