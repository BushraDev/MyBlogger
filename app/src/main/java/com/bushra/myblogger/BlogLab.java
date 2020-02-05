package com.bushra.myblogger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class BlogLab
{

    private static BlogLab sBlogLab;

    private Context bContext;
    static int uId=0;
    String postOwner = null;
    User user;
    Boolean result=false;
    //String uri="http://bushra.aba.vg/Blogger/";
    public static String uri="http://192.168.1.6/Blogger/";
    ArrayList<Post> postsArrayList = new ArrayList<>();

    private BlogLab(Context context)
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
                    BlogLab blogLab = BlogLab.get(context);
                    BlogLab.UserVolleyListiner listiner=new BlogLab.UserVolleyListiner()
                    {
                        @Override
                        public void onsucss(User user) {

                            LoginFragment.checkUser(user,bContext);
                        }
                    };

                    blogLab.getUser(email, password,context,listiner);
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error);
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


    public void getUser(final String email, final String pass, final Context context,final UserVolleyListiner listiner)
    {
        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_user.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                JSONObject jsonObject=null;

                    try {
                        jsonObject= new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        Log.e("response",response);
                        Log.e("user id ",jsonObject.getInt("u_id")+"");
                        user=User.getUser(jsonObject);
                        Log.e("user id ",user.getuId()+"");
                        listiner.onsucss(user);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();

                        try {
                            if((jsonObject.getString("result")).matches("failed"))
                            {
                                Log.e("response",response);
                                user=null;
                                listiner.onsucss(user);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }


            }


        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error);
            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String>parameter=new HashMap<>();
                parameter.put("user_email",email);
                parameter.put("user_password",pass);
                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);
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

                Log.e("error",error.getMessage());
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
                Log.e("error",error.getMessage());
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
                handleVolleyError(error);
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
                handleVolleyError(error);

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

    public static BlogLab get(Context context)
    {
        if (sBlogLab == null)
        {
            sBlogLab = new BlogLab(context);
        }
        return sBlogLab;
    }

    interface VolleyListiner
    {

        void onsucss(ArrayList<Post> posts);
    }

    interface CommentsVolleyListiner
    {

        void onsucss(ArrayList<String> comments);
    }

    interface UserVolleyListiner
    {

        void onsucss(User user);
    }

    public void getProgrammingPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<Post>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_programming_posts.php"
                , new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        ArrayList<Post> posts=new ArrayList<Post>();

                        JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                        for (int i =0;i<jsonArray.length();i++)
                        {
                            JSONObject post=jsonArray.getJSONObject(i);
                            Post p= Post.getPost(post);
                            posts.add(p);
                        }

                        listiner.onsucss(posts);
                    }
                    catch (JSONException e )
                    {
                        e.printStackTrace();

                        e.printStackTrace();

                        try {
                            JSONObject jsonObject=new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            if((jsonObject.getString("result")).matches("failed"))
                            {
                                Log.e("response",response);
                                Toast.makeText(bContext,"There are no programming posts",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex)
                        {
                            ex.printStackTrace();
                        }

                    }/*
                    catch (Exception e)
                    {
                        Toast.makeText(bContext,response,Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error);
            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);

    }

    public void getSecurityPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<Post>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_security_posts.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    ArrayList<Post> posts=new ArrayList<Post>();

                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject post=jsonArray.getJSONObject(i);
                        Post p= Post.getPost(post);
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
                handleVolleyError(error);

            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }

    public void getNewsPosts(final VolleyListiner listiner)
    {
        //postsArrayList=new ArrayList<Post>();

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_news_posts.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    ArrayList<Post> posts=new ArrayList<Post>();

                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject post=jsonArray.getJSONObject(i);
                        Post p= Post.getPost(post);
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

                handleVolleyError(error);

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
                handleVolleyError(error);
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

    public boolean haveNetwork()
    {
        boolean have_WIFI = false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) bContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected()) have_WIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))
                if (info.isConnected()) have_MobileData = true;
        }
        return have_WIFI || have_MobileData;
    }

    void handleVolleyError(VolleyError error)
    {
        if (error instanceof TimeoutError  || error.getCause() instanceof SocketTimeoutException
                || error.getCause() instanceof ConnectTimeoutException
                || error.getCause() instanceof SocketException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("Connection timed out"))) {
            Toast.makeText(bContext, bContext.getString(R.string.network_connection_timeout_error),
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(bContext,
                    bContext.getString(R.string.auth_failure_error),
                    Toast.LENGTH_LONG).show();

        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            Toast.makeText(bContext,
                    bContext.getString(R.string.Server_error),
                    Toast.LENGTH_LONG).show();

        }else if (error.getCause() instanceof OutOfMemoryError){
            Toast.makeText(bContext, bContext.getString(R.string.out_of_memory_error), Toast.LENGTH_SHORT).show();
        }else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException) {
            Toast.makeText(bContext,
                    bContext.getString(R.string.Parse_error),
                    Toast.LENGTH_LONG).show();
        }
        else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("connection"))){
            Toast.makeText(bContext, bContext.getString(R.string.network_connection_error),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(bContext, bContext.getString((R.string.unknown_error)),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
