package com.toolbox.follow.auto.adam.autofollowtoolbox;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private BluetoothConnector bluetoothConnector;
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothConnector = new BluetoothConnector(this);

        //Check if device bluetooth enabled if not exit
        if (bluetoothConnector.getAdapter() == null) {
            finish();
        }

        //If adapter off.. Attempt to turn on
        if (!bluetoothConnector.getAdapter().isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }
        else {
            bluetoothConnector.execute(); //bluetooth on so go ahead and connect to robot
        }

        //Set up Listeners
        setupListeners();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if((requestCode == REQUEST_ENABLE_BT) && (resultCode == RESULT_OK) && !bluetoothConnector.isConnected()) {
            bluetoothConnector.execute(); //Now connect to robot
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("paused");
    }

    private void setupListeners() {

        ImageButton upArrow = (ImageButton) findViewById(R.id.upArrow);
        upArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new Thread(new Runnable() {
                    public void run() {
                        byte[] sendBuffer = "0".getBytes();
                        try {
                            bluetoothConnector.getOutStream().write(sendBuffer);
                        }
                        catch(IOException e){
                            System.out.println("Error Sending");
                        }
                    }
                }).start();

                return true;
            }
        });

        ImageButton rightArrow = (ImageButton) findViewById(R.id.rightArrow);
        rightArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new Thread(new Runnable() {
                    public void run() {
                        byte[] sendBuffer = "1".getBytes();
                        try {
                            bluetoothConnector.getOutStream().write(sendBuffer);
                        } catch (IOException e) {
                            System.out.println("Error Sending");
                        }
                    }
                }).start();

                return true;
            }
        });

        ImageButton downArrow = (ImageButton) findViewById(R.id.downArrow);
        downArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new Thread(new Runnable() {
                    public void run() {
                        byte[] sendBuffer = "2".getBytes();
                        try {
                            bluetoothConnector.getOutStream().write(sendBuffer);
                        }
                        catch(IOException e){
                            System.out.println("Error Sending");
                        }
                    }
                }).start();

                return true;
            }
        });

        ImageButton leftArrow = (ImageButton) findViewById(R.id.leftArrow);
        leftArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new Thread(new Runnable() {
                    public void run() {
                        byte[] sendBuffer = "3".getBytes();
                        try {
                            bluetoothConnector.getOutStream().write(sendBuffer);
                        }
                        catch(IOException e){
                            System.out.println("Error Sending");
                        }
                    }
                }).start();

                return true;
            }
        });

        Switch autoFollowOnOff = (Switch) findViewById(R.id.autoFollowSwitch);
        autoFollowOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final boolean checked = isChecked;
                new Thread(new Runnable() {
                    public void run() {
                        byte[] sendBuffer = "5".getBytes();

                        if(checked){
                            sendBuffer = "4".getBytes();
                        }

                        try {
                            bluetoothConnector.getOutStream().write(sendBuffer);
                        }
                        catch(IOException e){
                            System.out.println("Error Sending");
                        }
                    }
                }).start();
            }
        });
    }
}
