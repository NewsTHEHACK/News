package com.example.hy.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapter.ListViewAdapter;
import fragment.News_frag;
import object.News_Point;
import object.News_item;
import object.sstatic;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    private LatLng latLng;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    SlidingDrawer sd;
    private List<News_Point> data=new ArrayList<>();
    private List<Polyline> datatrue=new ArrayList<>();
    private com.alibaba.fastjson.JSONArray informations=new com.alibaba.fastjson.JSONArray();
    private ListViewAdapter adapter;
    BDLocation m;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    Polyline mPolyline;
    private ListView listView;
    private List<News_item> listviewdata=new ArrayList<>();
    private boolean isFirstLoc = true; // 是否首次定位
    private TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.activity_detail);
        requestPower();
        sd=(SlidingDrawer)findViewById(R.id.sliding);
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                listviewdata.clear();
            }
        });
        initView();
        initMap();

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                for (News_Point temp:data){
                    //Log.d("CLICK",latLng.latitude+" "+latLng.longitude+" "+(temp.getStartx()+temp.getEndx())/2+" "+(temp.getStarty()+temp.getEndy())/2);
                    if (temp.isin(latLng.latitude,latLng.longitude)){
                        News_item ni=new News_item(temp.tag,"100",null,temp.id);
                        listviewdata.add(ni);
                        adapter.setData(listviewdata);
                        sd.toggle();
                        break;
                    }
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mBaiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                Log.d("Line",polyline.toString());
                return false;
            }
        });
    }


    private void initMap() {
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        //默认显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图

        //mBaiduMap.setTrafficEnabled(true);
        //开启热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();
    }
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                m=location;
                SendTask task=new SendTask();
                task.execute();
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(Detail.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(Detail.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(Detail.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(Detail.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(Detail.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(Detail.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mymap);
        listView=(ListView)findViewById(R.id.detail_listview);
        adapter=new ListViewAdapter(this);
        adapter.setData(listviewdata);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(Detail.this,NewsChat.class);
                sstatic.id=((News_item)adapter.getItem(position)).getId();
                Log.d("sstatic",sstatic.id);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.button:
                //卫星地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.buttons:
                //普通地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
    }
    public class SendTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            Log.d("USRL",""+1);
            try {
                URL ServerUrl = new URL("http://steins.xin:3000/news/news_map?x=" + m.getLongitude() + "&y=" + m.getLatitude());

                HttpURLConnection conn;
                conn = (HttpURLConnection) ServerUrl.openConnection();
                conn.setConnectTimeout(13000);
                conn.setRequestMethod("GET");
                //conn.setDoOutput(true);
                conn.setDoInput(true);
                if (conn.getResponseCode()==200) {
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while ((line = reader.readLine()) != null)   sb.append(line);
                    String result = new String(sb);
                    informations= com.alibaba.fastjson.JSONArray.parseArray(result);
                    Log.d("info1111",""+informations.size());
                    return 1;
                }
                else return 2;
            } catch (Exception e) {
                e.printStackTrace();
                return 3;
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            for (Iterator tor = informations.iterator(); tor.hasNext(); ) {
                com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject) tor.next();
                try {
                    double x=0,y=0;
                    if (json.getString("x")!=null) x = Double.parseDouble(json.getString("x"));//经度
                    if(json.getString("y")!=null) y = Double.parseDouble(json.getString("y"));//纬度
                    News_Point np = new News_Point(y, x, y + 0.0001, x + 0.0001);
                    np.tag=json.getString("title");
                    np.id=json.getString("url");
                    data.add(np);
                    final LatLng p1 = new LatLng(np.getStartx(), np.getStarty());
                    LatLng p2 = new LatLng(np.getEndx(), np.getEndy());
                    List<LatLng> points = new ArrayList<>();
                    points.add(p1);
                    points.add(p2);

                    //绘制折线
                    OverlayOptions ooPolyline = new PolylineOptions().width(30)
                            .color(0xAAFF0000).points(points);
                    mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                    datatrue.add(mPolyline);
                }catch (Exception e){

                }
            }
        }
    }
}
