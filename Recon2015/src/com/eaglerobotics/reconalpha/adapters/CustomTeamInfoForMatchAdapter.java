package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;
import com.eaglerobotics.reconalpha.util.TeamInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomTeamInfoForMatchAdapter extends ArrayAdapter<TeamInfo> {
  private final Activity context;
  private final ArrayList<TeamInfo> teams;

  static class ViewHolder {
    public TextView team;
    public TextView comms;
    public ProgressBar pb;
  }

  public CustomTeamInfoForMatchAdapter(Activity context, ArrayList<TeamInfo> labels) {
    super(context, R.layout.rowlayout, labels);
    this.context = context;
    this.teams = labels;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    
    if (rowView == null) {
      LayoutInflater inflater = context.getLayoutInflater();
      rowView = inflater.inflate(R.layout.rowlayoutforteaminfoformatch, null);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.team = (TextView) rowView.findViewById(R.id.team);
      viewHolder.comms = (TextView) rowView.findViewById(R.id.textComments);
      viewHolder.pb = (ProgressBar) rowView.findViewById(R.id.progbar);

      rowView.setTag(viewHolder);
    }

    ViewHolder holder = (ViewHolder) rowView.getTag();
    String s = teams.get(position).getTeamNum();
    String ac = teams.get(position).getSynopsis();
    int goodpct = teams.get(position).getGoodPct();

    holder.team.setText(s);
    holder.comms.setText(ac);
    holder.pb.setProgress(goodpct);
    


    return rowView;
  }
} 
