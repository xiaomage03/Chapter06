package com.dodola.atrace;

import android.os.Trace;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//https://juejin.cn/user/2832759143281048
public final class Atrace {
    static {
        System.loadLibrary("tracehook");
    }

    private static boolean sHasHook = false;
    private static boolean sHookFailed = false;


    public static synchronized boolean hasHacks() {
        if (!sHasHook && !sHookFailed) {
            sHasHook = installSystraceHook();

            sHookFailed = !sHasHook;
        }
        return sHasHook;
    }

   public static  void aSystraceStop(){
        systraceStop();
   }
    public static void enableSystrace() {
        if (!hasHacks()) {
            return;
        }
        enableSystraceNative();

        SystraceReflector.updateSystraceTags();
    }

    public static void restoreSystrace() {
        if (!hasHacks()) {
            return;
        }
        restoreSystraceNative();
        SystraceReflector.updateSystraceTags();
    }

    private static native boolean installSystraceHook();

    private static native void enableSystraceNative();

    private static native void restoreSystraceNative();
    private static native void systraceStop();

    private static final class SystraceReflector {
        public static final void updateSystraceTags() {
            if (sTrace_sEnabledTags != null && sTrace_nativeGetEnabledTags != null) {
                try {
                    sTrace_sEnabledTags.set(null, sTrace_nativeGetEnabledTags.invoke(null));
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
        // native层控制标签是否生效的方法
        private static final Method sTrace_nativeGetEnabledTags;
        // java层控制标签是否生效
        private static final Field sTrace_sEnabledTags;

        static {
            Method m;
            try {
                m = Trace.class.getDeclaredMethod("nativeGetEnabledTags");
                m.setAccessible(true);
            } catch (NoSuchMethodException e) {
                m = null;
            }
            sTrace_nativeGetEnabledTags = m;

            Field f;
            try {
                f = Trace.class.getDeclaredField("sEnabledTags");
                f.setAccessible(true);
            } catch (NoSuchFieldException e) {
                f = null;
            }
            sTrace_sEnabledTags = f;
        }
    }
}