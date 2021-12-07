package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  /*@Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_logout) {
      Log.d(TAG, "logout clicked");
      // TODO handle log out
      FirebaseAuth.getInstance().signOut();

      NavDirections action = DashboardFragmentDirections.actionNeedAuth();
      Navigation.findNavController().navigate(action);


      return true;
    }
    return super.onOptionsItemSelected(item);
  }*/
}