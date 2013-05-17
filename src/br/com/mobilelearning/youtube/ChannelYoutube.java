package br.com.mobilelearning.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import br.com.mobilelearning.R;
import br.com.mobilelearning.util.SampleList;

import com.actionbarsherlock.app.SherlockActivity;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChannelYoutube extends SherlockActivity {
	
	
		private VideosListView listView;
		private String titulo;
		private String assunto;

		/** Called when the activity is first created. */
	    @SuppressLint("NewApi")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	    	setTheme(SampleList.THEME);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.youtube_channel);
	        
	        Intent it = getIntent();

			if (it != null) {
				Bundle params = it.getExtras();
				if (params != null) {
					titulo = params.getString("titulo");
				}
			}

			getSupportActionBar().setTitle(titulo);
	        
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
						R.drawable.bg_striped_img);
				bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
				getSupportActionBar().setBackgroundDrawable(bg);
			} else {
				ActionBar bar = getActionBar();
				bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(51, 51, 51)));
			}
			      
	        
	        listView = (VideosListView) findViewById(R.id.videosListView);
	        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int posicao,long id) {
				
					final Video v = (Video) parent.getAdapter().getItem(posicao);
					Intent it = new Intent(getApplicationContext(),MainYoutube.class);
					it.putExtra("url", v.getUrl().substring(v.getUrl().indexOf("=") + 1));
					startActivity(it);
				}
	        	
	        	
				
	        	
			});
	        
	        
	        
	        if(titulo.equals("Esportes")){
	        	assunto = "StuntsAmazing1";
	        }else if (titulo.equals("Biologia")){
	        	assunto = "jubilut";
	        }else if (titulo.equals("Religião")){
	        	assunto = "VerdadeGospel";
	        }
	        
	        new GetYouTubeUserVideosTask(assunto).execute();
	    }

	   
		
		class GetYouTubeUserVideosTask extends AsyncTask<Void, Void, Void> {
			// A reference to retrieve the data when this task finishes
			public static final String LIBRARY = "Library";
			// A handler that will be notified when the task is finished
			// The user we are querying on YouTube for videos
			private final String username;
			private ProgressDialog dialog;
			private List<Video> videos;

			/**
			 * Don't forget to call run(); to start this task
			 * @param replyTo - the handler you want to receive the response when this task has finished
			 * @param username - the username of who on YouTube you are browsing
			 */
			public GetYouTubeUserVideosTask(String username) {
				this.username = username;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = ProgressDialog.show(ChannelYoutube.this, "Aguarde",
						"Procurando....");
			}

			
			@Override
			protected Void doInBackground(Void... arg0) {
				
				try {
				
					// Get a httpclient to talk to the internet
					HttpClient client = new DefaultHttpClient();
					// Perform a GET request to YouTube for a JSON list of all the videos by a specific user
					HttpUriRequest request = new HttpGet("https://gdata.youtube.com/feeds/api/videos?author="+username+"&v=2&alt=jsonc");
					// Get the response that YouTube sends back
					HttpResponse response = client.execute(request);
					// Convert this response into a readable string
					String jsonString = StreamUtils.convertToString(response.getEntity().getContent());
					// Create a JSON object that we can use from the String
					JSONObject json = new JSONObject(jsonString);

					// For further information about the syntax of this request and JSON-C
					// see the documentation on YouTube http://code.google.com/apis/youtube/2.0/developers_guide_jsonc.html

					// Get are search result items
					JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

					// Create a list to store are videos in
					videos = new ArrayList<Video>();
					// Loop round our JSON list of videos creating Video objects to use within our app
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						// The title of the video
						String title = jsonObject.getString("title");
						// The url link back to YouTube, this checks if it has a mobile url
						// if it doesnt it gets the standard url
						String url;
						try {
							url = jsonObject.getJSONObject("player").getString("mobile");
						} catch (JSONException ignore) {
							url = jsonObject.getJSONObject("player").getString("default");
						}
						// A url to the thumbnail image of the video
						// We will use this later to get an image using a Custom ImageView
						// Found here http://blog.blundell-apps.com/imageview-with-loading-spinner/
						String thumbUrl = jsonObject.getJSONObject("thumbnail").getString("sqDefault");
						
						// Create the video object and add it to our list
						videos.add(new Video(title, url, thumbUrl));
					}
				
					
					Log.i("teste",  "size = " + videos.size());

					
			
				// We don't do any error catching, just nothing will happen if this task falls over
				// an idea would be to reply to the handler with a different message so your Activity can act accordingly
				} catch (ClientProtocolException e) {
					Log.i("Feck", "w");
				} catch (IOException e) {
					Log.i("Feck", "b");
				} catch (JSONException e) {
					Log.i("Feck", "j");
				}
				
				
				
				return null;
			}
			
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				listView.setVideos(videos);
				dialog.dismiss();
			}
		
		
		
		}



		
		
		
		
		
		
		
}	