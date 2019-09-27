package net.findbyte.dnloadingview.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import net.findbyte.dnloadingview.R

object AnimationUtils {



    fun applyAnimation(
        context: Context,
        view: View?,
        animationResource: Int,
        animationOffsetMilisec: Int
    ) {
        val anim = android.view.animation.AnimationUtils.loadAnimation(context, animationResource)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.startOffset = animationOffsetMilisec.toLong()
        if (view != null) {
            view.animation = anim
            view.startAnimation(anim)
        }
    }


    fun applyAnimationRepeatless(
        context: Context,
        view: View?,
        animationResource: Int,
        animationOffsetMilisec: Int
    ) {
        val anim = android.view.animation.AnimationUtils.loadAnimation(context, animationResource)
        anim.startOffset = animationOffsetMilisec.toLong()
//        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                if (view != null) {
                    view.animation = anim
                    view.startAnimation(anim)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        if (view != null) {
            view.animation = anim
            view.startAnimation(anim)
        }
    }

    fun applyAnimationWithRunnable(
        context: Context,
        view: View?,
        animationResource: Int,
        animationOffsetMilisec: Int,
        toRunAfterAnimation: Runnable?
    ) {
        val anim = android.view.animation.AnimationUtils.loadAnimation(context, animationResource)
        anim.startOffset = animationOffsetMilisec.toLong()
//        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                toRunAfterAnimation?.run()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        if (view != null) {
            view.animation = anim
            view.startAnimation(anim)
        }
    }


    fun applyAnimationWithFinalHide(
        context: Context,
        view: View?,
        animationResource: Int,
        animationOffsetMilisec: Int
    ) {
        val anim = android.view.animation.AnimationUtils.loadAnimation(context, animationResource)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.startOffset = animationOffsetMilisec.toLong()
        if (view != null) {
            view.animation = anim
            view.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    view.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
        }
    }

    fun applyAnimationWithFinalShow(
        context: Context,
        view: View,
        animationResource: Int,
        animationOffsetMilisec: Int
    ) {
        view.visibility = View.VISIBLE
        val anim = android.view.animation.AnimationUtils.loadAnimation(context, animationResource)
        anim.interpolator = LinearInterpolator()
        anim.startOffset = animationOffsetMilisec.toLong()
        if (view != null) {
            view.animation = anim
            view.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
        }
    }


    fun applyAnimationSeed(
        context: Context,
        view: View?,
        paddingFrom: Int,
        paddingTo: Int,
        viewWidth: Int,
        viewHeight: Int,
        duration: Int,
        animationOffsetMilisec: Int
    ) {
        view?.let {
            val anim = SeedAnimation(view, paddingFrom, paddingTo, viewWidth, viewHeight)
            anim.startOffset = animationOffsetMilisec.toLong()
            anim.duration = duration.toLong()
            anim.interpolator = OvershootInterpolator()
            if (view != null) {
                view.animation = anim
                view.startAnimation(anim)
            }
        }
    }


    internal class SeedAnimation(
        private val view: View,
        private val mPaddingFrom: Int,
        private val mPaddingTo: Int,
        private val mViewWidth: Int,
        private val mViewHeight: Int
    ) :
        Animation() {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {

            val params = RelativeLayout.LayoutParams(
                mViewWidth + ((mPaddingTo - mPaddingFrom) * interpolatedTime + 0.5).toInt(),
                mViewHeight + ((mPaddingTo - mPaddingFrom) * interpolatedTime + 0.5).toInt()
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            view.setPadding(0, ((mPaddingTo - mPaddingFrom) * interpolatedTime + 0.5).toInt(), 0, 0)
            view.layoutParams = params
            view.alpha = interpolatedTime
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        override fun isFillEnabled(): Boolean {
            return true
        }
    }


    internal class ResizeAnimation(
        private val view: View,
        private val mNewWidth: Int,
        private val mNewHeight: Int
    ) :
        Animation() {
        private val mCurrentHeight: Int
        private val mCurrentWidth: Int


        init {
            this.mCurrentWidth = view.width
            this.mCurrentHeight = view.height

        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (view.layoutParams is RelativeLayout.LayoutParams) {
                val params = view.layoutParams as RelativeLayout.LayoutParams
                params.width =
                    mCurrentWidth + ((mNewWidth - mCurrentWidth) * interpolatedTime + 0.5).toInt()
                params.height =
                    mCurrentHeight + ((mNewHeight - mCurrentHeight) * interpolatedTime + 0.5).toInt()
                view.layoutParams = params
            } else {
                val params = view.layoutParams as LinearLayout.LayoutParams
                params.width =
                    mCurrentWidth + ((mNewWidth - mCurrentWidth) * interpolatedTime + 0.5).toInt()
                params.height =
                    mCurrentHeight + ((mNewHeight - mCurrentHeight) * interpolatedTime + 0.5).toInt()
                view.layoutParams = params
            }
            view.requestLayout()

        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        override fun isFillEnabled(): Boolean {
            return true
        }
    }


}
