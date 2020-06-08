package bupt.id2017211631.timemasterbig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TabFragment extends Fragment {
    private Context context;
    private String content; //Fragment的显示内容

    public TabFragment() {

    }

    @SuppressLint("ValidFragment")
    public TabFragment(Context context, String content) {
        this.context = context;
        this.content = content;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == "备份") {
            View view = View.inflate(getActivity(), R.layout.fragment_backup, null);
            Button backupBtn = view.findViewById(R.id.backup);
            Button cancelBtn = view.findViewById(R.id.cancelBU);
            backupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BackupTask(getActivity()).execute("backupDatabase");
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            return view;
        } else {
            View view = View.inflate(getActivity(), R.layout.fragment_recover, null);
            Button recoverBtn = view.findViewById(R.id.recoverBtn);
            Button cancelBtn = view.findViewById(R.id.cancelRC);
            recoverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BackupTask(getActivity()).execute("restoreDatabase");
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            return view;
        }
    }

}
