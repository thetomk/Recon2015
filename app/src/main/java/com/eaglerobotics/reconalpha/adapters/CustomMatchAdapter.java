package com.eaglerobotics.reconalpha.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R;


public class CustomMatchAdapter extends ArrayAdapter<TeamMatch> {
	  private final Activity context;
	  private final ArrayList<TeamMatch> matches;
	  private final MatchSched msched;

	  static class ViewHolder {
	    public TextView text;
	    public ProgressBar pb;
	    public TextView commentText;
	  }

	  public CustomMatchAdapter(Activity context, ArrayList<TeamMatch> labels, MatchSched matches) {
	    super(context, R.layout.rowlayout, labels);
	    this.context = context;
	    this.matches = labels;
	    this.msched = matches;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.rowlayoutformatchgroup, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.label);
	      viewHolder.pb = (ProgressBar) rowView.findViewById(R.id.progbar);
	      viewHolder.commentText = (TextView) rowView.findViewById(R.id.comments);
	    	rowView.setBackgroundColor(0);
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String s = matches.get(position).getTeamNum();
	    String alliance = matches.get(position).getAlliance();
          int total = 0;

          if (matches.get(position).getTotes() > 0) {total++;}
          if (matches.get(position).getBins() > 0) {total++;}
          if (matches.get(position).getNoodles() > 0) {total++;}

          total = total + (matches.get(position).getAutoBin()?1:0);
          total = total + (matches.get(position).getAutoTote()?1:0);
          total = total + (matches.get(position).getAutoStack()?1:0);
          total = total + (matches.get(position).getCoopStack()?1:0);
          total = total + (matches.get(position).getCoopTote()?1:0);
          total = total + (matches.get(position).getCarry()?1:0);
          total = total + (matches.get(position).getFast()?1:0);
          total = total + (matches.get(position).getStackBin()?1:0);
          total = total + (matches.get(position).getStackTote()?1:0);
          total = total + (matches.get(position).getDriver()?1:0);
          total = total + (matches.get(position).getPickable()?1:0);
          total = total + (matches.get(position).getNoodleBin()?1:0);
          total = total + (matches.get(position).getNoodleFloor()?1:0);
          total = total + (matches.get(position).getNoodleThrow()?1:0);
          total = total + (matches.get(position).getAutoMove()?1:0);
          total = total + (matches.get(position).getDied()?0:1);


          int goodpct = 0;
          if (total >0) {
              goodpct = (int) (((float) total / 19.0)*100);
          } else { goodpct = 0;}
	    holder.text.setText(s);
	    holder.pb.setProgress(goodpct);
//	    String txt = "[" + matches.get(position).getRobotType() + "] ";
	    holder.commentText.setText(matches.get(position).getComments());
	    
	    holder.text.setBackgroundColor(Color.parseColor(alliance));
	    
//    	rowView.setBackgroundColor(Color.parseColor("black"));
    	
//	    if ((s.equals(msched.getBlue1())) || (s.equals(msched.getBlue2())) || (s.equals(msched.getBlue3()))) {
//	    	rowView.setBackgroundColor(Color.parseColor("blue"));
//	    }
//	    if ((s.equals(msched.getRed1())) || (s.equals(msched.getRed2())) || (s.equals(msched.getRed3()))) {
//	    	rowView.setBackgroundColor(Color.parseColor("red"));
//	    }	   
	    


	    return rowView;
	  }
	} 
