package br.com.mobilelearning.youtube;

import java.io.Serializable;
import java.util.List;
import br.com.mobilelearning.youtube.Video;

@SuppressWarnings("serial")
public class Library implements Serializable{
	// The username of the owner of the library
	private String user;
	// A list of videos that the user owns
	private List<Video> videos;

	public Library(String user, List<Video> videos) {
		this.user = user;
		this.videos = videos;
	}

	/**
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the videos
	 */
	public List<Video> getVideos() {
		return videos;
	}
}