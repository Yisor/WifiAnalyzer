package com.komect.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import java.util.List;

public class MainActivity extends CheckPermissionsActivity {

    public static final String TAG = "MainActivity";
    private ListView mlistView;
    protected WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigurationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.wifi_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        mRecyclerView.setAdapter(mAdapter = new RecyclerViewAdapter(mWifiList));

        //if (Build.VERSION.SDK_INT >= 23) {
        //    // TODO 弹窗提示设置权限
        //}

        init();
    }


    /*
    * 控件初始化
    * */
    private void init() {
        //mlistView = (ListView) findViewById(R.id.wifi_list);

        mWifiAdmin = new WifiAdmin(MainActivity.this);
        mWifiAdmin.startScan(MainActivity.this);
        mWifiList = mWifiAdmin.getWifiList();
        if (mWifiList != null) {
            //mlistView.setAdapter(new MyAdapter(this, mWifiList));
            mAdapter.updateData(mWifiList);
        }

        mWifiConfigurationList = mWifiAdmin.getConfiguration();
        for (WifiConfiguration config : mWifiConfigurationList) {
        }
    }
}
