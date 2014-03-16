package com.pairwinter.wendy.plsa.edu.thu.mltool4j.dataimport;

import java.io.File;
import java.io.StringWriter;

import com.pairwinter.wendy.plsa.edu.thu.mltool4j.utils.InfoReport;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.tokenizer.SimpleTokenizer;
import edu.udo.cs.wvtool.generic.wordfilter.StopWordsWrapper;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.vectorcreation.TermOccurrences;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class VSModeling
{
	private final static double FilterNum = 3, Infinite = 100000000;

	public enum Tokenizer
	{
		Simple, NGram
	}

	public enum Wordfilter
	{
		Dummy, BuiltIn, FileFilter
	}

	public enum Stemmer
	{
		Dummy, Porter
	}

	public enum Weighting
	{
		BinaryOccurrence, TermOccurrence, TermFrequency, TFIDF
	}

	public StringBuffer modeling(String dir, Tokenizer tokenizer,
			Wordfilter wordfilter, Stemmer stemmer, Weighting weighting)
			throws WVToolException
	{
		WVTool wvt = new WVTool(false);

		// config
		WVTConfiguration config = loadConfig(tokenizer, wordfilter, stemmer,
				weighting);

		// load data
		File[] files = new File(dir).listFiles();
		WVTFileInputList list = new WVTFileInputList(files.length);
		for (File file : files)
		{
			list.addEntry(new WVTDocumentInfo(file.getAbsolutePath(), null,
					null, null));
		}

		// create wordlist
		WVTWordList wordList = wvt.createWordList(list, config);
		wordList.pruneByFrequency((int) FilterNum, (int) Infinite);

		// create vector
		StringWriter sw = new StringWriter();
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT,
				new WVTConfigurationFact(new WordVectorWriter(sw, true)));

		return sw.getBuffer();
	}

	private WVTConfiguration loadConfig(Tokenizer tokenizer,
			Wordfilter wordfilter, Stemmer stemmer, Weighting weighting)
	{
		WVTConfiguration config = new WVTConfiguration();

		switch (tokenizer)
		{
			case Simple:
				config.setConfigurationRule(WVTConfiguration.STEP_TOKENIZER,
						new WVTConfigurationFact(new SimpleTokenizer()));
			default:
				InfoReport.showMessage(this, "No tokenizer specified");
		}

		switch (wordfilter)
		{
			case BuiltIn:
				config.setConfigurationRule(WVTConfiguration.STEP_WORDFILTER,
						new WVTConfigurationFact(new StopWordsWrapper()));
			default:
				InfoReport.showMessage(this, "No wordfilter specified");
		}

		switch (stemmer)
		{
			case Porter:
				config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
						new WVTConfigurationFact(new PorterStemmerWrapper()));
			default:
				InfoReport.showMessage(this, "No stemmer specified");
		}

		switch (weighting)
		{
			case TermOccurrence:
				config.setConfigurationRule(
						WVTConfiguration.STEP_VECTOR_CREATION,
						new WVTConfigurationFact(new TermOccurrences()));
			default:
				InfoReport.showMessage(this, "No weighting specified");
		}

		return config;
	}
}
