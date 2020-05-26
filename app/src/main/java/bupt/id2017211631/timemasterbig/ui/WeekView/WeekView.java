package bupt.id2017211631.timemasterbig.ui.WeekView;

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
import java.util.Date;

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



    DBAdapter dbAdapter;
    Activity activity1 = new Activity();
    Activity activity2;
    Activity activity3;
    Activity activity4;
    Activity[] activitiesList = new Activity[4];
    Activity[] testactivitiesList = new Activity[4];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.fragment_weekview,null);
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();

        testactivitiesList[0] = (new Activity(1,"锻炼",Activity.strToDate("2020-05-31"),Activity.strToTime("00:00:00")
                ,Activity.strToTime("02:10:00"),""));
        testactivitiesList[1] = (new Activity(2,"学习",Activity.strToDate("2020-05-31"),Activity.strToTime("02:10:00")
                ,Activity.strToTime("03:25:00"),""));
        testactivitiesList[2] = (new Activity(3,"睡觉",Activity.strToDate("2020-05-31"),Activity.strToTime("03:25:00")
                ,Activity.strToTime("08:10:00"),""));
        testactivitiesList[3] = (new Activity(4,"吃饭",Activity.strToDate("2020-05-31"),Activity.strToTime("08:10:00")
                ,Activity.strToTime("15:30:00"),""));

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
                Calendar a = monthView.getSelectedCalendar();
                String b = String.valueOf(a.getWeek());
                //Log.d(b,String.valueOf(a));


//                dbAdapter.deleteAllActivity();
//                //插入测试数据
//                dbAdapter.insertActivity(testactivitiesList[0]);
//                dbAdapter.insertActivity(testactivitiesList[1]);
//                dbAdapter.insertActivity(testactivitiesList[2]);
//                dbAdapter.insertActivity(testactivitiesList[3]);

                //获取点击日期的年月日
                int y = a.getYear();
                int m = a.getMonth();
                int d = a.getDay();
                System.out.println("日期为:" + y + m + d );

                //该周的周一
                Date firstdate ;

                //该周的周日
                Date lastdate ;

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
                java.sql.Date sqlfirstdate = new java.sql.Date(firstdate.getTime());
                //System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
                cal.add(java.util.Calendar.DATE, 6);
                //System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));

                lastdate = cal.getTime();
                java.sql.Date sqllastdate = new java.sql.Date(lastdate.getTime());

                //查询某个时间段的所有数据(一周)
                activitiesList = dbAdapter.queryActivityByTime(sqlfirstdate,sqllastdate);

                //查询所有数据
                //activitiesList =dbAdapter.queryAllActivity();

                //删除所有数据
                //dbAdapter.deleteAllActivity();

                weekView.scrollToCalendar(a.getYear(),a.getMonth(),a.getDay());

                if (activitiesList == null){
                    mondayLayout.removeAllViews();
                    tuesdayLayout.removeAllViews();
                    wednesLayout.removeAllViews();
                    thursdayLayout.removeAllViews();
                    fridayLayout.removeAllViews();
                    saturdayLayout.removeAllViews();
                    sundayLayout.removeAllViews();
                }
                if(null != activitiesList){
                    //System.out.println("数组长度：" + activitiesList.length);
                    showWeekView(activitiesList);
                }
            }
        });

        showWeekView();
        return view;
    }


    //输入某一天，判断是周几，并显示在weekView中
    private void showWeekView(Activity[] activities){
        Tag[] tags =dbAdapter.queryAllTag();

        //清理界面
        mondayLayout.removeAllViews();
        tuesdayLayout.removeAllViews();
        wednesLayout.removeAllViews();
        thursdayLayout.removeAllViews();
        fridayLayout.removeAllViews();
        saturdayLayout.removeAllViews();
        sundayLayout.removeAllViews();


        LinearLayout linearLayout = tuesdayLayout;
        Button[] buttons = new Button[activities.length];
        for(int i = 0;i<activities.length;i++){
            //判断某一天是周几（date类型用）
            int y=activities[i].date.getYear();
            int m=activities[i].date.getMonth();
            int d=activities[i].date.getDay();
            int week = -1;
            if(m == 1 || m == 2)
            {
                m += 12;
                y--;
            }
            week = (d+2*m+3*(m+1)/5+y+y/4-y/100+y/400+1)%7; //判断某个日期是在某一周的周几
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
            int e = (int) (activities[i].endTime.getTime()-activities[i].startTime.getTime())/3600/24*2;
            Log.d("e",String.valueOf(e));
            linearParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, e, getResources().getDisplayMetrics())/* */;
            linearParams.setMargins(5,0,5,0);//修改按钮边距
            buttons[i].setLayoutParams(linearParams);
            buttons[i].setText(activities[i].tag);

            //System.out.println("颜色数组长度"+tags.length);
            //System.out.println(activities[i].tag);
            //显示颜色
            for(Tag tag:tags){
                if(tag.name.equals(activities[i].tag)){
                    buttons[i].setBackgroundColor(tag.color);
                    System.out.println("颜色"+ tag.color);
                }
                else{
                    System.out.println(tag.name+ "*" +activities[i].tag);
                }
            }
        }

    }

    //没有参数的时候，默认显示当天的周视图
    private void showWeekView(){
        Calendar a = monthView.getSelectedCalendar();
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
        java.sql.Date sqlfirstdate = new java.sql.Date(firstdate.getTime());
        //System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
        cal.add(java.util.Calendar.DATE, 6);
        //System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));

        lastdate = cal.getTime();
        java.sql.Date sqllastdate = new java.sql.Date(lastdate.getTime());

        //查询某个时间段的所有数据(一周)
        activitiesList = dbAdapter.queryActivityByTime(sqlfirstdate,sqllastdate);

        //查询所有数据
        //activitiesList =dbAdapter.queryAllActivity();

        //删除所有数据
        //dbAdapter.deleteAllActivity();

        weekView.scrollToCalendar(a.getYear(),a.getMonth(),a.getDay());

        if (activitiesList == null){
            mondayLayout.removeAllViews();
            tuesdayLayout.removeAllViews();
            wednesLayout.removeAllViews();
            thursdayLayout.removeAllViews();
            fridayLayout.removeAllViews();
            saturdayLayout.removeAllViews();
            sundayLayout.removeAllViews();
        }
        if(null != activitiesList){
            //System.out.println("数组长度：" + activitiesList.length);
            showWeekView(activitiesList);
        }
    }
}
