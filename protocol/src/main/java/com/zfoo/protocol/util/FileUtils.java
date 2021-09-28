package com.zfoo.protocol.util;

import com.zfoo.protocol.collection.CollectionUtils;

import java.io.*;
import java.nio.channels.FileChannel;
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
    public static File searchFileInProject(File file){
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

    public static void copyFileToDirectory(final File srcFile, final File destDir) throws IOException{
        copyFileToDirectory(srcFile,destDir,true);
    }

    public static void copyFileToDirectory(final File srcFile,final File destDir, final boolean preserveFileDate)throws IOException{
        if (destDir == null){
            throw new NullPointerException("Destination must not be null");
        }

        if (destDir.exists() && !destDir.isDirectory()){
            throw new IllegalArgumentException(StringUtils.format("Destination [destDir:{}] is not a directory", destDir));
        }

        final File destFile = new File(destDir , srcFile.getName());
        copyFile(srcFile,destFile,preserveFileDate);
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

    /**
     *内部复制文件方法。
     *这将缓存原始文件长度
     *如果输出文件长度与当前输入文件长度不同,引发IOException
     *因此，如果文件更改大小，它可能会失败。
     *如果输入文件被截断了一部分，它也可能会因“IllegalArgumentException:Negative size”而失败
     *通过复制数据，新文件的大小小于当前位置。
     * @param srcFile          the validated source file, must not be {@code null}
     * @param destFile         the validated destination file, must not be {@code null}
     * @param preserveFileDate whether to preserve the file date
     * @throws IOException              if an error occurs
     * @throws IOException              if the output file length is not the same as the input file length after the
     *                                  copy completes
     * @throws IllegalArgumentException "Negative size" if the file is truncated so that the size is less than the
     *                                  position
     */
    private static void doCopyFile(final File srcFile,final File destFile,final boolean preserveFileDate)throws IOException{
        if (destFile.exists()&&destFile.isDirectory()){
            throw new IOException(StringUtils.format("Destination [destFile:{}] exists but is a directory", destFile));
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output =null ;

        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            final long size = input.size();
            long pos =0;
            long count;

            //每次最多复制FILE_COPY_BUFFER_SIZE大小的文件
            while (pos < size){
                final long remain = size-pos;
                count = Math.min(remain,FILE_COPY_BUFFER_SIZE);
                final long bytesCopied = output.transferFrom(input,pos,count);
                if(bytesCopied==0){
                    break;
                }
                pos+=bytesCopied;
            }
        }finally {
            IOUtils.closeIO(output,fos,input,fis);
        }

        //复制出错
        final long srcLen = srcFile.length();
        final long dstLen = destFile.length();
        if (srcLen != dstLen){
            throw new IOException(StringUtils.format("Failed to copy full contents from [srcFile:{}] to [destFile:{}] Expected length:[srcLen:{}] Actual [dstLen:{}]"
                    , srcFile, destFile, srcLen, dstLen));
        }
        //是否保留文件日期
        if (preserveFileDate){
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    //-------------------------------------------读取文件----------------------------------------

    /**
     *将文件内容读入字节数组
     *
     * @param file 需要读的文件,不能为空
     * @return  文件的内容
     * @throws IOException
     */
    public static byte[] readFileToByteArray(final File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toByteArray(in);
        }finally {
            IOUtils.closeIO(in);
        }
    }

    public static String readFileToString(final File file){
        return StringUtils.joinWith(StringUtils.EMPTY , readFileToStringList(file));
    }

    //将文件内容转换为字符串
    public static List<String> readFileToStringList(final File file){

        FileInputStream fileInputStream = null;
        //从字节流到字符流
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        var list = new ArrayList<String>();
        try {
            fileInputStream = openInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream,StringUtils.DEFAULT_CHARSET_NAME);
            bufferedReader =new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                list.add(line);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeIO(bufferedReader,inputStreamReader,fileInputStream);
        }
        return list;
    }

    /**
     * 以追加的方式写入一个content
     * @param file  文件的绝对路径
     * @param content 写入的内容
     */
    public static void writeStringToFile(File file , String content){
        //字节流
        FileOutputStream fileOutputStream = null;
        // 转换流，设置编码集和解码集 .处理乱码问题，是字节到字符的桥梁
        OutputStreamWriter outputStreamWriter = null;
        //处理流中的缓存流，提高效率
        BufferedWriter bufferedWriter =null;
        // 如果不用缓冲流的话，程序是读一个数据，写一个数据，这样在数据量大的程序中非常影响效率。
        // 缓冲流作用是把数据先写入缓冲区，等缓冲区满了，再把数据写到文件里。这样效率就大大提高了

        try {
            //以追加的方式打开文件
            fileOutputStream = openOutputStream(file , true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream,StringUtils.DEFAULT_CHARSET_NAME);
            bufferedWriter =new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeIO(bufferedWriter, outputStreamWriter, fileOutputStream);
        }
    }

    //把输入流写到文件
    public static void writeInputStreamToFile(File file , InputStream inputStream){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openOutputStream(file,true);
            //把内容写入的操作在copy里
            IOUtils.copy(inputStream,fileOutputStream);
        }catch (IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeIO(fileOutputStream);
        }
    }
    //----------------------------------------打开和关闭文件流------------------------------------------

    /**
     * 为指定文件打开一个 FileInputStream且能够提供更好的错误信息而不是简单的调用流
     *
     * @param file 需要打开的文件，不能为空
     * @return  指定文件的流
     * @throws IOException
     */
    public static FileInputStream openInputStream(final File file) throws IOException{
        if (file.exists()){
            if (file.isDirectory()){
                throw new IOException(StringUtils.format("File [file:{}] exists but is a directory", file));
            }
            if (!file.canRead()){
                throw new IOException(StringUtils.format("File [file:{}] cannot be read", file));
            }
        }else {
            throw new FileNotFoundException(StringUtils.format("File [file:{}] does not exist", file));
        }
        return new FileInputStream(file);
    }


    /**
     * 如果文件不存在，则创建该文件。最好指定为true，以追加的方式打开文件
     * <p>
     * The parent directory will be created if it does not exist.The file will be created if it does not exist.
     *
     * @param file   the file to open for output, must not be {@code null}
     * @param append if {@code true}, then bytes will be added to the end of the file rather than overwriting
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()){
            if (file.isDirectory()){
                throw new IOException(StringUtils.format("File [file:{}] exists but is a directory", file));
            }
            if ((!file.canWrite())){
                throw new IOException(StringUtils.format("File [file:{}] cannot be written to", file));
            }
        }else {
            final File parentFile = file.getParentFile();
            if (parentFile != null){
                if (!parentFile.mkdirs() && !parentFile.isDirectory()){
                    throw new IOException(StringUtils.format("Directory [parentFile:{}] could not be created", parentFile));
                }
            }
        }
        return new FileOutputStream(file,append);
    }
    //-------------------------------------------文件名称---------------------------------

    /**
     * 获得文件的拓展名，拓展名不带"."
     * @param fileName
     * @return
     */
    public static String fileExtName(String fileName){
        if (StringUtils.isBlank(fileName)){
            return StringUtils.EMPTY;
        }
        var fileExtName = StringUtils.substringAfterLast(fileName,StringUtils.PERIOD);
        if (StringUtils.isBlank(fileExtName)||fileExtName.contains(UNIX_SEPARATOR)||fileExtName.contains(WINDOWS_SEPARATOR)){
            return StringUtils.EMPTY;
        }
        return fileExtName;
    }
    /**
     * 获得文件的名称,不带"."和拓展名
     */
    public static String fileSimpleName(String fileName){
        if (StringUtils.isBlank(fileName)){
            return StringUtils.EMPTY;
        }
        var fileSimpleName = StringUtils.substringBeforeLast(fileName,StringUtils.PERIOD);
        if (StringUtils.isBlank(fileSimpleName)||fileSimpleName.contains(UNIX_SEPARATOR)||fileSimpleName.contains(WINDOWS_SEPARATOR)){
            return StringUtils.EMPTY;
        }
        return fileSimpleName;
    }

}

