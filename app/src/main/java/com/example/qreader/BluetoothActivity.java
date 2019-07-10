package com.example.qreader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

//import android.widget.AdapterView
//import android.widget.AdapterView.OnClickListener

public class BluetoothActivity extends AppCompatActivity {

    Button btnPaired;
    ListView devicelist;
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private String scannedresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        TextView textView = (TextView)findViewById(R.id.textView);

        Intent newint = getIntent();
        scannedresult = newint.getStringExtra("ScannedResult");

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = myBluetooth.getBondedDevices();

        textView.setText("Make sure you are paired to Bluetooth device named "+scannedresult);
        //ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                //list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                if(bt.getName().equals(scannedresult)){

                    FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    //Storing profile object in the database

                        GunInfo gunInfo = new GunInfo(CurrentUser.getEmail(),bt.getName());
                        myRef.child("ProfileInfo").child(CurrentUser.getUid()).child(bt.getAddress()).setValue(gunInfo);



                    // Get the device MAC address, the last 17 chars in the View
                    //String info = ((TextView) v).getText().toString();
                    String address = bt.getAddress();
                    // Make an intent to start next activity.
                    Intent i = new Intent(BluetoothActivity.this, TriggerControlActivity.class);
                    //Change the activity.
                    i.putExtra("EXTRA_ADDRESS", address); //this will be received at ledControl (class) Activity
                    startActivity(i);


                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
    }

}
