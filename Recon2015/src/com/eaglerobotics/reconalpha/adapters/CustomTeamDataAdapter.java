package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;


import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.util.TeamInfo;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomTeamDataAdapter extends ArrayAdapter<TeamInfo> {
  private final Activity context;
  private final ArrayList<TeamInfo> teams;

  static class ViewHolder {
    public TextView team;
    public ProgressBar pb;
    public TextView order;
    public TextView val;
  }

  public CustomTeamDataAdapter(Activity context, ArrayList<TeamInfo> labels) {
    super(context, R.layout.rowlayout, labels);
    this.context = context;
    this.teams = labels;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    if (rowView == null) {
      LayoutInflater inflater = context.getLayoutInflater();
      rowView = inflater.inflate(R.layout.rowlayoutforteamdata, null);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.team = (TextView) rowView.findViewById(R.id.label);
      viewHolder.pb = (ProgressBar) rowView.findViewById(R.id.progbar);
      viewHolder.order = (TextView) rowView.findViewById(R.id.textOrder);
      viewHolder.val = (TextView) rowView.findViewById(R.id.textValue);
      rowView.setTag(viewHolder);
    }

    ViewHolder holder = (ViewHolder) rowView.getTag();

    holder.team.setText(teams.get(position).getTeamNum());
    holder.pb.setProgress(teams.get(position).getRating());
    holder.order.setText(position+1+".");
    holder.val.setText(teams.get(position).getRatingLabel());


    return rowView;
  }
} 
