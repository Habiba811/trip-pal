package eg.gov.iti.jets.trip_pal.ui.upcoming_trips;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import eg.gov.iti.jets.trip_pal.database.AppDatabase;
import eg.gov.iti.jets.trip_pal.database.TripEntity;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }


    public LiveData<List<TripEntity>> getTrips(Context context){
        return AppDatabase.getInstance(context).tripDao().getUpcoming();
    }


    public LiveData<String> getText() {
        return mText;
    }
}