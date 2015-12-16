package com.mian.motohelper;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tools.MyTools;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;//用來取代ActionBar的ToolBar
    private DrawerLayout drawer;//導航抽屜的layout
    private ActionBarDrawerToggle toggle;//位於ToolBar的導航抽屜開關
    private NavigationView navigationView;//用於顯示導航抽屜的選單，主要是要簡化選單的設計

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processViews();

        initToolbar();
        initDrawer();
        initNavigationView();

    }

    /**
     * 初始化畫面元件
     */
    private void processViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
    }

    /**
     * 初始化NavigationView，並設定監聽事件
     */
    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 把NavigationDrawer與ToolBar綑綁在一起
     */
    private void initDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 將ActionBar設為ToolBar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = item.getTitle().toString();
        switch (id) {
            case R.id.menu_index:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
            case R.id.menu_car_management:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
            case R.id.menu_maintenance_records_management:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
            case R.id.menu_gas_station_location:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
            case R.id.menu_setup:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
            case R.id.menu_about:
                toolbar.setTitle(title);
                MyTools.myLog("click" + title);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
