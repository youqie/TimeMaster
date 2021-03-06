package bupt.id2017211631.timemasterbig;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import bupt.id2017211631.timemasterbig.SQL.DBAdapter;


public class AddActivity extends AppCompatActivity {

    String[] tagsList;

    DBAdapter dbAdepter;

    Spinner tag;
    Button submitBtn;
    Button cancelBtn;
    TextView timeView;
    TextView noteView;

    String timenow;
    String datenow;

    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("添加活动");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View addView = View.inflate(this, R.layout.activity_add, null);
        submitBtn = findViewById(R.id.submit);
        cancelBtn = findViewById(R.id.cancel);
        tag = findViewById(R.id.Spinner01);
        timeView = findViewById(R.id.timenow);
        noteView = findViewById(R.id.editText2);

        dbAdepter = new DBAdapter(this);
        dbAdepter.open();//启动数据库

        // 获取并显示当前时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        datenow = dateFormat.format(date); // 日期
        timenow = timeFormat.format(date); // 时间
        java.sql.Date sqldatenow = Activity.strToDate(datenow);

        final Activity[] activities = dbAdepter.queryLastActivity(sqldatenow);
        if (activities == null) timenow = timeFormat.format(date);
        else if (activities[0].endTime.toString().equals("23:59:59")) {
            timenow = timeFormat.format(date);
        } else timenow = activities[0].endTime.toString(); // 时间


        timeView.setText(timenow);

        // 取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setTagsDropdown(); // 设置下拉选择框

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                if (activities != null) {
                    activities[0].endTime = Activity.strToTime(timenow);
                    dbAdepter.updateActivity(activities[0].ID, activities[0]);
                }

                dbAdepter.insertActivity(new Activity(1, (String) tag.getSelectedItem(),
                        Activity.strToDate(datenow), Activity.strToTime(timenow)
                        , Activity.strToTime("23:59:59"), noteView.getText().toString()));

                // 发送通知
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(AddActivity.this, MainActivity.class);

                String id = "channel_01";
                    //用户可以看到的通知渠道的名字
                CharSequence name = "TimeMaster";
                    //用户可看到的通知描述
                String description = "TimeMaster";
                    //构建NotificationChannel实例
                NotificationChannel notificationChannel =
                        new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                    //配置通知渠道的属性
                notificationChannel.setDescription(description);
                    //在notificationManager中创建通知渠道
                notificationManager.createNotificationChannel(notificationChannel);
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(AddActivity.this, 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new NotificationCompat.Builder(AddActivity.this, id)
                        //指定通知的标题内容
                        .setContentTitle("正在进行的活动")
                        //设置通知的内容
                        .setContentText((String) tag.getSelectedItem() + "……" + "从" +  timenow + "开始")
                        //指定通知被创建的时间
                        .setWhen(System.currentTimeMillis())
                        //设置通知的小图标
                        .setSmallIcon(R.drawable.noti)
                        //设置通知的大图标
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_luncher_round))
                        .setContentIntent(contentIntent)
                        .build();
                //设置无法被清除
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                /*
                调用NotificationManager的notify()方法将通知显示出来
                传入的第一个参数是通知的id
                传入的第二个参数是notification对象
                 */
                notificationManager.notify(1, notification);

                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void setTagsDropdown() {
        // 查询数据库内的所有标签
        Tag[] tags = dbAdepter.queryAllShowTag();

        // 初始化tagsList
        tagsList = new String[tags.length];

        int i = 0;
        for (Tag tag : tags) {
            tagsList[i] = tags[i].name;
            i++;
        }

        // 设置标签下拉选择栏的Adapter
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(AddActivity.this,
                android.R.layout.simple_spinner_dropdown_item, tagsList);
        tag.setAdapter(tagsAdapter);
    }
}
