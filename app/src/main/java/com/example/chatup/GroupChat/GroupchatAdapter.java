package com.example.chatup.GroupChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.MemoryData;
import com.example.chatup.R;
import com.example.chatup.chat.ChatAdapter;
import com.example.chatup.chat.ChatList;

import java.util.List;

public class GroupchatAdapter extends RecyclerView.Adapter<GroupchatAdapter.MyViewHolder> {
    private final Context context;
    private List<GroupchatList> groupchatLists;
    private final String userMobile;

    public GroupchatAdapter(List<GroupchatList> groupchatLists, Context context2) {
        this.groupchatLists = groupchatLists;
        this.context = context2;
        this.userMobile = MemoryData.getData(context2);

    }

    @NonNull
    @Override
    public GroupchatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupchatAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groupchat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupchatAdapter.MyViewHolder holder, int position) {
        GroupchatList list2 = groupchatLists.get(position);
        if (list2.getMobile().equals(userMobile)) {
            holder.mygroupLayout.setVisibility(View.VISIBLE);
            holder.groupoppoLayout.setVisibility(View.INVISIBLE);
            holder.mygroupMessage.setText(list2.getMessage());
            holder.mygroupTime.setText(list2.getTime()+"");
            return;
        }
        holder.mygroupLayout.setVisibility(View.INVISIBLE);
        holder.groupoppoLayout.setVisibility(View.VISIBLE);
        holder.groupoppoMessage.setText(list2.getMessage());
        holder.groupoppoTime.setText(list2.getTime());
        holder.oppousername.setText("From: " +list2.getMyname());

    }

    @Override
    public int getItemCount() {
        return groupchatLists.size();
    }
    public void updategroupChatList(List<GroupchatList> groupchatLists2) {
        this.groupchatLists = groupchatLists2;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout mygroupLayout;

        public TextView mygroupMessage;

        public TextView mygroupTime;

        public TextView oppousername;

        public LinearLayout groupoppoLayout;

        public TextView groupoppoMessage;

        public TextView groupoppoTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            groupoppoLayout = itemView.findViewById(R.id.oppolayout);
            mygroupLayout = itemView.findViewById(R.id.my_layout);
            groupoppoMessage = itemView.findViewById(R.id.oppoMessage);
            mygroupMessage = itemView.findViewById(R.id.my_Message);
            groupoppoTime = itemView.findViewById(R.id.oppoMsgTime);
            mygroupTime = itemView.findViewById(R.id.my_MsgTime);
            oppousername=itemView.findViewById(R.id.oppoMSGname);
        }
    }
}
