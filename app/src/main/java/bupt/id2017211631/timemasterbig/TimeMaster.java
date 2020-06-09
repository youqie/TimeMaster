package bupt.id2017211631.timemasterbig;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;

/**
 * Implementation of App Widget functionality.
 */
public class TimeMaster extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        DBAdapter dbAdepter;
        String datenow;
        dbAdepter = new DBAdapter(context);
        dbAdepter.open();//启动数据库

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        datenow = dateFormat.format(date); // 日期
        java.sql.Date sqldatenow = Activity.strToDate(datenow);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_master);
        Activity[] activities = dbAdepter.queryLastActivity(sqldatenow);
        String acnow = activities[0].tag;
        String timeStart = activities[0].startTime.toString();
      //  if (activities[0].endTime.toString() == "23:59:59") {
            views.setTextViewText(R.id.appwidget_text2, acnow + " 从" + timeStart);
       // }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                   updateAppWidget(context,appWidgetManager,appWidgetIds[0]);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task,0,100000);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

