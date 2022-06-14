package com.example.laundryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomerBooking extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private EditText name, contact;;

    private Button bookNow, bttnHome, dateButton, timeButton;

    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.editTxtFullname);
        contact = findViewById(R.id.editTxtContact);
        timeButton = findViewById(R.id.timePicker);
        dateButton = findViewById(R.id.datePicker);

        bookNow = findViewById(R.id.bookbttn);
        bttnHome = findViewById(R.id.homebttn);

        initDatePicker();
        dateButton.setText(getTodaysDate());

        Toast.makeText(this, "Welcome to your reservation process", Toast.LENGTH_SHORT).show();

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(name);
                checkField(contact);

                FirebaseUser user = fbAuth.getCurrentUser();
                DocumentReference docRef = fbStore.collection("Reservation").document(user.getUid());
                Map<String,Object> userInfo = new HashMap();
                userInfo.put("Full Name", name.getText().toString());
                userInfo.put("Contact", contact.getText().toString());
                userInfo.put("Time", timeButton.getText().toString());
                userInfo.put("Date", dateButton.getText().toString());
                userInfo.put("isCustomer", "1");
                docRef.set(userInfo);
            }
        });
        bttnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
                finish();
            }
        });
    }
    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour, minute;

                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CustomerBooking.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        if (hour>12){
                            timeButton.setText(hour+":"+min + " " + "pm");
                        }
                        else if (hour == 12){
                            timeButton.setText(hour+":"+min + " " + "pm");
                        }
                        else {
                            timeButton.setText(hour+":"+min + " " + "am" );
                        }
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
    }
    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month){
        if (month == 1)
            return "January";
        if (month == 2)
            return "February";
        if (month == 3)
            return "March";
        if (month == 4)
            return "April";
        if (month == 5)
            return "May";
        if (month == 6)
            return "June";
        if (month == 7)
            return "July";
        if (month == 8)
            return "August";
        if (month == 9)
            return "September";
        if (month == 10)
            return "October";
        if (month == 11)
            return "November";
        if (month == 12)
            return "December";

        return "January";
    }
    public void openDatePicker(View view){

        datePickerDialog.show();
    }
    public boolean checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            Toast.makeText(CustomerBooking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            textField.setError("Error");
            valid = false;
        }
        else{
            startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
            finish();
            Toast.makeText(CustomerBooking.this, "Reservation Complete", Toast.LENGTH_SHORT).show();
            valid = true;
        }
        return valid;
    }
}