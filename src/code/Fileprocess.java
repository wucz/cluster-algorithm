package code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import code.NlpirTest.CLibrary;

public class Fileprocess {
	static List<String> FileList = new ArrayList<String>(); // the list of file
	
	public static void main(String[] arg) throws IOException
	{
	//test	
		//File f =new File("E:\\20140928_nplir\\test");
	    //showDir(f); 		//递归遍历文件夹
		readDir("E:\\20140928_nplir\\test");
		System.out.println(FileList);
		for(String string:FileList){
			cutwords(readfile(string));
			
		}
	}
	
	public static String readfile(String path) throws IOException{
		File file=new File(path);
		String temp=null;
		if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
		try  {  
		
			BufferedReader bufReader=new BufferedReader(new FileReader(file));
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter("E:\\20140928_nplir\\test\\18届三中全会1.txt"));  
			
	
			StringBuffer sb=new StringBuffer();
        
			temp=bufReader.readLine();
			while(temp!=null){	//读文件
				sb.append(temp);
				temp=bufReader.readLine();
			}
			//System.out.println(sb.toString());
			//写文件
			//while(temp!=null){
			//bufWriter.write(res);
				//temp=bufReader.readLine();
			//}
			
			bufReader.close();
			bufWriter.close();
			return sb.toString();
		
		}
		catch(ArrayIndexOutOfBoundsException e)  
	    {  
	        System.out.println("没有指定文件");  
	        return null;
	    }  
	    catch(IOException e)  
	    {  
	        e.printStackTrace();  
	        return null;
	    } 
	}

	public static void readDir(String path)	throws IOException{
		try
        {
			File dir=new File(path);
			//System.out.println(dir);  
			File[] files =dir.listFiles();  
			for(File file:files){  
				if(file.isDirectory()){
					readDir(file.getPath());  
				}
				else{
					FileList.add(file.getAbsolutePath());
					//System.out.println(file);  
				}
			}
		}catch(FileNotFoundException e)
	    {
	        System.out.println(e.getMessage());
	    }
	}
	
	public static String StopwordHandler(String sentence){	//停用词
		//待做
		
		return sentence;
	}
	
	public static boolean IsStopword(String word){	//判断一些常用停用次
		String stopWordsList[] =
			{"的", "我们","要","自己","之","将","“","”","，","（","）","后","应","到","某","后",
				"个","是","位","新","一","两","在","中","或","有","更","好","",".","。","、","!"};//常用停用词
		
		for(String string : stopWordsList)
			if(string.equals(word))
				return true;
		return false;
	}
	
	public static ArrayList<String> cutwords(String file) throws IOException{	//传入的是文件内容
		
		String argu = "E:\\20140928_nplir";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return null;
		}
		
		ArrayList<String> words = new ArrayList<String>();
		String res=NlpirTest.CLibrary.Instance.
        		NLPIR_ParagraphProcess(file,0);
		//待做
		
		String[] temp=res.split(" ");
		for(String string:temp){
			//System.out.println(string);
			if(!IsStopword(string)){		//去除停用词
				words.add(string);
			}
		}
		//System.out.print(words);
		
		return words;
	}

}
