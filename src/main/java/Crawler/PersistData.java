package Crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Bean.TrajectoryDetail;

public class PersistData {
	private String baseurl;
	PersistData(String baseurl){
		this.baseurl= baseurl;
	}
	public void write(String UserId,ArrayList<TrajectoryDetail> list){
		File dir = new File(this.baseurl+"/"+UserId);
		if(!dir.exists()) dir.mkdir();
		for(TrajectoryDetail traject : list){
		String url = traject.getUrl().replaceAll("/","_");
		url = url.substring(7);
		File file = new File(this.baseurl+"/"+UserId+"/"+url+".txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//将数据写入到file中
		try {
			FileWriter fw = new FileWriter(file);
			fw.write("#info#\n");
			for(String info : traject.getInfo()){
				fw.write(info+"\n");
			}
			fw.write("#info#\n");
			fw.write("#traject#\n");
			fw.write(traject.getTrajectories());
			fw.write("#traject#");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}	
	}
	public void logurl(ArrayList<String> temp_urls){
		File file = new File(this.baseurl+"/logurl.txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			FileWriter fw = new FileWriter(file,true);
			for(String url : temp_urls) fw.write(url+"\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void writetest(String baseurl,String content){
		File file = new File(baseurl+"/test.txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		FileWriter fw = null;
		try {
		 fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fw!=null){
			try {
				fw.write(content);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}
	public  HashMap<String,Boolean> readurllog(){
		HashMap<String,Boolean> exiturl = new HashMap<String,Boolean>();
		File file = new File(this.baseurl+"/logurl.txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader (fr);
            String s;
             while ((s = br.readLine() )!=null) {
            	 exiturl.put(s, true);
              }
            fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	return exiturl;
	}
}
