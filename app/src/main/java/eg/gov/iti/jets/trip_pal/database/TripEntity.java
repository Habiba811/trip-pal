package eg.gov.iti.jets.trip_pal.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//(indices = {@Index(value = {"trip_id"}, unique = true)})
//(languageId = "lid") .. is for multi-lingual support and can be removed

//@Fts4
//@AutoValue
@Entity(tableName = "trip_table")
public class TripEntity implements Serializable {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "trip_id")
        private int ID;

        @ColumnInfo(name = "trip_name")
        private String tripName;
        @ColumnInfo(name = "trip_date")
        private String tripDate;
        @ColumnInfo(name = "trip_time")
        private String tripTime;
        @ColumnInfo(name = "trip_type")
        private String tripType;
        @ColumnInfo(name = "trip_status")
        private String tripStatus;
        @ColumnInfo(name = "trip_start")
        private String tripStart;
        @ColumnInfo(name = "trip_end")
        private String tripEnd;
        @ColumnInfo(name = "trip_notes")
        @TypeConverters(Converters.class)
        private ArrayList<String> tripNotes;


        //The following attributes mat or may not be incorporated (BONUS)
        //private double duration;
        //private double averageSpeed;

        //write constructors as needed and required
        public TripEntity(){} //default constructor

        public TripEntity(    //parameterized constructor
                        String name,
                        String date,
                        String time,
                        String type,
                        String status,
                        String start,
                        String end,
                        ArrayList<String> notes
        ){
                tripName = name;
                tripDate = date;
                tripTime = time;
                tripType = type;
                tripStatus = status;
                tripStart = start;
                tripEnd = end;
                tripNotes = notes;
        }


        //Setters & Getters...
        public void setID(int ID) {this.ID = ID;}
        public void setTripName(String tripName) {this.tripName = tripName;}
        public void setTripDate(String tripDate) {this.tripDate = tripDate;}
        public void setTripTime(String tripTime) {this.tripTime = tripTime;}
        public void setTripType(String tripType) {this.tripType = tripType;}
        public void setTripStatus(String tripStatus) {this.tripStatus = tripStatus;}
        public void setTripStart(String tripStart) {this.tripStart = tripStart;}
        public void setTripEnd(String tripEnd) {this.tripEnd = tripEnd;}
        public void setTripNotes(ArrayList<String> tripNotes) {this.tripNotes = tripNotes;}


        public int getID() {return ID;}
        public String getTripName() {return tripName;}
        public String getTripDate() {return tripDate;}
        public String getTripTime() {return tripTime;}
        public String getTripType() {return tripType;}
        public String getTripStatus() {return tripStatus;}
        public String getTripStart() {return tripStart;}
        public String getTripEnd() {return tripEnd;}
        public ArrayList<String> getTripNotes() {return tripNotes;}

        /*
        //Room uses this factory method to create Trip objects.
        public TripEntity create(
                int ID,
                String tripName,
                String tripDate,
                String tripTime,
                String tripType,
                String tripStatus,
                String tripStart,
                String tripEnd,
                String tripNotes) {
            return new AutoValue_TripEntity(ID, tripName, tripDate, tripTime, tripType, tripStatus, tripStart, tripEnd, tripNotes);
        }
        */
    }