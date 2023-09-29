package com.example.chatup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");
    EditText email;
    EditText name;
    EditText phone;
    Button btnloginpage;
    EditText password,confirmpassword;
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.name = findViewById(R.id.inputName);
        this.email = findViewById(R.id.inputEmaillgn);
        this.phone = findViewById(R.id.inputPhone);
        password =findViewById(R.id.passwordenter);
        confirmpassword = findViewById(R.id.confirmpassword);
        Button register = findViewById(R.id.btnRegister);
        btnloginpage= findViewById(R.id.buttonlgnpage);
        btnloginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Register.this,login.class);
               startActivity(intent);

            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading....");
//        if (!MemoryData.getData(this).isEmpty()) {
//            Intent intent = new Intent(this, NavigationActivity.class);
//            intent.putExtra("mobile", MemoryData.getData(this));
//            intent.putExtra("name", MemoryData.getName(this));
//            intent.putExtra("email", "");
//            startActivity(intent);
//            finish();
//        }
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                progressDialog.show();

                final String usernametxt = Register.this.name.getText().toString();
                final String emailtxt = Register.this.email.getText().toString();
                final String phonetxt = Register.this.phone.getText().toString();
                final String regpassword =password.getText().toString();
                final String regconfirm = confirmpassword.getText().toString();

                if (!(usernametxt.isEmpty()) && !(emailtxt.isEmpty()) && !(phonetxt.isEmpty()) && !(regpassword.isEmpty())) {

                    if (!emailtxt.matches(emailPattern)) {
                        email.setError("Wrong Email");
                        progressDialog.dismiss();
                    } else if (regpassword.length() < 8) {
                        password.setError("Too short password!");
                        progressDialog.dismiss();
                    }  else if (!regconfirm.equals(regpassword)) {
                        confirmpassword.setError("password mismatch");
                        password.setText("");
                        confirmpassword.setText("");
                        progressDialog.dismiss();

                    }else{

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.child("users").hasChild(phonetxt)) {
                                    Toast.makeText(Register.this, "phone exists", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                Register.this.databaseReference.child("users").child(phonetxt).child("email").setValue(emailtxt);
                                Register.this.databaseReference.child("users").child(phonetxt).child("name").setValue(usernametxt);
                                Register.this.databaseReference.child("users").child(phonetxt).child("password").setValue(regpassword);
                                Register.this.databaseReference.child("users").child(phonetxt).child("profile_pic").setValue("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpCKq1XnPYYDaUIlwlsvmLPZ-9-rdK28RToA&usqp=CAU");
                                MemoryData.saveData(phonetxt, Register.this);
                                MemoryData.saveName(usernametxt, Register.this);
                                Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, NavigationActivity.class);
                                intent.putExtra("mobile", phonetxt);
                                intent.putExtra("name", usernametxt);
                                intent.putExtra("email", emailtxt);
                                Register.this.startActivity(intent);
                                Register.this.finish();
                            }

                            public void onCancelled(DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });

                    }

                }else{
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


            }
        });
    }
}
