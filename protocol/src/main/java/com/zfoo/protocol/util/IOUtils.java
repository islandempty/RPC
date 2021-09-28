package com.zfoo.protocol.util;

import java.io.*;

/**
 * @author islandempty
 * @since 2021/6/16
 **/
public abstract class IOUtils {


    //表示文件或流的结尾
    public static final int EOF = -1;

    // The number of bytes in a byte
    public static final int ONE_BYTE = 1;
    // The number of bytes in a kilobyte
    public static final int BYTES_PER_KB = ONE_BYTE * 1024;
    // The number of bytes in a megabyte
    public static final int BYTES_PER_MB = BYTES_PER_KB * 1024;
    // The number of bytes in a gigabyte
    public static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

    //位计算
    public static final int BITS_PER_BYTE = ONE_BYTE * 8;
    public static final int BITS_PER_KB = BYTES_PER_KB * 8;
    public static final int BITS_PER_MB = BYTES_PER_MB * 8;
    public static final long BITS_PER_GB = BYTES_PER_GB * 8L;

    /**

     *将字节从InputStream复制到OutputStream。
     *<p>
     *大数据流（超过2GB）将返回字节复制值-1
     *在复制完成之后，因为正确的字节数不能作为int返回。
     *对于大型流，请使用copyragle（InputStream、OutputStream）方法。
     *
     *@param input 输入要读取的<code>InputStream</code>
     *@param output 输出写入的<code>OutputStream</code>
     *@返回复制的字节数，如果&gt Integer.MAX_VALUE，则返回-1；
     *@throws IOException IO异常
     */

    public static int copy(final InputStream input, final OutputStream output) throws IOException{
        byte[] buffer = new byte[BYTES_PER_KB];
        long count = 0;
        int n;
        //从InputStream中读取一个数组的数据，如果返回-1 则表示数据读取完成了
        while (EOF != (n = input.read(buffer))){
            output.write(buffer,0,n);
            count +=n;
        }

        if (count > BITS_PER_GB * 2L){
            return -1;
        }
        return (int)count;
    }

    /**
     *以byte[]的形式获取InputStream的内容。
     *
     *@param input 输入要读取的输入流
     *@return 返回请求的字节数组
     *@throws IOException IO异常
     */
    public static byte[] toByteArray(final InputStream input) throws IOException{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input,output);
        var bytes = output.toByteArray();
        IOUtils.closeIO(input,output);
        return bytes;
    }

    //可批量关闭IO流
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) {
            return;
        }

        for (Closeable obj : closeables) {
            if (obj == null) {
                continue;
            }
            try {
                obj.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




}

