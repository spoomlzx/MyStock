package com.spoom.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spoom.base.R;
import com.spoom.base.utils.SizeUtil;

/**
 * package com.spoom.plugin.ui
 *
 * @author spoomlan
 * @date 16/01/2018
 */

public class ImagePanel extends RelativeLayout {
    private ImageView ivIcon;
    private TextView tvText, tvUnread;
    private View vDivider;

    public ImagePanel(Context context) {
        super(context);
        init(context, null);
    }

    public ImagePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImagePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_image_panel, this);
        ivIcon = findViewById(R.id.iv_icon);
        tvText = findViewById(R.id.tv_text);
        tvUnread = findViewById(R.id.tv_unread);
        vDivider = findViewById(R.id.v_divider);

        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImagePanel);
            // 设置panel的image icon
            Drawable icon = ta.getDrawable(R.styleable.ImagePanel_image);
            if (null != icon) {
                ivIcon.setImageDrawable(icon);
            }
            // 根据imageSize属性设置imageView的尺寸等属性,默认36dp
            int imageSize = ta.getDimensionPixelSize(R.styleable.ImagePanel_imageSize, SizeUtil.dp2px(context, 36));
            LayoutParams params = new LayoutParams(imageSize, imageSize);
            params.addRule(ALIGN_PARENT_LEFT);
            params.addRule(CENTER_VERTICAL);
            params.setMarginStart(SizeUtil.dp2px(context, 14));
            ivIcon.setLayoutParams(params);
            // 设置panel 的text
            String text = ta.getString(R.styleable.ImagePanel_text);
            if (null != text) {
                tvText.setText(text);
            }
            // 设置是否显示右侧的未读条目数
            int unread = ta.getInteger(R.styleable.ImagePanel_unreadNumber, 0);
            if (0 == unread) {
                tvUnread.setVisibility(GONE);
            } else {
                tvUnread.setText(String.valueOf(unread));
                tvUnread.setVisibility(VISIBLE);
            }
            // 设置是否显示panel下方的分界线
            boolean bottomDivider = ta.getBoolean(R.styleable.ImagePanel_bottomDivider, true);
            if (bottomDivider) {
                vDivider.setVisibility(VISIBLE);
            } else {
                vDivider.setVisibility(GONE);
            }
            ta.recycle();
        }
    }

    public void setIcon(int resId) {
        ivIcon.setImageResource(resId);
    }

    public void setText(String text) {
        tvText.setText(text);
    }

    public void setUnread(int unread) {
        if (unread > 0) {
            tvUnread.setText(String.valueOf(unread));
            tvUnread.setVisibility(VISIBLE);
        } else {
            tvUnread.setVisibility(GONE);
        }
    }

    public void setDivider(boolean bottomDivider) {
        if (bottomDivider) {
            vDivider.setVisibility(VISIBLE);
        } else {
            vDivider.setVisibility(GONE);
        }
    }
}
