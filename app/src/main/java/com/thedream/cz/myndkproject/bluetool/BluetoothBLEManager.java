package com.thedream.cz.myndkproject.bluetool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import com.thedream.cz.myndkproject.utils.BluetoothDeviceUtil;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.SharePreUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class BluetoothBLEManager {

    private static final BluetoothBLEManager mBluetoothManager = new BluetoothBLEManager();
    private Handler mHandler;
    private boolean mScanning;
    private static BluetoothService mBluetoothLeService;
    private static String mDeviceAddress;
    private Context mContext;

    private BluetoothBLEManager() {
    }

    public static BluetoothBLEManager getInstance() {
        return mBluetoothManager;
    }

    public void init(Context context) {
        mContext = context;
        mHandler = new Handler();
    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                PrintUtil.print("Unable to initialize Bluetooth");
                return;
            }
            // Automatically connects to the device upon successful start-up initialization.
            //  连接设备
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public boolean hasBLE() {
        return mBluetoothLeService.initialize();
    }

    public boolean isOpenBLE() {
        return mBluetoothLeService.mBluetoothAdapter.isEnabled();
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void launchBLE(Context context, int flag) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((Activity) context).startActivityForResult(enableBtIntent, flag);
    }

    public void searchDevice(boolean enable) {
        //  检查是否存在绑定过的设备，如果有则解除绑定
//        removeBondDevice();

        if (enable) {
//             Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothLeService.mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, BluetoolGattAttributes.SCAN_TIME_OUT);
            mScanning = true;
            mBluetoothLeService.mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothLeService.mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void removeBondDevice() {
        if (!TextUtils.isEmpty(mDeviceAddress) && BluetoothAdapter.checkBluetoothAddress(mDeviceAddress)) {
            BluetoothDevice device = mBluetoothLeService.mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
            try {
                BluetoothDeviceUtil.removeBond(device.getClass(), device);
                BluetoothDeviceUtil.cancelPairingUserInput(device.getClass(), device);
            } catch (Exception e) {
                e.printStackTrace();
                PrintUtil.print("removeBondDevice :" + e.toString());
            }
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
        if (name.toLowerCase().contains("ihk") && BluetoothAdapter.checkBluetoothAddress(address)) {
            searchDevice(false);
            //  找到设备后自动连接，连接成功后保存地址
            if (connectDevice(device.getAddress())) {
                //  个人感觉地址保存没有必要
                mDeviceAddress = device.getAddress();
                SharePreUtil.put(mContext, SharePreUtil.KEY_PRIVATE_DEVICE_ADDRESS, mDeviceAddress, false);
            }
        }
    }

    public boolean connectDevice(String address) {
        return mBluetoothLeService.connect(address);
    }

    public boolean connectDevice() {
        return connectDevice(mDeviceAddress);
    }

    public void disconnectDevice() {
        mBluetoothLeService.disconnect();
    }

    public void setBluetoothAddress(String address) {
        mDeviceAddress = address;
    }

    public void getSupportedGattServices() {
        List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();
        PrintUtil.printCZ("getSupportedGattServices  " + (services == null));
        if (services != null) {
            PrintUtil.printCZ("getSupportedGattServices size: " + (services.size()));
        }
        if (services == null || services.size() == 0) return;
        for (BluetoothGattService service : services) {
            PrintUtil.printCZ("service uuid:" + service.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            if (characteristics == null) continue;
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                PrintUtil.printCZ("characteristic value:" + characteristic.getValue().toString());
            }
        }
    }

}
