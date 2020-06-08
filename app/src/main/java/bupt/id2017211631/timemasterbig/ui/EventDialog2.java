package bupt.id2017211631.timemasterbig.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;
import bupt.id2017211631.timemasterbig.SQL.Tag;
import bupt.id2017211631.timemasterbig.ui.DayView.DayView;


public class EventDialog2 extends DialogFragment {
    DBAdapter dbAdapter;
    public static Activity activity;
    Spinner tag;
    String[] tagsList;

    public static EventDialog2 newInstance(Activity activity) {
        Bundle args = new Bundle();

        EventDialog2 fragment = new EventDialog2();
        EventDialog2.activity = activity;
//        args.putLong("activity", activity);
//        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.manage_event2, null);
        TextView event_day = view.findViewById(R.id.event_day);
        event_day.setText(activity.date.toString());
        TextView event_start_time = view.findViewById(R.id.event_start_time);
        event_start_time.setText(activity.startTime.toString());
        TextView event_end_time = view.findViewById(R.id.event_end_time);
        event_end_time.setText(activity.endTime.toString());


        tag=(Spinner) view.findViewById(R.id.Spinner01);
        setTagsDropdown(); // 设置下拉选择框

        final TextView newstarttime = view.findViewById(R.id.event_starttime);
        final TextView newendtime = view.findViewById(R.id.event_endtime);

        newstarttime.setText(activity.startTime.toString());
        newendtime.setText(activity.endTime.toString());

        final long origin_start_time_s = activity.startTime.getTime();
         final long origin_end_time_s = activity.endTime.getTime();

        newstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        long end_time_s = Activity.strToTime(newendtime.getText().toString()).getTime();
                        String s=hourOfDay+":"+minute+":"+"00";
                        long new_s = Activity.strToTime(s).getTime();
                        if(new_s>=origin_start_time_s && new_s<=end_time_s)
                         newstarttime.setText(s);
                        else
                            Toast.makeText(getActivity(),"选择时间超出范围", Toast.LENGTH_LONG).show();
                    }
                }, 0, 0, true).show();

            }
        });

        newendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        long start_time_s = Activity.strToTime(newstarttime.getText().toString()).getTime();
                        String s=hourOfDay+":"+minute+":"+"00";
                        long new_e = Activity.strToTime(s).getTime();
                        if(new_e>=start_time_s && new_e<=origin_end_time_s)
                            newendtime.setText(s);
                        else
                            Toast.makeText(getActivity(),"选择时间超出范围", Toast.LENGTH_LONG).show();
                    }
                }, 0, 0, true).show();

            }
        });

        final EditText editText = view.findViewById(R.id.event_note);
        editText.setText(activity.note);

//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getFragmentManager(), "timePicker");

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbAdapter.insertActivity(new Activity(1,(String) tag.getSelectedItem(),
                                activity.date,Activity.strToTime(newstarttime.getText().toString())
                                ,Activity.strToTime(newendtime.getText().toString()),editText.getText().toString()));
//                        dbAdapter.updateActivity(activity.ID, activity);

                        dbAdapter.close();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbAdapter.close();
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void setTagsDropdown() {
        // 查询数据库内的所有标签
        Tag[] tags = dbAdapter.queryAllShowTag();

        // 初始化tagsList
        tagsList = new String[tags.length];

        int i = 0;
        for (Tag tag : tags) {
            tagsList[i] = tag.name;
            i++;
        }

        // 设置标签下拉选择栏的Adapter
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, tagsList);
        tag.setAdapter(tagsAdapter);
    }


}
