package net.findbyte.dnloadingview.models

import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import net.findbyte.dnloadingview.databinding.DnLoadingViewBinding

class DnLoadingViewConfigurationModel {
    var seedsCount: Int = 5
    var logoSizePercent: Float = 50f
    var seedSize: Float = 10f
    var logoImageRes: Int = -1
    var seedImageRes: Int = -1
    var seedColor: Int = -1
    var seedBorderColor: Int = 0
    var loaderSizePercent: Int = 80
    var loaderMaxSize: Float = 100f
    var loaderProgress: Int = 0
    var gravity: Int = Gravity.CENTER


    constructor(
        gravity: Int = Gravity.CENTER,
        seedsCount: Int = 5,
        logoSizePercent: Float = 50f,
        seedSize: Float = 10f,
        logoImageRes: Int = -1,
        seedImageRes: Int = -1,
        loaderSizePercent: Int = 80,
        loaderMaxSize: Float = 100f,
        loaderProgress: Int = 0
        ) {
        this.gravity = gravity
        this.seedsCount = seedsCount
        this.logoSizePercent = logoSizePercent
        this.seedSize = seedSize
        this.logoImageRes = logoImageRes
        this.seedImageRes = seedImageRes
        this.seedColor = seedColor
        this.seedBorderColor = seedBorderColor
        this.loaderSizePercent = loaderSizePercent
        this.loaderMaxSize = loaderMaxSize
        this.loaderProgress = loaderProgress


    }



}