package com.example.sheteng.ringview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheteng on 2017/5/9.
 */

public class RingView extends View {

    private static final String TAG = "RingView";
    private Paint mPaint;
    private Context mContext;
    //绘制区域宽
    private float mWidth;
    //绘制区域高
    private float mHeight;
    //外圆直径
    private float mOuterRing;
    //颜色
    private int[] colors = new int[]{0xFFF9A947, 0xFFe57646, 0xFF1E71BC, 0xFF428BFE};
    //绘制区域正方形
    private RectF mRectF;
    //各绘制角度集合
    private List<Float> mListAcr;
    //内圈大小
    private float mInnerRing;
    //每次绘制的角度
    private float mStartAcr = 1f;

    public RingView(Context context) {
        super(context, null, 0);
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mListAcr = new ArrayList<>();
        mContext = context;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingView);
        mOuterRing = typedArray.getDimension(R.styleable.RingView_outer_ring_radius, 0);
        mInnerRing = typedArray.getDimension(R.styleable.RingView_inner_ring_radius, 0);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        float left = (mWidth - mOuterRing) / 2;
        float top = (mHeight - mOuterRing) / 2;
        mRectF = new RectF(left, top, mWidth - left, mHeight - top);
    }

    public void initData(List<Float> list) {
        mListAcr.clear();
        if (list == null) {
            return;
        }
        float total = 0;
        for (Float integer : list) {
            total += integer;
        }
        if (total > 0) {
            float totalAcr = 0;
            for (int i = 0; i < list.size(); i++) {
                float acr = (list.get(i) / total) * 360;
                if (i == list.size() - 1) {
                    acr = 360 - totalAcr;
                }
                totalAcr += acr;
                mListAcr.add(acr);
            }

        }
        mStartAcr = 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int total = 0;
        mStartAcr += getInterpolation(mStartAcr / 360f);
        if (mStartAcr > 360) {
            mStartAcr = 360;
        }
        for (int i = 0; i < mListAcr.size(); i++) {
            mPaint.setColor(colors[i]);
            if (mListAcr.get(i) == 0) {
                continue;
            }
            float endAcr = mStartAcr - total;
            if (endAcr > mListAcr.get(i)){
                endAcr = mListAcr.get(i);
            }
            canvas.drawArc(mRectF, 270 + total, endAcr, true, mPaint);
            if (mStartAcr >= total + mListAcr.get(i)) {
                total += mListAcr.get(i);
                continue;
            } else {
                break;
            }
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mInnerRing / 2, mPaint);
        if (mStartAcr < 360) {
            invalidate();
        }
    }

    /**
     * 生成插值
     */
    public float getInterpolation(float input) {
        double cos = Math.cos(input * Math.PI);
        return (float) ((1f - Math.abs(cos)) * 6 + 3f);
    }
}
