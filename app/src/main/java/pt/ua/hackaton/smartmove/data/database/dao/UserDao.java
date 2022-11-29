package pt.ua.hackaton.smartmove.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import pt.ua.hackaton.smartmove.data.database.entities.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT first_name FROM users WHERE username = :username;")
    LiveData<String> getUserFirstName(String username);

    @Query("SELECT last_name FROM users WHERE username = :username;")
    LiveData<String> getUserLastName(String username);

    @Query("SELECT email FROM users WHERE username = :username;")
    LiveData<String> getUserEmail(String username);

    @Query("SELECT height FROM users WHERE username = :username;")
    LiveData<Integer> getUserHeight(String username);

    @Query("SELECT weight FROM users WHERE username = :username;")
    LiveData<Double> getUserWeight(String username);

    @Query("SELECT potential FROM users WHERE username = :username;")
    LiveData<Double> getUserPotential(String username);

    @Query("SELECT imc FROM users WHERE username = :username;")
    LiveData<Double> getUserIMC(String username);

    @Query("UPDATE users SET potential = :newPotential WHERE username = :username;")
    void setUserPotential(String username, double newPotential);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

}
