package org.turing.vpn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
  
/** 
 *  
 * 文件操作的工具类 
 *  
 * @version [8.0,2011-5-20 下午04:29:53] 
 */  
public class FileUtil {  
  
    private static final int DEFAULT_READING_SIZE = 8192;  
  
    public static void copyFile(File sourceFile, File destFile) throws IOException {  
        if (!destFile.exists()) {  
            File parent = destFile.getParentFile();  
            if (parent != null) {  
                if (!parent.exists()) {  
                    parent.mkdirs();  
                }  
            }  
            destFile.createNewFile();  
        }  
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(sourceFile);  
            out = new FileOutputStream(destFile);  
            copy(in, out);  
        } finally {  
            if (in != null)  
                in.close();  
            if (out != null)  
                out.close();  
        }  
    }  
  
    public static void copy(InputStream in, OutputStream out) throws IOException {  
        byte[] buf = new byte[1024];  
        int len = 0;  
        while ((len = in.read(buf, 0, buf.length)) != -1) {  
            out.write(buf, 0, len);  
        }  
    }  
  
    public static void writeFile(File file, byte[] contents) throws IOException {  
        if (!file.exists()) {  
            file.createNewFile();  
        }  
        if (!file.isFile()) {  
            throw new IOException("File to be written not exist, file path : " + file.getAbsolutePath());  
        }  
        FileOutputStream fileOut = null;  
        try {  
            fileOut = new FileOutputStream(file);  
            BufferedOutputStream bOutput = new BufferedOutputStream(fileOut);  
            bOutput.write(contents);  
            bOutput.flush();  
        } finally {  
            if (fileOut != null) {  
                try {  
                    fileOut.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
  
    }  
  
    public static byte[] readFileContent(File file) throws IOException {  
        if (!file.exists() || !file.isFile()) {  
            throw new IOException("File to be readed not exist, file path : " + file.getAbsolutePath());  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        FileInputStream in = new FileInputStream(file);  
        byte[] contents = new byte[0];  
        try {  
            copy(in, out);  
            contents = out.toByteArray();  
        } catch (IOException e) {  
            throw e;  
        } finally {  
            if(in != null){  
                try {  
                    in.close();  
                } catch (IOException e) {  
                };  
            }  
             
        }  
        return contents;  
    }  
  
    public static String readFileContentAsString(File file) throws IOException {  
        return readFileContentAsString(file, null);  
    }  
  
    public static String readFileContentAsString(File file, String charsetName) throws IOException {  
        if (!file.exists() || !file.isFile()) {  
            throw new IOException("File to be readed not exist, file path : " + file.getAbsolutePath());  
        }  
  
        FileInputStream fileIn = null;  
        InputStreamReader inReader = null;  
        BufferedReader bReader = null;  
        try {  
            fileIn = new FileInputStream(file);  
            inReader = charsetName == null ? new InputStreamReader(fileIn) : new InputStreamReader(fileIn, charsetName);  
            bReader = new BufferedReader(inReader);  
            StringBuffer content = new StringBuffer();  
            char[] chBuffer = new char[1024];  
            int readedNum = -1;  
            while ((readedNum = bReader.read(chBuffer)) != -1) {  
                content.append(chBuffer, 0, readedNum);  
            }  
  
            return content.toString();  
        } finally {  
            if (fileIn != null) {  
                try {  
                    fileIn.close();  
                } catch (IOException e) {  
                }  
            }  
  
            if (bReader != null) {  
                try {  
                    bReader.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
    }  
  
    /** 
     * 追加内容到文件里，会覆盖 
     *  
     * @param file 
     * @param content 
     * @throws IOException 
     */  
    public static void writeStringAsFileContent(File file, String content) throws IOException {  
        writeStringAsFileContent(file, content, null);  
    }  
  
    public static void writeStringAsFileContent(File file, String content, String charsetName) throws IOException {  
        if (!file.exists() || !file.isFile()) {  
            throw new IOException("File to be written not exist, file path : " + file.getAbsolutePath());  
        }  
  
        FileOutputStream fileOut = null;  
        OutputStreamWriter outWriter = null;  
        BufferedWriter bWriter = null;  
        try {  
            fileOut = new FileOutputStream(file);  
            outWriter = charsetName == null ? new OutputStreamWriter(fileOut)  
                                           : new OutputStreamWriter(fileOut, charsetName);  
            bWriter = new BufferedWriter(outWriter);  
            bWriter.write(content);  
            bWriter.flush();  
        } finally {  
            if (fileOut != null) {  
                try {  
                    fileOut.close();  
                } catch (IOException e) {  
                }  
            }  
  
            if (bWriter != null) {  
                try {  
                    bWriter.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
    }  
  
    /** 
     * 删除指定目录及其子目录下的指定文件 
     *  
     * @param dir 
     * @param filter 
     * @throws IOException 
     */  
    public static void deleteWholeDirectory(File dir) throws IOException {  
        if (!dir.exists() || !dir.isDirectory()) {  
            throw new IOException("Directory to be deleted not exist, directory path : " + dir.getAbsolutePath());  
        }  
  
        File[] children = dir.listFiles();  
        for (File child : children) {  
            if (child.isDirectory()) {  
                deleteWholeDirectory(child);  
            } else {  
                child.delete();  
            }  
        }  
  
        dir.delete();  
    }  
  
    /** 
     * 删除指定目录及其子目录下的指定文件 
     *  
     * @param dir 
     * @param filter 
     * @throws IOException 
     */  
    public static void deleteFilesInWholeDirectory(File dir, FilenameFilter filter) throws IOException {  
        if (!dir.exists() || !dir.isDirectory()) {  
            throw new IOException("Directory to be deleted not exist, directory path : " + dir.getAbsolutePath());  
        }  
  
        File[] children = dir.listFiles(filter);  
        for (File child : children) {  
            if (child.isDirectory()) {  
                deleteFilesInWholeDirectory(child, filter);  
            } else {  
                child.delete();  
            }  
        }  
  
        dir.delete();  
    }  
  
  
    /** 
     * 从XML文件中获取字符串内容 
     *  
     * @param xmlFilePath 
     * @return 
     * @throws IOException 
     */  
    public static String getStringFromXMLFile(String xmlFilePath) throws IOException {  
        File file = new File(xmlFilePath);  
        return getStringFromXMLFile(file);  
    }  
  
    /** 
     * 从XML文件中获取字符串内容 
     *  
     * @param xmlFile 
     * @return 
     * @throws IOException 
     */  
    public static String getStringFromXMLFile(File xmlFile) throws IOException {  
        String temp = "";  
        String result = "";  
        if (xmlFile.exists()) {  
            BufferedReader reader = null;  
            try {  
                reader = new BufferedReader(new FileReader(xmlFile));  
                while ((temp = reader.readLine()) != null) {  
                    result = result + temp;  
                }  
            } catch (FileNotFoundException e) {  
                throw new FileNotFoundException("file not found");  
            } catch (IOException e) {  
                throw new IOException();  
            } finally {  
                try {  
                    reader.close();  
                    reader = null;  
                } catch (IOException e) {  
                }  
            }  
  
            return result;  
        } else {  
            return null;  
        }  
  
    }  
  
    /** 
     *  
     * @param filePath 路径 
     * @param isExt 是否需要扩展名（如果有的话） 
     * @return 
     */  
    public static String getFileNameFromPath(String filePath, boolean isExt) {  
        File file = new File(filePath);  
        return getFileNameFromPath(file, isExt);  
  
    }  
  
    // 不带扩展名  
    public static String getFileNameFromPath(String filePath) {  
        File file = new File(filePath);  
        return getFileNameFromPath(file, false);  
  
    }  
  
    public static String getFileNameFromPath(File file, boolean isExt) {  
  
        String filePath = file.getAbsolutePath();  
  
        int begin = filePath.lastIndexOf(File.separator);  
        int end = filePath.lastIndexOf(".");  
  
        String fileName = null;  
        // 处理没有扩展名的文件  
        if (end == -1 || end < begin) {  
            fileName = filePath.substring(begin + 1);  
        } else {  
            if (isExt) {  
                fileName = filePath.substring(begin + 1);  
            } else {  
                fileName = filePath.substring(begin + 1, end);  
            }  
        }  
        return fileName;  
    }  
  
    public static boolean exists(URL url) {  
        // check if a file exists  
        if (url.getProtocol().equals("file")) {  
            File f = new File(url.getFile());  
            return f.exists();  
        }  
  
        // check if a jar file entry exists  
        if (url.getProtocol().equals("jar")) {  
            String spec = url.getFile();  
  
            if (spec.startsWith("file:")) {  
                int separator = spec.indexOf('!');  
                if (separator == -1) {  
                    return false;  
                }  
  
                String jarFileName = spec.substring(5, separator++);  
                String entryName = null;  
  
                // if ! is the last letter of the inner URL, entry not exist  
                if (++separator != spec.length()) {  
                    entryName = spec.substring(separator, spec.length());  
                } else {  
                    return false;  
                }  
  
                try {  
                    JarFile jarFile = new JarFile(jarFileName);  
                    JarEntry jarEntry = (JarEntry) jarFile.getEntry(entryName);  
                    jarFile.close();  
                    return jarEntry != null;  
                } catch (IOException ex) {  
                    return false;  
                }  
            }  
        }  
  
        // check if a resource exists  
        try {  
            InputStream is = url.openStream();  
            is.close();  
            return true;  
        } catch (IOException e) {  
            return false;  
        }  
    }  
  
    /** 
     * 拷贝隐藏文件(文件名以.开头的文件) 
     *  
     * @param source 
     * @param dest 
     * @throws IOException 
     */  
    public static void copyFilesIngoreHiddenFiles(File source, File dest) throws IOException {  
        if (source != null /*&& source.exists()*/&& dest != null /*&& dest.exists()*/) {  
            if (!source.getName().startsWith(".")) {  
                copyFile2(source, dest);  
  
                if (source.isDirectory()) {  
                    File[] list = source.listFiles();  
                    if (list != null) {  
                        for (File file : list) {  
                            copyFilesIngoreHiddenFiles(file, new File(dest.getAbsolutePath() + File.separator  
                                                                      + file.getName()));  
                        }  
                    }  
                }  
            }  
        }  
    }  
  
    /** 
     * 复制文件夹 
     *  
     * @throws IOException 
     */  
    public static void copyFolder(File srcDir, File destDir, List<?> excludedList) throws IOException {  
        if (destDir == null || !destDir.exists()) {  
            throw new FileNotFoundException("File destDir not existed");  
        }  
        if (srcDir == null || !srcDir.exists()) {  
            throw new FileNotFoundException("File srcDir not existed");  
        }  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            if (excludedList != null && excludedList.contains(files[i])) {  
                continue;  
            }  
            String fileName = files[i].getName();  
            if (files[i].isFile()) {  
                copyFileToDir(files[i], destDir);  
            } else {  
                File file = new File(destDir, fileName);  
                file.mkdir();  
                copyFolder(files[i], file, excludedList);  
            }  
        }  
    }  
  
    public static void copyFileToDir(File sourceFile, File destDir) throws IOException {  
        String name = sourceFile.getName();  
        File destFile = new File(destDir, name);  
        if (sourceFile.isDirectory()) {  
            destFile.mkdirs();  
            return;  
        }  
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(sourceFile);  
            out = new FileOutputStream(destFile);  
            copy(in, out);  
        } finally {  
            if (in != null)  
                in.close();  
            if (out != null)  
                out.close();  
        }  
    }  
  
    public static void copyFile2(File sourceFile, File destFile) throws IOException {  
        if (sourceFile.isDirectory()) {  
            destFile.mkdirs();  
            return;  
        }  
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(sourceFile);  
            out = new FileOutputStream(destFile);  
            copy(in, out);  
        } finally {  
            if (in != null)  
                in.close();  
            if (out != null)  
                out.close();  
        }  
    }  
  
    public static void copyFile(File sourceFile, File destDir, String newFileName) throws IOException {  
        File destFile = new File(destDir, newFileName);  
        if (sourceFile.isDirectory()) {  
            destFile.mkdirs();  
            return;  
        }  
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(sourceFile);  
            out = new FileOutputStream(destFile);  
            copy(in, out);  
        } finally {  
            if (in != null)  
                in.close();  
            if (out != null)  
                out.close();  
        }  
  
    }  
  
    public static byte[] toByteArray(String path) throws IOException {  
        File file = new File(path);  
        FileInputStream in = null;  
        try {  
            in = new FileInputStream(file);  
            byte[] data = new byte[(int) file.length()];  
            in.read(data);  
            return data;  
        } finally {  
            in.close();  
        }  
    }  
  
    /** 
     *  
     * @param sourceDir 
     * @param destDir 
     * @param excludedDirName 
     * @throws IOException 
     */  
    public static void copyDir(File sourceDir, File destDir, String excludedDirName) throws IOException {  
        copyDir(sourceDir, destDir, true, excludedDirName);  
    }  
  
    public static void copyDir(File from, File to) throws IOException {  
        copyDir(from, to, true, null);  
    }  
  
    public static void copyDir(File from, File to, boolean includeSubdirs) throws IOException {  
        copyDir(from, to, includeSubdirs, null);  
    }  
  
    public static void copyDir(File from, File to, boolean includeSubdirs, String excludedDirName) throws IOException {  
        if (!to.exists()) {  
            to.mkdirs();  
        }  
        if (from == null || !from.isDirectory() || !to.isDirectory())  
            return;  
        File fs[] = from.listFiles();  
        if (fs == null)  
            return;  
        for (int i = 0; i < fs.length; i++) {  
            String n = fs[i].getName();  
            File c = new File(to, n);  
            if (fs[i].isDirectory() && !includeSubdirs)  
                continue;  
            if (fs[i].isDirectory()) {  
                if (fs[i].getName().equals(excludedDirName)) {  
                    continue;  
                }  
                c.mkdirs();  
                copyDir(fs[i], c, includeSubdirs, excludedDirName);  
                continue;  
            }  
            copyFileToDir(fs[i], to);  
        }  
    }  
  
    public static File makeTempdir() throws IOException {  
        return makeTempDir((File) null, null, null);  
    }  
  
    public static File makeTempDir(File parentDir, String prefix, String suffix) throws IOException {  
        if (suffix == null)  
            suffix = ".tmp";  
        File tmpdir = null;  
        if (parentDir == null) {  
            tmpdir = new File(System.getProperty("java.io.tmpdir", "/temp"));  
        } else {  
            tmpdir = parentDir;  
        }  
        int counter = new Random().nextInt() & 0xffff;  
        File f;  
        do {  
            counter++;  
            f = new File(tmpdir, prefix + Integer.toString(counter) + suffix);  
        } while (f.exists() || !f.mkdirs());  
        return f;  
    }  
  
    public static File makeTempDir(String parentDir, String prefix, String suffix) throws IOException {  
        File fileParentDir = null;  
        if (parentDir != null)  
            fileParentDir = new File(System.getProperty("java.io.tmpdir", "/temp"), parentDir);  
        return makeTempDir(fileParentDir, prefix, suffix);  
    }  
  
    public static File getTempFile(File parentDir) {  
        if (parentDir == null)  
            parentDir = new File(System.getProperty("java.io.tmpdir", "/temp"));  
        int counter = new Random().nextInt() & 0xffff;  
        File f;  
        do {  
            counter++;  
            f = new File(parentDir, "tempfile" + Integer.toString(counter));  
        } while (f.exists());  
        return f;  
    }  
  
    /** 
     * get temp folder 
     *  
     * @return the temp folder may be null 
     */  
    public static File getTempFolder() {  
        File parentDir = new File(System.getProperty("java.io.tmpdir", "/temp"));  
        int counter = new Random().nextInt() & 0xffff;  
        File f = null;  
        do {  
            counter++;  
            f = new File(parentDir, "tempfolder" + Integer.toString(counter));  
        } while (f.exists());  
        f.mkdir();  
        return f;  
    }  
  
    public static void unpackZipFile(File zipFile, File dir) throws IOException {  
        InputStream fis = new FileInputStream(zipFile);  
        ZipInputStream zis = new ZipInputStream(fis);  
  
        try {  
            for (ZipEntry e = zis.getNextEntry(); e != null; e = zis.getNextEntry()) {  
                File f = new File(dir, e.getName());  
                if (e.isDirectory()) {  
                    if (!f.exists() && !f.mkdirs()) {  
                        throw new IOException("Cannot make directory " + f.getPath());  
                    }  
                } else {  
                    File d = f.getParentFile();  
                    if (d != null && !d.exists() && !d.mkdirs()) {  
                        throw new IOException("Cannot make directory " + d.getPath());  
                    }  
  
                    OutputStream out = new FileOutputStream(f);  
                    copy(zis, out);  
                    out.close();  
                }  
            }  
        } finally {  
            zis.close();  
            fis.close();  
        }  
    }  
  
    public static void makeJarFile(File jarFile, File dir) throws IOException {  
        makeJarFile(jarFile, dir, null);  
    }  
  
    /** 
     *  
     * @param jarFile 
     * @param dir 
     * @param excludedList java.io.File 
     * @throws IOException 
     */  
    public static void makeJarFile(File jarFile, File dir, Vector excludedList) throws IOException {  
        /* 
         * Create output jar file 
         */  
        OutputStream fos = new FileOutputStream(jarFile);  
        ZipOutputStream zos = new ZipOutputStream(fos);  
  
        /* 
         * Add entries to jar file 
         */  
        try {  
            addToJar(zos, dir, excludedList, new Vector());  
        } finally {  
            zos.close();  
            fos.close();  
        }  
    }  
  
    public static void addToJar(ZipOutputStream zos, File dir, Vector excludedList, Vector entriesAdded) throws IOException {  
        /* 
         * Expands list of files to process into full list of all files that can 
         * be found by recursively descending directories. 
         */  
        Vector v = new Vector();  
        expand(dir, dir.list(), excludedList, v);  
  
        /* 
         * Add entries to jar file 
         */  
        String path = dir.getPath();  
        if (!path.endsWith(File.separator)) {  
            path = path + File.separator;  
        }  
        path = path.replace(File.separatorChar, '/');  
        for (int i = 0; i < v.size(); i++) {  
            addFile(zos, path, (File) v.elementAt(i), entriesAdded);  
        }  
    }  
  
    private static void expand(File dir, String[] files, Vector excludeList, Vector v) {  
        if (files == null) {  
            return;  
        }  
        for (int i = 0; i < files.length; i++) {  
            File f = new File(dir, files[i]);  
            if (excludeList == null || !excludeList.contains(f)) {  
                if (f.isFile()) {  
                    v.addElement(f);  
                } else if (f.isDirectory()) {  
                    v.addElement(f);  
                    expand(f, f.list(), excludeList, v);  
                }  
            }  
        }  
    }  
  
    private static void addFile(ZipOutputStream zos, String path, File file, Vector entriesAdded) throws IOException {  
        String name = file.getPath();  
        boolean isDir = file.isDirectory();  
  
        if (isDir) {  
            if (!name.endsWith(File.separator)) {  
                name = name + File.separator;  
            }  
        }  
        name = name.replace(File.separatorChar, '/');  
        if (name.startsWith(path)) {  
            name = name.substring(path.length());  
        }  
        if (entriesAdded.contains(name)) {  
            return;  
        }  
  
        long size = isDir ? 0 : file.length();  
        ZipEntry e = new ZipEntry(name);  
        e.setTime(file.lastModified());  
        if (size == 0) {  
            e.setMethod(ZipEntry.STORED);  
            e.setSize(0);  
            e.setCrc(0);  
        }  
        zos.putNextEntry(e);  
        if (!isDir) {  
            InputStream in = new FileInputStream(file);  
            copy(in, zos);  
            in.close();  
        }  
        zos.closeEntry();  
        entriesAdded.addElement(name);  
    }  
  
    public static String getFileNameWithoutExt(String filePath) {  
        filePath = filePath.replace('\\', '/');  
        int pos = filePath.lastIndexOf('/');  
        String fileName = filePath.substring(pos + 1);  
        int pos2 = fileName.lastIndexOf('.');  
        if (pos2 == -1) {  
            return fileName;  
        } else {  
            return fileName.substring(0, pos2);  
        }  
    }  
  
    public static boolean isZipFile(File file) {  
        try {  
            ZipFile zip = new ZipFile(file);  
            zip.close();  
            return true;  
        } catch (IOException ex) {  
            return false;  
        }  
    }  
  
    public static void removeDir(File dir) {  
        if (dir.isDirectory()) {  
            removeFile(dir);  
        }  
        dir.delete();  
    }  
  
    public static void removeFile(File file) {  
        if (file.isFile()) {  
  
        } else if (file.isDirectory()) {  
            // Recursively remove all files in subdirectory.  
            String list[] = file.list();  
            if (list != null) {  
                for (int i = 0; i < list.length; i++) {  
                    File f = new File(file, list[i]);  
                    removeFile(f);  
                }  
            }  
        }  
        file.delete();  
    }  
  
    public static URL getFileURL(File file) {  
        try {  
            file = file.getCanonicalFile();  
        } catch (IOException e) {  
        }  
        String path = file.getAbsolutePath();  
        if (File.separatorChar != '/') {  
            path = path.replace(File.separatorChar, '/');  
        }  
        if (!path.startsWith("/")) {  
            path = "/" + path;  
        }  
        if (!path.endsWith("/") && file.isDirectory()) {  
            path = path + "/";  
        }  
        try {  
            return new URL("file", "", -1, path);  
        } catch (MalformedURLException e) {  
            // Should never happen since we specified the protocol  
            throw new InternalError();  
        }  
    }  
  
    /** 
     * 读取文件内容并将其转换为String类型 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileStringContent(File file) throws IOException {  
        return new String(getFileByteContent(file));  
    }  
  
    /** 
     * 读取文件内容并按指定字符编码类型来将其转换成String类型 
     *  
     * @param file 
     * @param charsetName 
     * @return 
     * @throws IOException 
     */  
    public static String getFileStringContent(File file, String charsetName) throws IOException {  
        return new String(getFileByteContent(file), charsetName);  
    }  
  
    /** 
     * 读取InputStream留的内容并将其转换为String类型 
     *  
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static String getInputStreamStringContent(InputStream inputStream) throws IOException {  
        return new String(getInputStreamAsByteArray(inputStream, -1));  
    }  
  
    /** 
     * Returns the contents of the given file as a byte array. 
     *  
     * @throws IOException if a problem occured reading the file. 
     */  
    public static byte[] getFileByteContent(File file) throws IOException {  
        InputStream stream = null;  
        try {  
            stream = new BufferedInputStream(new FileInputStream(file));  
            return getInputStreamAsByteArray(stream, (int) file.length());  
        } finally {  
            if (stream != null) {  
                try {  
                    stream.close();  
                } catch (IOException e) {  
                    // ignore  
                }  
            }  
        }  
    }  
  
    /** 
     * Returns the given input stream's contents as a byte array. If a length is specified (ie. if length != -1), only 
     * length bytes are returned. Otherwise all bytes in the stream are returned. Note this doesn't close the stream. 
     *  
     * @throws IOException if a problem occured reading the stream. 
     */  
    public static byte[] getInputStreamAsByteArray(InputStream stream, int length) throws IOException {  
        byte[] contents;  
        if (length == -1) {  
            contents = new byte[0];  
            int contentsLength = 0;  
            int amountRead = -1;  
            do {  
                int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE); // read at least 8K  
  
                // resize contents if needed  
                if (contentsLength + amountRequested > contents.length) {  
                    System.arraycopy(contents, 0, contents = new byte[contentsLength + amountRequested], 0, contentsLength);  
                }  
  
                // read as many bytes as possible  
                amountRead = stream.read(contents, contentsLength, amountRequested);  
  
                if (amountRead > 0) {  
                    // remember length of contents  
                    contentsLength += amountRead;  
                }  
            } while (amountRead != -1);  
  
            // resize contents if necessary  
            if (contentsLength < contents.length) {  
                System.arraycopy(contents, 0, contents = new byte[contentsLength], 0, contentsLength);  
            }  
        } else {  
            contents = new byte[length];  
            int len = 0;  
            int readSize = 0;  
            while ((readSize != -1) && (len != length)) {  
                // See PR 1FMS89U  
                // We record first the read size. In this case len is the actual read size.  
                len += readSize;  
                readSize = stream.read(contents, len, length - len);  
            }  
        }  
  
        return contents;  
    }  
  
}  