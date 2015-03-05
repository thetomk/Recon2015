package com.eaglerobotics.reconalpha.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;
import com.eaglerobotics.reconalpha.frcAPI.Event;
import com.eaglerobotics.reconalpha.frcAPI.Ranking;

import java.util.List;

public class CustomEventAdapter extends ArrayAdapter<Event> {
	  private final Activity context;
	  private final List<Event> eventList;

	  static class ViewHolder {
	    public TextView evcode;
	    public TextView location;
	    public TextView evname;
	    public TextView dates;

	  }

	  public CustomEventAdapter(Activity context, List<Event> labels) {
	    super(context, layout.rowlayout, labels);
	    this.context = context;
	    this.eventList = labels;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(layout.rowlayoutforevent, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.evcode = (TextView) rowView.findViewById(id.tVCode);
	      viewHolder.location = (TextView) rowView.findViewById(id.tVLocation);
	      viewHolder.evname = (TextView) rowView.findViewById(id.tVName);
	      viewHolder.dates = (TextView) rowView.findViewById(id.tVDates);

	      
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String cv = (eventList.get(position).getCode());
	    String lv = (eventList.get(position).getCity()+","+eventList.get(position).getStateprov());
	    String nv = (eventList.get(position).getName());
	    String sd = (eventList.get(position).getDateStart());
        String ed = (eventList.get(position).getDateEnd());

	    holder.evcode.setText(cv);
	    holder.location.setText(lv);
	    holder.evname.setText(nv);
	    holder.dates.setText(sd.substring(0,9) + " - "+ ed.substring(0,9));

	    


	    return rowView;
	  }
	} 

