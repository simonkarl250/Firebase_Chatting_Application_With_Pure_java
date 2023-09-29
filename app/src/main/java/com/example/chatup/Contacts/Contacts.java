package com.example.chatup.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
     RecyclerView recyclerView;
     ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
     ContactsAdapter contactsAdapter;
    String groupName;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");

//    public FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ((ImageView) findViewById(R.id.contbackBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {finish();
            }
        });

        recyclerView = findViewById(R.id.contact_recycler);
        groupName = getIntent().getStringExtra("groupname");

        checkPermission();
    }


    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(Contacts.this, Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Contacts.this,new String[]{Manifest.permission.READ_CONTACTS},100);
        }else{
           getContantList(); 
        }
    }

    private void getContantList() {
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor = getContentResolver().query(uri,null,null,null,sort);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
              @SuppressLint("Range")
              String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
              @SuppressLint("Range")
              String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
              Uri uriPhone =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
              String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?";
              Cursor phoneCursor= getContentResolver().query(uriPhone,null,selection,new String[]{id},null);
              if (phoneCursor.moveToNext()){
                  @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                  ContactModel model = new ContactModel();
                  model.setName(name);
                  model.setNumber(number);
                  model.setGroupname(groupName);
                  arrayList.add(model);
                  phoneCursor.close();

              }

            }
            cursor.close();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter =new ContactsAdapter(this,arrayList);
        recyclerView.setAdapter(contactsAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getContantList();
        }else {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }



}