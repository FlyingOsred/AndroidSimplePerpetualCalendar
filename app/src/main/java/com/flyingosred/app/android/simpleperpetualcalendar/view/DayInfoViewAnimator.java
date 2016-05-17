package com.flyingosred.app.android.simpleperpetualcalendar.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import static com.flyingosred.app.android.simpleperpetualcalendar.util.Utils.LOG_TAG;

public class DayInfoViewAnimator {

    private final View mExpendView;

    private final View mRootView;

    private Animator mCurrentAnimator = null;

    private int mShortAnimationDuration;

    private CountDownTimer mCountDownTimer = null;

    private Rect mFinalBounds = null;

    public DayInfoViewAnimator(Context context, View rootView, View expendView) {
        mRootView = rootView;
        mExpendView = expendView;
        mShortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public void start(View view) {
        stop();

        if (mFinalBounds == null) {
            mFinalBounds = new Rect();
            mExpendView.getGlobalVisibleRect(mFinalBounds);
        }

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect(mFinalBounds);
        final Rect parentBounds = new Rect();
        final Point globalOffset = new Point();

        view.getGlobalVisibleRect(startBounds);
        mRootView.getGlobalVisibleRect(parentBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        Log.d(LOG_TAG, "DayInfoViewAnimator Before calculation startBounds is " + startBounds
                + " finalBounds is " + finalBounds + " parentBounds is " + parentBounds
                + " globalOffset is " + globalOffset);

        if (startBounds.left - finalBounds.width() >= 0
                && startBounds.top - finalBounds.height() >= 0) {
            finalBounds.offsetTo(startBounds.left - finalBounds.width(),
                    startBounds.top - finalBounds.height());
            Log.d(LOG_TAG, "DayInfoViewAnimator Left top corner");
        } else if (startBounds.left - finalBounds.width() >= 0
                && startBounds.bottom + finalBounds.height() <= parentBounds.height()) {
            finalBounds.offsetTo(startBounds.left - finalBounds.width(), startBounds.bottom);
            Log.d(LOG_TAG, "DayInfoViewAnimator Left bottom corner");
        } else if (startBounds.right + finalBounds.width() <= parentBounds.width()
                && startBounds.bottom + finalBounds.height() <= parentBounds.height()) {
            finalBounds.offsetTo(startBounds.right, startBounds.bottom);
            Log.d(LOG_TAG, "DayInfoViewAnimator Right bottom corner");
        } else {
            finalBounds.offsetTo(startBounds.right, startBounds.top - finalBounds.height());
            Log.d(LOG_TAG, "DayInfoViewAnimator Right top corner");
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        Log.d(LOG_TAG, "DayInfoViewAnimator After calculation startBounds is " + startBounds
                + " finalBounds is " + finalBounds
                + " globalOffset is " + globalOffset + " startScale is " + startScale);

        mExpendView.setVisibility(View.VISIBLE);
        mExpendView.setPivotX(0f);
        mExpendView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mExpendView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(mExpendView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(mExpendView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mExpendView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        mExpendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide(startBounds, startScaleFinal);
            }
        });

        mCountDownTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                hide(startBounds, startScaleFinal);
            }
        };

        mCountDownTimer.start();
    }

    public void stop() {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void hide(Rect startBounds, float startScaleFinal) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        Log.d(LOG_TAG, "DayInfoViewAnimator hide startBounds is " + startBounds
                + " startScaleFinal is " + startScaleFinal);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mExpendView, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(mExpendView, View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(mExpendView, View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(mExpendView, View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpendView.setVisibility(View.INVISIBLE);
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mExpendView.setVisibility(View.INVISIBLE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }
}
