package cn.droidlover.xdroid.tools.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.droidlover.xdroid.Config;
import cn.droidlover.xdroid.tools.encryption.MDUtil;
import cn.droidlover.xdroid.tools.log.Logger;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/7/28 18:04
 * <p>
 * Description: 文件操作工具类
 */
public class FileUtil {

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    private static final String TAG = FileUtil.class.getSimpleName();

    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

    /**
     * 获取文件的行数
     */
    public static int countLines(File file) throws IOException {
        InputStream is = null;
        int count = 0;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] c = new byte[BUFFER_SIZE];
            int readChars;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
            }
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return count;
    }

    /**
     * 以列表的方式获取文件的所有行
     */
    public static List<String> lines(File file) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static File bytesToFile(byte[] buffer, final String filePath) {
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedOutput) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     文件
     * @param encoding 指定读取文件的编码
     */
    public static List<String> lines(File file, String encoding) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     */
    public static List<String> lines(File file, int lines) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file     文件
     * @param lines    起始行数
     * @param encoding 指定读取文件的编码
     */
    public static List<String> lines(File file, int lines, String encoding) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file 文件
     * @param str  内容
     */
    public static boolean appendLine(File file, String str) {
        RandomAccessFile randomFile = null;
        String lineSeparator = System.getProperty("line.separator", "\n");
        try {
            randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(lineSeparator + str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != randomFile) {
                    randomFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file     文件
     * @param str      内容
     * @param encoding 指定写入的编码
     */
    public static boolean appendLine(File file, String str, String encoding) {
        RandomAccessFile randomFile = null;
        String lineSeparator = System.getProperty("line.separator", "\n");
        try {
            randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write((lineSeparator + str).getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != randomFile) {
                    randomFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 快速清空一个超大的文件
     */
    public static boolean cleanFile(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write("");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fw) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取文件的Mime类型
     */
    public static String mimeType(String file) throws IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    /**
     * 获取文件的类型
     * <p>
     * PS:只利用文件头做判断故不全
     */
    public static String fileType(File file) {
        return FileTypeImpl.getFileType(file);
    }

    /**
     * 获取文件最后的修改时间
     */
    public static Date modifyTime(File file) {
        return new Date(file.lastModified());
    }

    /**
     * 获取文件的Hash
     */
    public static String hash(File file) {
        return MDUtil.FileMD(MDUtil.TYPE.MD5, file);
    }

    /**
     * 复制文件
     */
    public static boolean copy(String sourcePath, String targetPath) {
        File file = new File(sourcePath);
        return copy(file, targetPath);
    }

    /**
     * 复制文件
     * 通过该方式复制文件文件越大速度越是明显
     */
    public static boolean copy(File sourceFile, String targetFile) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(new File(targetFile));
            FileChannel in = inputStream.getChannel();
            FileChannel out = outputStream.getChannel();
            //设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                buffer.flip();//准备写入，防止其他读取，锁住文件
                out.write(buffer);
                buffer.clear();//准备读取。将缓冲区清理完毕，移动文件内部指针
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }

                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 创建多级目录
     */
    public static void createPaths(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件支持多级目录
     */
    public static boolean createFiles(String path) {
        File file = new File(path);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除一个文件
     */
    public static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 删除一个目录
     */
    public static boolean deleteDir(File file) {
        List<File> files = listFileAll(file);
        if (null != files) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f);
                } else {
                    deleteFile(f);
                }
            }
        }
        return file.delete();
    }

    /**
     * 快速的删除超大的文件
     */
    public static boolean deleteBigFile(File file) {
        return cleanFile(file) && file.delete();
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 路径
     */
    public static List<File> listFile(String path) {
        File file = new File(path);
        return listFile(file);
    }

    /**
     * 复制目录
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     */
    public static void copyDir(String sourcePath, String targetPath) {
        File file = new File(sourcePath);
        copyDir(file, targetPath);
    }

    /**
     * 复制目录
     *
     * @param sourceFile 源路径
     * @param targetPath 目标路径
     */
    public static void copyDir(File sourceFile, String targetPath) {
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            createPaths(targetPath);
        }
        File[] files = sourceFile.listFiles();
        if (null != files) {
            for (File file : files) {
                String path = file.getName();
                if (file.isDirectory()) {
                    copyDir(file, targetPath + "/" + path);
                } else {
                    copy(file, targetPath + "/" + path);
                }
            }
        }
    }

    /**
     * 罗列指定路径下的全部文件
     */
    public static List<File> listFile(File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     */
    public static List<File> listFileAll(File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (null != files) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFileAll(file));
                }
            }
        }
        return list;
    }

    /**
     * 获取指定目录下的特点文件,通过后缀名过
     */
    public static List<File> listFileFilter(File dirPath, final String postfix) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, postfix));
                } else {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(postfix.toLowerCase())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下搜寻文个文件
     */
    public static List<File> searchFile(File dirPath, String fileName) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, fileName));
                } else {
                    String Name = file.getName().toLowerCase();
                    if (Name.equals(fileName)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查找符合正则表达式reg的的文件
     */
    public static List<File> searchFileReg(File dirPath, String reg) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, reg));
                } else {
                    String Name = file.getName();
                    if (RegularUtil.isMatcher(Name, reg)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 保存图片到本地,并返回File
     */
    public static File saveFile(Context context, String filePath, String fileName, Bitmap bitmap, int sizeType, double size) {
        String path = saveFile(context, filePath, fileName, bitmap);
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                if (sizeType != 0) {
                    if (FileSizeUtil.getFileOrFilesSize(path, FileSizeUtil.SIZE_TYPE_MB) >= size) {
                        return compressPixel(filePath, fileName); ////超过限定大小进行文件压缩
                    }
                }
                return file;
            }
        }
        return null;
    }


    public static File compressPixel(String filePath, String fileName) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = 2;

        //inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        try {
            //load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath + File.separator + fileName, options);
            if (bmp == null) {

                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(filePath + File.separator + fileName);
                    BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        return getFile(bmp, fileName);
    }

    private static File getFile(Bitmap bitmap, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        File file = new File("photo/", fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 保存图片到本地
     */
    public static String saveFile(Context context, String filePath, String fileName, byte[] bytes) {
//        if (!AndPermission.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            return "";
//        }
        String fileFullName = "";
        FileOutputStream fos = null;
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                return null;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            if (fullFile.exists()) {
                fullFile.delete();
            }
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

    /**
     * 保存文件
     */
    public static String saveFile(Context context, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = BitmapUtil.bitmapToBytes(bitmap,true);
        return saveFile(context, filePath, fileName, bytes);
    }

    /**
     * uri 转 file
     *
     * @param uri
     * @param context
     * @return
     */
    public static File uriToFile(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    /**
     * file 转 uri
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取文件长度
     *
     * @param file
     */
    public static void getFileSize(File file) {
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            Logger.d("readableFileSize", "文件" + fileName + "的大小是：" + readableFileSize(file.length()));
        }
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
//    /**
//     * 保存文件
//     *
//     * @param body
//     *         文件流
//     * @param filename
//     *         文件名,含后缀
//     */
//    public static boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
//        Logger.i(TAG, "下载完成，准备写入文件.");
//        Context context = ContextHolder.getContext();
//        // 如果没有读写SD卡的权限，则不写入文件
//        if (null != context && !AndPermission.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            Logger.i(TAG, "请求读写SD卡权限");
//            return false;
//        }
//        // 目录不存在  则创建
//        File dir = new File(Config.ROOT_PATH.get());
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        try {
//            File         futureStudioIconFile = new File(Config.ROOT_PATH.get() + File.separator + filename);
//            InputStream  inputStream          = null;
//            OutputStream outputStream         = null;
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize           = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(futureStudioIconFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    if (read == -1) {
//                        break;
//                    }
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                    Logger.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//                outputStream.flush();
//                ToastUtil.toast("下载成功");
//                //Cutscenes.dismiss(true);
//                return true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * 文件是否存在
     */
    public static boolean isExists(String filename) {
        try {
            File file = new File(Config.ROOT_PATH.get() + File.separator + filename);
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}