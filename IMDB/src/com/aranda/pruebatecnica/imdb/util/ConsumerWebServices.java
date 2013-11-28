package com.aranda.pruebatecnica.imdb.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.http.AndroidHttpClient;
import android.util.Log;

/**
 * Clase que realiza la conexion a servicios web
 * @author Julio Galeano
 *
 *
 */
public class ConsumerWebServices {

	/**
	 * 
	 * @param url Direccion URL del servicio
	 * @param method Metodo GET o POST de la peticion
	 * @param params Parametros a enviar al servicio
	 * @return JSONObject con el resultado del servicio
	 * @throws IOException
	 */
	public static JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) throws IOException 
	{
		AndroidHttpClient httpClient = null;
		JSONObject json = null;

		try 
		{
			// Building the request
			httpClient = AndroidHttpClient.newInstance("android");
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),5000);

			if (method.equals("POST")) 
			{
				// request method is POST
				HttpPost postRequest = new HttpPost();
				URI uri = new URI(url);
				postRequest.setURI(uri);
				postRequest.setEntity(new UrlEncodedFormEntity(params));
				// getting the response
				HttpResponse httpResponse = httpClient.execute(postRequest);

				final int statusCode = httpResponse.getStatusLine()
						.getStatusCode();

				// check the response if it's ok
				if (statusCode != HttpStatus.SC_OK) 
				{
					Log.d("SERVICE", "" + httpResponse.getStatusLine());
					throw new IOException("" + httpResponse.getStatusLine());
				}
				final HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity, HTTP.UTF_8);
				entity.consumeContent();
				httpClient.close();
				json = new JSONObject(response);

			} 
			else if (method.equals("GET")) 
			{
				// request method is GET
				HttpGet getRequest = new HttpGet();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				URI uri = new URI(url);
				getRequest.setURI(uri);

				// getting the response
				HttpResponse httpResponse = httpClient.execute(getRequest);
				final int statusCode = httpResponse.getStatusLine()
						.getStatusCode();

				// check the response if it's ok
				if (statusCode != HttpStatus.SC_OK) {
					throw new IOException("" + httpResponse.getStatusLine());
				}

				final HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity, HTTP.UTF_8);
				entity.consumeContent();
				httpClient.close();

				Object jsonObj = new JSONTokener(response).nextValue();
				if (jsonObj instanceof JSONObject)
				{
					json = new JSONObject(response);
				}
				else if (jsonObj instanceof JSONArray)
				{
					json = new JSONObject();
					json.put("responseArray", new JSONArray(response));
				}
			}
			return json;
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		finally 
		{
			if (httpClient != null)
				httpClient.close();
		}
	}
}
