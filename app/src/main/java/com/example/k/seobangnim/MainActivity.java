package com.example.k.seobangnim;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();
        tabHost.setup();

        String[] str = {"[ Tab1 ]", "[ Tab2 ]", "[ Tab3 ]"};
        Intent[] tabIntent = {new Intent(this, Tab1Activity.class), new Intent(this, Tab1Activity.class), new Intent(this, Tab1Activity.class)};
        for (int i=0; i<3; i++) {
            TabHost.TabSpec spec = tabHost.newTabSpec(str[i]).setContent(tabIntent[i]).setIndicator(str[i]);
            tabHost.addTab(spec);
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 180;
        }
    }
}
