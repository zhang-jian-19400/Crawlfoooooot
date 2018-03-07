package Crawler;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Bean.TrajectoryDetail;
import main.data.DataReader;
import main.data.DataWriter;

public class repair {
	/*
	 * @author zhangjian
	 * @function  由于在爬取过程中，出现了爬取对象的内容为网页源码的问题。我们于是采用重爬机制
	 * 分析全量的文件，进行爬取。
	 * @param filepath 代表要遍历的根目录。
	 */
	private static int agentfrequence=0;
	private static int agentID=0;
	public void visitAllFile(String filepath) throws IOException{		
		File file = new File(filepath);
		if(!file.exists()) return;
		File[]filelist = file.listFiles();
		for(File subfile : filelist){
			if(subfile.isDirectory()) visitAllFile(subfile.getAbsolutePath());
			else
			{
				if(agentfrequence>15){
					agentfrequence=0;
					agentID++;
					changeAgent();
				}
				dealInvalidFile(subfile.getAbsolutePath(),"function GCK(c){i");	
			}
		}
		return ;
	}
	/*
	 * @param filepath 为要检测文件，InvalidFlag 为判断为无效的标志。
	 */
	public boolean dealInvalidFile(String filepath,String InvalidFlag)
	{
		DataReader dr = new DataReader();
		String content = dr.readFromFile(filepath);
		String contentsegment[] = filepath.split("\\\\");
		String website = contentsegment[contentsegment.length-1];
		String trajects="";
		if(content.contains(InvalidFlag)) 
		{
			trajects=reclawer(website);
			content = trajects;
			DataWriter dw = new DataWriter();
			dw.writeToFile(filepath, content);
			System.out.println("repair "+filepath+" finished!");
			agentfrequence++;
		}
		return true;
		
	}
	/*
	 * @param filepath具有网址信息，例如“www.foooooot.com_trip_749124_.txt”
	 */
	public String  reclawer(String filepath){
		Analysishtm analysis = new Analysishtm();
		boolean success = false;
		Document info=null;
		String trajectory="";
		filepath=filepath.replaceAll("_","/");
		String website  = filepath.substring(0, filepath.length()-4);
		//爬取info部分的对象		
		success = false;
		for(int i=0;i<10;i++){
			if(success) break;
		try {
//			Thread.sleep(500);
			info =  getContentByJsoup("http://"+website);
			success = true;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		}

		//关于某次活动的详情
		ArrayList<String> title_deatails = analysis.PhraseSecond(info,"");
		String infos="#info#\n";
		for(String detail:title_deatails){
			infos+=detail+"\n";
		}
		infos+="#info#\n";
		//爬取trajectory部分数据
		website = website+"offsettrackjson";
		 success = false;
		//对于无法访问的请求最多10次
		for(int i=0;i<10;i++){
			if(success) break;
		try {
			Thread.sleep(500);
			trajectory = "#traject#\n"+getContentByJsoup("http://"+website).body().toString()+"#traject#\n";
			success = true;
			if(trajectory.contains("function GCK(c){i")) success=false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
	}
		return infos+trajectory;
		}
	
	//通过jsoup获取
		public static Document getContentByJsoup(String url) throws Exception{
			URL urlpath;
			Document doc =null;
				urlpath = new URL(url);
				doc = Jsoup.parse(urlpath,30*1000);
				
			return doc;
		}
		public static String getContentByhttp(String url) throws IOException{
			String responseBody = "";
			//获取网页信息
					final String address = url;
					HttpGet httpget = new HttpGet(url);
			        System.out.println("executing request " + httpget.getURI());
			        //对拿到的response对象进行修改
			        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			                int status = response.getStatusLine().getStatusCode();
			                if (status >= 200 && status < 300) {
			                    HttpEntity entity = response.getEntity();
			                    return entity != null ? EntityUtils.toString(entity) : null;
			                } else {
			                    throw new ClientProtocolException("在爬取"+address+"时，Unexpected response status: " + status);
			                }
			            }
			        };
			        	CloseableHttpClient httpclient = HttpClients.createDefault();
			        	responseBody = httpclient.execute(httpget, responseHandler);
			        	httpclient.close();
					
			return responseBody;
		}
		public static void changeAgent() throws IOException{
			
			String base = "D:";
			HashMap<String,String> map=null;
			String ip="",port="";
			map=readPropertyTest(base+"\\ipproxy.progerties");
			int j=0;
			if(agentID==map.size())
			{
				System.getProperties().remove("http.proxyHost");  
		        System.getProperties().remove("http.proxyPort");
		        agentID=-1;
		        return ;
			}
			for(String key:map.keySet()){				
				port = map.get(key);
				ip=key;
				if(j++==agentID) break;
			}
			System.out.println("agent address:"+ip+":"+port);
			System.getProperties().setProperty("http.proxyHost", ip);
			System.getProperties().setProperty("http.proxy"+ "Port", port);	
		}
		public static HashMap<String,String> readPropertyTest(String properties) throws IOException{
			Properties prop = new Properties();
			HashMap<String,String> map = new HashMap<String,String>();
			InputStream in = new BufferedInputStream (new FileInputStream(properties));
			 prop.load(in);
			             Iterator<String> it=prop.stringPropertyNames().iterator();
			             while(it.hasNext()){
			                  String key=it.next();
			                  map.put(key, prop.getProperty(key));
			              }
			 in.close();
			 return map;
		}
		public static void main(String args[]) throws IOException{
			repair rp = new repair();
			rp.visitAllFile("D:/学习资料/数据集/foooooot旅游数据/foooooot");
		}
}
