package com.example.chatup.GroupChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatup.Contacts.Contacts;
import com.example.chatup.MemoryData;
import com.example.chatup.R;
import com.example.chatup.chat.Chat;
import com.example.chatup.chat.ChatAdapter;
import com.example.chatup.chat.ChatList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatgroup extends AppCompatActivity {


    public boolean LoadingFirstTime = true;

    public GroupchatAdapter GroupchatAdapter;
    public String chatKey;
    int members;


    public final List<GroupchatList> groupchatLists = new ArrayList<>();


    public RecyclerView groupchattingRecyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");
    String getUserMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        final EditText mesaageEditText = (EditText) findViewById(R.id.groupmessageEditTxt);
        groupchattingRecyclerView = (RecyclerView) findViewById(R.id.groupchattingRecyclerView);
        final String getName = getIntent().getStringExtra("name");
        final String myName = getIntent().getStringExtra("myname");
        String getProfilePic = getIntent().getStringExtra("profile_pic");
        this.chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");
        this.getUserMobile = MemoryData.getData(this);

        ((TextView) findViewById(R.id.groupusername)).setText(getName);
        databaseReference.child("Groups").child(getName).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                members = (int) snapshot.getChildrenCount();
                ((TextView) findViewById(R.id.members)).setText(members + " members");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        Picasso.get().load(getProfilePic).into((ImageView) (CircleImageView) findViewById(R.id.MygroupprofilePic));
        groupchattingRecyclerView.setHasFixedSize(true);
        groupchattingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupchatAdapter chatAdapter2 = new GroupchatAdapter(groupchatLists, this);
        GroupchatAdapter = chatAdapter2;
        groupchattingRecyclerView.setAdapter(chatAdapter2);

        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                    groupchatLists.clear();
                    for (DataSnapshot messagesSnapshot : snapshot.child("Groups").child(getName).child("messages").getChildren()) {
                            String messageTimestamps = messagesSnapshot.getKey();
                        String getmyname  = null;
                        String getmobilephone = messagesSnapshot.child("mobile").getValue(String.class);
                        if (messageTimestamps != null) {
                            getmyname = messagesSnapshot.child("name").getValue(String.class);
                        }
                        Date date = null;
                        if (messageTimestamps != null) {
                            date = new Date(new Timestamp(Long.parseLong(messageTimestamps)).getTime());
                        }
                           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                            groupchatLists.add(new GroupchatList((simpleDateFormat.format(date)) ,messagesSnapshot.child(NotificationCompat.CATEGORY_MESSAGE).getValue(String.class),getmobilephone,getName,simpleTimeFormat.format(date),getmyname));

                        GroupchatAdapter.updategroupChatList(groupchatLists);
                            groupchattingRecyclerView.scrollToPosition(groupchatLists.size());

                    }

                groupchattingRecyclerView.setAdapter(chatAdapter2);

            }

            public void onCancelled(DatabaseError error) {
            }
        });
        ((ImageView) findViewById(R.id.groupsendbtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                String getTxtMessage = mesaageEditText.getText().toString();

                databaseReference.child("Groups").child(getName).child("messages").child(currentTimestamp).child(NotificationCompat.CATEGORY_MESSAGE).setValue(getTxtMessage);
                databaseReference.child("Groups").child(getName).child("messages").child(currentTimestamp).child("name").setValue(myName);
                databaseReference.child("Groups").child(getName).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);
                mesaageEditText.setText("");
                GroupchatAdapter.updategroupChatList(groupchatLists);
                groupchattingRecyclerView.scrollToPosition(groupchatLists.size() + 1);

            }
        });
        ((ImageView) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {finish();
            }
        });
        findViewById(R.id.addUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Chatgroup.this, Contacts.class);
                intent.putExtra("groupname",getName);
                startActivity(intent);
            }
        });



    }
}