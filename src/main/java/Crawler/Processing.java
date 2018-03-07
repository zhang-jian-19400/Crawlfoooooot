package Crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import main.data.DataReader;
import main.data.DataWriter;
/**
 * 
 * @author ZhangJian
 *删除空的文件夹
 */
public class Processing {
	public void deleteEmpty(String path){
		File file = new File(path);
		File[]lists = file.listFiles();
		boolean flag=true;
		for(File subfile:lists){
			if(!subfile.isDirectory()) continue;
			flag=true;
			if(subfile.listFiles().length<1) flag=false;
			if(flag==false) subfile.delete();
		}		
	}
	/**
	 * 遍历文件夹操作，然后再进行转码操作
	 * @param dir 指需要转码的文件夹
	 */
	public void visitAll(String dir)
	{
		File file = new File(dir);
		File[]lists = file.listFiles();
		boolean flag=true;
		for(File subfile:lists){
			if(!subfile.isDirectory()) 
			{
				traslateCode(subfile.getAbsolutePath());
			}
			else
			{
				visitAll(subfile.getAbsolutePath());
			}
		}
	}
	
	public void traslateCode(String path)
	{
		String filename=path;
		DataReader dr = new DataReader();
		String content = dr.readFromFile(filename);
		DataWriter dw = new DataWriter();
		dw.writeToFile(filename, content);
	}
}
