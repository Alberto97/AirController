package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

abstract class ToolbarFragment : Fragment() {

    abstract fun getMenuResource(): Int?
    abstract fun getToolbar(): Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasMenu = getMenuResource() != null
        setHasOptionsMenu(hasMenu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(getToolbar())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuResource = getMenuResource()
        if (menuResource != null)
            inflater.inflate(menuResource, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }
}