package com.ie.protocol.util;

import com.ie.protocol.collection.CollectionUtils;

import java.io.File;
import java.util.*;

/**
 *
 * 文件操作工具类
 *
 * @author islandempty
 * @since 2021/6/17
 **/
public abstract class FileUtils {


    /**
     * 类Unix路径分隔符
     */
    private static final String UNIX_SEPARATOR = StringUtils.SLASH;

    /**
     * Windows路径分隔符
     */
    private static final String WINDOWS_SEPARATOR = StringUtils.BACK_SLASH;

    /**
     * 获取当前系统的换行分隔符
     * <pre>
     * Windows: \r\n
     * Mac: \r
     * Linux: \n
     * </pre>
     */
    public static final String LS = System.lineSeparator();
    public static final String UNIX_LS = "\\n";
    public static final String WINDOWS_LS = "\\r\\n";

    // The file copy buffer size (30 MB)
    public static final long FILE_COPY_BUFFER_SIZE = IOUtils.BYTES_PER_MB * 30;

    /**
     * 用户当前工作目录
     *
     * @return 绝对路径路径
     */
    public static String getProAbsPath(){
        return System.getProperty("user.dir");
    }

    //--------------------------搜索文件----------------------------

    /**
     * DFS搜索文件
     *
     * @param file      需要搜索的文件
     * @param filename  需要搜索的文件名
     * @return
     */
    private static File searchFileInProject(File file,String filename){

        //如果是个文件
        if(file.isFile() && file.getName().equals(filename)){
            return file;
        }

        //如果是个目录
        if(file.isDirectory()){
            //返回某个目录下所有文件和目录的绝对路径
            var files = file.listFiles();
            if (CollectionUtils.isEmpty(files)){
                return null;
            }

            for(File temp : files){
                File ans = searchFileInProject(temp, filename);
                if (null == ans){
                    continue;
                }
                return ans;
            }
        }
            return null;
    }

    /**
     * BFS找出所有文件
     *
     * @param fileOrDirectory
     * @return
     */
    public static List<File> getAllReadableFiles(File fileOrDirectory){
        List<File> readableFileList = new ArrayList<>();
        //LinkedList实现了queue接口
        Queue<File> queue = new LinkedList<>();
        queue.add(fileOrDirectory);
        while (!queue.isEmpty()){
            File file = queue.poll();
            if (file.isDirectory()){
                for (File f: file.listFiles()){
                    queue.offer(f);
                }
                continue;
            }
            if (file.canRead()){
                readableFileList.add(file);
            }
        }
        return readableFileList;
    }
}

