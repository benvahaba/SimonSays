package com.example.simon_says_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    static final int GOOGLE_SIGN = 123;
    private FirebaseAuth mAuth;
    private Button btn_login, btn_logout;
    private TextView text;
    private ImageView image;
    private ProgressBar progressBar;
    private GoogleSignInClient m_googleSignInClient;
    private DatabaseReference currentUserReff;
    private String userID;
    private TextView m_ContinueTV;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.login);
        btn_logout = findViewById(R.id.logout);
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        m_ContinueTV=(TextView) findViewById(R.id.continueBTN);
        progressBar = findViewById(R.id.progress_circular);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        m_googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        btn_login.setOnClickListener(v->SignInGoogle());
        btn_logout.setOnClickListener(v->logOut());
        if(mAuth.getCurrentUser()!=null)
        {
            FirebaseUser user=mAuth.getCurrentUser();
            userID=user.getUid();
            UpdateUI(user);


        }
    }

    void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = m_googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                if(account!=null)
                {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG", "Signin Success");
                FirebaseUser user = mAuth.getCurrentUser();
                UpdateUI(user);


                userID=user.getUid();

                    //if the user never logged in before and he has no database we set default values in firebase
                currentUserReff= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                currentUserReff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                        {
                            Map emptyUserData=new HashMap();
                            emptyUserData.put("nickname","");
                            emptyUserData.put("score",0);
                            currentUserReff.setValue(emptyUserData);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            } else {

                progressBar.setVisibility(View.VISIBLE);
                Log.d("TAG", "Signin Failure", task.getException());
                Toast.makeText(this, "Signin Failed!", Toast.LENGTH_SHORT).show();
                UpdateUI(null);
            }
        });

    }

    private void UpdateUI(FirebaseUser user) {
        if(user!=null)
        {
            String name=user.getDisplayName();
            String email=user.getEmail();


            text.append(name+"\n");
            text.append(email);
            btn_login.setVisibility(View.INVISIBLE);
            btn_logout.setVisibility(View.VISIBLE);

            m_ContinueTV.setVisibility(View.VISIBLE);
            goToNextActivity();

        }
        else
        {
            text.setText(getString(R.string.firebase_login));
            Picasso.get().load(R.drawable.ic_firebase_logo).into(image);
            btn_login.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.INVISIBLE);
            m_ContinueTV.setVisibility(View.INVISIBLE);

        }
    }

    private void goToNextActivity() {
        Intent i = new Intent(this, NickNameActivity.class);
        startActivity(i);
    }

    void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        m_googleSignInClient.signOut().addOnCompleteListener(this,task->
        { UpdateUI(null);});
    }

    public void onContinueClicked(View view) {
        goToNextActivity();
    }
}
