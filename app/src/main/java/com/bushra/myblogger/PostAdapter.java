package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context ctx;
    List<Post> postsArrayList=new ArrayList<>();
    private int item_per_display = 0;
    private PostAdapter.OnLoadMoreListener onLoadMoreListener = null;
    Boolean clicked=false;
    private final int VIEW_POST= 1;
    private final int VIEW_PROGRESS = 0;
    private boolean loading;

    public PostAdapter(Context context,int item_per_display, List<Post> posts)
    {
        postsArrayList=posts;
        this.item_per_display=item_per_display;
        ctx = context;
        Log.e("PPP",posts.get(0).getpCategory());
        Log.e("PPP",posts.size()+"");
        Log.e("PPP",item_per_display+"");
    }

    @Override
    public int getItemViewType(int position) {
        return this.postsArrayList.get(position).progress ? VIEW_PROGRESS : VIEW_POST;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress_bar;

        public ProgressViewHolder(View v) {
            super(v);
            progress_bar = v.findViewById(R.id.progress_bar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Log.e("ooo",i+"");
        RecyclerView.ViewHolder vh;
        if (i == VIEW_POST)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            vh= new PostAdapter.PostHolder(layoutInflater, viewGroup);
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress, viewGroup, false);
            vh = new ProgressViewHolder(v);
        }

        return  vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        Post post = postsArrayList.get(i);

        if (viewHolder instanceof PostHolder) {
            PostHolder view = (PostHolder) viewHolder;
            view.bind(post);
        }
        else {
            ((ProgressViewHolder) viewHolder).progress_bar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public void setLoading() {
        if (getItemCount() != 0)
        {
            this.postsArrayList.add(new Post(true));
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void setPosts(ArrayList<Post> post)
    {
        postsArrayList = post;
    }

    public void setOnLoadMoreListener(PostAdapter.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    class PostHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        Post post;
        User user;
        ImageView mPostPhotoImageView,mPostOwnerImageView;
        ImageButton mPostLikes;
        TextView mPostOwnerTextView,mPostTitleTextView,mPostDateTextView,mPostCategoryTextView,mPostLikesTextView,mPostCommentsTextView;

        public PostHolder(LayoutInflater inflater, ViewGroup viewGroup)
        {
            super(inflater.inflate(R.layout.posts_list_item,viewGroup,false));
            mPostOwnerImageView=itemView.findViewById(R.id.p_owner_photo);
            mPostOwnerTextView=itemView.findViewById(R.id.p_owner);
            mPostCategoryTextView=itemView.findViewById(R.id.p_cat);
            mPostTitleTextView=itemView.findViewById(R.id.p_title);
            mPostPhotoImageView=itemView.findViewById(R.id.p_photo);
            mPostLikesTextView=itemView.findViewById(R.id.p_likes);
            mPostCommentsTextView=itemView.findViewById(R.id.p_comments);
            mPostDateTextView=itemView.findViewById(R.id.p_date);
            mPostLikes=itemView.findViewById(R.id.add_like);
            itemView.setOnClickListener(this);
        }

        public void bind(Post p)
        {
            post = p;
            final BlogLab.UserVolleyListiner listiner = new BlogLab.UserVolleyListiner() {
                @Override
                public void onsucss(User u) {

                    user=u;
                    Picasso.get().load(BlogLab.get(ctx).getServerUrl()+user.getuPhoto()).into(mPostOwnerImageView);
                    mPostOwnerTextView.setText(user.getuName());
                }
            };
            BlogLab.get(ctx).getOwner(String.valueOf(post.getuId()),listiner);

            mPostCategoryTextView.setText(post.getpCategory());
            mPostTitleTextView.setText(post.getpTitle());
            Picasso.get().load(BlogLab.get(ctx).getServerUrl()+ post.getpPhoto()).into(mPostPhotoImageView);

            mPostLikesTextView.setText(post.getpLikes()+" likes");
            clicked=false;
            mPostLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final BlogLab.updatePostVolleyListiner uListener=new BlogLab.updatePostVolleyListiner()
                    {

                        @Override
                        public void onsucss(Boolean res) {
                            if(res)
                            {
                                new PostListActivity().setUpAdapter(PostListActivity.single_choice_selected);
                            }
                        }
                    };
                    if(clicked)
                    {
                        clicked=false;
                        post.setpLikes(post.getpLikes()-1);
                        BlogLab.get(ctx).updatePost(uListener,String.valueOf(post.getpId()),String.valueOf(post.getpLikes()),String.valueOf(post.getpComments()));

                    }
                    else
                    {
                        clicked=true;
                        post.setpLikes(post.getpLikes()+1);
                        BlogLab.get(ctx).updatePost(uListener,String.valueOf(post.getpId()),String.valueOf(post.getpLikes()),String.valueOf(post.getpComments()));

                    }
                }
            });
            mPostDateTextView.setText(this.post.getpDate());
            mPostCommentsTextView.setText(post.getpComments()+" comments");
        }
        @Override
        public void onClick(View view)
        {
            Intent intent = PostDetailsActivity.newIntent(ctx, post,user);
            ctx.startActivity(intent);
        }
    }
    public void insertData(List<Post> posts) {
        Log.e("zzz",posts.size()+"");
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = posts.size();
        this.postsArrayList.addAll(posts);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (postsArrayList.get(i).progress) {
                postsArrayList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

}