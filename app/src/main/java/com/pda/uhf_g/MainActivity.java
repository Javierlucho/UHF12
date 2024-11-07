package com.pda.uhf_g;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.handheld.uhfr.UHFRManager;
import com.pda.uhf_g.data.gps.IBaseGpsListener;
import com.pda.uhf_g.entity.GPSInfo;
import com.pda.uhf_g.service.TestService;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.ScanUtil;
import com.pda.uhf_g.util.SharedUtil;
import com.uhf.api.cls.Reader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IBaseGpsListener, NavigationView. OnNavigationItemSelectedListener{

    private InventoryViewModel viewModel;
    private static final int PERMISSION_LOCATION = 1000;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

//    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);;

    private AppBarConfiguration mAppBarConfiguration;

    public boolean isConnectUHF  = false;

    //
    private ScanUtil scanUtil;
    public UHFRManager mUhfrManager;//uhf
    public SharedPreferences mSharedPreferences;

    public List<String> listEPC = new ArrayList<>();//epc数据
    public static int type=-1;
    public NavController navController ;


    //############################## Location ###################################
//    LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(@NonNull LocationResult locationResult) {
//            for (Location location : locationResult.getLocations()) {
//                // Handle location updates here
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//
//            }
//        }
//    };
    //############################## Location End ###################################
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with location updates
                showLocation();
            } else {
                // Location permission denied, handle accordingly (e.g., display a message)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View view =  navigationView.getHeaderView(0);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mSharedPreferences = this.getSharedPreferences("UHF", MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permissions are granted, proceed with location updates

        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE); // Define a request code
        }
        //############################## Location ###################################
//        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
//                .setWaitForAccurateLocation(false) // Optional: Wait for a more accurate location
//                .setMinUpdateIntervalMillis(5000) // Optional: Minimum update interval
//                .setMaxUpdateDelayMillis(15000) // Optional: Maximum update delay
//                .setDurationMillis(60000) // Optional: Duration for which location updates are requested
//                .build();
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        //############################## Location End ###################################

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener()
        {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments)
            {
                //Fragment
                LogUtil.e("destination = " + destination.getNavigatorName());
            }
        });


        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initModule();
        setScanKeyDisable();
    }



    @Override
    protected void onStop() {
        super.onStop();
        setScanKeyEnable();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeModule() ;
    }

    private void setScanKeyDisable() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.N) {
            // For Android10.0 module
            scanUtil = ScanUtil.getInstance(this);
            scanUtil.disableScanKey("134");
            scanUtil.disableScanKey("137");
        }

    }


    private void setScanKeyEnable() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.N) {
            // For Android10.0 module
            scanUtil = ScanUtil.getInstance(this);
            scanUtil.enableScanKey("134");
            scanUtil.enableScanKey("137");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    SharedUtil sharedUtil ;
    /**
     * 初始化uhf模块
     */
    private void initModule() {
        mUhfrManager = UHFRManager.getInstance();// Init Uhf module
        if(mUhfrManager!=null){
            //5106和6106 /6107和6108 支持33db
            sharedUtil = new SharedUtil(this);
            Reader.READER_ERR err = mUhfrManager.setPower(sharedUtil.getPower(), sharedUtil.getPower());//set uhf module power

            if(err== Reader.READER_ERR.MT_OK_ERR){
                isConnectUHF = true ;
                Reader.READER_ERR err1 = mUhfrManager.setRegion(Reader.Region_Conf.valueOf(sharedUtil.getWorkFreq()));

                Toast.makeText(getApplicationContext(),"FreRegion:"+Reader.Region_Conf.valueOf(sharedUtil.getWorkFreq())+
                        "\n"+"Read Power:"+sharedUtil.getPower()+
                        "\n"+"Write Power:"+sharedUtil.getPower(),Toast.LENGTH_LONG).show();

                setParam() ;
                if(mUhfrManager.getHardware().equals("1.1.01")){
                    type=0;
                }

            }else {
                //5101 30db
                Reader.READER_ERR err1 = mUhfrManager.setPower(30, 30);//set uhf module power
                if(err1== Reader.READER_ERR.MT_OK_ERR) {
                    isConnectUHF = true ;
                    mUhfrManager.setRegion(Reader.Region_Conf.valueOf(mSharedPreferences.getInt("freRegion", 1)));
                    Toast.makeText(getApplicationContext(), "FreRegion:" + Reader.Region_Conf.valueOf(mSharedPreferences.getInt("freRegion", 1)) +
                            "\n" + "Read Power:" + 30 +
                            "\n" + "Write Power:" + 30, Toast.LENGTH_LONG).show();
                    setParam() ;
                }else {
                    Toast.makeText(this,getString(R.string.module_init_fail), Toast.LENGTH_SHORT).show();
                }
            }

        }else {
            Toast.makeText(this,getString(R.string.module_init_fail), Toast.LENGTH_SHORT).show();
        }


    }


    private void setParam() {
        //session
        mUhfrManager.setGen2session(sharedUtil.getSession());
        //taget
        mUhfrManager.setTarget(sharedUtil.getTarget());
        //q value
        mUhfrManager.setQvaule(sharedUtil.getQvalue());
        //FastId
        mUhfrManager.setFastID(sharedUtil.getFastId());
        //Rr Advance settings
        boolean b = mSharedPreferences.getBoolean("show_rr_advance_settings", false);
        if (b) {
            int jgTime = mSharedPreferences.getInt("jg_time", 6);
            int dwell = mSharedPreferences.getInt("dwell", 2);
            int i = mUhfrManager.setRrJgDwell(jgTime, dwell);
        }
    }



    private void closeModule() {
        if (mUhfrManager != null) {//close uhf module
            mUhfrManager.close();
            mUhfrManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_inventory_ipsp:
                navController.navigate(R.id.nav_inventory_ipsp);
                break ;
            case R.id.nav_inventory:
                navController.navigate(R.id.nav_inventory);
                break ;
            case R.id.nav_read_write_tag:
                navController.navigate(R.id.nav_read_write_tag);
                break;
            case R.id.nav_setting:
                navController.navigate(R.id.nav_setting);
                break;
            case R.id.nav_temp:
                navController.navigate(R.id.nav_temp);
                break;
            case R.id.nav_help:
                navController.navigate(R.id.nav_help);
                break;
            case R.id.nav_about:
                navController.navigate(R.id.nav_about);
                break;
            case R.id.nav_inventory_led:
                navController.navigate(R.id.nav_inventory_led);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    long exitSytemTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - exitSytemTime > 2000){
                Toast.makeText(getApplicationContext(), R.string.exit_app, Toast.LENGTH_SHORT).show();
                exitSytemTime = System.currentTimeMillis();
                return true;
            }else{
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onSupportNavigateUp() {
        //Log.e("pang", "onSupportNavigateUp") ;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.e("pang", "item = " + item.getItemId());
        return false;
    }

    // ############################## Location ###################################
    @Override
    public void onGpsStatusChanged(int i) {
        // nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // Handle location updates here
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            GPSInfo gpsInfo = new GPSInfo(latitude, longitude);
            viewModel.setCurrentLocation(gpsInfo);
            Log.d("Location", "longitude" + longitude + "latitude" + latitude);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        // nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        // nothing
    }

}