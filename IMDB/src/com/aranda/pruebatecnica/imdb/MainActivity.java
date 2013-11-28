package com.aranda.pruebatecnica.imdb;

import java.util.ArrayList;
import java.util.List;

import com.aranda.pruebatecnica.imdb.adapters.SeriesAdapter;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;
import com.aranda.pruebatecnica.imdb.services.AsyncImageLoader;
import com.aranda.pruebatecnica.imdb.services.SearchServiceAsync;
import com.aranda.pruebatecnica.imdb.util.IMDBApplication;
import com.aranda.pruebatecnica.interfaces.InterfaceActivityAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Julio Galeano
 * El la clase principal donde abre la aplicaci—n.
 */

public class MainActivity extends ActionBarActivity implements InterfaceActivityAsyncTask, OnItemClickListener, OnRefreshListener2 {

	private SearchView searchView;
	private List<SerieObj> seriesList = new ArrayList<SerieObj>();
	private SeriesAdapter seriesAdapter;
	private IMDBApplication app;
	private ProgressDialog progressDialog;
	private boolean newSearchFlag = false;
	private PullToRefreshGridView mPullRefreshGridView;
	private TextView txtMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		app = (IMDBApplication) getApplication();
		
		txtMain = (TextView) findViewById(R.id.txt_main);

		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gridview_series);
		mPullRefreshGridView.setOnRefreshListener(this);
		seriesAdapter = new SeriesAdapter(getApplicationContext(), seriesList);
		mPullRefreshGridView.setMode(Mode.PULL_UP_TO_REFRESH);
		mPullRefreshGridView.getRefreshableView().setAdapter(seriesAdapter);
		mPullRefreshGridView.getRefreshableView().setOnItemClickListener(this);
	}
	
	
	/**
	 *  El metodo configura la barra de busqueda. Se define el listener para la busqueda.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getResources().getString(R.string.buscar_titulo_pelicula));
		traverseView(searchView, 0);

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) 
			{
				if(!arg0.contains("#") && !arg0.contains("@"))
				{
					newSearchFlag=true;
					if(app.verifyConnection()!=0)
					{
						progressDialog = ProgressDialog.show(MainActivity.this, "", "Buscando...");
						SearchServiceAsync service = new SearchServiceAsync(getApplicationContext(), arg0, 9, 0, MainActivity.this);
						service.execute();
						InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
					}
					else
						Toast.makeText(getApplicationContext(), "El m—vil esta sin conexi—n a internet. Contecte el celular a internet e intente de nuevo", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(getApplicationContext(), "No se aceptan caracteres # o @. Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) 
			{
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Se define para dispositivos de diferentes versiones, este mantiene el dise–o y colores de la barra de busqueda.
	 * @param view
	 * @param index
	 */
	private void traverseView(View view, int index) {
		if (view instanceof SearchView) {
			SearchView v = (SearchView) view;
			for (int i = 0; i < v.getChildCount(); i++) {
				traverseView(v.getChildAt(i), i);
			}
		} else if (view instanceof LinearLayout) {
			LinearLayout ll = (LinearLayout) view;
			for (int i = 0; i < ll.getChildCount(); i++) {
				traverseView(ll.getChildAt(i), i);
			}
		} else if (view instanceof EditText) {
			((EditText) view).setTextColor(Color.WHITE);
			((EditText) view).setHintTextColor(Color.WHITE);
		} else if (view instanceof TextView) {
			((TextView) view).setTextColor(Color.WHITE);
		} else if (view instanceof ImageView) {
		} else {
			Log.v("View Scout", "Undefined view type here...");
		}
	}

	
	@Override
	public void onFinishedAsyncTask(List<SerieObj> seriesList) 
	{
		progressDialog.dismiss();
		if(newSearchFlag)
		{
			if(seriesList.size()==0)
			{
				Toast.makeText(getApplicationContext(), "No hay resultados para esta busqueda", Toast.LENGTH_SHORT).show();
				return;
			}
			txtMain.setVisibility(View.GONE);
			this.seriesList.clear();
			app.setQuantityRequests(9);
		}
		else
			app.setQuantityRequests(app.getQuantityRequests()+9);
		
		if(seriesList.size()==0)
		{
			Toast.makeText(getApplicationContext(), "No hay mas resultados para esta busqueda", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.seriesList.addAll(seriesList);
		seriesAdapter.notifyDataSetChanged();
		mPullRefreshGridView.onRefreshComplete();
		AsyncImageLoader imageLoader = new AsyncImageLoader(this.seriesList, seriesAdapter);
		imageLoader.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		app.setObj(seriesList.get(position));
		Intent i = new Intent(getApplicationContext(), DetailActivity.class);
		startActivity(i);
	}

	@Override
	public void onPullDownToRefresh() 
	{	
	}

	@Override
	public void onPullUpToRefresh() 
	{
		if(app.verifyConnection()!=0)
		{
			newSearchFlag=false;
			SearchServiceAsync service = new SearchServiceAsync(getApplicationContext(), searchView.getQuery().toString(), 9, app.getQuantityRequests(), this);
			service.execute();
		}
		else
			Toast.makeText(getApplicationContext(), "El m—vil esta sin conexi—n a internet. Contecte el celular a internet e intente de nuevo", Toast.LENGTH_SHORT).show();
	}
}
