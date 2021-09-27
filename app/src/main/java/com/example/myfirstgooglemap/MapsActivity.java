package com.example.myfirstgooglemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myfirstgooglemap.databinding.ActivityMapsBinding;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ImageButton imgbtn_no, imgbtn_disabled, imgbtn_smoke;

    private double disabledParkingPoints[] = {
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
    private LatLng center;

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


        for(int i = 0; i < disabledParkingPoints.length; i+=2){
            mMap.addMarker(new MarkerOptions().position(new LatLng(disabledParkingPoints[i], disabledParkingPoints[i+1])));
        }
    }
}