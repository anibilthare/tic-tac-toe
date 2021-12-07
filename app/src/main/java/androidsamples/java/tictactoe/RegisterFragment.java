package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class RegisterFragment  extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        view.findViewById(R.id.existing_user_login).setOnClickListener(v -> {
            NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
            Navigation.findNavController(view).navigate(action);
        });

        EditText emailText = view.findViewById(R.id.edit_email_register);
        EditText passText = view.findViewById(R.id.edit_password_register);
        EditText nameText = view.findViewById(R.id.edit_name_register);



        view.findViewById(R.id.btn_register)
                .setOnClickListener(v -> {
                    // TODO implement sign up logic
                    String email = emailText.getText().toString();
                    String pass = passText.getText().toString();
                    String name = nameText.getText().toString();

                    createUser(name,email,pass,view);




                    Log.d("TAAGE","Clicked");
                    //NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                    // Navigation.findNavController(view).navigate(action);
                });

        return view;
    }

    private void createUser(String name, String email, String pass, View view) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in is successful


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("User Data/" + mAuth.getCurrentUser().getUid());

                                        UserData newUser = new UserData(0,0);

                                        myRef.setValue(newUser);
                                        NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToDashboardFragment();
                                        Navigation.findNavController(getView()).navigate(action);

                                    }
                                }
                            });


                }


            }});}


}
