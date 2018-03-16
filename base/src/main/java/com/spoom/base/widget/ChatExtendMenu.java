package com.spoom.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.spoom.base.R;
import com.spoom.base.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * package com.spoom.plugin.ui
 *
 * @author spoomlan
 * @date 25/02/2018
 */

public class ChatExtendMenu extends GridView {
    private Context context;
    private List<ChatMenuItemModel> models = new ArrayList<>();

    public ChatExtendMenu(Context context) {
        super(context);
        init(context, null);
    }

    public ChatExtendMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatExtendMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatExtendMenu);
        int rowNum = ta.getInt(R.styleable.ChatExtendMenu_rowNum, 4);
        ta.recycle();
        setNumColumns(rowNum);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setGravity(Gravity.CENTER_VERTICAL);
        setVerticalSpacing(SizeUtil.dp2px(context, 8));
    }

    public void init() {
        setAdapter(new ItemAdapter(context, models));
    }

    /**
     * register menu item
     *
     * @param name        item name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerMenuItem(String name, int drawableRes, int itemId, ChatExtendMenuItemClickListener listener) {
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
        item.image = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        models.add(item);
    }

    /**
     * register menu item
     *
     * @param nameRes     resource id of itme name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerMenuItem(int nameRes, int drawableRes, int itemId, ChatExtendMenuItemClickListener listener) {
        registerMenuItem(context.getString(nameRes), drawableRes, itemId, listener);
    }

    private class ItemAdapter extends ArrayAdapter<ChatMenuItemModel> {
        private Context context;

        ItemAdapter(@NonNull Context context, @NonNull List<ChatMenuItemModel> objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_chat_extend_menu, null);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.item = convertView.findViewById(R.id.item);
                holder.ivIcon = convertView.findViewById(R.id.iv_icon);
                holder.tvName = convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }
            ChatMenuItemModel menuItemModel = models.get(position);
            holder.ivIcon.setImageResource(menuItemModel.image);
            holder.tvName.setText(menuItemModel.name);
            holder.item.setOnClickListener((View v) -> {
                if (menuItemModel.clickListener != null) {
                    menuItemModel.clickListener.onClick(menuItemModel.id, v);
                }
            });
            return convertView;
        }
    }

    private class ViewHolder {
        LinearLayout item;
        ImageView ivIcon;
        TextView tvName;
    }

    public interface ChatExtendMenuItemClickListener {
        void onClick(int itemId, View view);
    }

    class ChatMenuItemModel {
        String name;
        int image;
        int id;
        ChatExtendMenuItemClickListener clickListener;
    }
}
