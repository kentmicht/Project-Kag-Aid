package com.example.kagaid.kagaid;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Maps.MapsActivity;
import com.example.kagaid.kagaid.Patient.PatientRecords;
import com.example.kagaid.kagaid.SkinIllness.SkinIllness;

public class Homepage extends AppCompatActivity {

    public Button logoutBtn;
    private SensorManager sm;
    private float acelVal;  //CURRENT ACCELERATION AND GRAVITY
    private float acelLast; //LAST ACCELERATION AND GRAVITY
    private float shake;    //ACCELERATION VALUE differ FROM GRAVITY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        String userName = (String) getIntent().getStringExtra("USERNAME");

        TextView name = (TextView)findViewById(R.id.header);
        name.setText("Welcome "+userName+"!");
    }


    public void userName(String username){

    }
    public void logOut(View view) {
        alertLogout();
    }

    public void alertLogout(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(Homepage.this);
        a_builder.setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openLogin();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Kag-Aid");
        alert.show();
    }

    public void openLogin(){
        Intent login = new Intent(this, LogIn.class);
        startActivity(login);
    }

    public void map(View view){
        Intent mapNav = new Intent(this, MapsActivity.class);
        startActivity(mapNav);
    }

    public void openPatientRecord(View view){
        Intent patientRec = new Intent(this, PatientRecords.class);
        startActivity(patientRec);
    }

    public void openSkinIllness(View view){
        Intent skinIllness = new Intent(this, SkinIllness.class);
        startActivity(skinIllness);
    }


}
