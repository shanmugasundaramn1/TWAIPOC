export interface FunnelStep {
  label: string;
  value: number;
  color: string;
}

export interface NewsletterFunnelProps {
  steps: FunnelStep[];
}
