package bupt.id2017211631.timemasterbig;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import bupt.id2017211631.timemasterbig.SQL.Tag;
import bupt.id2017211631.timemasterbig.SQL.DBAdapter;

public class ManageTagActivity extends AppCompatActivity {

    String[] tagsList;

    DBAdapter dbAdepter;

    Spinner tag;
    Button deleteBtn;
    Button cancelBtn;
    Button addBtn;
    Button updateBtn;
    EditText newTagView;
    EditText color;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_tag);

        // View addView = View.inflate(this, R.layout.activity_add, null);
        addBtn = (Button) findViewById(R.id.add);
        updateBtn = (Button) findViewById(R.id.update);
        deleteBtn = (Button) findViewById(R.id.delete);
        cancelBtn = (Button) findViewById(R.id.cancel);
        tag = (Spinner) findViewById(R.id.Spinner01);
        newTagView = (EditText) findViewById(R.id.editText3);
        color = (EditText) findViewById(R.id.editText2);

        dbAdepter = new DBAdapter(this);
        dbAdepter.open();//启动数据库


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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag[] newtag = dbAdepter.queryTagByname(newTagView.getText().toString());
                if(newtag!=null)
                {try{//newtag[0].color = Color.parseColor(color.getText().toString());
                    newtag[0].isShow=1;
                    dbAdepter.updateTag(newtag[0]);
                    Toast.makeText(ManageTagActivity.this,"新增"+newtag[0].name+"标签"
                            , Toast.LENGTH_LONG).show();
                }
                 catch (Exception e) {color.setText("颜色不合法");}
                }
                else
                {
                   try
                   {
                        Tag addtag = new Tag();
                        addtag.name=newTagView.getText().toString();
                        addtag.color= Color.parseColor(color.getText().toString());
                        addtag.isShow=1;
                        dbAdepter.insertTag(addtag);
                       Toast.makeText(ManageTagActivity.this,"新增"+newtag[0].name+"标签"
                               , Toast.LENGTH_LONG).show();
                   }
                   catch (Exception e) {color.setText("颜色不合法");}
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tag[] newtag = dbAdepter.queryTagByname(tag.getSelectedItem().toString());
                try{newtag[0].color = Color.parseColor(color.getText().toString());
                    dbAdepter.updateTag(newtag[0]);
                    Toast.makeText(ManageTagActivity.this,"修改"+newtag[0].name+"颜色为"+color.getText().toString()
                            , Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {color.setText("颜色不合法");}
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
}
