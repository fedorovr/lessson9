package ru.csc.roman_fedorov.weatherapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CityDetailFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;
    private MyCursorAdapter adapter;
    private String city;

    public CityDetailFragment() {
        // Required empty public constructor
    }

    public static CityDetailFragment newInstance() {
        CityDetailFragment fragment = new CityDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        city = getArguments().getString(MainActivity.QUERIED_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_detail, container, false);

        final ListView citiesLV = (ListView) view.findViewById(R.id.detailed_weather_list);
        displayCityDetails();
        citiesLV.setAdapter(adapter);

        return view;
    }

    public void displayCityDetails() {
        android.support.v4.app.LoaderManager lm = getLoaderManager();//.initLoader(10, null, this);
        Log.d("TAGGG", "displayCityDetails: " + lm.toString());
        lm.initLoader(0, null, this);
        adapter = new MyCursorAdapter(getContext(), null, 0);
        Log.d("TAGGG", "displayCityDetails, number of elements is : " + String.valueOf(adapter.getCount()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String mSelectionClause = WeatherTable.WEATHER_CITY + " LIKE ?";
        String[] mSelectionArgs = {city};

        Log.d("TAGGG", "Created loader for " + city);
        CursorLoader cl = new CursorLoader(getContext(), DetailWeatherActivity.CITY_VALUES_URI, null, mSelectionClause, mSelectionArgs, null);
        return cl;
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
