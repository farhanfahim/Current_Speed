package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

    double currentSpeed,previousSpeed;
    double speedDifference;
    private TextView speedText;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedText = (TextView) findViewById(R.id.speed_text);
        speedText.setText("...");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                previousSpeed = currentSpeed;
                speedDifference = currentSpeed - previousSpeed;
                if (speedDifference > -30){
                    Toast.makeText(MainActivity.this, "crash", Toast.LENGTH_SHORT).show();
                }

                handler.postDelayed(this, 5000);
            }
        }, 0);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    // This is starting the GPS monitoring. Notice the second and third parameter.
    // Setting higher value might take less processing time. I want results here
    // as fast as possible.
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
    }

    // GPS will drain your battery. This is a good place to stop
    // monitoring it. You can also make it work some other way.
    @Override protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_speed, menu);
        return true;
    }

    // The following is the main callback. Process received GPS info as you wish.
    // We will just show the speed (in km/h, the received data is in m/s).
    @Override
    public void onLocationChanged(Location newLoc) {
        currentSpeed = newLoc.getSpeed();
        speedText.setText(String.format("%.0f", newLoc.getSpeed() * 3.6));
    }

    @Override
    public void onProviderDisabled(String arg0) {}

    @Override
    public void onProviderEnabled(String arg0) {}

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}