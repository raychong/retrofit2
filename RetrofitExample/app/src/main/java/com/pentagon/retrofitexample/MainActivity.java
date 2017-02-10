package com.pentagon.retrofitexample;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pentagon.retrofitexample.adapter.GoListAdapter;
import com.pentagon.retrofitexample.http.HttpMethods;
import com.pentagon.retrofitexample.model.City;
import com.pentagon.retrofitexample.model.GoList;
import com.pentagon.retrofitexample.model.Token;
import com.pentagon.retrofitexample.service.LocationService;
import com.pentagon.retrofitexample.subscribers.ProgressSubscriber;
import com.pentagon.retrofitexample.subscribers.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.current_location_address)
    TextView tvAddress;

    @Bind(R.id.refresh_loader)
    CircularProgressBar progressBar;

    private Context mContext;
    private GoListAdapter adapter;
    private LinearLayoutManager manager;

    private SubscriberOnNextListener getWorldFeaturedGoListOnNext;
    private SubscriberOnNextListener getTokenOnNext;
    private SubscriberOnNextListener getCityNameOnNext;

    private String accessToken = "";
    private String appId = "";
    private String appSecret = "";

    private ArrayList<GoList> goLists;
    private boolean loadingMore = true;
    private int page = 1;

    boolean permissionGranted = false;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = 1;
    public static final int PERMISSION_BLOCKED = 2;

    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LNG = "location_lng";
    public static final String LOCATION_ACTION = "locationAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        adapter = new GoListAdapter(mContext,null,720,500);
        goLists = new ArrayList<>();
        GoList g = new GoList();
        g.setDivider(true);
        goLists.add(0, g);
        adapter.setList(goLists);
        recyclerView.setAdapter(adapter);
        getTokenOnNext  = new SubscriberOnNextListener<Token>() {
            @Override
            public void onNext(Token token) {
                accessToken = token.getAccessToken().toString();
                getGoList();
            }
        };
        getAccessToken();
        getWorldFeaturedGoListOnNext = new SubscriberOnNextListener<List<GoList>>() {
            @Override
            public void onNext(List<GoList> subjects) {
                loadingMore = false;
                ArrayList<GoList> list = ( ArrayList<GoList>)subjects;
                goLists.addAll(list);
                adapter.append(list);
            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int pastVisibleItems = manager.findFirstVisibleItemPosition();
                if (!loadingMore) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        page++;
                        getGoList();
                    }
                }
            }
        });
        getCityNameOnNext= new SubscriberOnNextListener<City>() {
            @Override
            public void onNext(City city) {
                progressBar.setVisibility(View.GONE);
                tvAddress.setText(""+city.getAddress());
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            checkResult();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getGoList(){
        try{
            HttpMethods.getInstance().getWorldFeaturedGoList(new ProgressSubscriber(getWorldFeaturedGoListOnNext, MainActivity.this,false),"Bearer "+accessToken, 20, page);
        }catch (Exception e){
            e.printStackTrace();
        }
       }
    private void getAccessToken(){
        try{
            HttpMethods.getInstance().getToken(new ProgressSubscriber(getTokenOnNext, MainActivity.this,false), appId,appSecret);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return PERMISSION_BLOCKED;
            }
            return PERMISSION_DENIED;
        }
        return PERMISSION_GRANTED;
    }
    public void checkResult(){
        try{
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionStatus = getPermissionStatus(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                int permissionStatus2 = getPermissionStatus(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
                if (permissionStatus != PackageManager.PERMISSION_GRANTED || permissionStatus2 != PackageManager.PERMISSION_GRANTED) {
                    if(permissionStatus == PERMISSION_DENIED ||  permissionStatus2 == PERMISSION_DENIED){
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSIONS_MULTIPLE_REQUEST);

                    }else{
                        // Denied & never ask again
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSIONS_MULTIPLE_REQUEST);
                    }
                }else{
                    permissionGranted = true;
                }
            } else {
                permissionGranted = true;
            }

            if(permissionGranted)
                startLocationIntent();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean accessFineLoc = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean accessCoarseLoc = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(accessFineLoc && accessCoarseLoc)
                    {
                        permissionGranted = true;
                        startLocationIntent();
                    }
                }

                break;
        }
    }

    public void startLocationIntent(){
        try{
            // 注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(LOCATION_ACTION);
            this.registerReceiver(new LocationBroadcastReceiver(), filter);

            // 启动服务
            Intent intent = new Intent();
            intent.setClass(this, LocationService.class);
            startService(intent);

            // 等待提示
            tvAddress.setText("Detecting location...");
            progressBar.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(LOCATION_ACTION))
                return;
            Double lat = intent.getDoubleExtra(LOCATION_LAT,0.00);
            Double lng = intent.getDoubleExtra(LOCATION_LNG,0.00);
            if(lat != 0.00 && lng != 0.00){
                HttpMethods.getInstance().getCityName(new ProgressSubscriber(getCityNameOnNext, MainActivity.this,false),"Bearer "+accessToken, lat,lng);
            }else{
                progressBar.setVisibility(View.GONE);
                tvAddress.setText("No Location found");
            }
            MainActivity.this.unregisterReceiver(this);// 不需要时注销
        }
    }
}
