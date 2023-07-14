package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.dnnsgnzls.jetpack.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}