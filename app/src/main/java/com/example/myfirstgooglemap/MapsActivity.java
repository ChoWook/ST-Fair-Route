package com.example.myfirstgooglemap;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import static java.lang.Boolean.FALSE;

import java.util.ArrayList;
import java.io.*;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    // View 선언
    private Button btn_call;
    private ImageButton imgbtn_slope, imgbtn_disabled, imgbtn_smoke, imgbtn_find_route, imgbtn_search, imgbtn_find_route_daijkstra;
    private RelativeLayout layout_slide_up;
    private LinearLayout layout_bottom_btns, layout_search_line_1, layout_search_line_2, layout_search_line_3;
    private SlidingUpPanelLayout layout_slide;
    private AutoCompleteTextView autotext_building, autotext_building_from, autotext_building_to;
    private TextView text_building_no, text_building_name, text_building_name_eng, text_path, text_meter;
    private ImageView img_search, img_building_photo_1, img_building_photo_2, img_smoke, img_disabled, img_slope;
    private CheckBox Ckbox_stair;

    // 변수 선언
    private GoogleMap mMap;
    private LatLng center;
    private ArrayAdapter<String> stringadt_building;
    private ArrayList<Marker> markers_disabled, markers_smoking,markers_building, markers_slope;
    private ArrayList<Polyline> polylines;

    private static final int NODE = 43; // 노드(학교 장소) 갯수
    private static ArrayList<Vertex> vertex = new ArrayList<>(NODE); // vertex 객체배열

    private static final double[] SLOPE_POINTS = {
            37.628640, 127.081260,  // 학군단
            37.629383, 127.081671,  // 미래관
            37.631543, 127.082077,  // 혜성관
            37.631573, 127.081007,  // 제2창업보육센터
            37.631646, 127.080338,  // 대학본부 1
            37.631708, 127.080088,  // 대학본부 2
            37.632142, 127.079232,  // 창학관
            37.634287, 127.080421,  // 테크노파크
            37.634662, 127.079378,  // 창조관
            37.635203, 127.079022,  // 도예관
            37.636195, 127.076091,  // 불암학사
            37.636000, 127.076214,  // KB 1
            37.636068, 127.075887,  // kB 2
            37.635173, 127.076778,  // 어의관
            37.634795, 127.077352,  // 어학원
            37.634025, 127.076964,  // 1학
            37.633551, 127.076873,  // 도서관 별관
            37.633259, 127.076691,  // 도서관
            37.631255, 127.076059,  // 프론티어관
            37.630850, 127.078876,  // 100주년 기념관
            37.630828, 127.079517,  // 2학생회관
            37.629654, 127.080230,  // 아름관
            37.629409, 127.079404,  // 체육관
            37.629217, 127.080281,  // 대륙관 1
            37.629190, 127.080429   // 대륙관 2
    };

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

    private static ArrayList<String> BUILDING_NAMES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //LatLng 값 할당
        center = new LatLng(37.632503, 127.078933);

        //polylineOptions.color(Color.RED);

        // 뷰 할당
        btn_call = findViewById(R.id.btn_call);
        imgbtn_slope = mapFragment.getView().findViewById(R.id.btn_wheel);
        imgbtn_disabled = mapFragment.getView().findViewById(R.id.btn_disabled);
        imgbtn_smoke = mapFragment.getView().findViewById(R.id.btn_smoke);
        imgbtn_find_route = findViewById(R.id.btn_find_route);
        imgbtn_find_route_daijkstra = findViewById(R.id.btn_find_route_daijkstra);
        autotext_building = findViewById(R.id.autotext_building);
        autotext_building_from = findViewById(R.id.autotext_building_from);
        autotext_building_to = findViewById(R.id.autotext_building_to);
        imgbtn_search = findViewById(R.id.btn_search);
        layout_slide_up = findViewById(R.id.layout_slide_up);
        layout_bottom_btns = findViewById(R.id.layout_bottom_btns);
        layout_slide = findViewById(R.id.layout_slide);
        layout_slide.setPanelHeight(0);
        text_building_no = findViewById(R.id.bd_number);
        text_building_name = findViewById(R.id.bd_name);
        text_building_name_eng = findViewById(R.id.bd_eng_name);
        text_meter = findViewById(R.id.text_meter);
        text_path = findViewById(R.id.text_path);
        img_search = findViewById(R.id.img_search);
        img_building_photo_1 = findViewById(R.id.bd_photo_1);
        img_building_photo_2 = findViewById(R.id.bd_photo_2);
        img_smoke = findViewById(R.id.img_smoke);
        img_disabled = findViewById(R.id.img_disabled);
        img_slope = findViewById(R.id.img_slope);
        layout_search_line_1 = findViewById(R.id.search_line_1);
        layout_search_line_2 = findViewById(R.id.search_line_2);
        layout_search_line_2.setVisibility(View.INVISIBLE);
        layout_search_line_3 = findViewById(R.id.search_line_3);
        layout_search_line_3.setVisibility(View.INVISIBLE);
        Ckbox_stair = findViewById(R.id.Ckbox_stair);

        //어뎁터 할당
        stringadt_building = new ArrayAdapter<String>(mapFragment.getContext(), android.R.layout.simple_dropdown_item_1line, BUILDING_NAMES);
        autotext_building.setAdapter(stringadt_building);
        autotext_building_from.setAdapter(stringadt_building);
        autotext_building_to.setAdapter(stringadt_building);

        // 리스트 초기화
        markers_disabled = new ArrayList<>();
        markers_smoking = new ArrayList<>();
        markers_building = new ArrayList<>();
        markers_slope = new ArrayList<>();
        vertex = new ArrayList<>();
        polylines = new ArrayList<>();

        // Vertex 초기화
        try {
            setVertex();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 클릭 리스너 설정
        imgbtn_slope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleMarkersVisibility(markers_slope);
                if(markers_slope.get(0).isVisible()){
                    imgbtn_slope.setImageResource(R.drawable.btn_wheel_color);
                }
                else{
                    imgbtn_slope.setImageResource(R.drawable.btn_wheel_gray);
                }
            }
        });
        
        imgbtn_disabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleMarkersVisibility(markers_disabled);
                if(markers_disabled.get(0).isVisible()){
                    imgbtn_disabled.setImageResource(R.drawable.btn_park_color);
                }
                else{
                    imgbtn_disabled.setImageResource(R.drawable.btn_park_gray);
                }
            }
        });

        imgbtn_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleMarkersVisibility(markers_smoking);
                if(markers_smoking.get(0).isVisible()){
                    imgbtn_smoke.setImageResource(R.drawable.btn_smoke_color);
                }
                else{
                    imgbtn_smoke.setImageResource(R.drawable.btn_smoke_gray);
                }
            }
        });

        autotext_building.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 자동완성 클릭 시 키보드 숨기기
                HideKeyboard();
            }
        });

        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayBuildingInfo();
            }
        });


        imgbtn_find_route_daijkstra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 출발지랑 도착지 넣는 텍스트 뷰에서 값 가져오기
                int start = convert(autotext_building_from.getText().toString()); // 선택한 출발지를 객체배열의 고유번호로 바꿔줍니다.
                int end = convert(autotext_building_to.getText().toString()); // 선택한 도착지를 객체배열의 고유번호로 바꿔줍니다.
                initPolylines();
                if(start == -1 || end == -1) return;

                HideKeyboard();
                Daijkstra path = Daijkstra.getInstance(start, end); // 다익스트라 singleton 객체 Path를 생성합니다.

                try {
                    setVertex(); // vertex 초기화
                    path.calDaijkstra(getApplicationContext(), !Ckbox_stair.isChecked(), vertex); // 경로 계산
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String[] pathNode = path.getPathNode().split(" ", 0); // 계산된 경로(String)을 연산을 위해 배열로 만듭니다.

                // 캠퍼스지도상에 그릴 polyLine객체를 pathNode를 토대로 생성합니다.
                for (int i = 1; i < pathNode.length; i++) {
                    int preVertexNum = Integer.parseInt(pathNode[i-1]);
                    int postVertexNum = Integer.parseInt(pathNode[i]);
                    // polyline 그리는 코드
                    polylines.add(mMap.addPolyline((new PolylineOptions()).add(new LatLng(vertex.get(preVertexNum).latitude, vertex.get(preVertexNum).longitude),
                            new LatLng(vertex.get(postVertexNum).latitude, vertex.get(postVertexNum).longitude)).width(5).color(Color.RED)));
                }

                // 건물 고유숫자로 된 경로를 건물명으로 바꿉니다.
                String pathInfo = "";
                for (int i = 0; i < pathNode.length; i++) {
                    pathInfo += vertex.get(Integer.parseInt(pathNode[i])).name;
                    if (i == pathNode.length - 1)
                        break;
                    pathInfo += " -> ";
                }

                text_path.setText(pathInfo); // 경로 표시
                text_meter.setText(path.getMeter() + "m"); // 거리 표시

                layout_search_line_3.setVisibility(View.VISIBLE);
            }
        });

        imgbtn_find_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickFindRouteBtn();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickFindRouteBtn();
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

        // 경사로 마커 설정
        BitmapDescriptor bitmap_slope = GetBitmapDescriptor(R.drawable.ic_mark_wheel, 70);
        for(int i = 0; i < SLOPE_POINTS.length; i+=2){
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(SLOPE_POINTS[i], SLOPE_POINTS[i+1])));
            m.setIcon(bitmap_slope);
            m.setVisible(false);
            markers_slope.add(m);
        }

        // 건물 마커 설정
        BitmapDescriptor bitmap_building = GetBitmapDescriptor(R.drawable.ic_mark_building, 60);
        for(int i = 0; i < BUILDING_POINTS_SIZE; i+=2){
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(BUILDING_POINTS[i], BUILDING_POINTS[i+1])));
            m.setIcon(bitmap_building);
            m.setTag(vertex.get(i/2).name);
        }

        mMap.setOnMarkerClickListener(this);
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

    public void setVertex() throws IOException{
        try {
            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.vertex);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] st = line.split(" ");
                st[4] = st[4].replace("_", " ");
                for(int i = 2; i < 5; i++){
                    BUILDING_NAMES.add(st[i]);
                }

                Vertex v = new Vertex(Double.parseDouble(st[0]),
                        Double.parseDouble(st[1]),
                        Integer.parseInt(st[2]),
                        st[3],
                        st[4],
                        Integer.parseInt(st[5]) == 1,
                        Integer.parseInt(st[6]) == 1,
                        Integer.parseInt(st[7]) == 1,
                        st[8]
                        );
                vertex.add(v);

            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void HideKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int convert(String spot) {
        int result = -1; // 못 찾았을 경우, -1로 반환합니다.

        // 건물이름으로 찾습니다.
        for(int i = 0; i < NODE; i++) {
            if(spot.equals(vertex.get(i).name) | spot.equals(vertex.get(i).name_eng) | spot.equals(Integer.toString(vertex.get(i).id))) {
                return i;
            }
        }
        return result;
    }

    private void ClickFindRouteBtn(){
        HideKeyboard();
        layout_slide.setPanelHeight(0);
        layout_search_line_1.setVisibility(View.INVISIBLE);
        layout_search_line_2.setVisibility(View.VISIBLE);
        autotext_building_to.setText(autotext_building.getText());
        autotext_building.setText("");
    }

    private void DisplayBuildingInfo(){
        String text = autotext_building.getText().toString();
        int index;
        Context context = getApplicationContext();
        if((index = convert(text)) != -1){
            HideKeyboard();
            layout_slide.setPanelHeight(400);

            // vertex 에서 정보를 가져와 View 값 변경
            layout_bottom_btns.setVisibility(View.GONE);
            text_building_no.setText("No. " + vertex.get(index).id);
            text_building_name.setText(vertex.get(index).name);
            text_building_name_eng.setText(vertex.get(index).name_eng);
            int id = context.getResources().getIdentifier("photo_"+vertex.get(index).id + "_1", "drawable", context.getPackageName());
            img_building_photo_1.setImageResource(id);
            id = context.getResources().getIdentifier("photo_"+vertex.get(index).id + "_2", "drawable", context.getPackageName());
            img_building_photo_2.setImageResource(id);
            if(vertex.get(index).is_smoke){
                img_smoke.setImageResource(R.drawable.ic_smoke_color);
            }
            else{
                img_smoke.setImageResource(R.drawable.ic_smoke_gray);
            }
            if(vertex.get(index).is_disabled){
                img_disabled.setImageResource(R.drawable.ic_park_color);
            }
            else{
                img_disabled.setImageResource(R.drawable.ic_park_gray);
            }
            if(vertex.get(index).is_slope){
                img_slope.setImageResource(R.drawable.ic_wheel_color);
            }
            else{
                img_slope.setImageResource(R.drawable.ic_wheel_gray);
            }
            btn_call.setText(vertex.get(index).call);


            // 카메라 줌 인
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(vertex.get(index).latitude,  vertex.get(index).longitude)));
        }
    }

    public void initDisplay(){
        HideKeyboard();
        layout_slide.setPanelHeight(0);
        layout_search_line_1.setVisibility(View.VISIBLE);
        layout_search_line_2.setVisibility(View.INVISIBLE);
        layout_search_line_3.setVisibility(View.INVISIBLE);
        layout_bottom_btns.setVisibility(View.VISIBLE);
        initPolylines();
    }

    public void initPolylines(){
        for(Polyline p : polylines){
            p.remove();
        }
    }

    @Override
    public void onBackPressed() {
        if(layout_slide.getPanelHeight() != 0 || layout_search_line_2.getVisibility() == View.VISIBLE){
            initDisplay();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String tag = (String) marker.getTag();
        if(tag != null){
            autotext_building.setText(tag);
            initDisplay();
            DisplayBuildingInfo();
        }

        return false;
    }

}