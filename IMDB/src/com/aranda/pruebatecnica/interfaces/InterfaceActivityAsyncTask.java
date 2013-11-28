package com.aranda.pruebatecnica.interfaces;

import java.util.List;

import com.aranda.pruebatecnica.imdb.pojos.SerieObj;

/**
 * Interface que se debe implementar cuando se llama a un servicio con una clase asincrona
 * @author Julio Galeano
 *
 */
public interface InterfaceActivityAsyncTask 
{
	public void onFinishedAsyncTask(List<SerieObj> series);
}
