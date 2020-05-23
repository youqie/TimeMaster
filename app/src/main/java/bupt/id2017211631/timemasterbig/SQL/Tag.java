package bupt.id2017211631.timemasterbig.SQL;


public class Tag {
    public String name;
    public int color;
    public int isShow;

    @Override
    public String toString(){
        String result = "";
        result += "标签为" + this.name + "，";
        result += "颜色为" + this.color+ ".";
        result += "是否显示" + this.isShow;
        return result;
    }
}
