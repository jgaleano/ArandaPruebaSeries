package com.aranda.pruebatecnica.imdb;

import com.aranda.pruebatecnica.imdb.adapters.EpisodesAdapter;
import com.aranda.pruebatecnica.imdb.pojos.SerieObj;
import com.aranda.pruebatecnica.imdb.util.IMDBApplication;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends FragmentActivity
{
	private SerieSeassonsAdapter mSerieSeassonsAdapter;
	private ViewPager mViewPager;
	private static SerieObj serieObj;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_serie);

		IMDBApplication app = (IMDBApplication) getApplication();
		serieObj = app.getObj();
		
		TextView titleSerie = (TextView) findViewById(R.id.txt_detail_name);
		TextView genderSerie = (TextView) findViewById(R.id.txt_detail_gender);
		TextView actorsSerie = (TextView) findViewById(R.id.txt_detail_actors);
		
		titleSerie.setText(serieObj.getTitle());
		genderSerie.setText(serieObj.getGenders().toString().replace("[", "").replace("]", ""));
		actorsSerie.setText(serieObj.getActors().toString().replace("[", "").replace("]", ""));

		mSerieSeassonsAdapter = new SerieSeassonsAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSerieSeassonsAdapter);
		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
	}

	/**
	 * 
	 * @author Julio Galeano
	 *
	 * Clase que define el adapter para las diferentes paginas de las diferentes temporadas de la serie
	 */
	public static class SerieSeassonsAdapter extends FragmentPagerAdapter 
	{
		public SerieSeassonsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) 
		{
			EpisodesListFragment fragment = new EpisodesListFragment();
			fragment.setSeasson(i);
			return fragment;
		}

		@Override
		public int getCount() {
			if(serieObj.getSeassonsAndEpisodes()!=null)
				return serieObj.getSeassonsAndEpisodes().size();
			return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) 
		{
			return "Temporada " + (position + 1);
		}
	}

	/**
	 * 
	 * @author Julio Galeano
	 *
	 * Define el Fragment o vista que tendr‡ cada una de las paginas de cada temporada de la serie
	 */
	public static class EpisodesListFragment extends ListFragment 
	{
		int position;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
		{
			EpisodesAdapter adapter = new EpisodesAdapter(getActivity().getApplicationContext(), serieObj.getSeassonsAndEpisodes().get(position));
			setListAdapter(adapter);
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		
		/**
		 * Recibe la temporada
		 * @param position
		 */
		public void setSeasson(int position)
		{
			this.position = position;
		}
	}
}
