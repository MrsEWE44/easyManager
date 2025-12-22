package com.easymanager.mylife;

import com.easymanager.core.entity.easyManagerClientEntity;
import com.easymanager.core.entity.easyManagerServiceEntity;
import com.easymanager.core.utils.CMD;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class adbService {

    public final static int PORT = 43557;
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
            System.out.println("SocketServer create Exception:" + e.toString());
            e.printStackTrace();
        }
    }

    class processMsg implements Runnable {
        Socket socket;

        CMD cmd;
        Object o;

        String errorMSG;

        boolean isDead = false;

        public processMsg(Socket s) {
            socket = s;
        }

        public void run() {
            try {
                // 通过流读取内容
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                easyManagerClientEntity adbee2 = (easyManagerClientEntity) ois.readObject();
                cmd = listener.sendCMD(adbee2.getCmdstr());
                try{
                    o = listener.doSomeThing(adbee2);
                    errorMSG = "";
                    isDead = false;
                }catch (Exception e){
                    isDead = true;
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    errorMSG = sw.toString();
                    System.err.println("processMsg ::: " +sw);
                }
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                easyManagerServiceEntity a = new easyManagerServiceEntity(cmd,o,isDead,errorMSG);
                oos.writeObject(a);
                oos.flush();
                oos.close();
                socket.close();
                //需要实现appops的功能
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface SocketListener{

        CMD sendCMD(String cmdstr);

        Object doSomeThing(easyManagerClientEntity adbEntity2);

    }


}
