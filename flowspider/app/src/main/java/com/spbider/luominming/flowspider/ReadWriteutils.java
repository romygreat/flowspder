package com.spbider.luominming.flowspider;
import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import java.io.File;

/**
 * Created by luominming on 2017/10/16.
 */
public class ReadWriteutils {
    private  Activity activity;
    public ReadWriteutils(Activity activity1) {
        this.activity=activity1;
    }

    public long getSDTotalSize() {
//        File path=Environment.();
        File path =Environment.getExternalStorageDirectory();//获取内置SD卡内存
//       path= path.getParentFile();
//        Toast.makeText()
        Log.d("SDmemory", path.getParentFile().getTotalSpace()/1024/1024/1024+"GB");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return (blockSize * totalBlocks)/1024;
    }
    public long  getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return  (blockSize * availableBlocks)/1024;
    }
    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(activity, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(activity, blockSize * availableBlocks);
    }
   public String forMat( long size){
       return Formatter.formatFileSize(activity, size);
   }

}
