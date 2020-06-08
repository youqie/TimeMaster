package bupt.id2017211631.timemasterbig.ui.WeekView;


import android.graphics.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;
import bupt.id2017211631.timemasterbig.SQL.Tag;

public class WeekView extends Fragment {

    LinearLayout mondayPast;
    Button shrink;
    boolean isExp;
    CalendarLayout calendarLayout ;
    CalendarView monthView;
    CalendarView weekView;

    LinearLayout mondayLayout;
    LinearLayout tuesdayLayout;
    LinearLayout wednesLayout;
    LinearLayout thursdayLayout;
    LinearLayout fridayLayout;
    LinearLayout saturdayLayout;
    LinearLayout sundayLayout;

    private List<Activity> activitiesList;

    DBAdapter dbAdapter;
    Activity activity1 = new Activity();
    Activity activity2;
    Activity activity3;
    Activity activity4;
    Activity[] activities = new Activity[5];
    Activity[] testactivitiesList = new Activity[5];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.fragment_weekview,null);
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();


//        dbAdapter.deleteAllActivity();
//        //插入测试数据
//        dbAdapter.insertActivity(testactivitiesList[0]);
//        dbAdapter.insertActivity(testactivitiesList[1]);
//        dbAdapter.insertActivity(testactivitiesList[2]);
//        dbAdapter.insertActivity(testactivitiesList[3]);
//        dbAdapter.insertActivity(testactivitiesList[4]);

        activitiesList = new ArrayList<>();

        calendarLayout = view.findViewById(R.id.calendarLayout);
        monthView = view.findViewById(R.id.monthView);
        weekView = view.findViewById(R.id.weekView);

        mondayLayout = view.findViewById(R.id.mondayLayout);
        tuesdayLayout = view.findViewById(R.id.tuesdayLayout);
        wednesLayout = view.findViewById(R.id.wednesdayLayout);
        thursdayLayout = view.findViewById(R.id.thursdayLayout);
        fridayLayout = view.findViewById(R.id.fridayLayout);
        saturdayLayout = view.findViewById(R.id.saturdayLayout);
        sundayLayout = view.findViewById(R.id.sundayLayout);


        isExp = calendarLayout.isExpand();

        shrink = view.findViewById(R.id.shrinkMonthView);
        //点击按钮显示/隐藏月视图
        shrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExp){
                    calendarLayout.hideCalendarView();
                    shrink.setText("展开月视图");
                    isExp = false;
                }
                else{
                    calendarLayout.showCalendarView();
                    shrink.setText("收起月视图");
                    isExp = true;
                }
            }
        });

        /**
         * 用户点击月视图中的日期事件
         * 周视图同步跳转到相应的位置
         * 周视图的活动情况刷新
         */
        monthView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                showWeekView(calendar);
            }
        });
        weekView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                //System.out.println("day:"+calendar.getDay());
                showWeekView(calendar);
            }
        });
        //Create的时候就要调用一下这个显示周视图的函数，不然刚进来的时候不会自动显示
        showWeekView(monthView.getSelectedCalendar());
        return view;
    }


    //输入活动数组，将数组中所有的活动显示在周视图里

    private void showWeekView(final List<Activity> activities,int week){
        Tag[] tags =dbAdapter.queryAllTag();
        LinearLayout linearLayout = null;
        switch(week){
            //变换layout
            case 1:linearLayout = mondayLayout;break;
            case 2:linearLayout = tuesdayLayout;break;
            case 3:linearLayout = wednesLayout;break;
            case 4:linearLayout = thursdayLayout;break;
            case 5:linearLayout = fridayLayout;break;
            case 6:linearLayout = saturdayLayout;break;
            case 0:linearLayout = sundayLayout;break;
        }
        //清理界面
//        mondayLayout.removeAllViews();
//        tuesdayLayout.removeAllViews();
//        wednesLayout.removeAllViews();
//        thursdayLayout.removeAllViews();
//        fridayLayout.removeAllViews();
//        saturdayLayout.removeAllViews();
//        sundayLayout.removeAllViews();

        Button[] buttons = new Button[activities.size()];

        for(int i = 0;i<activities.size();i++){
//            //判断某一天是周几（date类型用）
//            int y=activities[i].date.getYear();
//            int m=activities[i].date.getMonth();
//            int d=activities[i].date.getDay();
//            int week = -1;
//            if(m == 1 || m == 2)
//            {
//                m += 12;
//                y--;
//            }
//            week = (d+2*m+3*(m+1)/5+y+y/4-y/100+y/400+1)%7; //判断某个日期是在某一周的周几
            switch(week){
                //变换layout
                case 1:linearLayout = mondayLayout;break;
                case 2:linearLayout = tuesdayLayout;break;
                case 3:linearLayout = wednesLayout;break;
                case 4:linearLayout = thursdayLayout;break;
                case 5:linearLayout = fridayLayout;break;
                case 6:linearLayout = saturdayLayout;break;
                case 0:linearLayout = sundayLayout;break;
            }

            buttons[i] = new Button(getActivity());
            linearLayout.addView(buttons[i]);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) buttons[i].getLayoutParams();

            /**
             * 计算每一个控件的长度的方法：
             * 1.获取时间轴所在的linearLayout的总高度
             * 2.获取每一个活动占每日24小时的百分比，给每一个按钮分配相应的高度
             * 注意：每一个时间轴后面跟了1个dp的分割线，可能会使时间轴和时间块不能完全对齐的情况出现。
             */
            System.out.println("时间轴的高度为："+ (int) (activities.get(i).endTime.getTime()-activities.get(i).startTime.getTime()));

            //每日时间的毫秒数
            int timeOfOneDay = 3600000*24;
            float e = (float) (activities.get(i).endTime.getTime()-activities.get(i).startTime.getTime())/timeOfOneDay * 1500;
            Log.d("e",String.valueOf(e));
            linearParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, e, getResources().getDisplayMetrics())/* */;
            linearParams.setMargins(5,0,5,0);//修改按钮边距
            buttons[i].setLayoutParams(linearParams);
            buttons[i].setText(activities.get(i).tag);
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialog(activities.get(finalI));
                }
            });
            //System.out.println("颜色数组长度"+tags.length);
            //System.out.println(activities.get(i).tag);
            //显示颜色
            for(Tag tag:tags){
                if(tag.name.equals(activities.get(i).tag)){
                    buttons[i].setBackgroundColor(tag.color);
                    System.out.println("颜色"+ tag.color);
                }
                else{
                    System.out.println(tag.name+ "*" +activities.get(i).tag);
                }
            }
        }

    }

    //没有参数的时候，默认显示当天的周视图
    private void showWeekView(Calendar a){
        //清理界面
        mondayLayout.removeAllViews();
        tuesdayLayout.removeAllViews();
        wednesLayout.removeAllViews();
        thursdayLayout.removeAllViews();
        fridayLayout.removeAllViews();
        saturdayLayout.removeAllViews();
        sundayLayout.removeAllViews();


        String b = String.valueOf(a.getWeek());

        //获取点击日期的年月日
        int y = a.getYear();
        int m = a.getMonth();
        int d = a.getDay();
        System.out.println("日期为:" + y + m + d );

        //该周的周一
        Date firstdate ;

        //该周的周日
        Date lastdate ;

        //用来存当天的日期
        Date currentdate;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(y,m-1,d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int dayWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
        }
        //设置一个星期的第一天
        cal.setFirstDayOfWeek(java.util.Calendar.MONDAY);

        //获得当前日期是一个星期的第几天
        int day = cal.get(java.util.Calendar.DAY_OF_WEEK);

        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(java.util.Calendar.DATE, cal.getFirstDayOfWeek()-day);

        //System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));

        //将java.util.date转换为java.sql.date用来查询
        firstdate = cal.getTime();
        currentdate = firstdate;
        //java.sql.Date sqlfirstdate = new java.sql.Date(firstdate.getTime());

        java.sql.Date sqlcurrentdate = new java.sql.Date(currentdate.getTime());

        java.sql.Date datenow = new java.sql.Date(new Date().getTime());
        /**
         * 由于每天的第一个活动不一定是0时开始的了，周视图显示的逻辑修改为：
         * 使用for循环，挨个显示从周一到周日每天的视图。
         * 如果每天的第一个视图不是从0开始的，那么计算开始的时间，并在开始时间的前面增加一个空白的按钮。
         */
        LinearLayout linearLayout = null;
        for(int i = 0;i<7;i++ ){

            //获取当前cal是星期几，切换对应的layout
            switch(cal.get(java.util.Calendar.DAY_OF_WEEK)-1){
                //变换layout
                case 1:linearLayout = mondayLayout;break;
                case 2:linearLayout = tuesdayLayout;break;
                case 3:linearLayout = wednesLayout;break;
                case 4:linearLayout = thursdayLayout;break;
                case 5:linearLayout = fridayLayout;break;
                case 6:linearLayout = saturdayLayout;break;
                case 0:linearLayout = sundayLayout;break;
            }
            //在对应的layout中显示按钮,此时的sqlcurrentdate在上面定义，等于每周的第一天。
            //从数据库中调取改天的数据
            activities = dbAdapter.queryActivityByTime(sqlcurrentdate,sqlcurrentdate);
            //System.out.println(activitiesList[0].startTime!= Activity.strToTime("00:00:00"));
            //如果该天没有活动，就将对应的layout清空
            if (activities == null){
                linearLayout.removeAllViews();
            }
            //如果第一个活动不是0点开始
            else if(activities !=null){

                activitiesList.clear();
                Log.v("Time",sqlcurrentdate.toString());
                String time = "00:00:00";
                    for (Activity activity : activities) {
                        if (!activity.startTime.toString().equals(time))
                            activitiesList.add(new Activity(1, "", sqlcurrentdate, Activity.strToTime(time)
                                    , activity.startTime, ""));
                        time = activity.endTime.toString();
                        activitiesList.add(activity);
                    }


                    if(sqlcurrentdate.toString().equals(datenow.toString()))
                    {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        java.sql.Time timenow =Activity.strToTime( format.format(new Date()));
                        activitiesList.get(activitiesList.size()-1).endTime=timenow;

                    }
                        //Log.v("endtime",activitiesList.get(activitiesList.size()-1).endTime.toString());

//                Button blank = new Button(getActivity());
//                linearLayout.addView(blank);
//                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) blank.getLayoutParams();
//
//                /**
//                 * 计算每一个控件的长度的方法：
//                 * 1.获取时间轴所在的linearLayout的总高度
//                 * 2.获取每一个活动占每日24小时的百分比，给每一个按钮分配相应的高度
//                 * 注意：每一个时间轴后面跟了1个dp的分割线，可能会使时间轴和时间块不能完全对齐的情况出现。
//                 */
//                //System.out.println("时间轴的高度为："+ (int) (activitiesList[0].endTime.getTime()-activitiesList[0].startTime.getTime()));
//
//                //每日时间的毫秒数
//                int timeOfOneDay = 3600000*24;
//                float e = (float) (activities[0].startTime.getTime()-Activity.strToTime("00:00:00").getTime())/timeOfOneDay * 1500;
//                Log.d("e",String.valueOf(e));
//                linearParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, e, getResources().getDisplayMetrics())/* */;
//                linearParams.setMargins(5,0,5,0);//修改按钮边距
//                blank.setLayoutParams(linearParams);
//                blank.setText("");
//                blank.setBackgroundColor(Color.TRANSPARENT);

                showWeekView(activitiesList,cal.get(java.util.Calendar.DAY_OF_WEEK)-1);
                //System.out.println("5.31:" + String.valueOf(cal.get(java.util.Calendar.DAY_OF_WEEK)-1));
            }
//            else{
//                //System.out.println("数组长度：" + activitiesList.length);
//                showWeekView(activities,cal.get(java.util.Calendar.DAY_OF_WEEK)-1);
//                //System.out.println("456" + String.valueOf(cal.get(java.util.Calendar.DAY_OF_WEEK)-1));
//            }

            cal.add(java.util.Calendar.DATE,1);
            currentdate = cal.getTime();
            sqlcurrentdate = new java.sql.Date(currentdate.getTime());
        }
    }
    private void showdialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(activity.tag+ " 于 "+ activity.date)
                .setMessage("备注: "+activity.note +"\n时间："+activity.startTime+"----"+activity.endTime).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
