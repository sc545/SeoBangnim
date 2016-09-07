package com.example.k.seobangnim;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGPoint;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mutecsoft on 2016-08-31.
 */
public class MapActivity extends NMapActivity implements NMapView.OnMapStateChangeListener {

    private static final String TAG = MapActivity.class.getSimpleName();
    // naver
    public static final String CLIENT_ID = "W7vyHsjqj0FrKivZfw_Z";
    public static final String CLIENT_SECRET = "3Xnp4iSoSL";
    NMapView mMapView;
    NMapController mMapController;
    NMapResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mMapOverlayManager;

    private static MapActivity mMapActivity;

    interface InvalidateListener {
        void invalidate(ArrayList arrayList);
    }

    private InvalidateListener mInvalidateListener = new InvalidateListener() {
        @Override
        public void invalidate(ArrayList arrayList) {
            invalidateMapView(arrayList);;
        }
    };

    // app
    private DrawerLayout mDrawerLayout;
    private FrameLayout mLayoutContent;
    private ListView mLvNavList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Create naver map
        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);

        mMapView.setClickable(true);


//        mMapView.setOnMapStateChangeListener();
//        mMapView.setOnMapViewTouchEventListener();

        mMapController = mMapView.getMapController();

        mMapView.setBuiltInZoomControls(true, null);

        mMapActivity = this;

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mMapOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);


        new SearchAsyncTask(this, mInvalidateListener).execute("갈비");
/*        int markerId = NMapPOIflagType.PIN;

// set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
        poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
        poiData.endPOIdata();

// create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mMapOverlayManager.createPOIdataOverlay(poiData, null);

// show all POI data
        poiDataOverlay.showAllPOIdata(0);*/




        // app
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_main, null);

        // Init toolbar
        Toolbar toolbar = (Toolbar) mDrawerLayout.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setTitle(getTitle());
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mLvNavList)) {
                    mDrawerLayout.closeDrawer(mLvNavList);
                } else {
                    mDrawerLayout.openDrawer(mLvNavList);
                }

            }
        });

        mLayoutContent = (FrameLayout) mDrawerLayout.findViewById(R.id.layoutContent);
        mLvNavList = (ListView) mDrawerLayout.findViewById(R.id.lvNavList);

        String[] navItems = {"btn1", "btn2", "btn3", "btn4", "btn5", "btn6"};
        mLvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, navItems));
        mLvNavList.setOnItemClickListener(new DrawerItemClickListener());

        GridLayout layoutMainContent = (GridLayout) mDrawerLayout.findViewById(R.id.layoutMainContent);
        layoutMainContent.setVisibility(View.INVISIBLE);

        mLayoutContent.addView(mMapView);

        setContentView(mDrawerLayout);

    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) { // success
            mMapController.setMapCenter(
                    new NGeoPoint(126.978371, 37.5666091), 11);
        } else { // fail
            android.util.Log.e("NMAP", "onMapInitHandler: error="
                    + nMapError.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }


    void invalidateMapView (ArrayList<HashMap<String, String>> arrayList) {
        int size = arrayList.size();
        int markerId = NMapPOIflagType.PIN;
        HashMap<String, String> map = null;

        // set POI data
        NMapPOIdata poiData =  new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        for(int i=0; i<size; i++) {
            map = arrayList.get(i);
//            new AlertDialog.Builder(this).setMessage(map.get("title") + ", " + map.get("mapx") + ", " + map.get("mapy")).show();
            NGPoint ngPoint = new NGPoint(Integer.parseInt(map.get("mapx")), Integer.parseInt(map.get("mapy")));
            nhn.api
            poiData.addPOIitem(ngPoint, map.get("title"), markerId, 0);
        }
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mMapOverlayManager.createPOIdataOverlay(poiData, null);

        // show all POI data
        poiDataOverlay.showAllPOIdata(0);

        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
            @Override
            public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                if (nMapPOIitem != null) {
                    Log.i(TAG, "onFocusChanged: "+nMapPOIitem.toString());
                } else {
                    Log.i(TAG, "onFocusChanged: ");
                }
            }

            @Override
            public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                Toast.makeText(MapActivity.this, "onCalloutClick: "+nMapPOIitem.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        // register callout overlay listener to customize it.
        mMapOverlayManager.setOnCalloutOverlayListener(new NMapOverlayManager.OnCalloutOverlayListener() {
            @Override
            public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
                return new NMapCalloutBasicOverlay(nMapOverlay, nMapOverlayItem, rect);
            }
        });
    }

    public static MapActivity getInstance() {
        if (mMapActivity == null) return null;

        return mMapActivity;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            searchLocation(position);
            mDrawerLayout.closeDrawer(mLvNavList);
        }
    }

    void searchLocation(int position) {
        SearchAsyncTask searchAsyncTask = new SearchAsyncTask(this, mInvalidateListener);
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                searchAsyncTask.execute("갈비");
                break;
            case 1:
                searchAsyncTask.execute("한솥");
                break;
            case 2:
                searchAsyncTask.execute("노래방");
                break;
            case 3:
                searchAsyncTask.execute("커피");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(mLvNavList)) {
            mDrawerLayout.closeDrawer(mLvNavList);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
