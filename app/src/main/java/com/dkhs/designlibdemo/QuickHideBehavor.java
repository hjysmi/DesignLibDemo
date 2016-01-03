package com.dkhs.designlibdemo;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2016/1/3.
 */
public class QuickHideBehavor extends CoordinatorLayout.Behavior<View> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    int all_dy;
    boolean isShowing = false;
    boolean isHiding = false;

    public QuickHideBehavor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if ((dy > 0 && all_dy < 0) || (dy < 0 && all_dy > 0)) {
            all_dy = 0;
            child.animate().cancel();
        }

        all_dy += dy;

        if (all_dy > child.getHeight() && child.getVisibility() == View.VISIBLE&&!isHiding) {

            hide(child);

        } else if (all_dy < 0 && child.getVisibility() == View.GONE&&!isShowing) {

            show(child);
        }

    }

    private void show(final View child) {
        isShowing = true;
        ViewPropertyAnimator animate = child.animate();
        animate.setInterpolator(INTERPOLATOR);
        animate.setDuration(200);
        animate.translationY(0);
        animate.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                child.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isShowing = false;
                if (!isHiding) {
                    hide(child);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animate.start();
    }

    private void hide(final View child) {
        isHiding = true;
        ViewPropertyAnimator animate = child.animate();
        animate.setInterpolator(INTERPOLATOR);
        animate.setDuration(200);
        animate.translationY(child.getHeight());
        animate.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isHiding = false;
                child.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isHiding = false;
                if (!isShowing) {
                    show(child);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animate.start();
    }
}
