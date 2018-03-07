package main.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat.Encoding;


import com.cloud.util.EncodingDetect;

public class DataReader {
	public String readFromFile(String filename){
		String fileEncode=EncodingDetect.getJavaEncode(filename);
		String content="",s="";
		File file = new File(filename);
		if(!file.exists()) return null;	
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),fileEncode));  				
			while((s=br.readLine())!=null){
			content = content+s+"\n";			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return content;
	}
}
