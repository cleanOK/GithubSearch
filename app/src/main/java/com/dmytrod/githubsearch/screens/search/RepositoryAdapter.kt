package com.dmytrod.githubsearch.screens.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmytrod.githubsearch.R
import com.dmytrod.githubsearch.model.Repository
import kotlinx.android.synthetic.main.repository_list_item.view.*

class RepositoryAdapter(private val repositories: List<Repository>) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.repository_list_item))

    override fun getItemCount() = repositories.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(repositories[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(repository: Repository) {
            itemView.name.text = repository.name
            itemView.stars.text = repository.starts.toString()
            itemView.seen.isChecked = repository.seen
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

}