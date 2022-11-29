package pt.ua.hackaton.smartmove.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import pt.ua.hackaton.smartmove.data.database.converters.DateConverter;

@Entity(tableName = "users")
@TypeConverters(DateConverter.class)
public class UserEntity {

    @PrimaryKey
    @ColumnInfo(name = "username")
    @NonNull
    public String username;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "weight")
    public double weight;

    @ColumnInfo(name = "height")
    public int height;

    @ColumnInfo(name = "imc")
    public double imc;

    @ColumnInfo(name = "potential")
    public double potential;

    public UserEntity(@NonNull String username, String firstName, String lastName, String email, String password, double weight, int height, double imc, double potential) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.imc = imc;
        this.potential = potential;
    }

}
