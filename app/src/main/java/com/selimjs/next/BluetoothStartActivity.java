package com.selimjs.next;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class BluetoothStartActivity extends AppCompatActivity
{

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mBluetoothSocket = null;
    TextView supported, pariedRefresh;
    Switch bluetoothBtn;
    ListView pairedList;
    Button checkConnection;

    ArrayList arrayListpaired, MAC, name;
    String device_name, deviceMAC;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_start);
        getSupportActionBar().setTitle("Bluetooth");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        supported = findViewById(R.id.supported);
        pariedRefresh = findViewById(R.id.textView4);
        bluetoothBtn = findViewById(R.id.BluetoothBtn);
        pairedList = findViewById(R.id.pairedList);
        checkConnection = findViewById(R.id.checkConnection);

        arrayListpaired = new ArrayList();
        MAC = new ArrayList();
        name = new ArrayList();

        clearpairedList();
        if (mBluetoothAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        } else {
            supported.setText("Bluetooth is supported");
        }

        if (mBluetoothAdapter.isEnabled()) {
            bluetoothBtn.setChecked(true);
            getPairedList();
        } else {
            bluetoothBtn.setChecked(false);
        }

        bluetoothBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mBluetoothAdapter.isEnabled()) {
                    /*Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, REQUEST_ENABLE_BT);*/
                    bluetoothBtn.setChecked(true);
                    mBluetoothAdapter.enable();
                } else {
                    bluetoothBtn.setChecked(false);
                    mBluetoothAdapter.disable();
                    clearpairedList();
                }
            }
        });

        pariedRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    getPairedList();
                } else {
                    clearpairedList();
                }
            }
        });

        pairedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBluetoothAdapter.isEnabled()) {
                    if (mBluetoothSocket == null) {
                        deviceMAC = MAC.get(position).toString();
                        device_name = name.get(position).toString();

                        try {
                            BluetoothDevice hc05 = mBluetoothAdapter.getRemoteDevice(deviceMAC);
                            System.out.println("" + hc05.getName());

                            mBluetoothSocket = hc05.createInsecureRfcommSocketToServiceRecord(myUUID);
                            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                            mBluetoothSocket.connect();
                            System.out.println("device connected successfully");

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("connection falied");
                        }
                    } else {
                        try {
                            mBluetoothSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mBluetoothSocket = null;

                    }
                    // check function
                    /*if (mBluetoothSocket != null) {
                        try {
                            mBluetoothSocket.getOutputStream().write("a".toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            }
        });

        checkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mBluetoothSocket.isConnected()) {
                        new AlertDialog.Builder(BluetoothStartActivity.this)
                                .setTitle("Bluetooth status")
                                .setMessage("Bluetoth is connected")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                } catch (Exception e) {
                    new AlertDialog.Builder(BluetoothStartActivity.this)
                            .setTitle("Bluetooth status")
                            .setMessage("Bluetoth isn't connected")
                            .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        });
    }

    private void getPairedList() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            arrayListpaired.clear();
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                arrayListpaired.add(deviceName+"\n"+deviceHardwareAddress);
                name.add(deviceName);
                MAC.add(deviceHardwareAddress);
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayListpaired);
            pairedList.setAdapter(adapter);
        } else {
            clearpairedList();
        }
    }

    private void clearpairedList() {
        arrayListpaired.clear();
        arrayListpaired.add("no paired devices");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayListpaired);
        pairedList.setAdapter(adapter);
    }
    
    public void showToast(String mess)
    {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBluetoothAdapter.isEnabled() && mBluetoothSocket != null) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}