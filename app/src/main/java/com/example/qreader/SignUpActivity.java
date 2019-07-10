package com.example.qreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText username, email, phonenumber, password, confirmpassword;
    Button signupbtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.emailid);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        password = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        signupbtn = (Button) findViewById(R.id.signupbtn);

        mAuth = FirebaseAuth.getInstance();



        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String usernamestring = username.getText().toString().trim();
                final String emailstring = email.getText().toString().trim();
                final String phonenumberstring = phonenumber.getText().toString().trim();
                final String passwordstring = password.getText().toString().trim();
                final String confirmpasswordstring = confirmpassword.getText().toString().trim();
                boolean verify = false;

                if (usernamestring.isEmpty() || emailstring.isEmpty() || phonenumberstring.isEmpty() || passwordstring.isEmpty() || confirmpasswordstring.isEmpty()) {
                    verify = false;
                    Toast.makeText(SignUpActivity.this, "All fields are compulsory.\n None is to be left empty.", Toast.LENGTH_LONG).show();
                } else if (!passwordstring.equals(confirmpasswordstring)) {
                    verify = false;
                    Toast.makeText(SignUpActivity.this, "Both the password fields should contain the same text.", Toast.LENGTH_LONG).show();
                } else {
                    verify = true;
                }


                if (verify) {
                    mAuth.createUserWithEmailAndPassword(emailstring, passwordstring)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success

                                        //Update the Display name of current Username " I don't exactly know where this value is stored in the database but it works . We can even Store profile pic in this manner .
                                        // It is more better than storing in the database because this method is easily accessible"  .
                                        final FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(usernamestring).build();
                                        CurrentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, CurrentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        //Create a "Profile" class object and store all the inserted values
                                        Profile profile = new Profile(usernamestring, emailstring, phonenumberstring, passwordstring);
                                        // Write a message to the database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference();
                                        //Storing profile object in the database
                                        myRef.child("ProfileInfo").child(CurrentUser.getUid()).setValue(profile);

                                        Toast.makeText(SignUpActivity.this, "Signup Successful.",
                                                Toast.LENGTH_SHORT).show();

                                        //Go to Home page
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Signup Failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                        password.setText("");
                                        confirmpassword.setText("");
                                        username.setText("");
                                        email.setText("");
                                        phonenumber.setText("");

                                    }

                                    // ...
                                }
                            });
                }

            }
        });

    }
}
