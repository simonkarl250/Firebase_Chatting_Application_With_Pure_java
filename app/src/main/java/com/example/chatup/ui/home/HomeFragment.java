package com.example.chatup.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.Contacts.ContactModel;
import com.example.chatup.Contacts.Contacts;
import com.example.chatup.Contacts.ContactsAdapter;
import com.example.chatup.MemoryData;
import com.example.chatup.Messages.MessageAdapter;
import com.example.chatup.Messages.MessageList;
import com.example.chatup.R;
import com.example.chatup.databinding.FragmentHomeBinding;
import com.example.chatup.login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public final List<MessageList> messageLists = new ArrayList<>();
    public String chatkey;
    private static String lastMessage ="";
    MessageAdapter messageAdapter;
    public String mobile;
    private static int unseenMessages=0;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");
    private String email;
    private String name;
     boolean dataset =false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView messagesRecycler = binding.messageRecyclerview;
        final CircleImageView userProfilePic = binding.userprofilePic;
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(root.getContext(), userProfilePic);
                popup.getMenuInflater().inflate(R.menu.dropdown, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        Toast.makeText(root.getContext(), "clicked"+menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if (menuItem.getTitle().toString().equals("Log out")){
                            MemoryData.cleardata(root.getContext(), chatkey);

                            Intent intent = new Intent(root.getContext(), login.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        Intent intent = requireActivity().getIntent();
        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        name = intent.getStringExtra("name");
        messagesRecycler.setHasFixedSize(true);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        messageAdapter = new MessageAdapter(this.messageLists, root.getContext());
        messagesRecycler.setAdapter(messageAdapter);
        final ProgressDialog progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading....");
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                String profilepicurl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);
                if (profilepicurl != null) {
                    Picasso.get().load(profilepicurl).into(userProfilePic);
                }
                progressDialog.dismiss();
            }

            public void onCancelled(DatabaseError error) {
                progressDialog.dismiss();
            }
        });








        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                messageLists.clear();
                unseenMessages = 0;
                lastMessage="";
                chatkey = "";
                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {

                    final String getMobile = dataSnapshot.getKey();
                    if (!getMobile.equals(mobile)) {
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                if (((int) snapshot.getChildrenCount()) > 0) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        String getKey = dataSnapshot1.getKey();
                                        chatkey = getKey;

                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if ((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))) {
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());

                                                    long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTs(root.getContext(), getKey));
                                                    lastMessage = chatDataSnapshot.child(NotificationCompat.CATEGORY_MESSAGE).getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                   }

//                                if (!dataset){
//                                    dataset=true;
////                                   messageLists.clear();
//                                    MessageList messageList;
//                                    messageList = new MessageList(dataSnapshot.child("name").getValue(String.class), getMobile, lastMessage, dataSnapshot.child("profile_pic").getValue(String.class), unseenMessages, chatkey);
//                                    messageLists.add(messageList);
//                                    messageAdapter.updateData(messageLists);
//                                }
                            }

                            public void onCancelled(DatabaseError error) {
                            }
                        });



//                        MessageList messageList;
//                        messageList = new MessageList(dataSnapshot.child("name").getValue(String.class), getMobile, "Message", dataSnapshot.child("profile_pic").getValue(String.class), 2, chatkey);
//                        messageLists.add(messageList);
//                        messageAdapter.updateData(messageLists);

                        if (ContextCompat.checkSelfPermission(root.getContext(), Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions((Activity) root.getContext(),new String[]{Manifest.permission.READ_CONTACTS},100);
                        }else{
                            getContantList(dataSnapshot.child("name").getValue(String.class), getMobile, "Message", dataSnapshot.child("profile_pic").getValue(String.class), 2, chatkey);
                        }



                    }
                }

            }

            public void onCancelled(DatabaseError error) {
            }
        });

        return root;
    }
    private void getContantList(String name,String phone,String lmessage,String profilephoto,int unseens,String chatk) {
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor = binding.getRoot().getContext().getContentResolver().query(uri,null,null,null,sort);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range")
                String phonename = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?";
                Cursor phoneCursor= binding.getRoot().getContext().getContentResolver().query(uriPhone,null,selection,new String[]{id},null);
                if (phoneCursor.moveToNext()){
                    @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (number.equals(phone)){
                        MessageList messageList;
                        messageList = new MessageList(phonename,phone, lmessage, profilephoto, unseens, chatk);
                        messageLists.add(messageList);
                        messageAdapter.updateData(messageLists);
                    }

                    phoneCursor.close();

                }

            }
            cursor.close();
        }



    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}