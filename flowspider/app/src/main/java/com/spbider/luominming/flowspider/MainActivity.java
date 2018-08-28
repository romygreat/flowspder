package com.spbider.luominming.flowspider;
import android.Manifest;
import com.spbider.luominming.flowspider.BuildConfig;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.Service;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.LoginFilter;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    String TAG = "test";
    private TextView test;
    String appName;
    private NetworkStatsManager networkStatsManager;
    Drawable drawable;
    TelephonyManager tm;
    ArrayList<HashMap<String, Object>> apptest = new ArrayList<>();
    ArrayList<HashMap<String, Object>> apptest1 = new ArrayList<>();
    ListView mListView = null;
    MyListAdapter myAdapter = null;
    String mtype = "WIFI";
    int countTime = 0;
    String subId;
   TimerTask timerTask=null;
    boolean b = false;
    boolean dislog = false;
    int fo=0;
    TrafficStats trafficStats=new TrafficStats();
    private final long  testTime=1000*60*62;
    private final int finishTime=8;
    private String fileNameCreate= "/sdcard/file.txt";
    private String filePredix="/sdcard/";
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if(countTime==0){
                    test.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    test.setText("\n\n\n\n"+"龙旗测试部提醒您\n\n"+"第"+(countTime+1)+"小时流量监控测试进行中"+"\n"+
                            "请保持电量充足勿动手机,耐心等待"
                    );
                }
                else {test.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
              test.setText("\n\n\n\n"+"龙旗测试部提醒您\n\n"+"第"+(countTime)+"小时流量监控测试进行中"+"\n"+
              "请保持电量充足勿动手机,耐心等待"
              );}
              if(countTime>=finishTime){ //数据为8
                  jishiboolean=true;
                  test.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                  test.setText("数据正在处理中，请稍后！");
                  try {
                      Thread.sleep(5*60*1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  test.setText("\n\n\n\n测试完成\n\n请查看文件管理器中的"+editTextString+".txt文件");
              }
            }
        }
    };
    private String editTextString="流量统计结果";
    private boolean jishiboolean=true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initView();
        networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
        setOnClickListenser();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
            b = true;
            tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        }
        else
            {
                tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Log.i(TAG, "onCreate: "+this.getExternalFilesDir("text/plain"));
        test.setTextColor(Color.BLACK);
        test.setText("测试步骤：\n\n"+"1、按照用例要求设置条件完成后，点击左上角菜单选择权限设置Uasge Access为on\n\n" +
                "2、建议将语言设置为英语，然后点击开始专项测试\n\n"+"" +
                "3、点击后耐心等待数据结果，从文件管理器将文件数据导出"+editTextString+"到电脑\n"+"");
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
    private void setOnClickListenser() {
        test.setOnClickListener(this);
    }
    private void initView() {
        test = findViewById(R.id.test);
        mListView = findViewById(R.id.listView);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            dayin("settings");
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.access) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//            dayin("test");
//            Log.i(TAG, "onNavigationItemSelected:test1 ");
        } else if (id == R.id.getWIFI) {
//            prepareDis();
//            Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//            intent1.putExtra("type", "WIFI");
//            startActivity(intent1);
//            MainActivity.this.finish();
            dayin("点击专项测试后台运行");
        } else if (id == R.id.getData) {
//            Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//            intent1.putExtra("type", "Data");
//            startActivity(intent1);
//            MainActivity.this.finish();
//            if (mtype.equals("Data"))
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
               subId = tm.getSubscriberId();
                if (subId == null)
                    dayin("请查看SIM卡是否插入");
            else{
                dayin("点击专项测试后台于运行");
            }
        }
        else if (id == R.id.jishi) {
            ShellUtils.execCommand("logcat -c",false,false);
           // countTime = 0;
            if(countTime>=9){
                countTime=0;
            }
           if(!hasPermissionToReadNetworkStatstest()){
               requestReadNetworkStats();
           }
           else {
               {
                   editlogName();
               }
           }
//        } else if (id == R.id.nav_share) {
//            dayin("share");
//            Vibrate(MainActivity.this,new long[]{2000,1000,2000,1000,2000,1000,2000,1000},true);
//          //generateCommonIntent("/sdcard/"+editTextString+".txt");
//        } else if (id == R.id.nav_send) {
////           cancel();
//            CounTimeUtils.startCountDownTime(CounTimeUtils.getDateSeconds());
//            Log.i(TAG, "onNavigationItemSelected: onTip");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test:
// test.setText("");
            default:
                break;
        }
    }
    private void prepareDis() {
        NetworkStats.Bucket bucket = null;
        {
            getMoblieData();
            try {
                bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                        "", 0, System.currentTimeMillis());
                bucket.getUid();
                Log.i(TAG, "prepareDis: "+ bucket.getUid());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Log.i("Info", "Total: " + Formatter.formatFileSize(MainActivity.this,
                bucket.getRxBytes() + bucket.getTxBytes())); //手机显示使用的是总量的数据
        Log.i("Info", "rx: " + Formatter.formatFileSize(MainActivity.this,
                bucket.getRxBytes())); //手机的下行速率
        Log.i("Info", "tx: " + Formatter.formatFileSize(MainActivity.this,
                bucket.getTxBytes())); //手机的上行速率

        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
        //ealWithRepaireActivity进入故障处理
        startActivity(intent1);
        MainActivity.this.finish();
//       // test.setText(
//                Formatter.formatFileSize(MainActivity.this,
//                        bucket.getRxBytes() + bucket.getTxBytes()));
////                }
    }

    public void dayin(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
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
                    if ("android.permission.INTERNET".equals(premission))
                    {
                        uid = info.applicationInfo.uid;
                        appName = (String) pm.getApplicationLabel(info.applicationInfo);
                        drawable = info.applicationInfo.loadIcon(pm);
//                         Log.i(TAG, "name:"+appName+"" +
//                                "\ngetuid: "+uid);

                        try {
                            NetworkStats.Bucket bucketuid = new NetworkStats.Bucket();
                            NetworkStats stats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "",
                                    0, System.currentTimeMillis(), uid);
                            int nextbucket = 0;
                            stats.getNextBucket(bucketuid);
                        //                            Log.i(TAG, "getuid: "+bucketuid1);
                            totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                            totalRX += bucketuid.getRxBytes();
                            totalTx += bucketuid.getTxBytes();
//                            Log.i(TAG, "getuid: "+stats.hasNextBucket());
                            while (stats.hasNextBucket()) {
                                stats.getNextBucket(bucketuid);
                              //  Log.i(TAG, "getuid: "+uid+"nextBucket"+nextbucket+"\n");
                             //   Log.i(TAG, appName+"totalnextbucket: "+formate(bucketuid.getRxBytes()+bucketuid.getTxBytes()));
                                totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                                totalRX += bucketuid.getRxBytes();
                                totalTx += bucketuid.getTxBytes();
                                nextbucket++;
                            }
                            HashMap<String, Object> test = new HashMap<>();
                           if (totalliu > 0)
                            {
                                String contentString=appName+","+formate(totalTx)+","+formate(totalRX)+","+formate(totalliu);
                                createFile(fileNameCreate,contentString);
//                                test.put("appname", appName);
//                                test.put("icon", drawable);
//                                test.put("RX", "上行" + formate(totalTx));
//                                test.put("TX", "下行" + formate(totalRX));//formate(totalRX))正确正确结果为下行，KEY值是需要修改的
//                                test.put("total", totalliu);
//                                apptest.add(test);
//                                Log.i("luominmingWIFI", appName + "," + formate(totalTx) + "   下行: " + formate(totalRX) +
//                                        "  总计: " + formate(totalliu) + "\n");
                                if (dislog)
                                    Log.i("luominmingWIFI", appName + "," + formate(totalTx) + "," + formate(totalRX) +
                                            "," + formate(totalliu) );
                            }
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
        String t = Formatter.formatFileSize(MainActivity.this, l);
        return t;
    }
    public void jishi(long period) {
        Timer timer = new Timer();
        timerTask = new TimerTask(){
            @Override
            public void run() {
                dislog = true;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }


                subId = tm.getSubscriberId();//可能报错，需要注意
                apptest.clear();  if(countTime<9){
//                Thread  logThread= new Thread(){
//                        @Override
//                        public void run() {
//                            ShellUtils.execCommand("logcat -c",false,false);
//                            for(int i=0;i<10000;i++){
//                                for(int j=0;j<100;j++)
//                                    fo=0;
//                            }
//                            ShellUtils.execCommand("logcat -v raw -s luominmingWIFI:I  -f /sdcard/"+editTextString+".txt",false,false);
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                logThread.start();

                    {  // ShellUtils.execCommand("logcat -c",false,false);
                        Log.i("luominmingWIFI", "APP名称"+",上行"+",下行"+",总量");
                        Message msg=handler.obtainMessage();
                        msg.what=1;
                        msg.obj=countTime;
                        handler.sendMessage(msg);
                    }
                    if(subId!=null)
                    {
                        for(int i=0;i<10000;i++){
                            for(int j=0;j<100;j++)
                         fo=0;
                        }
                       createFile(fileNameCreate,"-----第" + countTime+ "小时SIM卡流量统计开始-----");
                        Log.i("luominmingWIFI", "-----第" + countTime+ "小时SIM流量统计开始-----");
                         getMoblieData();
                    }
                    Log.i("luominmingWIFI", "-----第" + countTime + "小时WIFI流量统计开始-----");
                    createFile(fileNameCreate,"-----第" + countTime+ "小时WIFI流量统计开始-----");
                    getuid();
                    //ShellUtils.execCommand()

                }
                Message msg=handler.obtainMessage();
                msg.what=1;
                msg.obj=countTime;
                handler.sendMessage(msg);
                countTime++;

            }
        };
        timer.schedule(timerTask, 2000, period);
    }

    class MyListAdapter extends BaseAdapter {
        public MyListAdapter(Context context) {
            mContext = context;
        }
        public int getCount() {
            return apptest.size();
        }
        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_main_item, null);
                holder.iamge = convertView.findViewById(R.id.iv_main_item_laucher);
                holder.title = convertView.findViewById(R.id.tv_main_item_name);
                holder.text = convertView.findViewById(R.id.tv_main_item_download);
                holder.upload = convertView.findViewById(R.id.tv_main_item_upload);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            try {
                holder.title.setText((String) apptest.get(position).get("appname"));
                holder.text.setText((String) apptest.get(position).get("RX"));
                holder.upload.setText((String) apptest.get(position).get("TX"));//没有区分上行与下行
                //  Log.i(TAG, "getView: " + apptest.get(position).get("TX"));
                holder.iamge.setImageDrawable((Drawable) apptest.get(position).get("icon"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            // throw  new IllegalStateException();
            apptest.clear();
        }
        private Context mContext;
    }
    public class ViewHolder {
        ImageView iamge = null;
        TextView title = null;
        TextView text = null;
        TextView upload = null;
    }
    public void getMoblieData() {
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
                        appName = (String) pm.getApplicationLabel(info.applicationInfo);
                        drawable = info.applicationInfo.loadIcon(pm);
//                         Log.i(TAG, "name:"+appName+"" +
//                                "\ngetuid: "+uid);
                        try {
                            NetworkStats.Bucket bucketuid = new NetworkStats.Bucket();
                            NetworkStats Stats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, subId,
                                    0, System.currentTimeMillis(), uid);
                            int nextbucket = 0;
                            Stats.getNextBucket(bucketuid);
                            totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                            totalRX += bucketuid.getRxBytes();
                            totalTx += bucketuid.getTxBytes();
                            while (Stats.hasNextBucket()) {
                                Stats.getNextBucket(bucketuid);
                                totalliu += bucketuid.getRxBytes() + bucketuid.getTxBytes();
                                totalRX += bucketuid.getRxBytes();
                                totalTx += bucketuid.getTxBytes();
                                nextbucket++;
                            }
                            HashMap<String, Object> test = new HashMap<>();
                            if (totalliu > 0) {
                                String contentString=appName+","+formate(totalTx)+","+formate(totalRX)+","+formate(totalliu);
                                createFile(fileNameCreate,contentString);
//                                test.put("appname", appName);
//                                test.put("icon", drawable);
//                                test.put("RX", "上行" + formate(totalTx));
//                                test.put("TX", "下行" + formate(totalRX));//formate(totalRX))正确正确结果为下行，KEY值是需要修改的
//                                test.put("total", totalliu);
//                                apptest1.add(test);
                                if(dislog)
                                Log.i("luominmingWIFI", appName + "," + formate(totalTx) + "," + formate(totalRX) +
                                        "," + formate(totalliu) + "\n");
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
       // startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
    private boolean hasPermissionToReadNetworkStatstest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }
    private void requestReadNetworkStats() {
        dayin("请将龙旗流量监控设置为开启或ON");
        dayin("请将龙旗流量监控设置为开启或ON");
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
    public void editlogName() {
        final EditText inputedit=new EditText(MainActivity.this);
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(MainActivity.this);
        alertdialog.setTitle("请输入导出文件名称");
        alertdialog.setView(inputedit);
        alertdialog.setIcon(R.drawable.ic_menu_send);
        alertdialog.setNegativeButton("取消",null);
        alertdialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 editTextString = inputedit.getText().toString();
                fileNameCreate = filePredix+inputedit.getText().toString()+".txt";
                if(editTextString==null)
                {
                    dayin("不能为空");
//                    editTextString="testnull";
                    fileNameCreate=filePredix+"流量统计结果.txt";
                }
                if (countTime==0){
                    createFile(fileNameCreate,"-------龙旗软件测试部流量统计数据----------");
                    createFile(fileNameCreate,"---------designed by 骆敏明----------------");
                    createFile(fileNameCreate, "APP名称"+",上行"+",下行"+",总量");
                    if(jishiboolean){
                        jishi(testTime);
                        jishiboolean=false;
                        dayin("专项测试开始进行");
                    }else {
                        dayin("正在测试中");
                    }
                } else{
                    dayin("正在测试中,如需重新测试请重启APK");
                    dayin("正在测试中,如需重新测试请重启APK");
                }
            }
        });
        alertdialog.show();
    }
    private  Intent generateCommonIntent(String filePath) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_OPENABLE);;
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, "text/plain");

//        File file = new File("/storage/emulated/0/test.txt");
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "text/*");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent); // Cr
        return intent;
    }
    private  Uri getUri(Intent intent, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri =  uri = Uri.fromFile(file);
            FileProvider.getUriForFile(MainActivity.this.getApplicationContext(),
                    MainActivity.this.getApplicationContext().getPackageName() + BuildConfig.APPLICATION_ID+".GenericFileProvider",
                    file);
//            添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
    public void createFile(String fileName,String content) {
        //if (fileName != null)
        {
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    try (BufferedWriter bufferedOutputStream1 = new BufferedWriter(
                            new FileWriter(file, true))) {
                        bufferedOutputStream1.write(content+"\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
            try {
                try (BufferedWriter bufferedOutputStream1 = new BufferedWriter(
                        new FileWriter(file, true))) {
                    bufferedOutputStream1.write(content+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }}
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
    public void vibrateCancel(){
        Vibrator vib = (Vibrator)MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }

}

