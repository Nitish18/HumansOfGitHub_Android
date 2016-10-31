package hg.humansofgithub;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



/**
 * Created by nitish on 10/20/2016.
 */
public class Userprofile extends Activity {

    public static final String TAG = Userprofile.class.getSimpleName();

    private Userprofiledata gitinfo;
    ImageView Userprofile_iv1;
    ProgressBar PG_1;
    TextView Userprofile_tv1,Tv_bio,Tv_bio2,Tv_location,Tv_location2,Tv_blog,Tv_blog2,Tv_email,Tv_email2,Tv_publicrepos,Tv_publicrepos2
            ,Tv_followers,Tv_followers2,Tv_following,Tv_following2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        String final_url = bundle.getString("url");

        Userprofile_iv1 = (ImageView) findViewById(R.id.user_profile_iv1);
        PG_1 = (ProgressBar) findViewById(R.id.pg_1);
        Userprofile_tv1 = (TextView) findViewById(R.id.tv_1);

        Tv_bio = (TextView) findViewById(R.id.tv_bio);
        Tv_bio2 = (TextView) findViewById(R.id.tv_bio2);

        Tv_email = (TextView) findViewById(R.id.tv_email);
        Tv_email2 = (TextView) findViewById(R.id.tv_email2);

        Tv_blog = (TextView) findViewById(R.id.tv_blog);
        Tv_blog2 = (TextView) findViewById(R.id.tv_blog2);

        Tv_location = (TextView) findViewById(R.id.tv_location);
        Tv_location2 = (TextView) findViewById(R.id.tv_location2);


        Tv_followers = (TextView) findViewById(R.id.tv_followers);
        Tv_followers2 = (TextView) findViewById(R.id.tv_followers2);

        Tv_following = (TextView) findViewById(R.id.tv_following);
        Tv_following2 = (TextView) findViewById(R.id.tv_following2);

        Tv_publicrepos = (TextView) findViewById(R.id.tv_publicrepos);
        Tv_publicrepos2 = (TextView) findViewById(R.id.tv_publicrepos2);

        //Toast.makeText(Userprofile.this,final_url,Toast.LENGTH_LONG).show();

        getForecast(final_url);
        //Log.d(TAG, "Main UI code is running!");

    }

    private void getForecast(String final_url) {

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(final_url)
                    .build();

            Call call = client.newCall(request);


            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {


                }

                @Override
                public void onResponse(Response response) throws IOException {


                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            gitinfo = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void updateDisplay() {


        String img_url = gitinfo.getmIcon();

        String Name = gitinfo.getName();
        String Bio = gitinfo.getBio();
        String Blog = gitinfo.getBlog();
        String Email = gitinfo.getEmail();
        String Location = gitinfo.getLocation();
        Integer Followers = gitinfo.getFollowers();
        Integer Following = gitinfo.getFollowing();
        Integer Publicrepos = gitinfo.getRepos();

        Picasso.with(Userprofile.this).load(img_url).into(Userprofile_iv1);
        PG_1.setVisibility(View.GONE);

        Userprofile_tv1.setText(Name);
        Tv_bio2.setText(Bio);
        Tv_location2.setText(Location);
        Tv_email2.setText(Email);
        Tv_blog2.setText(Blog);
        Tv_following2.setText(Followers.toString());
        Tv_followers2.setText(Following.toString());
        Tv_publicrepos2.setText(Publicrepos.toString());
    }



    private Userprofiledata getCurrentDetails(String jsonData) throws JSONException {


        JSONObject currently = new JSONObject(jsonData);

        Userprofiledata currentWeather = new Userprofiledata();

        currentWeather.setmIcon(currently.getString("avatar_url"));
        currentWeather.setName(currently.getString("name"));
        currentWeather.setBio(currently.getString("bio"));
        currentWeather.setLocation(currently.getString("location"));
        currentWeather.setBlog(currently.getString("blog"));
        currentWeather.setFollowers(currently.getInt("followers"));
        currentWeather.setEmail(currently.getString("email"));
        currentWeather.setFollowing(currently.getInt("following"));
        currentWeather.setRepos(currently.getInt("public_repos"));

        return currentWeather;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
