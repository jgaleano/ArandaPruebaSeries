package com.aranda.pruebatecnica.imdb.adapters;

import java.util.List;

import com.aranda.pruebatecnica.imdb.pojos.EpisodesObj;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author Julio Galeano
 *
 * Define la vista y comportamiento de cada capitulo de una temporada.
 */
public class EpisodesAdapter extends ArrayAdapter<EpisodesObj> 
{
	private LayoutInflater inflater;
	
	/**
	 * Constructor. Inicializa el objeto.
	 * @param context Contexto de la aplicacion
	 * @param episodesList Lista de los episodios de una temporada
	 */
	public EpisodesAdapter(Context context, List<EpisodesObj> episodesList) 
	{
		super(context, android.R.layout.simple_list_item_1, episodesList);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		
		((TextView)convertView).setText( getItem(position).getEpisode() +" "+getItem(position).getTitle());
		return convertView;
	}
}
