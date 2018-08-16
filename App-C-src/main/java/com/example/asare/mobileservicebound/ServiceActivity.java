package com.example.asare.mobileservicebound;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ServiceActivity extends AppCompatActivity {

    Messenger messenger = null;
    Messenger receiveMessage = null;
    boolean isBound = false;
    final String TAG = "IncomingHandler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bindService(View view){
        //binding
        if (!isBound) {
            Intent myIntent = new Intent(this, MessengerService.class);
            bindService(myIntent, Connection, Context.BIND_AUTO_CREATE);
        }else{
            Toast.makeText(this, "Service is already bound...", Toast.LENGTH_SHORT).show();
        }

    }

    public void unBindService(View view){
        //unbinding from LocalService
        if(isBound) {
            Toast.makeText(this, "Unbinding...", Toast.LENGTH_SHORT).show();
            unbindService(Connection);
            isBound = false;
        }else{
            Toast.makeText(this, "Service is not bound...", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            messenger = new Messenger(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
            messenger = null;
            isBound = false;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.d(TAG, "onBindingDied");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(Connection);
        isBound = false;
    }
}
