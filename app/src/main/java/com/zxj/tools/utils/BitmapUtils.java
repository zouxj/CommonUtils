package com.zxj.tools.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zxj on 2015/10/30.
 */
public class BitmapUtils {
    /**
     * 读取图片资源
     * @param resId
     * @return
     */
    public static Bitmap readBitmap(int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // opt.inSampleSize =2;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = UIUtils.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 显示图片
     * @param imagePath
     * @param headImg
     */
    public static void displayImage(String imagePath,ImageView headImg)
    {
        Bitmap bitmap = getDiskBitmap(imagePath);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        bitmap = getCroppedBitmap(bitmap, w > h ? h : w);
        headImg.setImageBitmap(bitmap);
    }

    /**
     * 根据路径读取图片
     * @param pathString
     * @return
     */
    public static Bitmap getDiskBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if (file.exists())
            {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(pathString, bmpFactoryOptions);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius)
    {

        LogUtils.v("Utility", "getCroppedBitmap start");
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        if (sbmp != null)
        {
            sbmp.recycle();
            sbmp = null;
        }
        return output;
    }
    /**
     * 按像素图片压缩，然后覆盖
     *
     * @param strImgPath2
     */
    public static void myImageCompress(String strImgPath2)
    {
        Bitmap bmp = myReduceImage(strImgPath2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;//
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 300 && options > 10)
        {
            LogUtils.i("baos.toByteArray().length / 1024===",""+baos.toByteArray().length
                    / 1024);
            LogUtils.i("options===" ,options+"");
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(strImgPath2);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 按宽高压缩图片
     *
     * @param path
     * @return
     */
    public static Bitmap myReduceImage(String path)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 400);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        return bitmap;
    }
}
