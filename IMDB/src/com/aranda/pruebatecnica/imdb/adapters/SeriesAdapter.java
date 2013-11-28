package com.aranda.pruebatecnica.imdb.adapters;

import java.util.List;

import com.aranda.pruebatecnica.imdb.R;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Define la vista y comportamiento de cada serie.
 * @author Julio Galeano
 *
 */
public class SeriesAdapter extends ArrayAdapter<SerieObj> 
{
	private LayoutInflater inflater;
	
	/**
	 * Constructor. Inicializa el objeto.
	 * @param context Contexto de la aplicacion
	 * @param seriesList Lista de las series que retorna el servicio
	 */
	public SeriesAdapter(Context context, List<SerieObj> seriesList) 
	{
		super(context, R.layout.series_adapter_item, seriesList);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
			convertView = inflater.inflate(R.layout.series_adapter_item, parent, false);
		TextView txtItemSerie = (TextView) convertView.findViewById(R.id.txt_item_serie);
		ImageView imgItemSerie = (ImageView) convertView.findViewById(R.id.img_item_serie);
		
		txtItemSerie.setText(getItem(position).getTitle());
		if(getItem(position).getImgBmp()!=null)
			imgItemSerie.setImageBitmap(getItem(position).getImgBmp());
		else
			imgItemSerie.setImageResource(R.drawable.ic_launcher);
		
		
		return convertView;
	}

}
