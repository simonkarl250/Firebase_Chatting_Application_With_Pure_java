package com.example.chatup.Messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.R;
import com.example.chatup.chat.Chat;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    /* access modifiers changed from: private */
    public final Context context;
    private List<MessageList> messageLists;

    public MessageAdapter(List<MessageList> messageLists2, Context context2) {
        this.messageLists = messageLists2;
        this.context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MessageList list2 = this.messageLists.get(position);
        if (list2.getProfilepic() != null) {
            Picasso.get().load(list2.getProfilepic()).into((ImageView) holder.profilepic);
        }
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());
        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.INVISIBLE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
            holder.lastMessage.setTextColor(this.context.getResources().getColor(R.color.black));
        }
        holder.rootlayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MessageAdapter.this.context, Chat.class);
                intent.putExtra("mobile", list2.getMobile());
                intent.putExtra("name", list2.getName());
                intent.putExtra("profile_pic", list2.getProfilepic());
                intent.putExtra("chat_key", list2.getChatKey());
                MessageAdapter.this.context.startActivity(intent);
            }
        });
    }

    public void updateData(List<MessageList> messageLists2) {

        this.messageLists = messageLists2;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.messageLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public final TextView lastMessage;
        /* access modifiers changed from: private */
        public final TextView name;
        /* access modifiers changed from: private */
        public final CircleImageView profilepic;
        /* access modifiers changed from: private */
        public final LinearLayout rootlayout;
        /* access modifiers changed from: private */
        public final TextView unseenMessages;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.profilepic = (CircleImageView) itemView.findViewById(R.id.profilePic);
            this.lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            this.unseenMessages = (TextView) itemView.findViewById(R.id.unseenMessages);
            this.rootlayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
