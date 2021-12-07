package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {

  private static final String TAG = "DashboardFragment";
  private NavController mNavController;
  private View view1;
  private FirebaseAuth mAuth;
  UserData currentUserData;
  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public DashboardFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setHasOptionsMenu(true); // Needed to display the action menu for this fragment


  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dashboard, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view1=view;
    mAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView = view1.findViewById(R.id.list);
    OpenGamesAdapter adapter = new OpenGamesAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    //to think for better logic
    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference myRef1 = database1.getReference("Live Games") ;

    myRef1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DataSnapshot> task) {
        if (!task.isSuccessful()) {
          Log.e("firebase", "Error getting data", task.getException());
        }
        else {
          Log.d("firebase", String.valueOf(task.getResult().getValue()));
          List<String> listOfGames = new ArrayList<String>();

          if(task.getResult().getValue()!=null) {
            Log.d(TAG, task.getResult().getValue().toString());

            HashMap<String, HashMap> map = (HashMap<String, HashMap>) task.getResult().getValue();

            for (String key : map.keySet()) {
              Log.d(TAG, map.get(key).get("startGame").toString());

              if (map.get(key).get("startGame").equals(false)) {
                listOfGames.add(key);
              }
            }
            adapter.setEntries(listOfGames);
          }

        }
      }
    });


    ValueEventListener postListener1 = new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        List<String> listOfGames = new ArrayList<String>();

        if(dataSnapshot.getValue() != null) {
          Log.d(TAG, dataSnapshot.getValue().toString());

          HashMap<String, HashMap> map = (HashMap<String, HashMap>) dataSnapshot.getValue();

          for (String key : map.keySet()) {
//            Log.d(TAG, map.get(key).get("startGame").toString());

            if (map.get(key).get("startGame").equals(false)) {
              listOfGames.add(key);
            }
          }
          adapter.setEntries(listOfGames);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
      }
    };
    myRef1.addValueEventListener(postListener1);

    // TODO if a user is not logged in, go to LoginFragment
    mNavController = Navigation.findNavController(view);
    mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if(currentUser == null){
      //go to login fragment
      reAuth(view);
    }else{
      TextView nameView = view.findViewById(R.id.greeting);



      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference myRef = database.getReference("User Data/" + currentUser.getUid());

      myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
          if (!task.isSuccessful()) {
            Log.e("firebase", "Error getting data", task.getException());
          }
          else {
            Log.d("firebase", String.valueOf(task.getResult().getValue()));
            //currentUserData = (UserData) task.getResult().getValue();

            Map<String,Integer> mapUser = (Map<String, Integer>) task.getResult().getValue();
            String wins = String.valueOf(mapUser.get("wins"));
            String losses = String.valueOf(mapUser.get("loses"));

            TextView score = view.findViewById(R.id.txt_score);
            String scoreVal = "Wins : "+ wins + "  Loss : " + losses;
            score.setText(scoreVal);
          }
        }
      });


      String welcomeMessage = "Welcome, " + currentUser.getDisplayName();
      nameView.setText(welcomeMessage);

      ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          Map<String,Integer> mapUser = (Map<String, Integer>) dataSnapshot.getValue();
          String wins = String.valueOf(mapUser.get("wins"));
          String losses = String.valueOf(mapUser.get("loses"));

          TextView score = view.findViewById(R.id.txt_score);
          String scoreVal = "Wins : "+ wins + "  Loss : " + losses;
          score.setText(scoreVal);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          // Getting Post failed, log a message
          Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
      };
      myRef.addValueEventListener(postListener);

    }





    // Show a dialog when the user clicks the "new game" button
    view.findViewById(R.id.fab_new_game).setOnClickListener(v -> {

      // A listener for the positive and negative buttons of the dialog
      DialogInterface.OnClickListener listener = (dialog, which) -> {
        String gameType = "No type";
        if (which == DialogInterface.BUTTON_POSITIVE) {
          gameType = getString(R.string.two_player);

          FirebaseDatabase database = FirebaseDatabase.getInstance();


          DatabaseReference myRef = database.getReference("Live Games");

          myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
              if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
              }
              else {


                DatabaseReference myRef1 = database.getReference("Live Games/"+ mAuth.getCurrentUser().getUid());

                multiplayerGame newGame = new multiplayerGame(mAuth.getCurrentUser().getUid());
                myRef1.setValue(newGame);

              }}});


        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
          gameType = getString(R.string.one_player);
        }
        Log.d(TAG, "New Game: " + gameType);

        // Passing the game type as a parameter to the action
        // extract it in GameFragment in a type safe way
        NavDirections action = DashboardFragmentDirections.actionGame(gameType,currentUser.getUid());
        mNavController.navigate(action);
      };

      // create the dialog
      AlertDialog dialog = new AlertDialog.Builder(requireActivity())
              .setTitle(R.string.new_game)
              .setMessage(R.string.new_game_dialog_message)
              .setPositiveButton(R.string.two_player, listener)
              .setNegativeButton(R.string.one_player, listener)
              .setNeutralButton(R.string.cancel, (d, which) -> d.dismiss())
              .create();
      dialog.show();
    });
  }

  private void reAuth(View view) {
    NavDirections action = DashboardFragmentDirections.actionNeedAuth();
    Navigation.findNavController(view).navigate(action);
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

      NavDirections action = DashboardFragmentDirections.actionNeedAuth();
      Navigation.findNavController(view1).navigate(action);


      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}