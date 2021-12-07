package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO if a user is logged in, go to Dashboard
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //go to fragment


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.findViewById(R.id.btn_log_in)
                .setOnClickListener(v -> {
                    // TODO implement sign in logic
                    String email = ((EditText)view.findViewById(R.id.edit_email)).getText().toString();
                    String pass = ((EditText)view.findViewById(R.id.edit_password)).getText().toString();



                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // Sign in is successful


                                NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                                Navigation.findNavController(view).navigate(action);

                            }else{
                                Toast.makeText(getContext(),"Email/Password Incorrect",Toast.LENGTH_SHORT).show();
                            }


                        }});

                    Log.d("TAAGE","Clicked");

                });

        view.findViewById(R.id.btn_newUser).setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(v).navigate(action);
        });

        return view;
    }

    // No options menu in login fragment.
}