package com.example.chenhian.gpslocation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {
    Button btnShowLocation;
    GPSTracker gps;
    double latitude;
    double longitude;
    SharedPreferences precisionPrefs;
    PopupWindow popupWindow;

    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv =(TextView) findViewById(R.id.locationText);
        btnShowLocation = (Button) findViewById(R.id.show_Location);

        precisionPrefs = getSharedPreferences("PrecisionSelection",0 );
        Toast.makeText(getApplicationContext(),"For debugging : Current selection" + precisionPrefs.getInt("spinnerSelection", -1) , Toast.LENGTH_LONG).show();


        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);
                //Opens popup with current location and button to open google maps
                if (gps.canGetLocation()) {

                    if (precisionPrefs.getInt("spinnerSelection", -1) == 2) {

                        LayoutInflater layoutInflater
                                = (LayoutInflater) getBaseContext()
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.popup, null);
                        TextView tv = (TextView) popupView.findViewById(R.id.locationText);
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        tv.setText("For debugging purposes, Message to be removed : Latitude = " + latitude + " Longitude = " + longitude  + "\n\nUser is at " + ConvertPointToLocation());
                        popupWindow = new PopupWindow(
                                popupView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                        btnDismiss.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                popupWindow.dismiss();
                            }
                        });
                        Button launchmapbtn = (Button) popupView.findViewById(R.id.launchmap); // Button to launch google maps
                        launchmapbtn.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q=" +  ConvertPointToLocation())); // Prepare intent
                                    startActivity(geoIntent); // Initiate lookup
                                } catch (Exception e) {
                                }
                            }
                        });

                        popupWindow.showAsDropDown(btnShowLocation, -220, -1200);


                    }

                    if (precisionPrefs.getInt("spinnerSelection", -1) == 1) {
                        LayoutInflater layoutInflater
                                = (LayoutInflater) getBaseContext()
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = layoutInflater.inflate(R.layout.popup, null);
                        TextView tv = (TextView) popupView.findViewById(R.id.locationText);
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        tv.setText("For debugging purposes, Message to be removed in final version : Latitude = " + latitude + " Longitude = " + longitude + "\n\nPlaceholder : User is in Mountbatten Area");
                        popupWindow = new PopupWindow(
                                popupView,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);



                        Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                        btnDismiss.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                popupWindow.dismiss();
                            }
                        });
                        Button launchmapbtn = (Button) popupView.findViewById(R.id.launchmap); // Button to launch google maps
                        launchmapbtn.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q= Mountbatten MRT")); // Prepare intent
                                    startActivity(geoIntent); // Initiate lookup
                                } catch (Exception e) {
                                }
                            }
                        });

                        popupWindow.showAsDropDown(btnShowLocation, -220, -1200);


                    }

                    if (precisionPrefs.getInt("spinnerSelection", -1) == 0) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        Toast.makeText(getApplicationContext(), "User is in the " + closestToSide(latitude,longitude) + " Side of Singapore", Toast.LENGTH_LONG).show();

                    }
                }
                //Toast message telling user to on gps if off.
                else {
                    Toast.makeText(getApplicationContext(), "GPS is currently disabled, please enable GPS and try again.", Toast.LENGTH_LONG).show();
                }


            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.about:{
                aboutMenuItem();
                break;
            }
            case R.id.action_settings:{
                settingsMenuItem();
                break;
            }


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }




    private void precisionSettingsMenuItem() {

    }
    private void settingsMenuItem() {
        Intent PrecisionIntent = new Intent(this, PrecisionSettingsActivity.class);
        startActivity(PrecisionIntent);

    }






    private void aboutMenuItem() {
        new AlertDialog.Builder(this).setTitle("About").setMessage("App UI v1.5").setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Auto generated method stub
            }
        }).show();}






    public String ConvertPointToLocation() {
        String address = "";
        Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude , 1);

            if (addresses.size() > 0) {
                for (int index = 0;
                     index < addresses.get(0).getMaxAddressLineIndex(); index++)
                    address += addresses.get(0).getAddressLine(index) + " ";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }



    public String closestToSide(double latitude, double longitude) {
        //double array goes East, NorthEast, North, West, Central;
        double[] coordinates = {1.352517,103.944818,1.431511,103.834953,1.436987,103.788064,1.341056,103.709272,1.298839,103.844713};
        //eastLat = 1.352517; //Using Tampines as marker
        //eastLon = 103.944818;
        //northEastLat = 1.431511; //Using Yishun as marker
        //northEastLon = 103.834953;
        //northLat = 1.436987; //Using Woodlands as marker
        //northLon = 103.788064;
        //westlat = 1.341056; //Using Jurong West as marker
        //westLon = 103.709272;
        //centralLat = 1.298839;//Using Orchard as marker
        //centralLon = 103.844713;

        String[] locations  = {"East","NorthEast","North","West","Central"};
        double[] distance = new double[5];
        for(int i=0;i<=4;i++) {
            distance[i] = ((latitude-coordinates[2*i]) * (latitude-coordinates[2*i])) + ((longitude-coordinates[2*i+1]) * (longitude-coordinates[2*i+1]));
        }
        double small = distance[0];
        int closest = 0;
        for(int j=1;j<=4;j++) {
            if(distance[j]<small) {
                closest = j;
                small = distance[j];
            }
        }
        return locations[closest];
    }


}



