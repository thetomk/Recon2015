package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eaglerobotics.reconalpha.DynamoDBManager;
import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;

public class CustomSchedAdapter extends ArrayAdapter<MatchSched> {
	  private final Activity context;
	  private final ArrayList<MatchSched> matches;

	  static class ViewHolder {
	    public TextView matchText;
	    public TextView red1;
	    public TextView red2;
	    public TextView red3;
	    public TextView redscore;
	    public TextView blue1;
	    public TextView blue2;
	    public TextView blue3;
	    public TextView bluescore;
	  }

	  public CustomSchedAdapter(Activity context, ArrayList<MatchSched> labels) {
	    super(context, R.layout.rowlayout, labels);
	    this.context = context;
	    this.matches = labels;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.rowlayoutforsched, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.matchText = (TextView) rowView.findViewById(R.id.TextMatch);
	      viewHolder.red1 = (TextView) rowView.findViewById(R.id.TextRed1);
	      viewHolder.red2 = (TextView) rowView.findViewById(R.id.TextRed2);
	      viewHolder.red3 = (TextView) rowView.findViewById(R.id.TextRed3);
	      viewHolder.redscore = (TextView) rowView.findViewById(R.id.TextRedScore);
	      viewHolder.blue1 = (TextView) rowView.findViewById(R.id.TextBlue1);
	      viewHolder.blue2 = (TextView) rowView.findViewById(R.id.TextBlue2);
	      viewHolder.blue3 = (TextView) rowView.findViewById(R.id.TextBlue3);
	      viewHolder.bluescore = (TextView) rowView.findViewById(R.id.TextBlueScore);
	      
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String mat = matches.get(position).getMatchType() + "-" + matches.get(position).getMatchNum();
	    String r1 = matches.get(position).getRed1();
	    String r2 = matches.get(position).getRed2();
	    String r3 = matches.get(position).getRed3();
	    int rs = matches.get(position).getRedTotal();
	    String b1 = matches.get(position).getBlue1();
	    String b2 = matches.get(position).getBlue2();
	    String b3 = matches.get(position).getBlue3();
	    int bs = matches.get(position).getBlueTotal();

	    holder.matchText.setText(mat);
	    holder.red1.setText(r1);
	    holder.red2.setText(r2);
	    holder.red3.setText(r3);
	    holder.redscore.setText(Integer.toString(rs));
	    holder.blue1.setText(b1);
	    holder.blue2.setText(b2);
	    holder.blue3.setText(b3);
	    holder.bluescore.setText(Integer.toString(bs));

	    


	    return rowView;
	  }
	} 

