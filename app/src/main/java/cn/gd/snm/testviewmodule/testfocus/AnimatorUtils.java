/*
 * Copyright (C) 2016 hejunlin <hejunlin2013@gmail.com>
 * Github:https://github.com/hejunlin2013/TVSample
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.gd.snm.testviewmodule.testfocus;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


/**
 * @author Darren.liu
 * <p>
 * 动画效果。
 */
public class AnimatorUtils {

    protected View mTarget;
    protected float mScale = 1.1f;//设置放大比例
    protected AnimatorSet animatorSet;


    /**
     * Darrenadd: 放大动画。
     *
     * 使用该动画可以规避放大后获焦闪光抖动。
     */

    public static void setScaleAnimator2 (View view , boolean isScale , float scale){
        view.setPivotX(view.getWidth()/2);  // X方向中点
        view.setPivotY(view.getHeight()/2);   // Y方向中点。
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        ObjectAnimator scaleX;
        ObjectAnimator scaleY;
        if(isScale) {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
        }else{
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1f);
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1f);
        }
        animatorSet.setDuration(260);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }


}
