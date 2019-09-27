package net.findbyte.dnloadingview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.dn_loading_view.view.*
import net.findbyte.dnloadingview.databinding.DnLoadingViewBinding
import net.findbyte.dnloadingview.models.DnLoadingViewConfigurationModel
import net.findbyte.dnloadingview.utils.AnimationUtils
import net.findbyte.dnloadingview.utils.DimentionUtils
import java.util.ArrayList
import kotlin.math.min

class DnLoadingView : FrameLayout {
    private var mSeedImageList: MutableList<ImageView>? = null
    private var dnLoadingViewBinding: DnLoadingViewBinding? = null
    private var configuration: DnLoadingViewConfigurationModel = DnLoadingViewConfigurationModel()
    private var mLoaderProgress: Int = 0


    constructor(context: Context) : super(context) {
        inflateSubviews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getParameters(attrs)
        inflateSubviews()
        initAnimationBase()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        getParameters(attrs)
        inflateSubviews()
        initAnimationBase()
    }

    fun getBinding() : DnLoadingViewBinding?{
        return dnLoadingViewBinding
    }

    private fun getParameters(attrs: AttributeSet?) {
        configuration = DnLoadingViewConfigurationModel()
        configuration.seedSize = DimentionUtils.dpToPx(context, configuration.logoSizePercent)
        configuration.loaderMaxSize = DimentionUtils.dpToPx(context, configuration.loaderMaxSize)
        configuration.seedColor = context.resources.getColor(R.color.default_seed_color)
        configuration.seedBorderColor = context.resources.getColor(R.color.default_seed_border_color)
        configuration.loaderSizePercent = context.resources.getColor(R.color.default_seed_border_color)

        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.DnLoadingView)
            configuration.logoSizePercent = typeArray.getFloat(R.styleable.DnLoadingView_dn_logo_size_percent, configuration.logoSizePercent)
            configuration.seedSize = typeArray.getDimension(R.styleable.DnLoadingView_dn_logo_size_percent, configuration.seedSize)
            configuration.logoImageRes = typeArray.getResourceId(R.styleable.DnLoadingView_dn_logo_image_res, configuration.logoImageRes)
            configuration.seedImageRes = typeArray.getResourceId(R.styleable.DnLoadingView_dn_seed_image_res, configuration.seedImageRes)
            configuration.seedColor = typeArray.getColor(R.styleable.DnLoadingView_dn_seed_color, configuration.seedColor)
            configuration.seedBorderColor = typeArray.getColor(R.styleable.DnLoadingView_dn_seed_color, configuration.seedBorderColor)
            configuration.loaderSizePercent = typeArray.getColor(R.styleable.DnLoadingView_dn_seed_color, configuration.loaderSizePercent)
            configuration.loaderMaxSize = typeArray.getDimension(R.styleable.DnLoadingView_dn_loader_max_size, configuration.loaderMaxSize)
            configuration.seedsCount = typeArray.getInt(R.styleable.DnLoadingView_dn_seed_count, configuration.seedColor)
            mLoaderProgress = 0
            typeArray.recycle()
        }

    }

    override fun onDetachedFromWindow() {
        mSeedImageList?.clear()
        super.onDetachedFromWindow()

    }

    fun initAnimationBase() {
        //this.removeAllViews()
        this.visibility = View.VISIBLE

        mSeedImageList = ArrayList()
        for (i in 1..configuration.seedsCount) {
            val seedImage = ImageView(context)
            val params =
                RelativeLayout.LayoutParams(configuration.seedSize.toInt(), configuration.seedSize.toInt())
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            seedImage.layoutParams = params
            if(configuration.seedImageRes>=0)
                seedImage.setImageResource(configuration.seedImageRes)
            this.dnLoadingViewBinding?.progressIndicatorPopup?.addView(seedImage)
            mSeedImageList?.add(seedImage)

            dnLoadingViewBinding?.loadingContainer?.width?.let {
                AnimationUtils.applyAnimationSeed(
                    context,
                    seedImage,
                    0,
                    DimentionUtils.dpToPx(context, (it/3*0.8).toFloat()).toInt(),
                    DimentionUtils.dpToPx(context,configuration.seedSize).toInt(),
                    DimentionUtils.dpToPx(context,configuration.seedSize).toInt(),
                    500,
                    100 * i
                )
            }

            if (configuration.seedsCount != 0)
                seedImage.rotation = (360 / configuration.seedsCount * i).toFloat()

        }
        dnLoadingViewBinding?.progressIndicatorPopup?.let {progressView ->
            AnimationUtils.applyAnimationWithFinalShow(
                context,
                progressView,
                R.anim.animation_continuous_rotaion,
                0
            )
        }


    }

    fun stopAnimationBase(afterDisappearRun: Runnable?) {
        for (i in 1..configuration.seedsCount) {
            if (i == configuration.seedsCount) {
                if (mSeedImageList!![i - 1].visibility == View.VISIBLE) {
                    AnimationUtils.applyAnimationWithRunnable(context,
                        mSeedImageList!![i - 1],
                        R.anim.animation_disappear,
                        50 * i - 100,
                        Runnable {
                            mSeedImageList!![i - 1].visibility = View.GONE
                            afterDisappearRun?.run()
                        })
                } else {
                    afterDisappearRun?.run()
                }


            } else if (mSeedImageList!![i - 1].visibility == View.VISIBLE) {
                AnimationUtils.applyAnimationWithFinalHide(
                    context,
                    mSeedImageList!![i - 1], R.anim.animation_disappear, 50 * i - 100
                )
            }
        }
    }

    fun inflateSubviews(){
        val inflater = LayoutInflater.from(context)
        this.dnLoadingViewBinding = DataBindingUtil.inflate<DnLoadingViewBinding>(
            inflater,
            R.layout.dn_loading_view,
            this,
            true
        )

    }

    fun applyConfiguration(configuration: DnLoadingViewConfigurationModel) {
        this.configuration = configuration
        this.configuration.seedSize = DimentionUtils.dpToPx(context, this.configuration.logoSizePercent)
        this.configuration.loaderMaxSize = DimentionUtils.dpToPx(context, this.configuration.loaderMaxSize)
    }

    companion object {

        fun showLoadingProgress(view: ViewGroup) {
            showLoadingProgress(view, null)
        }

        fun showLoadingProgress(view: ViewGroup, configuration: DnLoadingViewConfigurationModel?) {
            var mainContainerLayout: DnLoadingView? = view.findViewById<RelativeLayout>(R.id.dn_loading_view)?.parent as? DnLoadingView
            view.context?.let {
                configuration?.let {
                    mainContainerLayout?.applyConfiguration(it)
                }
                mainContainerLayout?.getBinding()?.let { binding ->
                    if (mainContainerLayout?.visibility != View.VISIBLE) {
                        mainContainerLayout?.visibility = View.VISIBLE
                        mainContainerLayout?.initAnimationBase()
                        AnimationUtils.applyAnimationWithFinalShow(
                            view.context,
                            binding.root,
                            R.anim.animation_fade_out_fast,
                            0
                        )
                        binding.progressLogo?.let { logoImage ->
                            AnimationUtils.applyAnimationRepeatless(
                                view.context,
                                logoImage,
                                R.anim.animation_progress_logo,
                                0
                            )
                        }
                    }
                    binding.executePendingBindings()
                    binding.root.bringToFront()
                    return@showLoadingProgress
                }
                mainContainerLayout = DnLoadingView(view.context)
                configuration?.let {
                    mainContainerLayout?.applyConfiguration(it)
                    if(configuration.logoImageRes>=0)
                        mainContainerLayout?.dnLoadingViewBinding?.progressLogo?.setImageResource(configuration.logoImageRes)
                }


                configuration?.gravity?.let {
                    (mainContainerLayout?.getBinding()?.loadingContainer?.layoutParams as? RelativeLayout.LayoutParams)?.let { params ->
                        if (configuration.gravity or Gravity.CENTER == configuration.gravity || configuration.gravity == 0)
                            params.addRule(RelativeLayout.CENTER_IN_PARENT)
                        if (configuration.gravity or Gravity.CENTER_VERTICAL == configuration.gravity)
                            params.addRule(RelativeLayout.CENTER_VERTICAL)
                        if (configuration.gravity or Gravity.CENTER_HORIZONTAL == configuration.gravity)
                            params.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        if (configuration.gravity or Gravity.LEFT == configuration.gravity)
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        if (configuration.gravity or Gravity.RIGHT == configuration.gravity)
                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        if (configuration.gravity or Gravity.TOP == configuration.gravity)
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                        if (configuration.gravity or Gravity.BOTTOM == configuration.gravity)
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        mainContainerLayout?.getBinding()?.loadingContainer?.layoutParams = params
                    }
                    mainContainerLayout?.getBinding()?.executePendingBindings()
                }
                view.addView(mainContainerLayout)



                mainContainerLayout?.layoutParams?.width = view.width
                mainContainerLayout?.layoutParams?.height = view.height
                mainContainerLayout?.getBinding()?.dnLoadingView?.layoutParams?.width = view.width
                mainContainerLayout?.getBinding()?.dnLoadingView?.layoutParams?.height = view.height

                configuration?.loaderMaxSize?.let { loaderSize ->
                    if((view.width * configuration.loaderSizePercent / 100) > configuration.loaderMaxSize ||
                       (view.height * configuration.loaderSizePercent / 100) > configuration.loaderMaxSize) {

                            mainContainerLayout?.dnLoadingViewBinding?.loadingContainer?.layoutParams?.width =
                                configuration.loaderMaxSize.toInt()
                            mainContainerLayout?.dnLoadingViewBinding?.loadingContainer?.layoutParams?.height =
                                configuration.loaderMaxSize.toInt()
                        } else {
                            var maxSize = min(view.width * configuration.loaderSizePercent / 100, view.height * configuration.loaderSizePercent / 100)

                            mainContainerLayout?.dnLoadingViewBinding?.loadingContainer?.layoutParams?.width =
                                (maxSize * configuration.loaderSizePercent / 100)
                            mainContainerLayout?.dnLoadingViewBinding?.loadingContainer?.layoutParams?.height =
                                (maxSize * configuration.loaderSizePercent / 100)

                        }
                }



                mainContainerLayout?.dnLoadingViewBinding?.loadingContainer?.layoutParams?.width?.let { loaderSize ->
                    configuration?.let {
                        mainContainerLayout?.dnLoadingViewBinding?.progressLogo?.layoutParams?.width = (loaderSize * configuration.logoSizePercent / 100).toInt()
                        mainContainerLayout?.dnLoadingViewBinding?.progressLogo?.layoutParams?.height = (loaderSize * configuration.logoSizePercent / 100).toInt()
                    }
                }

                mainContainerLayout?.initAnimationBase()

                mainContainerLayout?.let {
                    AnimationUtils.applyAnimationWithFinalShow(
                        view.context,
                        it,
                        R.anim.animation_fade_out_fast,
                        0
                    )
                }

                mainContainerLayout?.getBinding()?.let { binding ->
                    mainContainerLayout?.getBinding()?.progressIndicatorPopup?.visibility = View.VISIBLE
                    AnimationUtils.applyAnimationRepeatless(
                        view.context,
                        binding.progressLogo,
                        R.anim.animation_progress_logo,
                        0
                    )
                    binding.executePendingBindings()
                    binding.root.bringToFront()
                }

            }
        }

        fun hideLoadingProgress(view: View) {
            var mainContainerLayout: DnLoadingView? = view.findViewById<RelativeLayout>(R.id.dn_loading_view)?.parent as? DnLoadingView

            mainContainerLayout?.let { mainView ->
                mainView.getBinding()?.let {binding ->
                    mainView.stopAnimationBase(Runnable {
                        binding.progressLogo?.clearAnimation()
                        binding.loadingContainer?.clearAnimation()
                        binding.progressIndicatorPopup?.clearAnimation()
                        binding.dnLoadingView?.clearAnimation()
                        AnimationUtils.applyAnimationWithRunnable(view.context,
                            mainView,
                            R.anim.animation_fade_in_fast,
                            0,
                            Runnable {
                                mainView.visibility = View.GONE
                            })
                    })
                }
            }
        }

    }

}