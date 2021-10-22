package eg.gov.iti.jets.trip_pal.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Insert;
import androidx.room.InvalidationTracker;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.google.auto.value.AutoValue;

import java.util.List;

@Database(entities = {TripEntity.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase{

    private static final String DB_NAME = "Trips_Database";
    private static AppDatabase tripDatabase = null;
    public static synchronized AppDatabase getInstance(Context context) {
        if (tripDatabase == null) {
            tripDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return tripDatabase;
    }
    public abstract TripDao tripDao();

    /*
    public abstract TripDao tripDao();
    AppDatabase db;
    TripDao tripDao = db.tripDao();
    List<TripEntity> tripList = tripDao.getAllTrips();
    */
}

