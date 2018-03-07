package main.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataWriter {
	public boolean writeToFile(String filename,String content){
		File fileobj = new File(filename);
		if(!fileobj.exists())return false;		
			try {
				BufferedWriter writer = new BufferedWriter(  
				        new OutputStreamWriter(  
				                new FileOutputStream(fileobj),"UTF-8"));  
				writer.write(content);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}	
			return true;
	}
}
