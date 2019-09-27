package net.findbyte.dnloadingviewsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import net.findbyte.dnloadingview.DnLoadingView
import net.findbyte.dnloadingview.models.DnLoadingViewConfigurationModel
import net.findbyte.dnloadingviewsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        var loadingCenterConfig = DnLoadingViewConfigurationModel(Gravity.CENTER, 5,50f,10f, R.drawable.logo, R.drawable.seed)

        binding.startLoadingButton.setOnClickListener {
            DnLoadingView.showLoadingProgress(binding.mainPageContainer, loadingCenterConfig)
        }
        binding.endLoadingButton.setOnClickListener {
            DnLoadingView.hideLoadingProgress(binding.mainPageContainer)
        }
    }
}
