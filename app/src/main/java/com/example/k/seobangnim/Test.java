package com.example.k.seobangnim;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;

/**
 * Created by mutecsoft on 2016-09-01.
 */
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Toolbar toolbar = new Toolbar(this);
        toolbar.setEnabled(true);
        toolbar.setLayoutParams(new Toolbar.LayoutParams(params));
        toolbar.setBackgroundColor(Color.parseColor("#ff0000"));
        toolbar.setTitle("Test");
        toolbar.setNavigationIcon(getDrawable(android.R.drawable.ic_menu_set_as));
//        Drawable drawable = Drawable.createFromPath(
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_drawer));
        addContentView(toolbar, params);

//        setActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
