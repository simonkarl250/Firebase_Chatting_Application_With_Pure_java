package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    Button btnregpage;
    EditText loginmobile;
    EditText  loginpassword;
    Button loginbtn;
    String lgnMobile ;
    String lgnPassword;
//    static String getname="";
//    static String getemail="";
//    static String getpass="";



    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginmobile =findViewById(R.id.editTextPhone);
        loginpassword =findViewById(R.id.editTextTextPassword);
        loginbtn =findViewById(R.id.login);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading....");
        if (!MemoryData.getData(this).isEmpty()) {
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lgnMobile = loginmobile.getText().toString();
                lgnPassword = loginpassword.getText().toString();

                progressDialog.show();

                if (!(lgnMobile.equals("")) && !(lgnPassword.equals(""))) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("users").hasChild(lgnMobile)) {
                            String getpass =  snapshot.child("users").child(lgnMobile).child("password").getValue(String.class);
                            String getemail = snapshot.child("users").child(lgnMobile).child("email").getValue(String.class);
                            String getname =  snapshot.child("users").child(lgnMobile).child("name").getValue(String.class);
//
                            if (getpass.equals(lgnPassword)){
                                   Intent intent = new Intent(login.this, NavigationActivity.class);
                                    intent.putExtra("mobile", lgnMobile);
                                    intent.putExtra("name", getname);
                                    intent.putExtra("email",getemail);
                                    MemoryData.saveData(lgnMobile, login.this);
                                    MemoryData.saveName(getname, login.this);
                                    startActivity(intent);
//                                    progressDialog.dismiss();
                                    finish();
                                }
                                else {
                                    Toast.makeText(login.this, "your password is wrong try again", Toast.LENGTH_SHORT).show();
                                    loginpassword.setText("");

                                    progressDialog.dismiss();
                                    return;
                                }

                        }else{
                            progressDialog.dismiss();
                           Toast.makeText(login.this, "Record not found! please Register", Toast.LENGTH_SHORT).show();
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                    if (!getpass.isEmpty()) {
//                        if (getpass.equals(lgnPassword)) {
//                            Intent intent = new Intent(login.this, NavigationActivity.class);
//                            intent.putExtra("mobile", lgnMobile);
//                            intent.putExtra("name", getname);
//                            intent.putExtra("email", getemail);
//                            MemoryData.saveData(lgnMobile, login.this);
//                            MemoryData.saveName(getname, login.this);
//                            startActivity(intent);
////                                    progressDialog.dismiss();
//                            finish();
//                        } else {
//                            Toast.makeText(login.this, "your password is wrong try again", Toast.LENGTH_SHORT).show();
//                            loginpassword.setText("");
//
//                            progressDialog.dismiss();
//
//                        }
//                    }else {
//                        Toast.makeText(login.this, "failed to login", Toast.LENGTH_SHORT).show();
//                    }


                    }else{
                    Toast.makeText(login.this, "all field required", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        btnregpage = findViewById(R.id.signuppg);
        btnregpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.chatup.login.this,Register.class);
                startActivity(intent);
            }
        });
    }
}