package com.easymanager.entitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class PKGINFO implements Serializable {

    public PKGINFO(){}

    public PKGINFO(String pkgname, String appname, String apkpath, String apkuid, String appversionname, Drawable appicon, Long filesize) {
        this.pkgname = pkgname;
        this.appname = appname;
        this.apkpath = apkpath;
        this.apkuid = apkuid;
        this.appversionname = appversionname;
        this.appicon = drawableToBytes(appicon);
        this.filesize = filesize;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApkpath() {
        return apkpath;
    }

    public void setApkpath(String apkpath) {
        this.apkpath = apkpath;
    }

    public String getApkuid() {
        return apkuid;
    }

    public void setApkuid(String apkuid) {
        this.apkuid = apkuid;
    }

    public String getAppversionname() {
        return appversionname;
    }

    public void setAppversionname(String appversionname) {
        this.appversionname = appversionname;
    }

    public Drawable getAppicon() {
        return bytesToDrawable(appicon);
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = drawableToBytes(appicon);
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        return "PKGINFO{" +
                "pkgname='" + pkgname + '\'' +
                ", appname='" + appname + '\'' +
                ", apkpath='" + apkpath + '\'' +
                ", apkuid='" + apkuid + '\'' +
                ", appversionname='" + appversionname + '\'' +
                ", appicon=" + appicon +
                ", filesize=" + filesize +
                '}';
    }

    private  String pkgname , appname , apkpath,apkuid,appversionname;
    private byte[] appicon;
    private Long filesize;




    /**
     * Drawable转换成byte[]
     * @param d
     * @return
     */
    public byte[] drawableToBytes(Drawable d) {
        Bitmap bitmap = this.drawableToBitmap(d);
        return this.bitmapToBytes(bitmap);
    }

    /**
     *  Drawable转换成Bitmap
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable == null){
            return  null;
        }
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换成byte[]
     * @param bm
     * @return
     */
    public byte[] bitmapToBytes(Bitmap bm) {
        if(bm == null){
            return  null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转换成Bitmap
     * @param b
     * @return
     */
    public Bitmap bytesToBitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * byte[]转换成Drawable
     * @param b
     * @return
     */
    public Drawable bytesToDrawable(byte[] b) {
        Bitmap bitmap = this.bytesToBitmap(b);
        return this.bitmapToDrawable(bitmap);
    }

    /**
     * Bitmap转换成Drawable
     * @param bitmap
     * @return
     */
    public Drawable bitmapToDrawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

}
