package com.example.yaoyan.customprocessbarrxjava;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by MAC on 2017/7/25.
 */

public class CustomProcess extends View {

    //设置bitMap的大小
    private static int bitMapWidth;
    private static  int bitMapHeight;

    //设置整个的大小
    private static int widthProcess;
    private static int heightProcess;

    private String text;
    private Resources res;
    private int currentProcess;
    private Paint paint;
    private Bitmap bm;

    private static int  Radius;

    private static int number1;
    private static int number2;
    private int textWidth;
    private int textHeight;
    private Rect bmRect;

    private boolean flag = false;

    public int getWidthProcess() {
        return widthProcess;
    }

    public void setWidthProcess(int widthProcess) {
        this.widthProcess = widthProcess;
    }

    public int getHeightProcess() {
        return heightProcess;
    }

    public static void setHeightProcess(int hp) {
        heightProcess = hp;
    }

    public int getRadius() {
        return Radius;
    }

    public int getBitMapWidth() {
        return bitMapWidth;
    }

    public static void setBitMapWidth(int bw) {
        bitMapWidth = bw;
    }

    public int getBitMapHeight() {
        return bitMapHeight;
    }

    public static void setBitMapHeight(int bh) {
        bitMapHeight = bh;
    }

    public static void  setRadius(int radius) {
        Radius = radius;
    }

    public int getNumber1() {
        return number1;
    }

    public static void setNumber1(int n1) {
        number1 = n1;
    }

    public int getNumber2() {
        return number2;
    }

    public static  void setNumber2(int n2) {
        number2 = n2;
    }

    public CustomProcess(Context context) {
        super(context);
        init();
    }

    public CustomProcess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CustomProcess(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }


    public int getCurrentProcess() {
        return currentProcess;
    }


    @BindingAdapter({"radius", "number1","number2","bitMapWidth","bitMapHeight","heightProcess"})
    public static  void loadImage(CustomProcess view, Integer radius,  Integer number1,Integer number2,Integer bitMapWidth,Integer bitMapHeight,Integer heightProcess) {
        setRadius(radius);
        setNumber1(number1);
        setNumber2(number2);
        setBitMapWidth(bitMapWidth);
        setBitMapHeight(bitMapHeight);
        setHeightProcess(bitMapHeight);
        setHeightProcess(heightProcess);
    }

    public void setCurrentProcess(int currentProcess) {
        this.currentProcess = currentProcess;
    }

    Action1<Void> init = new Action1<Void>() {
        @Override
        public void call(Void v) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            res = getResources();
            paint.setStyle(Paint.Style.STROKE);
            initBitmap();
        }

    };

    private void init() {
        observable.subscribe(init);
    }

    Observable observable = Observable.create(new Observable.OnSubscribe<Void>() {
        @Override
        public void call(Subscriber<? super Void> subscriber) {
            Void v = null;
            subscriber.onNext(v);
        }
    });


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            System.out.println("CustomProcess.onMeasure  EXACTLY");
            System.out.println("widthSize = " + widthSize);
            if (getWidthProcess() > widthSize) {
                System.out.println("getWidthProcess() = " + getWidthProcess());
                setWidthProcess(widthSize);
            }
            if (getWidthProcess() == 0) {
                setWidthProcess(widthSize);
            }

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            System.out.println("CustomProcess.onMeasure  AT_MOST");
            if (flag == false) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST);
            } else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(bmRect.bottom, MeasureSpec.AT_MOST);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(final Canvas canvas) {


        Observable.create(new Observable.OnSubscribe<Canvas>() {
            @Override
            public void call(Subscriber<? super Canvas> subscriber) {
                Void v = null;
                subscriber.onNext(canvas);
            }
        }).subscribe(new Action1<Canvas>() {
            @Override
            public void call(Canvas canvas) {
                //底层进度条
                bottomRect(canvas);
                //设置最上层的圆角矩形
                overRect(canvas);
                //设置BitMap
                Rect bmRect = drawBitMap(canvas);
                //设置文字
                textRect(canvas, bmRect);
                System.out.println("CustomProcess.onDraw");
                requestLayout();
                flag = true;
            }
        });


    }

    private void bottomRect(Canvas canvas) {
        RectF r1 = new RectF();

        float proportion = (float) (getNumber1() * 1.0 / getNumber2());
        System.out.println("proportion = " + proportion);

        setCurrentProcess((int) (proportion * getWidthProcess()));

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.backColor));

        r1.right = getWidthProcess();
        r1.bottom = getHeightProcess();
        canvas.drawRoundRect(r1, getRadius(), getRadius(), paint);
    }


    private void textRect(Canvas canvas, Rect bmRect) {
        text = getNumber1() + "/" + getNumber2();
        int textsize = (int) getResources().getDimension(R.dimen.sp_12);
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextSize(textsize);


        Rect mRound = new Rect();
        paint.getTextBounds(text, 0, text.length(), mRound);
        textWidth = mRound.width();
        textHeight = mRound.height();

        int dx = bmRect.right - bmRect.left;
        int dy = bmRect.bottom - bmRect.top;

        canvas.drawText(text, bmRect.left + (dx - textWidth) / 2, bmRect.bottom - (dy - textHeight) / 2 + 10, paint);

    }

    private Rect drawBitMap(Canvas canvas) {
        bmRect = new Rect();
        bmRect.left = currentProcess - getBitMapWidth() / 2;
        bmRect.top = getHeightProcess();
        bmRect.right = currentProcess + getBitMapWidth() / 2;
        bmRect.bottom = getHeightProcess() + getBitMapHeight();
        canvas.drawBitmap(bm, null, bmRect, null);
        return bmRect;
    }


    void overRect(Canvas canvas) {
        Rect r2 = new Rect();
        paint.setStyle(Paint.Style.FILL);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#ffb257"),
                Color.parseColor("#ff8c57")});
        gd.setCornerRadius(getRadius());

        r2.right = currentProcess;
        r2.bottom = getHeightProcess();
        gd.setBounds(r2);
        gd.draw(canvas);
    }

    private void initBitmap() {
        bm = BitmapFactory.decodeResource(res, R.mipmap.jifenkuang);
    }
}
