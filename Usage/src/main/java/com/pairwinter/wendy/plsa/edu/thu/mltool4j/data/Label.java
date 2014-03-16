package com.pairwinter.wendy.plsa.edu.thu.mltool4j.data;


import com.pairwinter.wendy.plsa.edu.thu.mltool4j.utils.ErrorReport;

public class Label
{
	public static int str2int(String strLabel)
	{
		if (strLabel.toLowerCase().equals("postive"))
		{
			return +1;
		}
		else if (strLabel.toLowerCase().equals("negative"))
		{
			return -1;
		}
		else if (strLabel.toLowerCase().equals("unknown"))
		{
			return 0;
		}
		else
		{
			ErrorReport.showMessage(Label.class, "str2int", "input label: "
                    + strLabel);
			return Integer.MIN_VALUE;
		}
	}
}
