package com.aranda.pruebatecnica.imdb.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.aranda.pruebatecnica.imdb.R;
import com.aranda.pruebatecnica.imdb.pojos.EpisodesObj;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;

/**
 * Clase que hace la interpretacion de la respuesta JSON a objetos
 * @author Julio Galeano
 *
 */
public class SerieObjParser 
{
	private Context context;

	/**
	 * Metodo principal de la interpretacion de JSON a objetos
	 * @param context Contexto de la aplicacion
	 * @param obj Objeto JSON respuesta del servicio.
	 * @return Lista de objetos tipo SerieObj que corresponde a cada serie y su detalle, retornada por el servicio.
	 * @throws JSONException
	 */
	public List<SerieObj> serieObjParser(Context context, JSONObject obj) throws JSONException
	{
		this.context = context;
		List<SerieObj> series = new ArrayList<SerieObj>();

		if(!obj.has(context.getString(R.string.code)))
		{
			JSONArray jsonArray;
			if(obj.has(context.getString(R.string.responseArray)))
				jsonArray = obj.getJSONArray(context.getString(R.string.responseArray));
			else
				jsonArray = obj.getJSONArray(context.getString(R.string.result));

			for(int i=0; i<jsonArray.length(); i++)
			{
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				SerieObj serieObj = new SerieObj();
				serieObj.setTitle(jsonObj.getString(context.getString(R.string.title)));
				if(jsonObj.has(context.getString(R.string.poster)))
					serieObj.setImgUrl(jsonObj.getJSONObject(context.getString(R.string.poster)).getString(context.getString(R.string.imdb)));
				if(jsonObj.has(context.getString(R.string.genres)))
					serieObj.setGenders(parseArrayStrings(jsonObj.getJSONArray(context.getString(R.string.genres))));
				if(jsonObj.has(context.getString(R.string.actors)))
					serieObj.setActors(parseArrayStrings(jsonObj.getJSONArray(context.getString(R.string.actors))));
				if(jsonObj.has(context.getString(R.string.episodes)))
					serieObj.setSeassonsAndEpisodes(parseSeassonsAndEpisodes(jsonObj.getJSONArray(context.getString(R.string.episodes))));
				series.add(serieObj);
			}
		}
		return series;
	}

	/**
	 * Interpreta las listas JSON compuestas por strings
	 * @param jsonArray Array JSON compuesto por strings
	 * @return Lista de cadenas de string
	 * @throws JSONException
	 */
	private List<String> parseArrayStrings(JSONArray jsonArray) throws JSONException
	{
		List<String> listString = new ArrayList<String>();
		for(int i=0; i<jsonArray.length(); i++)
		{
			listString.add(jsonArray.getString(i));
		}
		return listString;
	}

	/**
	 * Realiza la interpretacion y clasificacion de temporadas con sus respectivos episodios 
	 * @param jsonArray Array JSON de episodios retornado por el servicio.
	 * @return Lista de temporadas con su respectiva lista de episodios.
	 * @throws JSONException
	 */
	private List<List<EpisodesObj>> parseSeassonsAndEpisodes(JSONArray jsonArray) throws JSONException
	{
		List<List<EpisodesObj>> seassonsAndEpisodes = new ArrayList<List<EpisodesObj>>();

		List<EpisodesObj> episodesList = new ArrayList<EpisodesObj>();
		for(int i=0; i<jsonArray.length(); i++)
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			EpisodesObj episodesObj = new EpisodesObj();
			if(jsonObj.has(context.getString(R.string.title)))
				episodesObj.setTitle(jsonObj.getString(context.getString(R.string.title)));
			if(jsonObj.has(context.getString(R.string.episode)))
				episodesObj.setEpisode(jsonObj.getInt(context.getString(R.string.episode)));
			if(jsonObj.has(context.getString(R.string.seasson)))
				episodesObj.setSeasson(jsonObj.getInt(context.getString(R.string.seasson)));
			episodesList.add(episodesObj);
		}

		Collections.sort(episodesList, new Comparator<EpisodesObj>(){
			public int compare(EpisodesObj emp1, EpisodesObj emp2) 
			{
				Integer emp1Int = Integer.valueOf(emp1.getSeasson());
				Integer emp2Int = Integer.valueOf(emp2.getSeasson());
				return emp1Int.compareTo(emp2Int);
			}
		});

		Collections.sort(episodesList, new Comparator<EpisodesObj>(){
			public int compare(EpisodesObj emp1, EpisodesObj emp2) 
			{
				if(emp1.getSeasson() == emp2.getSeasson())
				{
					Integer emp1Int = Integer.valueOf(emp1.getEpisode());
					Integer emp2Int = Integer.valueOf(emp2.getEpisode());
					return emp1Int.compareTo(emp2Int);
				}
				return 0;
			}
		});

		List<EpisodesObj> episodesForSeasson = new ArrayList<EpisodesObj>();
		for(int i=0; i< episodesList.size(); i++)
		{
			if(i==0)
			{
				episodesForSeasson.add(episodesList.get(i));
			}
			else if(episodesList.get(i).getSeasson()==episodesForSeasson.get(episodesForSeasson.size()-1).getSeasson())
			{
				episodesForSeasson.add(episodesList.get(i));
			}
			else
			{
				seassonsAndEpisodes.add(episodesForSeasson);
				episodesForSeasson = new ArrayList<EpisodesObj>();
				episodesForSeasson.add(episodesList.get(i));
			}
		}
		seassonsAndEpisodes.add(episodesForSeasson);


		return seassonsAndEpisodes;
	}
}
