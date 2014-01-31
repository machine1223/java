package com.pw.common.logging;

import org.apache.commons.io.FileUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrokerLogParser {
	
	private static LogWrapper logger = LogWrapper.getLogger(BrokerLogParser.class);
	
	public class CallInitRecord {
		String hour;
		
		long startTime;
		
		long endTime;
		
		String id;
	}
	
	public class LogForHour {
		long maxHttpCallTime = 0;
		long minHttpCallTime = Long.MAX_VALUE;
		
		float totalHttpCallTime = 0;
		
		int httpCallCount;
		
		public void addCallInitRecord(CallInitRecord cir) {
			this.httpCallCount++;
			
			long delta = cir.endTime - cir.startTime;
			if (delta > maxHttpCallTime) maxHttpCallTime = delta;
			if (delta < minHttpCallTime) minHttpCallTime = delta;
			
			totalHttpCallTime += delta;
		}
		
		@Override
		public String toString() {
			return String.format("total calls: %s, max latency: %s, median latency: %s, avg latency: %s", 
					this.httpCallCount, this.maxHttpCallTime, ((this.maxHttpCallTime + this.minHttpCallTime) / 2),
					this.totalHttpCallTime / this.httpCallCount);
		}
	}
	
	public Map<String, LogForHour> entries = new TreeMap<String, LogForHour>();
	
	public Map<String, CallInitRecord> pendingCalls = new HashMap<String, CallInitRecord>();

//	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	DateTimeFormatter dateParser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	
	public String getHourForDateStr(String dateStr) {
		dateStr = dateStr.substring(1, 15);
		return dateStr + "00:00";
	}
	
	public Date getDateForDateStr(String dateStr) throws Exception {
		dateStr = dateStr.substring(1, dateStr.length()-1);
		
		Date d = dateParser.parseDateTime(dateStr).toDate();
		//logger.info("Found date %s from %s", d, dateStr);
		return d;
	}
	
	//[2013-03-27 22:23:29.200] [TRACE] twilio_engine - cb297b7b-ce9c-4f40-8198-937e392d1e27 0 1 Callback url is http://50.18.173.33:8001/twilio/callback/cb297b7b-ce9c-4f40-8198-937e392d1e27/0/1/
	public void handleCallInitLine(String line) throws Exception {
		String[] tokens = line.split(" ");
		String dateStr = tokens[0] + " " + tokens[1];
		
		CallInitRecord cir = new CallInitRecord();
		cir.startTime = getDateForDateStr(dateStr).getTime();
		cir.id = tokens[5] + "_" + tokens[6];
		cir.hour = getHourForDateStr(dateStr);
		//logger.info("Got id %s", cir.id);
		pendingCalls.put(cir.id, cir);
	}
	
	//[2013-03-27 22:23:29.940] [TRACE] twilio_engine - cb297b7b-ce9c-4f40-8198-937e392d1e27 0 1 Call to +16173000445 successful
	public void handleCallCompleteLine(String line) throws Exception {
		String[] tokens = line.split(" ");
		String id = tokens[5] + "_" + tokens[6];
		CallInitRecord cir = pendingCalls.remove(id);
		
		if (cir == null) {
			logger.info("No cir matching %s", id);
			return;
		}
		
		String dateStr = tokens[0] + " " + tokens[1];
		String hour = getHourForDateStr(dateStr);
		
		cir.endTime = getDateForDateStr(dateStr).getTime();
		
		LogForHour lfh = entries.get(hour);
		if (lfh == null) {
			lfh = new LogForHour();
			entries.put(hour, lfh);
		}
		
		lfh.addCallInitRecord(cir);
	}
	
	public void parseLog(File file) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		Pattern callInitLine = Pattern.compile("Callback url is");
		Pattern callCompleteLine = Pattern.compile("Call to (\\+\\d*) successful");
		while((line = br.readLine()) != null) {
			Matcher m = callInitLine.matcher(line);
			if (m.find()) {
				handleCallInitLine(line);
			}
			
			m = callCompleteLine.matcher(line);
			if (m.find()) {
				handleCallCompleteLine(line);
			}
		}
		
		br.close();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		/**/
		String sourceDirStr = "/Users/wrb/Downloads/logs";
		String root = "/Users/wrb/Downloads/parsed_logs";
		
		File sourceDir = new File(sourceDirStr);
		if (!sourceDir.exists()) throw new Exception("Yuck");
		
		File rootDir = new File(root);
		if (!rootDir.exists()) rootDir.mkdir();
		File destDir = new File(rootDir, System.currentTimeMillis() + "");
		destDir.mkdir();
		
		File[] brokerFiles = sourceDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathName) {
				return pathName.getName().indexOf("broker.log") != -1;
			}
		});
		
		for (File file : brokerFiles) {
			
			String fileName = "broker.log." + file.lastModified();
			
			File destFile = new File(destDir, fileName);
			FileUtils.copyFile(file, destFile);
		}
		
		sourceDir = destDir;
		
		/**		
		File sourceDir = new File("/Users/wrb/Downloads/parsed_logs/1364521190033");
		**/
		
		String[] brokerFilesStr = sourceDir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.indexOf("broker.log") != -1;
			}
		});
		
		List<String> files = Arrays.asList(brokerFilesStr);
		Collections.sort(files);
		
		BrokerLogParser blp = new BrokerLogParser();
		for (String file : files) {
			// Parse file
			blp.parseLog(new File(sourceDir, file));
		}
		
		for (String hour : blp.entries.keySet()) {
			System.out.println(hour + " " + blp.entries.get(hour));
		}
	}

}
