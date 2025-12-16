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
import android.os.Looper;
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
import com.easymanager.utils.dialog.HelpDialogUtils;
import com.easymanager.utils.MyActivityManager;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.PackageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSharedLayoutActivity extends Activity {

    private ArrayList<String> list = new ArrayList<>();

    private EditText fsisharedip , fsisharedport;
    private Button fsladdfile,fsladdapp,fslstartshared,fslstopshared;
    private ListView fsllv;
    private Context context;
    private Activity activity;
    private int mode,default_port = 25444;

    private HttpServer httpServer;

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
        fslstopshared = findViewById(R.id.fslstopshared);
        fsllv = findViewById(R.id.fsllv);
        clickBt();
        new HelpDialogUtils().showHelp(context,HelpDialogUtils.APP_MANAGE_HELP,mode);
    }

    private void clickBt() {
        String ip = getIpAddress();
        fsisharedip.setFocusableInTouchMode(false);
        fsisharedip.setText(ip);
        fsisharedport.setHint(getLanStr(R.string.default_port) + default_port);
        fslstopshared.setEnabled(false);

        fslstartshared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        fslstartshared.setText(getLanStr(R.string.now_is_file_share));
                        httpServer = new HttpServer();
                        httpServer.setSharedPaths(list);
                        httpServer.start(activity);
                        if(httpServer.isRunning){
                            fslstartshared.setEnabled(false);
                            fslstopshared.setEnabled(true);
                        }
                    }
                });
            }
        });

        fslstopshared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(httpServer != null && httpServer.isRunning){
                    httpServer.stop();
                    if(!httpServer.isRunning){
                        fslstartshared.setEnabled(true);
                        fslstartshared.setText(R.string.button_start_share);
                        fslstopshared.setEnabled(false);
                    }
                }
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
        String extstorage = du.ft.getExternalStorageDirectory();
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
                            if(httpServer != null ){
                                httpServer.setSharedPaths(list);
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
//                    Log.d("f2",file2.toString() + " ---- " + file2.getParent());
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
            if(httpServer != null ){
                httpServer.stop();
            }
            activity.onBackPressed();
//            MyActivityManager.getIns().kill(activity);
//            System.exit(0);
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
        if(httpServer != null ){
            httpServer.stop();
        }
        super.onBackPressed();
//        MyActivityManager.getIns().kill(activity);
//        System.exit(0);
    }


    private String getLanStr(int id){
        return du.tu.getLanguageString(context,id);
    }

    private class HttpServer {
        private static final String TAG = "HttpServer";
        private ServerSocket serverSocket;
        private boolean isRunning = false;
        private int port = default_port; // 默认端口
        private ExecutorService executorService;
        private Activity activity;
        private Handler mainHandler;

        // 全局共享列表
        private ArrayList<String> sharedPaths = new ArrayList<>();
        private Map<String, String> mimeTypes = new HashMap<>();

        public HttpServer() {
            executorService = Executors.newFixedThreadPool(10);
            mainHandler = new Handler(Looper.getMainLooper());
            initMimeTypes();
        }

        // 设置共享路径（从外部传入）
        public void setSharedPaths(ArrayList<String> paths) {
            this.sharedPaths = paths;
        }

        private  String etos(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }

        // 添加共享路径
        public void addSharedPath(String path) {
            if (!sharedPaths.contains(path)) {
                sharedPaths.add(path);
            }
        }

        // 移除共享路径
        public void removeSharedPath(String path) {
            sharedPaths.remove(path);
        }

        public void start(Activity activity) {
            this.activity = activity;
            if (isRunning) {
                return;
            }

            String ip = fsisharedip.getText().toString().trim();
            String portstr = fsisharedport.getText().toString().trim();

            if(portstr == null || portstr.isEmpty()){
                port = 25444;
            }else{
                port = Integer.valueOf(portstr);
            }

            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(TAG+ "  Server starting at: http://" + ip + ":" + port);

                        serverSocket = new ServerSocket(port);

                        while (isRunning) {
                            try {
                                Socket clientSocket = serverSocket.accept();
                                executorService.execute(new ClientHandler(clientSocket));
                            } catch (IOException e) {
                                if (isRunning) {
                                    System.err.println(TAG+ "  Error accepting connection :: " + etos(e) );
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println(TAG + "  Failed to start server ::: " + etos(e) );
                    }
                }
            }).start();
        }

        public void stop() {
            isRunning = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println(TAG + "  Error closing server socket ::: " + etos(e) );
                }
            }
            executorService.shutdown();
            System.out.println(TAG + " Server is stoppend....");
        }

        // 初始化MIME类型
        private void initMimeTypes() {
            mimeTypes.put("html", "text/html");
            mimeTypes.put("htm", "text/html");
            mimeTypes.put("txt", "text/plain");
            mimeTypes.put("css", "text/css");
            mimeTypes.put("js", "application/javascript");
            mimeTypes.put("json", "application/json");
            mimeTypes.put("png", "image/png");
            mimeTypes.put("jpg", "image/jpeg");
            mimeTypes.put("jpeg", "image/jpeg");
            mimeTypes.put("gif", "image/gif");
            mimeTypes.put("pdf", "application/pdf");
            mimeTypes.put("zip", "application/zip");
            mimeTypes.put("mp3", "audio/mpeg");
            mimeTypes.put("mp4", "video/mp4");
            mimeTypes.put("avi", "video/x-msvideo");
            mimeTypes.put("mov", "video/quicktime");
            mimeTypes.put("doc", "application/msword");
            mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            mimeTypes.put("xls", "application/vnd.ms-excel");
            mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
            mimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        }

        // 客户端处理器
        private class ClientHandler implements Runnable {
            private Socket clientSocket;

            public ClientHandler(Socket socket) {
                this.clientSocket = socket;
            }

            @Override
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    // 读取请求
                    String requestLine = in.readLine();
                    if (requestLine == null) {
                        return;
                    }

                    System.out.println(TAG + "  Request: " + requestLine);

                    // 解析请求
                    String[] requestParts = requestLine.split(" ");
                    if (requestParts.length < 2) {
                        sendError(out, 400, "Bad Request");
                        return;
                    }

                    String method = requestParts[0];
                    String requestPath = URLDecoder.decode(requestParts[1], "UTF-8");

                    // 处理不同的HTTP方法
                    if (method.equals("GET")) {
                        handleGet(requestPath, out, in);
                    } else if (method.equals("POST")) {
                        handlePost(requestPath, out, in);
                    } else {
                        sendError(out, 405, "Method Not Allowed");
                    }

                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println(TAG + "  Error handling client ::: " + etos(e) );
                }
            }

            // 处理GET请求
            private void handleGet(String requestPath, OutputStream out, BufferedReader in) throws IOException {
                String ppp = "/browse/"+du.ft.getExternalStorageDirectory();
                if(requestPath.equals(ppp)){
                    requestPath = "/";
                }

                // 如果请求根路径，显示共享列表
                if (requestPath.equals("/") || requestPath.equals("/index.html") || requestPath.equals(Environment.getExternalStorageDirectory().toString())) {
                    sendDirectoryListing(out, "/");
                    return;
                }

                // 处理文件下载
                if (requestPath.startsWith("/download/")) {
                    String filePath = requestPath.substring(9); // 去掉/download/
                    serveFile(filePath, out, true);
                    return;
                }

                // 处理文件预览
                if (requestPath.startsWith("/preview/")) {
                    String filePath = requestPath.substring(8); // 去掉/preview/
                    serveFile(filePath, out, false);
                    return;
                }

                // 处理浏览目录
                if (requestPath.startsWith("/browse/")) {
                    String browsePath = requestPath.substring(8);
                    sendDirectoryListing(out, browsePath);
                    return;
                }

                // 默认处理文件
                serveFile(requestPath, out, false);
            }

            // 处理POST请求（文件上传）
            private void handlePost(String requestPath, OutputStream out, BufferedReader in) throws IOException {
                if (requestPath.equals("/upload")) {
                    handleFileUpload(out, in);
                } else {
                    sendError(out, 404, "Not Found");
                }
            }

            // 处理文件上传
            private void handleFileUpload(OutputStream out, BufferedReader reader) throws IOException {
                // 读取请求头
                String line;
                int contentLength = 0;
                String boundary = null;

                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.substring(15).trim());
                    } else if (line.startsWith("Content-Type:")) {
                        int boundaryIndex = line.indexOf("boundary=");
                        if (boundaryIndex != -1) {
                            boundary = line.substring(boundaryIndex + 9);
                        }
                    }
                }

                if (boundary == null || contentLength == 0) {
                    sendError(out, 400, "Bad Request");
                    return;
                }

                // 读取文件数据
                char[] buffer = new char[contentLength];
                int read = reader.read(buffer, 0, contentLength);
                String requestBody = new String(buffer, 0, read);

                // 解析multipart/form-data
                String[] parts = requestBody.split("--" + boundary);

                String fileName = null;
                String fileContent = null;
                String uploadPath = null;

                for (String part : parts) {
                    if (part.contains("filename=")) {
                        // 提取文件名
                        int nameIndex = part.indexOf("filename=\"");
                        if (nameIndex != -1) {
                            int endIndex = part.indexOf("\"", nameIndex + 10);
                            fileName = part.substring(nameIndex + 10, endIndex);
                        }

                        // 提取文件内容
                        int contentStart = part.indexOf("\r\n\r\n");
                        if (contentStart != -1) {
                            fileContent = part.substring(contentStart + 4, part.length() - 2);
                        }
                    } else if (part.contains("name=\"path\"")) {
                        // 提取上传路径
                        int contentStart = part.indexOf("\r\n\r\n");
                        if (contentStart != -1) {
                            uploadPath = part.substring(contentStart + 4, part.length() - 2);
                        }
                    }
                }

                if (fileName != null && fileContent != null && uploadPath != null) {
                    // 保存文件
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    File file = new File(uploadDir, fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(fileContent.getBytes());
                    fos.close();

                    // 发送成功响应
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "\r\n" +
                            "<html><body>" +
                            "<h2>File uploaded successfully!</h2>" +
                            "<p>File: " + fileName + "</p>" +
                            "<p>Path: " + uploadPath + "</p>" +
                            "<a href='/browse/" + uploadPath + "'>Go Back</a>" +
                            "</body></html>";
                    out.write(response.getBytes());
                } else {
                    sendError(out, 400, "Invalid upload data");
                }
            }

            // 发送目录列表
            private void sendDirectoryListing(OutputStream out, String path) throws IOException {
                StringBuilder html = new StringBuilder();
                html.append("<html><head>");
                html.append("<title>File Server</title>");
                html.append("<style>");
                html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
                html.append("h1 { color: #333; }");
                html.append("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
                html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
                html.append("th { background-color: #f2f2f2; }");
                html.append("tr:hover { background-color: #f5f5f5; }");
                html.append("a { text-decoration: none; color: #0066cc; }");
                html.append("a:hover { text-decoration: underline; }");
                html.append(".upload-form { margin: 20px 0; padding: 20px; background: #f9f9f9; border: 1px solid #ddd; }");
                html.append("</style>");
                html.append("</head><body>");

                html.append("<h1>File Server</h1>");

                // 显示当前路径
                html.append("<p>Current Path: ").append(path).append("</p>");

                // 添加上传功能
                html.append("<div class='upload-form'>");
                html.append("<h3>Upload File to Current Directory</h3>");
                html.append("<form action='/upload' method='post' enctype='multipart/form-data'>");
                html.append("<input type='hidden' name='path' value='").append(path).append("'>");
                html.append("<input type='file' name='file'>");
                html.append("<input type='submit' value='Upload'>");
                html.append("</form>");
                html.append("</div>");

                // 刷新按钮
                html.append("<button onclick=\"location.reload()\">Refresh</button>");

                // 目录和文件列表
                html.append("<table>");
                html.append("<tr><th>Type</th><th>Name</th><th>Size</th><th>Actions</th></tr>");

                // 添加上一级目录链接
                if (!path.equals("/")) {
                    String parentPath = new File(path).getParent();
                    if (parentPath == null) parentPath = "/";
                    html.append("<tr>");
                    html.append("<td>📁</td>");
                    html.append("<td><a href='/browse/").append(parentPath).append("'>.. (Parent Directory)</a></td>");
                    html.append("<td>-</td>");
                    html.append("<td></td>");
                    html.append("</tr>");
                }

                // 获取共享路径下的文件和文件夹
                List<FileInfo> fileList = new ArrayList<>();

                for (String sharedPath : sharedPaths) {
                    File baseDir = new File(sharedPath);
                    if (baseDir.exists() && path.equals("/")) {
                        // 根目录显示共享路径
                        fileList.add(new FileInfo(baseDir.getName(), baseDir.getAbsolutePath(),
                                baseDir.isDirectory(), baseDir.length()));
                    } else if (path.startsWith(sharedPath) || sharedPath.startsWith(path)) {
                        // 浏览子目录
                        File currentDir = new File(path);
                        if (currentDir.exists() && currentDir.isDirectory()) {
                            File[] files = currentDir.listFiles();
                            if (files != null) {
                                for (File file : files) {
                                    fileList.add(new FileInfo(file.getName(), file.getAbsolutePath(),
                                            file.isDirectory(), file.length()));
                                }
                            }
                        }
                    }
                }

                // 排序：文件夹在前，文件在后
                Collections.sort(fileList, new Comparator<FileInfo>() {
                    @Override
                    public int compare(FileInfo f1, FileInfo f2) {
                        if (f1.isDirectory && !f2.isDirectory) return -1;
                        if (!f1.isDirectory && f2.isDirectory) return 1;
                        return f1.name.compareToIgnoreCase(f2.name);
                    }
                });

                // 显示文件和文件夹
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                for (FileInfo fileInfo : fileList) {
                    html.append("<tr>");

                    // 类型图标
                    if (fileInfo.isDirectory) {
                        html.append("<td>📁</td>");
                    } else {
                        html.append("<td>📄</td>");
                    }

                    // 名称
                    html.append("<td>");
                    if (fileInfo.isDirectory) {
                        html.append("<a href='/browse/").append(fileInfo.path).append("'>");
                        html.append(fileInfo.name).append("/");
                        html.append("</a>");
                    } else {
                        html.append(fileInfo.name);
                    }
                    html.append("</td>");

                    // 大小
                    html.append("<td>");
                    if (fileInfo.isDirectory) {
                        html.append("-");
                    } else {
                        html.append(formatFileSize(fileInfo.size));
                    }
                    html.append("</td>");

                    // 操作
                    html.append("<td>");
                    if (fileInfo.isDirectory) {
                        html.append("<a href='/browse/").append(fileInfo.path).append("'>Open</a>");
                    } else {
                        html.append("<a href='/download/").append(fileInfo.path).append("'>Download</a> | ");
                        html.append("<a href='/preview/").append(fileInfo.path).append("'>Preview</a>");
                    }
                    html.append("</td>");

                    html.append("</tr>");
                }

                html.append("</table>");
                html.append("</body></html>");

                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + html.length() + "\r\n" +
                        "\r\n" +
                        html.toString();

                out.write(response.getBytes());
            }

            // 提供文件服务
            private void serveFile(String filePath, OutputStream out, boolean download) throws IOException {
                File file = new File(filePath);

                if (!file.exists()) {
                    sendError(out, 404, "File not found: " + filePath);
                    return;
                }

                if (!file.canRead()) {
                    sendError(out, 403, "Access denied");
                    return;
                }

                // 检查文件是否在共享路径中
                boolean isShared = false;
                for (String sharedPath : sharedPaths) {
                    if (file.getAbsolutePath().startsWith(sharedPath)) {
                        isShared = true;
                        break;
                    }
                }

                if (!isShared) {
                    sendError(out, 403, "Access denied - File not in shared paths");
                    return;
                }

                // 获取MIME类型
                String mimeType = getMimeType(file.getName());

                // 设置响应头
                String header = "HTTP/1.1 200 OK\r\n";
                header += "Content-Type: " + mimeType + "\r\n";
                header += "Content-Length: " + file.length() + "\r\n";

                if (download) {
                    header += "Content-Disposition: attachment; filename=\"" + file.getName() + "\"\r\n";
                }

                header += "\r\n";

                out.write(header.getBytes());

                // 发送文件内容
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                fis.close();
            }

            // 发送错误响应
            private void sendError(OutputStream out, int code, String message) throws IOException {
                String response = "HTTP/1.1 " + code + " " + message + "\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "<html><body>" +
                        "<h1>" + code + " " + message + "</h1>" +
                        "</body></html>";
                out.write(response.getBytes());
            }

            // 获取MIME类型
            private String getMimeType(String fileName) {
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex != -1) {
                    String ext = fileName.substring(dotIndex + 1).toLowerCase();
                    if (mimeTypes.containsKey(ext)) {
                        return mimeTypes.get(ext);
                    }
                }
                return "application/octet-stream";
            }

            // 格式化文件大小
            private String formatFileSize(long size) {
                if (size < 1024) {
                    return size + " B";
                } else if (size < 1024 * 1024) {
                    return String.format("%.1f KB", size / 1024.0);
                } else if (size < 1024 * 1024 * 1024) {
                    return String.format("%.1f MB", size / (1024.0 * 1024.0));
                } else {
                    return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
                }
            }
        }

        // 文件信息类
        private class FileInfo {
            String name;
            String path;
            boolean isDirectory;
            long size;

            FileInfo(String name, String path, boolean isDirectory, long size) {
                this.name = name;
                this.path = path;
                this.isDirectory = isDirectory;
                this.size = size;
            }
        }
    }



}
