package hr.itrojnar.instagram.view.main

import androidx.paging.ExperimentalPagingApi
import hr.itrojnar.instagram.viewmodel.PostsViewModel

@ExperimentalPagingApi
class PostsState(postsViewModel: PostsViewModel) {
    val posts = postsViewModel.posts
}