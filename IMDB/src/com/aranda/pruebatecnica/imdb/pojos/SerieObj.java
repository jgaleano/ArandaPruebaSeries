package com.aranda.pruebatecnica.imdb.pojos;

import java.util.List;

import android.graphics.Bitmap;

/**
 * Objeto que contiene la informacion de una serie.
 * @author Julio Galeano
 *
 */
public class SerieObj
{
	private String title="";
	private String imgUrl="";
	private Bitmap imgBmp;
	private List<String> genders;
	private List<String> actors;
	private List<List<EpisodesObj>> seassonsAndEpisodes;

	/**
	 * @return the imgBmp
	 */
	public Bitmap getImgBmp() {
		return imgBmp;
	}
	/**
	 * @param imgBmp the imgBmp to set
	 */
	public void setImgBmp(Bitmap imgBmp) {
		this.imgBmp = imgBmp;
	}
	/**
	 * @return the name
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param name the name to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	/**
	 * @return the genders
	 */
	public List<String> getGenders() {
		return genders;
	}
	/**
	 * @param genders the genders to set
	 */
	public void setGenders(List<String> genders) {
		this.genders = genders;
	}
	/**
	 * @return the actors
	 */
	public List<String> getActors() {
		return actors;
	}
	/**
	 * @param actors the actors to set
	 */
	public void setActors(List<String> actors) {
		this.actors = actors;
	}
	/**
	 * @return the seassonsAndEpisodes
	 */
	public List<List<EpisodesObj>> getSeassonsAndEpisodes() {
		return seassonsAndEpisodes;
	}
	/**
	 * @param seassonsAndEpisodes the seassonsAndEpisodes to set
	 */
	public void setSeassonsAndEpisodes(List<List<EpisodesObj>> seassonsAndEpisodes) {
		this.seassonsAndEpisodes = seassonsAndEpisodes;
	}
}
