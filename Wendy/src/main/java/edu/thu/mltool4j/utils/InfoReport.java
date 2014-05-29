package edu.thu.mltool4j.utils;

import org.apache.log4j.Logger;

public class InfoReport
{
	//	private static Logger logger = Logger.getLogger(InfoReport.class.getName());

	public static void showMessage(Object infobj, String msg)
	{
		//		logger.info("Info in class: " + infobj.getClass().getName() + "\tMsg: "
		//				+ msg);
		System.out.println("Info in class: " + infobj.getClass().getName()
				+ "\tMsg: " + msg);
	}

	public static void showMessage(String msg)
	{
		//		logger.info("Info: " + msg);
		System.out.println("Info: " + msg);
	}
}
