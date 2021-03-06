package com.autonavi.minimap.route.foot.util;

import android.content.BroadcastReceiver;
import java.lang.ref.WeakReference;

public abstract class AmapBroadcastReceiver<T> extends BroadcastReceiver {
    private WeakReference<T> a = null;

    public AmapBroadcastReceiver(T t) {
        if (t != null) {
            this.a = new WeakReference<>(t);
        }
    }

    /* access modifiers changed from: protected */
    public final T a() {
        if (this.a != null) {
            return this.a.get();
        }
        return null;
    }
}
