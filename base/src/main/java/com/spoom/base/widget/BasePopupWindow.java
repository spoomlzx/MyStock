package com.spoom.base.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * package com.spoom.plugin.ui
 *
 * @author spoomlan
 * @date 10/02/2018
 */

public abstract class BasePopupWindow {
    protected Context context;
    protected View contentView;
    protected PopupWindow instance;

    public BasePopupWindow(Context context, int layoutResId, int width, int height) {
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(layoutResId, null, false);
        initView();
        initEvent();

        instance = new PopupWindow(contentView, width, height, true);
        initWindow();
    }

    protected void initWindow() {
        instance.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instance.setOutsideTouchable(true);
        instance.setTouchable(true);
    }

    /**
     * init the content view
     */
    protected abstract void initView();

    /**
     * init the event,such as set listener
     */
    protected abstract void initEvent();

    public View getContentView() {
        return contentView;
    }

    public PopupWindow getPopupWindow() {
        return instance;
    }

    public void showOfAnchor(View anchor, LayoutGravity layoutGravity, int xRetouch, int yRetouch) {
        int[] offset = layoutGravity.getOffset(anchor, instance);
        instance.showAsDropDown(anchor, offset[0] + xRetouch, offset[1] + yRetouch);
    }

    public void showAsDropdown(View anchor, int xOffset, int yOffset) {
        instance.showAsDropDown(anchor, xOffset, yOffset);
    }

    public void showAtLocation(View parent, int gravity, int xOffset, int yOffset) {
        instance.showAtLocation(parent, gravity, xOffset, yOffset);
    }

    public void dismiss() {
        instance.dismiss();
    }

    public static class LayoutGravity {
        private int layoutGravity;
        // waring, don't change the order of these constants!
        public static final int ALIGN_LEFT = 0x1;
        public static final int ALIGN_ABOVE = 0x2;
        public static final int ALIGN_RIGHT = 0x4;
        public static final int ALIGN_BOTTOM = 0x8;
        public static final int TO_LEFT = 0x10;
        public static final int TO_ABOVE = 0x20;
        public static final int TO_RIGHT = 0x40;
        public static final int TO_BOTTOM = 0x80;
        public static final int CENTER_HORI = 0x100;
        public static final int CENTER_VERT = 0x200;

        public LayoutGravity(int gravity) {
            layoutGravity = gravity;
        }

        public int getLayoutGravity() {
            return layoutGravity;
        }

        public void setLayoutGravity(int gravity) {
            layoutGravity = gravity;
        }

        public void setHoriGravity(int gravity) {
            layoutGravity &= (0x2 + 0x8 + 0x20 + 0x80 + 0x200);
            layoutGravity |= gravity;
        }

        public void setVertGravity(int gravity) {
            layoutGravity &= (0x1 + 0x4 + 0x10 + 0x40 + 0x100);
            layoutGravity |= gravity;
        }

        public boolean isParamFit(int param) {
            return (layoutGravity & param) > 0;
        }

        public int getHoriParam() {
            for (int i = 0x1; i <= 0x100; i = i << 2) {
                if (isParamFit(i)) {
                    return i;
                }
            }
            return ALIGN_LEFT;
        }

        public int getVertParam() {
            for (int i = 0x2; i <= 0x200; i = i << 2) {
                if (isParamFit(i)) {
                    return i;
                }
            }
            return TO_BOTTOM;
        }

        public int[] getOffset(View anchor, PopupWindow window) {
            int anchWidth = anchor.getWidth();
            int anchHeight = anchor.getHeight();

            int winWidth = window.getWidth();
            int winHeight = window.getHeight();
            View view = window.getContentView();
            if (winWidth <= 0) {
                winWidth = view.getWidth();
            }
            if (winHeight <= 0) {
                winHeight = view.getHeight();
            }

            int xoff = 0;
            int yoff = 0;

            switch (getHoriParam()) {
                case ALIGN_LEFT:
                    xoff = 0;
                    break;
                case ALIGN_RIGHT:
                    xoff = anchWidth - winWidth;
                    break;
                case TO_LEFT:
                    xoff = -winWidth;
                    break;
                case TO_RIGHT:
                    xoff = anchWidth;
                    break;
                case CENTER_HORI:
                    xoff = (anchWidth - winWidth) / 2;
                    break;
                default:
                    break;
            }
            switch (getVertParam()) {
                case ALIGN_ABOVE:
                    yoff = -anchHeight;
                    break;
                case ALIGN_BOTTOM:
                    yoff = -winHeight;
                    break;
                case TO_ABOVE:
                    yoff = -anchHeight - winHeight;
                    break;
                case TO_BOTTOM:
                    yoff = 0;
                    break;
                case CENTER_VERT:
                    yoff = (-winHeight - anchHeight) / 2;
                    break;
                default:
                    break;
            }
            return new int[]{xoff, yoff};
        }
    }
}