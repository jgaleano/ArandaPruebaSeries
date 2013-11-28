package com.aranda.pruebatecnica.imdb.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.aranda.pruebatecnica.imdb.R;
import com.aranda.pruebatecnica.imdb.parser.SerieObjParser;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;
import com.aranda.pruebatecnica.imdb.util.ConsumerWebServices;
import com.aranda.pruebatecnica.interfaces.InterfaceActivityAsyncTask;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Clase asincrona que llama al servicio de series en un hilo diferente al de la UI
 * @author Julio Galeano
 *
 */
public class SearchServiceAsync extends AsyncTask<Void, Void, List<SerieObj>>
{
	private String searchCriteria;
	int limitItemsToPage;
	int offset;
	private InterfaceActivityAsyncTask parentListener;
	private Context context;

	/**
	 * Inicializa el objeto
	 * @param context Contexto de la aplicacion
	 * @param searchCriteria Criterio de busqueda
	 * @param limitItemsToPage Limite de resultados por pagina
	 * @param offset 
	 * @param parentListener Callback cuando responda el servicio
	 */
	public SearchServiceAsync(Context context, String searchCriteria, int limitItemsToPage, int offset, InterfaceActivityAsyncTask parentListener)
	{
		this.context = context;
		this.searchCriteria = searchCriteria;
		this.limitItemsToPage = limitItemsToPage;
		this.offset = offset;
		this.parentListener = parentListener;
	}

	@Override
	 protected List<SerieObj> doInBackground(Void... arg0)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				
		params.add(new BasicNameValuePair(context.getString(R.string.type_param), context.getString(R.string.type_value)));
		params.add(new BasicNameValuePair(context.getString(R.string.title_param), searchCriteria));
		params.add(new BasicNameValuePair(context.getString(R.string.limit_param), limitItemsToPage+""));
		params.add(new BasicNameValuePair(context.getString(R.string.offset), offset+""));

		try
		{
			 JSONObject obj = ConsumerWebServices.makeHttpRequest(context.getString(R.string.url_search_service),"GET", params);
			 return (new SerieObjParser()).serieObjParser(context, obj);
			 
		} catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	 protected void onPostExecute(List<SerieObj> result)
	{
		parentListener.onFinishedAsyncTask(result);
	}

}
