package br.com.mobilelearning.youtube;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainYoutube extends YouTubeBaseActivity {

	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YouTubePlayerView youTubeView = new YouTubePlayerView(this);
		
		Intent it = getIntent();

		if (it != null) {
			Bundle params = it.getExtras();
			if (params != null) {
				url = params.getString("url");
			}
		}

		setContentView(youTubeView);
		youTubeView.initialize("AIzaSyAtymR1EwlYnXlShcg9w1-yjzOTDVQqlPQ",
				new OnInitializedListener() {

					@Override
					public void onInitializationFailure(Provider provider,
							YouTubeInitializationResult result) {
					}

					@Override
					public void onInitializationSuccess(Provider provider,
							YouTubePlayer player, boolean wasRestored) {
						if (!wasRestored) {
							//player.cueVideo("9bZkp7q19f0");
							player.cueVideo(url);
						}
					}
				});
	}  
	
	
}
