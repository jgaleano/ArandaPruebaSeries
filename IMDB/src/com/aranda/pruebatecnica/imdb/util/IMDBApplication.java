package com.aranda.pruebatecnica.imdb.util;

import com.aranda.pruebatecnica.imdb.pojos.SerieObj;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Clase tipo Singleton que tiene informacion que comparten las pantallas o debe estar en el contexto de la aplicacion
 * @author Julio Galeano
 *
 */
public class IMDBApplication extends Application
{
	private SerieObj obj;
	private int quantityRequests;

	/**
	 * Verifica la conexion a red.
	 * 0 Not connection
	 * 1. Connection by 3g
	 * 2. Connection by WIFI
	 * */
	public int verifyConnection()
	{
		ConnectivityManager manager;
		NetworkInfo networkInfo = null;

		try
		{
			manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			
			
			//Codigo de prueba
			NetworkInfo[] info = manager.getAllNetworkInfo();
	        if (info != null) {
	            for (int i = 0; i < info.length; i++) {
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                    Log.w("Conexion", "Tipo: "+info[i].getTypeName()+" - InfoExtra: "+info[i].getExtraInfo()+" - SubTipo: "+info[i].getSubtype());
	                }
	            }
	        }
			
			networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//For 3G check
			boolean is3g=false;
			if(networkInfo!=null)
				is3g = networkInfo.isConnectedOrConnecting();
			else
				Log.w("Conexion", "Validacion 3G - NetworkInfo NULL");

			networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			//For WiFi Check
			boolean isWifi=false;
			if(networkInfo!=null)
				isWifi = networkInfo.isConnectedOrConnecting();
			else
				Log.w("Conexion", "Validacion WIFI - NetworkInfo NULL");

			if(is3g)
			{
				return 1;
			}
			else if (isWifi) 
			{ 
				return 2;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @return the obj
	 */
	public SerieObj getObj() {
		return obj;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(SerieObj obj) {
		this.obj = obj;
	}

	/**
	 * @return the quantityRequests
	 */
	public int getQuantityRequests() {
		return quantityRequests;
	}

	/**
	 * @param quantityRequests the quantityRequests to set
	 */
	public void setQuantityRequests(int quantityRequests) {
		this.quantityRequests = quantityRequests;
	}
}
