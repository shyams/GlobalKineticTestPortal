package WebService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;


import org.apache.http.impl.client.DefaultHttpClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shyam
 */
public class TestWebservice extends HttpServlet {
//    protected String endpoint = "http://localhost:8080/GlobalKineticTestService/webresources/users";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try {
            String endpoint = "http://" + request.getServerName() + ":" + request.getServerPort() + "/GlobalKineticTestService/webresources/users";
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            response.getWriter().print(sb.toString());
            br.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String output = makeRequest(request, "POST");
            System.out.print(output + "helloo");
            response.getWriter().print(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String res = makeRequest(request, "PUT");
            response.getWriter().print(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected HttpResponse makePutRequest(HttpServletRequest request) {
        String endpoint = "http://" + request.getServerName() + ":" + request.getServerPort() + "/GlobalKineticTestService/webresources/users";
        HttpResponse httpRes = null;
        try {
            StringBuilder data = new StringBuilder();
            String str = null;
            while ((str = request.getReader().readLine()) != null) {
                data.append(str);
            }

            HttpClient httpClient = new DefaultHttpClient();
            HttpPut putRequest = new HttpPut(endpoint);
            System.out.println("String " + data.toString());
            StringEntity input = new StringEntity(data.toString());
            input.setContentType("application/json");
            putRequest.setEntity(input);

            httpRes = httpClient.execute(putRequest);
            System.err.println(httpRes.getStatusLine() + "done");
            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpRes != null) {
            return httpRes;
        } else {
            return null;
        }

    }

    protected String makeRequest(HttpServletRequest request, String requestType) {
        StringBuilder outputdata = null;

        try {
            String endpoint = "http://" + request.getServerName() + ":" + request.getServerPort() + "/GlobalKineticTestService/webresources/users";
            System.out.println(endpoint);
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("Content-Type", "application/json");
            StringBuilder data = new StringBuilder();
            String str = null;
            while ((str = request.getReader().readLine()) != null) {
                data.append(str);
            }

            String input = data.toString();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            outputdata = new StringBuilder();
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                outputdata.append(output);
            }

            conn.disconnect();

        } catch (Exception e) {

            e.printStackTrace();

        }
        if (outputdata != null) {
            return outputdata.toString();
        } else {
            return "";
        }

    }
}
