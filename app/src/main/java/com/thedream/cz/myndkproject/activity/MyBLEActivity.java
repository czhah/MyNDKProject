package com.thedream.cz.myndkproject.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bluetool.BluetoolGattAttributes;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.ArrayList;
import java.util.List;

public class MyBLEActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 101;
    private Button btnSearch;

    private String mDeviceAddress;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning = false;
    private TextView tvDevice;
    private List<String> addressList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ble);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBLE();
            }
        });
        Button btnConn = (Button) findViewById(R.id.btn_conn);
        tvDevice = (TextView) findViewById(R.id.tv_device);
        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connDevice();
            }
        });

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            btnSearch.setEnabled(true);
        }
        mHandler = new Handler();
    }

    public void searchDevice(boolean enable) {

        if (enable) {
//             Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, BluetoolGattAttributes.SCAN_TIME_OUT);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void connDevice() {
        if (!TextUtils.isEmpty(mDeviceAddress)) {
            Intent intent = new Intent(this, MyBLEConnectActivity.class);
            intent.putExtra(BluetoothDevice.class.getSimpleName(), mDeviceAddress);
            startActivity(intent);
        }
    }

    private void searchBLE() {
        searchDevice(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchDevice(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "未打开不支持", Toast.LENGTH_SHORT).show();
                } else {
                    btnSearch.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    // Device scan callback.
    private final BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    //  找到设备并保存
                    PrintUtil.printCZ("查找设备:" + device.getName() + " " + device.getAddress());
                    checkAndConnDevice(device);
                }
            };

    private void checkAndConnDevice(BluetoothDevice device) {
        String name = device.getName();
        String address = device.getAddress();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) return;
        if (name.toLowerCase().contains("ihk-1509988") && BluetoothAdapter.checkBluetoothAddress(address)) {
            searchDevice(false);
            mDeviceAddress = device.getAddress();
            tvDevice.setText("我的设备:" + name + "  " + address);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchDevice(false);
    }
}
