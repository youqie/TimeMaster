package bupt.id2017211631.timemasterbig.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;


public class EventDialog extends DialogFragment {
    DBAdapter dbAdapter;
    Activity activity;

    public static EventDialog newInstance(long id) {
        Bundle args = new Bundle();

        EventDialog fragment = new EventDialog();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        long id = getArguments().getLong("id");

        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();
        activity = dbAdapter.queryActivityByID(id)[0];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.manage_event, null);
        TextView event_day = view.findViewById(R.id.event_day);
        event_day.setText(activity.date.toString());
        TextView event_start_time = view.findViewById(R.id.event_start_time);
        event_start_time.setText(activity.startTime.toString());
        TextView event_end_time = view.findViewById(R.id.event_end_time);
        event_end_time.setText(activity.endTime.toString());
        TextView title = view.findViewById(R.id.event_type);
        title.setText(activity.tag);
        title.setBackgroundColor(dbAdapter.queryTagByname(activity.tag)[0].color);
        final EditText editText = view.findViewById(R.id.event_note);
        editText.setText(activity.note);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.note = editText.getText().toString();
                        dbAdapter.updateActivity(activity.ID, activity);
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
}
