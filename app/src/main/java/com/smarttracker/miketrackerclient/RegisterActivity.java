package com.smarttracker.miketrackerclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText beaconEditText =  findViewById(R.id.editText);
        final EditText nameEditText = findViewById(R.id.editText2);
        final EditText passwordEditText = findViewById(R.id.editText3);

        Button registerButton =  findViewById(R.id.button6);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference inputName = database.getReference("users/" + beaconEditText.getText().toString() + "/name");
                DatabaseReference inputPassword = database.getReference("users/" + beaconEditText.getText().toString() + "/password");
                DatabaseReference inputBeaconId = database.getReference("users/" + beaconEditText.getText().toString() + "/beaconId");

                inputName.setValue(nameEditText.getText().toString());
                inputPassword.setValue(passwordEditText.getText().toString());
                inputBeaconId.setValue(beaconEditText.getText().toString());
            }
        });
    }
}

