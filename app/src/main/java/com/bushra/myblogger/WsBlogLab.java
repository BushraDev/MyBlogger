package com.bushra.myblogger;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WsBlogLab
{

    private static WsBlogLab sBlogLab;

    private Context bContext;

    static int uId=0;
    String postOwner = null;


    Boolean result=false;
    String uri="http://bushra.aba.vg/Blogger/";
    //String uri="http://192.168.137.1/Blogger/";
    ArrayList<PostModel> postsArrayList = new ArrayList<>();

    private WsBlogLab(Context context)
    {
        bContext = context.getApplicationContext();
    }


    public void addUser(final String name, final String email , final String password, final String birthdate , final String gender, final String photo, final Context context)
     {

         uId=0;

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"add_user.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try
                {

                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    Toast.makeText(bContext,jsonObject.getString("res"),Toast.LENGTH_SHORT).show();
                    result=true;


                }
                catch (JSONException e )
                {
                    Log.e("json error",e.getMessage());
                    Toast.makeText(bContext,"json\n"+response,Toast.LENGTH_SHORT).show();


                }
                catch (Exception e)
                {
                    Log.e("add user",e.getMessage());
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();

                }

                if(result)
                {
                    WsBlogLab.get(bContext).getUser(email,password,context);

                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {


            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("user_name",name);
                parameter.put("user_email",email);
                parameter.put("user_password",password);
                parameter.put("user_birthdate",birthdate);
                parameter.put("user_gender",gender);
                parameter.put("user_photo",photo);

                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }


    public int getUser(final String email, final String pass, final Context context)
    {
        uId=0;
        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_user.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                        JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        uId = jsonObject.getInt("u_id");

                }
                catch (JSONException e )
                    {

                        Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                        Log.e("get user",e.getMessage());
                    }
                if(uId != 0)
                {
                    Intent i = BlogsActivity.newIntent(bContext,uId);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    bContext.startActivity(i);

                    //LoginFragment.editor.putString("email", email);
                    //LoginFragment.editor.commit();

                    try
                    {
                        ((Activity)context).finish();

                    }catch (Exception e)
                    {
                        Log.e("finish activity",e.getMessage());
                    }
                }

            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("user_email",email);
                parameter.put("user_password",pass);
                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
        return uId;
    }
    public String getPostOwnerName(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_post_owner.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    postOwner = jsonObject.getString("name");

                }
                catch (JSONException e )
                {

                    Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("user_id",id);
                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
        return postOwner;
    }

    public String getPostOwnerPhoto(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_post_owner.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    postOwner = jsonObject.getString("photo");

                }
                catch (JSONException e )
                {

                    Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("user_id",id);
                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
        return postOwner;
    }




    public void addPost(final String category,final String title,final String content,final String date,final int u_Id,final String pho)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"add_post.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                String res = null;
                try
                {

                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    Toast.makeText(bContext,jsonObject.getString("res"),Toast.LENGTH_SHORT).show();

                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response+"cant convert to json",Toast.LENGTH_SHORT).show();
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("post_title",title);
                parameter.put("post_category",category);
                parameter.put("post_content",content);
                parameter.put("post_date",date);
                parameter.put("post_uid",u_Id+"");
                parameter.put("post_photo",pho);

                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);

    }

    public void addComment(final String pId,final String uId,final String comment)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"add_comment.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                String res = null;
                try
                {

                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    Toast.makeText(bContext,jsonObject.getString("res"),Toast.LENGTH_SHORT).show();

                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response+"cant convert to json",Toast.LENGTH_SHORT).show();
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String,String>parameter=new HashMap<>();
                parameter.put("p_id",pId);
                parameter.put("u_id",uId);
                parameter.put("comment",comment);


                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);

    }


    public static WsBlogLab get(Context context)
    {
        if (sBlogLab == null)
        {
            sBlogLab = new WsBlogLab(context);
        }
        return sBlogLab;
    }



    interface VolleyListiner
    {

        void onsucss(ArrayList<PostModel> postModels);
    }

    interface CommentsVolleyListiner
    {

        void onsucss(ArrayList<String> comments);
    }




    public void getProgrammingPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<PostModel>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_programming_posts.php"
                , new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        ArrayList<PostModel> posts=new ArrayList<PostModel>();

                        JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                        for (int i =0;i<jsonArray.length();i++)
                        {
                            JSONObject post=jsonArray.getJSONObject(i);
                            PostModel p=PostModel.getPost(post);
                            posts.add(p);
                        }

                        listiner.onsucss(posts);
                    }
                    catch (JSONException e )
                    {
                        e.printStackTrace();
                        Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                    }
                }
            }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);

    }

    public void getSecurityPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<PostModel>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_security_posts.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    ArrayList<PostModel> posts=new ArrayList<PostModel>();

                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject post=jsonArray.getJSONObject(i);
                        PostModel p=PostModel.getPost(post);
                        posts.add(p);
                    }

                    listiner.onsucss(posts);
                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                    Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }

    public void getNewsPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<PostModel>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_news_posts.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    ArrayList<PostModel> posts=new ArrayList<PostModel>();

                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject post=jsonArray.getJSONObject(i);
                        PostModel p=PostModel.getPost(post);
                        posts.add(p);
                    }

                    listiner.onsucss(posts);
                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                    Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }

    public void getComments(final CommentsVolleyListiner listiner,final String pId)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_comments.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    ArrayList<String> comments=new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject comment=jsonArray.getJSONObject(i);
                        comments.add(comment.getString("comment"));
                    }

                    listiner.onsucss(comments);
                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                    Toast.makeText(bContext,"error converting string to json",Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(bContext, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<>();
                parameter.put("p_id", pId);


                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }


    public String getServerUrl()
    {
        return uri;
    }
}
