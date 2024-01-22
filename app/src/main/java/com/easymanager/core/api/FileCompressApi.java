package com.easymanager.core.api;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * 该类负责实现tar、gzip、zip、bzip、xz解压缩文件功能
 *
 *
 * */

public class FileCompressApi extends baseAPI {

    public static final int DEFAULT_TYPE = -1;
    public static final int TAR_GZIP_TYPE = 1;
    public static final int TAR_BZIP_TYPE = 2;
    public static final int TAR_XZ_TYPE = 3;

    public static final int TAR_LZMA_TYPE = 4;

    public static final int TAR_ZSTD_TYPE=5;

    public static final int ZIP_COMPRESS_TYPE = 0;
    public static final int ZIP_DECOMPRESS_TYPE = 1;
    public static final int TAR_COMPRESS_TYPE = 2;
    public static final int TAR_DECOMPRESS_TYPE = 3;
    public static final int TAR_GZIP_COMPRESS_TYPE = 4;
    public static final int TAR_GZIP_DECOMPRESS_TYPE = 5;
    public static final int TAR_BZIP_COMPRESS_TYPE = 6;
    public static final int TAR_BZIP_DECOMPRESS_TYPE = 7;
    public static final int TAR_XZ_COMPRESS_TYPE = 8;
    public static final int TAR_XZ_DECOMPRESS_TYPE = 9;
    public static final int TAR_LZMA_COMPRESS_TYPE = 10;
    public static final int TAR_LZMA_DECOMPRESS_TYPE = 11;
    public static final int TAR_ZSTD_COMPRESS_TYPE = 12;
    public static final int TAR_ZSTD_DECOMPRESS_TYPE = 13;


    public FileCompressApi(){}
    /**
     * 压缩指定目录下的所有文件
     * @param dir 指定压缩目录
     * @param zipPath 指定输出压缩包位置
     */
    public void zipCompress(String dir,String zipPath) throws IOException {
        File file = new File(dir);
        /**1.为zip文件创建目录*****/
        File zipOutFile = new File(zipPath);
        if(!zipOutFile.getParentFile().exists()){
            zipOutFile.getParentFile().mkdirs();
        }

        /**为zip文件创建目录*****/
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath));
        if(file.isDirectory()){
            File[] subFiles = file.listFiles();
            for (File subFile : subFiles) {
                buildZipDir(zipOutputStream,subFile,null);
            }
        }else{
            buildZipDir(zipOutputStream,file,null);
        }

        zipOutputStream.close();
    }

    public void buildZipDir(ZipOutputStream zipOut,File file,String baseDir) throws IOException {
        if(file.isFile()){
            String zipEntryName = baseDir == null ? file.getName() : baseDir+File.separator+file.getName();
            zipOut.putNextEntry(new ZipEntry(zipEntryName));
            byte[] buffer = new byte[1024];
            try(InputStream inputStream = new FileInputStream(file)){
                int len = -1;
                while((len = inputStream.read(buffer)) != -1){
                    zipOut.write(buffer,0,len);
                }
                zipOut.flush();
                zipOut.closeEntry();
            }
        }
        else{//文件是dir，继续递归找文件
            File[] subFiles = file.listFiles();
            if(subFiles.length ==0){//处理空文件夹
                String zipName = baseDir == null ? file.getName() :baseDir + File.separator+ file.getName() ;
                zipOut.putNextEntry(new ZipEntry(zipName));
                zipOut.closeEntry();
            }else{
                for (File subFile : subFiles) {
                    String subBaseDir =baseDir == null ? file.getName() :baseDir + File.separator+ file.getName() ;
                    buildZipDir(zipOut,subFile,subBaseDir);
                }
            }
        }
    }


    public boolean isNull(String str){
        return str == null || str.equals("");
    }


    public void zipDeCompress(String zipPath,String unzipPath) throws IOException {
        File file = new File(zipPath);
        ZipFile zipFile = new ZipFile(file);//zip文件
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        if(isNull(unzipPath)){
            unzipPath = zipPath.replace(".zip","");
        }
        ZipEntry nextEntry = null;
        while( (nextEntry = zipInputStream.getNextEntry()) != null){
            String name = nextEntry.getName();
            String fileOutPath = unzipPath + File.separator + name;
            File fileOut = new File(fileOutPath);
            if(!fileOut.getParentFile().exists()){
                fileOut.getParentFile().mkdirs();
            }
            if(!fileOut.exists()){
                fileOut.createNewFile();
            }

            OutputStream  output = new FileOutputStream(fileOutPath);
            InputStream inputStream = zipFile.getInputStream(nextEntry);//通过ZipFile获取到ZipEntry的文件流
            int read = -1;
            byte[] buffer = new byte[1024];
            while((read = inputStream.read(buffer) ) != -1){
                output.write(buffer,0, read);
            }
            output.flush();
            output.close();
            inputStream.close();
        }
        zipInputStream.close();
        zipFile.close();
    }


    public void tarCompress(String dirPath , String outName,int mode) throws IOException {
        File file = new File(dirPath);
        File file1 = new File(outName);
        if(file1.exists() && file1.isFile()){
            file1.delete();
        }else{
            file1.getParentFile().mkdirs();
        }
        if(file.exists()){
            try (TarArchiveOutputStream out = getTarOutStream(outName,mode)){
                out.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
                addToArchiveCompression(out, file, ".");
            }
        }

    }

    public void tarDeCompress(String filePath, String outPath , int mode) throws IOException {
        File file = new File(filePath);
        File outDirPath = new File(outPath);
        if(file.exists() && file.isFile()){
            if(outDirPath.exists()){
                outDirPath.delete();
            }

            File parentFile = outDirPath.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }

            try (TarArchiveInputStream fin = getTarInStream(filePath,mode)){
                TarArchiveEntry entry;
                while ((entry = fin.getNextTarEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    File curfile = new File(outDirPath, entry.getName());
                    File parent = curfile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    IOUtils.copy(fin, new FileOutputStream(curfile));
                }
            }
        }

    }

    public TarArchiveOutputStream  getTarOutStream(String s,int mode) throws IOException {
        switch (mode){
            case TAR_GZIP_TYPE:
                return new TarArchiveOutputStream(new GzipCompressorOutputStream(new FileOutputStream(s)));
            case TAR_BZIP_TYPE:
                return new TarArchiveOutputStream(new BZip2CompressorOutputStream(new FileOutputStream(s)));
            case TAR_XZ_TYPE:
                return new TarArchiveOutputStream(new XZCompressorOutputStream(new FileOutputStream(s)));
            case TAR_ZSTD_TYPE:
            case TAR_LZMA_TYPE:
                return new TarArchiveOutputStream(new LZMACompressorOutputStream(new FileOutputStream(s)));
            default:
                return new TarArchiveOutputStream(new FileOutputStream(s));
        }
    }

    public TarArchiveInputStream  getTarInStream(String s,int mode) throws IOException {
        switch (mode){
            case TAR_GZIP_TYPE:
                return new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(s)));
            case TAR_BZIP_TYPE:
                return new TarArchiveInputStream(new BZip2CompressorInputStream(new FileInputStream(s)));
            case TAR_XZ_TYPE:
                return new TarArchiveInputStream(new XZCompressorInputStream(new FileInputStream(s)));
            case TAR_ZSTD_TYPE:
            case TAR_LZMA_TYPE:
                return new TarArchiveInputStream(new LZMACompressorInputStream(new FileInputStream(s)));
            default:
                return new TarArchiveInputStream(new FileInputStream(s));
        }

    }

    public void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException {
        String entry = (dir.equals(".")?"":dir + File.separator) + file.getName();
        if (file.isFile()){
            TarArchiveEntry tarArchiveEntry =  new TarArchiveEntry(file, entry);
            tarArchiveEntry.setMode(tarArchiveEntry.getMode() | 0755);
            tarArchiveEntry.setSize(file.length());
            out.putArchiveEntry(tarArchiveEntry);
            try (FileInputStream in = new FileInputStream(file)){
                IOUtils.copy(in, out);
            }
            out.closeArchiveEntry();
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null){
                for (File child : children){
                    addToArchiveCompression(out, child, entry);
                }
            }
        }
    }

    //文件压缩或者解压函数
    public void CompressOrDecompressFile(String dirPath , String outPath , int mode) {
        try {
            switch (mode){
                case ZIP_COMPRESS_TYPE:
                    zipCompress(dirPath, outPath);
                    break;
                case ZIP_DECOMPRESS_TYPE:
                    zipDeCompress(dirPath,outPath);
                    break;
                case TAR_COMPRESS_TYPE:
                    tarCompress(dirPath,outPath, DEFAULT_TYPE);
                    break;
                case TAR_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath,outPath,DEFAULT_TYPE);
                    break;
                case TAR_GZIP_COMPRESS_TYPE:
                    tarCompress(dirPath,outPath, TAR_GZIP_TYPE);
                    break;
                case TAR_GZIP_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath,outPath,TAR_GZIP_TYPE);
                    break;
                case TAR_BZIP_COMPRESS_TYPE:
                    tarCompress(dirPath,outPath, TAR_BZIP_TYPE);
                    break;
                case TAR_BZIP_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath,outPath,TAR_BZIP_TYPE);
                    break;
                case TAR_XZ_COMPRESS_TYPE:
                    tarCompress(dirPath,outPath, TAR_XZ_TYPE);
                    break;
                case TAR_XZ_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath,outPath,TAR_XZ_TYPE);
                    break;
                case TAR_LZMA_COMPRESS_TYPE:
                    tarCompress(dirPath,outPath,TAR_LZMA_TYPE);
                    break;
                case TAR_LZMA_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath, outPath, TAR_LZMA_TYPE);
                    break;
                case TAR_ZSTD_COMPRESS_TYPE:
                    tarCompress(dirPath, outPath, TAR_ZSTD_TYPE);
                    break;
                case TAR_ZSTD_DECOMPRESS_TYPE:
                    tarDeCompress(dirPath, outPath, TAR_ZSTD_TYPE);
                    break;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
