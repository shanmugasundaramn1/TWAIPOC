export const fetchNewsletters = async (): Promise<string[]> => {
  try {
    const response = await fetch('http://localhost:8080/api/newsletters');
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Error fetching newsletters:', error);
    throw error;
  }
};

export const fetchPartners = async (): Promise<string[]> => {
  try {
    const response = await fetch('http://localhost:8080/api/partners');
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Error fetching partners:', error);
    throw error;
  }
};

export interface EnrichmentFailure {
  label: string;
  value: number;
}

interface EnrichmentFailureResponse {
  failureReasonCounts: {
    [key: string]: number;
  };
}

const formatDateToYYYYMMDD = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

export const fetchEnrichmentFailures = async (
  newsletterName: string,
  partnerName: string,
  date: Date | null
): Promise<EnrichmentFailure[]> => {
  if (!newsletterName || !partnerName || !date) {
    return [];
  }

  try {
    const formattedDate = formatDateToYYYYMMDD(date);

    // Create URL with properly encoded parameters
    const url = new URL('http://localhost:8080/api/v1/enrichment/failures');
    url.searchParams.append('newsletterName', newsletterName);
    url.searchParams.append('partnerName', partnerName);
    url.searchParams.append('palDate', formattedDate);

    const response = await fetch(url.toString());
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data: EnrichmentFailureResponse = await response.json();
    
    // Transform the Map into an array of LossItems
    return Object.entries(data.failureReasonCounts).map(([label, value]) => ({
      label,
      value
    }));
  } catch (error) {
    console.error('Error fetching enrichment failures:', error);
    throw error;
  }
};
