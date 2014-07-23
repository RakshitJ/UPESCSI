package com.upes.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fourmob.poppyview.PoppyViewHelper;
import com.upes.csi.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class SectionThreeFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    private PoppyViewHelper mPoppyViewHelper;

    private View rootView;
    private View poppyView;
    private ListView listView;
    private ArrayList<String> eventTitle;
    private Document document;
    private String URL = "http://www.upescsi.in/events/";
    private ArrayAdapter<String> adapter;
    private View row;
    private TextView tv;
    private ImageView iv;

    public SectionThreeFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_section_three, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        eventTitle = new ArrayList<String>();
        adapter = new EventsAdapter(getActivity(), android.R.layout.simple_list_item_1, eventTitle);
        new EventTitle().execute();

        return rootView;
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
                for (Element element : titleClass) {

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
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Instantiating PoppyView
        mPoppyViewHelper = new PoppyViewHelper(getActivity(), PoppyViewHelper.PoppyViewPosition.TOP);
        poppyView = mPoppyViewHelper.createPoppyViewOnListView(R.id.list, R.layout.poppyview_actionbar);
        //Setting poppyView layout
        TextView tv = (TextView) poppyView.findViewById(R.id.poppy_title);
        tv.setText(R.string.title_section3);
        int tv_color = getResources().getColor(R.color.white);
        tv.setTextColor(tv_color);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
