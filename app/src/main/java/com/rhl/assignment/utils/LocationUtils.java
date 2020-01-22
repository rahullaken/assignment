package com.rhl.assignment.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LocationUtils {

    private static final String TAG = "LocationUtils";
    private static LocationUtils locationUtils;

    public static LocationUtils getInstance() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    public void getLocations(Context context, final OnLocationListener onLocationListener) {
        if (!checkPermission(context, permission.ACCESS_COARSE_LOCATION)) {
            Log.d("", "getLocations:定位权限关闭，无法获取地理位置 ");
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    onLocationListener.onLocationRequest(location);
                } else {
                    onLocationListener.onLocationRequest(null);
                }
            }
        });
    }

    private boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission." + permName.toString());
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private enum permission {
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION
    }

    public void requestLocationInfo(final Context context, final OnLocationInfoListener onLocationInfoListener) {
        if (!checkPermission(context, permission.ACCESS_COARSE_LOCATION)) {
            Log.d("", "getLocations:定位权限关闭，无法获取地理位置 ");
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Observable.just(location)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            Geocoder geocoder = new Geocoder(context);
                            try {
                                List<Address> fromLocation = null;
                                if (location != null) {
                                    fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Address address = fromLocation.get(0);
                                    String countryName = address.getCountryName();
                                    String adminArea = address.getAdminArea();
                                    String localty = address.getLocality();
                                    String postalCode = address.getPostalCode();
                                    onLocationInfoListener.onLocationRequest(location, countryName, adminArea, localty, postalCode);
                                    Log.i(TAG, fromLocation.toString());
                                }
                            } catch (IOException e) {
                                onLocationInfoListener.onLocationRequest(null, null, null, null, null);
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.d(TAG, "call: ------->"+throwable.getMessage());
                        }
                    });

                } else {
                    onLocationInfoListener.onLocationRequest(null,null, null, null,null);
                }
            }
        });

    }

    public interface OnLocationInfoListener {
        void onLocationRequest(Location location, String countryName, String adminArea, String localty, String postalCode);
    }

    public interface OnLocationListener {
        void onLocationRequest(Location location);
    }
}
