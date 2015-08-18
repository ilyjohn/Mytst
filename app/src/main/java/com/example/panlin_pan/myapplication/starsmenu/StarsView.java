package com.example.panlin_pan.myapplication.starsmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.panlin_pan.myapplication.R;

/**
 * Created by panlin_pan on 8/14/2015.
 */
public class StarsView extends ViewGroup implements View.OnClickListener {
    public OnMenuItemClickListener onMenuItemClickListener;
    Point[][] points;
    int radius;
    MenuPos pos;
    boolean isOpen = false;

    public StarsView(Context context) {
        this(context, null);
    }

    public StarsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StarsView, defStyleAttr, 0);

        //radius = a.getInt(R.styleable.StarsView_radius, 5);

        radius = (int) a.getDimension(R.styleable.StarsView_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

        int iPos = a.getInt(R.styleable.StarsView_pos, 0);
        switch (iPos) {
            case 0:
                pos = MenuPos.left_top;
                break;
            case 1:
                pos = MenuPos.right_top;
                break;
            case 2:
                pos = MenuPos.left_bottom;
                break;
            case 3:
                pos = MenuPos.right_bottom;
                break;
        }
        a.recycle();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            View btn = getChildAt(i);
            btn.setMinimumWidth(20);

            measureChild(btn, widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;

        placeMainButton();

        int n = getChildCount();
        int btnLeft = 0, btnTop = 0;
        for (int i = 0; i < n - 1; i++) {
            View btn = getChildAt(i + 1);
            btn.setVisibility(View.GONE);
            int cWidth = btn.getMeasuredWidth();
            int cHeight = btn.getMeasuredHeight();

            double angle = i * Math.PI / 2 / (n - 2);

            btnLeft = (int) (Math.sin(angle) * radius);
            btnTop = (int) (Math.cos(angle) * radius);
            if (pos == MenuPos.right_top || pos == MenuPos.right_bottom) {
                btnLeft = getMeasuredWidth() - cWidth - btnLeft;
            }
            if (pos == MenuPos.left_bottom || pos == MenuPos.right_bottom) {
                btnTop = getMeasuredHeight() - cHeight - btnTop;
            }
            btn.layout(btnLeft, btnTop, btnLeft + cWidth, btnTop + cHeight);
        }
    }

    private void placeMainButton() {
        int btnLeft = 0, btnTop = 0;
        View mainBtn = getChildAt(0);
        mainBtn.setOnClickListener(this);
        int width = mainBtn.getMeasuredWidth();
        int height = mainBtn.getMeasuredHeight();

        switch (pos) {
            case left_top:
                btnLeft = 0;
                btnTop = 0;
                break;
            case left_bottom:
                btnLeft = 0;
                btnTop = getMeasuredHeight() - height;
                break;
            case right_top:
                btnLeft = getMeasuredWidth() - width;
                btnTop = 0;
                break;
            case right_bottom:
                btnLeft = getMeasuredWidth() - width;
                btnTop = getMeasuredHeight() - height;
                break;
        }
        mainBtn.layout(btnLeft, btnTop, btnLeft + width, btnTop + height);
    }

    @Override
    public void onClick(View v) {
        rotateMainBtn(v, 0, 360, 1500);
        toggleStarsMenu(2500);
    }

    private void rotateMainBtn(View v, float start, float end, int duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        v.setAnimation(rotateAnimation);
    }

    public void toggleStarsMenu(int duration) {
        int n = getChildCount();
        for (int i = 0; i < n - 1; i++) {
            final View child = getChildAt(i + 1);
            child.setVisibility(View.VISIBLE);
            double angle = Math.PI / 2 / (n - 2);
            int left = (int) (Math.sin(angle * i) * radius);
            int top = (int) (Math.cos(angle * i) * radius);


            int xFlag = 1;
            int yFlag = 1;
            if (pos == MenuPos.left_top || pos == MenuPos.left_bottom)
                xFlag = -1;
            if (pos == MenuPos.left_top || pos == MenuPos.right_top)
                yFlag = -1;


            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = null;
            if (!isOpen) {
                translateAnimation = new TranslateAnimation(xFlag * left, 0, yFlag * top, 0);
                child.setClickable(true);
                child.setFocusable(true);
            } else {
                translateAnimation = new TranslateAnimation(0, xFlag * left, 0, yFlag * top);
                child.setClickable(false);
                child.setFocusable(false);
            }
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(duration);
            translateAnimation.setStartOffset((i * 600) / n);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isOpen) {
                        child.setVisibility(View.GONE);
                    }
                }
            });


            RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setFillAfter(true);

            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);

            child.startAnimation(animationSet);

            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.ofFloat(child, "width", 30);
            animatorSet.play(objectAnimator);
            animatorSet.start();

            final int index = i + 1;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onMenuItemClick(child, index);
                    MenuItemClick(index - 1);
                    isOpen = !isOpen;
                }
            });
        }

        isOpen = !isOpen;
    }

    private void MenuItemClick(int index) {
        int duration = 1000;
        int childCount = getChildCount();
        for (int j = 0; j < childCount - 1; j++) {
            View cv = getChildAt(j + 1);
            AnimationSet animationSet1 = new AnimationSet(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            ScaleAnimation scaleAnimation = null;
            if (j == index) {
                scaleAnimation = new ScaleAnimation(1f, 4f, 1f, 4f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            } else {
                scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }
            animationSet1.addAnimation(scaleAnimation);
            animationSet1.addAnimation(alphaAnimation);
            animationSet1.setDuration(duration);
            animationSet1.setFillAfter(true);
            cv.startAnimation(animationSet1);

        }
    }

    public enum MenuPos {
        left_top, right_top, left_bottom, right_bottom
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(View v, int pos);
    }
}
