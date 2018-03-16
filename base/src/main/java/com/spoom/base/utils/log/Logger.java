package com.spoom.base.utils.log;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * package com.spoom.utils.log
 *
 * @author spoomlan
 * @date 30/12/2017
 */

public class Logger {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    private static int level = DEBUG;
    private static boolean link = true;

    private static String tag = "spoom.im";

    public static void setLevel(int level) {
        Logger.level = level;
    }

    public static void setTag(String tag) {
        Logger.tag = tag;
    }

    public static void v(String msg) {
        v(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(Object obj) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= DEBUG) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            if (obj == null) {
                Log.d(tag, "data is null");
            } else {
                Log.d(tag, "│ ==> " + obj.getClass().getSimpleName());
                Log.d(tag, "│ ==> " + gson.toJson(obj));
                if (link) {
                    Log.d(tag, callMethodAndLine(thisMethodStack));
                }
            }
        }
    }

    public static void d(String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= DEBUG) {
            Log.d(tag, msg);
            if (link) {
                Log.d(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void d(String tag, String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= DEBUG) {
            Log.d(tag, msg);
            if (link) {
                Log.d(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void i(String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= INFO) {
            Log.i(tag, msg);
            if (link) {
                Log.i(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void i(String tag, String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= INFO) {
            Log.i(tag, msg);
            if (link) {
                Log.i(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void w(String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= WARN) {
            Log.w(tag, msg);
            if (link) {
                Log.w(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void w(String tag, String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= WARN) {
            Log.w(tag, msg);
            if (link) {
                Log.w(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void e(String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= ERROR) {
            Log.e(tag, msg);
            if (link) {
                Log.e(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static void e(String tag, String msg) {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        if (level <= ERROR) {
            Log.e(tag, msg);
            if (link) {
                Log.e(tag, callMethodAndLine(thisMethodStack));
            }
        }
    }

    public static String callMethodAndLine(StackTraceElement thisMethodStack) {
        String result = " ==>> Thread: " + Thread.currentThread().getName() + " @ ";
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")";
        return result;
    }

}
