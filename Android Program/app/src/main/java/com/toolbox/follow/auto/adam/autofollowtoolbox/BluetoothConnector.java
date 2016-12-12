package com.toolbox.follow.auto.adam.autofollowtoolbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Adam on 10/25/2016.
 */
public class BluetoothConnector extends AsyncTask<Void, Void, Void> {

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private boolean btConnected = false;

    // SPP UUID service
    private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    private static String macAddress = "00:15:FF:F3:27:01";


    private ProgressDialog progress;
    private AppCompatActivity activity;
    private Context context;

    public BluetoothConnector(AppCompatActivity activity) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.activity = activity;
        context = activity;
        progress = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        progress.setTitle("Connecting");
        progress.setMessage("Please wait while connecting to robot...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        connectBT();
        System.out.println("Background");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        System.out.println("Post Execute");

        if(progress.isShowing()) {
            progress.dismiss();
        }
    }

    public void connectBT() {



        BluetoothDevice remoteDevice = null;
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device: pairedDevices){
                if(device.getName().equalsIgnoreCase("AEGIN")){
                    remoteDevice = device;
                    break;
                }
            }
        }

        try {
            btSocket = remoteDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
            btSocket.connect();
            outStream = btSocket.getOutputStream();
            btConnected = true;
        }
        catch(IOException e){
            System.out.println("Error Connecting");
        }
    }

    public BluetoothAdapter getAdapter(){
        return btAdapter;
    }

    public OutputStream getOutStream() {
        return outStream;
    }

    public boolean isConnected() {
        return btConnected;
    }
}
