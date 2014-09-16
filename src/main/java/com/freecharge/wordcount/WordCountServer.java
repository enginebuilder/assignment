package com.freecharge.wordcount;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.freecharge.wordcount.model.SystemConfig;
import com.freecharge.wordcount.model.WordCountHandler;

public class WordCountServer {

	private static final String responseJsonTemplate = "{\"count\": %s}";
	
    public static void main(String[] args) throws Exception {
    	
    	final SystemConfig systemConfig = SystemConfig.getSystemConfig();
    	
    	HttpServer server = HttpServer.createSimpleServer("/", systemConfig.getServerPort());
    	server.getServerConfiguration().addHttpHandler(
    	    new HttpHandler() {
    	        public void service(Request request, Response response) throws Exception {
    	            String query = request.getParameter("query");
    	            WordCountHandler countHandler = new WordCountHandler(systemConfig);
    	            int wordCount = countHandler.getWordCount(query);
    	            Integer wordCountResponse = new Integer(wordCount);
    	            String outputResponse = String.format(responseJsonTemplate, wordCountResponse);
    	            
    	            response.setContentType("application/json");
    	            response.setContentLength(outputResponse.getBytes().length);
    	            response.getWriter().write(outputResponse);
    	            
    	        }
    	    },
    	    "/");
    	try {
    	    server.start();
    	    System.out.println("Press any key to stop the server...");
    	    System.in.read();
    	} catch (Exception e) {
    	    System.err.println(e);
    	}
    }

    
}