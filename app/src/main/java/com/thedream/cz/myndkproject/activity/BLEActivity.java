package com.thedream.cz.myndkproject.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.thedream.cz.myndkproject.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BLEActivity extends AppCompatActivity {

    private final int REQUEST_OPEN_BLE = 1;

    private MyHandler mHandler = new MyHandler(this);
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isScaning;
    private boolean hasBlu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        PackageManager packageManager = getPackageManager();

        hasBlu = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        Log.i("cz", "是否支持蓝牙:" + hasBlu + "  当前线程:" + Thread.currentThread());

        if (hasBlu) {
            findViewById(R.id.btn_ble).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBLE();
                }
            });

            findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkLocalPermission(BLEActivity.this)) {
                        searchDevice(true);
                    }
                }
            });

            findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchDevice(false);
                }
            });


            IntentFilter intentFilter = new IntentFilter();
            //  找到蓝牙设备
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            //  蓝牙状态已改变
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            //  请求蓝牙配对
            intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
            //  建立低级别连接
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            //  断开低级别连接
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            //  已经取消远程连接请求，表示很快就会断开
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            //  远程设备蓝牙已改变
            intentFilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
            //  首次检索到远程设备的好友名称
            intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);

            //  完成本地蓝牙发现设备
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            //  本地蓝牙启动发现远程设备
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            //  本地蓝牙更改连接的蓝牙名称
            intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
            //  本地蓝牙扫描模式已更改
            intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            //  本地蓝牙状态已更改
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiver, intentFilter);
        }
    }

    private void searchDevice(boolean enable) {
        if (bluetoothLeScanner == null) return;
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("cz", "结束搜索");
                    bluetoothLeScanner.stopScan(mScannerCallback);
                }
            }, 10000);
            Log.i("cz", "开启搜索");
            bluetoothLeScanner.startScan(mScannerCallback);
        } else {
            Log.i("cz", "结束搜索");
            bluetoothLeScanner.stopScan(mScannerCallback);
        }
    }

    private void openBLE() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.i("cz", "未开启蓝牙");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_OPEN_BLE);
            return;
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        Log.i("cz", "已开启蓝牙");
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null && devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Log.i("cz", "设备:" + device.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_BLE) {
            if (resultCode == RESULT_OK) {
                //  蓝牙已开启
//                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            } else {
                Log.i("cz", "蓝牙开启失败:" + resultCode);
            }

        }
    }

    private ScanCallback mScannerCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i("cz", "onScanResult callbackType :" + callbackType + " result:" + result.toString());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            if (results != null && results.size() > 0) {
                for (ScanResult result : results) {
                    Log.i("cz", "onBatchScanResults :" + result.toString());
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i("cz", "onScanFailed  errorCode:" + errorCode);
        }
    };


    private static class MyHandler extends Handler {

        private WeakReference<BLEActivity> activites;

        public MyHandler(BLEActivity mContext) {
            activites = new WeakReference<BLEActivity>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activites == null) return;
            BLEActivity mContext = activites.get();
            if (mContext == null || mContext.isFinishing()) return;

        }
    }

    public boolean checkLocalPermission(Context mContext) {
        if (mContext == null) return false;
        //  ACCESS_COARSE_LOCATION:粗略定位;ACCESS_FINE_LOCATION:通过gps定位
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.i("cz", "1找到蓝牙设备 ACTION_FOUND");
                foundDevice();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.i("cz", "1蓝牙状态已改变 ACTION_BOND_STATE_CHANGED");
            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
                Log.i("cz", "1请求蓝牙配对 ACTION_PAIRING_REQUEST");
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.i("cz", "1建立低级别连接 ACTION_ACL_CONNECTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.i("cz", "1断开低级别连接 ACTION_ACL_DISCONNECTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Log.i("cz", "1已经取消远程连接请求，表示很快就会断开 ACTION_ACL_DISCONNECT_REQUESTED");
            } else if (BluetoothDevice.ACTION_CLASS_CHANGED.equals(action)) {
                Log.i("cz", "1远程设备蓝牙已改变 ACTION_CLASS_CHANGED");
            } else if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {
                Log.i("cz", "1首次检索到远程设备的好友名称 ACTION_NAME_CHANGED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i("cz", "2完成本地蓝牙发现设备 ACTION_DISCOVERY_FINISHED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i("cz", "2本地蓝牙启动发现远程设备 ACTION_DISCOVERY_STARTED");
            } else if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action)) {
                Log.i("cz", "2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED  name:" + updateBLEName());
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                Log.i("cz", "2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED  mode:" + updateBLEMode());
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.i("cz", "2本地蓝牙状态已更改 ACTION_STATE_CHANGED  state:" + updateBLEState());
            }

        }
    };

    private void foundDevice() {
        if (bluetoothAdapter == null) return;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null && devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Log.i("cz", "设备:" + device.toString());
            }
        }
    }

    private int updateBLEState() {
        if (bluetoothAdapter == null) return -999;
        return bluetoothAdapter.getState();
    }

    private int updateBLEMode() {
        if (bluetoothAdapter == null) return -999;
        return bluetoothAdapter.getScanMode();
    }

    private String updateBLEName() {
        if (bluetoothAdapter == null) return "xxx";
        return bluetoothAdapter.getName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasBlu) {
            unregisterReceiver(receiver);
        }
    }

    /**
     *
     * //  请求配对
     1蓝牙状态已改变 ACTION_BOND_STATE_CHANGED
     1建立低级别连接 ACTION_ACL_CONNECTED
     1首次检索到远程设备的好友名称 ACTION_NAME_CHANGED
     1远程设备蓝牙已改变 ACTION_CLASS_CHANGED
     1请求蓝牙配对 ACTION_PAIRING_REQUEST


     //  无法配对（配对失败）
     1蓝牙状态已改变 ACTION_BOND_STATE_CHANGED
     1断开低级别连接 ACTION_ACL_DISCONNECTED

     //  断开蓝牙
     2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED
     12本地蓝牙状态已更改 ACTION_STATE_CHANGED
     2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED
     2本地蓝牙状态已更改 ACTION_STATE_CHANGED

     //  开启蓝牙
     2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED
     2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED
     2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED
     2本地蓝牙状态已更改 ACTION_STATE_CHANGED
     2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED
     2本地蓝牙状态已更改 ACTION_STATE_CHANGED

     //  更改本地蓝牙名称
     2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED  name:红米手机
     2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED  name:红米手机2
     2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED  mode:23
     */
}
