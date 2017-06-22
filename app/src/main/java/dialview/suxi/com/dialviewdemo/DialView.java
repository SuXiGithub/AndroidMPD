package dialview.suxi.com.dialviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by suxi on 2017/6/17.
 */

public class DialView extends ProgressBar {

    private int height;
    private int width;
    private RectF strokeRectF; //小扇形区域
    private Paint arcPaint;
    private Paint strokePaint;

    //渐变数组
    private int[] arcColors = new int[]{
            Color.parseColor("#b3f09738"),
            Color.parseColor("#b3DE4B30")};


    private RectF arcSectF;
    private float mCenterX;
    private float interCircleRadius;
    private float outerCircleRadius;
    private Paint linePaint;
    private float mCenterY;
    private int mLineCount = 120;
    private int dp35;
    private int dp10;
    private Paint textPaint;
    private Paint whiteStrokePaint;
    private int maxNum;
    private Rect textBound1;
    private String text;
    private Rect textBound2;
    private String text2;
    private int textWidth1;
    private int textWidth2;
    private int dp40;

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setColor(getResources().getColor(R.color.orange2));


        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 8));

        whiteStrokePaint = new Paint();
        whiteStrokePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 8));
        whiteStrokePaint.setColor(getResources().getColor(R.color.white));
        whiteStrokePaint.setStyle(Paint.Style.STROKE);
        whiteStrokePaint.setAntiAlias(true);
        whiteStrokePaint.setDither(true); //防抖


        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2.0f);
        linePaint.setColor(getResources().getColor(R.color.orange3));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(20);
        textPaint.setColor(getResources().getColor(R.color.white));

        dp10 = DensityUtil.dip2px(getContext(), 10);
        dp35 = DensityUtil.dip2px(getContext(), 35);
        dp40 = DensityUtil.dip2px(getContext(), 40);

        textBound1 = new Rect();
        text = "0";
        textPaint.getTextBounds(text, 0, text.length(), textBound1);
        textWidth1 = Math.abs(textBound1.right - textBound1.left);

        textBound2 = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthSize = width;
            heightSize = height;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = width;
        } else if (heightMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
            heightSize = widthSize/2;
            width = widthSize;
            height = heightSize;
        }
        setMeasuredDimension(widthSize, heightSize);

        //设置颜色渐变
        SweepGradient sweepGradient =
                new SweepGradient(width/2, height, arcColors, null);
        strokePaint.setShader(sweepGradient);

        //从外往里画
        outerCircleRadius = height - DensityUtil.dip2px(getContext(), 35);
        interCircleRadius = height - DensityUtil.dip2px(getContext(), 45);

        //确定渐变带位置
        strokeRectF = new RectF();
        strokeRectF.left = width / 2 - interCircleRadius + dp10;
        strokeRectF.top = height  - interCircleRadius + dp10;
        strokeRectF.right = width / 2  + interCircleRadius - dp10;
        strokeRectF.bottom = height  + interCircleRadius - dp10;

        /**
         * 确定扇形区域
         */
        arcSectF = new RectF();
        arcSectF.left = strokeRectF.left + dp10;
        arcSectF.top = strokeRectF.top + dp10;
        arcSectF.right = strokeRectF.right - dp10;
        arcSectF.bottom = strokeRectF.bottom - dp10;

        mCenterX = width/2;
        mCenterY = height - 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画渐变颜色的圆弧
        canvas.drawArc(strokeRectF, 180, 180, false, strokePaint);

        //畫小扇形
        canvas.drawArc(arcSectF, 180, 180, false, arcPaint);

        //画刻度线
        drawLine(canvas);

        //画进度
        drawProgress(canvas);

    }

    private void drawProgress(Canvas canvas) {
        canvas.drawArc(strokeRectF, 180, 360.0f * getProgress()/getMax(), false, whiteStrokePaint);
    }

    private void drawLine(Canvas canvas) {

        //画右边90度刻度线
        for (int i = 0; i < 31; i++) {


            float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
            float rotateDegrees = i * unitDegrees;
            if(i == 10){
                outerCircleRadius = height - dp35;
                canvas.drawText(maxNum*2/3 + "", mCenterX + (float) Math.sin(rotateDegrees) * outerCircleRadius, mCenterY -(float) Math.cos(rotateDegrees) * outerCircleRadius - 2, textPaint);
            }else if(i == 30){
                outerCircleRadius = height - dp35;
                canvas.drawText(maxNum + "", height + outerCircleRadius, height - 2, textPaint);

            }else {
                outerCircleRadius = height - dp40;
            }

            float startX = mCenterX + (float) Math.sin(rotateDegrees) * interCircleRadius;
            float startY = mCenterY - (float) Math.cos(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.sin(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterY - (float) Math.cos(rotateDegrees) * outerCircleRadius;

            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }

        //画左边90度刻度线
        for (int i = 90; i < mLineCount; i++) {

            float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
            float rotateDegrees = i * unitDegrees;

            if(i == 90){
                outerCircleRadius = height - dp35;
                canvas.drawText("0", height - outerCircleRadius - textWidth1, height - 2, textPaint);
            }else if(i == 110){

                //计算文字的宽度
                text2 = maxNum/3 + "";
                textPaint.getTextBounds(text2, 0, text2.length(), textBound2);
                textWidth2 = Math.abs(textBound2.right - textBound2.left);

                outerCircleRadius = height - dp35;
                canvas.drawText(maxNum/3 + "", mCenterX + (float) Math.sin(rotateDegrees) * outerCircleRadius - textWidth2,mCenterY - (float) Math.cos(rotateDegrees) * outerCircleRadius - 2, textPaint);
            }else{
                outerCircleRadius = height - dp40;
            }

            float startX = mCenterX + (float) Math.sin(rotateDegrees) * interCircleRadius;
            float startY = mCenterY - (float) Math.cos(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.sin(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterY - (float) Math.cos(rotateDegrees) * outerCircleRadius;

            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }
    }

    public void setMaxNum(int num){
        this.maxNum = num;
    }
}
