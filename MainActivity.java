package com.example.myapplication5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView getinfo, txtRam, txtBattery, imeitxt;
    TelephonyManager tm;
    Button btngetinfo;
    String imei;
    long bytesAvailable, megAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        StatFs statFs=new StatFs(Environment.getDownloadCacheDirectory().getPath());
        bytesAvailable=statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        megAvailable=bytesAvailable/(1024*1024);
        imeitxt=findViewById(R.id.imeitxt);
        txtBattery=findViewById(R.id.txtBattery);
        registerReceiver(this.mBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;

        }
    }
    private BroadcastReceiver mBattery=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            float batteryPer=level*100/(float)scale;
            txtBattery.setText("Battery Status: "+batteryPer+" %");
        }
    };

    private void initView() {
        getinfo=findViewById(R.id.setInformation);
        btngetinfo=findViewById(R.id.getInformation);
        txtRam=findViewById(R.id.txtRam);
        tm=(TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        btngetinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String information=getSystemInfo();
                imeitxt.setText("IMEI: "+imei+"\n");


                getinfo.setText(information);
                txtRam.setText(getMemoryInfo());
            }
        });

    }

    private String getMemoryInfo() {
        Context context=getApplicationContext();
        ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        DecimalFormat twoDecimalFormate=new DecimalFormat("#.##");

        String finalValue="";
        long totalMemory=memoryInfo.totalMem;

        double kb=totalMemory/1024.0;
        double mb=totalMemory/1048576.0;
        double gb=totalMemory/1073741824.0;
        double tb=totalMemory/1099511627776.0;

        if(tb>1){
            finalValue=twoDecimalFormate.format(tb).concat(" TB");
        }
        else if(gb>1){
            finalValue=twoDecimalFormate.format(gb).concat(" GB");
        }
        else if(mb>1){
            finalValue=twoDecimalFormate.format(mb).concat(" MB");
        }
        else if(kb>1){
            finalValue=twoDecimalFormate.format(kb).concat(" KB");
        }
        else{
            finalValue=twoDecimalFormate.format(totalMemory).concat(" BYTES");
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("RAM: ").append(finalValue).append("\n")
                .append("Available Internal/External free space: "+ megAvailable +" MB");
        return stringBuilder.toString();
    }
    /*private String getImei() {
        return

    }*/

    private String getSystemInfo() {

        return "Model: "+" "+ Build.MODEL+"\n" +
                "ID: "+" "+ Build.ID+"\n"+
                "Manufacturer: "+" "+ Build.MANUFACTURER+"\n"+
                "Brand: "+" "+ Build.BRAND+"\n"+
                "Type: "+" "+ Build.TYPE+"\n"+
                "User: "+" "+ Build.USER+"\n"+
                "Base: "+" "+ Build.VERSION_CODES.BASE+"\n"+
                "SDK: "+" "+ Build.VERSION.SDK+"\n"+
                "Board: "+" "+ Build.BOARD+"\n"+
                "Host: "+" "+ Build.HOST+"\n"+
                "Fingerprint: "+" "+ Build.FINGERPRINT+"\n"+
                "VersionCode: "+" "+ Build.VERSION.RELEASE+"\n";

    }
}