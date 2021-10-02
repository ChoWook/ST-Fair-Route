package com.example.myfirstgooglemap;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng center;
    private Button btn_search;
    private ImageButton imgbtn_no, imgbtn_disabled, imgbtn_smoke;
    private RelativeLayout layout_slide_up;
    private LinearLayout layout_bottom_btns;
    private SlidingUpPanelLayout layout_slide;
    private AutoCompleteTextView autotext_building;
    private ArrayAdapter<String> stringadt_building;
    private ArrayList<Marker> markers_disabled, markers_smoking,markers_building;

    private static final int NODE = 43; // 노드(학교 장소) 갯수
    public static Vertex[] vertex = new Vertex[NODE]; // vertex 객체배열

    private static final double[] DISABLED_PARKING_POINTS = {
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

    private static final double[] SMOKING_AREA_POINTS ={
            37.630713, 127.081326,  // 무궁관
            37.629591, 127.080873,  // 미래관
            37.631827, 127.082108,  // 혜성관
            37.634577, 127.080723,  // 테크노파크
            37.633451, 127.079107,  // 파워플랜트
            37.634243, 127.077463,  // 1학
            37.633387, 127.076363,  // 도서관
            37.635256, 127.077838,  // 어학원
            37.635253, 127.075544,  // 성림학사
            37.636265, 127.076488,  // 불암학사
            37.632136, 127.076447,  // 하이테크관
            37.631622, 127.075835,  // 프론티어관
            37.630627, 127.077733,  // 100주년기념관
            37.629124, 127.079813,  // 체육관
            37.630542, 127.080306  // 아름관
    };

    private static final double[] BUILDING_POINTS = {
            37.631670, 127.080185, // 1 대학본부 (0)
            37.632083, 127.077907,// 2 다산관 (1)
            37.632073, 127.079401, // 3 창학관 (2)
            37.631573, 127.080996,// 4 제2창업보육센터 (3)
            37.631490, 127.081943, // 5 혜성관 (4)
            37.633215, 127.080681, // 6 청운관 (5)
            37.634341, 127.080179, // 7 서울테크노파크 (6)
            37.634656, 127.079290, // 8 창조관 (7)
            37.633497, 127.079171, // 10 파워플랜트 (8)
            37.633063, 127.077541, // 11 붕어방 (9)
            37.631048, 127.077536, // 13 정문 (10)
            37.635286, 127.077908, // 14 도예실습실 (11)
            37.630817, 127.076288, // 30 Seoultech어린이집 (12)
            37.630873, 127.076016, // 31 창업보육센터 (13)
            37.631261, 127.076161, // 32 프론티어관 (14)
            37.632028, 127.076450, // 33 하이테크관 (15)
            37.633233, 127.076792, // 34 중앙도서관 (16)
            37.633569, 127.076849, // 35 중앙도서관 별관 (17)
            37.633240, 127.077094, // 36 수연관 (18)
            37.634035, 127.076884, // 37 제1학생회관 (19)
            37.634756, 127.077449, // 38 국제관 (20)
            37.634569, 127.078115, // 39 다빈치관 (21)
            37.635201, 127.076831, // 40 어의관 (22)
            37.636197, 127.076135, // 41 불암학사 (23)
            37.635711, 127.076275, // 42 KB학사 (24)
            37.635202, 127.075995, // 43 성림학사 (25)
            37.636282, 127.075501, // 44 협동문 (26)
            37.635967, 127.076942, // 45 수림학사 (27)
            37.634729, 127.077126, // 46 누리학사 (28)
            37.630919, 127.078807, // 51 100주년기념관 (29)
            37.630850, 127.079550, // 52 제2학생회관 (30)
            37.630884, 127.079699, // 53 상상관 (31)
            37.630040, 127.080653, // 54 아름관 (32)
            37.629373, 127.079612, // 55 체육관 (33)
            37.629191, 127.080350, // 56 대륙관 (34)
            37.630929, 127.081577, // 57 무궁관 (35)
            37.628666, 127.081019, // 59 학군단 (36)
            37.629363, 127.081379, // 60 미래관 (37)
            37.631405, 127.082823, // 61 창의문(후문) (38)
            37.629697, 127.079441, // 62 테크노큐브 (39)
            37.629667, 127.078790, // 63 축구장 (40)
            37.631059, 127.077574, // 교차로 1 (41)
            37.631247, 127.079555 // 교차로 2 (42)
    };

    private static final int BUILDING_POINTS_SIZE = 40 * 2; // 교차로 제외 건물 수 * (위도 + 경도 = 2)

    private static final String[] BUILDING_NAMES = new String[] {
      "미래관", "창학관", "테크노 큐브"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //LatLng 값 할당
        center = new LatLng(37.632503, 127.078933);

        // 뷰 할당
        imgbtn_no  = mapFragment.getView().findViewById(R.id.btn_no);
        imgbtn_disabled = mapFragment.getView().findViewById(R.id.btn_disabled);
        imgbtn_smoke = mapFragment.getView().findViewById(R.id.btn_smoke);
        autotext_building = findViewById(R.id.autotext_building);
        btn_search = findViewById(R.id.btn_search);
        layout_slide_up = findViewById(R.id.layout_slide_up);
        layout_bottom_btns = findViewById(R.id.layout_bottom_btns);
        layout_slide = findViewById(R.id.layout_slide);
        layout_slide.setPanelHeight(0);

        //어뎁터 할당
        stringadt_building = new ArrayAdapter<String>(mapFragment.getContext(), android.R.layout.simple_dropdown_item_1line, BUILDING_NAMES);
        autotext_building.setAdapter(stringadt_building);

        // 리스트 초기화
        markers_disabled = new ArrayList<>();
        markers_smoking = new ArrayList<>();
        markers_building = new ArrayList<>();

        // 클릭 리스너 설정
        imgbtn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        imgbtn_disabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleMarkersVisibility(markers_disabled);
            }
        });

        imgbtn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleMarkersVisibility(markers_smoking);
            }
        });

        autotext_building.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 자동완성 클릭 시 키보드 숨기기
                HideKeyboard();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = autotext_building.getText().toString();

                for(String str : BUILDING_NAMES){
                    if(str.equals(text)){
                        HideKeyboard();
                        layout_slide.setPanelHeight(300);
                        layout_bottom_btns.setVisibility(View.GONE);

                        // TODO 검색어에 따라서 패널에 정보 추가 하기
                        
                        
                        break;
                    }
                }

            }
        });

    }

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


        // 장애인 주차 구역 마커 설정
        BitmapDescriptor bitmap_disabled = GetBitmapDescriptor(R.drawable.ic_mark_parking, 70);
        for(int i = 0; i < DISABLED_PARKING_POINTS.length; i+=2){
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(DISABLED_PARKING_POINTS[i], DISABLED_PARKING_POINTS[i+1])));
            m.setIcon(bitmap_disabled);
            m.setVisible(false);
            markers_disabled.add(m);
        }

        // 흡연 구역 마커 설정
        BitmapDescriptor bitmap_smoking = GetBitmapDescriptor(R.drawable.ic_mark_smoking, 70);
        for(int i = 0; i < SMOKING_AREA_POINTS.length; i+=2){
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(SMOKING_AREA_POINTS[i], SMOKING_AREA_POINTS[i+1])));
            m.setIcon(bitmap_smoking);
            m.setVisible(false);
            markers_smoking.add(m);
        }

        // 건물 마커 설정
        for(int i = 0; i < BUILDING_POINTS_SIZE; i+=2){
            markers_building.add(mMap.addMarker(new MarkerOptions().position(new LatLng(BUILDING_POINTS[i], BUILDING_POINTS[i+1]))));
        }
    }

    private void ToggleMarkersVisibility(ArrayList<Marker> markers){
        int size = markers.size();
        if(markers.get(0).isVisible()){    // 마커가 표시 중이라면 보이지 않게 만들기
            for(int i = 0; i < size; i++){
                markers.get(i).setVisible(false);
            };
        }
        else{                                       // 마커가 표시 중이 아니라면 보이게 만들기
            for(int i = 0; i < size; i++){
                markers.get(i).setVisible(true);
            };
        }
    }

    private BitmapDescriptor GetBitmapDescriptor(@DrawableRes int id, int size) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth() * size / 100,
                vectorDrawable.getIntrinsicHeight() * size / 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void setVertex() throws IOException{
        File vertexFile = new File("vertex.txt"); // 한줄씩 위도, 경도, 건물번호, 이름

        // 해당 파일이 없을 경우, 예외처리s
        if (!vertexFile.exists()) {
            System.out.println("vertex 파일이 존재하지 않습니다.");
            System.exit(2);
        }

        Scanner input = new Scanner(vertexFile);
        int vertexNum = 0;

        // path문서의 인접노드와 거리값을 SpotList에 저장합니다.
        // 하... vertex class도 get/set 메서드 만들어야하나..
        while(input.hasNext()){
            StringTokenizer st = new StringTokenizer(input.nextLine());

            vertex[vertexNum].latitude = Double.parseDouble(st.nextToken());
            vertex[vertexNum].longitude = Double.parseDouble(st.nextToken());
            vertex[vertexNum].id = Integer.parseInt(st.nextToken());
            vertex[vertexNum].name = st.nextToken();
            vertex[vertexNum].name_eng = st.nextToken();
            vertex[vertexNum].is_smoke = Integer.parseInt(st.nextToken()) == 1;
            vertex[vertexNum].is_disabled = Integer.parseInt(st.nextToken()) == 1;
            vertex[vertexNum].is_slope = Integer.parseInt(st.nextToken()) == 1;

            vertexNum++;
        }
        input.close();
    }

    private void HideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}