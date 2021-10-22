package eg.gov.iti.jets.trip_pal.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TripDao {

    @Query("SELECT * FROM trip_table") List<TripEntity> getAllTrips();

    @Query("SELECT * FROM trip_table WHERE trip_id = :ID") TripEntity findTripByID(int ID);

    //@Query("SELECT * FROM trip_table WHERE trip_id IN :tripIDs") List<TripEntity> loadTripsByIDs(int[] tripIDs);

    @Query("SELECT * FROM trip_table WHERE trip_status = 'Upcoming'") LiveData<List<TripEntity>> getUpcoming();

    @Query("SELECT * FROM trip_table WHERE trip_status = 'Done'") List<TripEntity> getDone();

    @Query("SELECT * FROM trip_table WHERE trip_status = 'Cancelled'") List<TripEntity> getCancelled();

    @Insert (onConflict = REPLACE) void insert(TripEntity tripEntity);

    @Update void update(TripEntity tripEntity);

    @Delete void delete(TripEntity tripEntity);
}