package com.example.laundryapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    EditText regName, regUsrname, regContact, regAddress, usrEmail, regPss, regCnfrmPss;
    Button btnRegister;
    TextView lgnAccount;
    boolean valid = true;
    boolean psswrdVisible;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr_register);

        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        regName = findViewById(R.id.reg_fullname);
        regUsrname = findViewById(R.id.reg_username);
        regContact = findViewById(R.id.reg_phonenum);
        regAddress = findViewById(R.id.reg_address);
        usrEmail = findViewById(R.id.reg_email);
        regPss = findViewById(R.id.reg_password);
        regCnfrmPss = findViewById(R.id.reg_cnfrm_psswrd);
        btnRegister = findViewById(R.id.bttnReg);
        lgnAccount = findViewById(R.id.rgstrdUsr);

        regPss.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int reg_psswrd = 2;
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=regPss.getRight()-regPss.getCompoundDrawables()[reg_psswrd].getBounds().width()){
                        int selection = regPss.getSelectionEnd();
                        if (psswrdVisible){
                            regPss.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_off,0);
                            regPss.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            psswrdVisible = false;
                        }
                        else {
                            regPss.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_on,0);
                            regPss.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            psswrdVisible = true;
                        }
                        regPss.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        regCnfrmPss.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int reg_Cnfrmpsswrd = 2;
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=regCnfrmPss.getRight()-regCnfrmPss.getCompoundDrawables()[reg_Cnfrmpsswrd].getBounds().width()){
                        int selection = regCnfrmPss.getSelectionEnd();
                        if (psswrdVisible){
                            regCnfrmPss.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_off,0);
                            regCnfrmPss.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            psswrdVisible = false;
                        }
                        else {
                            regCnfrmPss.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_visibility_on,0);
                            regCnfrmPss.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            psswrdVisible = true;
                        }
                        regCnfrmPss.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(regName);
                checkField(regUsrname);
                checkField(regContact);
                checkField(regAddress);
                checkField(usrEmail);
                checkField(regPss);
                checkField(regCnfrmPss);

                if (valid){
                    // start the user registration
                    fbAuth.createUserWithEmailAndPassword(usrEmail.getText().toString(),regPss.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fbAuth.getCurrentUser();
                            Toast.makeText(UsrRegister.this,"Account Created",Toast.LENGTH_SHORT).show();
                            DocumentReference docRef = fbStore.collection("Customers").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap();
                            userInfo.put("Full Name", regName.getText().toString());
                            userInfo.put("Username", regUsrname.getText().toString());
                            userInfo.put("Phone No.", regContact.getText().toString());
                            userInfo.put("Address", regAddress.getText().toString());
                            userInfo.put("Email", usrEmail.getText().toString());
                            userInfo.put("Password",regPss.getText().toString());
                            userInfo.put("isCustomer", "1");
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
            Toast.makeText(UsrRegister.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            textField.setError("Error");
            valid = false;
        }
        else if (!regPss.equals(regCnfrmPss)){
            Toast.makeText(UsrRegister.this, "Password are not match", Toast.LENGTH_SHORT).show();
        }
        else{
            valid = true;
        }
        return valid;
    }
}