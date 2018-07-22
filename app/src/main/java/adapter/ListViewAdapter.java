package adapter;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import object.News_item;

import com.example.hy.maps.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hy on 2018/5/13.
 */

public class ListViewAdapter extends BaseAdapter {

    List<News_item> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context c;
    public ListViewAdapter( Context context) {
        //this.myAppInfos = myAppInfos;
        this.c=context;
        this.mInflater=mInflater.from(context);
    }
    public void setData(List<News_item> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        final News_item meta = data.get(position);
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item,null);
            mViewHolder.img = (ImageView) convertView.findViewById(R.id.listview_item_img);
            mViewHolder.tag = (TextView) convertView.findViewById(R.id.listview_item_des);
            mViewHolder.person=(TextView)convertView.findViewById(R.id.listview_item_other) ;
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.img.setImageBitmap(meta.getBitmap());
        mViewHolder.tag.setText(meta.getTag());
        mViewHolder.person.setText(meta.getPerson());
        return convertView;
    }
    private class ViewHolder {
        ImageView img;
        TextView tag;
        TextView person;
    }
}
