package com.example.webtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.map.BitmapDescriptorFactory.fromResource;
import static com.baidu.mapapi.map.MapStatusUpdateFactory.zoomTo;


public class MapMain extends BaseActivity implements View.OnClickListener {

    public LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean FIRST_LAUNCH = true, MAKE_MARK = false;
    private double longitude, latitude;
    private float radius;
    private StringBuilder currentPosition;
    private String location_data;
    private List<HelpEachOther> mhelpEachOtherList = new ArrayList<>();
    private static long lastTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAPTEST", "create");

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map_main);

        mapView = (MapView) findViewById(R.id.bmapView);
        positionText = (TextView) findViewById(R.id.detailLocation);
        ImageButton myLocation = (ImageButton) findViewById(R.id.myLocation);
        Button askHelp = (Button) findViewById(R.id.askHelp);
        Button giveHelp = (Button) findViewById(R.id.giveHelp);
        Button userInformation = (Button) findViewById(R.id.userInformation);
        Button contactWith=(Button) findViewById(R.id.contactWith);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarMap);
        setSupportActionBar(toolbar);

        mapView.getChildAt(2).setPadding(0, 0, 1080 - 180, 220);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        myLocation.setOnClickListener(this);
        askHelp.setOnClickListener(this);
        giveHelp.setOnClickListener(this);
        userInformation.setOnClickListener(this);
        contactWith.setOnClickListener(this);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //marker.setAlpha(0);
                infoWindowMake(marker);
                return false;
            }
        });

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapMain.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapMain.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapMain.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapMain.this, permissions, 1);
        } else {
            requestLocation();
        }

    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(1000);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setEnableSimulateGps(true);

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myLocation:
                toMyLocation();
                break;
            case R.id.askHelp:
                Intent intentAsk = new Intent(MapMain.this, HelpPage.class);
                intentAsk.putExtra("longitude_data", longitude);
                intentAsk.putExtra("latitude_data", latitude);
                intentAsk.putExtra("location_data", location_data);
                startActivity(intentAsk);
                break;
            case R.id.giveHelp:
                Intent intentGive = new Intent(MapMain.this, AcceptPage.class);
                startActivity(intentGive);
                break;
            case R.id.userInformation:
                Intent intentUser = new Intent(MapMain.this, UserPage.class);
                startActivity(intentUser);
                break;
            case R.id.contactWith:
                Intent intentContact = new Intent(MapMain.this, ContactPage.class);
                startActivity(intentContact);

                break;
            default:
                break;
        }
    }

    private void buildMyLocation() {
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();

        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        locationBuilder.accuracy(radius);
        MyLocationData locationData = locationBuilder.build();

        baiduMap.setMyLocationData(locationData);
    }

    public void toMyLocation() {
        LatLng latLng = new LatLng(latitude, longitude);
        MapStatusUpdate updateZoom = zoomTo(18f);
        baiduMap.animateMapStatus(updateZoom);
        MapStatusUpdate updateLocation = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(updateLocation);
        buildMyLocation();
    }

    public void clearMap() {
        baiduMap.clear();
    }

    public void makeMark() {
        baiduMap.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initMark();
            }
        }).start();

        Log.d("MarkNo", "" + mhelpEachOtherList.size());

        for (int i = 0; i < mhelpEachOtherList.size(); i++) {
            switch (mhelpEachOtherList.get(i).getHistory()) {
                case 0:
                    if (!mhelpEachOtherList.get(i).getReleaseUser().getUsername()
                            .equals(AVUser.getCurrentUser().getUsername())) {
                        LatLng latLng = new LatLng(mhelpEachOtherList.get(i).getLatitude()
                                , mhelpEachOtherList.get(i).getLongitude());
                        BitmapDescriptor bitmap =
                                fromResource(R.drawable.ic_person_pin_black_18dp_x);
                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(String.valueOf(i))
                                .icon(bitmap)
                                .animateType(MarkerOptions.MarkerAnimateType.grow);
                        baiduMap.addOverlay(options);
                    }
                    break;
                case 1:
                    if (mhelpEachOtherList.get(i).getAcceptUser().getUsername()
                            .equals(AVUser.getCurrentUser().getUsername())) {
                        LatLng latLng = new LatLng(mhelpEachOtherList.get(i).getLatitude()
                                , mhelpEachOtherList.get(i).getLongitude());
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.ic_layers_black_18dp_xxx);
                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(String.valueOf(i))
                                .icon(bitmap)
                                .animateType(MarkerOptions.MarkerAnimateType.grow);
                        baiduMap.addOverlay(options);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    private void initMark() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Log.d("initMark", "start");
                mhelpEachOtherList.clear();
                AVQuery<AVObject> avQuery = new AVQuery<>("Help");
                avQuery.whereLessThanOrEqualTo("history", 1);
                avQuery.orderByDescending("createdAt");
                avQuery.include("release");
                avQuery.include("accept");
                avQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                HelpEachOther helpEachOther = new HelpEachOther(null, null
                                        , null, null
                                        , 0, 0
                                        , 0, 0, null
                                        , null, null, null);
                                helpEachOther.setLatitude(list.get(i).getDouble("latitude"));
                                helpEachOther.setLongitude(list.get(i).getDouble("longitude"));
                                helpEachOther.setContent(list.get(i).getString("content"));
                                helpEachOther.setRemark(list.get(i).getString("remark"));
                                helpEachOther.setHistory(list.get(i).getInt("history"));
                                helpEachOther.setLocation(list.get(i).getString("location"));
                                helpEachOther.setReleaseUser(list.get(i).getAVUser("release"));
                                helpEachOther.setCreatedAt(list.get(i).getCreatedAt());
                                helpEachOther.setUpdatedAt(list.get(i).getUpdatedAt());
                                helpEachOther.setObjectIDnumber(list.get(i).getObjectId());
                                helpEachOther.setAcceptUser(list.get(i).getAVUser("accept"));
                                mhelpEachOtherList.add(helpEachOther);
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }

    private void infoWindowMake(final Marker marker) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Double latMark = marker.getPosition().latitude;
                Double longMark = marker.getPosition().longitude;
                LatLng latLng = new LatLng(latMark, longMark);

                final int num = Integer.valueOf(marker.getTitle());

                View view = LayoutInflater.from(MapMain.this).inflate(R.layout.info_window, null);
                TextView textView = (TextView) view.findViewById(R.id.textInfoWindow);
                Button button = (Button) view.findViewById(R.id.buttonInfoWindow);
                textView.setText(mhelpEachOtherList.get(num).getContent() + "  " + mhelpEachOtherList.get(num).getLocation());

                if (mhelpEachOtherList.get(num).getHistory()==1)
                {
                    button.setClickable(false);
                    button.setVisibility(View.GONE);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapMain.this, "接受请求", Toast.LENGTH_SHORT).show();
                        AVObject help = AVObject.createWithoutData("Help",
                                mhelpEachOtherList.get(num).getObjectIDnumber());
                        AVUser avUser = new AVUser();
                        help.put("accept", avUser.getCurrentUser());
                        help.put("history", 1);
                        help.saveInBackground();
                        baiduMap.clear();
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baiduMap.hideInfoWindow();
                    }
                });

                InfoWindow infoWindow = new InfoWindow(view, latLng, 0);
                baiduMap.showInfoWindow(infoWindow);
            }
        });
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final StringBuilder currentPosition = new StringBuilder();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    radius = location.getRadius();
                    location_data = location.getAddrStr();

                    currentPosition.append("精度：").append(location.getRadius()).append(" ");
                    currentPosition.append("纬度：").append(longitude).append(" ");
                    currentPosition.append("经线：").append(latitude).append("\n");
                    currentPosition.append("地址信息：").append("\n")
                            .append(location.getAddrStr()).append("\n");
                    currentPosition.append("定位方式：");
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS").append(" ");
                        currentPosition.append("卫星数目：").append(location.getSatelliteNumber()).append("\n");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }

                    if (FIRST_LAUNCH) {
                        toMyLocation();
                        makeMark();
                        FIRST_LAUNCH = false;
                    }

                    buildMyLocation();

                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    @Override
    public void onBackPressed()
    {
        long currentTime=System.currentTimeMillis();
        if (currentTime-lastTime<1000){
            super.onBackPressed();
        }else
        {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
        }
        lastTime=currentTime;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.showMyLocationDetail:

                break;
            case R.id.showMarks:
                if (MAKE_MARK)
                {
                    item.setIcon(R.drawable.ic_visibility_off_white_48dp);
                    clearMap();
                    MAKE_MARK=!MAKE_MARK;
                }else
                {
                    item.setIcon(R.drawable.ic_visibility_white_48dp);
                    makeMark();
                    MAKE_MARK=!MAKE_MARK;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MAPTEST", "resume");
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MAPTEST", "pause");
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAPTEST", "destory");
        mLocationClient.stop();
        mapView.onDestroy();
    }
}
