package com.easymanager.core.server;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.FileDescriptor;
import java.util.Objects;

public class easyManagerBinderWrapper implements IBinder {

    private final  IBinder ori;

    public easyManagerBinderWrapper(IBinder ori2) {
        this.ori = Objects.requireNonNull(ori2);
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return ori.getInterfaceDescriptor();
    }

    @Override
    public boolean pingBinder() {
        return ori.pingBinder();
    }

    @Override
    public boolean isBinderAlive() {
        return ori.isBinderAlive();
    }

    @Override
    public IInterface queryLocalInterface(String s) {
        return ori.queryLocalInterface(s);
    }

    @Override
    public void dump(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {
        ori.dump(fileDescriptor,strings);
    }

    @Override
    public void dumpAsync(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {
        ori.dumpAsync(fileDescriptor, strings);
    }

    @Override
    public boolean transact(int i, Parcel parcel, Parcel parcel1, int i1) throws RemoteException {
        return ori.transact(i, parcel, parcel1, i1);
    }

    @Override
    public void linkToDeath(DeathRecipient deathRecipient, int i) throws RemoteException {
        ori.linkToDeath(deathRecipient, i);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient deathRecipient, int i) {
        return ori.unlinkToDeath(deathRecipient, i);
    }
}
