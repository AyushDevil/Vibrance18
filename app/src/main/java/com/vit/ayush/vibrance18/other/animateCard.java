package com.vit.ayush.vibrance18.other;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;


public class animateCard {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesdown)
    {
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(holder.itemView,"translationY",goesdown==true?200:0,0);
        objectAnimator.setDuration(500);

        animatorSet.playSequentially(objectAnimator);
        animatorSet.start();
    }
}

