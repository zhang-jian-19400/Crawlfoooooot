package main.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import com.cloud.util.EncodingDetect;

public class deleteRepeate {
	public static void main(String args[]){
		String filename="D:/学习资料/数据集/foooooot旅游数据/foooooot/logurl.txt";
		String filenamenew="D:/学习资料/数据集/foooooot旅游数据/foooooot/logurlnew.txt";
		HashSet<String> set = new HashSet<String>();
		String fileEncode=EncodingDetect.getJavaEncode(filename);
		String content="",s="";
		File file = new File(filename);
		if(!file.exists()) return ;	
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),fileEncode));  				
			while((s=br.readLine())!=null){
				set.add(s);			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String str:set){
			content+=str+"\n";
		}
		DataWriter dw = new DataWriter();
		dw.writeToFile(filenamenew, content);
		return ;
	}
}
