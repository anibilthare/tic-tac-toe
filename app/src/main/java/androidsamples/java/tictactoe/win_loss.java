package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class win_loss {
    public int wins;
    public int loss;
    public String name;

    public win_loss(String n, int w, int l)
    {
        name = n;
        wins = w;
        loss = l;
    }
}
