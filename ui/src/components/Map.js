// Map.js
import React, { useEffect, useRef } from "react";
import { ConzoneCoordinates, ConzoneRoute } from "./ConzoneCoordinates";

const Map = ({ startLocation, endLocation }) => {
  const mapContainer = useRef(null);

  useEffect(() => {
    const initializeMap = () => {
      try {
        const { kakao } = window;
        const position = new kakao.maps.LatLng(37.450805, 127.128954);

        window.map = new kakao.maps.Map(mapContainer.current, {
          center: position,
          level: 4,
        });

        // 마커 그리기
        const markerOptions = { clickable: true };
        const markers = Object.entries(ConzoneCoordinates).map(
          ([name, coord]) => {
            const markerPosition = new kakao.maps.LatLng(coord.lat, coord.lng);
            const marker = new kakao.maps.Marker({
              position: markerPosition,
              clickable: markerOptions.clickable,
            });

            marker.setMap(window.map);

            return { name, marker };
          }
        );

        // 인포윈도우 설정
        const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

        markers.forEach(({ name, marker }) => {
          kakao.maps.event.addListener(marker, "click", () => {
            infowindow.setContent(name);
            infowindow.open(window.map, marker);
          });
        });

        // 경로 그리기
        const linePath = Object.values(ConzoneRoute).map(
          ({ lat, lng }) => new kakao.maps.LatLng(lat, lng)
        );

        const polyline = new kakao.maps.Polyline({
          path: linePath,
          strokeWeight: 5,
          strokeColor: "red",
          strokeOpacity: 0.7,
          strokeStyle: "solid",
        });

        polyline.setMap(window.map);
      } catch (error) {
        console.error("지도 초기화 오류: ", error);
      }
    };

    initializeMap();
  }, [startLocation, endLocation]);

  return <div className="map-container" ref={mapContainer}></div>;
};

export default Map;
