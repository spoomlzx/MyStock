package com.spoom.xiaohei.activity.main.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.promeg.pinyinhelper.Pinyin;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.spoom.xiaohei.R;

import java.util.List;

/**
 * package com.lan.ichat.activity.main.contacts
 *
 * @author spoomlan
 * @date 07/01/2018
 */

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<NimUserInfo> friends;

    public ContactsAdapter(Context ctx, List<NimUserInfo> friends) {
        this.context = ctx;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public NimUserInfo getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_contacts_list, null);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.iv_avatar);
            holder.tvHeader = convertView.findViewById(R.id.tv_header);
            holder.tvName = convertView.findViewById(R.id.tv_username);
            convertView.setTag(holder);
        }
        NimUserInfo friend = friends.get(position);
        String name = friend.getName();
        String avatar = friend.getAvatar();
        String header = String.valueOf(Pinyin.toPinyin(friend.getName().toCharArray()[0]).toUpperCase().charAt(0));
        String preHeader = getPreHeader(position);
        if (header.equals(preHeader)) {
            holder.tvHeader.setVisibility(View.GONE);
        } else {
            holder.tvHeader.setText(header);
            holder.tvHeader.setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(name);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_avatar);
        Glide.with(context)
                .load(avatar)
                .apply(options)
                .into(holder.ivAvatar);
        return convertView;
    }

    private class ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvHeader;
    }

    private String getPreHeader(int position) {
        if (position > 0) {
            NimUserInfo preUser = friends.get(position - 1);
            return String.valueOf(Pinyin.toPinyin(preUser.getName().toCharArray()[0]).toUpperCase().charAt(0));
        } else {
            return null;
        }
    }
}
