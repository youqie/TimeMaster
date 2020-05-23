package bupt.id2017211631.timemasterbig.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ARightAdapter extends BaseAdapter {
    private Context context;
    List<Activity> list;
    Tag[] tags;
    ListView listView;
    View listItem;


    public ARightAdapter(Context context, List<Activity> models, Tag[] tags, ListView left_view) {
        super();
        this.context = context;
        this.list = models;
        this.tags = tags;
        this.listView =left_view;
        ListAdapter listAdapter = listView.getAdapter();
        listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold =new ViewHold(tags.length);
        Activity activities = (Activity) getItem(position);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String timenow = timeFormat.format(date); // 时间
        String datenow = dateFormat.format(date);

        double time;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_right_item_a, parent,false);
            LinearLayout liner = (LinearLayout)convertView.findViewById(R.id.activitylist);

            if(activities.endTime.toString().equals("23:59:59") && activities.date.toString().equals(datenow))
             time=(Activity.strToTime(timenow).getTime()-activities.startTime.getTime())/900000.0;
            else time=(activities.endTime.getTime()-activities.startTime.getTime())/900000.0;


            //总高度
            int hight = listItem.getMeasuredHeight();
            double totalhight = hight*time;


            int i=0;
            for(Tag tag:tags)
            {

                View itemview = LayoutInflater.from(context).inflate(R.layout.layout_right_itme2, null);
                TextView item = (TextView) itemview.findViewById(R.id.right_item);
                item.getLayoutParams().height= (int) (Math.round(totalhight)+(Math.round(time)-1)*listView.getDividerHeight());
                viewHold.textView[i]=item;
                if(activities.tag.equals(tag.name)) viewHold.textView[i].setBackgroundColor(tag.color);
                liner.addView(itemview);
                i++;
            }

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }


        return convertView;
    }

    static class ViewHold {

       TextView textView[];

       ViewHold(int tagnumber)
       {
           textView = new TextView[tagnumber];
       }
    }

}
