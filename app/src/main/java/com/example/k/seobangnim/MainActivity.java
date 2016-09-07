package com.example.k.seobangnim;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by mutecsoft on 2016-09-01.
 */
public class MainActivity extends Activity {
    private static final String EXTRA_ITEM = "item";
    private static final int PERMISSION_REQUEST_CODE = 999;
    String[] permissions = {"android.permission.INTERNET"};

    private DrawerLayout mDrawerLayout;
    private FrameLayout mLayoutContent;
    private ListView mLvNavList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED ) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        mLayoutContent = (FrameLayout) findViewById(R.id.layoutContent);
        mLvNavList = (ListView) findViewById(R.id.lvNavList);

        String[] navItems = {"btn1", "btn2", "btn3", "btn4", "btn5", "btn6"};
        mLvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, navItems));
        mLvNavList.setOnItemClickListener(new DrawerItemClickListener());

        Button[] btn = new Button[6];
        int[] btnId = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6};
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        for (int i=0; i<btn.length; i++) {
            btn[i] = (Button) findViewById(btnId[i]);
            btn[i].setOnClickListener(buttonClickListener);
        }

    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = 0;
            switch (v.getId()) {
                case R.id.btn1:
                    position = 0;
                    break;
                case R.id.btn2:
                    position = 1;
                    break;
                case R.id.btn3:
                    position = 2;
                    break;
                case R.id.btn4:
                    position = 3;
                    break;
                case R.id.btn5:
                    position = 4;
                    break;
                case R.id.btn6:
                    position = 5;
                    break;
            }
            startMapActivity(position);
        }
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startMapActivity(position);
            mDrawerLayout.closeDrawer(mLvNavList);
        }
    }

    private void startMapActivity(int position) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_ITEM, position);
        startActivity(intent);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(mLvNavList)) {
            mDrawerLayout.closeDrawer(mLvNavList);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
