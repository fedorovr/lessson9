package ru.csc.roman_fedorov.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CitiesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private OnCityQuiredListener mListener;
    private MyCursorAdapter adapter;

    public CitiesListFragment() {
        // Required empty public constructor
    }

    public static CitiesListFragment newInstance() {
        CitiesListFragment fragment = new CitiesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cities_list, container, false);

        final Context context = getContext();
        Button queryButton = (Button) view.findViewById(R.id.query_city_button);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cityET = (EditText) view.findViewById(R.id.input_city);
                String queryUrl = MainActivity.getQuery(cityET.getText().toString());
                new DownloadWeatherData().execute(context, queryUrl);
            }
        });

        adapter = new MyCursorAdapter(context, null, 0);
        final ListView citiesLV = (ListView) view.findViewById(R.id.cities_list);
        citiesLV.setAdapter(adapter);
        citiesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                mListener.onQuire(c.getString(c.getColumnIndex(WeatherTable.WEATHER_CITY)));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCityQuiredListener) {
            mListener = (OnCityQuiredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCityQuiredListener {
        void onQuire(String cityName);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("TAGGG", "onCreateLoader in list, id is  " + String.valueOf(id));
        return new CursorLoader(getContext(), MainActivity.DISTINCT_VALUES_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
