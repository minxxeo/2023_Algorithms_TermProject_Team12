const express = require("express");
const bodyParser = require("body-parser");
const axios = require("axios");
const Dfs = require("./src/algorithms/Dfs");
const Calculator = require("./src/algorithms/Calculator");

const app = express();
const port = 3001;

app.use(bodyParser.json());

// 출발지 또는 도착지의 좌표를 찾는 함수
const findLocation = async (place) => {
  try {
    // Kakao Map API의 장소 검색 서비스 사용
    const response = await axios.get(
      `https://dapi.kakao.com/v2/local/search/keyword.json?query=${encodeURIComponent(
        place
      )}`,
      {
        headers: {
          Authorization: "KakaoAK <4fdadc29336b42eacca20f0824eb787a>", // 본인의 Kakao API 키로 교체
        },
      }
    );

    // 가장 첫 번째 검색 결과의 좌표 반환
    if (response.data.documents.length > 0) {
      const { x, y } = response.data.documents[0].address;

      return { lat: parseFloat(y), lng: parseFloat(x) };
    } else {
      throw new Error("장소를 찾을 수 없습니다.");
    }
  } catch (error) {
    console.error("장소 검색 오류:", error);
    throw error;
  }
};

const calculateMetrics = async (startLocationName, endLocationName) => {
  // 출발지와 도착지의 좌표를 찾음
  const startCoordinate = await findLocation(startLocationName);
  const endCoordinate = await findLocation(endLocationName);

  // 좌표를 콘존 ID로 변환하는 로직이 필요한 경우 여기에 추가

  // 예시로 임시로 콘존 ID를 지정
  const startConzonId = 1;
  const endConzonId = 2;

  const dfs = new Dfs();
  const [distance, line] = dfs.getStart2End(startConzonId, endConzonId, 1); // mode: 1은 거리 계산

  const metrics = {
    taxiFare: Calculator.calcTexi(distance, 30), // 임의의 값 사용
    minTravelTime: distance / 40, // 임의의 값 사용
    minDistance: distance,
    path: line, // 이동 경로
  };

  return metrics;
};

app.post("/api/findTaxi", async (req, res) => {
  const { startLocationName, endLocationName } = req.body;

  try {
    const metrics = await calculateMetrics(startLocationName, endLocationName);
    res.json(metrics);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// 추가: 루트 경로에 대한 간단한 응답
app.get("/", (req, res) => {
  res.send("Hello, this is the Taxi Finder server!");
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
