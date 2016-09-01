package com.example.k.seobangnim;

import android.os.Bundle;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;

/**
 * Created by mutecsoft on 2016-08-31.
 */
public class MapActivity extends NMapActivity {
    private static final String CLIENT_ID = "W7vyHsjqj0FrKivZfw_Z";
    NMapView mMapView;
    NMapController mMapController;

    private static MapActivity mMapActivity;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID);

        setContentView(mMapView);

        ActionBar actionBar = getActionBar();


        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.show();
        } else {
            Toast.makeText(this, "actionBar = null", Toast.LENGTH_SHORT).show();
        }
        mMapView.setClickable(true);

//        mMapView.setOnMapStateChangeListener();
//        mMapView.setOnMapViewTouchEventListener();

        mMapController = mMapView.getMapController();

        mMapView.setBuiltInZoomControls(true, null);

        mMapActivity = this;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;

        }
        return true;
    }

    public static MapActivity getInstance() {
        if (mMapActivity == null) return null;

        return mMapActivity;
    }

}
