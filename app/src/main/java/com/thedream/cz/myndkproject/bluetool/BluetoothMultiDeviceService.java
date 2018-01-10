package com.thedream.cz.myndkproject.bluetool;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.thedream.cz.myndkproject.utils.BluetoothDeviceUtil;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 多设备连接
 */
public class BluetoothMultiDeviceService extends Service {

    private final String TAG = "BluetoothService";

    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    private ArrayMap<String, BluetoothGatt> gattArrayMap = new ArrayMap<>();

    /**
     * 与设备已连接
     */
    private static final int STATE_CONNECTED = 0;
    /**
     * 与设备连接断开
     */
    private static final int STATE_DISCONNECTED = 1;
    /**
     * 与设备连接中...
     */
    private static final int STATE_CONNECTING = 2;
    private int mConnectionState = STATE_DISCONNECTED;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DATA_FAILED =
            "com.example.bluetooth.le.ACTION_DATA_FAILED";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String EXTRA_TYPE =
            "com.example.bluetooth.le.EXTRA_TYPE";
    public final static String EXTRA_ADDRESS =
            "com.example.bluetooth.le.EXTRA_ADDRESS";

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("远程设备特征改变之后发出通知的回调  onCharacteristicChanged");
            broadcastUpdate(address, ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("读取特征之后的回调  onCharacteristicRead() " + address);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(address, ACTION_DATA_AVAILABLE, characteristic);
            } else {
                broadcastFailed(address, ACTION_DATA_FAILED, characteristic);
                Log.w(TAG, "onCharacteristicRead received: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("写入特征之后的回调  onCharacteristicWrite() " + address);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(address, ACTION_DATA_AVAILABLE, characteristic);
            } else {
                broadcastFailed(address, ACTION_DATA_FAILED, characteristic);
                Log.w(TAG, "onCharacteristicWrite received: " + status);
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            PrintUtil.print("连接状态改变的回调 onConnectionStateChange() status:" + status + "  newState:" + newState);
            String address = gatt.getDevice().getAddress();
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //  连接成功 保存信息
                gattArrayMap.put(address, gatt);

                //  发送广播通知已连接
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(address, intentAction);
                PrintUtil.print("Connected to GATT server.");
                // Attempts to discover services after successful connection.
                boolean discoverServicesEnable = gatt.discoverServices();
                PrintUtil.print("Attempting to start service discovery:" + discoverServicesEnable);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //  断开连接
                gatt.disconnect();
                gatt.close();

                //  发送广播通知
                intentAction = ACTION_GATT_DISCONNECTED;
                PrintUtil.print("Disconnected from GATT server.");
                broadcastUpdate(address, intentAction);
            } else {
                //  断开连接
                gatt.disconnect();
                gatt.close();

                PrintUtil.print("other state from GATT server: " + newState);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("描述读取之后的回调  onDescriptorRead()" + (status == BluetoothGatt.GATT_SUCCESS) + " address:" + address);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("描述写入之后的回调  onDescriptorWrite()" + (status == BluetoothGatt.GATT_SUCCESS) + " address:" + address);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("给定设备连接的MTU已改变的回调  onMtuChanged() " + (status == BluetoothGatt.GATT_SUCCESS) + " address:" + address);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("报告远程设备连接的RSSI的回调   onReadRemoteRssi() " + (status == BluetoothGatt.GATT_SUCCESS) + " address:" + address);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("可写入的事物完成时的回调  onReliableWriteCompleted() " + (status == BluetoothGatt.GATT_SUCCESS) + " address:" + address);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String address = gatt.getDevice().getAddress();
            PrintUtil.print("远程设备的远程服务，特性和描述符列表已被更新，即已经发现新服务时调用回调  onServicesDiscovered() " + address);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(address, ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                PrintUtil.print("onServicesDiscovered received: " + status);
            }
        }


    };

    private void broadcastUpdate(String address, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_ADDRESS, address);
        sendBroadcast(intent);
    }

    private void broadcastFailed(String address, String action, BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_TYPE, characteristic.getUuid().toString());
        intent.putExtra(EXTRA_ADDRESS, address);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String address, final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        //  在这里处理信息并通过广播传递给注册的页面

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        String uuid = characteristic.getUuid().toString();
        if (BluetoolGattAttributes.FFE1.equals(uuid)) {
            //  时间读取
            String str = dispatchData(characteristic.getValue(), false);
            if (str != null) {
                PrintUtil.printCZ("时间读取或写入:" + str);
                intent.putExtra(EXTRA_DATA, str);
                intent.putExtra(EXTRA_TYPE, uuid);
            }
        } else if (BluetoolGattAttributes.FFE2.equals(uuid)) {
            //  时间提醒
            String str = dispatchData(characteristic.getValue(), false);
            if (str != null) {
                PrintUtil.printCZ("设置提醒:" + str);
                intent.putExtra(EXTRA_DATA, str);
                intent.putExtra(EXTRA_TYPE, uuid);
            }
        } else if (BluetoolGattAttributes.FFE3.equals(uuid)) {
            //  速率读取
            String str = dispatchData(characteristic.getValue(), false);
            if (str != null) {
                PrintUtil.printCZ("速率读取或写入:" + str);
                intent.putExtra(EXTRA_DATA, str);
                intent.putExtra(EXTRA_TYPE, uuid);
            }
        } else if (BluetoolGattAttributes.FFF1.equals(uuid)) {
            //  时间读取
            String str = dispatchData(characteristic.getValue(), true);
            if (str != null) {
                PrintUtil.printCZ("读取动作:" + str);
                intent.putExtra(EXTRA_DATA, str);
                intent.putExtra(EXTRA_TYPE, uuid);
            }
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        intent.putExtra(EXTRA_ADDRESS, address);
        sendBroadcast(intent);
    }

    private String dispatchData(byte[] value, boolean isAction) {
        if (value != null && value.length > 0) {
            final StringBuffer stringBuffer = new StringBuffer(value.length);
            final int n = isAction ? 2 : 0;  //  如果是传送动作数据则对最后两位做特殊处理
            for (int i = 0; i < value.length; i++) {
                if (i < value.length - n) {
                    int b = BluetoothDeviceUtil.bcd2int(value[i]);
                    if (i == value.length - 3) {
                        b = (int) value[i];
                    }
                    stringBuffer.append(b + " ");
                } else {
                    stringBuffer.append(BluetoothDeviceUtil.turn16to10(String.format("%02x%02x", value[value.length - 1], value[value.length - 2])));
                    break;
                }
            }
            return stringBuffer.toString();
        }
        return null;
    }

    public class LocalBinder extends Binder {
        public BluetoothMultiDeviceService getService() {
            return BluetoothMultiDeviceService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }


    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {

                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        if (getBluetoothAdapter() == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    private BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
        return mBluetoothAdapter;
    }

    public boolean connect(final String address) {
        if (getBluetoothAdapter() == null || TextUtils.isEmpty(address)) {
            PrintUtil.printCZ("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            PrintUtil.printCZ("the address is invalid.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        //  连接之前保存的设备地址
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            gattArrayMap.remove(address);
        }

        final BluetoothDevice device = getBluetoothAdapter().getRemoteDevice(address);
        if (device == null) {
            PrintUtil.printCZ("Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        device.connectGatt(this, false, mGattCallback);
        PrintUtil.printCZ("Trying to create a new connection." + device.toString());
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Iterator<String> iterator = gattArrayMap.keySet().iterator();
        while (iterator.hasNext()) {
            String address = iterator.next();
            BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
            }
        }
    }

    public void disconnect(String address) {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        Iterator<String> iterator = gattArrayMap.keySet().iterator();
        while (iterator.hasNext()) {
            String address = iterator.next();
            BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
            if (bluetoothGatt != null) {
                bluetoothGatt.close();
            }
        }
        gattArrayMap.clear();
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     * 读取设备的信息，信息则由BluetoothGattCallback#onCharacteristicRead()获取
     *
     * @param characteristic The characteristic to read from.
     */
    private boolean readCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (getBluetoothAdapter() == null || bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        boolean readEnable = bluetoothGatt.readCharacteristic(characteristic);
        return readEnable;
    }

    public boolean readCharacteristic(String address, String serviceUUID, String characteristicUUID) {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        BluetoothGattCharacteristic characteristic = getSupportedGattCharacteristic(address, serviceUUID, characteristicUUID);
        if (characteristic == null) return false;
        return readCharacteristic(bluetoothGatt, characteristic);
    }

    /**
     * 写入信息
     *
     * @param characteristic
     */
    private boolean writeCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (getBluetoothAdapter() == null || bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        boolean writeEnable = bluetoothGatt.writeCharacteristic(characteristic);
        PrintUtil.printCZ("writeEnable: " + writeEnable);
        return writeEnable;
    }

    public boolean writeCharacteristic(String address, String serviceUUID, String characteristicUUID, byte[] date) {
        if (TextUtils.isEmpty(serviceUUID) || TextUtils.isEmpty(characteristicUUID)) return false;
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        BluetoothGattCharacteristic characteristic = getSupportedGattCharacteristic(address, serviceUUID, characteristicUUID);
        if (characteristic == null) return false;
        characteristic.setValue(date);
        return writeCharacteristic(bluetoothGatt, characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     * 设置连接的设备是否发送通知
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    private boolean setCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothGatt not conn");
            return false;
        }
        boolean notifyResult = bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        PrintUtil.print("设置通知状态：" + notifyResult + "  enabled:" + enabled);

//        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                UUID.fromString(BluetoolGattAttributes.ENABLE_NOTIFY));
//        if(descriptor != null) {
//            descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            return mBluetoothGatt.writeDescriptor(descriptor);
//        }
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
        if (descriptors != null) {
            for (BluetoothGattDescriptor bluetoothGattDescriptor : descriptors) {
                bluetoothGattDescriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean writeDescriptor = bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
                PrintUtil.printCZ("设置了 writeDescriptor:" + writeDescriptor);
            }
        }
        return false;
    }

    //BluetoothGatt: onCharacteristicRead() - Device=7C:EC:79:45:36:BB handle=62 Status=0
    //
    public boolean setCharacteristicNotification(String address, String serviceUUID, String characteristicUUID, boolean enabled) {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGattCharacteristic characteristic = getSupportedGattCharacteristic(address, serviceUUID, characteristicUUID);
        if (characteristic == null) return false;
        return setCharacteristicNotification(address, characteristic, enabled);
    }

    public BluetoothGattCharacteristic getSupportedGattCharacteristic(String address, String serviceUUID, String characteristicUUID) {
        if (getBluetoothAdapter() == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return null;
        }
        List<BluetoothGattService> services = getSupportedGattServices(address);
        if (services == null) return null;
        for (BluetoothGattService service : services) {
            if (serviceUUID.equals(service.getUuid().toString())) {
                return service.getCharacteristic(UUID.fromString(characteristicUUID));
            }
        }
        return null;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     * 获取连接的设备支持的属性
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices(String address) {
        BluetoothGatt bluetoothGatt = gattArrayMap.get(address);
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothGatt not conn");
            return null;
        }
        return bluetoothGatt.getServices();
    }
}
