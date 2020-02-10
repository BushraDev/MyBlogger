package com.bushra.myblogger;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class BlogLab
{

    private static BlogLab sBlogLab;

    private Context bContext;
    static int uId=0;
    User user;
    Boolean result=false;
    //String uri="http://bushra.aba.vg/Blogger/";
    public static String uri="http://192.168.1.4/Blogger/";

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
                ProgressDialog progress;
                progress = new ProgressDialog(context);
                progress.setMessage("Signing Up...");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
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
                    progress.dismiss();

                    blogLab.getUser(email, password,context,listiner);
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error,context);
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
                handleVolleyError(error,context);
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

    public void getOwner(final String id,final UserVolleyListiner listiner)
    {
        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"get_post_owner.php"
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
    }

    public void addPost(final addPostVolleyListiner listener,final String category,final String title,final String content,final String date,final int u_Id,final String pho,final Context context)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"add_post.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    if(jsonObject.getString("res").matches("failed"))
                    {
                        listener.onsucss(false);
                        Log.e("post",response);
                    }
                    else
                    {
                        listener.onsucss(true);
                    }

                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error,context);
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

    public void addComment(final addCommentsVolleyListiner listiner,final String pId,final String uId,final String comment)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"add_comment.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try
                {

                    JSONObject jsonObject= new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    Log.e("response",response);
                    Log.e("res ",jsonObject.getString("res")+"");
                    if((jsonObject.getString("res")).matches("failed"))
                    {
                        Log.e("response",response);
                        listiner.onsucss(false);
                    }
                    else
                        listiner.onsucss(true);


                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                }

            }

        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //handleVolleyError(error);

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

    interface getCommentsVolleyListiner
    {

        void onsucss(ArrayList<Comment> comments);
    }
    interface addCommentsVolleyListiner
    {

        void onsucss(Boolean res);
    }
    interface updatePostVolleyListiner
    {
        void onsucss(Boolean res);
    }

    interface addPostVolleyListiner
    {
        void onsucss(Boolean res);
    }

    interface UserVolleyListiner
    {

        void onsucss(User user);
    }

    public void getProgrammingPosts(final VolleyListiner listiner, final Context context)
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

                        JSONArray jsonArray = new JSONArray(response);
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

                    }
                }
            }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handleVolleyError(error,context);
            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);

    }

    public void getSecurityPosts(final VolleyListiner listiner, final Context context)
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

            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }

    public void getNewsPosts(final VolleyListiner listiner, final Context context)
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


            }
        }

        );

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }

    public void getComments(final getCommentsVolleyListiner listiner,final String pId)
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
                    ArrayList<Comment> comments=new ArrayList<Comment>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        comments.add(Comment.getComment(jsonArray.getJSONObject(i)));
                    }

                    listiner.onsucss(comments);
                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               // handleVolleyError(error);
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

    void handleVolleyError(VolleyError error,Context context)
    {
        String warningMessage;
        if(error instanceof NoConnectionError)
        {
            ConnectivityManager cm = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                warningMessage=bContext.getString(R.string.server_error);
            } else {
                warningMessage=bContext.getString(R.string.network_connection_error);
            }
        }else if (error instanceof TimeoutError ) {
            warningMessage= bContext.getString(R.string.network_connection_timeout_error);
        } else if (error instanceof AuthFailureError) {
            warningMessage= bContext.getString(R.string.auth_failure_error);

        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            warningMessage= bContext.getString(R.string.server_error);

        }else if (error.getCause() instanceof OutOfMemoryError){
            warningMessage= bContext.getString(R.string.out_of_memory_error);
        }else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException) {
            warningMessage= bContext.getString(R.string.Parse_error);
        }
        else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("connection"))){
            warningMessage= bContext.getString(R.string.network_connection_error);
        }
        else {
            warningMessage= bContext.getString((R.string.unknown_error));
        }
        showCustomDialog(warningMessage,context);

    }

    private void showCustomDialog(String warningMessage, Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);
        ((TextView)dialog.findViewById(R.id.warning_message)).setText(warningMessage);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void updatePost(final updatePostVolleyListiner listiner,final String pId,final String likes,final String comments)
    {

        StringRequest request = new StringRequest(Request.Method.POST
                , uri+"update_post.php"
                , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("res").matches("failed"))
                    listiner.onsucss(false);
                    else
                        listiner.onsucss(true);
                }
                catch (JSONException e )
                {
                    e.printStackTrace();
                }
            }
        }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("error",error+"");
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<>();
                parameter.put("p_id", pId);
                parameter.put("p_likes", likes);
                parameter.put("p_comments", comments);


                return parameter;

            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(bContext);
        requestQueue.add(request);
    }
}
