package com.example.storyapp

import com.example.storyapp.data.response.DetailStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<DetailStoryItem> {
        val items: MutableList<DetailStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = DetailStoryItem(
                i.toString(),
                "createdAt $i",
                "name $i",
                "desc $i",
                i.toDouble(),
                "id $i",
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}