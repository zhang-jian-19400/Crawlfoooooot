package Crawler;

import java.io.IOException;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.xml.sax.InputSource;

import Bean.TrajectoryDetail;

public class CrawlerThread implements Runnable{
	public static int ThreadNum = 1;
	private String base= "D:/学习资料/数据集/foooooot旅游数据/foooooot"; //最终文件放到在的跟目录
//	private static String base= "/home/kpnm/dataset";
//	private static String base = "F:/foooooot";
	private int UserId;
	private String url = "http://www.foooooot.com/userindex/??/trip/";
	private String baseurl = "http://www.foooooot.com";
	private static HashMap<String,Boolean> exiturl;
	CrawlerThread(int UserId,HashMap<String,Boolean> exiturl){
		this.UserId = UserId;
		this.exiturl = exiturl; 
		url = url.replace("??",new Integer(this.UserId).toString());
		synchronized(this){this.ThreadNum--;}
		System.out.println("当前的线程数量是："+this.ThreadNum);
	}
	/*
	 * 按URL页面的照列表将所有的页面爬取完毕后，才能向进度文件中写UserID。
	 * 获取页面需要爬取的记录条数，用以判断完成的指标
	 * 在向进度表中写UserID时需要加锁
	 * 最后将完成的信息写回到文件中
	 */
	public void run() {
	// 初始化一些变量数据
		ArrayList<TrajectoryDetail> UserTrajectories = new ArrayList<TrajectoryDetail>();
		ArrayList<String> temp_urls = new ArrayList<String>();
		PersistData persistdata  = new PersistData(base);
		
		
		Analysishtm analysis = new Analysishtm();
//		String url = "http://www.foooooot.com/userindex/4/trip/";
		Document webContent=null;
		boolean success = false;
		
		
		//对于无法访问的请求最多10次
		for(int i=0;i<10;i++){
			if(success) break;
		try {
	//		Thread.sleep(500);
			webContent =  getContentByJsoup(url);
			success = true;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		}
		System.out.println("正在爬取"+url);
		if(webContent==null) {synchronized(this){this.ThreadNum++;}return ;}//如果没有值，则返回空记录页面。
	//如果页面不空，则开始分析具体的页面   注意列表中存储的数据是http://www.foooooot.com/trip/272737/，我们还得加上offsettrackjson 获取轨迹数据
		ArrayList<String> urllist = analysis.PhraseFirst(webContent,baseurl,url);
		for(String urlsecond : urllist){
			//查下表，如果有爬取过，则忽略这条轨迹	
			if(this.exiturl.get(urlsecond)!=null) continue;
			success = false;
			TrajectoryDetail detail = new TrajectoryDetail();
			//对于无法访问的请求最多10次
			for(int i=0;i<10;i++){
				if(success) break;
			try {
//				Thread.sleep(500);
				webContent =  getContentByJsoup(urlsecond);
				success = true;
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			}
			}
			System.out.println("爬取第二个页面");
			//如果doc为空，即超时没有请求到数据。这次爬取结果作废
			if(webContent==null) continue ;//如果没有值，则返回空记录页面。
			System.out.println("得到响应"+urlsecond);
			//关于某次活动的详情
			ArrayList<String> title_deatails = analysis.PhraseSecond(webContent,baseurl);
			String trajectory ="";
			//获取不同的轨迹
			success = false;
			//对于无法访问的请求最多10次
			for(int i=0;i<10;i++){
				if(success) break;
			try {
				Thread.sleep(500);
//				trajectory = getContentByhttp(urlsecond+"offsettrackjson");
				trajectory = getContentByJsoup(urlsecond+"offsettrackjson").body().toString();
				success = true;
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			}	
			if(trajectory.equals("")) continue;
			System.out.println("得到响应"+urlsecond+"offsettrackjson");
			//把已近记录的地址写到磁盘上		
			temp_urls.add(urlsecond);		
			detail.setInfo(title_deatails);
			detail.setTrajectories(trajectory);
			detail.setUrl(urlsecond);
			UserTrajectories.add(detail);
		}	
	//写到磁盘上		
		persistdata.write(new Integer(this.UserId).toString(),UserTrajectories);
		persistdata.logurl(temp_urls);
		synchronized(this){this.ThreadNum++;} //完成爬虫工作
		System.out.println("爬取完毕："+url);
		}
		}		
   /**
    * 获取页面信息
    * @param url
    * @return Webcontent
 * @throws IOException 
    */
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
	
//通过jsoup获取
	public static Document getContentByJsoup(String url) throws Exception{
		URL urlpath;
		Document doc =null;
			urlpath = new URL(url);
			doc = Jsoup.parse(urlpath,30*1000);
		return doc;
	}

}
