package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserData {
    public int wins=0;
    public int loses=0;

    public  UserData(int wins,int loses){
        this.wins = wins;
        this.loses=loses;
    }
    //to set up setters getters;


}
