package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDetailsActivity extends AppCompatActivity
{

    private static final String EXTRA_POST = "com.bushra.myblogger.post";
    private static final String EXTRA_User = "com.bushra.myblogger.user";

    Toolbar toolbar;
    Post post;
    User user;
    ImageView urPhoto, image, send;
    TextView urName, date, title, content;
    EditText comment;
    RecyclerView mRecyclerview;
    CommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        post = (Post) getIntent().getSerializableExtra(EXTRA_POST);
        user = (User) getIntent().getSerializableExtra(EXTRA_User);

        initToolbar();
        initPostInfoContainer();

        mRecyclerview = findViewById(R.id.comment_recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this));
        setUpAdapter();
        initNewPostCommentContainer();

    }


    public static Intent newIntent(Context packageContext, Post post,User user)
    {
        Intent intent = new Intent(packageContext, PostDetailsActivity.class);
        intent.putExtra(EXTRA_POST,  post);
        intent.putExtra(EXTRA_User,  user);
        return intent;
    }

    private void initToolbar()
    {
        toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        urPhoto = findViewById(R.id.ur_photo);
        Picasso.get().load( BlogLab.get(PostDetailsActivity.this).getServerUrl() + "\\" + user.getuPhoto()).into(urPhoto);

        urName = findViewById(R.id.ur_name);
        urName.setText(user.getuName());

        date = findViewById(R.id.date);
        date.setText(post.getpDate());
    }

    private  void initPostInfoContainer()
    {
        title = findViewById(R.id.title);
        title.setText(post.getpTitle());

        image = findViewById(R.id.image);
        String pimageUrl = BlogLab.get(PostDetailsActivity.this).getServerUrl() + "\\" + post.getpPhoto();
        Picasso.get().load(pimageUrl).into(image);

        content = findViewById(R.id.content);
        content.setText(post.getpContent());
    }

    private void initNewPostCommentContainer()
    {
        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogLab.addCommentsVolleyListiner listiner = new BlogLab.addCommentsVolleyListiner() {
                    @Override
                    public void onsucss(Boolean result) {
                    if(result==false)
                    {
                        Toast.makeText(PostDetailsActivity.this,"Failed posting comment",Toast.LENGTH_LONG).show();
                    }
                    else if(result==true)
                    {
                        Toast.makeText(PostDetailsActivity.this,"Comment posted successfully",Toast.LENGTH_LONG).show();
                        setUpAdapter();
                        final BlogLab.updatePostVolleyListiner uListener=new BlogLab.updatePostVolleyListiner()
                        {

                            @Override
                            public void onsucss(Boolean res) {
                                if(res)
                                {
                                    setUpAdapter();
                                }
                            }
                        };
                        int com=post.getpComments()+1;
                        BlogLab.get(PostDetailsActivity.this).updatePost(uListener,String.valueOf(post.getpId()),String.valueOf(post.getpLikes()),String.valueOf(com));

                    }

                    }
                };
                final String MyPREFERENCES = "Bushra";
                SharedPreferences sharedpreferences= getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                final int uId=sharedpreferences.getInt("id",0 );
                BlogLab.get(PostDetailsActivity.this).addComment(listiner,String.valueOf(post.getpId()), String.valueOf(uId), comment.getText().toString());
                comment.setText("");
            }
        });
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        ArrayList<Comment> comments;

        public CommentAdapter(ArrayList<Comment> comment) {
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
            String comment = comments.get(i).getComment();
            int uId = comments.get(i).getuId();
            viewHolder.bind(comment,uId);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public void setPosts(ArrayList<Comment> comment) {
            comments = comment;
        }
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView mCommentOwnerName,mCommentTextView;
        ImageView mCommentOwnerPhoto;


        public CommentHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.comment_list_item, viewGroup, false));
            mCommentTextView = itemView.findViewById(R.id.comment_tv);
            mCommentOwnerName = itemView.findViewById(R.id.c_owner_name);
            mCommentOwnerPhoto = itemView.findViewById(R.id.c_owner_photo);

        }

        public void bind(String comment,int id) {
            String p_comment = comment;
            int u_id=id;
            mCommentTextView.setText(p_comment);
            final BlogLab.UserVolleyListiner listiner = new BlogLab.UserVolleyListiner() {
                @Override
                public void onsucss(User u) {

                    user=u;
                    Picasso.get().load(BlogLab.get(PostDetailsActivity.this).getServerUrl()+user.getuPhoto()).into(mCommentOwnerPhoto);
                    mCommentOwnerName.setText(user.getuName());
                }
            };
            BlogLab.get(PostDetailsActivity.this).getOwner(String.valueOf(u_id),listiner);
        }

    }

    private void setUpAdapter()
    {

        BlogLab blogLab = BlogLab.get(PostDetailsActivity.this);
        BlogLab.getCommentsVolleyListiner listiner=new BlogLab.getCommentsVolleyListiner() {
            @Override
            public void onsucss(ArrayList<Comment> comments) {

                if (mAdapter == null)
                {
                    mAdapter = new CommentAdapter(comments);
                    mRecyclerview.setAdapter(mAdapter);
                }
                else
                {
                    mAdapter.setPosts(comments);
                    mAdapter.notifyDataSetChanged();
                }

            }
        };


        blogLab.getComments(listiner,String.valueOf(post.getpId()));
    }


}
