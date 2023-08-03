/*
 * Copyright (C) 2023 The LineageOS Project
 *
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

package com.dolby.tile

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Switch

import androidx.preference.Preference
import androidx.preference.PreferenceFragment

import com.android.settingslib.widget.MainSwitchPreference
import com.android.settingslib.widget.OnMainSwitchChangeListener
import com.android.settingslib.widget.RadioButtonPreference

import com.dolby.tile.R

class DolbyFragment : PreferenceFragment(), OnMainSwitchChangeListener {

    private lateinit var switchBar: MainSwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.dolby_settings)

        switchBar = findPreference<MainSwitchPreference>(PREF_DOLBY_ENABLE)!!
        switchBar.addOnSwitchChangeListener(this)
        switchBar.isChecked = DolbyCore.isEnabled()

        for ((key, value) in PREF_DOLBY_MODES) {
            val preference = findPreference<RadioButtonPreference>(key)!!
            preference.setOnPreferenceClickListener {
                setProfile(value)
                true
            }
        }

        val launchAppPreference = findPreference<Preference>("launch_app")!!
        launchAppPreference.setOnPreferenceClickListener {
            launchExternalApp()
            true
        }
    }

    override fun onSwitchChanged(switchView: Switch, isChecked: Boolean) {
        DolbyCore.setEnabled(isChecked)
    }

    private fun setProfile(profile: Int) {
        DolbyCore.setProfile(profile)

        for ((key, value) in PREF_DOLBY_MODES) {
            val preference = findPreference<RadioButtonPreference>(key)!!
            preference.isChecked = value == profile
        }
    }

private fun launchExternalApp() {
    val packageName = "com.dolby.daxappui2
    val activityName = "com.dolby.daxappui2.MainActivity"

    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(packageName, activityName)

    val packageManager = requireContext().packageManager
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
    }
}


    companion object {
        const val PREF_DOLBY_ENABLE = "dolby_enable"

        val PREF_DOLBY_MODES = mapOf(
            "dolby_profile_tds" to DolbyCore.PROFILE_TDS,
            "dolby_profile_dummy" to DolbyCore.PROFILE_DUMMY
        )
    }
}
