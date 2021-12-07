package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class GameFragment extends Fragment {
  private static final String TAG = "GameFragment";
  private static final int GRID_SIZE = 9;
  boolean win=false;
  public boolean movePlayer1 = true;
  private  static String gameName;
  private final Button[] mButtons = new Button[GRID_SIZE];
  private NavController mNavController;
  GameFragmentArgs args;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    // Extract the argument passed with the action in a type-safe way
    args = GameFragmentArgs.fromBundle(getArguments());
    Log.d(TAG, "New game type = " + args.getGameType());

    gameName = args.getGameName();

    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");

        // TODO show dialog only when the game is still in progress
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.confirm)
                .setMessage(R.string.forfeit_game_dialog_message)
                .setPositiveButton(R.string.yes, (d, which) -> {
                  // TODO update loss count
                  updateLossCount();
                  mNavController.popBackStack();
                })
                .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                .create();
        dialog.show();
      }
    };
    requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
  }

  private void updateLossCount() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User Data/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/loses");

    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
          Log.e("firebase", "Error getting data", task.getException());
        }
        else {



          Long loses = (Long) task.getResult().getValue();

          Log.d("Tag",loses.toString());

          myRef.setValue(loses+1);

        }
      }
    });
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_game, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mNavController = Navigation.findNavController(view);

    mButtons[0] = view.findViewById(R.id.button0);
    mButtons[1] = view.findViewById(R.id.button1);
    mButtons[2] = view.findViewById(R.id.button2);

    mButtons[3] = view.findViewById(R.id.button3);
    mButtons[4] = view.findViewById(R.id.button4);
    mButtons[5] = view.findViewById(R.id.button5);

    mButtons[6] = view.findViewById(R.id.button6);
    mButtons[7] = view.findViewById(R.id.button7);
    mButtons[8] = view.findViewById(R.id.button8);

    for (int i = 0; i < mButtons.length; i++) {
      int finalI = i;
      mButtons[i].setOnClickListener(v -> {
        Log.d(TAG, "Button " + finalI + " clicked");
        // TODO implement listeners

        if(args.getGameType().equals( getString(R.string.one_player))){
          //single player
          if(mButtons[finalI].getText() != "X" && mButtons[finalI].getText() != "O") {
            //play the move
            mButtons[finalI].setText("X");
            Log.d("touched","YOU played");
            if(checkWin()){
              AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                      .setTitle(R.string.won)
                      .setMessage(R.string.congratulations)
                      .setPositiveButton(R.string.ok, (d, which) -> {
                        // TODO update win count
                        updateWinCount();
                        mNavController.popBackStack();
                      })
                      .create();
              dialog.show();

            }else{
              int temp=0;
              for(int j =0;j<9;j++){
                if(mButtons[j].getText().toString().equals("X")|| mButtons[j].getText().toString().equals("O")){
                  temp++;
                }
              }

              if(temp==9 && (!checkWin())){

                Log.d("DRAW","YOU DREW");

                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.draw)
                        .setMessage(R.string.you_drew)
                        .setPositiveButton(R.string.ok, (d, which) -> {
                          // TODO update no count
                          mNavController.popBackStack();
                        })
                        .create();
                dialog.show();
              }else {


                //play random move;
                Random rand = new Random();
                int randomNumber = rand.nextInt(9);

                while (mButtons[randomNumber].getText().toString().equals("X") || mButtons[randomNumber].getText().toString().equals("O")) {
                  randomNumber = rand.nextInt(9);
                }

                mButtons[randomNumber].setText("O");

                if (checkWin()) {
                  AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                          .setTitle(R.string.loose)
                          .setMessage(R.string.you_lost)
                          .setPositiveButton(R.string.ok, (d, which) -> {
                            // TODO update loose count
                            updateLossCount();
                            mNavController.popBackStack();



                          })
                          .create();
                  dialog.show();
                }
              }


            }
          }



        }else{
          //double player
          FirebaseAuth mAuth = FirebaseAuth.getInstance();
          boolean player1;
          //check
          if(mAuth.getCurrentUser().getUid().toString().equals(gameName)){
            player1 = true;
          }else{
            player1 = false;
          }


          //check player move
          FirebaseDatabase database = FirebaseDatabase.getInstance();
          DatabaseReference myRef1 = database.getReference("Live Games/" + gameName + "/player1Move" ) ;
          DatabaseReference myRef = database.getReference("Live Games/" + gameName  ) ;
          // DatabaseReference myRef1 = database.getReference("Live Games/" + gameName  ) ;

          //implement somewhere on top so that this is called only once
          myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
              if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
              }
              else {
                movePlayer1 = (boolean) ((HashMap)task.getResult().getValue()).get("player1Move");

                for(int i=0;i<9;i++){
                  mButtons[i].setText(((HashMap)task.getResult().getValue()).get("b"+i).toString());
                }
              }
            }
          });

          ValueEventListener postListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Log.d(TAG, "onDataChange: dataSnapshot.getValue()");
              movePlayer1 = (boolean) ((HashMap)dataSnapshot.getValue()).get("player1Move");

              for(int i=0;i<9;i++){
                mButtons[i].setText(((HashMap)dataSnapshot.getValue()).get("b"+i).toString());
              }


              if(checkWin() && !win){
                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.loose)
                        .setMessage(R.string.you_lost)
                        .setPositiveButton(R.string.ok, (d, which) -> {
                          // TODO update loose count
                          updateLossCount();

                          mNavController.popBackStack();

                        })
                        .create();
                dialog.show();
              }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              // Getting Post failed, log a message
              Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
          };
          myRef.addValueEventListener(postListener1);

          String marking;

          if(player1){
            marking = "X";
          }else {
            marking = "O";
          }

          if(player1 == movePlayer1){

            if(mButtons[finalI].getText().toString().equals("")) {
              //play the move
              mButtons[finalI].setText(marking);
              Log.d("touched", "YOU played");


              DatabaseReference temp = database.getReference("Live Games/" + gameName + "/b" + finalI) ;
              temp.setValue(marking);






              if (checkWin()) {
                win = true;
                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.won)
                        .setMessage(R.string.congratulations)
                        .setPositiveButton(R.string.ok, (d, which) -> {
                          // TODO update win count
                          updateWinCount();
                          mNavController.popBackStack();
                        })
                        .create();
                dialog.show();

              }


              movePlayer1 = !movePlayer1;
              myRef1.setValue(movePlayer1);
            }


          }
        }
      });
    }
  }

  private void updateWinCount(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User Data/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/wins");

    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
          Log.e("firebase", "Error getting data", task.getException());
        }
        else {



          Long wins = (Long) task.getResult().getValue();

          Log.d("Tag",wins.toString());

          myRef.setValue(wins+1);

        }
      }
    });

  }

  private boolean checkWin() {
    for(int i=0;i<9;i=i+3){
      if(mButtons[i].getText().toString().equals(mButtons[i + 1].getText().toString()) && mButtons[i + 2].getText().toString().equals(mButtons[i + 1].getText().toString()) && !mButtons[i + 2].getText().toString().equals("")){
        Log.d("Test", mButtons[i+2].getText().toString());
        return true;
      }
    }

    for(int i=0;i<3;i++){
      if(mButtons[i].getText().toString().equals(mButtons[i + 3].getText().toString())&& !mButtons[i].getText().toString().equals("") && mButtons[i + 6].getText().toString().equals(mButtons[i + 3].getText().toString()) ){
        return true;
      }
    }

    if(mButtons[0].getText().toString().equals(mButtons[4].getText().toString())&& (!mButtons[0].getText().toString().equals("")) && mButtons[0].getText().toString().equals(mButtons[8].getText().toString())){
      return true;
    }

    if(mButtons[2].getText().toString().equals(mButtons[4].getText().toString())&& !mButtons[2].getText().toString().equals("") && mButtons[2].getText().toString().equals(mButtons[6].getText().toString())){
      return true;
    }


    return false;
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity

  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_logout) {
      Log.d(TAG, "logout clicked");
      // TODO handle log out
      FirebaseAuth.getInstance().signOut();

      //handle game loss
      updateLossCount();
      NavDirections action = GameFragmentDirections.actionGameFragmentToLoginFragment();
      Navigation.findNavController(getView()).navigate(action);


      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // handle app close
}
