package ymsli.com.ea1h.views.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import kotlinx.android.synthetic.main.fragment_home_header_double.*
import kotlinx.android.synthetic.main.home_fragment_no_bike.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.Event
import java.lang.Exception

class HomeNoBikeFragment : BaseFragment<HomeViewModel>() {

    /** Fragment object creation factory. */
    companion object {
        const val TAG = "HomeNoBikeFragment"
        fun newInstance(): HomeNoBikeFragment {
            val args = Bundle()
            val fragment =
                HomeNoBikeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.home_fragment_no_bike

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        tv_welcome.text = "${requireContext().resources.getString(R.string.welcome_string)} ${viewModel.getUserProfileDetail().fullName?:""}"
        //load layout background from API
        val bikeImageUrl = viewModel.getContentValue(21)
        Glide.with(requireActivity())
            .asBitmap()
            .load(bikeImageUrl)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    try {
                        val layoutBackground = BitmapDrawable(resources, resource)
                        cl_no_bike_parent.background = layoutBackground
                    }
                    catch (ex: Exception){

                    }
                }
            })

        btn_answer_back.setOnClickListener {
            viewModel.answerBackButton.postValue(Event(true))
        }

        btn_driving_history.setOnClickListener {
            viewModel.drivingHistoryButton.postValue(Event(true))
        }

        btn_locate_bike.setOnClickListener {
            viewModel.locateBikeButton.postValue(Event(true))
        }

        btn_elock.setOnClickListener {
            viewModel.eLockButton.postValue(Event(true))
        }

        btn_hazard.setOnClickListener {
            viewModel.hazardButton.postValue(Event(true))
        }

        btn_parking_record.setOnClickListener {
            viewModel.parkingRecordButton.postValue(Event(true))
        }
    }
}