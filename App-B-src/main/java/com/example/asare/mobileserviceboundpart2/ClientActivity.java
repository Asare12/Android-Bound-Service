package com.example.asare.mobileserviceboundpart2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends AppCompatActivity {

    final String TAG = "ClientMAIN";

    static final int JOB1 = 1;
    static final int JOB2 = 2;
    static final int JOB1_RESPONSE = 3;
    static final int JOB2_RESPONSE = 4;

    Messenger messenger = null;
    Messenger receiveMessage = null;
    boolean isBound = false;

    private Intent serviceIntent;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.example.asare.mobileservicebound",
                "com.example.asare.mobileservicebound.MessengerService"));

        textView = (TextView) findViewById(R.id.textView);
        Log.d(TAG,"onCreate Finished");
    }

     ServiceConnection Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            messenger = new Messenger(iBinder);
            receiveMessage = new Messenger(new ResponseHandler());
            isBound = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
            receiveMessage = null;
            isBound = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public void bindService(View view){
        //binding
        Log.d(TAG, "bindService");
        if (!isBound) {
            boolean b = bindService(serviceIntent, Connection, Context.BIND_AUTO_CREATE);
            if(b){
                Log.d(TAG, "bindService TRUE");
            }else{
                Log.d(TAG, "bindService FALSE");
            }

        }

    }

    public void unBindService(View view){
        //unbinding from LocalService
        Toast.makeText(this, "Unbinding...", Toast.LENGTH_SHORT).show();
        unbindService(Connection);
        isBound = false;

    }

    public void getMessage(View view) {

        if (isBound) {
            String button = (String) ((Button) view).getText();
            if (button.equals("GET FIRST MESSAGE")) {
                Log.d(TAG,"First Message 1");
                Message msg = Message.obtain(null, JOB1);
                Log.d(TAG,"First Message 2");
                msg.replyTo = new Messenger(new ResponseHandler());
                Log.d(TAG,"First Message 3");
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    Log.d(TAG,"First Message Exception : "+e.getMessage());
                    e.printStackTrace();
                }
                Log.d(TAG,"First Message END");
            } else if (button.equals("GET SECOND MESSAGE")) {
                Message msg = Message.obtain(null, JOB2);
                msg.replyTo = new Messenger(new ResponseHandler());
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(getApplicationContext(), "Service unbound, cant get message", Toast.LENGTH_SHORT).show();
        }
    }

    class ResponseHandler extends Handler{
        String message;
        public void handleMessage(Message msg){
            Log.d(TAG,"ResponseHandler Start");

            Log.d(TAG,"ResponseHandler Message" + msg.toString());

            switch (msg.what){
                case JOB1_RESPONSE:

                    Log.d(TAG,"ResponseHandler 1");
                message = msg.getData().getString("response_message");

                    Log.d(TAG,"ResponseHandler 2");
                textView.setText(message);

                    Log.d(TAG,"ResponseHandler 3");
                break;

                case JOB2_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                    break;
            }

            Log.d(TAG,"ResponseHandler END");
            super.handleMessage(msg);
        }

    }
}
