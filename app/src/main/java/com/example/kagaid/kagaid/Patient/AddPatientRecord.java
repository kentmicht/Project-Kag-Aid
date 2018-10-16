package com.example.kagaid.kagaid.Patient;
/**
 * Created by TEAM4RA (Alcantara, Genelsa, Mozo, Talisaysay)
 **/
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kagaid.kagaid.Diagnosis.PostDiagnosis;
import com.example.kagaid.kagaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class AddPatientRecord extends AppCompatActivity {

    EditText fullname, address;
    TextView birthdate;
    Spinner gender;
    Button patientAdd;
    String uId;
    String bId;

    DatabaseReference databasePatient;
    private android.app.DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "AddPatientRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_record);

        //Internet Connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //Add Patient Records

        }else {
            toastMessage("No Internet Connection");
        }

        uId = (String) getIntent().getStringExtra("USER_ID");
        bId = (String) getIntent().getStringExtra("BARANGAY_ID");
        databasePatient = FirebaseDatabase.getInstance().getReference("person_information");

        toastMessage("User Id:" + uId);
        toastMessage("Barangay Id: " + bId);

        fullname = (EditText) findViewById(R.id.fullname);
        birthdate = (TextView) findViewById(R.id.birthday);
        address = (EditText) findViewById(R.id.address);
        gender = (Spinner) findViewById(R.id.gender);
        patientAdd = (Button) findViewById(R.id.addPatientBtn);

        patientAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addPatient();
            }
        });
        birthdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPatientRecord.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDataSet: yyyy-mm-dd: " + year + "-" + month + "-" + dayOfMonth);
                if(month >= 1 && month <=9){
                    String date = year + "-0" + month + "-" + dayOfMonth;
                    birthdate.setText(date);
                }else{
                    String date = year + "-" + month + "-" + dayOfMonth;
                    birthdate.setText(date);
                }


            }
        };
    }

    private void addPatient(){
        final String fullnameP = fullname.getText().toString().trim();
        final String bdayP = birthdate.getText().toString();
        final String addressP = address.getText().toString();
        final String genderP = gender.getSelectedItem().toString();
        final String status = "1";

        if(!TextUtils.isEmpty(fullnameP) && !bdayP.matches("Birthdate") && !TextUtils.isEmpty(addressP)){
            //checking if there's unique
            databasePatient.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean duplicatePatient = false;
                    for (DataSnapshot dsPatient : dataSnapshot.getChildren()) {
                        if(fullnameP.equals(dsPatient.child("fullname").getValue().toString())){
                            duplicatePatient = true;
                        }
                    }
                    if(duplicatePatient == false){
                        String pid = databasePatient.push().getKey();
                        String age = calculateAge(bdayP);
                        //Patient patient = new Patient(pid, fullnameP, bdayP, genderP, addressP);
                        Patient patient = new Patient(pid, fullnameP, bdayP, genderP, addressP, "Not yet scanned", status, age, bId);

                        databasePatient.child(pid).setValue(patient);

                        fullname.setText("");
                        birthdate.setText("");
                        address.setText("");

                        progressDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toastMessage("Patient Record Added");
                                finish();
                            }
                        }, 2000);
                    }else{
                        toastMessage("Patient has been added already");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            toastMessage("Please don't leave any field empty.");
        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    public String currentDate(){
        java.util.Date c = java.util.Calendar.getInstance().getTime();

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public String calculateAge(String date){
        String age = null;
        String year = date.substring(0, 4);

        age = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(year));
        return age;
    }

    public void back(View view){
        finish();
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void progressDialog(){
        ProgressDialog pd = new ProgressDialog(AddPatientRecord.this);
        pd.setMessage("Processing Patient's information...");
        pd.setCancelable(false);
        pd.show();
    }


}
