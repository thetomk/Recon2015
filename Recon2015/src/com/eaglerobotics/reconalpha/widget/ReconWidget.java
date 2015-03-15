package com.eaglerobotics.reconalpha.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Implementation of App Widget functionality.
 */
public class ReconWidget extends AppWidgetProvider {



	  @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	      int[] appWidgetIds) {

		    // Get all ids
		    ComponentName thisWidget = new ComponentName(context,
		    		ReconWidget.class);
		    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		    // Build the intent to call the service
		    Intent intent = new Intent(context.getApplicationContext(),
		        WidgetUpdateService.class);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

		    // Update the widgets via the service
		    context.startService(intent);
		  }
}
