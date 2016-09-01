package com.example.k.seobangnim;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
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
public class SecondActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM = "item";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String[] navItems = {"btn1", "btn2", "btn3", "btn4", "btn5", "btn6"};
    private ListView lvNavList;
    private FrameLayout layoutMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_second);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            Toast.makeText(this, "getActionBar() = null", Toast.LENGTH_SHORT).show();
        }

        lvNavList = (ListView) findViewById(R.id.lvNavList);
        layoutMainContent = (FrameLayout) findViewById(R.id.layoutMainContent);

        lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());


    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startMapActivity(position);
            drawerLayout.closeDrawer(lvNavList);
        }
    }

    private void startMapActivity(int position) {
        Intent intent = new Intent(this, Test.class);
        intent.putExtra(EXTRA_ITEM, position);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && drawerLayout.isDrawerOpen(lvNavList)) {
            drawerLayout.closeDrawer(lvNavList);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
