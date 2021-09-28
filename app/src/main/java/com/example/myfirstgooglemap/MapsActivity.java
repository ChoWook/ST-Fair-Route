package com.example.myfirstgooglemap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private ActivityMapsBinding binding;
    private LatLng center;
    private ImageButton imgbtn_no, imgbtn_disabled, imgbtn_smoke;
    private AutoCompleteTextView autotext_building;
    private ArrayAdapter<String> stringadt_building;

    private static final double DISABLED_PARKING_POINTS[] = {
            37.635782, 127.076478,   // 성림학사
        37.635043, 127.076773,  // 어의관
        37.635024, 127.077384,  // 어학원
        37.635181, 127.077881,  // 다빈치관
        37.635418, 127.078644,  // 다반치관 뒤
        37.634893, 127.078727,   // 다빈치관 2
        37.632736, 127.076647,   // 도서관
        37.631298, 127.076540,   // 하이테크관
        37.630884, 127.076612,   // 하이테크관 2
        37.631987, 127.079457,   // 창학관
        37.630690, 127.079890,   // 상상관
        37.630654, 127.080113,   // 상상관 2
        37.629943, 127.079822,   // 테크노 큐브브
        37.629418, 127.079333,   // 체육관
        37.634616, 127.079499,   // 창조관
        37.634180, 127.081226,   // 테크노파크
        37.634395, 127.081159,   // 테크노파크 2
        37.633069, 127.080874,   // 청운관
        37.632762, 127.079377,   // 창학관
        37.631430, 127.080446,   // 대학본부
        37.631472, 127.080906,   // 대학본부 2
        37.630063, 127.081388,   // 미래관
        37.628661, 127.081079   // 학군단
    };

    private static final double BUILDING_POINTS[] = {
            37.631670, 127.080185, // 1 대학본부
            37.632083, 127.077907,// 2 다산관
            37.632073, 127.079401, // 3 창학관
            37.631573, 127.080996,// 4 제2창업보육센터
            37.631490, 127.081943, // 5 혜성관
            37.633215, 127.080681, // 6 청운관
            37.634341, 127.080179, // 7 서울테크노파크
            37.634656, 127.079290, // 8 창조관
            37.633497, 127.079171, // 10 파워플랜트
            37.633063, 127.077541, // 11 붕어방
            37.631048, 127.077536, // 13 정문
            37.635286, 127.077908, // 14 도예실습실
            37.630817, 127.076288, // 30 Seoultech어린이집
            37.630873, 127.076016, // 31 창업보육센터
            37.631261, 127.076161, // 32 프론티어관
            37.632028, 127.076450, // 33 하이테크관
            37.633233, 127.076792, // 34 중앙도서관
            37.633569, 127.076849, // 35 중앙도서관 별관
            37.633240, 127.077094, // 36 수연관
            37.634035, 127.076884, // 37 제1학생회관
            37.634756, 127.077449, // 38 국제관
            37.634569, 127.078115, // 39 다빈치관
            37.635201, 127.076831, // 40 어의관
            37.636197, 127.076135, // 41 불암학사
            37.635711, 127.076275, // 42 KB학사
            37.635202, 127.075995, // 43 성림학사
            37.636282, 127.075501, // 44 협동문
            37.635967, 127.076942, // 45 수림학사
            37.634729, 127.077126, // 46 누리학사
            37.630919, 127.078807, // 51 100주년기념관
            37.630850, 127.079550, // 52 제2학생회관
            37.630884, 127.079699, // 53 상상관
            37.630040, 127.080653, // 54 아름관
            37.629373, 127.079612, // 55 체육관
            37.629191, 127.080350, // 56 대륙관
            37.630929, 127.081577, // 57 무궁관
            37.628666, 127.081019, // 59 학군단
            37.629363, 127.081379, // 60 미래관
            37.631405, 127.082823, // 61 창의문(후문)
            37.629697, 127.079441, // 62 테크노큐브
            37.629667, 127.078790// 63 축구장
    };

    private static final String[] BUILDING_NAMES = new String[] {
      "미래관", "창학관", "테크노 큐브"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //LatLng 값 할당
        center = new LatLng(37.632503, 127.078933);

        // 뷰 할당
        imgbtn_no  = mapFragment.getView().findViewById(R.id.btn_no);
        imgbtn_disabled = mapFragment.getView().findViewById(R.id.btn_disabled);
        imgbtn_smoke = mapFragment.getView().findViewById(R.id.btn_smoke);
        autotext_building = mapFragment.getView().findViewById(R.id.autotext_building);

        //어뎁터 할당
        stringadt_building = new ArrayAdapter<String>(mapFragment.getContext(), android.R.layout.simple_dropdown_item_1line, BUILDING_NAMES);
        autotext_building.setAdapter(stringadt_building);


        // 버튼 클릭 리스너 설정
        imgbtn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        imgbtn_disabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgbtn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 카메라 위치 변경 및 축소/확대 제한
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(18);

        // 지도 범위 제한
        LatLngBounds adelaideBounds = new LatLngBounds(
                new LatLng(37.630708, 127.077063), // SW bounds
                new LatLng(37.634123, 127.080401)  // NE bounds
        );
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds);


        // 장애인 주차 구역
        for(int i = 0; i < DISABLED_PARKING_POINTS.length; i+=2){
            //mMap.addMarker(new MarkerOptions().position(new LatLng(DISABLED_PARKING_POINTS[i], DISABLED_PARKING_POINTS[i+1])));
        }

        // 건물
        for(int i = 0; i < BUILDING_POINTS.length; i+=2){
            mMap.addMarker(new MarkerOptions().position(new LatLng(BUILDING_POINTS[i], BUILDING_POINTS[i+1])));
        }
    }
}