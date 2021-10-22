package eg.gov.iti.jets.trip_pal.ui.done_trips;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eg.gov.iti.jets.trip_pal.R;
import eg.gov.iti.jets.trip_pal.database.AppDatabase;
import eg.gov.iti.jets.trip_pal.database.TripEntity;
import eg.gov.iti.jets.trip_pal.trip.TripAdapter;
import eg.gov.iti.jets.trip_pal.ui.upcoming_trips.HomeViewModel;

public class GalleryFragment extends Fragment {


    private GalleryViewModel galleryViewModel;
    public RecyclerView recyclerView;
    public List tripList;
    TripAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        tripList = new ArrayList<TripEntity>();
        //TripAdapter mAdapter = new TripAdapter(getContext(), tripList);
        mAdapter = new TripAdapter(getContext(), tripList);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.getRecycledViewPool().clear();
        //mAdapter.notifyDataSetChanged();
        tripList = getDoneTrips();
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    private List<TripEntity> getDoneTrips() {
        class GetTrips extends AsyncTask<Void, Void, List<TripEntity>> {
            @Override
            protected List<TripEntity> doInBackground(Void... voids) {
                List<TripEntity> doneTripList = AppDatabase.getInstance(getContext()).tripDao().getDone();
                return doneTripList;
            }
            @Override
            protected void onPostExecute(List<TripEntity> tripList) {
                super.onPostExecute(tripList);
                //mAdapter = new TripAdapter(getContext(), tripList);
                //recyclerView.setAdapter(mAdapter);
                mAdapter.changeData(tripList);
            }
        }
        GetTrips tripGetter = new GetTrips();
        tripGetter.execute();
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.mInterface = new TripAdapter.TripDeleterInterface() {
            @Override
            public void deleteTrip(TripEntity tripEntity) {
                /*new AlertDialog.Builder(getContext())
                        .setTitle("Warning!")
                        .setMessage("Do you really wish to delete this trip?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){//onClick of yes deletes a trip
                                try{
                                    //tripList.remove(position);
                                    //notifyItemRemoved(position);
                                    //notifyItemRangeChanged(position, tripList.size());
                                    Toast.makeText(getContext(), tripEntity.getTripName(), Toast.LENGTH_SHORT).show();
                                    deleteTrip(tripEntity);
                                }catch (IndexOutOfBoundsException e) {
                                    Toast.makeText(getContext(), "Your Trip List is already Empty.", Toast.LENGTH_SHORT).show();
                                }}})
                        .setNegativeButton(android.R.string.no, null).show();
            */
                AppDatabase.getInstance(getContext()).tripDao().delete(tripEntity);}
        };
    }
}