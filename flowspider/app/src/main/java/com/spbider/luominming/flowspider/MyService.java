package com.spbider.luominming.flowspider;

import android.app.Service;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luominming on 2018/3/4.
 */

public class MyService extends Service {

    private NetworkStatsManager networkStatsManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("test", "onCreate: ");
        networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getuid();
        return super.onStartCommand(intent, flags, startId);
    }
    private int getuid() {
        PackageManager pm = this.getPackageManager();
        int uid = 0;
        long time = System.currentTimeMillis();
        List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    long totalliu = 0L;
                    long totalRX = 0L;
                    long totalTx = 0L;
                    if ("android.permission.INTERNET".equals(premission)) {
                        uid = info.applicationInfo.uid;
                       String appName = (String) pm.getApplicationLabel(info.applicationInfo);
                        Drawable drawable = info.applicationInfo.loadIcon(pm);
                        try {
                            NetworkStats.Bucket bucketuid = new NetworkStats.Bucket();
                            NetworkStats Stats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "",
                                    0, System.currentTimeMillis(), uid);
                            int nextbucket = 0;
                            Stats.getNextBucket(bucketuid);
                            totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                            totalRX += bucketuid.getRxBytes();
                            totalTx += bucketuid.getTxBytes();
                            while (Stats.hasNextBucket()) {
                                Stats.getNextBucket(bucketuid);
//
                                totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                                totalRX += bucketuid.getRxBytes();
                                totalTx += bucketuid.getTxBytes();
                                nextbucket++;
                            }
                            HashMap<String, Object> test = new HashMap<>();


//                                test.put("appname", appName);
//                                test.put("icon", drawable);
//                                test.put("RX", "上行" + formate(totalTx));
//                                test.put("TX", "下行" + formate(totalRX));//formate(totalRX))正确正确结果为下行，KEY值是需要修改的
//                                test.put("total", totalliu);
//                                apptest.add(test);
////                                Log.i("luominmingWIFI", appName + "," + formate(totalTx) + "   下行: " + formate(totalRX) +
////                                        "  总计: " + formate(totalliu) + "\n");
//                                //if (dislog)
                            if (totalliu > 0)  Log.i("Service", appName + "," + formate(totalTx) + "," + formate(totalRX) +
                                        "," + formate(totalliu) + "\n");
                            } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }
            }
        return uid;
    }
    public String formate(long l) {
        String t = Formatter.formatFileSize(getApplicationContext(), l);
        return t;
    }

}
