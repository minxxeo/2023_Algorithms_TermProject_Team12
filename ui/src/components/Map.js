// Map.js
import React, { useEffect, useRef } from "react";

const Map = ({ onClick }) => {
  const mapContainer = useRef(null);

  useEffect(() => {
    const loadKakaoMapScript = () => {
      return new Promise((resolve) => {
        const script = document.createElement("script");
        script.src =
          "//dapi.kakao.com/v2/maps/sdk.js?appkey=da112912a21c56cd0a11eb26da6aaebd";
        script.async = true;
        script.onload = resolve;
        document.head.appendChild(script);
      });
    };

    const initializeMap = () => {
      try {
        const { kakao } = window;
        const position = new kakao.maps.LatLng(37.450805, 127.128954);

        const map = new kakao.maps.Map(mapContainer.current, {
          center: position,
          level: 4,
        });

        const marker = new kakao.maps.Marker({ position });
        marker.setMap(map);

        const content = `
          <div class="customoverlay">
            <span>포썸</span>
          </div>`;

        new kakao.maps.CustomOverlay({
          map,
          position,
          content: content,
        });
      } catch (error) {
        console.error("지도 초기화 오류: ", error);
      }
    };

    const initialize = async () => {
      try {
        if (!window.kakao) {
          console.log("Kakao 지도 SDK 로딩 중...");
          await loadKakaoMapScript();
        }

        // Kakao 지도 SDK 로딩 후 초기화 작업 수행
        if (window.kakao) {
          console.log("Kakao 지도 SDK 로딩 완료. 초기화 중...");
          initializeMap();
        } else {
          console.error("Kakao 지도 SDK 로드 실패.");
        }
      } catch (error) {
        console.error("초기화 오류:", error);
      }
    };

    initialize();
  }, []);

  return (
    <div
      id="map"
      ref={mapContainer}
      style={{ width: "100%", height: "400px", display: "block" }}
      onClick={onClick}
    ></div>
  );
};

export default Map;
