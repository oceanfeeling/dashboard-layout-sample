package com.example.dashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class DashboardLayout extends ViewGroup {

    public interface Callbacks {
        void onLayoutComplete(int renderedIcons);
    }

    public enum Mode {
        SEQUENTIAL,
        SEQUENTIAL_HALF_OFFSET,
    }
	
	private int mMaxChildWidth = 0;
    private int mMaxChildHeight = 0;
    private int mPage;
    private Mode mMode = Mode.SEQUENTIAL_HALF_OFFSET;

    private Callbacks mCallbacks = sCallbacks;
    private int mRenderedIcons;

    private static Callbacks sCallbacks = new Callbacks() {
        @Override
        public void onLayoutComplete(int renderedIcons) {
        }
    };

    public DashboardLayout(Context context) {
        super(context);
    }

    public DashboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

	public DashboardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @SuppressWarnings("unused")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxChildWidth = 0;
        mMaxChildHeight = 0;

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
            mMaxChildHeight = Math.max(mMaxChildHeight, child.getMeasuredHeight());
        }

        int w = resolveSize(mMaxChildWidth, widthMeasureSpec);
        int h = resolveSize(mMaxChildHeight, heightMeasureSpec);

        Log.i("DashboardLayout", "w:" + w + ", h:" + h);

        setMeasuredDimension(w, h);
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
                invalidate();
            }
        });
    }

    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(mMode == Mode.SEQUENTIAL) {
            onLayoutSequential(changed, l, t, r, b);
        } else if(mMode == Mode.SEQUENTIAL_HALF_OFFSET) {
            onLayoutSequentialHalfOffset(changed, l, t, r, b);
        }

        mCallbacks.onLayoutComplete(mRenderedIcons);
	}

    public void setCallbacks(Callbacks callbacks) {
        if(callbacks == null) {
            mCallbacks = sCallbacks;
        } else {
            mCallbacks = callbacks;
        }
    }
	
	public void setPage(int page) {
		this.mPage = page;
		requestLayout();
	}

    private void onLayoutSequential(boolean changed, int l, int t, int r, int b) {
        // get the measured width and height
        int width = r - l;
        int height = b - t;

        // the final horizontal padding between icons
        int horzSpacing = 0;
        // the final vertical padding between icons
        int vertSpacing = 0;

        // the minimum required padding between icons
        int minHorzPadding = (int) (mMaxChildWidth);
        int minVertPadding = (int) (mMaxChildHeight);

        // horizontal resolution
        int cols = 1;
        while(true) {
            int spacing = (width - cols * mMaxChildWidth) / (cols + 1);
            if(spacing < minHorzPadding) {
                break;
            } else {
                cols++;
            }
        }

        // vertical resolution
        int rows = 1;
        while(true) {
            int spacing = (height - (rows * mMaxChildHeight)) / (rows + 1);
            if(spacing < minVertPadding) {
                break;
            } else {
                rows++;
            }
        }

        // calculate again spacings
        horzSpacing = (width - (cols * mMaxChildWidth)) / (cols + 1);
        vertSpacing = (height - (rows * mMaxChildHeight)) / (rows + 1);

        int maxIcons = rows * cols; // total icons we can render is
        mRenderedIcons = maxIcons;
        int startHorzOffset = horzSpacing; //(int) horzSpacing / 2;
        int startVertOffset = vertSpacing; //(int) vertSpacing / 2;
        int index = 0;
        int currentRow;
        int currentCol;
        int left, top, right, bottom;
        int count = getChildCount();

        for(int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() == GONE) {
                continue;
            }

            currentRow = index / cols;
            currentCol = index % cols;

            left = startHorzOffset + ((mMaxChildWidth + horzSpacing) * currentCol);
            top = startVertOffset + ((mMaxChildWidth + vertSpacing) * currentRow);
            right = left + mMaxChildWidth;
            bottom = top + mMaxChildHeight;

            child.layout(left, top, right, bottom);

            index++;

            if(index >= maxIcons) {
                break;
            }
        }
    }

    private void onLayoutSequentialHalfOffset(boolean changed, int l, int t, int r, int b) {
        // get the measured width and height
        int paddingW = getPaddingLeft() + getPaddingRight();
        int paddingH = getPaddingTop() + getPaddingBottom();
        int width = r - l - paddingW;
        int height = b - t - paddingH;

        // the final horizontal padding between icons
        int horzSpacing = 0;
        // the final vertical padding between icons
        int vertSpacing = 0;

        // the minimum required padding between icons
        int minHorzPadding = (int) (mMaxChildWidth);
        int minVertPadding = (int) (mMaxChildHeight);

        // horizontal resolution
        int cols = 1;
        while(true) {
            int spacing = (width - cols * mMaxChildWidth) / cols;
            if(spacing < minHorzPadding) {
                break;
            } else {
                cols++;
            }
        }

        // vertical resolution
        int rows = 1;
        while(true) {
            int spacing = (height - (rows * mMaxChildHeight)) / rows;
            if(spacing < minVertPadding) {
                break;
            } else {
                rows++;
            }
        }

        // calculate again spacings
        horzSpacing = (width - (cols * mMaxChildWidth)) / cols;
        vertSpacing = (height - (rows * mMaxChildHeight)) / rows;

        int maxIcons = rows * cols; // total icons we can render is
        mRenderedIcons = maxIcons;
        int startHorzOffset = (int) horzSpacing / 2;
        int startVertOffset = (int) vertSpacing / 2;
        int index = 0;
        int startIndex = maxIcons * mPage;
        int endIndex = startIndex + maxIcons;
        int currentRow;
        int currentCol;
        int left, top, right, bottom;
        int count = getChildCount();

        for(int i = 0; i < count; i++) {
            if(!(i >= startIndex && i <= endIndex)) {
                continue;
            }

            View child = getChildAt(i);
            if(child.getVisibility() == GONE) {
                continue;
            }

            currentRow = index / cols;
            currentCol = index % cols;

            left = startHorzOffset + ((mMaxChildWidth + horzSpacing) * currentCol) + getPaddingLeft();
            top = startVertOffset + ((mMaxChildWidth + vertSpacing) * currentRow) + getPaddingTop();
            right = left + mMaxChildWidth;
            bottom = top + mMaxChildHeight;
            Log.i("DashboardLayout", "l:" + left + ", t:" + top + "r:" + right + "b:" + bottom);

            child.layout(left, top, right, bottom);

            index++;

            if(index >= maxIcons) {
                break;
            }
        }
    }
}