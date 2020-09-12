package org.alberto97.hisenseair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.alberto97.hisenseair.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var _displayInPanel = false
    val displayInPanel get() = _displayInPanel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _displayInPanel = intent.getBooleanExtra(UIConstants.EXTRA_USE_PANEL, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
