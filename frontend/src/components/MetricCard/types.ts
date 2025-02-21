export interface MetricCardProps {
  title: string;
  value: string;
  icon: 'subscribers' | 'open-rate' | 'click-rate' | 'bounce-rate';
  change?: number;
}
