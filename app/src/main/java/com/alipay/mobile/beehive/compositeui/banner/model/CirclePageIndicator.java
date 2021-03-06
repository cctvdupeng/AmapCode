package com.alipay.mobile.beehive.compositeui.banner.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import com.alipay.mobile.beehive.R;
import com.alipay.mobile.beehive.compositeui.banner.widget.BannerView.BannerPagerAdapter;

public class CirclePageIndicator extends View implements PageIndicator {
    private boolean mCentered;
    private int mCurrentPage;
    private OnPageChangeListener mListener;
    private int mOrientation;
    private float mPageOffset;
    private final Paint mPaintFill;
    private final Paint mPaintPageFill;
    private final Paint mPaintStroke;
    private float mRadius;
    private int mScrollState;
    private boolean mSnap;
    private int mSnapPage;
    private ViewPager mViewPager;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public final /* synthetic */ Object createFromParcel(Parcel parcel) {
                return a(parcel);
            }

            public final /* synthetic */ Object[] newArray(int i) {
                return a(i);
            }

            private static SavedState a(Parcel in) {
                return new SavedState(in, 0);
            }

            private static SavedState[] a(int size) {
                return new SavedState[size];
            }
        };
        int a;

        /* synthetic */ SavedState(Parcel x0, byte b) {
            this(x0);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.a = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.a);
        }
    }

    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaintPageFill = new Paint(1);
        this.mPaintStroke = new Paint(1);
        this.mPaintFill = new Paint(1);
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0);
            this.mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered, true);
            this.mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation, 0);
            this.mPaintPageFill.setStyle(Style.FILL);
            this.mPaintPageFill.setColor(a.getColor(R.styleable.CirclePageIndicator_pageColor, -1644826));
            this.mPaintStroke.setStyle(Style.STROKE);
            this.mPaintStroke.setColor(a.getColor(R.styleable.CirclePageIndicator_strokeColor, -2236963));
            this.mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.CirclePageIndicator_strokeWidth, (float) BannerResConst.dip2px(context, 0.0d)));
            this.mPaintFill.setStyle(Style.FILL);
            this.mPaintFill.setColor(a.getColor(R.styleable.CirclePageIndicator_fillColor, -16733441));
            this.mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius, (float) BannerResConst.dip2px(context, 3.0d));
            this.mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, true);
            Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
            if (background != null) {
                setBackgroundDrawable(background);
            }
            a.recycle();
        }
    }

    public void setCentered(boolean centered) {
        this.mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return this.mCentered;
    }

    public void setPageColor(int pageColor) {
        this.mPaintPageFill.setColor(pageColor);
        invalidate();
    }

    public int getPageColor() {
        return this.mPaintPageFill.getColor();
    }

    public void setFillColor(int fillColor) {
        this.mPaintFill.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return this.mPaintFill.getColor();
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case 0:
            case 1:
                this.mOrientation = orientation;
                requestLayout();
                return;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setStrokeColor(int strokeColor) {
        this.mPaintStroke.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return this.mPaintStroke.getColor();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mPaintStroke.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return this.mPaintStroke.getStrokeWidth();
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setSnap(boolean snap) {
        this.mSnap = snap;
        invalidate();
    }

    public boolean isSnap() {
        return this.mSnap;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        float dX;
        float dY;
        float dX2;
        float dY2;
        super.onDraw(canvas);
        if (this.mViewPager != null) {
            int count = ((BannerPagerAdapter) this.mViewPager.getAdapter()).getRealCount();
            if (count == 0) {
                return;
            }
            if (this.mCurrentPage >= count) {
                setCurrentItem(count - 1);
                return;
            }
            if (this.mOrientation == 0) {
                longSize = getWidth();
                longPaddingBefore = getPaddingLeft();
                longPaddingAfter = getPaddingRight();
                shortPaddingBefore = getPaddingTop();
            } else {
                longSize = getHeight();
                longPaddingBefore = getPaddingTop();
                longPaddingAfter = getPaddingBottom();
                shortPaddingBefore = getPaddingLeft();
            }
            float threeRadius = this.mRadius * 4.0f;
            float shortOffset = ((float) shortPaddingBefore) + this.mRadius;
            float longOffset = ((float) longPaddingBefore) + this.mRadius;
            if (this.mCentered) {
                longOffset += (((float) ((longSize - longPaddingBefore) - longPaddingAfter)) / 2.0f) - ((((float) count) * threeRadius) / 2.0f);
            }
            float pageFillRadius = this.mRadius;
            if (this.mPaintStroke.getStrokeWidth() > 0.0f) {
                pageFillRadius -= this.mPaintStroke.getStrokeWidth() / 2.0f;
            }
            for (int iLoop = 0; iLoop < count; iLoop++) {
                float drawLong = longOffset + (((float) iLoop) * threeRadius);
                if (this.mOrientation == 0) {
                    dX2 = drawLong;
                    dY2 = shortOffset;
                } else {
                    dX2 = shortOffset;
                    dY2 = drawLong;
                }
                if (this.mPaintPageFill.getAlpha() > 0) {
                    canvas.drawCircle(dX2, dY2, pageFillRadius, this.mPaintPageFill);
                }
                if (pageFillRadius != this.mRadius) {
                    canvas.drawCircle(dX2, dY2, this.mRadius, this.mPaintStroke);
                }
            }
            float cx = ((float) (this.mSnap ? this.mSnapPage : this.mCurrentPage)) * threeRadius;
            if (!this.mSnap) {
                cx += this.mPageOffset * threeRadius;
            }
            if (this.mOrientation == 0) {
                dX = longOffset + cx;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = longOffset + cx;
            }
            canvas.drawCircle(dX, dY, this.mRadius, this.mPaintFill);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void setViewPager(ViewPager view) {
        if (this.mViewPager != view) {
            if (this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener(null);
            }
            if (view.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.mViewPager = view;
            this.mViewPager.setOnPageChangeListener(this);
            invalidate();
        }
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mViewPager.setCurrentItem(item);
        this.mCurrentPage = item;
        invalidate();
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    public void onPageScrollStateChanged(int state) {
        this.mScrollState = state;
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(state);
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCount = ((BannerPagerAdapter) this.mViewPager.getAdapter()).getRealCount();
        if (realCount != 0) {
            this.mCurrentPage = position % realCount;
            this.mPageOffset = positionOffset;
            invalidate();
            if (this.mListener != null) {
                this.mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }
    }

    public void onPageSelected(int position) {
        if (this.mSnap || this.mScrollState == 0) {
            int realCount = ((BannerPagerAdapter) this.mViewPager.getAdapter()).getRealCount();
            if (realCount != 0) {
                this.mCurrentPage = position % realCount;
                this.mSnapPage = position % realCount;
                invalidate();
            } else {
                return;
            }
        }
        if (this.mListener != null) {
            this.mListener.onPageSelected(position);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 0) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }

    private int measureLong(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824 || this.mViewPager == null) {
            return specSize;
        }
        int count = ((BannerPagerAdapter) this.mViewPager.getAdapter()).getRealCount();
        int result = (int) (((float) (getPaddingLeft() + getPaddingRight())) + (((float) (count * 2)) * this.mRadius) + (((float) (count - 1)) * this.mRadius) + 1.0f);
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(result, specSize);
        }
        return result;
    }

    private int measureShort(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        int result = (int) ((2.0f * this.mRadius) + ((float) getPaddingTop()) + ((float) getPaddingBottom()) + 1.0f);
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(result, specSize);
        }
        return result;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.a;
        this.mSnapPage = savedState.a;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.a = this.mCurrentPage;
        return savedState;
    }
}
