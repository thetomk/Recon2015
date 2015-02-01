package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import com.eaglerobotics.reconalpha.DynamoDBManager;
import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<TeamMatch> {
  private final Activity context;
  private final ArrayList<TeamMatch> matches;

  static class ViewHolder {
    public TextView text;
    public ImageView gimage;
    public ImageView rimage;
    public ProgressBar pb;
  }

  public CustomArrayAdapter(Activity context, ArrayList<TeamMatch> labels) {
    super(context, R.layout.rowlayout, labels);
    this.context = context;
    this.matches = labels;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    if (rowView == null) {
      LayoutInflater inflater = context.getLayoutInflater();
      rowView = inflater.inflate(R.layout.rowlayoutwithbar, null);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) rowView.findViewById(R.id.label);
      viewHolder.pb = (ProgressBar) rowView.findViewById(R.id.progbar);
 //    viewHolder.gimage = (ImageView) rowView.findViewById(R.id.greenicon);
 //    viewHolder.rimage = (ImageView) rowView.findViewById(R.id.redicon);
      rowView.setTag(viewHolder);
    }

    ViewHolder holder = (ViewHolder) rowView.getTag();
    String s = matches.get(position).getKey();
    Float tp = (float) matches.get(position).getTotes();
    Float bp = (float) matches.get(position).getBins();
    Float np = (float) matches.get(position).getNoodles();
    int goodpct = 0;
    if (tp+bp+np >0) {
    	goodpct = (int) ((bp+np / (tp+bp+np))*100);
    } else { goodpct = 0;}
    holder.text.setText(s);
    holder.pb.setProgress(goodpct);


    return rowView;
  }
} 
