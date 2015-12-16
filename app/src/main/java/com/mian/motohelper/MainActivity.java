package com.mian.motohelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mian.motohelper.fragments.AboutFragment;
import com.mian.motohelper.fragments.CarInformationFragment;
import com.mian.motohelper.fragments.GasStationLocationFragment;
import com.mian.motohelper.fragments.IndexFragment;
import com.mian.motohelper.fragments.MaintenanceRecordsManagementFragment;
import com.mian.motohelper.fragments.SetupFragment;
import com.tools.MyTools;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;//用來取代ActionBar的ToolBar
    private DrawerLayout drawer;//導航抽屜的layout
    private ActionBarDrawerToggle toggle;//位於ToolBar的導航抽屜開關
    private NavigationView navigationView;//用於顯示導航抽屜的選單，主要是要簡化選單的設計
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int nowContentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processViews();

        initIndex();
        initToolbar();
        initDrawer();
        initNavigationView();

    }

    /**
     * 初始化首頁
     */
    private void initIndex() {
        getFragmentManager().beginTransaction().add(R.id.content_frameLayout, new IndexFragment()).commit();
        nowContentId = R.id.menu_index;
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

    /**
     * 按下導航抽屜的項目時所要執行的動作
     *
     * @param item 所點選的項目
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = item.getTitle().toString();
        toolbar.setTitle(title);
        MyTools.myLog("click" + title);
        if (nowContentId != id) {
            replaceFragment(id, title);
        } else {
            Toast.makeText(MainActivity.this, "目前正處於\"" + title + "\"頁面", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(int id, String title) {
        Fragment fragment = null;

        if (id == R.id.menu_index) {
            fragment = new IndexFragment();
        } else if (id == R.id.menu_car_information) {
            fragment = new CarInformationFragment();
        } else if (id == R.id.menu_maintenance_records_management) {
            fragment = new MaintenanceRecordsManagementFragment();
        } else if (id == R.id.menu_gas_station_location) {
            fragment = new GasStationLocationFragment();
        } else if (id == R.id.menu_setup) {
            fragment = new SetupFragment();
        } else if (id == R.id.menu_about) {
            fragment = new AboutFragment();
        }
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.content_frameLayout, fragment, title).addToBackStack(title);
        fragmentTransaction.commit();

        nowContentId = id;
    }


    /**
     * 按下返回鍵要執行的動作
     */
    @Override
    public void onBackPressed() {
        int count = 0;
        try {
            count = fragmentManager.getBackStackEntryCount();//取得退線堆疊數量
        } catch (Exception ex) {
            MyTools.myLog(ex.toString());
            finish();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count != 0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
            //MyTools.myLog("BackStackEntryCount = " + count);
        } else {
            //MyTools.myLog("BackStackEntryCount = " + count);
            super.onBackPressed();
        }
    }

}
