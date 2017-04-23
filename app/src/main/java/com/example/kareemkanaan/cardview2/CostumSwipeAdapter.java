package com.example.kareemkanaan.cardview2;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kareemkanaan on 3/4/17.
 */

public class CostumSwipeAdapter extends PagerAdapter {
    List<Uri> images;
    private int[] image_resources = {R.drawable.homeless,  R.drawable.case8};
    private Context ctxt;
    private LayoutInflater layoutInflater;

    public CostumSwipeAdapter(Context ctxt,List<Uri> images){
        this.ctxt = ctxt;
        this.images=images;

    }


    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater= (LayoutInflater) ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.image_layout,container , false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);

        Picasso.with(ctxt).load(images.get(position)).into(imageView);
        container.addView(item_view);

        return item_view;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
