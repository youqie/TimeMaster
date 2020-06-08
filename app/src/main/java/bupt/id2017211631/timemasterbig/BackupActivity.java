package bupt.id2017211631.timemasterbig;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class BackupActivity extends AppCompatActivity {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("备份与恢复");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button backupBtn = findViewById(R.id.backup);
        Button restoreBtn = findViewById(R.id.restore);
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackupTask(BackupActivity.this).execute("backupDatabase");
            }
        });
        restoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackupTask(BackupActivity.this).execute("restoreDatabase");
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
}
