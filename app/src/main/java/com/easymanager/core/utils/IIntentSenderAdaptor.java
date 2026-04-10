package com.easymanager.core.utils;

import android.content.IIntentSender;
import android.content.Intent;

public abstract class IIntentSenderAdaptor extends IIntentSender.Stub {

    public abstract void send(Intent intent);
}
