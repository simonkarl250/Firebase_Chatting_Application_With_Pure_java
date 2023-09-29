package com.example.chatup.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatup.MemoryData;
import com.example.chatup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class Chat extends AppCompatActivity {

    public boolean LoadingFirstTime = true;

    public ChatAdapter chatAdapter;
    public String chatKey;
    public final List<ChatList> chatLists = new ArrayList();
    public RecyclerView chattingRecyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");
    String getUserMobile = "";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final EditText mesaageEditText = (EditText) findViewById(R.id.messageEditTxt);
        this.chattingRecyclerView = (RecyclerView) findViewById(R.id.chattingRecyclerView);
        final String getName = getIntent().getStringExtra("name");
        String getProfilePic = getIntent().getStringExtra("profile_pic");
        this.chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");
        this.getUserMobile = MemoryData.getData(this);
        ((TextView) findViewById(R.id.name)).setText(getName);
        Picasso.get().load(getProfilePic).into((ImageView) (CircleImageView) findViewById(R.id.profilePic));
        this.chattingRecyclerView.setHasFixedSize(true);
        this.chattingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter chatAdapter2 = new ChatAdapter(this.chatLists, this);
        this.chatAdapter = chatAdapter2;
        this.chattingRecyclerView.setAdapter(chatAdapter2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = snapshot;
                if (Chat.this.chatKey.isEmpty()) {
                    String unused = Chat.this.chatKey = getMobile + Chat.this.getUserMobile;
                    String tempkey = Chat.this.getUserMobile + getMobile;
                    if (dataSnapshot.hasChild("chat")) {
                        for (DataSnapshot chatkeySnapshot : dataSnapshot.child("chat").getChildren()) {
                            if (chatkeySnapshot.getKey().equals(Chat.this.chatKey) || chatkeySnapshot.getKey().equals(tempkey)) {
                                String unused2 = Chat.this.chatKey = chatkeySnapshot.getKey();
                            }
                        }
                    }
                }
                if (dataSnapshot.hasChild("chat") && dataSnapshot.child("chat").child(Chat.this.chatKey).hasChild("messages")) {
                    Chat.this.chatLists.clear();
                    for (DataSnapshot messagesSnapshot : dataSnapshot.child("chat").child(Chat.this.chatKey).child("messages").getChildren()) {
                        if (messagesSnapshot.hasChild(NotificationCompat.CATEGORY_MESSAGE) && messagesSnapshot.hasChild("mobile")) {
                            String messageTimestamps = messagesSnapshot.getKey();
                            Date date = new Date(new Timestamp(Long.parseLong(messageTimestamps)).getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
                            SimpleDateFormat simpleTimeFormat;
                            simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                            long now = Long.parseLong( String.valueOf(System.currentTimeMillis()));
                            long then =Long.parseLong(messageTimestamps);
                            if ((now-then) >300000){
                                databaseReference.child("chat").child(chatKey).child("messages").child(messageTimestamps).removeValue();
                                 continue;
                            }

                            Chat.this.chatLists.add(new ChatList((String) messagesSnapshot.child("mobile").getValue(String.class), getName,  messagesSnapshot.child(NotificationCompat.CATEGORY_MESSAGE).getValue(String.class), simpleDateFormat.format(date), simpleTimeFormat.format(date)));
                            if (!Chat.this.LoadingFirstTime) {
                                long parseLong = Long.parseLong(messageTimestamps);
                                Chat chat = Chat.this;
                                if (parseLong <= Long.parseLong(MemoryData.getLastMsgTs(chat, chat.chatKey))) {
                                   }
                            }
                            boolean unused3 = Chat.this.LoadingFirstTime = false;
                            MemoryData.saveLastMsgTS(messageTimestamps, Chat.this.chatKey, Chat.this);
                            Chat.this.chatAdapter.updateChatList(Chat.this.chatLists);
                            Chat.this.chattingRecyclerView.scrollToPosition(Chat.this.chatLists.size()+2);
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        ((ImageView) findViewById(R.id.sendbtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String currentTimestamp = String.valueOf(System.currentTimeMillis());
                String getTxtMessage = mesaageEditText.getText().toString();
                Chat.this.databaseReference.child("chat").child(Chat.this.chatKey).child("user_1").setValue(getUserMobile);
                Chat.this.databaseReference.child("chat").child(Chat.this.chatKey).child("user_2").setValue(getMobile);
                Chat.this.databaseReference.child("chat").child(Chat.this.chatKey).child("messages").child(currentTimestamp).child(NotificationCompat.CATEGORY_MESSAGE).setValue(getTxtMessage);
                Chat.this.databaseReference.child("chat").child(Chat.this.chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(Chat.this.getUserMobile);
                mesaageEditText.setText("");
                Chat.this.chatAdapter.updateChatList(Chat.this.chatLists);
                Chat.this.chattingRecyclerView.scrollToPosition(Chat.this.chatLists.size() + 1);
            }
        });
        ((ImageView) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Chat.this.finish();
            }
        });
    }

}
