package com.framgia.e_learningsimple.model;

import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.util.ValueName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class RequestHelper {
    public static ResponseHelper executeRequest(String link, Method method, ValueName... args) throws IOException {
        ArrayList<ValueName> nameValuePairs = new ArrayList<ValueName>(Arrays.asList(args));
        return executeRequest(link, method, nameValuePairs);
    }

    public static ResponseHelper executeRequest(String link, Method method, ArrayList<ValueName> args) throws IOException {
        ResponseHelper response;
        int responseStatusCode;
        HttpURLConnection connection = null;
        if (method != Method.GET) {
            URL url = new URL(link);
            String charset = "UTF-8";
            String data = String.format("%s=%s",
                    URLEncoder.encode(args.get(0).getName(), charset),
                    URLEncoder.encode(args.get(0).getValue(), charset));
            for (ValueName valueName : args) {
                data += String.format("&%s=%s",
                        URLEncoder.encode(valueName.getName(), charset),
                        URLEncoder.encode(valueName.getValue(), charset));
            }
            connection = (HttpURLConnection) url.openConnection();
            if (method == Method.PATCH) {
                connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            }
            connection.setDoOutput(true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(data);
            streamWriter.flush();
            streamWriter.close();
        } else {
            link += String.format("?%s=%s", args.get(0).getName(), args.get(0).getValue());
            for (ValueName valueName : args) {
                link += String.format("&%s=%s", valueName.getName(), valueName.getValue());
            }
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
        }

        responseStatusCode = connection.getResponseCode();
        InputStream inputStream = (responseStatusCode >= ErrorNetwork.OK
                && responseStatusCode < ErrorNetwork.BAD_REQUEST) ?
                connection.getInputStream() : connection.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            break;
        }
        response = new ResponseHelper(connection.getResponseCode(), stringBuilder.toString());
        return response;
    }

    public enum Method {POST, GET, PATCH}
}
