package edu.thu.mltool4j.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.thu.mltool4j.utils.ErrorReport;
import edu.thu.mltool4j.utils.InfoReport;

public class ReutersParser
{
	public static ArrayList<String> parse(File SGMLDir) throws IOException
	{
		// split sgm
		ArrayList<String> xmldataset = split(SGMLDir);

		// parse content
		ArrayList<String> dataset = new ArrayList<String>();
		for (String xmldata : xmldataset)
		{
			StringBuffer rawData = new StringBuffer();

			// parse topic
			String[] topicContainer = xmldata.split("</?TOPICS>");
			if (topicContainer.length != 3)
			{
				ErrorReport.showMessage("topicContainer.length != 3");
				return null;
			}
			else
			{
				// skip multi-topics or no-topic
				String[] topics = topicContainer[1].trim().split("</?D>");
				if (topics.length != 2)
				{
					InfoReport
							.showMessage("Skipped multi-categories and no-cateogry: "
									+ topicContainer[1]);
					continue;
				}
				else
				{
					rawData.append(topics[1] + "\n");
				}
			}

			// parse title
			String[] titleContainer = xmldata.split("</?TITLE>");
			if (titleContainer.length != 3)
			{
				InfoReport.showMessage("titleContainer.length != 3: " + xmldata);
				continue;
			}
			else
			{
				rawData.append(titleContainer[1] + "\n");
			}

			// parse body
			String[] bodyContainer = xmldata.split("</?BODY>");
			if (bodyContainer.length != 3)
			{
				InfoReport.showMessage("bodyContainer.length != 3: " + xmldata);
				continue;
			}
			else
			{
				rawData.append(bodyContainer[1]);
			}
			
			dataset.add(rawData.toString());
		}
		
		InfoReport.showMessage("Parse " + dataset.size() + " data");
		return dataset;
	}

	/**
	 * split the original SGML file, and extract the separate articles from it.
	 * 
	 * @param SGMLDir
	 *            the original SGML files directory
	 * 
	 * @return a split data list
	 */
	private static ArrayList<String> split(File SGMLDir)
	{
		String DOC_DECLARE = "<!DOCTYPE lewis SYSTEM \"lewis.dtd\">";
		String REUTERS_STA = "<REUTERS";
		String REUTERS_END = "</REUTERS>";

		ArrayList<String> dataset = new ArrayList<String>();
		BufferedReader fileReader = null;
		try
		{
			for (File file : SGMLDir.listFiles())
			{
				fileReader = new BufferedReader(new FileReader(file));

				/*
				 * read document declaration in the head of each SGM file
				 */
				String line = "";
				if ((line = fileReader.readLine()) != null
						&& line.equals(DOC_DECLARE))
				{
					/* nothing to do */
				}
				else
				{
					ErrorReport.showMessage(Class.class,
							"Invalid document declaration");
					return null;
				}

				/*
				 * read file content and split it into separate files
				 */
				while ((line = fileReader.readLine()) != null)
				{
					StringBuffer data = new StringBuffer();
					if (line.startsWith(REUTERS_STA))
					{
						data.append(line);
					}
					while ((line = fileReader.readLine()) != null
							&& !line.equals(REUTERS_END))
					{
						data.append(line);
					}
					data.append(line);

					dataset.add(data.toString());
				}
			}
			InfoReport.showMessage("Split " + dataset.size() + " data");
			return dataset;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		}
		finally
		{
			if (fileReader != null)
				try
				{
					fileReader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}
}
