package bupt.id2017211631.timemasterbig.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import bupt.id2017211631.timemasterbig.R;

public class ColorAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> list;


    public ColorAdapter(Context context, List<Integer> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list!=null) {
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
        ColorViewHold hold;
        if (convertView==null) {
            hold=new ColorViewHold();
            convertView= LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, null);
            hold.View= convertView.findViewById(android.R.id.text1);
            convertView.setTag(hold);
        }else {
            hold=(ColorViewHold) convertView.getTag();
        }

        hold.View.setBackgroundColor(list.get(position));
        return convertView;
    }

    static class ColorViewHold{
        CheckedTextView View;
    }


}
