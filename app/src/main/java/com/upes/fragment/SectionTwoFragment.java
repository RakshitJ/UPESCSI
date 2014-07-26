package com.upes.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class SectionTwoFragment extends Fragment {

    private PoppyViewHelper mPoppyViewHelper;
    private View poppyView;
    private View rootView;
    private ListView listView;
    private ImageView iv;
    private TextView tv;
    String URL = "http://www.upescsi.in/events/";

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
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Instantiating poppyView
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

}
