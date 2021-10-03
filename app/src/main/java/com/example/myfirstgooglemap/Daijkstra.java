package com.example.myfirstgooglemap;
import static java.lang.Boolean.TRUE;

import android.content.Context;

import java.io.*;
import java.util.*;

public class Daijkstra {
    private static final int INFINITE = 1000000; // 다익스트라 알고리즘을 위한 초기값
    private static int[] dist; // 가중치(거리)
    private static final int NODE = 43; // 노드(학교 장소) 갯수
    private static int[] parent; // 경로 저장
    private static int spotCnt; // 경로를 지난 노드 수

    private int startNode; // 출발지
    private int endNode; // 목적지
    private String pathNode; // 경로
    private int meter; // 총 거리
    private int trableTime; // 가는 데까지 예상 필요시간

    // 외부에서 인스턴스 생성을 막기 위한 private 생성자
    private Daijkstra() {
    }

    // 싱글톤 패턴
    private static class Singleton{
        private static final Daijkstra INSTANCE = new Daijkstra();
    }

    // 다익스트라 유일 인스턴스 get Method
    public static Daijkstra getInstance(int start, int end){
        Singleton.INSTANCE.startNode = start;
        Singleton.INSTANCE.endNode = end;
        return Singleton.INSTANCE;
    }

    public void calDaijkstra(Context context, boolean disabled, ArrayList<Vertex> vertex) throws IOException {

        // 장애유무에 따라 간선정보를 다르게 입력합니다.
        InputStream is;
        if (disabled == TRUE)
            is = context.getResources().openRawResource(R.raw.edge_disabled);
        else
            is = context.getResources().openRawResource(R.raw.edge);

        // 간선정보를 입력할 인접리스트
        ArrayList<Spot>[] SpotList;
        SpotList = new ArrayList[NODE + 1];
        for(int i = 0; i < NODE + 1; i++){
            SpotList[i] = new ArrayList<>();
        }

        // 인접리스트 초기화
        for(int i = 1 ; i <= NODE; i++){
            SpotList[i] = new ArrayList<>();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] st = line.split(" ");

            int start = Integer.parseInt(st[0]);
            int end = Integer.parseInt(st[1]);
            int cost = vertex.get(start).calDistance(vertex.get(end));

            SpotList[start].add(new Spot(end, cost));
            SpotList[end].add(new Spot(start, cost));
        }

        // 최단경로를 저장한다.
        dist = new int[NODE + 1];
        // 경로 추적을 위한 부모노드 정보를 저장한다.
        parent = new int[NODE + 1];
        //거리를 무한대로 초기화
        Arrays.fill(dist, INFINITE);
        spotCnt = 0;

        // 다익스트라를 이용하여 최단거리를 구합니다.
        PriorityQueue<Spot> pq = new PriorityQueue<>();
        boolean[] check = new boolean[NODE + 1];

        pq.add(new Spot(startNode, 0));
        dist[startNode] = 0;

        while(!pq.isEmpty()){
            Spot curSpot = pq.poll();
            int cur = curSpot.end;

            if(check[cur]) continue;
            check[cur] = true;

            for(Spot Spot : SpotList[cur]){
                if(dist[Spot.end] > dist[cur] + Spot.cost){
                    dist[Spot.end] = dist[cur] + Spot.cost;
                    pq.add(new Spot(Spot.end, dist[Spot.end]));
                    parent[Spot.end] = cur;
                }
            }
        }

        Stack<Integer> stack = searchPath(startNode, endNode);

        StringBuilder sb = new StringBuilder();
        while(!stack.isEmpty()){
            int spot = stack.pop();
            sb.append(spot + " ");
        }

        meter = dist[endNode];
        pathNode = sb.toString();
        trableTime = (int)(meter/1.1); // 초속 1.1m/s로 계산했을 때
    }

    public static Stack<Integer> searchPath(int startNode, int endNode){
        Stack<Integer> stack = new Stack<>();
        int cur = endNode;

        while(cur != startNode) {
            stack.push(cur);
            spotCnt++;

            cur = parent[cur];
        }
        stack.push(cur);
        spotCnt++;

        return stack;
    }

    public int getMeter() {
        return meter;
    }
    public String getPathNode() { return pathNode; }
    public int getNodeCount() {
        return spotCnt;
    }
    public int getTime() {
        return trableTime;
    }
}
