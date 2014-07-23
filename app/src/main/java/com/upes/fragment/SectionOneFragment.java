package com.upes.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.poppyview.PoppyViewHelper;
import com.upes.csi.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SectionOneFragment extends Fragment {

    private View rootView;
    private static final String TAG = SectionOneFragment.class.getSimpleName();
    private ListView listView;
    private String URL = "http://www.upescsi.in/events/";
    private PoppyViewHelper mPoppyViewHelper;
    private View poppyview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> eventTitle;
    private ArrayList<String> eventSummary;
    private ArrayList<String> eventImageUrl;
    private View row;
    private ImageView iv;
    private TextView tv;
    private TextView tv2;
    private Document document;
    private Bitmap bitmap;
    int count;
    int i;

    private OnFragmentInteractionListener mListener;

    public SectionOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_section_one, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);





        eventTitle = new ArrayList<String>();

        //Getting class usage counts
        //new Count().execute();
        Toast.makeText(getActivity(), ""+count, Toast.LENGTH_SHORT).show();

        //new EventTitle().execute();
        eventTitle.add("asdf");
        eventTitle.add("qwert");

        //listView.setAdapter(adapter);
        adapter = new EventsAdapter(getActivity(), android.R.layout.simple_list_item_1, eventTitle);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // Set up the poppy view
        //mPoppyViewHelper = new PoppyViewHelper(getActivity(), PoppyViewHelper.PoppyViewPosition.TOP);
        //poppyview = mPoppyViewHelper.createPoppyViewOnListView(android.R.id.list, R.layout.poppyview_actionbar);

        //return rootView;
        return null;
    }

    //Getting item counts
    private class Count extends AsyncTask<Void, Void, Void> {
        Elements elements;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //count = 0;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Getting ref html
            try {
                document = Jsoup.connect(URL).get();
                // Using elements to get class data
                elements = document.select("a[class=cbp-vm-image] img[src]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            count = elements.size();
            //Fetching array variables (image url, title array)
            /*for(i=0; i<count; i++) {
                //new Image().execute();
                new EventTitle().execute();
            }*/

        }
    }

    //Image asyncTask
    private class Image extends AsyncTask<Void, Void, Void> {
        String imgSrc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Getting ref html
            try {
                document = Jsoup.connect(URL).get();
                // Using elements to get class data
                Elements imgClass = document.select("a[class=cbp-vm-image] img[src]");
                //Selecting element at specific position
                Element classElement = imgClass.get(i);
                //Locating src attribute
                imgSrc = classElement.attr("src");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //eventImageUrl.add(i, imgSrc);
        }
    }

    //Event Title asyncTask
    private class EventTitle extends AsyncTask<Void, Void, Void> {
        String titleSrc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Getting ref html
            try {
                document = Jsoup.connect(URL).get();
                // Using elements to get class data
                Elements titleClass = document.select("h3[class=cbp-vm-title]");
                //Selecting element at specific position
                for(Element element : titleClass) {

                    titleSrc = element.text();
                    eventTitle.add(titleSrc);
                    //adapter.add(titleSrc);
                    Log.d("Title", titleSrc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //eventTitle.add(i, titleSrc);
            //listView.deferNotifyDataSetChanged();
            adapter.notifyDataSetChanged();
            adapter.addAll(eventTitle);
        }
    }

    //Adapter class
    private class EventsAdapter extends ArrayAdapter<String> {

        public EventsAdapter(Context context, int resource, ArrayList<String> item) {
            super(context, resource, item);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.events_list_item, parent, false);
            iv = (ImageView) row.findViewById(R.id.imageView1);
            tv = (TextView) row.findViewById(R.id.textView1);

            //Download image from URL at position
            /*String imgURL = eventImageUrl.get(position);
            try {
                InputStream inputStream = new java.net.URL(URL+imgURL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv.setImageBitmap(bitmap);*/

            //Setting event title
            String title = eventTitle.get(position);
            tv.setText(title);

            return row;
        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up the poppy view
        mPoppyViewHelper = new PoppyViewHelper(getActivity());
        //poppyview = mPoppyViewHelper.createPoppyViewOnListView(android.R.id.list, R.layout.poppyview_actionbar);
    }
}
