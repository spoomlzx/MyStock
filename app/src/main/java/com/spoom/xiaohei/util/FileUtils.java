package com.spoom.xiaohei.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import com.spoom.xiaohei.manager.LocalUserManager;

import java.io.*;
import java.util.Random;

/**
 * package com.lan.ichat.util
 *
 * @author spoomlan
 * @date 14/01/2018
 */

public class FileUtils {
    private static String baseUrl;
    private static File base;
    private static File avatar;
    private static File image;
    private static File voice;
    private static File video;

    public FileUtils(Context context) {
        baseUrl = "/spoom/" + EncryptUtils.encryptMD5ToString(LocalUserManager.getInstance().getUsername()) + "/";
        base = new File(Environment.getExternalStorageDirectory() + baseUrl);
        if (!base.exists()) {
            base.mkdirs();
        }
        avatar = new File(base, "/avatar/");
        if (!avatar.exists()) {
            avatar.mkdirs();
        }
        image = new File(base, "/image/");
        if (!image.exists()) {
            image.mkdirs();
        }
        voice = new File(base, "/voice/");
        if (!voice.exists()) {
            voice.mkdirs();
        }
        video = new File(base, "/video/");
        if (!video.exists()) {
            video.mkdirs();
        }

    }

    public File getImage() {
        return image;
    }

    public File getAvatarPath() {
        Random random = new Random();
        return new File(avatar, Integer.toHexString(random.nextInt(256)) + "/" + Long.toHexString(random.nextLong()) + ".png");
    }

    /**
     * 保存图片
     *
     * @param src    源图片
     * @param file   要保存到的文件
     * @param format 格式 png,jpeg,webp
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean saveImg(final Bitmap src,
                           final File file,
                           final Bitmap.CompressFormat format) {
        if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) {
            return false;
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(os);
        }
        return ret;
    }

    /**
     * 判断 bitmap 对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    private static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && !file.delete()) {
            return false;
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean createOrExistsDir(final File file) {
        // 如果存在，是目录则返回 true，是文件则返回 false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
}
