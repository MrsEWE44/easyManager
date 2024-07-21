package com.easymanager.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.entitys.PKGINFO;
import com.easymanager.utils.base.DialogUtils;
import com.easymanager.utils.FileTools;
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PackageUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class FileSharedLayoutActivity extends Activity {

    private ArrayList<String> list = new ArrayList<>();

    private EditText fsisharedip , fsisharedport;
    private Button fsladdfile,fsladdapp,fslstartshared;
    private ListView fsllv;
    private Context context;
    private Activity activity;
    private int mode,default_port = 25444;


    private DialogUtils du = new DialogUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_shared_layout);
        MyActivityManager.getIns().add(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initBt();
    }

    private void initBt() {
        context = this;
        activity = this;
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);
        fsisharedip = findViewById(R.id.fsisharedip);
        fsisharedport = findViewById(R.id.fsisharedport);
        fsladdfile = findViewById(R.id.fsladdfile);
        fsladdapp = findViewById(R.id.fsladdapp);
        fslstartshared = findViewById(R.id.fslstartshared);
        fsllv = findViewById(R.id.fsllv);
        clickBt();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void clickBt() {
        String ip = getIpAddress();
        fsisharedip.setFocusableInTouchMode(false);
        fsisharedip.setText(ip);
        fsisharedport.setHint(getLanStr(R.string.default_port) + default_port);


        fslstartshared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fslstartshared.setEnabled(false);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        fslstartshared.setText(getLanStr(R.string.now_is_file_share));
                        new HttpServer().start(activity);
                    }
                });
            }
        });

        fsladdfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectFileAndAppDialog(true);
            }
        });


        fsladdapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectFileAndAppDialog(false);
            }
        });

    }

    private void showSelectFileAndAppDialog(boolean isFile){
        String extstorage = Environment.getExternalStorageDirectory().toString();
        ArrayList<String> flist = new ArrayList<>();
        ArrayList<PKGINFO> pkginfos = new ArrayList<>();
        ArrayList<Boolean> checkboxs = new ArrayList<>();
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        View view2 = getLayoutInflater().inflate(R.layout.file_shared_select_dialog_layout, null);
        ListView fssdllv = view2.findViewById(R.id.fssdllv);
        String str = isFile?getLanStr(R.string.is_share_file_msg):getLanStr(R.string.is_share_app_msg);
        ProgressDialog show = du.showMyDialog(context,  getLanStr(R.string.now_get_ing_head)+str+getLanStr(R.string.now_get_ing_end));
        Handler handler = new Handler() {
            @Override
            public void handleMessage( Message msg) {
                if (msg.what == 0) {
                    show.dismiss();

                    ab.setView(view2);
                    ab.setTitle(getLanStr(R.string.dialog_choice_text)+str);
                    ab.setNegativeButton(getLanStr(R.string.dialog_sure_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int i1 = 0; i1 < checkboxs.size(); i1++) {
                                if(checkboxs.get(i1)){
                                    if(isFile){
                                        String s = flist.get(i1);
                                        if(s.equals(getLanStr(R.string.file_share_previous_text))){
                                            File file = new File(s);
                                            if(file.isFile() || file.isDirectory()){
                                                list.add(s);
                                            }
                                        }else{
                                            list.add(s);
                                        }
                                    }else{
                                        list.add(pkginfos.get(i1).getApkpath());
                                    }
                                }
                            }
                            du.showUsers(context,fsllv,list,checkboxs);
                        }
                    });
                    ab.setPositiveButton(getLanStr(R.string.dialog_cancel_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    ab.create().show();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isFile){
                    getLocalFiles(extstorage,null,flist,checkboxs,fssdllv);
                }else{
                    PackageUtils packageUtils = new PackageUtils();
                    packageUtils.queryUserEnablePKGS(activity,pkginfos,checkboxs,0);
                    du.showPKGS(context,fssdllv,pkginfos,checkboxs);
                }

                du.sendHandlerMSG(handler,0);
            }
        }).start();

    }

    //获取当前设备的ip地址
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }


    //显示文件选择框
    private void getLocalFiles(String extstorage, String path,ArrayList<String> flist,ArrayList<Boolean> checkboxs,ListView fssdllv) {
        flist.clear();
        checkboxs.clear();
        String AnDir = extstorage + "/Android";
        String fff = (path==null)?extstorage + "/" :path;
        if(path != null && path.indexOf(extstorage)==-1){
            fff = extstorage;
        }
        //如果只是授权文件存储权限，则只需要通关file列出所有文件夹即可
        File file1 = new File(fff);
        if (file1.listFiles() != null) {
            for (File file : file1.listFiles()) {
                if(file.getAbsolutePath().indexOf(AnDir) == -1){
                    flist.add(file.toString());
                    checkboxs.add(false);
                }
            }
        }
        Collections.sort(flist, String::compareTo);
        if (flist.size() >0 && flist.get(0).length() > extstorage.length()) {
            flist.add(0, getLanStr(R.string.file_share_previous_text));
            checkboxs.add(false);
        }else{
            flist.add(getLanStr(R.string.file_share_previous_text));
            checkboxs.add(false);
        }
        fssdllv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String full_path = flist.get(i);
                File file = new File(full_path);
                if (file.getName().indexOf(getLanStr(R.string.file_share_previous_text)) != -1) {
                    File file2 = new File(path==null?extstorage:path);
                    String paaa = file2.getParent();
                    if(paaa.equals(extstorage)){
                        paaa = extstorage;
                    }
                    Log.d("f2",file2.toString() + " ---- " + file2.getParent());
                    getLocalFiles(extstorage, paaa, flist,checkboxs,fssdllv);
                } else {
                    if (file.isDirectory()) {
                        getLocalFiles(extstorage, full_path, flist,checkboxs,fssdllv);
                    }
                }
            }
        });
        fssdllv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                View vvv = fssdllv.getChildAt(i);
                TextView ttt = vvv.findViewById(R.id.gliltv);
                Log.d("ttt",ttt.getText().toString());
                return false;
            }
        });
        du.showUsers(context,fssdllv,flist,checkboxs);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OtherTools otherTools = new OtherTools();
        otherTools.addMenuBase(this,menu,mode);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            activity.onBackPressed();
            MyActivityManager.getIns().kill(activity);
            System.exit(0);
        }

        if(itemId == 5){
            new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
        }

        if(itemId == 6){
            MyActivityManager.getIns().killall();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyActivityManager.getIns().kill(activity);
        System.exit(0);
    }

    //http服务器内部类
    private class HttpServer {

        //判断是否为第一次访问
        private boolean isFirst = true;

        //临时list
        private ArrayList<String> tempList = new ArrayList<>();

        //保留上一级路径
        private String parenPath;

        //获取端口
        private String s = fsisharedport.getText().toString();

        //获取ip
        private String s2 = fsisharedip.getText().toString();

        private String ipAndPort = s2 + ":" + (s.isEmpty() ? default_port : s);

        //html界面头部
        private String htmlhead = "<html>\n" +
                "<head>\n" +
                "\n" +
                "<h1 style=\"text-align: center;\">easyManager file download web page</h1>\n" +
                "<meta charset=\"utf-8\">\n" +
                "\n" +
                "</head>\n" +
                "<body>\n";

        //html界面尾部
        private String htmlend = "</body>\n" +
                "</html>\n" +
                "\n" +
                "<script>\n" +
                "function bt(a){\n" +
                "\twindow.open(\"http://" + ipAndPort + "/easyManager?file=\"+a)\n" +
                "}\n" +
                "</script>";

        private Context context;

        private ServerSocket serverSocket = null;
        private Socket socket = null;

        public void start(Context context2) {
            context = context2;
            tempList.addAll(list);
            String extstorage = Environment.getExternalStorageDirectory().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocket = new ServerSocket(s.isEmpty() ? default_port : Integer.valueOf(s));
                        Log.d(FileSharedLayoutActivity.class.getName(), "listen port : " + serverSocket.getLocalPort());

                        while (true) {//死循环时刻监听客户端链接
                            //开始服务
                            try {
                                socket = serverSocket.accept();
                                Log.d(FileSharedLayoutActivity.class.getName(), "new con : " + socket.getInetAddress() + ":" + socket.getPort());

                                //读取HTTP请求信息
                                InputStream socketIn = socket.getInputStream();
                                int size = socketIn.available();
                                byte[] b = new byte[size];
                                socketIn.read(b);
                                String request = new String(b);
                                String[] split = request.split("\r\n");
//                                Log.d("request",request);
                                //创建HTTP响应结果
                                //创建响应协议、状态
                                String httpStatus = "HTTP/1.1 200 OK\r\n";
                                for (String s : split) {
                                    if (s.indexOf("GET") != -1) {
                                        String path = s.split(" ")[1];
                                        //判断是否为请求easyManager
                                        if (path.indexOf("easyManager") != -1) {
                                            String parm = path.split("\\?")[1];
                                            Log.d("parm", parm);
                                            //判断是否为请求easyManager的文件列表以及文件下载
                                            if (parm.indexOf("file") != -1) {
                                                parm = parm.split("=")[1];
                                                Integer index = Integer.valueOf(parm.trim());
                                                String data = isFirst ? list.get(index) : tempList.get(index);
                                                //判断是否点击的“上一页”
                                                if(data.equals(getLanStr(R.string.file_share_previous_text)) && extstorage.equals(parenPath)){
                                                    tempList.clear();
                                                    tempList.addAll(list);
                                                    returnFileList(list, httpStatus, socket);
                                                }else{
                                                    if (data.equals(getLanStr(R.string.file_share_previous_text))) {
                                                        data = parenPath;
                                                    }
                                                    data = data.replaceAll("//", "/");
                                                    File file = new File(data);
//                                                    Log.d("data",data + " -- " + parenPath + " -- " + file.getAbsolutePath());
                                                    if (file.isDirectory()) {
                                                        tempList.clear();
                                                        File[] files = file.listFiles();
                                                        if (files != null && files.length > 0) {
                                                            for (File listFile : files) {
                                                                tempList.add(listFile.getAbsolutePath());
                                                            }
                                                        }
                                                        Collections.sort(tempList, String::compareTo);

                                                        if (tempList.size() >0 && tempList.get(0).length() > extstorage.length()) {
                                                            tempList.add(0, getLanStr(R.string.file_share_previous_text));
                                                        }else{
                                                            tempList.add(getLanStr(R.string.file_share_previous_text));
                                                        }
                                                        parenPath = file.getParent();
                                                        returnFileList(tempList, httpStatus, socket);

                                                    } else {
                                                        String contentType = "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8");
                                                        //创建响应头
                                                        String responseHeader = "Content-Description:File Transfer\r\nContent-disposition:" + contentType + "\r\nContent-Length: " + file.length() + "\r\nContent-Transfer-Encoding:binary\r\n\r\n";
                                                        OutputStream socketOut = socket.getOutputStream();
                                                        //发送响应协议、状态码及响应头、正文
                                                        socketOut.write(httpStatus.getBytes());
                                                        socketOut.write(responseHeader.getBytes());
                                                        InputStream in = new FileInputStream(file);
                                                        byte[] b2 = new byte[1024];
                                                        int len = in.read(b2);
                                                        try {
                                                            while (len != -1) {
                                                                //在这里会出现下载出错的问题，需要改善一下。
                                                                //2023年1月30日18点32分
                                                                //2024年3月3日15点27分，修复失败，暂时不清楚什么原因导致，安卓4.4无法正常使用
                                                                socketOut.write(b2, 0, len);
                                                                len = in.read(b2);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            in.close();
                                                            socketOut.close();
                                                            socket.close();
                                                        }

                                                    }
                                                }
                                            } else {
                                                returnText(httpStatus, socket, getLanStr(R.string.parm_error));
                                            }

                                        } else {
                                            returnFileList(list, httpStatus, socket);
                                        }

                                    }
                                }
                                if(!socket.isClosed()){
                                    socket.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isFirst=false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //返回文件列表
        public void returnFileList(ArrayList<String> slist, String httpStatus, Socket socket) throws Exception {
            StringBuilder sb = new StringBuilder();
            sb.append(htmlhead);
            if (slist.size() < 1) {
                sb.append("<h1>"+getLanStr(R.string.file_share_not_select_file)+"</h1>");
            } else {
                for (int i = 0; i < slist.size(); i++) {
                    File file = new File(slist.get(i));
                    if (file.isDirectory() || file.toString().equals(getLanStr(R.string.file_share_previous_text))) {
                        sb.append("<div><table border=\"1\"><td><a href=\"" + "http://" + ipAndPort + "/easyManager?file=" + i + "\" <h1>" + file.getName() + "</h1></a></td></table></div>");
                    } else {
                        sb.append("<div><table border=\"1\"><td>" + file.getName() + "</td><td>" + new FileTools().getSize(file.length(), 0) + "</td><td><button onclick=\"bt(" + i + ")\">"+getLanStr(R.string.file_share_start_download)+"</button></td></table></div>");
                    }
                }
            }
            sb.append(htmlend);
            returnText(httpStatus, socket, sb.toString());
        }

        //返回文本内容给浏览器
        public void returnText(String httpStatus, Socket socket, String text) throws Exception {
            String contentType = "text/html;text/plain;charset=UTF-8";
            //创建响应头
            String responseHeader = "Content-Type:" + contentType + "\r\n\r\n";
            OutputStream socketOut = socket.getOutputStream();
            //发送响应协议、状态码及响应头、正文
            socketOut.write(httpStatus.getBytes());
            socketOut.write(responseHeader.getBytes());
            socketOut.write(text.getBytes());
            socketOut.close();
        }

    }

    private String getLanStr(int id){
        return du.tu.getLanguageString(context,id);
    }



}
