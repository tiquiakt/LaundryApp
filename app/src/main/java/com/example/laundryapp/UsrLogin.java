package com.example.laundryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UsrLogin extends AppCompatActivity {

    EditText usrEmail, usrPassword;
    Button btnLogin, btnGuest;
    ShapeableImageView fb, google, twitter;
    TextView frgtpass,  rgstrAccount;
    boolean valid, usrAdmin = true;
    boolean psswrdVisible;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr_login);

        usrEmail = findViewById(R.id.usrname);
        usrPassword = findViewById(R.id.psswrd);
        btnLogin = findViewById(R.id.bttnLogin);
        btnGuest = findViewById(R.id.bttnGuest);
        frgtpass = findViewById(R.id.frgt_pass);
        rgstrAccount = findViewById(R.id.newUsr);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        usrPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int usr_psswrd = 2;
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=usrPassword.getRight()-usrPassword.getCompoundDrawables()[usr_psswrd].getBounds().width()){
                        int selection = usrPassword.getSelectionEnd();
                        if (psswrdVisible){
                            usrPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_off,0);
                            usrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            psswrdVisible = false;
                        }
                        else {
                            usrPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_on,0);
                            usrPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            psswrdVisible = true;
                        }
                        usrPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        rgstrAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UsrRegister.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(usrEmail);
                checkField(usrPassword);
                Log.d("TAG", "onClick" + usrEmail.getText().toString());

                if (valid){
                    fbAuth.signInWithEmailAndPassword(usrEmail.getText().toString(),usrPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(UsrLogin.this,"Login Success", Toast.LENGTH_SHORT).show();
                           if (usrAdmin){
                               isAdmin(authResult.getUser().getUid());
                           }
                           else {
                               isCustomer(authResult.getUser().getUid());
                           }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UsrLogin.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
                finish();
            }
        });
    }
    private void isAdmin(String uid) {
        DocumentReference docRef = fbStore.collection("Admin").document(uid);
        // take the data from document
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess" + documentSnapshot.getData());
                // check user if admin or customer

                if (documentSnapshot.getString("isAdmin") != null){
                    startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
                    finish();
                }
                else{
                    isCustomer(fbAuth.getUid());
                }
                }
        });
    }
    private void isCustomer(String uid) {
        DocumentReference docRef = fbStore.collection("Customers").document(uid);
        // take the data from document
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess" + documentSnapshot.getData());
                // check user if customer
                if (documentSnapshot.getString("isCustomer") != null){
                    startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
                    finish();
                }
            }
        });
    }


    private boolean checkField(@NonNull EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            Toast.makeText(UsrLogin.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }

}