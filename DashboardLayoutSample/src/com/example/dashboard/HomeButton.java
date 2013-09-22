package com.example.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by conne3ion on 9/20/13.
 */
public class HomeButton extends RelativeLayout {

    Button mButton;
    TextView mLabel;

    String mLabelText;
    Drawable mButtonBackground;

    public HomeButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public HomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public HomeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.home_dash_button, this);

        mButton = (Button) findViewById(android.R.id.button1);
        mLabel = (TextView) findViewById(android.R.id.text1);

        if(attrs != null && mLabelText == null && mButtonBackground == null) {
            TypedArray arr = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.HomeButton);

            mLabelText = arr.getString(R.styleable.HomeButton_label);
            int mButtonBackgroundResId = arr.getResourceId(R.styleable.HomeButton_buttonBackground, 0);
            if(mButtonBackgroundResId != 0) {
                mButtonBackground = context.getResources().getDrawable(mButtonBackgroundResId);
            }

            arr.recycle();
        }

        mLabel.setText(mLabelText);
        setButtonBackground(mButtonBackground, false);
    }

    public void setOnClickListener(OnClickListener listener) {
        mButton.setOnClickListener(listener);
    }

    public String getLabel() {
        return mLabelText;
    }

    public void setLabel(String text) {
        mLabelText = text;
        mLabel.setText(mLabelText);
        invalidate();
        requestLayout();
    }

    public void setLabel(int resId) {
        if(resId == 0) {
            mLabelText = null;
        } else {
            mLabelText = getContext().getString(resId);
        }

        mLabel.setText(mLabelText);
        invalidate();
        requestLayout();
    }

    public void setButtonBackground(Drawable drawable) {
        setButtonBackground(drawable, true);
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi") private void setButtonBackground(Drawable drawable, boolean invalidate) {
        mButtonBackground = drawable;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mButton.setBackground(drawable);
        } else {
            mButton.setBackgroundDrawable(drawable);
        }

        if(invalidate) {
            invalidate();
            requestLayout();
        }
    }
}
