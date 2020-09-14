package com.app.common.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.app.common.domain.Constant;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by WUJINGWEI on 2018/6/1.
 */

public class FileUtil {
    /**
     * 创建SD卡文件夹
     *
     * @param dirPath 文件夹路径
     * @return true创建成功，false创建失败
     */
    public static boolean createSDCardDir(String dirPath) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return false;
        File tmpFile = new File(dirPath);
        if (tmpFile.exists()) return true;
        boolean res = tmpFile.mkdirs();
        return res;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件物理路径
     * @return true删除成功，false删除失败
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断给定的文件夹目录是否为空
     *
     * @param folderPath 文件目录路径
     * @return true目录为空，false目录不为空
     */
    public static boolean isFolderEmpty(String folderPath) {
        if (folderPath == null || folderPath.equals("")) return true;
        File folder = new File(folderPath);
        if (!folder.exists()) return true;
        String[] files = folder.list();
        if (files == null || files.length == 0) return true;
        return false;
    }

    /**
     * 解压文件
     * @param zipFilePath
     * @param targetPath
     * @return
     */
    public static boolean unZipFile(String zipFilePath,String targetPath){
        //如果unZipPath是空的化，默认解压到zipFilePath所在的目录中的zipFilePath文件夹下
        if(TextUtils.isEmpty(targetPath)){
            targetPath = zipFilePath.substring(0,zipFilePath.lastIndexOf(Constant.ID_COLON_DOT));
        }
        File unZipFile = new File(targetPath);
        if(unZipFile.exists()){
            unZipFile.delete();
        }
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            //1.获取要解压的文件 ， 并指定解压格式为“GBK”
            zipFile = new ZipFile(zipFilePath);
            //zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
            //读取zip文件中每一个文件及文件夹
            Enumeration<?> entryEnum = zipFile.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                //总共有多少文件或是文件夹就循环多少次
                while (entryEnum.hasMoreElements()) {
                    //获取下一个文件或者文件夹
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.getSize() > 0 && zipEntry.getName().contains("bundle/")) {
                        // 文件  File.separator就是“/”
                        File targetFile = new File(targetPath
                                + File.separator + zipEntry.getName());
                        //将文件读取到指定文件位置
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                            os.flush();
                        }
                        is.close();
                        os.close();
                    }
                    //如果是文件夹，则创建文件夹
                    if (zipEntry.isDirectory()) {
                        String pathTemp = targetPath + File.separator
                                + zipEntry.getName();
                        File file = new File(pathTemp);
                        file.mkdirs();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            //关闭流
            try {
                if (null != zipFile) {
                    zipFile.close();
                    zipFile = null;
                }
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * 将字节码写到文件中
     */
    public static void writeBytesToFile(byte[] bs,String path) {
        if(bs ==null){
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(path);
            ByteArrayInputStream bis =  new ByteArrayInputStream (bs);
            byte[] buff = new byte[1024];
            int len = bis.read(buff);
            while (len!= -1) {
                out.write(buff, 0, len);
                len = bis.read(buff);
            }
            bis.close();
            out.close();
        }catch (Exception e){
        }

    }


    /**
     * 获取外部存储目录中 公共存储路径
     *
     * @return 返回文件路径
     */
    public static File getExternalStorageDir() {
        File fileDir =null;
        if (hasExternalStorage()) {
            fileDir = Environment.getExternalStorageDirectory();
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        }

        return fileDir;
    }


    /**
     * 获取存储路径
     *
     * @param context
     * @return 返回文件路径
     */
    public static File getFileDir(Context context) {
        File fileDir;
        if (hasExternalStorage()) {
            fileDir = context.getExternalFilesDir(null);
        } else {
            fileDir = context.getFilesDir();
        }
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    /**
     * 文件拷贝
     * @param context
     * @param fileName
     * @param desFile
     */
    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getApplicationContext().getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int i;
            while ((i = in.read(bytes)) != -1)
                out.write(bytes, 0, i);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否有sdk
     * @return
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
