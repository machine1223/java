package com.pairwinter.wendy.plsa.edu.thu.mltool4j.data;

public class Dictionary
{
	private static Dictionary instance = null;

	private Dictionary()
	{
	}

	public static Dictionary getInstance()
	{
		if (instance == null)
		{
			instance = new Dictionary();
		}
		return instance;
	}
}