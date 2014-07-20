package com.upes.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.upes.csi.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class SectionTwoFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private ImageView iv;
    private TextView tv;
    String URL = "http://www.upescsi.in/events/";

    private OnFragmentInteractionListener mListener;

    public SectionTwoFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_section_two, container, false);
        iv = (ImageView) rootView.findViewById(R.id.imageView1);
        //iv.setImageResource(R.drawable.csi_logo);
        tv = (TextView) rootView.findViewById(R.id.textView1);
        tv.setText("Testing");
        new Image().execute();
        return rootView;
    }

    //Image AsyncTask
    private class Image extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iv.setImageResource(R.drawable.csi_logo);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //Connect to the website
                Document document = Jsoup.connect(URL).get();
                // Using elements to get class data
                Elements img2 = document.select("a[class=cbp-vm-image] img[src]");
                Element img = img2.get(2);
                // Locate the src attribute
                String imgSrc = img.attr("src");
                // Download image from the url
                InputStream inputStream = new java.net.URL(URL+imgSrc).openStream();
                //Decode into bitmap
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Setting downloaded image into imageView
            iv.setImageBitmap(bitmap);
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

}
