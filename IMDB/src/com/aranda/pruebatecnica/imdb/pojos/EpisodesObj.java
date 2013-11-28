package com.aranda.pruebatecnica.imdb.pojos;

/**
 * Objeto que tendra la informacion de un episodio.
 * @author Julio Galeano
 *
 */
public class EpisodesObj
{
	private int seasson;
	private int episode;
	private String title="";
	/**
	 * @return the seasson
	 */
	public int getSeasson() {
		return seasson;
	}
	/**
	 * @param seasson the seasson to set
	 */
	public void setSeasson(int seasson) {
		this.seasson = seasson;
	}
	/**
	 * @return the episode
	 */
	public int getEpisode() {
		return episode;
	}
	/**
	 * @param episode the episode to set
	 */
	public void setEpisode(int episode) {
		this.episode = episode;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
