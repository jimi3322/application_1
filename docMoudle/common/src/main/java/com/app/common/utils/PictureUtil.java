package com.app.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.WebView;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {


    /**
     * 把bitmap转换成String
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static String bitmapToString(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath) {

        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 320, 400);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径删除图片
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment.getExternalStorageDirectory(),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     */
    public static String getAlbumName() {
        return "yiqi";
    }

    public static String getPicWidthHeight(String filePath) {
        return "";
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, int color) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // int color = Color.TRANSPARENT;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        paint.setAlpha(0xff);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static synchronized Bitmap changeBitmapToSuitable(Bitmap bitmap, int sourceWidht,
                                                             int sourceHeight, int targetWidht, int targetHeight) {
        Matrix matrix = new Matrix();
        matrix.postScale(targetWidht / (float) sourceWidht, targetHeight
                / (float) sourceHeight);
        Bitmap tempBitmap = null;
        try {
            tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, sourceWidht, sourceHeight,
                    matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return tempBitmap;
    }

    public static final Bitmap decodeByte2Array(byte data[]) {
        Bitmap tempBitmap = null;
        try {
            Options opts = new Options();
            opts.inPurgeable = true;// xuesong: 优化图片占用内存. 12-01-07
            tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return tempBitmap;

    }

    public static final Bitmap getBitmapFromAssets(String filepath, Context context) {
        Options opts = new Options();
        opts.inPurgeable = true;// xuesong: 优化图片占用内存. 12-01-07
        return getBitmapFromAssets(context, filepath, opts);
    }

    public static final Bitmap getBitmapFromAssets(Context context, String filepath, Options options) {
        if (filepath == null || filepath.equals(""))
            return null;
        Bitmap mFacicon = null;
        try {
            InputStream ins = context.getAssets().open(filepath);
            mFacicon = BitmapFactory.decodeStream(ins, null, options);
            if (ins != null)
                ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return mFacicon;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees,
                    (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle(); //Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return b;
    }

    public static Bitmap convertPngToJpeg(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        Bitmap oldBitmap = decodeByteArray(bytes, 0, bytes.length);
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }

        try {
            Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(0xffffffff, Mode.SRC);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(oldBitmap, 0, 0, paint);
            return newBitmap;
        } catch (Exception e) {
            //Utils.error(ImageUtils.class, Utils.getStackTrace(e));
            return null;
        }

    }

    @TargetApi(11)
    public static void setHardAcclessByBitmapSize(Bitmap bitmap, ImageView imageView) {
        if (bitmap != null && imageView != null) {
            final boolean shouldDisableHWAcceleration = (bitmap.getWidth() > 2048 || bitmap
                    .getHeight() > 2048)
                    && Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB;
            if (shouldDisableHWAcceleration) {
                imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    public static Bitmap decodeFile(String filePath) {
        Options options = new Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return decodeFile(filePath, options);
    }

    public static Bitmap decodeFile(String filePath, int viewWidth, int viewHeight) {
        if (!new File(filePath).exists()) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options); // 此时返回为空

        options.inSampleSize = getCompressRatio(options.outWidth, options.outHeight, viewWidth, viewHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        //Utils.debug(TAG, "decodeFile - width: " + options.outWidth + ", height: " + options.outHeight + ", viewWidth: " + viewWidth + ", viewHeight: " + viewHeight + ", compress ratio: " + options.inSampleSize);

        return decodeFile(filePath, options);
    }

    public static Bitmap decodeFile(String filePath, Options options) {
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            //Utils.debug(TAG, "decodeFile: out of memory, causing gc...");

            try {
                bm = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError ie) {
                //Utils.debug(TAG, "decodeFile: still no memory after gc...");
                bm = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static Bitmap decodeSampleFromFile(String filePath, int compressedRatio) {
        if (!new File(filePath).exists()) {
            return null;
        }

        Options options = new Options();
        options.inSampleSize = compressedRatio;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return decodeFile(filePath, options);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        Options options = new Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return decodeByteArray(data, offset, length, options);
    }

    public static Bitmap decodeByteArrayWithCompressedRatio(byte[] data, int offset, int length,
                                                            int compressRatio) {
        Options options = new Options();
        options.inSampleSize = compressRatio;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return decodeByteArray(data, offset, length, options);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, int viewWidth, int viewHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options); // 此时返回为空

        options.inSampleSize = getCompressRatio(options.outWidth, options.outHeight, viewWidth, viewHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        //sUtils.debug(TAG, "decodeByteArray - width: " + options.outWidth + ", height: " + options.outHeight + ", viewWidth: " + viewWidth + ", viewHeight: " + viewHeight + ", compress ratio: " + options.inSampleSize);

        return decodeByteArray(data, offset, length, options);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, Options options) {
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeByteArray(data, offset, length, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            //Utils.debug(TAG, "decodeByteArray: out of memory, causing gc...");

            try {
                bm = BitmapFactory.decodeByteArray(data, offset, length, options);
            } catch (OutOfMemoryError ie) {
                //Utils.debug(TAG, "decodeByteArray: still no memory after gc...");
                bm = null;
            }
        }

        return bm;
    }

    public static int getCompressRatio(int bWidth, int bHeight, int viewWidth, int viewHeight) {
        int compressRatio = 1;
        if (bWidth > 0 && bHeight > 0 && viewWidth > 0 && viewHeight > 0) {
            if (viewWidth * bHeight > bWidth * viewHeight) {
                // 考虑宽边
                compressRatio = (int) ((float) bWidth / viewWidth + 0.2f);
            } else {
                // 考虑高边
                compressRatio = (int) ((float) bHeight / viewHeight + 0.2f);
            }

            compressRatio = Math.max(compressRatio, 1);
        }

        return compressRatio;
    }

    public static Bitmap getCompressedBitmap(String filePath, int widthLimit) {
        return getCompressedBitmap(decodeFile(filePath), widthLimit);
    }

    public static Bitmap getCompressedBitmap(byte[] bytes, int widthLimit) {
        return getCompressedBitmap(decodeByteArray(bytes, 0, bytes.length), widthLimit);
    }

    public static Bitmap getCompressedBitmap(Bitmap origin, int widthLimit) {
        if (origin == null) {
            return null;
        }

        int width = origin.getWidth();
        int height = origin.getHeight();
        if (width > widthLimit) {
            height = (int) ((float) height * widthLimit / width + 0.5f);
            width = widthLimit;
        } else {
            return origin;
        }

        Bitmap ret = null;
        try {
            ret = Bitmap.createScaledBitmap(origin, width, height, false);
        } catch (OutOfMemoryError e) {
            System.gc();
            //Utils.debug(TAG, "getCompressedBitmap: out of memory, causing gc...");

            try {
                ret = Bitmap.createScaledBitmap(origin, width, height, false);
            } catch (OutOfMemoryError ie) {
                //Utils.debug(TAG, "getCompressedBitmap: still not enough memory after gc, returning null ...");
                ret = null;
            }
        }
        return ret;
    }

    public static Bitmap decodeResource(Resources res, int id) {
        Bitmap bm = null;
        Options options = new Options();
        try {
            bm = BitmapFactory.decodeResource(res, id, options);
        } catch (OutOfMemoryError e) {
            System.gc();
            //Utils.debug(TAG, "decodeResource: out of memory, causing gc...");

            try {
                bm = BitmapFactory.decodeResource(res, id, options);
            } catch (OutOfMemoryError ie) {
                //Utils.debug(TAG, "decodeResource: still no memory after gc...");
                bm = null;
            }
        }
        return bm;
    }

    public static boolean writeBitmapToFile(Bitmap bitmap, String path, int quality) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(path));
            bitmap.compress(CompressFormat.JPEG, quality, outputStream);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Bitmap.Config config) {
        Bitmap bm = null;
        try {
            bm = Bitmap.createBitmap(colors, width, height, config);
        } catch (OutOfMemoryError e) {
            System.gc();
            //Utils.debug(TAG, "createBitmap: out of memory, causing gc...");

            try {
                bm = Bitmap.createBitmap(colors, width, height, config);
            } catch (OutOfMemoryError ie) {
                //Utils.debug(TAG, "createBitmap: still no memory after gc...");
                bm = null;
            }
        }
        return bm;
    }

    public static Bitmap convertViewToBitmap(View view) {
        int bitmapWidth = view.getWidth();
        int bitmapHeight = view.getHeight();
        if (bitmapWidth == 0 || bitmapHeight == 0) {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        } else {
            view.measure(MeasureSpec.makeMeasureSpec(bitmapWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(bitmapHeight, MeasureSpec.EXACTLY));
        }
        bitmapWidth = view.getMeasuredWidth();
        bitmapHeight = view.getMeasuredHeight();
        view.layout(0, 0, bitmapWidth, bitmapHeight);
        Bitmap bitmap = null;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();
            return bitmap;
        } catch (OutOfMemoryError ex) {

        }
        return bitmap;
    }

    /**
     * 截屏
     *
     * @return
     */
    public static Bitmap captureScreen(WebView webView, View decorview, Resources resource) {
        if (webView != null) {
            Picture picture = webView.capturePicture();
            // liudingyu umeng crash analysis: picture may be null, so protect it
            if (picture != null) {
                int width = picture.getWidth();
                int height = picture.getHeight();
                if (width > 0 && height > 0) {
                    height = resource.getDisplayMetrics().heightPixels;//由于某些网页太长导致图片过大而内存溢出crash，所以截图最多只截取屏幕高
                    Bitmap bmp = Bitmap.createBitmap(width, height,
                            Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(bmp);
                    picture.draw(canvas);
                    int bmpWidth = bmp.getWidth() > bmp.getHeight() ? bmp.getHeight() : bmp.getWidth();//取最小的高or宽
                    Bitmap scaleBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpWidth);
                    bmp.recycle();
                    System.gc();
                    return scaleBitmap;
                }
            }
        }
        return convertViewToBitmap(decorview);
    }

    /**
     * 获取按照图片宽高和屏幕宽高同比缩放后的图片
     */
    public static Bitmap getAfterDealBitmap(Context context,
                                            String photoShootPath) {
        // 1.得到屏幕的宽高信息
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        System.out.println("屏幕宽高：" + metrics.widthPixels + "-" + metrics.heightPixels);

        // 2.得到图片的宽高。
        Options opts = new Options();// 解析位图的附加条件
        opts.inJustDecodeBounds = true;// 不去解析真实的位图，只是获取这个位图的头文件信息
        BitmapFactory.decodeFile(photoShootPath, opts);
        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        System.out.println("图片宽高： " + bitmapWidth + "-" + bitmapHeight);

        // 3.计算缩放比例
        int dx = bitmapWidth / metrics.widthPixels;
        int dy = bitmapHeight / metrics.heightPixels;
        int scale = 4;
        if (dx > dy && dy >= 1) {
            System.out.println("按照水平方法缩放,缩放比例：" + dx);
            scale = dx;
        }

        if (dy > dx && dx >= 1) {
            System.out.println("按照垂直方法缩放,缩放比例：" + dy);
            scale = dy;
        }
        if (dy == dx && dx >= 1) {
            scale = dx;
        }
        // 4.缩放加载图片到内存。
        opts.inSampleSize = scale;
        opts.inJustDecodeBounds = false;// 真正的去解析这个位图。
        return BitmapFactory.decodeFile(photoShootPath, opts);
    }

    /**
     * 获取按照图片宽高和屏幕宽高同比缩放后的图片
     *
     * @param width          int 目标图片宽度
     * @param height         int 目标图片高度
     * @param photoShootPath String 图片地址
     */
    public static Bitmap getAfterDealBitmap(int width, int height,
                                            String photoShootPath) {

        // 2.得到图片的宽高。
        Options opts = new Options();// 解析位图的附加条件
        opts.inJustDecodeBounds = true;// 不去解析真实的位图，只是获取这个位图的头文件信息
        BitmapFactory.decodeFile(photoShootPath, opts);
        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        System.out.println("图片宽高： " + bitmapWidth + "-" + bitmapHeight);

        // 3.计算缩放比例
        int dx = bitmapWidth / width;
        int dy = bitmapHeight / height;

        int scale = 4;

        if (dx > dy && dy >= 1) {
            System.out.println("按照水平方法缩放,缩放比例：" + dx);
            scale = dx;
        }

        if (dy > dx && dx >= 1) {
            System.out.println("按照垂直方法缩放,缩放比例：" + dy);
            scale = dy;
        }

        if (dy == dx && dx >= 1) {
            scale = dx;
        }


        // 4.缩放加载图片到内存。
        opts.inSampleSize = scale;
        opts.inJustDecodeBounds = false;// 真正的去解析这个位图。
        return BitmapFactory.decodeFile(photoShootPath, opts);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        if (bgimage == null) {
            return null;
        }
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /***//**
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    /**
     * 将彩色图转换为灰度图
     * @param img 位图
     * @return  返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        if (img==null)return null;
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高

        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

}
