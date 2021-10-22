package eg.gov.iti.jets.trip_pal.trip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import eg.gov.iti.jets.trip_pal.R;

public class TripCreatorActivity extends AppCompatActivity {

    TripFillerFragment filler;
    String container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creator);
        filler = (TripFillerFragment) getSupportFragmentManager().findFragmentById(R.id.trip_filler_fragment);
        int storedTripID = getIntent().getIntExtra("TripObject", -1);
        Log.i("CHECKKKK", "onCreate: " + storedTripID);
        storeTripID(storedTripID);
        //container = filler.getTripObject();
        //Log.i(container, "onCreate: ");
        Intent returnIntent = new Intent();
        returnIntent.putExtra("trip", container);
        setResult(Activity.RESULT_OK, returnIntent);
        //finish();
    }

    public void storeTripID(int ID){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("STORED_TRIP_ID", ID).commit();
    }
}