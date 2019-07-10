package com.example.qreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                //try {
                //converting the data to json
                //  JSONObject obj = new JSONObject(result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ScanActivity.this, BluetoothActivity.class);
                intent.putExtra("ScannedResult",result.getContents());
                startActivity(intent);

                //} catch (JSONException e) {
                //e.printStackTrace();
                //if control comes here
                //that means the encoded format not matches
                //in this case you can display whatever data is available on the qrcode
                //to a toast
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                //}
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /*
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }*/
}