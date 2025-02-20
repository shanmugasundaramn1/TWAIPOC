export interface EngagementDataPoint {
  timestamp: string;
  value: number;
}

export interface EngagementByTimeProps {
  data: EngagementDataPoint[];
} 