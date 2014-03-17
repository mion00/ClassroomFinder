/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Network;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mion00
 */
public class MultiThreadHTTP {
    private ExecutorService service;
    private CompletionService<Void> excompl;
    
    public MultiThreadHTTP() {
        service = Executors.newCachedThreadPool();
        excompl = new ExecutorCompletionService<>(service);
        
}
    static class GetCallable implements Callable<Void> {
    String url;
    List<String> resp_list;
    
    @Override
    public Void call() throws IOException {
        URL obj = new URL(url);
        
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");


	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.flush();
	wr.close();
 
 
	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
        in.close();
        con.disconnect();
        resp_list.add(response.toString());
        return null;
    }

    public GetCallable(String url, List<String> resp_list) {
        this.url = url;
        this.resp_list = resp_list;
    }
    
    
}
    static class PostCallable implements Callable<Void> {
        String parameters;
        List<String> resp_list;
        String url;

        @Override
        public Void call() throws IOException {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            resp_list.add(response.toString());
            return null;
        }
        public PostCallable(String parameters, String url, List<String> resp_list) {
            this.url = url;
            this.parameters = parameters;
            this.resp_list = resp_list;
        }
    }
    
    public List<String> DownloadFromUrls (Collection<String> urls) {
  
        List<String> resp_list = new ArrayList<>(urls.size());
        List<String> sync_list = Collections.synchronizedList(resp_list);
        //////////////////////////////////////////////////////////////
        for (String s : urls) {
            GetCallable task= new GetCallable(s, sync_list);
            excompl.submit(task);
        }
        
        int nr_tasks = urls.size();
        
        for (int i=0; i < nr_tasks; i++) {
            try {
                excompl.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiThreadHTTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        service.shutdown(); 
        
        return sync_list;
    }
    
    public List<String> PostfFromParameters (String url, Collection<String> parameters) {
        List<String> resp_list = new ArrayList<>(parameters.size());
        List<String> sync_list = Collections.synchronizedList(resp_list);
        
        for (String s : parameters) {
            PostCallable task= new PostCallable(url, s, sync_list);
            excompl.submit(task);
        }
        
        int nr_tasks = parameters.size();
        
        for (int i=0; i < nr_tasks; i++) {
            try {
                excompl.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiThreadHTTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        service.shutdown(); 
        
        return sync_list;
    }
}
