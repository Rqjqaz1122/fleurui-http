package top.wrqj.client;

import top.wrqj.model.HttpConfig;
import top.wrqj.model.Request;
import top.wrqj.model.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeHttpClientAdapter implements HttpClient {

    private HttpConfig httpConfig;

    @Override
    public Response execute(Request request) throws IOException {
        URL url = new URL(request.getUrl());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try{
            connection.setRequestMethod(request.getMethod());
            if(request.getHeaders() != null) {
                request.getHeaders().forEach(connection::setRequestProperty);
            }
            if(request.getBody() != null && request.getBody().length > 0) {
                connection.setDoOutput(true);
                try(OutputStream os = connection.getOutputStream()){
                    os.write(request.getBody());
                }
            }
            connection.setConnectTimeout(httpConfig.getConnectionTimeout());
            connection.setReadTimeout(httpConfig.getReadTimeout());
            int responseCode = connection.getResponseCode();
            Map<String,String> headers = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                headers.put(entry.getKey(),String.join(", ",entry.getValue()));
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try(InputStream inputStream = responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }
            }
            return new Response(responseCode,headers,outputStream.toByteArray());
        }finally {
            connection.disconnect();
        }
    }

    @Override
    public void configure(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }
}
