package com.selimjs.next;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class ControlActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket mBluetoothSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Switch toggle1, toggle2, toggle3;
    Button forward, left, right, backward, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        getSupportActionBar().setTitle("Control");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        toggle1 = findViewById(R.id.toggle1);
        toggle2 = findViewById(R.id.toggle2);
        toggle3 = findViewById(R.id.toggle3);
        forward = findViewById(R.id.forward);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        backward = findViewById(R.id.backward);
        stop = findViewById(R.id.stop);

        if (!mBluetoothAdapter.isEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Bluetooth")
                    .setMessage("Turn on bluetooth to use Control")
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
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            String name, deviceMAC = "98:D3:31:F9:EA:90"; //"98:D3:51:F5:DC:F2";
            for (BluetoothDevice bt: devices) {
                if (bt.getName() == "HC-05") {
                    name = bt.getName();
                    deviceMAC = bt.getAddress();
                }
            }

            BluetoothDevice hc05 = mBluetoothAdapter.getRemoteDevice(deviceMAC);
            try {
                mBluetoothSocket = hc05.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData("F");
                }
            });

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData("L");
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData("R");
                }
            });

            backward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData("B");
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData("S");
                }
            });

            toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (toggle1.isChecked()) {
                        sendData("1");
                    } else {
                        sendData("2");
                    }
                }
            });

            toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (toggle2.isChecked()) {
                        sendData("3");
                    } else {
                        sendData("4");
                    }
                }
            });

            toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (toggle3.isChecked()) {
                        sendData("5");
                    } else {
                        sendData("6");
                    }
                }
            });
        }
    }

    private void sendData(String s) {
        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket.getOutputStream().write(s.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
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