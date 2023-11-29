// src/components/TaxiFinder.js
import React, { useState } from "react";
import Map from "./Map";

const TaxiFinder = () => {
  const [startLocation, setStartLocation] = useState(null);
  const [endLocation, setEndLocation] = useState(null);
  const [selectedMetric, setSelectedMetric] = useState("taxiFare");
  const [result, setResult] = useState(null);

  const handleMapClick = ({ latlng }) => {
    if (latlng) {
      const { lat, lng } = latlng;
      if (!startLocation) {
        setStartLocation({ lat, lng });
      } else {
        setEndLocation({ lat, lng });
      }
    }
  };

  const calculateMetrics = () => {
    // 모의 데이터 (택시 요금, 이동 시간, 거리)
    const mockData = {
      taxiFare: 3000, // 1km당 3000원
      minTravelTime: 30, // 최소 이동 시간 (분)
      minDistance: 10, // 최소 거리 (km)
      weather: "맑음", // 날씨 정보
      restArea: "휴게소 A", // 휴게소 정보
      restAreaDistance: 5, // 휴게소까지의 거리 (km)
      restAreaFee: 2000, // 휴게소 이용료
    };

    return mockData;
  };

  const handleFindTaxi = () => {
    const metrics = calculateMetrics();
    setResult(`최소 ${selectedMetric}: ${metrics[selectedMetric]}`);
  };

  return (
    <div
      style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <h1 style={{ margin: "20px 0" }}>Taxi Finder</h1>
      <div style={{ display: "flex", marginBottom: "20px" }}>
        <div style={{ marginRight: "20px" }}>
          <label style={{ display: "block", marginBottom: "5px" }}>
            출발지:
          </label>
          <input
            type="text"
            placeholder="출발지 입력"
            style={{ marginBottom: "10px" }}
          />
          <label style={{ display: "block", marginBottom: "5px" }}>
            도착지:
          </label>
          <input type="text" placeholder="도착지 입력" />
        </div>
        <Map
          onClick={handleMapClick}
          startLocation={startLocation}
          endLocation={endLocation}
        />
      </div>
      <div style={{ marginBottom: "20px" }}>
        <label style={{ display: "block", marginBottom: "10px" }}>
          계산할 지표 선택:
        </label>
        <div style={{ display: "flex", gap: "10px" }}>
          <div>
            <input
              type="radio"
              id="taxiFare"
              name="metric"
              value="taxiFare"
              checked={selectedMetric === "taxiFare"}
              onChange={() => setSelectedMetric("taxiFare")}
            />
            <label htmlFor="taxiFare">택시 요금</label>
          </div>
          <div>
            <input
              type="radio"
              id="minDistance"
              name="metric"
              value="minDistance"
              checked={selectedMetric === "minDistance"}
              onChange={() => setSelectedMetric("minDistance")}
            />
            <label htmlFor="minDistance">최소 이동 거리</label>
          </div>
          <div>
            <input
              type="radio"
              id="minTravelTime"
              name="metric"
              value="minTravelTime"
              checked={selectedMetric === "minTravelTime"}
              onChange={() => setSelectedMetric("minTravelTime")}
            />
            <label htmlFor="minTravelTime">최소 이동 시간</label>
          </div>
        </div>
      </div>
      <button onClick={handleFindTaxi}>택시 찾기</button>
      {result && (
        <div style={{ marginTop: "20px", textAlign: "center" }}>{result}</div>
      )}
    </div>
  );
};

export default TaxiFinder;
