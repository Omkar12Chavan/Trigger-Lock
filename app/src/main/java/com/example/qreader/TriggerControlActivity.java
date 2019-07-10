package com.example.qreader;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class TriggerControlActivity extends AppCompatActivity {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private ProgressDialog progress;
    private boolean isBtConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trigger_control);
        super.onCreate(savedInstanceState);

        //receive the address of the bluetooth device
        Intent newint = getIntent();
        address = newint.getStringExtra("EXTRA_ADDRESS");
        msg(address);

        //call the widgtes
        btnOn = (Button) findViewById(R.id.on);
        btnOff = (Button) findViewById(R.id.off);
        btnDis = (Button) findViewById(R.id.disc);

        new ConnectBT().execute(); //Call the class to connect

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("ON");

                if (btSocket != null) {
                    msg("SOCKET ON");
                    try {
                        String str = new String();
                        btSocket.getOutputStream().write("1".getBytes());
                        for (byte b : "1".getBytes()) {
                            str += b;
                        }
                        Toast.makeText(TriggerControlActivity.this, str, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        msg("Error");
                    }
                }      //method to turn on
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("OFF");
                if (btSocket != null) {
                    msg("SOCKET OFF");
                    try {
                        String str = new String();
                        btSocket.getOutputStream().write("0".getBytes());
                        for (byte b : "0".getBytes()) {
                            str += b;
                        }
                        Toast.makeText(TriggerControlActivity.this, str, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        msg("Error");
                    }
                }   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btSocket != null) //If the btSocket is busy
                {
                    try {
                        btSocket.close(); //close connection
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
                Intent intent=new Intent(TriggerControlActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(TriggerControlActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }


        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }


}

