export interface LossItem {
  label: string;
  value: number;
}

export interface DataEnrichmentLossProps {
  items?: LossItem[];
  isLoading?: boolean;
  error?: string | null;
}
