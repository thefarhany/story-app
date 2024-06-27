package com.example.storyapp.ui.story.detailstory

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.response.DetailStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStory : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, DetailStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        getDetailStory(storyItem!!)
        supportActionBar?.title = getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDetailStory(detail: DetailStoryItem) {
        binding.tvItemName.text = detail.name
        binding.storyDescription.text = detail.description
        binding.storyDate.text = detail.createdAt
        Glide.with(this)
            .load(detail.photoUrl)
            .into(binding.imgItemPhoto)
    }

    companion object {
        var EXTRA_STORY = "extra_story"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}