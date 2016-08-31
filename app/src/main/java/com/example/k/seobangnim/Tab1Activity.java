package com.example.k.seobangnim;

import android.os.Bundle;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;

/**
 * Created by mutecsoft on 2016-08-31.
 */
public class Tab1Activity extends NMapActivity {
    private static final String CLIENT_ID = "W7vyHsjqj0FrKivZfw_Z";
    NMapView mMapView;
    NMapController mMapController;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);

        setContentView(mMapView);

        mMapView.setClickable(true);

//        mMapView.setOnMapStateChangeListener();
//        mMapView.setOnMapViewTouchEventListener();

        mMapController = mMapView.getMapController();

        mMapView.setBuiltInZoomControls(true, null);
    }



}
