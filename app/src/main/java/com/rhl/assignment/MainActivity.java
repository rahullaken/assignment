package com.rhl.assignment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rhl.assignment.bean.FileBean;
import com.rhl.assignment.utils.FileUtils;
import com.rhl.assignment.utils.LocationUtils;
import com.rhl.assignment.viewmodel.FileViewModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.functions.Action1;

public class MainActivity extends HiddenCameraActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getName();

    private GoogleMap googleMap;
    private Marker mCurrentMarker;
    private Context mContext;
    private CameraConfig mCameraConfig;
    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    private double lat, longi;
    private String CAMERA_IMG_PATH = Environment.getExternalStorageDirectory() + "/" + "Assignment" + "/img/%s.jpg";
    private String CAMERA_IMG = Environment.getExternalStorageDirectory() + "/" + "Assignment" + "/img/";
    private FileViewModel fileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void getLocationPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted -> LocationUtils.getInstance().requestLocationInfo(mContext, (location, countryName, adminArea, localty, postalCode) -> {
            if (location != null) {
                showMarker(location.getLatitude(), location.getLongitude());
            } else {
                Log.d(TAG, "getUserPincode: empty");
            }
        }), throwable -> Log.e(TAG, "call: " + throwable.getMessage()));
    }

    private void showMarker(double latitude, double longitude) {
        lat = latitude;
        longi = longitude;
        if (mCurrentMarker != null) {
            mCurrentMarker.remove();
        }
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        //options.title(address);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mCurrentMarker = googleMap.addMarker(options);

        googleMap.setOnMapClickListener(point -> {
            showMarker(point.latitude, point.longitude);
            takePicture();
        });
    }


    private void initView() {
        fileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
        showPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) mContext);
        mCameraConfig = new CameraConfig()
                .getBuilder(this)
                .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .setImageRotation(CameraRotation.ROTATION_270)
                .build();
        //Check for the camera permission for the runtime
        new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    startCamera(mCameraConfig);
                } else {
                    Toast.makeText(mContext, "Please Allow the Camera Permission", Toast.LENGTH_SHORT).show();
                }
            }
        }, throwable -> {
            Log.e(TAG, "call: " + throwable.getMessage());
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        runOnUiThread(() -> {
            if (mCurrentMarker != null) {
                mCurrentMarker.remove();
            }
            getLocationPermission();

        });

    }

    private void showPermission() {
        new RxPermissions((Activity) mContext).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (!granted) {
                Toast.makeText(mContext, "Please Allow the all Permission", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            Log.e(TAG, "call: " + throwable.getMessage());
        });
    }


    @Override
    public void onImageCapture(@NonNull File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        Log.d(TAG, "onImageCapture: " + bitmap);
        File file = new File(CAMERA_IMG);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filename = String.valueOf(System.currentTimeMillis());
        String filePath = String.format(CAMERA_IMG_PATH, filename);
        FileUtils.saveBitmap(filePath, bitmap);
        FileBean fileBean = new FileBean();
        fileBean.setFileName(filename);
        fileBean.setLat(lat);
        fileBean.setLongi(longi);

        //adding to database
        fileViewModel.saveFile(mContext, fileBean);

    }

    @Override
    public void onCameraError(int errorCode) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1) {
            Intent intent = new Intent(mContext, FileListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
