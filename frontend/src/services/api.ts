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
