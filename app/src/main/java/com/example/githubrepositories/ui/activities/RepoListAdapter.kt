package com.example.githubrepositories.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.R
import com.example.githubrepositories.data.RepoResult
import com.example.githubrepositories.extensions.ctx
import kotlinx.android.synthetic.main.item_repo.view.*


class RepoListAdapter(private val repoList: RepoResult) : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_repo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRepo(repoList.items[position])
    }

    override fun getItemCount(): Int = repoList.items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindRepo(repo: RepoResult.Item) {
            itemView.userName.text = repo.owner.login.orEmpty()
            itemView.repoName.text = repo.fullName.orEmpty()
            itemView.repoDescription.text = repo.description.orEmpty()
        }
    }
}



