package com.perpuz;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabhost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, buku.class);
        spec = tabhost.newTabSpec("buku").setIndicator("buku",null).setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, anggota.class);
        spec = tabhost.newTabSpec("anggota").setIndicator("anggota",null).setContent(intent);
        tabhost.addTab(spec);

    }
}