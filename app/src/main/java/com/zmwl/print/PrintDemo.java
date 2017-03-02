package com.zmwl.print;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.text.ParseException;

/**
 * Android蓝牙打印
 */
public class PrintDemo extends Activity {
    Button btnSearch;
    Button btnSendDraw;
    Button btnSend;
    Button btnClose;
    EditText edtContext;
    EditText edtPrint;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //获取设备消息


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mService = new BluetoothService(this, mHandler);
        //蓝牙不可用退出程序
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //蓝牙未打开，打开蓝牙
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        try {
            btnSendDraw = (Button) this.findViewById(R.id.btn_test);
            btnSendDraw.setOnClickListener(new ClickEvent());
            btnSearch = (Button) this.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(new ClickEvent());
            btnSend = (Button) this.findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new ClickEvent());
            btnClose = (Button) this.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new ClickEvent());
            edtContext = (EditText) findViewById(R.id.txt_content);
            btnClose.setEnabled(false);
            btnSend.setEnabled(false);
            btnSendDraw.setEnabled(false);
        } catch (Exception ex) {
            Log.e("出错信息", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnSearch) {
                //搜索蓝牙设备
                Intent serverIntent = new Intent(PrintDemo.this, DeviceListActivity.class);      //运行另外一个类的活动
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            } else if (v == btnSend) {
                //开启打印
                //打印的信息
                StringBuffer buffer = new StringBuffer();
                try {
                    String currTime = DateUtils.getStandardDate(System.currentTimeMillis()); //当前时间
                    buffer.append("下单时间：").append(currTime).append("\n\n");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String title = PrintUtils.printTitle("桌号:001");
                buffer.append(title).append("\n\n");
                String msg1 = PrintUtils.printThreeData("菜单", "单价", "数量");
                buffer.append(msg1).append("\n");
                String msg2 = PrintUtils.printStar("海鲜类");
                buffer.append(msg2).append("\n");
                String msg3 = PrintUtils.printThreeData("辣炒蚬子", "36.0", "2");
                buffer.append(msg3).append("\n");
                String msg4 = PrintUtils.printStar("大锅菜");
                buffer.append(msg4).append("\n");
                String msg5 = PrintUtils.printThreeData("小鸡炖蘑菇", "48.0", "2");
                buffer.append(msg5).append("\n");
                String msg6 = PrintUtils.printThreeData("糖醋鱼", "86.0", "1");
                buffer.append(msg6).append("\n");
                String msg7 = PrintUtils.printThreeData("铁锅焖鱼头", "68.0", "1");
                buffer.append(msg7).append("\n\n");
                String msg8 = PrintUtils.printTitle("用餐人数：1人");
                buffer.append(msg8).append("\n");
                buffer.append("周旭的小票").append("\n'");
                String msg = buffer.toString();



                // String msg = edtContext.getText().toString();
                if (msg.length() > 0) {
                    mService.sendMessage(msg + "\n", "GBK");
                }
            } else if (v == btnClose) {
                mService.stop();
            } else if (v == btnSendDraw) {
                String msg = "";
                String lang = getString(R.string.strLang);
                //printImage();

                byte[] cmd = new byte[3];
                cmd[0] = 0x1b;
                cmd[1] = 0x21;
                if ((lang.compareTo("en")) == 0) {
                    cmd[2] |= 0x10;
                    mService.write(cmd);           //倍宽、倍高模式
                    mService.sendMessage("Congratulations!\n", "GBK");
                    cmd[2] &= 0xEF;
                    mService.write(cmd);           //取消倍高、倍宽模式
                    msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                            + "  the company is a high-tech enterprise which specializes" +
                            " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";


                    mService.sendMessage(msg, "GBK");
                } else if ((lang.compareTo("ch")) == 0) {
                    cmd[2] |= 0x10;
                    mService.write(cmd);           //倍宽、倍高模式
                    mService.sendMessage("恭喜您！\n", "GBK");
                    cmd[2] &= 0xEF;
                    mService.write(cmd);           //取消倍高、倍宽模式
                    msg = "  您已经成功的连接上了我们的蓝牙打印机！\n\n"
                            + "  本公司是一家专业从事研发，生产，销售商用票据打印机和条码扫描设备于一体的高科技企业.\n\n";

                    mService.sendMessage(msg, "GBK");
                }
            }
        }
    }

    /**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //已连接
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            btnClose.setEnabled(true);
                            btnSend.setEnabled(true);
                            btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //正在连接
                            Log.i("Bluetooth", ".....is connecting");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.i("Bluetooth", ".....wait connecting");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    btnClose.setEnabled(false);
                    btnSend.setEnabled(false);
                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                //请求打开蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    //蓝牙已经打开
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    //用户不允许打开蓝牙
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                //请求连接某一蓝牙设备
                if (resultCode == Activity.RESULT_OK) {
                    //已点击搜索列表中的某个设备项
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //获取列表项中设备的mac地址
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }


    //打印图形
    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);   //打印byte流数据
    }
}
