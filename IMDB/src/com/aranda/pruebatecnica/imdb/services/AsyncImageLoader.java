package com.aranda.pruebatecnica.imdb.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.aranda.pruebatecnica.imdb.adapters.SeriesAdapter;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Clase asincrona que descarga las imagenes en un hilo diferente al de la UI
 * @author Julio Galeano
 *
 */
public class AsyncImageLoader extends AsyncTask<Void, Bitmap, Void>
{
	private List<Integer> aux;
	private List<SerieObj> listData;
	private SeriesAdapter adapter;

	/**
	 * Inicializa el objeto
	 * @param listData Lista de series
	 * @param adapter Adapter de la lista de episodios
	 */
	public AsyncImageLoader(List<SerieObj> listData, SeriesAdapter adapter)
	{
		this.listData = listData;
		this.adapter = adapter;
		aux = new ArrayList<Integer>();
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		Bitmap image=null;
		URL imageURL = null;
		HttpURLConnection conn;
		InputStream is;

		for (int i = 0; i < listData.size(); i++)
		{
			if(!isCancelled())
			{
				try
				{
					if(!listData.get(i).getImgUrl().equalsIgnoreCase("") &&  
							!listData.get(i).getImgUrl().equalsIgnoreCase("0") && 
							listData.get(i).getImgBmp()==null)
					{
						imageURL = new URL(listData.get(i).getImgUrl());
						conn = (HttpURLConnection) imageURL.openConnection();
						conn.setDoInput(true);
						conn.connect();
						is = conn.getInputStream();					
						image = BitmapFactory.decodeStream(is);
					}
					else if(listData.get(i).getImgBmp()!=null)
						image = listData.get(i).getImgBmp();
					aux.add(i);
					publishProgress(image);				
				} 
				catch (MalformedURLException e)
				{
					// URL DE IMAGEN INVALIDA
					Log.e("Errores", listData.get(i).getTitle()+" - No posee una imagen valida - "+listData.get(i).getImgUrl());
				}
				catch (IOException e)
				{
					Log.e("Errores",listData.get(i).getTitle()+" - Error descargando la imagen");
					e.printStackTrace();
				}
				catch (NullPointerException e)
				{
					Log.e("Errores",listData.get(i).getTitle()+"No posee una imagen");
					e.printStackTrace();
				}
				catch (OutOfMemoryError e)
				{
					Log.e("Errores",listData.get(i).getTitle()+"La imagen excede el tamaño");
					e.printStackTrace();
				}
			}
			else
			{
				i=listData.size();
				Log.d("Errores", "ImageLoader Cancel - Exit thread");
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Bitmap... bitmap)
	{
		super.onProgressUpdate(bitmap);
		try
		{
			listData.get(aux.get(0)).setImgBmp(bitmap[0]);
			adapter.notifyDataSetChanged();
			aux.remove(0);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}