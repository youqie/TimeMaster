package bupt.id2017211631.timemasterbig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // View addView = View.inflate(this, R.layout.activity_add, null);
        submitBtn = (Button) findViewById(R.id.submit);
        cancelBtn = (Button) findViewById(R.id.cancel);
        tag = (Spinner) findViewById(R.id.Spinner01);
        timeView = (TextView) findViewById(R.id.timenow);
        noteView = (TextView) findViewById(R.id.editText2);

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
        if(activities==null) timenow = timeFormat.format(date);
        else if(activities[0].endTime.toString().equals("23:59:59"))
        {
            timenow = timeFormat.format(date);
        }
        else   timenow = activities[0].endTime.toString(); // 时间


        timeView.setText(timenow);

//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_add);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//
//        // 返回按键
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        // 取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setTagsDropdown(); // 设置下拉选择框

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activities!=null)
                {
                    activities[0].endTime = Activity.strToTime(timenow);
                    dbAdepter.updateActivity(activities[0].ID, activities[0]);
                }

                dbAdepter.insertActivity(new Activity(1,(String) tag.getSelectedItem(),
                        Activity.strToDate(datenow),Activity.strToTime(timenow)
                ,Activity.strToTime("23:59:59"),noteView.getText().toString()));
                finish();
//                @SuppressLint("SdCardPath") File f = new File(
//                        "/data/data/com.jhs.stocklinkscrool/shared_prefs/ActivityNow.xml");
//                if (f.exists()) {
//                    SharedPreferences sharedPreferences =
//                            getSharedPreferences("ActivityNow", Context.MODE_PRIVATE);
//                    Activity preActivity = new Activity();
//                    preActivity.tag = sharedPreferences.getString("KEY_TAG", "");
//                    preActivity.date =
//                            Activity.strToDate(sharedPreferences.getString("KEY_DATE", ""));
//                    preActivity.startTime =
//                            Activity.strToTime(sharedPreferences.getString("KEY_START_TIME", ""));
//                    preActivity.endTime =
//                            Activity.strToTime(timenow);
//                    preActivity.note = sharedPreferences.getString("KEY_NOTE","");
//
//                    long l = dbAdepter.insertActivity(preActivity);
//
//                    saveSharedPreferences();
//                    finish();
//
//                } else {
//                    saveSharedPreferences();
//                    finish();
//                }

            }
        });
    }

//    private void saveSharedPreferences() {
//        SharedPreferences sharedPreferences =
//                getSharedPreferences("ActivityNow", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.clear();
//
//        editor.putString("KEY_TAG", (String) tag.getSelectedItem());
//        editor.putString("KEY_DATE", datenow);
//        editor.putString("KEY_START_TIME", timenow);
//        editor.putString("KEY_NOTE", noteView.getText().toString());
//
//        editor.apply();
//    }

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
