package com.example.infinity.test;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;

import java.net.Socket;
import java.util.UUID;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.lang.String;

import android.os.ParcelUuid;

public class FirstActivity extends ListActivity {

    private static final int REQUEST_COARSE_LOCATION = 900 ;
    private Button b1, b2, b3, b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;


    ListView lv;
    ListView lv2;

    ArrayList al = new ArrayList();

    ArrayList<BluetoothDevice> list = new ArrayList();
    ArrayList list1 = new ArrayList();
    List<BluetoothDevice> bluetoothList = new ArrayList<BluetoothDevice>();

    ArrayAdapter<String> adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.list_view_id);
        //lv2 = (ListView) findViewById(R.id.list);
        lv2 = getListView();

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list1);
        // New code for communication
        setListAdapter(adapter);




        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast("Selected");
                Log.d("Device Selected: " , ""+bluetoothList.get(position));
                Intent in = new Intent(FirstActivity.this,MapActivity.class);
                in.putExtra("device", bluetoothList.get(position));
                startActivity(in);
                //connectThread = new ConnectThread(bluetoothList.get(position));
            }
        });
        //lv2.setAdapter(adapter);
    }


    public void Turn_ON_button(View view) {

        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned On", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already On", Toast.LENGTH_LONG).show();
        }

    }


    public void Turn_OFF_button(View view) {

        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();

    }


    public void Get_Visible_button(View view) {

        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);

    }

    // The “List_Devices_button” method will query about the set of paired devices to see if the device is already known.


    public void List_Devices_button(View view) {


        pairedDevices = BA.getBondedDevices(); // This is to see if the desired device is already known (This information comes from the API, that has the value stored in it)

        for (BluetoothDevice bt : pairedDevices)
        {
            list.add(bt);
            al.add(bt.getName());
        }
        //list.add(bt.getName() + "\n" + bt.getAddress());  // This line of code will get the name and address of the devices that were previously connected to this device (i.e. paired devices)

        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, al);
        lv.setAdapter(adapter2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(FirstActivity.this,MapActivity.class);
                in.putExtra("device", list.get(i));
                startActivity(in);
            }
        });
    }

    // “The Find_Device_button” method will start the discovery of new devices.


    public void Find_Device_button(View view) {

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        BA.startDiscovery();


    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                showToast("job started");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismiss progress dialog
                for(int i =0; i < bluetoothList.size();i++){
                    //    Log.d(bluetoothList.get(i).getName(),bluetoothList.get(i).EXTRA_UUID.toString());
                }
                showToast("job finished");
            }
            if(BluetoothDevice.ACTION_UUID.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                for (int i=0; i<uuidExtra.length; i++) {
                    Log.d(" Device: " + device.getName() ,  uuidExtra[i].toString());
                }
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                Log.d("Device"," found");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                bluetoothList.add(device);
                list1.add(device.getName() +"\n" + device.getAddress());
//                Log.d(device.getName(),device.getUuids().toString());
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    protected void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    BA.startDiscovery(); // --->
                    BA.startDiscovery();
                } else {
                    //TODO re-request
                }
                break;
            }
        }
    }


}

