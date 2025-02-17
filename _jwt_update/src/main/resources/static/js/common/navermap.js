// Naver Map API
var dmap = null;
var fmap = null;

function initDMap() {
    var mapElement1 = document.getElementById('dmap');
    if (mapElement1) {  // dmap이 존재하면 초기화
        dmap = new naver.maps.Map(mapElement1, {
            center: new naver.maps.LatLng(37.497936, 127.027600), // 강남역 좌표
            zoom: 15 // 초기 확대 수준
        });

        var marker1 = new naver.maps.Marker({
            position: new naver.maps.LatLng(37.497936, 127.053600),
            map: dmap,
            title: '강남역'
        });
        dmap.setCenter(marker1.getPosition());
        dmap.setZoom(17);
    }
}

function initFMap() {
    var mapElement2 = document.getElementById('fmap');
    if (mapElement2) {  // fmap이 존재하면 초기화
        fmap = new naver.maps.Map(mapElement2, {
            center: new naver.maps.LatLng(37.497936, 127.027600), // 강남역 좌표
            zoom: 15 // 초기 확대 수준
        });

        var marker2 = new naver.maps.Marker({
            position: new naver.maps.LatLng(37.497936, 127.053600),
            map: fmap,
            title: '서울 시청'
        });
        fmap.setCenter(marker2.getPosition());
        fmap.setZoom(17);
    }
}

// 페이지 로드 시 각 지도 초기화 호출
window.onload = function() {
    initDMap();
    initFMap();
};
