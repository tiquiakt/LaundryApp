package com.example.laundryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsrRegister extends AppCompatActivity {

    EditText regName, regContact, usrEmail, regPss, regCnfrmPss;
    Button btnRegister;
    TextView lgnAccount;
    boolean valid = true;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr_register);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        regName = findViewById(R.id.reg_fullname);
        regContact = findViewById(R.id.reg_phonenum);
        usrEmail = findViewById(R.id.reg_email);
        regPss = findViewById(R.id.reg_password);
        regCnfrmPss = findViewById(R.id.reg_cnfrm_psswrd);
        btnRegister = findViewById(R.id.bttnReg);
        lgnAccount = findViewById(R.id.rgstrdUsr);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(regName);
                checkField(regContact);
                checkField(usrEmail);
                checkField(regPss);
                checkField(regCnfrmPss);

                if (valid){
                    // start the user registration
                    fbAuth.createUserWithEmailAndPassword(usrEmail.getText().toString(),regPss.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(UsrRegister.this,"Account Created",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fbAuth.getCurrentUser();
                            DocumentReference docRef = fbStore.collection("Customers").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap();
                            userInfo.put("Full Name", regName.getText().toString());
                            userInfo.put("Phone No.", regContact.getText().toString());
                            userInfo.put("Email", usrEmail.getText().toString());
                            userInfo.put("isCustomer", "0");
                            docRef.set(userInfo);

                            startActivity(new Intent(getApplicationContext(),customer_dashboard.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UsrRegister.this,"Failed to Create Account",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        lgnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UsrLogin.class));
            }
        });
    }

    public boolean checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }
}