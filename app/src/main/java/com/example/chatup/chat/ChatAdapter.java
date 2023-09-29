package com.example.chatup.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatup.MemoryData;
import com.example.chatup.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private final Context context;
    private List<ChatList> chatLists;
    private final String userMobile;

    public ChatAdapter(List<ChatList> chatLists2, Context context2) {
        this.chatLists = chatLists2;
        this.context = context2;
        this.userMobile = MemoryData.getData(context2);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatList list2 = this.chatLists.get(position);
        if (list2.getMobile().equals(this.userMobile)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.INVISIBLE);
            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getTime());
            return;
        }
        holder.myLayout.setVisibility(View.INVISIBLE);
        holder.oppoLayout.setVisibility(View.VISIBLE);
        holder.oppoMessage.setText(list2.getMessage());
        holder.oppoTime.setText(list2.getTime());
    }

    public int getItemCount() {
        return this.chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists2) {
        this.chatLists = chatLists2;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public LinearLayout myLayout;
        /* access modifiers changed from: private */
        public TextView myMessage;
        /* access modifiers changed from: private */
        public TextView myTime;
        /* access modifiers changed from: private */
        public LinearLayout oppoLayout;
        /* access modifiers changed from: private */
        public TextView oppoMessage;
        /* access modifiers changed from: private */
        public TextView oppoTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.oppoLayout = itemView.findViewById(R.id.oppolayout);
            this.myLayout = itemView.findViewById(R.id.my_layout);
            this.oppoMessage = itemView.findViewById(R.id.oppoMessage);
            this.myMessage = itemView.findViewById(R.id.my_Message);
            this.oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            this.myTime = itemView.findViewById(R.id.my_MsgTime);
        }
    }
}
