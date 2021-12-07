package androidsamples.java.tictactoe;

import java.util.UUID;

public class multiplayerGame {
    public String user1;
    public boolean startGame;
    public String user2;
    public  boolean player1Move;

    public String b1;
    public String b2;
    public String b3;
    public String b4;
    public String b5;
    public String b6;
    public String b7;
    public String b8;
    public String b0;



    public multiplayerGame( String displayName) {
        user1 = displayName;
        startGame = false;
        b1="";
        b2="";
        b3="";
        b4="";
        b5="";
        b6="";
        b7="";
        b8="";
        b0="";
        user2 = "";
        player1Move = true;

    }

    public void secondPlayerJoin(String userUID){
        user2 = userUID;
        startGame = true;
    }
}
