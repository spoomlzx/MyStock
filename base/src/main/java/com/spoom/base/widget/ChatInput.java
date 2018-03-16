package com.spoom.base.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.spoom.base.R;
import com.spoom.base.utils.SizeUtil;

/**
 * package com.lan.ichat.activity.chat.widget
 *
 * @author spoomlan
 * @date 23/01/2018
 */

public class ChatInput extends LinearLayout implements View.OnClickListener {
    private InputMethodManager inputMethodManager;
    private int keyboardHeight;
    private Activity activity;

    private View mContentView;

    private Button btnTextMode, btnVoiceMode;
    private LinearLayout llInputText, llInputVoice;

    private Button btnExpression, btnKeyboard;
    private Button btnMore;

    private EditText etSendText;
    private Button btnSpeak;

    private Button btnSend;

    private LinearLayout llMore, llEmoji, llExtend;
    private ChatExtendMenu chatExtendMenu;
    // 使用该控件时，实现该接口，监听ChatInput控件的事件
    private ChatInputListener chatInputListener;

    int[] itemNames;
    int[] itemIcons;

    public ChatInput(Context context) {
        super(context);
        init(context);
    }

    public ChatInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_chat_input, this);
    }

    public void initView(Activity activity, View aboveView, int[] itemNames, int[] itemIcons) {
        this.itemNames = itemNames;
        this.itemIcons = itemIcons;
        this.mContentView = aboveView;
        btnTextMode = findViewById(R.id.btn_to_voice_mode);
        llInputText = findViewById(R.id.ll_input_text);
        btnVoiceMode = findViewById(R.id.btn_to_text_mode);
        llInputVoice = findViewById(R.id.ll_input_voice);

        btnExpression = findViewById(R.id.btn_expression);
        btnKeyboard = findViewById(R.id.btn_keyboard);

        btnMore = findViewById(R.id.btn_more);
        btnSend = findViewById(R.id.btn_send);
        etSendText = findViewById(R.id.et_send_text);
        btnSpeak = findViewById(R.id.btn_speak);

        llMore = findViewById(R.id.ll_more);
        llEmoji = findViewById(R.id.ll_emoji);
        llExtend = findViewById(R.id.ll_extend);

        chatExtendMenu = findViewById(R.id.chat_extend_menu);

        initEmoji();
        initExtendMenu();
        // 设置按键监听
        setListener();

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        hideSoftInput();
    }

    private void initEmoji() {

    }

    private void initExtendMenu() {
        MyExtendMenuItemClickListener clickListener = new MyExtendMenuItemClickListener();
        for (int i = 0; i < itemNames.length; i++) {
            chatExtendMenu.registerMenuItem(itemNames[i], itemIcons[i], i, clickListener);
        }
        chatExtendMenu.init();
    }

    private void setListener() {
        // 切换到voice mode
        btnTextMode.setOnClickListener(this);
        // 切换到text mode
        btnVoiceMode.setOnClickListener(this);
        // 调出表情界面
        btnExpression.setOnClickListener(this);
        // 调出输入键盘
        btnKeyboard.setOnClickListener(this);
        // 弹出相册，拍摄，位置等等模块的选择界面
        btnMore.setOnClickListener(this);
        // 发送按钮点击
        btnSend.setOnClickListener(this);
        // 输入框中内容发生变化时，出现发送按钮
        etSendText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!btnExpression.isShown()) {
                    btnKeyboard.setVisibility(GONE);
                    btnExpression.setVisibility(VISIBLE);
                }
                if (llMore.isShown()) {
                    lockContentHeight();
                    hideEmotionLayout(true);
                    etSendText.postDelayed(this::unlockContentHeightDelayed, 200L);
                    chatInputListener.onEditTextUp();
                    return false;
                }

                if (!isSoftInputShown()) {
                    chatInputListener.onEditTextUp();
                }
            }
            return false;
        });
        etSendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(s.toString())) {
                    btnMore.setVisibility(VISIBLE);
                    btnSend.setVisibility(GONE);
                } else {
                    btnMore.setVisibility(GONE);
                    btnSend.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btnMore.setVisibility(VISIBLE);
                    btnSend.setVisibility(GONE);
                } else {
                    btnMore.setVisibility(GONE);
                    btnSend.setVisibility(VISIBLE);
                }
            }
        });

        btnSpeak.setOnTouchListener((v, event) -> chatInputListener.onPressToSpeakBtnTouch(v, event));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_to_voice_mode) {
            showVoiceRecordingView();
            chatInputListener.onEditTextUp();
            btnSend.setVisibility(GONE);
            btnMore.setVisibility(VISIBLE);
            btnKeyboard.setVisibility(GONE);
            btnExpression.setVisibility(VISIBLE);
        } else if (i == R.id.btn_to_text_mode) {
            showInputText();
            chatInputListener.onEditTextUp();
            if (!TextUtils.isEmpty(etSendText.getText().toString())) {
                btnMore.setVisibility(GONE);
                btnSend.setVisibility(VISIBLE);
            }
            showSoftInput();
        } else if (i == R.id.btn_expression) {
            showInputText();
            chatInputListener.onEditTextUp();
            btnExpression.setVisibility(GONE);
            btnKeyboard.setVisibility(VISIBLE);
            if (isSoftInputShown()) {
                lockContentHeight();
                setEmojiMode();
                showEmotionLayout();
                unlockContentHeightDelayed();
            } else {
                setEmojiMode();
                showEmotionLayout();
            }
            etSendText.requestFocus();
        } else if (i == R.id.btn_keyboard) {
            showInputText();
            chatInputListener.onEditTextUp();
            btnKeyboard.setVisibility(GONE);
            btnExpression.setVisibility(VISIBLE);
            lockContentHeight();
            hideEmotionLayout(true);
            unlockContentHeightDelayed();
        } else if (i == R.id.btn_more) {
            btnKeyboard.setVisibility(GONE);
            btnExpression.setVisibility(VISIBLE);
            showInputText();
            chatInputListener.onEditTextUp();
            if (llMore.isShown()) {
                if (llEmoji.isShown()) {
                    setExtendMode();
                } else {
                    lockContentHeight();
                    hideEmotionLayout(true);
                    unlockContentHeightDelayed();
                }
            } else {
                if (isSoftInputShown()) {
                    lockContentHeight();
                    setExtendMode();
                    showEmotionLayout();
                    unlockContentHeightDelayed();
                } else {
                    setExtendMode();
                    showEmotionLayout();
                }
            }
        } else if (i == R.id.btn_send) {
            String content = etSendText.getText().toString();
            etSendText.setText("");
            chatInputListener.onSendButtonClick(content);
        }
    }

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    private void showVoiceRecordingView() {
        llInputText.setVisibility(GONE);
        llInputVoice.setVisibility(VISIBLE);
        interceptBackPress();
        hideSoftInput();
    }

    /**
     * 如果more界面弹出，则返回键并不finish activity
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (llMore.isShown()) {
            hideEmotionLayout(false);
            if (!btnExpression.isShown()) {
                btnKeyboard.setVisibility(GONE);
                btnExpression.setVisibility(VISIBLE);
            }
            return true;
        }
        return false;
    }

    private void hideEmotionLayout(boolean showSoftInput) {
        if (llMore.isShown()) {
            llMore.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = keyboardHeight;
        }
        hideSoftInput();
        llMore.getLayoutParams().height = softInputHeight;
        llMore.setVisibility(View.VISIBLE);
    }

    private void setEmojiMode() {
        llExtend.setVisibility(GONE);
        llEmoji.setVisibility(VISIBLE);
    }

    private void setExtendMode() {
        llEmoji.setVisibility(GONE);
        llExtend.setVisibility(VISIBLE);
    }

    /**
     * 显示输入文本框，隐藏语音输入
     */
    private void showInputText() {
        llInputText.setVisibility(VISIBLE);
        llInputVoice.setVisibility(GONE);
    }

    public void showSoftInput() {
        etSendText.requestFocus();
        etSendText.post(() -> inputMethodManager.showSoftInput(etSendText, InputMethodManager.RESULT_UNCHANGED_SHOWN));
    }

    public void hideSoftInput() {
        etSendText.clearFocus();
        inputMethodManager.hideSoftInputFromWindow(etSendText.getWindowToken(), 0);
    }

    private void lockContentHeight() {
        LayoutParams params = (LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        etSendText.postDelayed(() -> ((LayoutParams) mContentView.getLayoutParams()).weight = 1.0F, 200L);
    }

    private int getSupportSoftInputHeight() {
        return SizeUtil.getSupportSoftInputHeight((Activity) getContext());
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }

    public void setChatInputListener(ChatInputListener chatInputListener) {
        this.chatInputListener = chatInputListener;
    }

    public interface ChatInputListener {
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);

        /**
         * send button clicked listener, used to send message
         *
         * @param content
         */
        void onSendButtonClick(String content);

        /**
         * after keyboard shown, move the messages to the last one
         */
        void onEditTextUp();

        void onAlbumItemClick();

        void onPhotoItemClick();

        void onCollectItemClick();

        void onContactItemClick();

        void onTransferItemClick();

        void onFileItemClick();
    }

    class MyExtendMenuItemClickListener implements ChatExtendMenu.ChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            switch (itemId) {
                case 0:
                    chatInputListener.onAlbumItemClick();
                    break;
                case 1:
                    chatInputListener.onPhotoItemClick();
                    break;
                case 2:
                    chatInputListener.onCollectItemClick();
                    break;
                case 3:
                    chatInputListener.onContactItemClick();
                    break;
                case 4:
                    chatInputListener.onTransferItemClick();
                    break;
                case 5:
                    chatInputListener.onFileItemClick();
                    break;
                default:
                    break;
            }
        }
    }
}
