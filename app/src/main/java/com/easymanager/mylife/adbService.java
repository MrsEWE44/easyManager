package com.easymanager.mylife;

import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;
import com.easymanager.core.utils.CMD;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class adbService {

    public final static int PORT = 56997;
    private SocketListener listener;

    public adbService(SocketListener listener) {
        this.listener = listener;
        try {
            // 利用ServerSocket类启动服务，然后指定一个端口
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("easyManager adb server running " + PORT + " port");
            ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
            // 新建一个线程池用来并发处理客户端的消息
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    5,
                    10,
                    50000,
                    TimeUnit.MILLISECONDS,
                    queue
            );
            while (true) {
                Socket socket = serverSocket.accept();
                // 接收到新消息
                executor.execute(new processMsg(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SocketServer create Exception:" + e.toString());
        }
    }

    class processMsg implements Runnable {
        Socket socket;

        CMD cmd;
        Object o;

        public processMsg(Socket s) {
            socket = s;
        }

        public void run() {
            try {
                // 通过流读取内容
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                easyManagerClientEntity adbee2 = (easyManagerClientEntity) ois.readObject();
                cmd = listener.sendCMD(adbee2.getCmdstr(),false);
                o = listener.doSomeThing(adbee2);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                easyManagerServiceEntity a = new easyManagerServiceEntity(cmd,o);
                oos.writeObject(a);
                oos.flush();
                oos.close();
                socket.close();
                //需要实现appops的功能
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("socket connection error：" + e.getStackTrace().toString());
            }
        }
    }

    public interface SocketListener{

        CMD sendCMD(String cmdstr,boolean isRoot);

        Object doSomeThing(easyManagerClientEntity adbEntity2);

    }


}
