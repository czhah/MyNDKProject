package com.thedream.cz.myndkproject.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.utils.BLUUtil;

import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BLEActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_OPEN_BLE = 1;

    private Button btnBle;
    private Button btnSearch;
    private Button btnStop;
    private Button btnConn;
    private TextView tvMyDevice;
    private TextView tvOtherDevice;

    private BluetoothAdapter bluetoothAdapter;
    private boolean bluEnable = false;
    private String mMyAddress = "";
    private String mOtherAddress = "";
    private BluetoothDevice huaweiDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        PackageManager packageManager = getPackageManager();
        boolean hasBlu = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!hasBlu) {
            finish();
            return;
        }
        btnBle = (Button) findViewById(R.id.btn_ble);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnConn = (Button) findViewById(R.id.btn_conn);
        tvMyDevice = (TextView) findViewById(R.id.tv_my_device);
        tvOtherDevice = (TextView) findViewById(R.id.tv_other_device);


        btnBle.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnConn.setOnClickListener(this);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            finish();
            return;
        }
        if (bluetoothAdapter.isEnabled()) {
            btnBle.setText("蓝牙已开启");
            btnBle.setEnabled(false);
            showMyAddress();
        } else {
            btnBle.setText("打开蓝牙");
            btnBle.setEnabled(true);
        }
        bluEnable = true;

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

    private void showMyAddress() {
        mMyAddress = bluetoothAdapter.getAddress();
        String name = bluetoothAdapter.getName();
        tvMyDevice.setText("我的设备: " + name + " " + mMyAddress);

        boolean isValidAddress = BluetoothAdapter.checkBluetoothAddress(mMyAddress);
        Log.i("cz", "我的设备地址是否可用:" + isValidAddress);
        if (isValidAddress) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mMyAddress);
            if (device != null) {
                Log.i("cz", "我的设备: " + device.getName() + " " + device.getAddress());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ble:
                openBLE();
                break;
            case R.id.btn_search:
//                if (PermissionUtil.checkLocalPermission(BLEActivity.this)) {
//                }
                searchDevice(true);
                break;
            case R.id.btn_stop:
                searchDevice(false);
                break;
            case R.id.btn_conn:
                break;
            default:
                break;
        }
    }

    private void openBLE() {
        if (!bluetoothAdapter.isEnabled()) {
            Log.d("cz", "开启蓝牙...");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_OPEN_BLE);
        }
    }

    private void searchDevice(boolean enable) {
        if (bluetoothAdapter == null) return;
        if (enable) {
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();
            boolean discovery = bluetoothAdapter.startDiscovery();
            Log.i("cz", "开启搜索蓝牙设备 discovery:" + discovery);
        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
                Log.i("cz", "结束搜索蓝牙设备");
            }
            btnSearch.setText("查找蓝牙设备");
            btnSearch.setEnabled(true);
        }
    }

    private void connDevice(BluetoothDevice device) {
        btnConn.setVisibility(View.VISIBLE);
        btnConn.setText("连接中...");
        showBondDevice();
//        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//        UUID uuid = UUID.fromString(SPP_UUID);
//        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
//        socket.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_BLE) {
            if (resultCode == RESULT_OK) {
                btnBle.setText("蓝牙已开启");
                btnBle.setEnabled(false);
                showMyAddress();
            } else {
                Log.i("cz", "蓝牙开启失败:" + resultCode);
            }

        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //  必须有定位权限，不然这里的广播收不到
                BluetoothDevice device = foundDevices(intent);
                if (device != null) {
                    //  中断搜索
                    searchDevice(false);
                    //  显示查找信息
                    showOtherAddress(device);
                    //  处理不同连接信息
                    dispatchOtherDevice(device);
                }
                Log.d("cz", "1本地蓝牙 找到远程蓝牙设备 ACTION_FOUND");
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                Log.d("cz", "1蓝牙状态已改变 ACTION_BOND_STATE_CHANGED");
                //  远程蓝牙设备状态改变的时候发出这个广播, 例如设备被匹配, 或者解除配对
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                Log.i("cz", "ACTION_BOND_STATE_CHANGED bondState: " + bondState);
                BluetoothDevice device = foundDevices(intent);
                if (device != null && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.i("cz", "用户点击配对后开始进行连接");
                    connDevice(device);
                }

            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
                Log.d("cz", "1请求蓝牙配对 ACTION_PAIRING_REQUEST");
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.d("cz", "1建立低级别连接 ACTION_ACL_CONNECTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.d("cz", "1断开低级别连接 ACTION_ACL_DISCONNECTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Log.d("cz", "1已经取消远程连接请求，表示很快就会断开 ACTION_ACL_DISCONNECT_REQUESTED");
            } else if (BluetoothDevice.ACTION_CLASS_CHANGED.equals(action)) {
                Log.d("cz", "1远程设备蓝牙已改变 ACTION_CLASS_CHANGED  ");
                //  一个远程设备的绑定状态发生改变时发出广播
//                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
//                Log.i("cz", "bondState: "+bondState);

            } else if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {
                Log.d("cz", "1首次检索到远程设备的好友名称 ACTION_NAME_CHANGED");


            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                btnSearch.setText("搜索中...");
                btnSearch.setEnabled(false);
                Log.d("cz", "2本地蓝牙启动 查找远程蓝牙设备 ACTION_DISCOVERY_STARTED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                btnSearch.setText("查找蓝牙设备");
                btnSearch.setEnabled(true);
                Log.d("cz", "2本地蓝牙完成 查找远程蓝牙设备 ACTION_DISCOVERY_FINISHED  ");
            } else if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action)) {
                Log.d("cz", "2本地蓝牙更改连接的蓝牙名称 ACTION_LOCAL_NAME_CHANGED  name:" + updateBLEName());
            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                Log.d("cz", "2本地蓝牙扫描模式已更改 ACTION_SCAN_MODE_CHANGED  mode:" + updateBLEMode());
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int prevStatr = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, BluetoothAdapter.STATE_OFF);
                int newStatr = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                Log.d("cz", "2本地蓝牙状态已更改 ACTION_STATE_CHANGED  prevStatr:" + prevStatr + " newStatr:" + newStatr);
            }

        }
    };


    private BluetoothDevice foundDevices(Intent intent) {
        if (intent == null) return null;
        if (intent != null) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && checkDevice(device)) {
                Log.i("cz", "用户找到指定蓝牙设备");
                return device;
            }
        }
        return null;
    }

    private boolean checkDevice(BluetoothDevice device) {
        String name = device.getName();
        if (name != null && "华为".equals(name)) return true;
        return false;
    }

    private void showOtherAddress(BluetoothDevice device) {
        String name = device.getName();
        //  mac地址
        mOtherAddress = device.getAddress();
        //  蓝牙连接状态 1、BOND_NONE没有连接 2、BOND_BONDING连接中 3、BOND_BONDED已连接
        int bondState = device.getBondState();
        //  uuid
        ParcelUuid[] uuids = device.getUuids();
        //  蓝牙类型 1、DEVICE_TYPE_LE低功耗 2、DEVICE_TYPE_DUAL双模式  3、DEVICE_TYPE_CLASSIC传统模式
        int type = device.getType(); // type:1
        Log.i("cz", "第一次查找成功并显示查找信息 name:" + name + " address:" + mOtherAddress + " bondState:" + bondState + " type:" + type);
        tvOtherDevice.setText("待连接设备: " + name + " " + mOtherAddress);
    }

    private void dispatchOtherDevice(BluetoothDevice device) {
        //  蓝牙连接状态 1、BOND_NONE没有连接 2、BOND_BONDING连接中 3、BOND_BONDED已连接
        int bondState = device.getBondState();
        if (bondState == BluetoothDevice.BOND_NONE) {
            Log.i("cz", "用户对未配对的蓝牙设备进行配对...");
            //  配对
            pairDevice(device);
        } else if (bondState == BluetoothDevice.BOND_BONDED) {
            Log.i("cz", "用户对已完成配对的蓝牙设备进行连接...");
            //  连接设备
            connDevice(device);
        } else if (bondState == BluetoothDevice.BOND_BONDING) {
            Log.i("cz", "正在配对...");
        }
    }

    private void pairDevice(BluetoothDevice otherDevice) {
        //  匹配设备
        Log.d("cz", "正在配对 " + otherDevice.getName() + " 设备" + otherDevice.getAddress());
        try {
            BLUUtil.createBond(otherDevice.getClass(), otherDevice);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("cz", "配对失败:" + e.toString());
        }
    }

    private void showBondDevice() {
        if (bluetoothAdapter == null) return;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null && devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Log.i("cz", "已经绑定的设备:" + device.toString());
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
        String address = bluetoothAdapter.getAddress();
        Log.d("cz", "address:" + address);
        return bluetoothAdapter.getName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluEnable) {
            unregisterReceiver(receiver);
        }
    }

}
