package br.com.mobilelearning;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import br.com.mobilelearning.util.SampleList;
import com.actionbarsherlock.app.SherlockActivity;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import br.com.mobilelearning.youtube.ChannelYoutube;


public class MainDetails extends SherlockActivity implements
		OnItemClickListener {

	private String urls[] = {
			"http://farm3.staticflickr.com/2058/2354207421_35c3d20d73.jpg",
			"http://farm3.staticflickr.com/2563/3909054081_cf51bcf803_n.jpg",
			"http://www3.eca.usp.br/sites/default/files/u276/blogging-image.jpg" };

	private String titulo;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(SampleList.THEME);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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

		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the
													// sides as well

		StaggeredAdapter adapter = new StaggeredAdapter(MainDetails.this,
				R.id.imageView1, urls);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		
		Intent it = new Intent(this,ChannelYoutube.class);
		it.putExtra("titulo", titulo);		
		startActivity(it);
		
		
	}
	

}
