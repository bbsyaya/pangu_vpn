package org.turing.vpn;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {

public static final String UTF8 = "utf-8";
public static final String GBK = "gbk";
public static final String GB2312 = "gb2312";
public static final String ISO88591 = "ISO-8859-1";
public static int READ_TIMEOUT = 30000;
public static int CONNECT_TIMEOUT = 30000;

/**
 * post请求数据
 * 
 * @param connectURL
 * @param param
 * @param charset
 * @return
 */
public static String doPost(String connectURL, String param, String charset) {
    byte[] bytes = null;
    ByteArrayOutputStream byteArrayOut = null;
    URL url = null;
    HttpURLConnection httpPost = null;
    OutputStream out = null;
    InputStream in = null;
    try {
        url = new URL(connectURL);
        httpPost = (HttpURLConnection) url.openConnection();
        httpPost.setRequestMethod("POST");
        httpPost.setDoInput(true);
        httpPost.setDoOutput(true);
        httpPost.setUseCaches(false);
        httpPost.setConnectTimeout(CONNECT_TIMEOUT);
        httpPost.setReadTimeout(READ_TIMEOUT);
        //httpPost.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        httpPost.connect();
        out = httpPost.getOutputStream();
        out.write(param.getBytes(charset));
        out.flush();
        in = httpPost.getInputStream();
        byteArrayOut = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int l = 0;
        while ((l = in.read(buf)) != -1) {
            byteArrayOut.write(buf, 0, l);
        }
        bytes = byteArrayOut.toByteArray();
        return new String(bytes, charset);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        close(byteArrayOut);
        close(out);
        close(in);
        close(httpPost);
    }
    return null;
}

public static String doPostSSL(String connectURL, Map<String, String> params, String charset) throws MalformedURLException, IOException, UnsupportedEncodingException {
    //_ignoreSSL();
    return doPost(connectURL, params, charset);
}

/**
 * post请求数据
 * 
 * @param connectURL
 * @param param
 * @param charset
 * @return
 */
public static String doPost(String connectURL, Map<String, String> params, String charset) {
    String param = "";
    if (params != null && !params.isEmpty()) {
        StringBuffer paramBuf = new StringBuffer();
        for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            String value = params.get(key);
            paramBuf.append("&").append(key).append("=").append(value);
        }
        param = paramBuf.substring(1);
    }
    System.out.println("post url:" + connectURL);
    System.out.println("post data:" + param);
    byte[] bytes = null;
    ByteArrayOutputStream byteArrayOut = null;
    URL url = null;
    HttpURLConnection httpPost = null;
    OutputStream out = null;
    BufferedInputStream in = null;
    try {
        url = new URL(connectURL);
        httpPost = (HttpURLConnection) url.openConnection();
        httpPost.setRequestMethod("POST");
        httpPost.setDoInput(true);
        httpPost.setDoOutput(true);
        httpPost.setUseCaches(false);
        httpPost.setConnectTimeout(CONNECT_TIMEOUT);
        httpPost.setReadTimeout(READ_TIMEOUT);
        //httpPost.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        httpPost.connect();
        out = httpPost.getOutputStream();
        out.write(param.getBytes(charset));
        out.flush();

        try {
            in = new BufferedInputStream(httpPost.getInputStream());
        } catch (IOException e) {
            in = new BufferedInputStream(httpPost.getErrorStream());
        }

        byteArrayOut = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int l = 0;
        while ((l = in.read(buf)) != -1) {
            byteArrayOut.write(buf, 0, l);
        }
        bytes = byteArrayOut.toByteArray();
        return new String(bytes, charset);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        close(byteArrayOut);
        close(in);
        close(out);
        close(httpPost);
    }
    return null;
}

/**
 * Get请求数据
 * 
 * @param connectURL
 * @param param
 * @param charset
 * @return
 */
public static String doGet(String connectURL, String charset) {
    byte[] bytes = null;
    ByteArrayOutputStream byteArrayOut = null;
    URL url = null;
    HttpURLConnection httpGet = null;
    InputStream in = null;
    try {
        url = new URL(connectURL);
        httpGet = (HttpURLConnection) url.openConnection();
        httpGet.setConnectTimeout(CONNECT_TIMEOUT);
        httpGet.setReadTimeout(READ_TIMEOUT);
        httpGet.connect();
        in = httpGet.getInputStream();
        byteArrayOut = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int l = 0;
        while ((l = in.read(buf)) != -1) {
            byteArrayOut.write(buf, 0, l);
        }
        bytes = byteArrayOut.toByteArray();
        return bytes != null ? new String(bytes, charset) : null;
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        close(byteArrayOut);
        close(in);
        close(httpGet);
    }
    return null;
}

private static void close(Closeable stream) {
    if (stream != null) {
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private static void close(HttpURLConnection httpConn){
    if(httpConn != null){
        httpConn.disconnect();
    }
}
}