package com.example.chatup.Contacts;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
   Activity activity;
    ArrayList<ContactModel> arrayList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");


    public ContactsAdapter(Activity activity, ArrayList<ContactModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
    ContactModel model = arrayList.get(position);
    holder.tvName.setText(model.getName());
    holder.tvNumber.setText(model.getNumber());
   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {
           if (snapshot.hasChild("Groups")){
               if (snapshot.child("Groups").child(model.getGroupname()).child("members").hasChild(model.getNumber())) {
                   holder.floatingActionButton.setVisibility(View.GONE);
               }
           }


               }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {

       }
   });

    holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("Groups")){
                        if (!snapshot.child("Groups").child(model.getGroupname()).child("members").hasChild(model.getNumber())) {
                            databaseReference.child("Groups").child(model.getGroupname()).child("members").child(model.getNumber()).setValue("");
                            holder.floatingActionButton.setVisibility(View.GONE);
                            holder.floatingActionButtonRM.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    });
    holder.floatingActionButtonRM.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference.child("Groups").child(model.getGroupname()).child("members").child(model.getNumber()).removeValue();
            holder.floatingActionButtonRM.setVisibility(View.GONE);
            holder.floatingActionButton.setVisibility(View.VISIBLE);

        }
    });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        FloatingActionButton floatingActionButton,floatingActionButtonRM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            floatingActionButton= itemView.findViewById(R.id.invite);
            floatingActionButtonRM= itemView.findViewById(R.id.remove);

        }
    }
}
