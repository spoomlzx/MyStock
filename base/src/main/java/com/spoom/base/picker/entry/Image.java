package com.spoom.base.picker.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * package com.spoom.base.picker.entry
 *
 * @author spoomlan
 * @date 03/03/2018
 */

public class Image implements Parcelable {
    private String path;
    private long time;
    private String name;
    private long duration;

    public Image(String path, long time, String name) {
        this.path = path;
        this.time = time;
        this.name = name;
        this.duration = 0;
    }

    public Image(String path, long time, String name, long duration) {
        this.path = path;
        this.time = time;
        this.name = name;
        this.duration = duration;
    }

    protected Image(Parcel in) {
        path = in.readString();
        time = in.readLong();
        name = in.readString();
        duration = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeLong(time);
        dest.writeString(name);
        dest.writeLong(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
