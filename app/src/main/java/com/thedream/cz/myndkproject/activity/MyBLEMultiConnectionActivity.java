package com.thedream.cz.myndkproject.activity;

import android.bluetooth.BluetoothDevice;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.bluetool.BluetoolGattAttributes;
import com.thedream.cz.myndkproject.bluetool.BluetoothMultiDeviceService;
import com.thedream.cz.myndkproject.bluetool.BluetoothService;
import com.thedream.cz.myndkproject.utils.BluetoothDeviceUtil;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thedream.cz.myndkproject.bluetool.BluetoothMultiDeviceService.EXTRA_ADDRESS;

public class MyBLEMultiConnectionActivity extends AppCompatActivity {

    private BluetoothMultiDeviceService mBluetoothLeService;

    @BindView(R.id.tv_multi_address)
    TextView tvAddress;
    @BindView(R.id.btn_conn_one)
    Button btnConnOne;
    @BindView(R.id.btn_disconn_one)
    Button btnDisconnOne;
    @BindView(R.id.btn_read_time_one)
    Button btnReadOne;
    @BindView(R.id.btn_write_time_one)
    Button btnWriteOne;
    @BindView(R.id.btn_conn_two)
    Button btnConnTwo;
    @BindView(R.id.btn_disconn_two)
    Button btnDisconnTwo;
    @BindView(R.id.btn_read_time_two)
    Button btnReadTwo;
    @BindView(R.id.btn_write_time_two)
    Button btnWriteTwo;
    @BindView(R.id.tv_multi_info)
    TextView tvInfo;
    @BindView(R.id.btn_conn_three)
    Button btnConnThree;
    @BindView(R.id.btn_disconn_three)
    Button btnDisconnThree;

    private String addressOne;
    private String addressTwo;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            PrintUtil.printCZ("onServiceConnected");
            mBluetoothLeService = ((BluetoothMultiDeviceService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                PrintUtil.print("Unable to initialize Bluetooth");
                MyBLEMultiConnectionActivity.this.finish();
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            PrintUtil.printCZ("onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };
    private String addressThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blemulti_connection);
        ButterKnife.bind(this);

        Intent gattServiceIntent = new Intent(this, BluetoothMultiDeviceService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        String mDeviceAddress = intent.getStringExtra(BluetoothDevice.class.getSimpleName());
        tvAddress.setText(mDeviceAddress);

        String[] addressArray = mDeviceAddress.split(",");
        addressOne = addressArray[0];
        addressTwo = addressArray[1];
        addressThree = addressArray[2];
    }

    @OnClick(R.id.btn_conn_one)
    public void connOne() {
        btnConnOne.setText("连接中...");
        boolean connect = mBluetoothLeService.connect(addressOne);
        PrintUtil.printCZ("连接状态1: " + connect);
    }

    @OnClick(R.id.btn_disconn_one)
    public void disconnOne() {
        mBluetoothLeService.disconnect(addressOne);
    }

    @OnClick(R.id.btn_read_time_one)
    public void readOne() {
        mBluetoothLeService.readCharacteristic(addressOne, BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1);
    }

    @OnClick(R.id.btn_write_time_one)
    public void writeOne() {
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
        mBluetoothLeService.writeCharacteristic(addressOne, BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1, bcd);
    }

    private String zeroFill(String data) {
        if (data.length() == 1) data = "0" + data;
        return data;
    }

    @OnClick(R.id.btn_conn_two)
    public void connTwo() {
        btnConnTwo.setText("连接中...");
        boolean connect = mBluetoothLeService.connect(addressTwo);
        PrintUtil.printCZ("连接状态2: " + connect);
    }

    @OnClick(R.id.btn_disconn_two)
    public void disconnTwo() {
        mBluetoothLeService.disconnect(addressTwo);
    }

    @OnClick(R.id.btn_read_time_two)
    public void readTwo() {
        mBluetoothLeService.readCharacteristic(addressTwo, BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1);
    }

    @OnClick(R.id.btn_write_time_two)
    public void writeTwo() {
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
        mBluetoothLeService.writeCharacteristic(addressTwo, BluetoolGattAttributes.FFE0, BluetoolGattAttributes.FFE1, bcd);
    }

    @OnClick(R.id.btn_conn_three)
    public void connThree() {
        btnConnThree.setText("连接中...");
        boolean connect = mBluetoothLeService.connect(addressThree);
        PrintUtil.printCZ("连接状态2: " + connect);
    }

    @OnClick(R.id.btn_disconn_three)
    public void disconnThree() {
        mBluetoothLeService.disconnect(addressThree);
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String address = intent.getStringExtra(EXTRA_ADDRESS);
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                PrintUtil.printCZ("远程设备连接");
                showConnectType(true, address);
            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                PrintUtil.printCZ("远程设备连接断开");
                showConnectType(false, address);
            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                PrintUtil.printCZ("远程设备服务发现" + address);
//                dispatchService(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
                PrintUtil.printCZ("操作数据成功");
                String data = intent.getStringExtra(BluetoothService.EXTRA_DATA);
                String type = intent.getStringExtra(BluetoothService.EXTRA_TYPE);
                if (TextUtils.isEmpty(data) || TextUtils.isEmpty(type)) return;
                String[] split = data.split(" ");
                if (split == null || split.length == 0) return;
                displayData(address, type, split);
            } else if (BluetoothService.ACTION_DATA_FAILED.equals(action)) {
                PrintUtil.printCZ("操作数据失败");
                String type = intent.getStringExtra(BluetoothService.EXTRA_TYPE);
                if (TextUtils.isEmpty(type)) return;
                dispatchFailed(address, type);
            }
        }
    };

    private void showConnectType(boolean isConn, String address) {
        PrintUtil.printCZ("远程设备:" + address + (isConn ? "  连接" : "  断开"));
        if (addressOne.equals(address)) {
            btnConnOne.setText(isConn ? "已连接" : "重新连接");
            btnConnOne.setEnabled(!isConn);
            btnDisconnOne.setEnabled(isConn);
            btnReadOne.setEnabled(isConn);
            btnWriteOne.setEnabled(isConn);
        } else if (addressTwo.equals(address)) {
            btnConnTwo.setText(isConn ? "已连接" : "重新连接");
            btnConnTwo.setEnabled(!isConn);
            btnDisconnTwo.setEnabled(isConn);
            btnReadTwo.setEnabled(isConn);
            btnWriteTwo.setEnabled(isConn);
        } else {
            btnConnThree.setText(isConn ? "已连接" : "重新连接");
            btnConnThree.setEnabled(!isConn);
            btnDisconnThree.setEnabled(isConn);
        }
    }

    private void displayData(String address, String type, String[] data) {
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
                    PrintUtil.print("设备:" + address + "year:" + year + " month:" + month + " day:" + day + " week:" + week + " hour:" + hour + " minute:" + minute + " second:" + second);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minute, second);
                    tvInfo.setText("设备:" + address + "读取或设置时间:" + calendar.getTime());
                } catch (NumberFormatException e) {
                    PrintUtil.print("设备:" + address + "显示时间失败:" + e.toString());
                }
            }
        } else if (BluetoolGattAttributes.FFE2.equals(type)) {
            //  设置提醒  11 13 48 17 50
            tvInfo.setText("设备:" + address + " 设置提醒成功");
        } else if (BluetoolGattAttributes.FFE3.equals(type)) {
            if (data.length == 1) {
                PrintUtil.printCZ("设备:" + address + "是否是高速模式" + data[0]);
                tvInfo.setText(("0".equals(data[0])) ? "设备:" + address + "当前连接处于低速模式" : "设备:" + address + "当前连接处于高速模式");
            }
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
                    PrintUtil.print("设备:" + address + "year:" + year + " month:" + month + " day:" + day + " week:" + week + " hour:" + hour + " minute:" + minute + " second:" + second);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minute, second);
                    tvInfo.setText("设备:" + address + "动作:" + action + " 时间：" + calendar.getTime() + " 温度:" + temperature + " 重量:" + weight + "g");
                } catch (NumberFormatException e) {
                    PrintUtil.print("设备:" + address + "显示动作失败:" + e.toString());
                }
            }
        }

    }

    private void dispatchFailed(String address, String type) {
        String content = address;
        if (BluetoolGattAttributes.FFE1.equals(type)) {
            content = "读取或设置时间失败";
        } else if (BluetoolGattAttributes.FFE2.equals(type)) {
            content = "设置提醒失败";
        } else if (BluetoolGattAttributes.FFE3.equals(type)) {
            content = "设置最大长度失败";
        } else if (BluetoolGattAttributes.FFF1.equals(type)) {
            content = "显示动作失败";
        }
        PrintUtil.printCZ("设备:" + address + " " + content);
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
