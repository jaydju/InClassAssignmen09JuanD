package com.example.android.inclassassignmen09_juand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference userReference;

    EditText questionField;
    EditText answerField;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        questionField = (EditText) findViewById(R.id.question_field);
        answerField = (EditText) findViewById(R.id.answer_field);

        userReference = database.getReference("HardCodedUser");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    userReference = database.getReference(user.getUid());
                }
                else {
                    startActivity(new Intent(MainActivity.this, loginActivity.class));
                }
            }
        };


    }

    public void sendToFirebase(View view) {
        String q = questionField.getText().toString();
        String a = answerField.getText().toString();
        TestItem testItem = new TestItem(q, a);
        userReference.push().setValue(testItem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    public void logOut(View view){auth.signOut();}

}
