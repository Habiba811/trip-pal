package eg.gov.iti.jets.trip_pal.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eg.gov.iti.jets.trip_pal.MainActivity;
import eg.gov.iti.jets.trip_pal.R;
import eg.gov.iti.jets.trip_pal.database.AppDatabase;
import eg.gov.iti.jets.trip_pal.database.TripEntity;

import static android.app.Activity.RESULT_OK;


public class TripFillerFragment extends Fragment{

    Calendar mCalendar = Calendar.getInstance();
    TripEntity tripEntity = new TripEntity();

     private PlacesClient placesClient;
     EditText tripNameEditText;
     EditText fromEditText;
     EditText toEditText;
     EditText tripDatePicker;
     EditText tripTimePicker;
     EditText notesAdder;
     RadioGroup tripTypeRadioGroup;
     RadioButton oneWayTrip;
     RadioButton roundTrip;
     Button saveTripDataButton;
     Button cancelSavingTripDataButton;


    public TripFillerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_filler,container,false);
        tripNameEditText = view.findViewById(R.id.trip_name_edit_text_id);
        fromEditText = view.findViewById(R.id.from_edit_text_id);
        toEditText = view.findViewById(R.id.to_edit_text_id);
        notesAdder = view.findViewById(R.id.add_notes_edit_text_id);
        tripDatePicker = view.findViewById(R.id.trip_date_date_picker_id);
        tripTimePicker = view.findViewById(R.id.trip_time_time_picker_id);
        tripTypeRadioGroup = view.findViewById(R.id.trip_type_radio_group_button_id);
        oneWayTrip = view.findViewById(R.id.one_way_trip_radio_button_id);
        roundTrip = view.findViewById(R.id.round_trip_radio_button_id);
        saveTripDataButton = view.findViewById(R.id.save_trip_data_button_id);
        cancelSavingTripDataButton = view.findViewById(R.id.cancel_saving_trip_data_button_id);

        //final TripEntity tripEntity = (TripEntity) getIntent().getSerializableExtra("trip");
        //loadTrip(tripEntity);






        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        tripDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        tripTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tripTimePicker.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        saveTripDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            saveTrip();
            Log.i("Saved", "onClick: ");
            }
        });

        cancelSavingTripDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }






    public void saveTrip() {

        int isSelected = tripTypeRadioGroup.getCheckedRadioButtonId();

        if (tripNameEditText.getText().toString() == null) {
            tripNameEditText.setError("Required");
            tripNameEditText.requestFocus();
            return;
        }

        if (fromEditText.getText().toString() == null) {
            fromEditText.setError("Required");
            fromEditText.requestFocus();
            return;
        }

        if (toEditText.getText().toString() == null) {
            toEditText.setError("Required");
            toEditText.requestFocus();
            return;
        }

        if (tripDatePicker.getText().toString() == null) {
            tripDatePicker.setError("Required");
            tripDatePicker.requestFocus();
            return;
        }

        if (tripTimePicker.getText().toString() == null) {
            tripTimePicker.setError("Required");
            tripTimePicker.requestFocus();
            return;
        }

        if (isSelected == -1) {
            Toast.makeText(getContext(), "Make sure to set your trip type.", Toast.LENGTH_SHORT).show();
            return;
        }


    class SaveTrip extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a trip
                //TripEntity tripEntity = new TripEntity();
                tripEntity.setTripName(tripNameEditText.getText().toString());
                tripEntity.setTripDate(tripDatePicker.getText().toString());
                tripEntity.setTripTime(tripTimePicker.getText().toString());
                tripEntity.setTripType(getTripType());
                tripEntity.setTripStatus("Upcoming");
                tripEntity.setTripStart(fromEditText.getText().toString());
                tripEntity.setTripEnd(toEditText.getText().toString());
                //tripEntity.setTripNotes(notesAdder.getText().append());
                AppDatabase.getInstance(getContext()).tripDao().insert(tripEntity);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getActivity().finish();
                startActivity(new Intent(getContext(), MainActivity.class));
                Toast.makeText(getContext(), "Your Trip is Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveTrip saveTrip = new SaveTrip();
        saveTrip.execute();
    }



    public void deleteTrip(TripEntity tripEntity) {

        class DeleteTrip extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase.getInstance(getContext()).tripDao().delete(tripEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        }

        DeleteTrip deleteTrip = new DeleteTrip();
        deleteTrip.execute();
    }



    public void updateTrip(TripEntity tripEntity) {

        //setting the blank fields to the data of the to_be_updated trip
        String fetchedTripName = tripEntity.getTripName();
        String fetchedTripDate = tripEntity.getTripDate();
        String fetchedTripTime = tripEntity.getTripTime();
        String fetchedTripType = tripEntity.getTripType();
        String fetchedTripStart = tripEntity.getTripStart();
        String fetchedTripEnd = tripEntity.getTripEnd();
        //String fetchedTripNotes = tripEntity.getTripNotes();

        tripNameEditText.setText(tripEntity.getTripName());
        tripDatePicker.setText(tripEntity.getTripName());
        tripTimePicker.setText(tripEntity.getTripName());
        fromEditText.setText(tripEntity.getTripStart());
        toEditText.setText(tripEntity.getTripEnd());
        if (tripEntity.getTripType() == "One Way Trip"){
            oneWayTrip.setChecked(true);
        }else{
            roundTrip.setChecked(true);
        }
        //notesAdder.setText(tripEntity.getTripNotes());
        //ends here


        class UpdateTrip extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                tripEntity.setTripName(tripNameEditText.getText().toString());
                tripEntity.setTripDate(tripDatePicker.getText().toString());
                tripEntity.setTripTime(tripTimePicker.getText().toString());
                tripEntity.setTripStart(fromEditText.getText().toString());
                tripEntity.setTripEnd(toEditText.getText().toString());
                tripEntity.setTripType(getTripType());
                tripEntity.setTripStatus("Upcoming");
                //tripEntity.setTripNotes(notesAdder.getText().append(" , "));
                AppDatabase.getInstance(getContext()).tripDao().update(tripEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getActivity().finish();
                startActivity(new Intent(getContext(), MainActivity.class));
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
            }
        }
        UpdateTrip updateTrip = new UpdateTrip();
        updateTrip.execute();
    }



    /*private void loadTrip(TripEntity tripEntity) {
    tripNameEditText.setText(tripEntity.getTripName());
    fromEditText.setText(tripEntity.getTripStart());
    toEditText.setText(tripEntity.getTripEnd());
    tripDatePicker.setText(tripEntity.getTripDate());
    tripTimePicker.setText(tripEntity.getTripDate());
    if(getTripTypeString() == "One Way"){
        oneWayTrip.setChecked(true);
    }else{
        roundTrip.setChecked(true);
    }}*/


    private String getNotes() {
        return notesAdder.getText().toString();
    }

    private void updateLabel() {
        String mFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(mFormat, Locale.US);
        tripDatePicker.setText(sdf.format(mCalendar.getTime()));
    }

    public String getTripType() {
        String tripType = null;
        if(oneWayTrip.isChecked()){
            tripType = "One Way Trip";
        }else if(roundTrip.isChecked()){
            tripType = "Round Trip";
        }
        return tripType;
    }

    /*public String getTripObject() {return tripNameString;}*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fromEditText.setFocusable(false);
        toEditText.setFocusable(false);

        Places.initialize(getActivity().getApplicationContext(), "AIzaSyBaZ4c9OHwgMEU-gMQxXeqKItLQN3VqG6U");
        placesClient = Places.createClient(getActivity());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        fromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
            }
        });

        toEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            fromEditText.setText(place.getAddress());
            toEditText.setText(place.getAddress());
        }

    }
}