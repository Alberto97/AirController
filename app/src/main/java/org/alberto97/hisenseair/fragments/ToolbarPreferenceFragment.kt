package org.alberto97.hisenseair.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceFragmentCompat
import org.alberto97.hisenseair.R

abstract class ToolbarPreferenceFragment : PreferenceFragmentCompat() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.theme?.applyStyle(R.style.AppTheme_ToolbarPreferenceOverlay, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Must be called after setSupportActionBar
        toolbar.setupWithNavController(findNavController())
    }

    fun getToolbar(): ActionBar? {
        return (activity as AppCompatActivity).supportActionBar
    }
}