package com.spoom.base.picker.entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package com.spoom.base.picker.entry
 *
 * @author spoomlan
 * @date 04/03/2018
 */

public class LoadImage {
    public static void loadImageFromSdcard(final Context context, int type, LoadCallback callback) {
        new Thread(() -> {
            Uri uri = MediaStore.Files.getContentUri("external");
            ContentResolver contentResolver = context.getContentResolver();
            final String[] imageProjection = new String[]{
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
            };
            final String[] videoProjection = new String[]{
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Video.VideoColumns.DURATION
            };

            final String imageSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND _size > ?";
            final String[] imageArgs = {
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                    String.valueOf(10 * 1024)
            };
            final String videoSelection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + " > 0";
            final String[] videoArgs = {
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            };
            Cursor cursor;
            ArrayList<Folder> folders = new ArrayList<>();
            Map<String, Folder> folderMap = new HashMap<>();
            Folder allImage = new Folder("全部照片");
            if (1 == type >> 1) {
                folders.add(allImage);
            }
            Folder allVideo = new Folder("全部视频");
            if (1 == (type & 1)) {
                folders.add(allVideo);
            }
            if (1 == type >> 1) {
                cursor = contentResolver.query(uri, imageProjection, imageSelection, imageArgs, "date_added desc");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                        long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED));
                        String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                        if (!".downloading".equals(getExtensionName(path))) { //过滤未下载完成的文件
                            Image image = new Image(path, time, name);
                            allImage.addImage(image);
                            if (folderMap.containsKey(folderName)) {
                                folderMap.get(folderName).addImage(image);
                            } else {
                                Folder newFolder = new Folder(folderName);
                                newFolder.addImage(image);
                                folders.add(newFolder);
                                folderMap.put(folderName, newFolder);
                            }
                        }
                    }
                    cursor.close();
                }
            }
            if (1 == (type & 1)) {
                cursor = contentResolver.query(uri, videoProjection, videoSelection, videoArgs, "date_added desc");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                        long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                        if (!".downloading".equals(getExtensionName(path))) { //过滤未下载完成的文件
                            Image image = new Image(path, time, name, duration);
                            allVideo.addImage(image);
                        }
                    }
                    cursor.close();
                }
            }
            callback.success(folders);
        }).start();
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public interface LoadCallback {
        void success(List<Folder> folderList);
    }
}
