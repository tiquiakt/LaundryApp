package com.example.laundryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

public class customer_dashboard<uid> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView name, userName, useremail;
    EditText usr_email, usrAddress;
    Button booking, purchase;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;
    String userID;
    boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        name = findViewById(R.id.profile_fullname);
        userName = findViewById(R.id.profile_username);

        booking = findViewById(R.id.button3);
        purchase = findViewById(R.id.button4);
        booking.setOnClickListener(this);
        purchase.setOnClickListener(this);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        userID = fbAuth.getCurrentUser().getUid();
        DocumentReference docRef = fbStore.collection("Customers").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                View header = navView.getHeaderView(0);
                name.setText(value.getString("Full Name"));
                userName.setText(value.getString("Username"));

                useremail = header.findViewById(R.id.profile_email);
                useremail.setText(value.getString("Email"));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.custom_home:
                startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProfile()).commit();
                break;
            case R.id.bookHistory:
                startActivity(new Intent(getApplicationContext(),FragmentBookingHstry.class));
                break;
            case R.id.message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMessage()).commit();
                Toast.makeText(this, "Send Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSettings()).commit();
                break;
            case R.id.share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentShare()).commit();
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHelp()).commit();
                break;
            case R.id.logout:
                startActivity(new Intent(getApplicationContext(),UsrLogin.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3){
            startActivity(new Intent(customer_dashboard.this, CustomerBooking.class));
            Toast.makeText(this, "Proceeding", Toast.LENGTH_SHORT).show();

        }
        else if (view.getId() == R.id.button4){
            Toast.makeText(this, "In progress", Toast.LENGTH_SHORT).show();
        }
    }
}