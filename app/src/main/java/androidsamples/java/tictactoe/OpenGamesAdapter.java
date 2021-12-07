package androidsamples.java.tictactoe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.ViewHolder> {

  private final LayoutInflater mInflater;
  private List<String> mEntries;

  public OpenGamesAdapter(DashboardFragment dashboardFragment) {
    // FIXME if needed
    mInflater = LayoutInflater.from(dashboardFragment.getContext());
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    // TODO bind the item at the given position to the holder
    if (mEntries != null) {
      //multiplayerGame current = mEntries.get(position);
      String toSet = mEntries.get(position);
      holder.mContentView.setText(toSet);
    }
  }

  @Override
  public int getItemCount() {
    return (mEntries == null) ? 0 : mEntries.size();
  }

  public void setEntries(List<String> entries) {
    mEntries = entries;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    public final TextView mContentView;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      mContentView = view.findViewById(R.id.button00);

      mContentView.setOnClickListener(v -> {
        Log.d("TAG", "Game Pressed");

        String gameUid = mContentView.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference myRef1 = database.getReference("Live Games/" + gameUid + "/startGame") ;
        myRef1.setValue(true);






        NavDirections action = DashboardFragmentDirections.actionGame(String.valueOf(R.string.two_player),gameUid);
        Navigation.findNavController(view).navigate(action);

      });
    }

    @NonNull
    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
}