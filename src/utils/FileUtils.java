package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FileUtils {
	private static FileWriter writer = null;
	private static BufferedWriter bufferWriter = null;
	private static FileReader reader = null;
	private static BufferedReader bufferReader = null;
	
	public static Vector<String> listFilesInFolder(String folderPath)
	{
		Vector<String> files = new Vector<String>();
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
//				System.out.println("File " + listOfFiles[i].getAbsolutePath());
				files.add(listOfFiles[i].getAbsolutePath());
			}
		}
		return files;
	}
	
	
	public static boolean checkExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	public static boolean checkExistNDel(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
			
		}
		return false;
	}
	public static long checkFileSizeInMegaBytes(String filePath) {
		File file = new File(filePath);
		return file.length()/(1024*1024);
	}
	
	public static void openFileAppend(String filename) {
		closeFile();
		try {
			writer = new FileWriter(filename, true);
			bufferWriter = new BufferedWriter(writer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void openFile(String filename)
	{
		closeFile();
		try {
			writer = new FileWriter(filename);
			bufferWriter = new BufferedWriter(writer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeToFile(String s)
	{
		try {
			bufferWriter.write(s + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void flushToFile()
	{
		try {
			bufferWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void closeFile()
	{
		try {
			if (writer != null) writer.close();
			if (bufferWriter != null) bufferWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Vector<String> readContentFromFile(String filePath)
	{
		Vector<String> lines = new Vector<String>();
		
		try {
			reader = new FileReader(filePath);
			bufferReader = new BufferedReader(reader);
			String line = "";
			while ((line = bufferReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
				if (bufferReader != null) bufferReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lines;
	}
	
	
}
