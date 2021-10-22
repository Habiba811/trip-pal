package eg.gov.iti.jets.trip_pal.trip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eg.gov.iti.jets.trip_pal.R;
import eg.gov.iti.jets.trip_pal.database.TripEntity;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>{

    private static final String TAG = "RecyclerView";
    private Context context;
    private List<TripEntity> tripList;

    public TripDeleterInterface mInterface = null;//***


    public TripAdapter(Context context, List<TripEntity> tripList){
        super();
        this.context = context;
        this.tripList = tripList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//may need to implement View.OnClickListener

        public ConstraintLayout constraintLayout;   //Constraint Layout
        public CardView cardView;                   //CardView container

        //TextViews
        public TextView nameLabel;                  //ex: Trip No. 01
        public TextView dateLabel;                  //ex: 15/03/2021
        public TextView timeLabel;                  //ex: 07:30 AM
        public TextView typeLabel;                  //ex: one way or round trip
        public TextView statusLabel;                //ex: upcoming, done or cancelled
        public TextView startLabel;                 //ex: Home
        public TextView endLabel;                   //ex: Work
        public TextView notesLabel;

        //ImageViews
        public ImageView edit;
        public ImageView delete;
        public ImageView showNotes;

        public ImageView tripStatusImg;
        public ImageView tripTypeImg;
        public ImageView tripDateImg;
        public ImageView tripTimeImg;
        public ImageView tripFromImg;
        public ImageView tripToImg;

        Button startTripButton;

        public ViewHolder(@NonNull View v) {
            super(v);

            constraintLayout = v.findViewById(R.id.constraint_layout_id);
            cardView = v.findViewById(R.id.cardview_id);

            edit = v.findViewById(R.id.action_edit_id);
            delete = v.findViewById(R.id.action_delete_id);
            showNotes = v.findViewById(R.id.action_show_notes_id);

            nameLabel = v.findViewById(R.id.trip_name_label_id);
            dateLabel = v.findViewById(R.id.trip_date_label_id);
            timeLabel = v.findViewById(R.id.trip_time_label_id);
            startLabel = v.findViewById(R.id.trip_from_label_id);
            endLabel = v.findViewById(R.id.trip_to_label_id);
            //typeLabel = v.findViewById(R.id.trip_type_label_id);
            //notesLabel = v.findViewById(R.id.trip_notes_label_id);

            tripStatusImg = v.findViewById(R.id.trip_status_imageView_id);
            tripTypeImg = v.findViewById(R.id.trip_type_imageView_id);
            tripDateImg = v.findViewById(R.id.trip_date_imageView_id);
            tripTimeImg = v.findViewById(R.id.trip_time_imageView_id);
            tripFromImg = v.findViewById(R.id.trip_from_imageView_id);
            tripToImg = v.findViewById(R.id.trip_to_imageView_id);

            startTripButton = v.findViewById(R.id.start_button_id);
        }
    }


    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.trip_row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.setIsRecyclable(true);
        Log.i(TAG, "==  === onCreateViewHolder: =====");
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        TripEntity tripEntity = tripList.get(position);

        viewHolder.edit.setImageResource(R.drawable.i_edit);
        viewHolder.delete.setImageResource(R.drawable.i_delete);
        viewHolder.showNotes.setImageResource(R.drawable.i_notes);

        //viewHolder.tripStatusImg.setImageResource(R.drawable.i_done);
        viewHolder.tripTypeImg.setImageResource(R.drawable.i_one_way);
        viewHolder.tripDateImg.setImageResource(R.drawable.i_date);
        viewHolder.tripTimeImg.setImageResource(R.drawable.i_time);
        viewHolder.tripFromImg.setImageResource(R.drawable.i_car);
        viewHolder.tripToImg.setImageResource(R.drawable.i_location);

        viewHolder.nameLabel.setText(tripEntity.getTripName());
        viewHolder.dateLabel.setText(tripEntity.getTripDate());
        viewHolder.timeLabel.setText(tripEntity.getTripTime());
        viewHolder.startLabel.setText(tripEntity.getTripStart());
        viewHolder.endLabel.setText(tripEntity.getTripEnd());
        //viewHolder.typeLabel.setText(tripEntity.getTripType());
        //viewHolder.statusLabel.setText(tripEntity.getTripStatus());
        //viewHolder.notesLabel.setText(tripEntity.getTripNotes());




        viewHolder.startTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripEntity tripEntity = tripList.get(position);
                tripEntity.setTripType("Done");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ viewHolder.endLabel.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);//add map intent when the user start a trip
            }
        });



        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripEntity tripEntity = tripList.get(position);
                Intent intent = new Intent(context, TripCreatorActivity.class).putExtra("TripObject", tripEntity.getID());
                Log.i("CHECK", "onClick: " + tripEntity.getID());
                context.startActivity(intent);
            }
        });


        if(mInterface != null){//***
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//onClick of delete generates a dialog
                    confirmDelete(position);
                }
            });
        }


        viewHolder.showNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Trip Notes")
                        .setCancelable(false)
                        .setMessage((CharSequence) tripEntity.getTripNotes())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.cancel();
                                }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    public int getItemCount(){
        return tripList.size();
    }

    public void confirmDelete(int position){
        new AlertDialog.Builder(context)
                .setTitle("Warning!")
                .setMessage("Do you really wish to delete this trip?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        mInterface.deleteTrip(tripList.get(position));
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void changeData(List<TripEntity> data){
        tripList = data;
        notifyDataSetChanged();
    }

    public void removeData(){}

    public interface TripDeleterInterface{//***
        void deleteTrip(TripEntity tripEntity);
    }
}









