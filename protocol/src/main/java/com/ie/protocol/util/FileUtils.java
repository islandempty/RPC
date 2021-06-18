package com.ie.protocol.util;

import com.ie.protocol.collection.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * 搜索文件
     * @param file
     * @return 没找到直接返回
     */
    public static File searchFileProject(File file){
        return searchFileInProject(new File(getProAbsPath()), file.getName());
    }

    /**
     * 搜索文件
     * 文件名必须是文件全称，包括文件名的后缀
     *
     * @param filename 文件名的全称，包括文件名的后缀
     * @return
     */
    public static File searchFileInProject(String filename){
        return searchFileInProject(new File(getProAbsPath()),filename);
    }

    //----------------------------创建和删除文件----------------------------

    /**
     * 在path文件夹下创建一个filename文件
     *
     * @param path  路径
     * @param fileName  文件名
     * @return 创建新的file
     * @throws IOException
     */
    public static File createFile(String path, String fileName) throws IOException{
        var file = createDirectory(path);
        //getAbsoluteFile
        var newFile = new File(file.getAbsoluteFile() + File.separator +fileName);

        if (newFile.exists()){
            throw new RuntimeException(StringUtils.format("文件已存在[fileName:{}]",fileName));
        }
        //createNewFile文件不存在则创建，存在则不创建并返回false
        if (!newFile.createNewFile()){
            throw new RuntimeException(StringUtils.format("创建文件[fileName:{}]失败",fileName));
        }

        return newFile;
    }

    public static File createDirectory(String path){
        var file = new File(path);
        if (!file.exists()){
            //生成这个路径下的目录
            if (!file.mkdirs()){
                throw new RuntimeException(StringUtils.format("Directory [file:{}] could not be created",file));
            }
        }
        return file;
    }

    /**
     *删除文件。如果文件是一个目录，删除它和所有子目录。
     *
     * @param file  要删除的文件或目录不能为空
     */
    public static void deleteFile(final File file){
        //如果是文件夹
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for(File eachFile:files){
                    deleteFile(eachFile);
                }
            }
            //所有文件都被删除再删除文件夹
            if (!file.delete()){
                throw new RuntimeException("Unable to delete file directory: " + file);
            }
        }else {
            //如果是文件
            boolean filePresent = file.exists();
            if(filePresent){
                if(!file.delete()){
                    throw new RuntimeException("Unable to delete file: " + file);
                }
            }
        }
    }
    //------------------------------复制文件-----------------------
/**
 *将文件复制到保留文件日期的新位置。
 *<p>
 *此方法将指定源文件的内容复制到
 *指定的目标文件。保存目标文件的目录是
 *如果不存在则创建。如果目标文件存在，则
 *方法将覆盖它。
 *<p>
 *<strong>注意：</strong>此方法尝试保留文件的最后一个
 *但是，使用{@link File#setLastModified（long）}修改日期/时间
 *不能保证这次行动会成功。
 *如果修改操作失败，则不提供任何指示。
 *
 * @param srcFile  an existing file to copy, must not be null
 *      * @param destFile the new file, must not be null
 *      * @throws IOException if source or destination is invalid
 *      * @throws IOException if an IO error occurs during copying
 *      * @throws IOException if the output file length is not the same as the input file length after the copy completes
*/
    public static void copyFile(final File srcFile,final File destFile) throws IOException{
        copyFile(srcFile,destFile,true);
    }


    /**
     *将文件复制到保新位置。
     *<p>
     *此方法将指定源文件的内容复制到,指定的目标文件
     *保存目标文件的目录是
     *如果不存在则创建。如果目标文件存在，则
     *方法将覆盖它。
     *<p>
     *<strong>注意：</strong>此方法尝试保留文件的最后一次修改
     *但是，使用{@link File#setLastModified（long）}修改日期/时间
     *不能保证这次行动会成功。
     *如果修改操作失败，则不提供任何指示。
     *
     * @param srcFile          an existing file to copy, must not be {@code null}
     * @param destFile         the new file, must not be {@code null}
     * @param preserveFileDate true if the file date of the copy
     *                         should be the same as the original
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @throws IOException if the output file length is not the same as the input file length after the copy completes
     */
    public static void copyFile(final File srcFile,final File destFile,final boolean preserveFileDate)throws IOException{
        checkFileRequirements(srcFile,destFile);

        if (srcFile.isDirectory()){
            throw new IOException(StringUtils.format("Source [srcFile:{}] exists but is a directory", srcFile));
        }

        if (srcFile.getCanonicalFile().equals(destFile.getCanonicalFile())){
            throw new IOException(StringUtils.format("Source [srcFile:{}] and destination [destFile:{}] are the same", srcFile, destFile));
        }

        final File parentFile = destFile.getParentFile();
        if (parentFile != null){
            if (!parentFile.mkdirs()&&!parentFile.isDirectory()){
                throw new IOException(StringUtils.format("Destination [parentFile:{}] directory cannot be created", parentFile));
            }
        }
        //存在但没有访问权限
        if (destFile.exists()&&!destFile.canWrite()){
            throw new IOException(StringUtils.format("Destination [destFile:{}] exists but is read-only", destFile));
        }
        doCopyFile(srcFile,destFile,preserveFileDate);

    }

    /**
     * 检查复制的要求
     *
     * @param src   源文件
     * @param dest  目标文件
     * @throws FileNotFoundException  如果目标文件不存在
     */
    private static void checkFileRequirements(File src,File dest) throws FileNotFoundException{
        if (src == null){
            throw new NullPointerException("Source must not be null");
        }

        if (dest == null){
            throw new NullPointerException("Destination must not be null");
        }

        if (!src.exists()){
            throw new FileNotFoundException(StringUtils.format("Source [src:{}] does not exist", src));
        }
    }
}

