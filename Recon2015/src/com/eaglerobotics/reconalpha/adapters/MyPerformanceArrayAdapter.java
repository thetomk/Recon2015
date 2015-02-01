package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPerformanceArrayAdapter extends ArrayAdapter<String> {
  private final Activity context;
  private final ArrayList<String> names;

  static class ViewHolder {
    public TextView text;
    public ImageView gimage;
    public ImageView rimage;
  }

  public MyPerformanceArrayAdapter(Activity context, ArrayList<String> labels) {
    super(context, R.layout.rowlayout, labels);
    this.context = context;
    this.names = labels;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    if (rowView == null) {
      LayoutInflater inflater = context.getLayoutInflater();
      rowView = inflater.inflate(R.layout.rowlayout, null);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) rowView.findViewById(R.id.label);
      viewHolder.gimage = (ImageView) rowView.findViewById(R.id.greenicon);
      viewHolder.rimage = (ImageView) rowView.findViewById(R.id.redicon);
      rowView.setTag(viewHolder);
    }

    ViewHolder holder = (ViewHolder) rowView.getTag();
    String s = names.get(position);
    holder.text.setText(s);
//    if (s.startsWith("Windows7") || s.startsWith("iPhone")
//        || s.startsWith("Solaris")) {
//      holder.gimage.setImageResource(R.drawable.ic_action_green);
//    } else {
//      holder.rimage.setImageResource(R.drawable.ic_action_red);
//    }

    return rowView;
  }
} 
