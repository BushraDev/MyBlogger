package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_POST = "com.bushra.myblogger.post";

    Post post;

    ImageView urPhoto, image, send;
    TextView urName, date, title, content;
    EditText comment;

    RecyclerView mRecyclerview;
    CommentAdapter mAdapter;


    public static Intent newIntent(Context packageContext, Post post) {
        Intent intent = new Intent(packageContext, PostDetailsActivity.class);
        intent.putExtra(EXTRA_POST, (Serializable) post);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        post = (Post) getIntent().getSerializableExtra(EXTRA_POST);

        mRecyclerview = findViewById(R.id.comment_recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this));
        setUpAdapter();


        urPhoto = findViewById(R.id.ur_photo);

        urName = findViewById(R.id.ur_name);
        final String u_name = BlogLab.get(PostDetailsActivity.this).getPostOwnerName(String.valueOf(post.getuId()));
        urName.setText(u_name);

        date = findViewById(R.id.date);
        date.setText(post.getpDate());

        image = findViewById(R.id.image);
        String pimageUrl = BlogLab.get(PostDetailsActivity.this).getServerUrl() + "\\" + post.getpPhoto();
        Picasso.get().load(pimageUrl).into(image);

        title = findViewById(R.id.title);
        title.setText(post.getpTitle());

        content = findViewById(R.id.content);
        content.setText(post.getpContent());

        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogLab.get(PostDetailsActivity.this).addComment(String.valueOf(post.getpId()), String.valueOf(post.getuId()), comment.getText().toString());
                comment.setText("");
                setUpAdapter();
            }
        });

    }


    class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        ArrayList<String> comments;

        public CommentAdapter(ArrayList<String> comment) {
            comments = comment;
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(PostDetailsActivity.this);
            return new CommentHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(CommentHolder viewHolder, int i) {
            String comment = comments.get(i);
            viewHolder.bind(comment);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public void setPosts(ArrayList<String> comment) {
            comments = comment;
        }
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView mCommentTextView;

        public CommentHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.comment_list_item, viewGroup, false));
            mCommentTextView = itemView.findViewById(R.id.comment_tv);

        }

        public void bind(String comment) {
            String p_comment = comment;
            mCommentTextView.setText(p_comment);
        }

    }

    private void setUpAdapter()
    {

        BlogLab blogLab = BlogLab.get(PostDetailsActivity.this);
        BlogLab.CommentsVolleyListiner listiner=new BlogLab.CommentsVolleyListiner() {
            @Override
            public void onsucss(ArrayList<String> comments) {

                if (mAdapter == null) {
                    mAdapter = new CommentAdapter(comments);
                    mRecyclerview.setAdapter(mAdapter);
                }
                else
                    mAdapter.setPosts(comments);
                    mAdapter.notifyDataSetChanged();

            }
        };


        blogLab.getComments(listiner,String.valueOf(post.getpId()));
    }
}
