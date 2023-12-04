import React, { useState, useEffect } from "react";
import Map from "./Map";
import { ConzoneCoordinates } from "./ConzoneCoordinates";
import "../App.css";

const TaxiFinder = () => {
  const [startLocation, setStartLocation] = useState("");
  const [endLocation, setEndLocation] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [conzoneNames, setConzoneNames] = useState([]);
  const [filteredConzoneNames, setFilteredConzoneNames] = useState([]);
  const [inputValue, setInputValue] = useState("");

  useEffect(() => {
    const fetchConzoneNames = async () => {
      try {
        const response = await fetch("/assets/conzonloc.txt");
        const textData = await response.text();
        const conzoneNamesArray = parseConzoneNames(textData);
        setConzoneNames(conzoneNamesArray);
        setFilteredConzoneNames(conzoneNamesArray);
      } catch (error) {
        console.error("Error fetching conzone names:", error);
      }
    };

    fetchConzoneNames();
  }, []);

  const parseConzoneNames = (textData) => {
    const lines = textData.split("\n");
    const conzoneNamesArray = lines
      .slice(1)
      .map((line, index) => {
        const values = line.split(",");
        return values[15]?.replace(/"/g, "");
      })
      .filter((name) => name);

    const uniqueConzoneNamesArray = Array.from(new Set(conzoneNamesArray));

    // 가나다순으로 정렬
    return uniqueConzoneNamesArray.sort((a, b) => a.localeCompare(b));
  };

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

  const handleFindTaxi = () => {
    try {
      setResult({
        minDistance: 361.26,
        fee: "482,100 원",
        time: "3시간 48분",
        path: "부산TG - 노포IC - 노포JC - 양산JC - 양산IC - 통도사IC - 서울주JC - 서울산IC - 언양JC - 활천IC - 경주IC - 건천IC - 영천JC - 북안IC - 동영천IC - 화산JC - 신녕IC - 동군위IC - 군위JC - 서군위IC - 도개IC - 상주JC - 낙동JC - 남상주IC - 화서IC - 속리산IC - 보은IC - 회인IC - 문의청남대IC - 청주JC - 남이JC - 청주IC - 옥산IC - 독립기념관IC - 천안JC - 천안IC - 북천안IC - 안성IC - 안성JC - 남사진위IC - 오산IC - 동탄JC - 기흥동탄IC - 기흥IC - 수원신갈IC", // (생략) 실제 로직에 맞게 수정 필요
      });
      setLoading(false);

      const bounds = new window.kakao.maps.LatLngBounds();
      Object.values(ConzoneCoordinates).forEach(({ lat, lng }) => {
        bounds.extend(new window.kakao.maps.LatLng(lat, lng));
      });
      window.map.setBounds(bounds);

      document.getElementById("resultModal").classList.add("show");
    } catch (error) {
      console.error("택시 찾기 오류:", error);
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    document.getElementById("resultModal").classList.remove("show");
  };

  const handleInputChange = (event, setLocation, setFilteredNames) => {
    const value = event.target.value;
    setInputValue(value);

    // 입력 값이 포함된 이름만 필터링
    const filteredNames = conzoneNames.filter((name) => name.includes(value));

    setFilteredNames(filteredNames);
    setLocation(value);
  };

  const handleDropdownSelect = (selectedName, setLocation) => {
    setLocation(selectedName);
    setInputValue(selectedName);
  };

  return (
    <div className="taxi-finder-container">
      <Map
        startLocation={startLocation}
        endLocation={endLocation}
        onClick={handleMapClick}
      />
      <div className="taxi-finder-form">
        <label>출발지:</label>
        <input
          type="text"
          placeholder="출발지 입력"
          id="startLocation"
          value={startLocation}
          onChange={(e) =>
            handleInputChange(e, setStartLocation, setFilteredConzoneNames)
          }
        />
        <div className="dropdown">
          <select
            onChange={(e) =>
              handleDropdownSelect(e.target.value, setStartLocation)
            }
          >
            {filteredConzoneNames.map((name, index) => (
              <option key={index} value={name}>
                {name}
              </option>
            ))}
          </select>
        </div>
        <label>도착지:</label>
        <input
          type="text"
          placeholder="도착지 입력"
          id="endLocation"
          value={endLocation}
          onChange={(e) =>
            handleInputChange(e, setEndLocation, setFilteredConzoneNames)
          }
        />
        <div className="dropdown">
          <select
            onChange={(e) =>
              handleDropdownSelect(e.target.value, setEndLocation)
            }
          >
            {filteredConzoneNames.map((name, index) => (
              <option key={index} value={name}>
                {name}
              </option>
            ))}
          </select>
        </div>
        <button onClick={handleFindTaxi}>찾기</button>
        {loading && <p style={{ marginTop: "10px" }}>로딩 중...</p>}
      </div>
      {result && (
        <div id="resultModal" className="taxi-result-modal">
          <button
            onClick={handleCloseModal}
            style={{
              position: "absolute",
              top: "10px",
              right: "10px",
              fontSize: "16px",
              border: "none",
              background: "none",
              cursor: "pointer",
            }}
          >
            ✕
          </button>
          <p style={{ margin: "5px 0" }}>이동 거리: {result.minDistance} km</p>
          <p style={{ margin: "5px 0" }}>이동 요금: {result.fee}</p>
          <p style={{ margin: "5px 0" }}>이동 시간: {result.time}</p>
          <p style={{ margin: "5px 0" }}>이동 경로: {result.path}</p>
        </div>
      )}
    </div>
  );
};

export default TaxiFinder;
