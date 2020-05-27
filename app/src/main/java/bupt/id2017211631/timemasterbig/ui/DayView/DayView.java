package bupt.id2017211631.timemasterbig.ui.DayView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bupt.id2017211631.timemasterbig.MyHorizontalScrollView;
import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;
import bupt.id2017211631.timemasterbig.SQL.Tag;
import bupt.id2017211631.timemasterbig.UtilTools;
import bupt.id2017211631.timemasterbig.adapter.ALeftAdapter;
import bupt.id2017211631.timemasterbig.adapter.ARightAdapter;
import bupt.id2017211631.timemasterbig.ui.EventDialog;

public class DayView extends Fragment {

    View view;
    public static Handler handler = new Handler();
    //顶部tag
    Tag[] tags;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private DBAdapter dbAdapter;
    //左侧固定一列数据适配
    private ListView left_container_listview;
    private List<String> leftList;
    //右侧数据适配
    private ListView right_container_listview;
    private List<Activity>
            activitiesList;
    private MyHorizontalScrollView title_horsv;
    private MyHorizontalScrollView content_horsv;
    private Runnable RefreshLable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 15);// 间隔15秒
        }

        void update() {
            initRightData();
            initRightView();
        }
    };

    public Runnable refreshTag = new Runnable() {
        public void run() {
            initTag(view);
        }
    };


    @Override
    public void onResume(){
        super.onResume();
        handler.post(refreshTag);
        handler.post(RefreshLable);// 间隔120秒
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (isAdded()) {
            view = View.inflate(getActivity(), R.layout.fragment_dayview, null);
        }

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open(); //启动数据库

        findView(view);
        initTag(view);
        initLeftData();
        initLeftView();
        handler.post(RefreshLable);

        return view;
    }



    public void findView(View view) {
        title_horsv = (MyHorizontalScrollView) view.findViewById(R.id.title_horsv);
        left_container_listview = (ListView) view.findViewById(R.id.left_container_listview);
        content_horsv = (MyHorizontalScrollView) view.findViewById(R.id.content_horsv);
        right_container_listview = (ListView) view.findViewById(R.id.right_container_listview);
    }

    public void initTag(View view) {
        if (isAdded()) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        LinearLayout tagList = (LinearLayout) view.findViewById(R.id.taglist);
        tagList.removeAllViews();

        tags = dbAdapter.queryAllShowTag();

        for (Tag tag : tags) {
            if (isAdded()) {
                View tagview = android.view.LayoutInflater.from(getActivity()).inflate(R.layout.layout_tagname, null);
                TextView text = (TextView) tagview.findViewById(R.id.tag);
                text.setText(tag.name);
                //text.setId(i);
                tagList.addView(tagview);
            }//将TextView 添加到子View 中
        }
    }

    private void initLeftData() {
        leftList = new ArrayList<>();

        leftList.add("0:15");
        leftList.add("0:30");
        leftList.add("0:45");

        for (int i = 1; i < 24; i++) {
            String time = String.valueOf(i);
            leftList.add(time + ":00");
            leftList.add(time + ":15");
            leftList.add(time + ":30");
            leftList.add(time + ":45");
        }

        leftList.add("24:00");
        leftList.add("24:00");
    }

    private void initLeftView() {

        // 设置两个水平控件的联动
        title_horsv.setScrollView(content_horsv);
        content_horsv.setScrollView(title_horsv);
        //添加左侧数据
        ALeftAdapter adapter = new ALeftAdapter(getActivity(), leftList);
        left_container_listview.setAdapter(adapter);
        UtilTools.setListViewHeightBasedOnChildren(left_container_listview);

    }

    private void initRightView() {
        if (isAdded()) {
            ARightAdapter myRightAdapter = new ARightAdapter(
                    getActivity(), activitiesList, tags, left_container_listview
            );
            right_container_listview.setAdapter(myRightAdapter);
            ViewGroup.LayoutParams params = right_container_listview.getLayoutParams();
            params.height = left_container_listview.getLayoutParams().height;
            right_container_listview.setLayoutParams(params);

            right_container_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Activity activities = (Activity) parent.getAdapter().getItem(position);

                    // TO-DO change id to correct
                    if (!activities.tag.equals("")) {
                        EventDialog.newInstance(activities.ID).show(getFragmentManager(), "event_dialog");
                    }

                }
            });
        }
    }

    //初始化数据源
    private void initRightData() {

        activitiesList = new ArrayList<>();

        Date date = new Date();
        java.sql.Date datenow = Activity.strToDate(dateFormat.format(date)); // 日期

        Activity[] allactivities = dbAdapter.queryActivityByTime(datenow, datenow);

        String time = "00:00:00";
        if (allactivities != null)
            for (Activity activity : allactivities) {
                if (!activity.startTime.toString().equals(time))
                    activitiesList.add(new Activity(1, "", datenow, Activity.strToTime(time)
                            , activity.startTime, ""));

                time = activity.endTime.toString();
                activitiesList.add(activity);
            }

    }

}
