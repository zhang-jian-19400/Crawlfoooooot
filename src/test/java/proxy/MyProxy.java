package proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MyProxy {
	/**
	 * @param args
	 */
	public static void main(String args[]){
		System.setProperty("http.proxyHost", "218.56.132.157");
		System.setProperty("http.proxyPort", "8080");
		URL url;
		try {
			
			url = new URL("http://www.foooooot.com");
			InputStream in = url.openStream();
			BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
			String line; 
			String result="";			
            while ((line = Bin.readLine())!=null) {
                result += line; 
            }
            System.out.println(new String(result.getBytes(),"utf-8"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
