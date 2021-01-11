package com.miko.gamesss.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miko.gamesss.R
import com.miko.gamesss.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    private var gameId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        gameId = DetailActivityArgs.fromBundle(intent.extras as Bundle).gameId
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}