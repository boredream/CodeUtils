package utils;

import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CHARSET = "UTF-8";

    /**
     * Supported request methods.
     */
    public interface Method {
        int GET = 0;
        int POST = 1;
    }

    public static String getString(String url) throws Exception {
        return getOrPostString(Method.GET, url, null, null);
    }

    public static String getString(String url, Map<String, String> headers) throws Exception {
        return getOrPostString(Method.GET, url, null, headers);
    }

    public static String postJson(String url, String json, Map<String, String> headers) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        String response = null;
        try {
            StringEntity s = new StringEntity(json);
            s.setContentEncoding(CHARSET);
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                response = EntityUtils.toString(res.getEntity());// 返回json格式：
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static String putJson(String url, String json, Map<String, String> headers)
            throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(url);
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                put.setHeader(entry.getKey(), entry.getValue());
            }
        }
        String response = null;
        try {
            StringEntity s = new StringEntity(json);
            s.setContentEncoding(CHARSET);
            s.setContentType("application/json");//发送json数据需要设置contentType
            put.setEntity(s);
            HttpResponse res = client.execute(put);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                response = EntityUtils.toString(res.getEntity());// 返回json格式：
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static String postString(String url, Map<String, String> postParams)
            throws Exception {
        return getOrPostString(Method.POST, url, postParams, null);
    }

    public static String postString(String url, Map<String, String> postParams, Map<String, String> headers)
            throws Exception {
        return getOrPostString(Method.POST, url, postParams, headers);
    }

    private static String postFile(String url, File file, Map<String, String> headers) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        if (headers != null) {
            map.putAll(headers);
        }
        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl);

        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty(HEADER_CONTENT_TYPE,
                getBodyContentType());
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        int len;
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(file);
        while ((len = fis.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();

        // Initialize HttpResponse with data from the HttpURLConnection.
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could
            // not be retrieved.
            // Signal to the caller that something was wrong with the
            // connection.
            throw new IOException(
                    "Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }

        Header contentTypeHeader = response.getHeaders(HTTP.CONTENT_TYPE)[0];
        String responseCharset = parseCharset(contentTypeHeader);

        byte[] bytes = entityToBytes(response.getEntity());
        String responseContent = new String(bytes, responseCharset);
        return responseContent;
    }

    private static String getOrPostString(int method, String url,
                                          Map<String, String> postParams, Map<String, String> headers) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        if (headers != null) {
            map.putAll(headers);
        }
        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl);

        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }

        setConnectionParametersForRequest(connection, method, postParams);
        // Initialize HttpResponse with data from the HttpURLConnection.
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could
            // not be retrieved.
            // Signal to the caller that something was wrong with the
            // connection.
            throw new IOException(
                    "Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields()
                .entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue()
                        .get(0));
                response.addHeader(h);

            }
        }

        Header contentTypeHeader = response.getHeaders(HTTP.CONTENT_TYPE)[0];
        String responseCharset = parseCharset(contentTypeHeader);

        byte[] bytes = entityToBytes(response.getEntity());
        String responseContent = new String(bytes, responseCharset);
        return responseContent;
    }

    public static String downloadFile() {
        return null;
    }

    public static byte[] getOrPostFile(int method, String url,
                                       Map<String, String> postParams, Map<String, String> headers) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        if (headers != null) {
            map.putAll(headers);
        }
        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl);

        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }

        setConnectionParametersForRequest(connection, method, postParams);
        // Initialize HttpResponse with data from the HttpURLConnection.
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could
            // not be retrieved.
            // Signal to the caller that something was wrong with the
            // connection.
            throw new IOException(
                    "Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields()
                .entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue()
                        .get(0));
                response.addHeader(h);

            }
        }

        Header contentTypeHeader = response.getHeaders(HTTP.CONTENT_TYPE)[0];
        String responseCharset = parseCharset(contentTypeHeader);

        byte[] bytes = entityToBytes(response.getEntity());
        return bytes;
    }

    /**
     * Returns the charset specified in the Content-Type of this header, or the
     * HTTP default (utf-8) if none can be found.
     */
    private static String parseCharset(Header contentTypeHeader) {
        String contentType = contentTypeHeader.getValue();
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }

        return "utf-8";
    }

    private static byte[] entityToBytes(HttpEntity entity) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            InputStream in = entity.getContent();
            int count;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
        } finally {
            bytes.close();
        }
        return bytes.toByteArray();
    }

    /**
     * Initializes an {@link HttpEntity} from the given
     * {@link HttpURLConnection}.
     *
     * @param connection
     * @return an HttpEntity populated with data from <code>connection</code>.
     */
    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    /**
     * Opens an {@link HttpURLConnection} with parameters.
     *
     * @param url
     * @return an open connection
     * @throws IOException
     */
    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(60 * 1000);
        connection.setReadTimeout(60 * 1000);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        // // use caller-provided custom SslSocketFactory, if any, for HTTPS
        // if ("https".equals(url.getProtocol()) && mSslSocketFactory != null) {
        // ((HttpsURLConnection)connection).setSSLSocketFactory(mSslSocketFactory);
        // }

        return connection;
    }

    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded
     * encoded string.
     */
    private static byte[] encodeParameters(Map<String, String> params,
                                           String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(),
                        paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(),
                        paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: "
                    + paramsEncoding, uee);
        }
    }

    /**
     * Returns the raw POST body to be sent.
     */
    private static byte[] getPostBody(Map<String, String> postParams) {
        if (postParams != null && postParams.size() > 0) {
            return encodeParameters(postParams, CHARSET);
        }
        return null;
    }

    private static String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + CHARSET;
    }

    private static void setConnectionParametersForRequest(
            HttpURLConnection connection, int method,
            Map<String, String> postParams) throws IOException {
        switch (method) {
            case Method.GET:
                connection.setRequestMethod("GET");
                break;
            case Method.POST:
                byte[] postBody = getPostBody(postParams);
                if (postBody != null) {
                    // Prepare output. There is no need to set Content-Length
                    // explicitly,
                    // since this is handled by HttpURLConnection using the size of
                    // the prepared
                    // output stream.
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE,
                            getBodyContentType());
                    DataOutputStream out = new DataOutputStream(
                            connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
                break;
        }
    }
}
