package Crawler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class Startworking {
//	private static String base= "D:/学习资料/数据集/foooooots/foooooot";
	private static String base = "D:/学习资料/数据集/foooooot旅游数据/foooooot";
//	private static String base= "/home/kpnm/dataset";
	public static void main(String args[]){
		int CrawlerNum = 100000,UserNum=Integer.parseInt(args[0]); //Integer.parseInt(args[0])表示需要爬取的用户数量，其实可以根据网页返回值进行判断18254
		PersistData persist = new PersistData(base);
		HashMap<String,Boolean> exiturl = persist.readurllog();
		HashMap<String,String> map=null;
		String ip="",port="";
		int i=6,j=0,pointnum=0; //j用于
		try {
//			map=readPropertyTest("D:\\javaweb_project\\Crawlfooooot\\src\\main\\java\\Crawler\\ipproxy.progerties");
			map=readPropertyTest(base+"\\ipproxy.progerties");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int ip_num = map.keySet().size();
		while(CrawlerNum>=UserNum){	
			if(i>5){ //i 用于 发送5个爬虫工作后，然后换个IP,j表示是哪个IP地址
			for(String key:map.keySet()){
				port = map.get(key);
				ip=key;
				if(j++==pointnum) break;
			}
			System.getProperties().setProperty("http.proxyHost", ip);
			System.getProperties().setProperty("http.proxy"+ "Port", port);	
			if(ip.equals("localhost")) 
			{
				System.getProperties().remove("http.proxyHost");  
		        System.getProperties().remove("http.proxyPort");
			}
			System.out.println(ip+":"+port);
			i=0;
			}
			i++;
			if(j!=0){
			j=0;
			if(pointnum<ip_num)pointnum++;
			else pointnum = 0;
			}
//			while(CrawlerThread.ThreadNum>0)
			{
			System.out.println("当前正在爬取第"+UserNum+"用户");
			CrawlerThread crawler = new CrawlerThread(UserNum,exiturl); //一旦创建这个线程便消除一个线程数量。线程消亡后数量又增加了1.
			crawler.run();
//			new Thread(crawler).start();
			System.out.println("已经爬取完"+UserNum+"用户");
			UserNum++;		
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
}

