package com.example.chenhian.gpslocation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class PrecisionSettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.precision_settings);
        SharedPreferences prefs1 = getSharedPreferences("PrecisionSelection",0 );
        SharedPreferences prefs2 = getSharedPreferences("PrivacySelection",0 );
      //  SharedPreferences prefs = getSharedPreferences(0);


        Spinner precisionSettings = (Spinner) findViewById(R.id.spinner);
        precisionSettings.setSelection(prefs1.getInt("spinnerSelection", 0));

        Spinner privacySettings = (Spinner) findViewById(R.id.spinner2);
        privacySettings.setSelection(prefs2.getInt("spinnerSelection", 0));

    }

    protected void onPause() {
        super.onPause();
        Spinner precisionSettings = (Spinner) findViewById(R.id.spinner);
        SharedPreferences.Editor editor = getSharedPreferences("PrecisionSelection",0).edit();
        int selectedPositionPrecision = precisionSettings.getSelectedItemPosition();
        editor.putInt("spinnerSelection", selectedPositionPrecision);
        editor.commit();

        Spinner privacySettings = (Spinner) findViewById(R.id.spinner2);
        editor = getSharedPreferences("PrivacySelection",0).edit();
        int selectedPositionPrivacy = privacySettings.getSelectedItemPosition();
        editor.putInt("spinnerSelection", selectedPositionPrivacy);
        editor.commit();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}


