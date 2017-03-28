package com.softpo.setttingupfilesharing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.softpo.adapter.ImageAdapter;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // 应用内部存储根路径
    private File mPrivateRootDir;
    // 根路径下的images文件夹
    private File mImagesDir;
    // images文件夹下的文件数组
    File[] mImageFiles;

    //返回的Intent
    private Intent mResultIntent;
    //展示数据
    private GridView mFileGridView;
    //返回的Uri
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取Intent
        mResultIntent =
                getIntent();
        // 获得根路径文件夹
        mPrivateRootDir = getFilesDir();
        // 获取images文件夹
        mImagesDir = new File(mPrivateRootDir, "images");
        //获取images下的文件列表
        mImageFiles = mImagesDir.listFiles();
        mFileGridView = (GridView) findViewById(R.id.gv);

        //使用GridView展示数据
        mFileGridView.setAdapter(new ImageAdapter(this, mImageFiles));
        // 设置点击事件
        mFileGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
            /*
             * When a filename in the ListView is clicked, get its
             * content URI and send it to the requesting app
             */
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view,
                                            int position,
                                            long rowId) {
                        //点击位置的图片文件
                        File requestFile = new File(mImageFiles[position].getAbsolutePath());
                        try {
                            //通过FileProvider生成uri格式如下：content://com.example.myapp.fileprovider/myimages/galaxy3.jpg
                            fileUri = FileProvider.getUriForFile(
                                    MainActivity.this,
                                    "com.example.myapp.fileprovider",
                                    requestFile);
                            if (fileUri != null) {
                                // 添加读的权限
                                mResultIntent.addFlags(
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // 设置权限
                                mResultIntent.setDataAndType(
                                        fileUri,
                                        getContentResolver().getType(fileUri));
                                // 返回结果
                                MainActivity.this.setResult(Activity.RESULT_OK,
                                        mResultIntent);
                            } else {
                                mResultIntent.setDataAndType(null, "");
                                MainActivity.this.setResult(RESULT_CANCELED,
                                        mResultIntent);
                            }
                        } catch (IllegalArgumentException e) {
                            Log.e("File Selector",
                                    "The selected file can't be shared: " +
                                            mImageFiles[position].getAbsolutePath());
                        }finally {
                                finish();
                        }
                    }
                });
    }
}
