package Crawler;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

public class Analysishtm {
	/**
	 * 分析是否是404 或者 空地址 或者 有内容
	 * @param content
	 * @return 该用户的行程记录列表
	 */
	
	public ArrayList<String> PhraseFirst(Document doc,String baseUri,String subbase){
		ArrayList<String> list = new ArrayList<String>();
		Elements next =doc.getElementsByClass("next");
		if(next.size()>0) {	
			String crawlurl =subbase.substring(0, subbase.length()-1)+next.get(0).attr("href");
			Document webContent=null;
			boolean success = false;
			//对于无法访问的请求最多10次
			for(int i=0;i<20;i++){
				if(success) break;
			try {
				Thread.sleep(500);
				webContent =  CrawlerThread.getContentByJsoup(crawlurl);
				success = true;
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			}
			}
			if(success==true){
			System.out.println("正在爬取"+crawlurl);
			list.addAll(PhraseFirst(webContent,baseUri,subbase));
			}
		}
		Elements routes = doc.getElementsByClass("m_t_dl_1");
		if(routes.size()!=0)
		for(Element e : routes){  //由于404或是空内容的元素都为0.所以这里都排除了
			//这个元素里面还有许多元素。我们需要获取href中包含“trip”的
			Elements links = e.getElementsByTag("a");
			for(Element link:links){
				String value = link.attr("href");
				if (value.contains("/trip/")){
					list.add(baseUri+value);
				}
			}
		}
		return list;
	}
//返回每条行程的详细信息
	public ArrayList<String> PhraseSecond(Document doc,String baseUri){
	ArrayList<String> TrajectoryDetail = new ArrayList<String>();
	Elements InfoArea = doc.getElementsByClass("trip_box_right");
	Elements items = null;
	if (InfoArea.size()==1)  items = InfoArea.get(0).getElementsByTag("dd");
	else {System.out.println("详情信息展示区域不止一块哦!"); return TrajectoryDetail;}
	if(items.size()!=0){
		for (int index=0;index<items.size();index++){
			Element item = items.get(index);
			switch(index){
			//先得到用户 名称
			//再得到评分
			case 0:{
				Elements name = item.getElementsByTag("a");
				String nameValue = name.get(0).attr("title");
				Elements rate = item.getElementsByClass("ratingReal");
				String rateValue = rate.get(0).attr("rating");
				TrajectoryDetail.add(rateValue);
			}break;
			//得到时间
			case 1:{
				String time = item.html().replaceAll("&nbsp;", "");
				TrajectoryDetail.add(time);
			}break;
			//得到行走方式 地点
			case 2:{
				String content = item.html();
				content = content.replaceAll("<+[^>]*>+","").replaceAll("&nbsp;", "");
				TrajectoryDetail.add(content);
			}break;
			//得到难易程度
			case 3:{
				String content = item.html();
				content = content.replaceAll("<+[^>]*>+","").replaceAll("&nbsp;", "");
				TrajectoryDetail.add(content);
			}break;
			//得到上下海拔
			case 4:{
				String content = item.html();
				content = content.replaceAll("<+[^>]*>+","").replaceAll("&nbsp;", "");
				TrajectoryDetail.add(content);
			}break;
			//得到最高速度
			case 5:{
				String content = item.html();
				content = content.replaceAll("<+[^>]*>+","").replaceAll("&nbsp;", "");
				TrajectoryDetail.add(content);
			}break;
			}
		}
	}
	//得到评论总结
	Elements describtions = doc.getElementsByClass("trip_box_description");
	String describtion = describtions.get(0).html();
	describtion = describtion.replaceAll("&nbsp;","").replaceAll("<+[^>]*>+","");
	TrajectoryDetail.add(describtion);
	return TrajectoryDetail;
	}
	
}
