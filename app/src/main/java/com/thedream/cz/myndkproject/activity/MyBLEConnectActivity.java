package com.thedream.cz.myndkproject.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bluetool.BluetoolGattAttributes;
import com.thedream.cz.myndkproject.bluetool.BluetoothService;
import com.thedream.cz.myndkproject.utils.BluetoothDeviceUtil;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyBLEConnectActivity extends AppCompatActivity {

    private TextView tvDevice;
    private Button btnConnection;
    private Button btnDisConnection;
    private TextView tvInfo;
    private Button btnRTime;
    private Button btnWTime;
    private Button btnTTime;

    private BluetoothService mBluetoothLeService;
    private String mDeviceAddress;
    private boolean isConnect = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            PrintUtil.printCZ("onServiceConnected");
            mBluetoothLeService = ((BluetoothService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                PrintUtil.print("Unable to initialize Bluetooth");
                MyBLEConnectActivity.this.finish();
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            PrintUtil.printCZ("onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };
    private EditText etText;
    private Button btnNTime;
    private Button btnATime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bleconnect);

        tvDevice = (TextView) findViewById(R.id.tv_device);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        etText = (EditText) findViewById(R.id.et_text);
        btnConnection = (Button) findViewById(R.id.btn_connection);
        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection();
            }
        });
        btnDisConnection = (Button) findViewById(R.id.btn_disconnection);
        btnDisConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnection();
            }
        });
        btnRTime = (Button) findViewById(R.id.btn_time_r);
        btnRTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTime();
            }
        });
        btnWTime = (Button) findViewById(R.id.btn_time_w);
        btnWTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeTime();
            }
        });
        btnTTime = (Button) findViewById(R.id.btn_time_t);
        btnTTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindTime();
            }
        });
        btnATime = (Button) findViewById(R.id.btn_time_a);
        btnATime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptTime();
            }
        });
        btnNTime = (Button) findViewById(R.id.btn_time_n);
        btnNTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyTime();
            }
        });


        Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(BluetoothDevice.class.getSimpleName());
        tvDevice.setText("我的设备: " + mDeviceAddress);
    }


    private void connection() {
        btnConnection.setText("连接中...");
        // Automatically connects to the device upon successful start-up initialization.
        boolean connect = mBluetoothLeService.connect(mDeviceAddress);
        PrintUtil.printCZ("连接状态: " + connect);
    }

    private void disconnection() {
        mBluetoothLeService.disconnect();
    }

    private void readTime() {
        if (!isConnect) return;
        mBluetoothLeService.readCharacteristic(BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1);
    }

    private void writeTime() {
        if (!isConnect) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println("现在时间是：" + new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = zeroFill(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        String day = zeroFill(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        String week = zeroFill(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1));
        String hour = zeroFill(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        String minute = zeroFill(String.valueOf(calendar.get(Calendar.MINUTE)));
        String second = zeroFill(String.valueOf(calendar.get(Calendar.SECOND)));
        PrintUtil.printCZ("year：" + year + " month：" + month + " day：" + day + " week：" + week
                + " hour：" + hour + " minute：" + minute + " second：" + second);
        String time = second + minute + hour + week + day + month + year.substring(2);
        PrintUtil.print("time:" + time);
        byte[] bcd = BluetoothDeviceUtil.str2Bcd(time);
        PrintUtil.print("bcd size:" + bcd.length);
        mBluetoothLeService.writeCharacteristic(BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1, bcd);
    }

    private String zeroFill(String data) {
        if (data.length() == 1) data = "0" + data;
        return data;
    }

    private void remindTime() {
        if (!isConnect) return;
        byte[] data = new byte[5];
        data[0] = (byte) BluetoothDeviceUtil.int2bcd(11);
        data[1] = (byte) BluetoothDeviceUtil.int2bcd(13);
        data[2] = (byte) BluetoothDeviceUtil.int2bcd(30);
        data[3] = (byte) BluetoothDeviceUtil.int2bcd(17);
        data[4] = (byte) BluetoothDeviceUtil.int2bcd(50);
        mBluetoothLeService.writeCharacteristic(BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE2, data);
    }


    private void notifyTime() {
        if (!isConnect) return;
        mBluetoothLeService.setCharacteristicNotification(BluetoolGattAttributes.FFF0, BluetoolGattAttributes.FFF1, true);
    }

    /**
     * 远程设备特征改变之后发出通知的回调  onCharacteristicChanged
     * 读取动作:3   21 24 12 15 15 20 11 17   18 21 00
     * 操作数据成功
     */
    private void acceptTime() {
        if (!isConnect) return;
        mBluetoothLeService.readCharacteristic(BluetoolGattAttributes.FFF0, BluetoolGattAttributes.FFF1);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                PrintUtil.printCZ("远程设备连接");
                isConnect = true;
                showConnectType();
            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnect = false;
                PrintUtil.printCZ("远程设备连接断开");
                showConnectType();
            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                PrintUtil.printCZ("远程设备服务发现");
                dispatchService(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
                PrintUtil.printCZ("操作数据成功");
                String data = intent.getStringExtra(BluetoothService.EXTRA_DATA);
                String type = intent.getStringExtra(BluetoothService.EXTRA_TYPE);
                if (TextUtils.isEmpty(data) || TextUtils.isEmpty(type)) return;
                String[] split = data.split(" ");
                if (split == null || split.length == 0) return;
                displayData(type, split);
            } else if (BluetoothService.ACTION_DATA_FAILED.equals(action)) {
                PrintUtil.printCZ("操作数据失败");
                String type = intent.getStringExtra(BluetoothService.EXTRA_TYPE);
                if (TextUtils.isEmpty(type)) return;
                dispatchFailed(type);
            }
        }
    };

    private void dispatchFailed(String type) {
        if (BluetoolGattAttributes.FFE1.equals(type)) {
            Toast.makeText(this, "读取或设置时间失败", Toast.LENGTH_SHORT).show();
        } else if (BluetoolGattAttributes.FFE2.equals(type)) {
            Toast.makeText(this, "设置提醒失败", Toast.LENGTH_SHORT).show();
        } else if (BluetoolGattAttributes.FFE3.equals(type)) {
            Toast.makeText(this, "设置最大长度失败", Toast.LENGTH_SHORT).show();
        } else if (BluetoolGattAttributes.FFF1.equals(type)) {
            Toast.makeText(this, "显示动作失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayData(String type, String[] data) {
        if (BluetoolGattAttributes.FFE1.equals(type)) {
            //  时间读取/设置
            if (data.length == 7) {
                try {
                    int year = Integer.parseInt("20" + data[6]);
                    int month = Integer.parseInt(data[5]) - 1;
                    int day = Integer.parseInt(data[4]);
                    int week = Integer.parseInt(data[3]);
                    int hour = Integer.parseInt(data[2]);
                    int minute = Integer.parseInt(data[1]);
                    int second = Integer.parseInt(data[0]);
                    PrintUtil.print("year:" + year + " month:" + month + " day:" + day + " week:" + week + " hour:" + hour + " minute:" + minute + " second:" + second);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minute, second);
                    tvInfo.setText("读取或设置时间:" + calendar.getTime());
                } catch (NumberFormatException e) {
                    PrintUtil.print("显示时间失败:" + e.toString());
                }
            }
        } else if (BluetoolGattAttributes.FFE2.equals(type)) {
            //  设置提醒  11 13 48 17 50
            tvInfo.setText("设置提醒成功");
        } else if (BluetoolGattAttributes.FFF1.equals(type)) {
            //  动作数据
            if (data.length == 10) {
                try {
                    int weight = Integer.parseInt(data[9]);
                    int temperature = Integer.parseInt(data[8]);
                    int year = Integer.parseInt("20" + data[7]);
                    int month = Integer.parseInt(data[6]) - 1;
                    int day = Integer.parseInt(data[5]);
                    int week = Integer.parseInt(data[4]);
                    int hour = Integer.parseInt(data[3]);
                    int minute = Integer.parseInt(data[2]);
                    int second = Integer.parseInt(data[1]);
                    int action = Integer.parseInt(data[0]);
                    PrintUtil.print("year:" + year + " month:" + month + " day:" + day + " week:" + week + " hour:" + hour + " minute:" + minute + " second:" + second);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minute, second);
                    tvInfo.setText("动作:" + action + " 时间：" + calendar.getTime() + " 温度:" + temperature + " 重量:" + weight + "g");
                } catch (NumberFormatException e) {
                    PrintUtil.print("显示动作失败:" + e.toString());
                }
            }
        }

    }

    private void showConnectType() {
        btnConnection.setText(isConnect ? "已连接" : "重新连接");
        btnConnection.setEnabled(!isConnect);
        btnDisConnection.setVisibility(!isConnect ? View.INVISIBLE : View.VISIBLE);
        if (!isConnect) {
            btnRTime.setEnabled(false);
            btnWTime.setEnabled(false);
            btnTTime.setEnabled(false);
            btnNTime.setEnabled(false);
            btnATime.setEnabled(false);
        }
    }

    private void dispatchService(List<BluetoothGattService> services) {
        PrintUtil.print("设备是否为空:" + (services == null));
        if (services != null) {
            PrintUtil.print("设备不为空: " + services.size());
            for (BluetoothGattService service : services) {
                if (hasTimeService(service) || hasActionService(service)) continue;
                PrintUtil.print("service uuid:" + service.getUuid().toString() + " type:" + service.getType());
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                if (characteristics == null) continue;
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    PrintUtil.printCZ("characteristic uuid:" + characteristic.getUuid().toString()
                            + " permissions:" + characteristic.getPermissions()
                            + " properties:" + characteristic.getProperties()
                            + " write type:" + characteristic.getWriteType());
                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    if (descriptors == null || descriptors.size() == 0) continue;
                    for (BluetoothGattDescriptor descriptor : descriptors) {
                        PrintUtil.print("descriptor uuid:" + descriptor.getUuid().toString());
                    }
                }
            }
        }
    }

    private boolean hasActionService(BluetoothGattService service) {
        if (BluetoolGattAttributes.FFF0.equals(service.getUuid().toString())) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                if (BluetoolGattAttributes.FFF1.equals(characteristic.getUuid().toString())) {
                    btnNTime.setEnabled(true);
                    btnATime.setEnabled(true);
                    notifyTime();
                }
            }
        }
        return false;
    }

    private boolean hasTimeService(BluetoothGattService service) {
        if (BluetoolGattAttributes.FFE0.equals(service.getUuid().toString())) {
            //  时间服务
            //service uuid:0000ffe0-0000-1000-8000-00805f9b34fb type:0
            //characteristic uuid:0000ffe1-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
            //characteristic uuid:0000ffe2-0000-1000-8000-00805f9b34fb permissions:0 properties:8 write type:2
            //characteristic uuid:0000ffe3-0000-1000-8000-00805f9b34fb permissions:0 properties:10 write type:2
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                if (BluetoolGattAttributes.FFE1.equals(characteristic.getUuid().toString())) {
                    btnRTime.setEnabled(true);
                    btnWTime.setEnabled(true);
                } else if (BluetoolGattAttributes.FFE2.equals(characteristic.getUuid().toString())) {
                    btnTTime.setEnabled(true);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothService.ACTION_DATA_FAILED);
        return intentFilter;
    }
}
