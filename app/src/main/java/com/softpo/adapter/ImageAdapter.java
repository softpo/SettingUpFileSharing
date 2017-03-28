package com.softpo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;

/**
 * Created by softpo on 2017/3/28.
 * Email: likaiyuan.cool@163.com
 */

public class ImageAdapter extends BaseAdapter {

    Context mContext;
    File[] imageFiles;

    public ImageAdapter(Context context, File[] imageFiles) {
        this.mContext = context;
        this.imageFiles = imageFiles;
    }

    @Override
    public int getCount() {
        return imageFiles!=null?imageFiles.length:0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(mContext);
            ((ImageView) convertView).setScaleType(ImageView.ScaleType.FIT_XY);

            ListView.LayoutParams layoutParams = new ListView.LayoutParams(120, 120);

            ((ImageView) convertView).setPadding(15,15,15,15);

            ((ImageView) convertView).setLayoutParams(layoutParams);

        }
        //将大图片变成缩略图
        createThumbnail(position,convertView);

        return convertView;
    }

    private void createThumbnail(int position, View convertView) {
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFiles[position].getAbsolutePath(), opts);

        //获取图片的宽高
        int height = opts.outHeight;
        int width = opts.outWidth;

        //定义缩放比例
        int sampleSize = 1;

        while ((height / sampleSize > Cache.IMAGE_MAX_HEIGH)

                || (width / sampleSize > Cache.IMAGE_MAX_WIDTH)) {

            sampleSize *= 2;

        }

        opts.inJustDecodeBounds = false;

        opts.inSampleSize = sampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[position].getAbsolutePath(), opts);

        ((ImageView)convertView).setScaleType(ImageView.ScaleType.FIT_XY);

        ((ImageView) convertView).setImageBitmap(bitmap);

    }

    //默认大小
    class Cache{

        //单位是像素
        public static final int IMAGE_MAX_HEIGH=240;

        public static final int IMAGE_MAX_WIDTH=240;

    }
}
