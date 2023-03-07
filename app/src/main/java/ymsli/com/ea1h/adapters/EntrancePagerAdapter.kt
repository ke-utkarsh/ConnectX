package ymsli.com.ea1h.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import ymsli.com.ea1h.R
import ymsli.com.ea1h.views.entrance.SignInFragment
import ymsli.com.ea1h.views.entrance.SignUpFragment

class EntrancePagerAdapter(fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity){

    private val PAGE_TILES = arrayOf(fragmentActivity.resources.getString(R.string.sign_in),fragmentActivity.resources.getString(R.string.sign_up))

    override fun getItemCount(): Int = PAGE_TILES.size

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return SignInFragment.newInstance()
            else -> return  SignUpFragment.newInstance()
        }
    }
}