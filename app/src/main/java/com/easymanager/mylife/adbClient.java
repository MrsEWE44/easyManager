package com.easymanager.mylife;

import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class adbClient {

    private final int PORT = 56997;
    private SocketListener listener;

    private easyManagerServiceEntity adbEn;

    public adbClient(easyManagerClientEntity adee2, SocketListener listener) {
        this.listener = listener;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = new Socket();
                ObjectOutputStream oos;
                try {
                    socket.connect(new InetSocketAddress("127.0.0.1", PORT));
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    // 发送指令
                    oos.writeObject(adee2);
                    oos.flush();

                    // 读取服务端返回
                    readServerData(socket,oos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void readServerData(final Socket socket,ObjectOutputStream oos) {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//            cmd = (CMD)ois.readObject();
            adbEn = (easyManagerServiceEntity) ois.readObject();
            ois.close();
            oos.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public easyManagerServiceEntity getAdbEntity(){
        return adbEn;
    }

    public interface SocketListener {

        easyManagerServiceEntity getAdbEntity(easyManagerServiceEntity adfb);
    }

}
