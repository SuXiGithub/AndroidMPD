package dialview.suxi.com.dialviewdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by suxi on 2017/6/19.
 */

public class DialViewGroup extends RelativeLayout {

    private int childWidth1;
    private int childHeight1;
    private int childWidth2;
    private int childHeight2;
    private int maxNum = 0;
    private int mProgress = 0;
    private int mDuration = 4000;
    private float progressFloat;

    public DialViewGroup(Context context) {
        super(context);
    }

    public DialViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int height = getChildAt(0).getMeasuredHeight() + getChildAt(1).getMeasuredHeight()/2;

        childWidth1 = getChildAt(0).getMeasuredWidth();
        childHeight1 = getChildAt(0).getMeasuredHeight();
        childWidth2 = getChildAt(1).getMeasuredWidth();
        childHeight2 = getChildAt(1).getMeasuredHeight();

        if(childWidth1 >= childWidth2){
            widthSize = childWidth1;
        }else{
            widthSize = childWidth2;
        }

        heightSize = height;

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        getChildAt(1).layout(getWidth()/2 - childWidth2 + childHeight2/2, getHeight() - childHeight2, getWidth()/2 + childHeight2/2, getHeight());
        ImageView imageView = (ImageView) getChildAt(1);

        if(maxNum == 0)
            return;

        final DialView progressBar = (DialView) getChildAt(0);
        progressBar.setMaxNum(maxNum);
        progressBar.setMax(300); //以300为最大，这样画进度条会平滑一些

        progressFloat = Float.parseFloat(mProgress + "");

        ValueAnimator animator = ValueAnimator.ofFloat(0, progressFloat/maxNum * 150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();

                progressBar.setProgress((int)(progress + 0.5));
            }
        });
        animator.setDuration(mDuration);
        animator.start();

        ObjectAnimator oaAnimator= ObjectAnimator.ofFloat(imageView, "rotation", 0, progressFloat/maxNum * 180);

        //如果不指定中心点的话就是按照图标自己的中心进行旋转
        imageView.setPivotX(imageView.getWidth() - imageView.getHeight()/2);//设置指定旋转中心点X坐标
        imageView.setPivotY(imageView.getHeight()/2);//设置指定旋转中心点X坐标，注意的是这个点（100,100）是想对于view的坐标，不是屏幕的左上角的0，0位置，有了这你就可以实现和补间动画一样的效果
        oaAnimator.setDuration(mDuration);
        oaAnimator.start();

    }

    /**
     * 传入最大刻度，自动分成四份
     * @param num
     */
    public void setMaxNum(int num){
        this.maxNum = num;
    }

    /**
     * @param progress 出入当前需要指向的刻度
     */
    public void setProgress(int progress){
        this.mProgress = progress;
    }

    /**
     * 动画执行时间  单位毫秒
     * @param duration
     */
    public void setDuration(int duration){
        this.mDuration = duration;
    }
}
