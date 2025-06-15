package com.s22010040.test5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    com.s22010040.lab2.DatabaseHelper myDb;
    EditText editUsername, editPassword ;
    Button btnAddData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        myDb = new com.s22010040.lab2.DatabaseHelper(this);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnAddData = findViewById(R.id.button);

        addData();

    }

    public  void addData(){
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDataInserted = myDb.insertData(editUsername.getText().toString(),editPassword.getText().toString());

                if (isDataInserted == true){
                    Toast.makeText(MainActivity.this, "data is inserted properly", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, Map.class);
                    startActivity(intent);}

                else{
                    Toast.makeText(MainActivity.this, "data is not inserted properly", Toast.LENGTH_LONG).show();}
            }
        });
    }
}