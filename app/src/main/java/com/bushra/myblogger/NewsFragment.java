package com.bushra.myblogger;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFragment extends Fragment
{

    RecyclerView mRecyclerview;
    PostAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v =  inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerview=v.findViewById(R.id.news_posts_recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUpAdapter();

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUpAdapter();
    }

    class PostAdapter extends RecyclerView.Adapter<PostHolder>
    {

        ArrayList<Post> postsArrayList;

        public PostAdapter(ArrayList<Post> posts)
        {
            postsArrayList=posts;
        }

        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PostHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder viewHolder, int i)
        {
            Post post = postsArrayList.get(i);
            viewHolder.bind(post);
        }

        @Override
        public int getItemCount() {
            return postsArrayList.size();
        }
        public void setPosts(ArrayList<Post> post)
        {
            postsArrayList = post;
        }
    }

    class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Post post;
        ImageView mPostPhotoImageView,mPostShareImageView,mPostOwnerImageView;
        TextView mPostOwnerTextView,mPostTitleTextView,mPostDateTextView;

        public PostHolder(LayoutInflater inflater, ViewGroup viewGroup)
        {
            super(inflater.inflate(R.layout.posts_list_item,viewGroup,false));
            mPostPhotoImageView=itemView.findViewById(R.id.p_photo);
            mPostTitleTextView=itemView.findViewById(R.id.p_title);
            mPostOwnerTextView=itemView.findViewById(R.id.p_owner);
            mPostDateTextView=itemView.findViewById(R.id.p_date);
            mPostShareImageView=itemView.findViewById(R.id.p_share);
            mPostOwnerImageView=itemView.findViewById(R.id.p_owner_photo);
        }

        public void bind(Post post) {
            this.post = post;
            String pimageUrl = BlogLab.get(getActivity()).getServerUrl() + "\\" + this.post.getpPhoto();
            mPostTitleTextView.setText(this.post.getpTitle());
            String u_name = BlogLab.get(getActivity()).getPostOwnerName(String.valueOf(this.post.getuId()));
            mPostOwnerTextView.setText(u_name);
            mPostDateTextView.setText(this.post.getpDate());
            Picasso.get().load(pimageUrl).into(mPostPhotoImageView);
        }

        @Override
        public void onClick(View view)
        {

        }
    }

    private void setUpAdapter()
    {

        BlogLab blogLab = BlogLab.get(getActivity());
        // ArrayList<Post> posts = blogLab.getPostsArrayList();

        BlogLab.VolleyListiner listiner=new BlogLab.VolleyListiner() {
            @Override
            public void onsucss(ArrayList<Post> postModels) {

                mAdapter = new PostAdapter(postModels);
                mRecyclerview.setAdapter(mAdapter);


            }
        };

        blogLab.getNewsPosts(listiner);
    }

}
