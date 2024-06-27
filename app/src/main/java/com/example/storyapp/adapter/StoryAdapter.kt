package com.example.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.DetailStoryItem
import com.example.storyapp.databinding.StoryItemBinding
import com.example.storyapp.ui.story.detailstory.DetailStory
import com.example.storyapp.utils.GetTime

class StoryAdapter: PagingDataAdapter<DetailStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.itemView.setOnClickListener {
            val date = story?.createdAt?.let { GetTime.getDate(it) }
            val data = story?.let { it1 ->
                DetailStoryItem(
                    story?.photoUrl,
                    date,
                    story?.name,
                    story?.description,
                    story?.lon,
                    it1.id,
                    story?.lat
                )
            }

            val intent = Intent(it.context, DetailStory::class.java)
            intent.putExtra(DetailStory.EXTRA_STORY, data)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                it.context as Activity,
                androidx.core.util.Pair(holder.binding.storyImage, "image"),
                androidx.core.util.Pair(holder.binding.tvStoryTitle, "name"),
                androidx.core.util.Pair(holder.binding.storyDescription, "description"),
                androidx.core.util.Pair(holder.binding.storyDate, "date")
            )

            it.context.startActivity(intent, options.toBundle())
        }
    }

    class MyViewHolder( val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: DetailStoryItem) {
            val date = storyItem.createdAt?.let { GetTime.getDate(it) }

            binding.tvStoryTitle.text = storyItem.name
            binding.storyDescription.text = storyItem.description
            binding.storyDate.text = date

            Glide.with(binding.root)
                .load(storyItem.photoUrl)
                .into(binding.storyImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailStoryItem>() {
            override fun areItemsTheSame(oldItem: DetailStoryItem, newItem: DetailStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DetailStoryItem,
                newItem: DetailStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}








//

