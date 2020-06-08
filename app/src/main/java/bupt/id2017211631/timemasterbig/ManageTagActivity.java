package bupt.id2017211631.timemasterbig;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bupt.id2017211631.timemasterbig.SQL.Tag;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;
import bupt.id2017211631.timemasterbig.adapter.ColorAdapter;

public class ManageTagActivity extends AppCompatActivity {

    String[] tagsList;
    List<Integer> colorList = new ArrayList<>();

    DBAdapter dbAdepter;

    Spinner tag;
    Button deleteBtn;
    Button cancelBtn;
    Button addBtn;
    Button updateBtn;
    EditText newTagView;
    Spinner color;

    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_tag);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("管理标签");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View addView = View.inflate(this, R.layout.activity_add, null);
        addBtn = findViewById(R.id.add);
        updateBtn = findViewById(R.id.update);
        deleteBtn = findViewById(R.id.delete);
        cancelBtn = findViewById(R.id.cancel);
        tag = findViewById(R.id.Spinner01);
        newTagView = findViewById(R.id.editText3);
        color = findViewById(R.id.Spinner02);

        dbAdepter = new DBAdapter(this);
        dbAdepter.open();//启动数据库

        // 取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setTagsDropdown(); // 设置下拉选择框
        setColorsDropdown();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag[] newtag = dbAdepter.queryTagByname(newTagView.getText().toString());
                if(newtag!=null)
                {//newtag[0].color = Color.parseColor(color.getText().toString());
                    newtag[0].isShow=1;
                    dbAdepter.updateTag(newtag[0]);
                    Toast.makeText(ManageTagActivity.this,"新增"+newtag[0].name+"标签"
                            , Toast.LENGTH_LONG).show();
                }
                else
                {
                        Tag addtag = new Tag();
                        addtag.name=newTagView.getText().toString();
//                        addtag.color= Color.parseColor(color.getText().toString());
                        addtag.color = Integer.parseInt(color.getSelectedItem().toString());
                        addtag.isShow=1;
                        dbAdepter.insertTag(addtag);
                       Toast.makeText(ManageTagActivity.this,"新增"+addtag.name+"标签"
                               , Toast.LENGTH_LONG).show();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag[] newtag = dbAdepter.queryTagByname(tag.getSelectedItem().toString());
               newtag[0].color = Integer.parseInt(color.getSelectedItem().toString());
                    dbAdepter.updateTag(newtag[0]);
                    Toast.makeText(ManageTagActivity.this,"修改"+newtag[0].name+"颜色为"+
                            Integer.parseInt(color.getSelectedItem().toString())
                            , Toast.LENGTH_LONG).show();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Tag[] newtag = dbAdepter.queryTagByname(tag.getSelectedItem().toString());
               newtag[0].isShow = 0;
               dbAdepter.updateTag(newtag[0]);
               Toast.makeText(ManageTagActivity.this,"删除"+newtag[0].name+"成功", Toast.LENGTH_LONG).show();
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
            tagsList[i] = tag.name;
            i++;
        }

        // 设置标签下拉选择栏的Adapter
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(ManageTagActivity.this,
                android.R.layout.simple_spinner_dropdown_item, tagsList);
        tag.setAdapter(tagsAdapter);
    }

    private void setColorsDropdown() {

        colorList.add(Color.rgb(243, 156, 18));
        colorList.add(Color.rgb(26, 188, 156));
        colorList.add(Color.rgb(52, 152, 219));
        colorList.add(Color.rgb(254, 67, 101));
        colorList.add(Color.rgb(44, 87, 201));
        colorList.add(Color.rgb(84, 127, 153));
        colorList.add(Color.rgb(66, 164, 210));
        colorList.add(Color.rgb(120, 34, 142));
        colorList.add(Color.rgb(230, 87, 31));

//        // 设置标签下拉选择栏的Adapter
//        ArrayAdapter<String> colorsAdapter = new ArrayAdapter<String>(ManageTagActivity.this,
//                android.R.layout.simple_spinner_dropdown_item, tagsList);

        ColorAdapter colorsAdapter = new ColorAdapter(ManageTagActivity.this,colorList);

        color.setAdapter(colorsAdapter);
    }
}
