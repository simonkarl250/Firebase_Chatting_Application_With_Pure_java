package com.example.chatup.groupMessages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.GroupChat.Chatgroup;

import com.example.chatup.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyGroupViewHolder> {

    public final Context context;
    private List<GroupList> groupLists;

    public GroupsAdapter(Context context2, List<GroupList> groupLists2) {
        this.context = context2;
        this.groupLists = groupLists2;
    }

    public GroupsAdapter.MyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyGroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(MyGroupViewHolder holder, int position) {
        final GroupList gList2 = groupLists.get(position);
        if (gList2.getGroupprofilepic() != null) {
            Picasso.get().load(gList2.getGroupprofilepic()).into((ImageView) holder.Groupprofilepic);

        }
        holder.Groupname.setText(gList2.getGroupname());
        holder.GrouplastMessage.setText(gList2.getGrouplastMessage());
        if (gList2.getGroupunseenMessages() == 0) {
            holder.GroupunseenMessages.setVisibility(View.GONE);
            holder.GrouplastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.GroupunseenMessages.setVisibility(View.VISIBLE);
            holder.GroupunseenMessages.setText(gList2.getGroupunseenMessages());
            holder.GrouplastMessage.setTextColor(this.context.getResources().getColor(R.color.black));
        }
        holder.grouprootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsAdapter.this.context, Chatgroup.class);
                intent.putExtra("mobile", gList2.getMobile());
                intent.putExtra("name", gList2.getGroupname());
                intent.putExtra("profile_pic", gList2.getGroupprofilepic());
                intent.putExtra("myname",gList2.getMyname());
                GroupsAdapter.this.context.startActivity(intent);
            }
        });

    }
    public  void updateGroupData(List<GroupList> groupLists2) {
        groupLists = groupLists2;

    }
    @Override
    public int getItemCount() {
        return groupLists.size();
    }

    static class MyGroupViewHolder extends RecyclerView.ViewHolder {

        public final TextView GrouplastMessage;

        public final TextView Groupname;

        public final CircleImageView Groupprofilepic;

        public final LinearLayout grouprootlayout;

        public final TextView GroupunseenMessages;

        public MyGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            GrouplastMessage = itemView.findViewById(R.id.grouplastMessage);
            Groupname = itemView.findViewById(R.id.groupname);
            Groupprofilepic =itemView.findViewById(R.id.groupprofilePic);
            grouprootlayout= (LinearLayout) itemView.findViewById(R.id.groupsrootLayout);
            GroupunseenMessages= itemView.findViewById(R.id.groupunseenMessages);

        }
    }
}
