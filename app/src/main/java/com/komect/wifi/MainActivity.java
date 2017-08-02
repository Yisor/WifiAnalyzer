package com.komect.wifi;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends CheckPermissionsActivity {

    public static final String TAG = "MainActivity";
    protected WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private TextView currentSsid;
    private TextView currentChannel;
    private TextView linkSpeed;
    private TextView downloadSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.wifi_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        mRecyclerView.setAdapter(mAdapter = new RecyclerViewAdapter(mWifiList));
        init();
    }


    /*
    * 控件初始化
    * */
    private void init() {
        currentSsid = (TextView) findViewById(R.id.current_ssid);
        currentChannel = (TextView) findViewById(R.id.current_channel);
        linkSpeed = (TextView) findViewById(R.id.link_speed);
        downloadSpeed = (TextView) findViewById(R.id.download_speed);

        mWifiAdmin = new WifiAdmin(MainActivity.this);

        currentSsid.setText(mWifiAdmin.getSSID());
        linkSpeed.setText(String.format("连接速度：%dMbps", mWifiAdmin.getLinkSpeed()));
        currentChannel.setText(String.format("信道：%d", WifiUtil.getCurrentChannel(this)));

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(mTimerTask, 0, 3000, TimeUnit.MILLISECONDS);
    }


    final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            Log.d("Tlog", "run: ");
            if (mWifiAdmin != null) {
                mWifiAdmin.startScan(MainActivity.this);
                mWifiList = mWifiAdmin.getWifiList();
                if (mWifiList != null) {
                    Message msg = new Message();
                    msg.obj = mWifiList;
                    mHandler.sendMessage(msg);
                    Log.d("Tlog", "runing ");
                }
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter.updateData((List<ScanResult>) msg.obj);
        }
    };
}
