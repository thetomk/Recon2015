package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomTeamArrayAdapter extends ArrayAdapter<TeamMatch> {
  private final Activity context;
  private final ArrayList<TeamMatch> matches;

  static class ViewHolder {
    public TextView text;
    public ProgressBar pb;
    public TextView timeText;
    public TextView comments;
  }

  public CustomTeamArrayAdapter(Activity context, ArrayList<TeamMatch> labels) {
    super(context, R.layout.rowlayout, labels);
    this.context = context;
    this.matches = labels;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    if (rowView == null) {
      LayoutInflater inflater = context.getLayoutInflater();
      rowView = inflater.inflate(R.layout.rowlayoutforteam, null);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) rowView.findViewById(R.id.label);
      viewHolder.pb = (ProgressBar) rowView.findViewById(R.id.progbar);
      viewHolder.comments = (TextView) rowView.findViewById(R.id.textComments);
      viewHolder.timeText = (TextView) rowView.findViewById(R.id.createTime);

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
    holder.comments.setText(matches.get(position).getComments());
    holder.timeText.setText(matches.get(position).getCreateTime() + " (" + matches.get(position).getCreator() + ")");


    return rowView;
  }
} 
