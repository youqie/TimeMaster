package bupt.id2017211631.timemasterbig.SQL;


import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class Activity {
    public int ID = -1;
    public String tag;
    public Date date;
    public Time startTime;
    public Time endTime;
    public String note;

    public  Activity()
    {}

    public Activity(int id, String tag, Date date, Time startTime, Time endTime, String note)
    {
        this.ID = id;
        this.tag = tag;
        this.date = date;
        this.startTime =startTime;
        this.endTime = endTime;
        this.note = note;
    }


    @Override
    public String toString(){
        String result = "";
        result += "id为" + this.ID +"，";
        result += "标签为" + this.tag + "，";
        result += "日期为" + this.date.toString() + "，";
        result += "开始时间为" + this.startTime.toString() + "，";
        result += "终止时间为" + this.endTime.toString() +"，";
        result += "注释为" + this.note+".";
        return result;
    }

    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    public static java.sql.Time strToTime(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time time = new java.sql.Time(d.getTime());
        return time;
    }
}


