package com.example.laundryapp;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.core.DatabaseInfo;
        import com.google.firebase.firestore.model.DatabaseId;

        import java.util.HashMap;
        import java.util.Map;

public class usr_Register extends AppCompatActivity {

    boolean valid = true;
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://laundryapp-1dea8-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usrFullName = findViewById(R.id.reg_fullname);
        final EditText PhoneNo = findViewById(R.id.reg_phonenum);
        final EditText usrEmail = findViewById(R.id.reg_email);
        final EditText usrPassword = findViewById(R.id.reg_password);
        final EditText CnfrmPassword = findViewById(R.id.reg_cnfrm_psswrd);

        final Button btnReg = findViewById(R.id.bttnReg);
        final TextView lgnAccount = findViewById(R.id.rgstrdUsr);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(usrFullName);
                checkField(PhoneNo);
                checkField(usrEmail);
                checkField(usrPassword);
                checkField(CnfrmPassword);

                final String Fullname = usrFullName.getText().toString();
                final String Email = usrEmail.getText().toString();
                final String Contact = PhoneNo.getText().toString();
                final String Password = usrPassword.getText().toString();
                final String ConfirmPassword = CnfrmPassword.getText().toString();

                if (Fullname.isEmpty() || Email.isEmpty() || Contact.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()){
                    Toast.makeText(usr_Register.this,"Kindly fill all fields.",Toast.LENGTH_SHORT).show();
                }
                else if (!Password.equals(ConfirmPassword)){
                    Toast.makeText(usr_Register.this,"Password are not match.",Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.child("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Email)){
                                Toast.makeText(usr_Register.this,"Account is already exist.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                dbRef.child("Customers").child(Email).child("Fullname").setValue(Fullname);
                                dbRef.child("Customers").child(Email).child("Phone No.").setValue(Contact);
                                dbRef.child("Customers").child(Email).child("Email").setValue(Email);
                                dbRef.child("Customers").child(Email).child("Password").setValue(Password);

                                Toast.makeText(usr_Register.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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

