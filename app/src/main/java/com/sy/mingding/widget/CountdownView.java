package com.sy.mingding.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * @Author: ez
 * @Time: 2019/2/14 2:13
 * @Description: 倒计时控件
 */

@SuppressWarnings("all")
public class CountdownView extends View {

    // 控件宽
    private int width;
    // 控件高
    private int height;
    //是否可以滑动改变
    boolean isTouch =true;
    // 刻度盘半径
    private int dialRadius;
    // 小时刻度高
    private float hourScaleHeight = dp2px(6);
    // 分钟刻度高
    private float minuteScaleHeight = dp2px(4);
    // 定时进度条宽
    private float arcWidth = dp2px(6);
    // 时间-分
    private int time = 0;
    // 时间-分
    private int minute = 0;
    // 时间-秒
    private int second = 0;
    // 刻度盘画笔
    private Paint dialPaint;
    // 时间画笔
    private Paint timePaint;
    // 是否移动
    private boolean isMove;
    // 当前旋转的角度
    private float rotateAngle;
    // 当前的角度
    private float currentAngle;
    // 时间改变监听
    private OnCountdownListener onCountdownListener;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 刻度盘画笔
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setColor(Color.parseColor("#94C5FF"));
        dialPaint.setStyle(Paint.Style.STROKE);
        dialPaint.setStrokeCap(Paint.Cap.ROUND);

        // 时间画笔
        timePaint = new Paint();
        timePaint.setAntiAlias(true);
        timePaint.setColor(Color.parseColor("#94C5FF"));
        timePaint.setTextSize(sp2px(33));
        timePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        width = height = Math.min(h, w);
        // 刻度盘半径
        dialRadius = (int) (width / 2 - dp2px(10));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制刻度盘
        drawDial(canvas);
        // 绘制定时进度条
        drawArc(canvas);
        // 绘制时间
        drawTime(canvas);
    }

    /**
     * 绘制刻度盘
     *
     * @param canvas 画布
     */
    private void drawDial(Canvas canvas) {
        // 绘制外层圆盘
        dialPaint.setStrokeWidth(dp2px(2));
        canvas.drawCircle(width / 2, height / 2, dialRadius, dialPaint);

        // 将坐标原点移到控件中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.save();

        // 绘制小时刻度
        for (int i = 0; i < 12; i++) {
            // 定时时间为0时正常绘制小时刻度
            // 小时刻度没有被定时进度条覆盖时正常绘制小时刻度
            if (minute == 0 || i > minute / 5) {
                canvas.drawLine(0, -dialRadius, 0, -dialRadius + hourScaleHeight, dialPaint);
            }
            // 360 / 12 = 30;
            canvas.rotate(30);
        }

        // 绘制分钟刻度
        dialPaint.setStrokeWidth(dp2px(1));
        for (int i = 0; i < 60; i++) {
            // 小时刻度位置不绘制分钟刻度
            // 分钟刻度没有被定时进度条覆盖时正常绘制分钟刻度
            if (i % 5 != 0 && i > minute) {
                canvas.drawLine(0, -dialRadius, 0, -dialRadius + minuteScaleHeight, dialPaint);
            }
            // 360 / 60 = 6;
            canvas.rotate(6);
        }
    }

    /**
     * 绘制定时进度条
     *
     * @param canvas 画布
     */
    private void drawArc(Canvas canvas) {
        if (minute > 0) {
            // 绘制起始标志
            dialPaint.setStrokeWidth(dp2px(3));

            canvas.drawLine(0, -dialRadius - hourScaleHeight, 0, -dialRadius + hourScaleHeight, dialPaint);

            // 取消直线圆角设置
            dialPaint.setStrokeCap(Paint.Cap.BUTT);

            // 绘制进度
            for (int i = 0; i <= minute * 6; i++) {
                canvas.drawLine(0, -dialRadius - arcWidth / 2, 0, -dialRadius + arcWidth / 2, dialPaint);
                // 最后一次绘制后不旋转画布
                if (i != minute * 6) {
                    canvas.rotate(1);
                }
            }

            // 绘制结束标志
            dialPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(0, -dialRadius - hourScaleHeight, 0, -dialRadius + hourScaleHeight, dialPaint);
        }
    }

    /**
     * 绘制时间
     *
     * @param canvas 画布
     */
    private void drawTime(Canvas canvas) {
        canvas.restore();
        String timeText = String.format(Locale.CHINA, "%02d", minute) + " : "+String.format(Locale.CHINA, "%02d", second);
        // 获取时间的宽高
        float timeWidth = timePaint.measureText(timeText);
        float timeHeight = Math.abs(timePaint.ascent() + timePaint.descent());
        // 居中显示
        canvas.drawText(timeText, -timeWidth / 2, timeHeight / 2, timePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isTouch){

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下的角度
                    currentAngle = calcAngle(event.getX(), event.getY());
                    calcTimeFromCurrent(currentAngle);
                    break;

                case MotionEvent.ACTION_MOVE:
                    // 标记正在移动
                    isMove = true;
                    // 移动的角度
                    float moveAngle = calcAngle(event.getX(), event.getY());
                    // 滑过的角度偏移量
                    float angleOffset = moveAngle - currentAngle;

                    // 防止越界
                    if (angleOffset < -270) {
                        angleOffset = angleOffset + 360;
                    } else if (angleOffset > 270) {
                        angleOffset = angleOffset - 360;
                    }

                    currentAngle = moveAngle;
                    // 计算时间
                    calcTimeFromOffset(angleOffset);
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    if (isMove && onCountdownListener != null) {
                        // 回调倒计时改变方法
                        onCountdownListener.countdown(minute);
                        isMove = false;
                    }
                    break;
                }
            }
        }
        return true;
    }

//



    /**
     * 以刻度盘圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *TODO:
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        // 以刻度盘圆心为坐标圆点
        float x = targetX - width / 2;
        float y = 0-(targetY - height / 2);

        Log.e("calcAngle","targetX:"+targetX+"  targetY:"+targetY);
        Log.e("calcAngle","x:"+x+"   y:"+y);
        // 滑过的弧度
        double radian=0;

        if (x != 0) {
            float tan = Math.abs(y / x);
           // Log.e("calAngle",""+tan+"    y="+y+"    x="+x);
            if (x > 0) {
                if (y >= 0) {
                    // 第一象限
                    radian = Math.atan(1/tan);
                } else {
                    // 第二象限
                    radian =+Math.PI/2+Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    // 第四象限
                    radian = 2*Math.PI - Math.atan(1/tan);
                } else {
                    // 第三象限
                    radian = Math.PI + Math.atan(1/tan);
                }
            }
        } else {
            if (y > 0) {
                // Y轴向下方向
                radian = Math.PI / 2;
            } else {
                // Y轴向上方向
                radian = Math.PI + Math.PI / 2;
            }
        }

        // 完整圆的弧度为2π，角度为360度，所以180度等于π弧度
        // 弧度 = 角度 / 180 * π
        // 角度 = 弧度 / π * 180
        return (float) (radian / Math.PI * 180);
    }

    /**
     * 计算时间
     *
     * @param angle 增加的角度
     */
    private void calcTimeFromOffset(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > 360) {
            rotateAngle = 360;
        }
        minute = (int) rotateAngle / 6;
        invalidate();
    }

    /**
     * 计算时间
     * @param angle 当前的角度
     */
    private void calcTimeFromCurrent(float angle) {
        rotateAngle = angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > 360) {
            rotateAngle = 360;
        }
        minute = (int) rotateAngle / 6;
        invalidate();
    }

    /**
     * 设置倒计时
     *
     * @param minute 分钟
     */
    public void setCountdown(int time) {
        if (minute < 0 || minute > 3600) {
            return;
        }
        this.minute = time/60;
        this.second=time%60;
        rotateAngle = minute * 6;
        invalidate();
    }


    /**
     * Sets countdown isTouch.
     *
     * @param flag 是否可以滑动改变
     */
    public void setCountdownFlag(boolean flag) {
        this.isTouch =flag;
    }

    /**
     * 设置倒计时监听
     *
     * @param onTempChangeListener 倒计时监听接口
     */
    public void setOnCountdownListener(OnCountdownListener onCountdownListener) {
        this.onCountdownListener = onCountdownListener;
    }

    /**
     * 倒计时监听接口
     */
    public interface OnCountdownListener {
        /**
         * 倒计时
         *
         * @param temp 时间
         */
        void countdown(int time);
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}