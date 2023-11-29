// src/util/LiveData.js

const KEY = "8820206620";
const BASE_URL = `http://data.ex.co.kr/openapi/odtraffic/trafficAmountByRealtime?key=${KEY}&type=json`;

// Function to fetch the real-time communication data from the JSON API
export const getListComponent = async () => {
  try {
    const response = await fetch(BASE_URL);
    const data = await response.json();
    return data.list;
  } catch (error) {
    console.error("Error fetching real-time data:", error);
    return null;
  }
};

// Function to calculate the average time based on real-time data
export const getAverageTime = async () => {
  console.log("Fetching real-time data");
  const list = await getListComponent();

  const traffic = new Map();
  const tCount = new Map();

  try {
    for (let i = 0; i < list.length; i++) {
      const temp = list[i];

      const conzoneID = temp.conzoneName;
      const timeAvg = parseInt(temp.timeAvg);
      const grade = parseInt(temp.grade);

      if (grade === 0) continue; // Skip cases with indeterminate judgment

      // If conzone information does not exist, add it
      if (!traffic.has(conzoneID)) {
        traffic.set(conzoneID, timeAvg);
        tCount.set(conzoneID, 1);
      } else {
        // If conzone information exists, update time and count
        traffic.set(conzoneID, traffic.get(conzoneID) + timeAvg);
        tCount.set(conzoneID, tCount.get(conzoneID) + 1);
      }
    }
    console.log("Finished");
    // Additional processing if needed
    // for (const s of traffic.keys()) {
    //   traffic.set(s, traffic.get(s) / tCount.get(s));
    // }
  } catch (error) {
    console.error("Error processing real-time data:", error);
  }

  return traffic;
};
