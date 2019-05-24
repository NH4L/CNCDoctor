package cn.aysst.www.doctor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.doctor.utils.Http;
import cn.aysst.www.doctor.utils.MyBitmapUtils;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.aysst.www.doctor.HomepageActivity.getBitmapFromUri;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private TabLayout mTabLayout;
    private Fragment[] mFragmensts;
    private TextView username, textSignature;
    private String account = "";
    private String name, email, phone, signature, sex, portraitAddr;
    private float money = 0.00f;
    private String user_json = "";
    private CircleImageView portraitImage;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();
    private String result = "";
    private Bitmap portraitBit = null;

    private static final int REQUEST_WE_STORAGE_PER = 100;
    private static final int REQUEST_RE_STORAGE_PER = 101;

    private static final int isChangePortrait = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initView(navigationView);

    }

    private void initView(NavigationView navigationView) {

        mFragmensts = DataGenerator.getFragments();
        manager = getSupportFragmentManager();

        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                for (int i=0; i<mTabLayout.getTabCount(); i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for(int i=0; i<3; i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }


        View header = navigationView.getHeaderView(0);
//        Intent intent = getIntent();
//        account = intent.getStringExtra("account");
        account = "lcy";
        user_json = httpPostUserinfoReq(account);
        Log.d("user", user_json);
        try {
            JSONObject jsonObject = new JSONObject(user_json);
            name = (String) jsonObject.getString("name");
            email = (String) jsonObject.getString("email");
            sex = (String)jsonObject.getString("sex");
            money = (float) jsonObject.getDouble("money");
            phone = (String)jsonObject.getString("phone");
            signature = (String)jsonObject.getString("signature");
            portraitAddr = (String)jsonObject.getString("portraitAddr");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        username = (TextView)header.findViewById(R.id.main_user_account);
        username.setText(name);
        textSignature = (TextView)header.findViewById(R.id.main_user_signature);
        portraitImage = (CircleImageView)header.findViewById(R.id.portrait);

        textSignature.setText(signature);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//存储用户名
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putFloat("money", money);
        editor.putString("sex", sex);
        editor.putString("phone", phone);
        editor.putString("signature", signature);

        editor.commit();//提交修改

        if (portraitAddr.equals("无")) {
            portraitImage.setImageResource(R.drawable.people_fill);
        } else {
            new MyBitmapUtils().disPlay(portraitImage, portraitAddr);
            //new showPortraitAsyncTask().execute(portraitAddr);
        }

        username.setOnClickListener(this);
        textSignature.setOnClickListener(this);
        portraitImage.setOnClickListener(this);

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            if (requestPer()){
                openGallery();
            }
        } else if (id == R.id.nav_file) {
            if (requestPer()){
                openFile();
            }
        } else if (id == R.id.nav_record) {
            if (requestPer()){
                openRecord();
            }
        } else if (id == R.id.nav_aboutAs) {
            goAboutUs();
        } else if (id == R.id.nav_money){
            goRecharge();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始化时展示头像
     */
    class showPortraitAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            return getPortrait(params[0]);
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            portraitImage.setImageBitmap(result);
//            Toast.makeText(MainActivity.this, "图片加载成功", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getPortrait(String fileUrl) {
        Bitmap result = null;
        try {
            Log.d("imgUrl", fileUrl);
            URL imgURI = new URL(fileUrl);
            URLConnection conn = imgURI.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
            portraitBit = getBitmapFromUri(MainActivity.this, uri);
            result = portraitBit;
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void onTabItemSelected(int position){
        transaction = manager.beginTransaction();
        hideAllFragemnt(manager);
        switch (position){
            case 0:
                showFragment("fragment_home", mFragmensts[0]);
                break;
            case 1:
                showFragment("fragment_release", mFragmensts[1]);
                break;
            case 2:
                showFragment("fragment_notice", mFragmensts[2]);
                break;
        }
    }

    private void hideAllFragemnt(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment:fragments){
            transaction.hide(fragment);
        }
    }

    private String httpPostUserinfoReq(String account) {
        FormBody body = new FormBody.Builder()
                .add("msg", "userInfoAction")
                .add("account", account)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        result = response.body().string();
                        Log.d("postData", result);
                    } else {
                        throw new IOException("Unexpected code" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_user_account:
                Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case R.id.main_user_signature:
                Intent intent1 = new Intent(MainActivity.this, HomepageActivity.class);
                startActivity(intent1);
                break;
            case R.id.portrait:
                Intent intent2 = new Intent(MainActivity.this, HomepageActivity.class);
                startActivityForResult(intent2,isChangePortrait);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case isChangePortrait:
                if (resultCode == RESULT_OK){
                    if (data != null){
                        if (data.getBooleanExtra("isChangePortrait",false)){
                            Uri uri = data.getParcelableExtra("portraitBitStr");
                            try {
                                ((ImageView)findViewById(R.id.portrait)).setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }


    /**
     * Base64字符串转换成图片
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_WE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"“写”内存权限已开放",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_RE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"“读”内存权限已开放",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showFragment(String tag, Fragment fragment){
        if (manager.findFragmentByTag(tag) != null){
            transaction.show(fragment);
        }else{
            transaction.add(R.id.home_container, fragment,tag);
            transaction.show(fragment);
        }
        transaction.commit();

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Boolean requestPer(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_APN_SETTINGS},REQUEST_WE_STORAGE_PER);
        }else if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_RE_STORAGE_PER);
        }else {
            return true;
        }
        return false;
    }

    private void openGallery(){
        Matisse.from(MainActivity.this)
                .choose(MimeType.allOf())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(0)//可选的最大数
                .capture(false)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "cn.aysst.www.doctor.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(1);
    }

    private void openFile(){
        new LFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(2)
                .withIconStyle(Constant.ICON_STYLE_YELLOW)
                .withMutilyMode(false)
                .withTitle("选择文件")
                .withBackgroundColor("#FFFFFF")
                .withTitleColor("#000000")
                .withFileFilter(new String[]{".wav",".aif",".au",".mp3",".ram",".wma",".mmf",".amr",".aac",".flac"})
                .withBackIcon(Constant.BACKICON_STYLEONE)
                .start();
    }

    private void openRecord(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivity(intent);
    }

    private void goAboutUs(){
        Intent intent = new Intent(MainActivity.this,AboutusActivity.class);
        startActivity(intent);
    }

    private void goRecharge(){
        Intent intent = new Intent(MainActivity.this,RechargeActivity.class);
        startActivity(intent);
    }
}
