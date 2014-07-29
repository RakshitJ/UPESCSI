package com.upes.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

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
    private LruCache<String, Bitmap> mMemoryCache;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private File cacheDir;
    private File cacheFile;
    private FileInputStream fileInputStream;
    private SmoothProgressBar poppyViewProgress;

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
        rootView = inflater.inflate(R.layout.fragment_section_one, container, false);

        //Create a path pointing to sys-recommended cache dir for app
        cacheDir = new File(getActivity().getCacheDir(), "thumbnails");

        //Getting maximum available VM memory
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //Using 1/8th of the available memory for this memory cache
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap1) {

                //The cache size will be measured in kb rather than number of items
                return bitmap1.getByteCount() / 1024;
            }
        };

        //RelativeLayout emptyView = (RelativeLayout) rootView.findViewById(R.id.emptyView);
        listView = (ListView) rootView.findViewById(R.id.list);
        //listView.addHeaderView(emptyView, null, false);
        eventTitle = new ArrayList<String>();
        eventImageUrl = new ArrayList<String>();
        adapter = new EventsAdapter(getActivity(), android.R.layout.simple_list_item_1, eventTitle);
        new EventTitle().execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Finding navigation drawer fragment
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        //Instantiating poppyView
        /*mPoppyViewHelper = new PoppyViewHelper(getActivity(), PoppyViewHelper.PoppyViewPosition.TOP);
        poppyView = mPoppyViewHelper.createPoppyViewOnListView(R.id.list, R.layout.poppyview_actionbar);
        //Setting poppyView layout
        poppyViewProgress = (SmoothProgressBar) poppyView.findViewById(R.id.poppyViewProgress);
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
        ImageView imageView = (ImageView) poppyView.findViewById(R.id.drawer_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationDrawerFragment.mDrawerLayout.openDrawer(mNavigationDrawerFragment.mFragmentContainerView);
            }
        });*/
    }

    //Setting eventTitle and eventImageURL arrayList
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

        try {
            cacheFile = new File(cacheDir, ""+imgURL.hashCode());
            fileInputStream = new FileInputStream(cacheFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("cacheURL", "File path not found");
        }

        final Bitmap bitmap1 = getBitmapFromMemCache(imgURL);
        final Bitmap bitmap2 = BitmapFactory.decodeStream(fileInputStream);

        //Checking for cache in disk
        if (bitmap2 != null) {
            imageView.setImageBitmap(bitmap2);
        }
        else {
            //Checking for cache in memory
            if (bitmap1 != null) {
                imageView.setImageBitmap(bitmap1);
            }
            else {

                if (cancelPotentialWork(imgURL, imageView)) {
                    final LoadImage task = new LoadImage(imageView);
                    final AsyncDrawable asyncDrawable = new AsyncDrawable(imgURL, task);
                    imageView.setImageDrawable(asyncDrawable);
                    task.execute(imgURL);
                }
            }
        }
    }

    /*
    * Loading bitmaps and Handling concurrency
    */

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private String data = null;

        public LoadImage(ImageView imageView) {
            //Using weak reference so that imageView can be garbage collected
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            data = strings[0];
            final Bitmap bitmap1 = decodeSampledBitmapFromURL(data, 150, 150);
            //Adding bitmap to memory cache
            addBitmapToMemoryCache(data, bitmap1);
            //Adding bitmap as cache image
            putBitmapInDiskCache(data, bitmap1);
            return bitmap1;
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

    /*
    * Using memory cache
    */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /*
    * Using disk cache
    */
    private void putBitmapInDiskCache(String key, Bitmap bitmap) {

        cacheDir.mkdir();
        //Create a path in that dir for a file
        cacheFile = new File(cacheDir, ""+key.hashCode());
        try {
            //Create a file at the path, and open it for writing, obtaining the output stream
            cacheFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
            //Write the bitmap to the output stream, in PNG format
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            //Flush and close the output stream
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("cachePng", "Error while saving image to cache");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
