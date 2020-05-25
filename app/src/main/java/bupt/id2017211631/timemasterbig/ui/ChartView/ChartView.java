package bupt.id2017211631.timemasterbig.ui.ChartView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bupt.id2017211631.timemasterbig.R;
import bupt.id2017211631.timemasterbig.SQL.Activity;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;
import bupt.id2017211631.timemasterbig.SQL.Tag;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartView extends Fragment {


    Date startDate = new Date();
    Date endDate = new Date();

    PlaceholderFragment placeholderFragment;

    String formatTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        return format.format(date);
    }

    void setDateOnClickListener(View DatePicker, final TextView DateText, final Calendar DateCalendar, final Date date) {
        date.setTime(DateCalendar.getTime().getTime());
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                DateCalendar.set(Calendar.YEAR, year);
                                DateCalendar.set(Calendar.MONTH, month);
                                DateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                date.setTime(DateCalendar.getTime().getTime());
                                DateText.setText(formatTime(date));
                                placeholderFragment.updateDate(startDate, endDate);
                            }
                        },
                        DateCalendar.get(Calendar.YEAR),
                        DateCalendar.get(Calendar.MONTH),
                        DateCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

    void initDateTime(TextView startDateText, Calendar startDateCalendar, TextView endDateText, Calendar endDateCalendar) {
        startDateText.setText(formatTime(startDateCalendar.getTime()));
        endDateText.setText(formatTime(endDateCalendar.getTime()));
    }

    void setDataTime(View view) {
        TextView startDateText = view.findViewById(R.id.startDate);
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDateCalendar.getTime().getTime() - DateUtils.DAY_IN_MILLIS * 6);
        setDateOnClickListener(startDateText, startDateText, startDateCalendar, startDate);
        TextView endDateText = view.findViewById(R.id.endDate);
        Calendar endDateCalendar = Calendar.getInstance();
        setDateOnClickListener(endDateText, endDateText, endDateCalendar, endDate);

        initDateTime(startDateText, startDateCalendar, endDateText, endDateCalendar);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_charts, null);
        setDataTime(view);
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();
        placeholderFragment = new PlaceholderFragment(
                view.findViewById(R.id.lineChartView), view.findViewById(R.id.columnChartView),
                startDate, endDate, dbAdapter, getActivity());
        return view;
    }

    public static class PlaceholderFragment {
        private LineChartView chartTop;
        private ColumnChartView chartBottom;
        private Context context;
        private LineChartData lineData;
        private ColumnChartData columnData;
        private Date startDate, endDate;
        private DBAdapter dbAdapter;

        public PlaceholderFragment(View lineChartView, View columnChartView, Date StartDate, Date EndDate, DBAdapter dbAdapter, Context context) {
            chartTop = (LineChartView) lineChartView;
            chartBottom = (ColumnChartView) columnChartView;
            startDate = StartDate;
            endDate = EndDate;
            this.dbAdapter = dbAdapter;
            this.context = context;
            generateInitialLineData();
            generateColumnData();
        }

        public void updateDate(Date StartDate, Date EndDate) {
            startDate = StartDate;
            endDate = EndDate;
            generateInitialLineData();
            generateColumnData();
        }

        private Tag[] getTags() {
            return dbAdapter.queryAllTag();
        }

        private float queryByDatesAndTag(String tag, Date startDate, Date endDate) {
            Activity[] res = dbAdapter.queryActivityByTime(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
            if (res == null) return 0;
            long ret = 0;
            for (Activity re : res) {
                if (re.tag.equals(tag)) {
                    ret += re.endTime.getTime() - re.startTime.getTime();
                }
            }
            return (float) ret / DateUtils.HOUR_IN_MILLIS;
        }

        private float queryByDateAndTag(String tag, Date date) {
            Activity[] res = dbAdapter.queryActivityByTime(new java.sql.Date(date.getTime()), new java.sql.Date(date.getTime()));
            if (res == null) return 0;
            long ret = 0;
            for (Activity re : res) {
                if (re.tag.equals(tag)) {
                    ret += re.endTime.getTime() - re.startTime.getTime();
                }
            }
            return (float) ret / DateUtils.HOUR_IN_MILLIS;
        }

        private void generateColumnData() {
            Tag[] tags = getTags();
            int numSubcolumns = 1;
            int numColumns = tags.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    if (startDate.compareTo(endDate) > 0) {
                        values.add(new SubcolumnValue(0, ChartUtils.pickColor()));
                    } else
                        values.add(new SubcolumnValue(queryByDatesAndTag(tags[i].name, startDate, endDate), tags[i].color));
                }

                axisValues.add(new AxisValue(i).setLabel(tags[i].name));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueColumnTouchListener(chartBottom));

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);

        }

        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */

        String getDay(long time) {
            Date date = new Date(time);
            return new SimpleDateFormat("MM-dd", Locale.CHINESE).format(date);
        }


        private void generateInitialLineData() {
            long Start = startDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS, End = endDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS;
            int size = (int) ((End - Start) / DateUtils.DAY_IN_MILLIS);


            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
//            for (int i = 0; i < numValues; ++i) {
//                values.add(new PointValue(i, 0));
//                axisValues.add(new AxisValue(i).setLabel(days[i]));
//            }
            for (int i = 0; ; ++i) {
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i).setLabel(getDay(Start)));
                Start += DateUtils.DAY_IN_MILLIS;
                if (Start > End) break;
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            line.setHasPoints(size <= 7);
            line.setFilled(true);
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);
            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(-0.1f, 24, (float) size + 0.1f, 0);
//            Viewport v = new Viewport(chartTop.getMaximumViewport());
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);
            chartTop.setZoomType(ZoomType.HORIZONTAL);
            chartTop.setViewportChangeListener(new LineChartViewportChangeListener(chartTop, 7));
        }

        private void generateLineData(int color, float range, int index) {
            long Start = startDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS;
            String tags;
            if (index == -1) {
                tags = null;
            } else {
                tags = getTags()[index].name;
            }
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();
            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            for (int i = 0; i < line.getValues().size(); i++) {
                if (tags != null) {
                    line.getValues().get(i).setTarget(line.getValues().get(i).getX(), queryByDateAndTag(tags, new Date(Start)));
                    Start += DateUtils.DAY_IN_MILLIS;
                } else {
                    line.getValues().get(i).setTarget(line.getValues().get(i).getX(), 0);
                }
            }
            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
            chartTop.setOnValueTouchListener(new ValueLineTouchListener(chartTop, tags));
        }

        private class ValueColumnTouchListener implements ColumnChartOnValueSelectListener {
            ColumnChartView columnChartView;

            public ValueColumnTouchListener(ColumnChartView columnChartView) {
                this.columnChartView = columnChartView;
            }

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                generateLineData(value.getColor(), 24, columnIndex);

                long Start = startDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS, End = endDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS;
                int size = (int) ((End - Start) / DateUtils.DAY_IN_MILLIS) + 1;
                if (value.getValue() * 60 > 120) {
                    Toast.makeText(context, "在选定的" + size + "天内，共花费了约" + (float) (Math.round(value.getValue() * 10)) / 10 + "小时在" + getTags()[columnIndex].name + "上", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "在选定的" + size + "天内，共花费了" + (int) (value.getValue() * 60) + "分钟在" + getTags()[columnIndex].name + "上", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onValueDeselected() {
                generateLineData(ChartUtils.COLOR_GREEN, 0, 0);
            }
        }


        private class ValueLineTouchListener implements LineChartOnValueSelectListener {
            LineChartView lineChartView;
            String tag;

            public ValueLineTouchListener(LineChartView lineChartView, String tag) {
                this.lineChartView = lineChartView;
                this.tag = tag;
            }


            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue pointValue) {
                if (tag == null) return;
                long Start = startDate.getTime() / DateUtils.DAY_IN_MILLIS * DateUtils.DAY_IN_MILLIS;
                long Now = (Start + DateUtils.DAY_IN_MILLIS);
                String date = getDay(Now);
                if (pointValue.getY() * 60 > 120) {
                    Toast.makeText(context, "在" + date + "，共花费了约" + (float) (Math.round(pointValue.getY() * 10)) / 10 + "小时在" + tag + "上", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "在" + date + "，共花费了" + (int) (pointValue.getY() * 60) + "分钟在" + tag + "上", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onValueDeselected() {
            }
        }

        private class LineChartViewportChangeListener implements ViewportChangeListener {
            LineChartView lineChartView;
            String tag;
            float changeSize;

            public LineChartViewportChangeListener(LineChartView lineChartView, float changeSize) {
                this.lineChartView = lineChartView;
                this.changeSize = changeSize;
                this.tag = null;
            }


            @Override
            public void onViewportChanged(Viewport viewport) {
                if (viewport.right - viewport.left <= changeSize) {
                    lineChartView.getLineChartData().getLines().get(0).setHasPoints(true);
                } else {
                    lineChartView.getLineChartData().getLines().get(0).setHasPoints(false);
                }
            }
        }
    }
}
