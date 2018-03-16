package com.spoom.xiaohei.activity.main.conversation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.R;
import com.spoom.xiaohei.util.TimeUtil;

import java.util.List;

/**
 * package com.lan.ichat.activity.main.conversation
 *
 * @author spoomlan
 * @date 17/12/2017
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private Context context;
    private List<RecentContact> conversations;
    private OnClickListener onClickListener;

    public ConversationAdapter(Context context, List<RecentContact> conversations, OnClickListener listener) {
        this.context = context;
        this.conversations = conversations;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentContact conversation = conversations.get(position);
        //Friend friend = ContactManager.getInstance().getFriend(conversation.getContactId());
        NimUserInfo friend = NIMClient.getService(UserService.class).getUserInfo(conversation.getContactId());
        Glide.with(context)
                .load(friend.getAvatar())
                .into(holder.ivAvatar);
        holder.tvName.setText(friend.getName());
        holder.tvTime.setText(TimeUtil.getTimeString(conversation.getTime()));
        holder.tvMessage.setText(conversation.getContent());
        holder.rlItem.setOnClickListener(v -> onClickListener.onItemClick(conversation));
        holder.rlItem.setOnLongClickListener(v -> onClickListener.onItemLongClick(holder.rlItem));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvTime, tvMessage;
        RelativeLayout rlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_item);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }

    public interface OnClickListener {
        void onItemClick(RecentContact conversation);

        boolean onItemLongClick(View view);
    }
}
