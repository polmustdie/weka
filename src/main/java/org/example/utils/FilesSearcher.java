package org.example.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesSearcher {
	
	// search all files in a directory
	public static String[] search(String directory){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff")){
				lspaths.add(files[i].getAbsolutePath());
			}
		}
		
		String[] paths = new String[lspaths.size()];	
		
		for(int k=0; k<lspaths.size(); k++){
			paths[k] = lspaths.get(k);
		}
		
		return paths;
	}
	
	// serch filtered arff files
	public static String[] search(String directory, String filter){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff") && files[i].getName().startsWith(filter)){
				lspaths.add(files[i].getAbsolutePath());
			}
		}
		
		String[] paths = new String[lspaths.size()];		
		
		for(int k=0; k<lspaths.size(); k++){
			paths[k] = lspaths.get(k);
		}
		
		return paths;
	}
	
	// search files with specific indices
	public static String[] search(String directory, int index){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff") && files[i].getName().contains(String.valueOf(index) + ".")){
				lspaths.add(files[i].getAbsolutePath());
			}
		}
		
		String[] paths = new String[lspaths.size()];	
		
		for(int k=0; k<lspaths.size(); k++){
			paths[k] = lspaths.get(k);
		}
		
		return paths;
	}

}
