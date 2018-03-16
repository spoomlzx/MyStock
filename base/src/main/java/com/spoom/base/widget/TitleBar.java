package com.spoom.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.spoom.base.R;

/**
 * package com.spoom.plugin.ui
 *
 * @author spoomlan
 * @date 01/02/2018
 */

public class TitleBar extends RelativeLayout {
    private RelativeLayout rlTitleBar;
    private LinearLayout leftButton, rightTextButton;
    private ImageButton ibLeft, ibSearch, ibRight;
    private TextView tvTitle, tvRightTextButton;

    public TitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_title_bar, this);
        rlTitleBar = findViewById(R.id.rl_title_bar);
        leftButton = findViewById(R.id.ll_left_btn);
        rightTextButton = findViewById(R.id.ll_right_text_btn);
        ibLeft = findViewById(R.id.ib_left);
        ibRight = findViewById(R.id.ib_right);
        ibSearch = findViewById(R.id.ib_search);
        tvTitle = findViewById(R.id.tv_title);
        tvRightTextButton = findViewById(R.id.tv_right_btn);
        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            // 设置titleBar的background
            Drawable backColor = ta.getDrawable(R.styleable.TitleBar_backColor);
            if (null != backColor) {
                rlTitleBar.setBackground(backColor);
            }
            // 设置左边的Button，1：← 2：X
            int left = ta.getInteger(R.styleable.TitleBar_leftButton, 0);
            if (LeftButtonType.NONE == left) {
                leftButton.setVisibility(GONE);
            } else if (LeftButtonType.BACK == left) {
                leftButton.setVisibility(VISIBLE);
                ibLeft.setImageResource(R.drawable.topbar_back);
            } else if (LeftButtonType.CLOSE == left) {
                leftButton.setVisibility(VISIBLE);
                ibLeft.setImageResource(R.drawable.topbar_back);
            }
            // 设置标题
            String title = ta.getString(R.styleable.TitleBar_title);
            if (null != title) {
                tvTitle.setText(title);
            }
            // 设置是否显示搜索按钮
            boolean showSearch = ta.getBoolean(R.styleable.TitleBar_showSearch, false);
            if (showSearch) {
                ibSearch.setVisibility(VISIBLE);
            } else {
                ibSearch.setVisibility(GONE);
            }
            // 如果是文字按钮
            String rightBtnText = ta.getString(R.styleable.TitleBar_rightTextButton);
            if (null != rightBtnText) {
                rightTextButton.setVisibility(VISIBLE);
                ibRight.setVisibility(GONE);
                tvRightTextButton.setText(rightBtnText);
            }
            // 设置右边的按钮，如果设置了该按钮，则rightTextButton 属性失效
            Drawable right = ta.getDrawable(R.styleable.TitleBar_rightButton);
            if (null != right) {
                ibRight.setVisibility(VISIBLE);
                rightTextButton.setVisibility(GONE);
                ibRight.setImageDrawable(right);
            }
            ta.recycle();
        }
    }

    public void setLeftButton(int type) {
        if (LeftButtonType.NONE == type) {
            leftButton.setVisibility(GONE);
        } else if (LeftButtonType.BACK == type) {
            leftButton.setVisibility(VISIBLE);
            ibLeft.setImageResource(R.drawable.topbar_back);
        } else if (LeftButtonType.CLOSE == type) {
            leftButton.setVisibility(VISIBLE);
            ibLeft.setImageResource(R.drawable.topbar_back);
        }
    }

    public void setLeftButtonClickListener(OnClickListener listener) {
        if (leftButton.getVisibility() == VISIBLE) {
            ibLeft.setOnClickListener(listener);
        }
    }

    public void setSearchButtonClickListener(OnClickListener listener) {
        ibSearch.setVisibility(VISIBLE);
        ibSearch.setOnClickListener(listener);
    }

    public void setRightButton(int resId, OnClickListener listener) {
        ibRight.setVisibility(VISIBLE);
        rightTextButton.setVisibility(GONE);
        ibRight.setImageResource(resId);
        ibRight.setOnClickListener(listener);
    }

    public void setRightTextButton(int resId, OnClickListener listener) {
        ibRight.setVisibility(GONE);
        rightTextButton.setVisibility(VISIBLE);
        tvRightTextButton.setText(resId);
        rightTextButton.setOnClickListener(listener);
    }

    public void setRightTextButton(String text, OnClickListener listener) {
        ibRight.setVisibility(GONE);
        rightTextButton.setVisibility(VISIBLE);
        tvRightTextButton.setText(text);
        rightTextButton.setOnClickListener(listener);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setBackgroundColor(int color) {
        rlTitleBar.setBackgroundColor(color);
    }

    public interface LeftButtonType {
        int NONE = 0;
        int BACK = 1;
        int CLOSE = 2;
    }
}
