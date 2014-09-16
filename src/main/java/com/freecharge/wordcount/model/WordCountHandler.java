package com.freecharge.wordcount.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordCountHandler {

	private Map<String, Integer> wordCountMap;
	private List<File> files;
	private ExecutorService executor;
	
	public WordCountHandler(SystemConfig systemConfig) {
		
		File dir = new File(systemConfig.getFileCorpusLocation());
		for (File file : dir.listFiles()) {
			if(file.getName().endsWith(".txt")) {
				files.add(file);
			}
		}
		wordCountMap = new HashMap<String, Integer>();
		executor = Executors.newFixedThreadPool(systemConfig.getThreadPoolSize());
	}
	
	public int countWord(String word, File file) {
		int count = 0;
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String nextToken = scanner.next();
				if (nextToken.equalsIgnoreCase(word))
					count++;
			}
			return count;
		} catch (FileNotFoundException e) {
			return 0;
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
	public int getWordCount(final String word) {
		List<Future<Integer>> futureResultList = new ArrayList<Future<Integer>>();
		for (final File file : files) {
			Future<Integer> result = executor.submit(
				new Callable<Integer>() {
					public Integer call() throws Exception {
						return countWord(word, file);
					}
				});
			futureResultList.add(result);
		}
		int count = 0;
		for (Future<Integer> future : futureResultList) {
			try {
				count+=future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		wordCountMap.put(word, count);
		return count;
	}
	
}
