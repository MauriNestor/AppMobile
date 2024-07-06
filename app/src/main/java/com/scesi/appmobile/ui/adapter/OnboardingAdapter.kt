package com.scesi.appmobile.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scesi.appmobile.R

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val onboardingScreens = listOf(
        R.layout.onboarding_screen1,
        R.layout.onboarding_screen2
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = onboardingScreens.size

    override fun getItemViewType(position: Int): Int = onboardingScreens[position]

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
