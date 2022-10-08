package com.example.hackathon2022

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderList (item: View) : RecyclerView.ViewHolder(item) {
    val characterList: TextView = item.findViewById(R.id.Text1)
}