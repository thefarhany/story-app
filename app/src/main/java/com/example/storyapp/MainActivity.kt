package com.example.storyapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.location.MapsActivity
import com.example.storyapp.ui.onboarding.OnboardingActivity
import com.example.storyapp.ui.story.addstory.AddStory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        setUpObservers()

        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddStory::class.java))
        }
    }

    private fun setUpObservers() {
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        mainViewModel.getStories.observe(this) {
            if (it != null) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            mainViewModel.logout()

            val intent = Intent(this, OnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            true
        }

        R.id.action_location -> {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}