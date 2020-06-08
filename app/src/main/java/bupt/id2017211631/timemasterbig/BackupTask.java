package bupt.id2017211631.timemasterbig;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupTask extends AsyncTask<String, Void, Integer> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restoreDatabase";
    private Context mContext;

    public BackupTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub　　　
        // 默认路径是 /data/data/(包名)/databases/*.db
        File dbFile = mContext.getDatabasePath
                ("/data/data/bupt.id2017211631.timemasterbig/databases/"
                        + "activity.db");
        File dbFile_shm = mContext.getDatabasePath
                ("/data/data/bupt.id2017211631.timemasterbig/databases/"
                        + "activity.db-shm");
        File dbFile_wal = mContext.getDatabasePath
                ("/data/data/bupt.id2017211631.timemasterbig/databases/"
                        + "activity.db-wal");
        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "/TimeMaster/EBCBackup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        File backup_shm = new File(exportDir, dbFile_shm.getName());
        File backup_wal = new File(exportDir, dbFile_wal.getName());

        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                if (backup.exists() && backup_shm.exists() && backup_wal.exists()) {
                    fileCopy(dbFile, backup);
                    fileCopy(dbFile_shm, backup_shm);
                    fileCopy(dbFile_wal, backup_wal);
                } else {
                    backup.createNewFile();
                    backup_shm.createNewFile();
                    backup_wal.createNewFile();
                    fileCopy(dbFile, backup);
                    fileCopy(dbFile_shm, backup_shm);
                    fileCopy(dbFile_wal, backup_wal);
                }

                Looper.prepare();
                Toast toast = Toast.makeText(mContext, "备份成功", Toast.LENGTH_SHORT);
                toast.show();
                Looper.loop();
                return Log.d("backup", "ok");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("backup", "fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                fileCopy(backup_shm, dbFile_shm);
                fileCopy(backup_wal, dbFile_wal);
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, "恢复成功", Toast.LENGTH_SHORT);
                toast.show();
                Looper.loop();
                return Log.d("restore", "success");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return Log.d("restore", "fail");
            }
        } else {
            return null;
        }

    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}