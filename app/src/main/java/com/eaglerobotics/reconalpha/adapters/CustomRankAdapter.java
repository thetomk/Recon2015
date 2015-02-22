package com.eaglerobotics.reconalpha.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;
import com.eaglerobotics.reconalpha.frcAPI.Ranking;

import java.util.List;

public class CustomRankAdapter extends ArrayAdapter<Ranking> {
	  private final Activity context;
	  private final List<Ranking> rankList;

	  static class ViewHolder {
	    public TextView rank;
	    public TextView teamnum;
	    public TextView autopts;
	    public TextView totepts;
	    public TextView binpts;
	    public TextView noodlepts;
	    public TextView cooppts;
	    public TextView matchdone;
	    public TextView avg;
	  }

	  public CustomRankAdapter(Activity context, List<Ranking> labels) {
	    super(context, layout.rowlayout, labels);
	    this.context = context;
	    this.rankList = labels;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(layout.rowlayoutforranking, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.rank = (TextView) rowView.findViewById(id.tVRank);
	      viewHolder.teamnum = (TextView) rowView.findViewById(id.tVTeam);
	      viewHolder.autopts = (TextView) rowView.findViewById(id.tVAuto);
	      viewHolder.totepts = (TextView) rowView.findViewById(id.tVTote);
	      viewHolder.binpts = (TextView) rowView.findViewById(id.tVBin);
	      viewHolder.noodlepts = (TextView) rowView.findViewById(id.tVNoodle);
	      viewHolder.cooppts = (TextView) rowView.findViewById(id.tvCoop);
	      viewHolder.matchdone = (TextView) rowView.findViewById(id.tVMatches);
	      viewHolder.avg = (TextView) rowView.findViewById(id.tVQualAvg);
	      
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String rv = Integer.toString(rankList.get(position).getRank());
	    String tv = Integer.toString(rankList.get(position).getTeamNumber());
	    String ap = Integer.toString(rankList.get(position).getAutoPoints());
	    String tp = Integer.toString(rankList.get(position).getTotePoints());
	    String bp = Integer.toString(rankList.get(position).getContainerPoints());
	    String np = Integer.toString(rankList.get(position).getLitterPoints());
	    String cp = Integer.toString(rankList.get(position).getCoopertitionPoints());
	    String mp = Integer.toString(rankList.get(position).getMatchesPlayed());
	    String qa = Float.toString(rankList.get(position).getQualAverage());

	    holder.rank.setText(rv);
	    holder.teamnum.setText(tv);
	    holder.autopts.setText("Auto: "+ap);
	    holder.totepts.setText("Tote: "+tp);
	    holder.binpts.setText("Bin: "+bp);
	    holder.noodlepts.setText("Nd: "+np);
	    holder.cooppts.setText("Co: "+cp);
	    holder.matchdone.setText("(Matches: "+mp+")");
	    holder.avg.setText("Av: "+qa);

	    


	    return rowView;
	  }
	} 

