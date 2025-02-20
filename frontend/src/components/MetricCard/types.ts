export interface MetricCardProps {
  title: string;
  value: string;
  change: number;
  icon: 'subscribers' | 'open-rate' | 'click-rate' | 'bounce-rate';
}
