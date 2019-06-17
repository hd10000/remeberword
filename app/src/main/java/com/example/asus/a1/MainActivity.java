package com.example.asus.a1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView mword;
    TextView mexplain;
    TextView msymbol;
    read r;
    String filename;
    ArrayList<word> w;
    char[] chars;
    char c;
    int index;//当前单词下标
    boolean isPicked;//文件否已选择
    int[] indexs;

    SharedPreferences.Editor editor;//用来写配置文件
    SharedPreferences pref;//用来读取配置文件
    MyDBHelper dbHelper;

    CountDownTimer timer;
    boolean isTimer;

    public static final  String TAG="MainActivity";


    String[] wordclass = {"n.","adj.","v"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//               index+=1;
                pickXls();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener(){
               @Override
               public boolean onLongClick(View v) {
                   if (index!=0){
                       index=0;
                       Toast.makeText(MainActivity.this,
                               "已将下标清零，再次长按撤销",
                               Toast.LENGTH_LONG).show();
                       timer.cancel();
                       setTip(filename,index);
                   }else{
                       index=pref.getInt("filename",0);
                       setTip(filename,index);
                       Toast.makeText(MainActivity.this,
                               "已从第 "+index+" 个回复",
                               Toast.LENGTH_LONG).show();
                   }

                   return true;
               }
           }

        );

        new Permisson().checkPermission(MainActivity.this);
        init();

    }
    private void init(){
        mword = findViewById(R.id.word);
        mexplain = findViewById(R.id.explain);
        msymbol = findViewById(R.id.sybolm);
        index=0;
        indexs=new int[5];
        indexs[0]=1;
        indexs[1]=2;
        indexs[2]=3;

        isPicked=false;
        editor = getSharedPreferences("config",MODE_PRIVATE).edit();
        pref = getSharedPreferences("config",MODE_PRIVATE);
        dbHelper = new MyDBHelper(this,"lib.db",null,1);

        Log.d(TAG, "init: lastfilename" + pref.getString("lastFileName","null"));
        filename = "/storage/emulated/0/Download/test.xls";
        readConfig();
        r = new read();
        if(filename==null){
            msymbol.setText("点击+号添加词库");
        }else {
            msymbol.setText("继续上次文件"+filename);
            mexplain.setText("第 "+index+" 个");
        }

        timer = new CountDownTimer(300000, 1500) {
            public void onTick(long millisUntilFinished) {
                index++;
                readword(index);
            }
            public void onFinish() {

            }
        };
        isTimer=false;
    }
    public void readword(int i){

            String[] line=r.readline(filename,i,this,indexs);
            mword.setText(line[0]+" ");
            msymbol.setText(line[1]);
            mexplain.setText(line[2]);
        Log.d(TAG, "readword: "+filename+" i "+i);
            editor.putInt(filename,i);
            editor.apply();
        Log.d(TAG, "readword: "+pref.getInt(filename,0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        isTimer=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            timer.cancel();
            isTimer = false;

        }
        if ((event.getAction()==MotionEvent.ACTION_UP)){
            timer.start();
            isTimer = true;
        }

        return super.onTouchEvent(event);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.num00:
                indexs[0]=0;
                break;
            case R.id.num01:
                indexs[0]=1;
                break;
            case R.id.num02:
                indexs[0]=2;
                break;
            case R.id.num03:
                indexs[0]=3;
                break;

            case R.id.num10:
                indexs[1]=0;
                break;
            case R.id.num11:
                indexs[1]=1;
                break;
            case R.id.num12:
                indexs[1]=2;
                break;
            case R.id.num13:
                indexs[1]=3;
                break;

            case R.id.num20:
                indexs[2]=0;
                break;
            case R.id.num21:
                indexs[2]=1;
                break;
            case R.id.num22:
                indexs[2]=2;
                break;
            case R.id.num23:
                indexs[2]=3;
                break;

    }
        Toast.makeText(
                MainActivity.this,
                "indexs[]:"+indexs[0]+" "+indexs[1]+" "+indexs[2],
                Toast.LENGTH_LONG
        ).show();
        return super.onOptionsItemSelected(item);
    }
    public void pickXls(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            if (requestCode == 1) {
                Uri uri = data.getData();
                filename = uri.getPath().split("raw:")[1];//从系统文件管理器得到的文件路径


                getIndex(filename);//创建选择文件对应的 键名
                editor.putString("lastFileName",filename);
                editor.apply();
                setTip(filename,index);

                Log.d(TAG, "onActivityResult: "+filename);
            }
        }
    }
    public void getIndex(String filename){
        index = pref.getInt(filename,0);

        Log.d(TAG, "Index: "+index);
    }
    public void test(){

    }
    public void readConfig(){
        filename = pref.getString("lastFileName","");
        index = pref.getInt(filename,0);
    }
    public void setTip(String filename1,int index1){
        mword.setText("触摸开始");
        msymbol.setText("当前词库："+filename);
        mexplain.setText("第 "+index+" 个");
    }

}
