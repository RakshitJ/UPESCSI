package com.upes.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.lang.ref.WeakReference;
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
    private View poppyView;
    int count;
    int i;

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
        rootView = inflater.inflate(R.layout.fragment_section_three, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        eventTitle = new ArrayList<String>();
        eventImageUrl = new ArrayList<String>();
        adapter = new EventsAdapter(getActivity(), android.R.layout.simple_list_item_1, eventTitle);
        new EventTitle().execute();

        return rootView;
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
                Elements titleElements = document.select("h3[class=cbp-vm-title]");
                //Getting another set of elements
                Elements imgElements = document.select("a[class=cbp-vm-image] img[src]");
                //Selecting element at specific position for event title
                for(Element element : titleElements) {

                    //Event title
                    titleSrc = element.text();
                    eventTitle.add(titleSrc);
                    //adapter.add(titleSrc);
                    Log.d("Title", titleSrc);
                }
                //storing images src in array
                for(Element imageElement : imgElements) {

                    //Images src
                    imgSrc = imageElement.attr("src");
                    eventImageUrl.add(imgSrc);
                    Log.d("Image src", imgSrc);
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

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private String data = null;

        public LoadImage(ImageView imageView) {
            //Using weak reference so that imageView can be garbage collected
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            data = strings[0];
            return decodeSampledBitmapFromURL(data, 100, 100);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap1) {
            if (isCancelled()) {
                bitmap1 = null;
            }

            if(imageViewWeakReference != null && bitmap1 !=null) {
                final ImageView imageView = imageViewWeakReference.get();
                final LoadImage bitmapWorkerTask = getBitmapWorkerTask(imageView);

                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap1);
                }
            }
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
            String imgURL = eventImageUrl.get(position);
            loadBitmap(imgURL, iv);

            //Setting event title
            String title = eventTitle.get(position);
            tv.setText(title);

            return row;
        }

    }

    public void loadBitmap(String imgURL, ImageView imageView) {

        if (cancelPotentialWork(imgURL, imageView)) {
            final LoadImage task = new LoadImage(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(imgURL, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imgURL);
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
        tv.setText(R.string.title_section1);
        int tv_color = getResources().getColor(R.color.white);
        tv.setTextColor(tv_color);
        ImageView iv = (ImageView) poppyView.findViewById(R.id.actionbar_overflow);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.global, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    /*
    * Loading bitmaps and Handling concurrency
    */

    //Calculating inSampleSize to decode bitmaps
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        //Getting raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth) {

            final int halfHeight = height/2;
            final int halfWidth = width/2;

            //Calculate the largest inSampleSize value that is power of 2 and keeps both
            //height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //First getting options (width and height) with inJustDecodeBounds true, then
    //decode again with new inSampleSize value and inJustDecodeBounds false

    public static Bitmap decodeSampledBitmapFromURL(String imgURL, int reqWidth, int reqHeight) {
        String URL = "http://www.upescsi.in/events/";
        Bitmap bitmap1 = null;

        //First decode with inJustDecodeBounds set to true to get dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            InputStream inputStream = new java.net.URL(URL+imgURL).openStream();
            bitmap1 = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Calculating the inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bimap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            InputStream inputStream = new java.net.URL(URL+imgURL).openStream();
            bitmap1 = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap1;
    }

    //Drawable subclass to store reference back to worker task
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<LoadImage> bitmapWorkerTaskReference;

        public AsyncDrawable(String imgURL, LoadImage bitmapWorkerTask) {
            //super(imgURL, bitmap1);
            bitmapWorkerTaskReference = new WeakReference<LoadImage>(bitmapWorkerTask);
        }

        public LoadImage getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    //Check if another running task is already associated with imageView
    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final LoadImage bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if(bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            //If the bitmap is not yet set or differs from new data
            if (bitmapData == null || bitmapData != data) {
                //Cancel previous task
                bitmapWorkerTask.cancel(true);
            }
            else {
                //The same work is already in progress
                return false;
            }
        }
        //No task is associated with the ImageView, or existing task was cancelled
        return true;
    }

    //Used to retrieve task associated with a particular imageView
    private static LoadImage getBitmapWorkerTask(ImageView imageView) {
        if(imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
