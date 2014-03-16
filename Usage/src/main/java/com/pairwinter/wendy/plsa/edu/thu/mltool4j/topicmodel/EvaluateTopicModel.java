package com.pairwinter.wendy.plsa.edu.thu.mltool4j.topicmodel;

import de.timefinder.algo.roomassignment.KuhnMunkresAlgorithm;
import com.pairwinter.wendy.plsa.edu.thu.mltool4j.data.Dataset;

/**
 * This class contains several method to evaluate topic model performance
 * 
 * @author eDisOn
 * 
 */
public class EvaluateTopicModel
{
	public static double accuracy(FinalModel target, String outputfile)
	{
		// clustering
		Dataset dataset = target.getDataset();
		double[][] Pz_d = target.getDocTopicDistribute();
		int ntopics = target.getNumberofTopics();
		int ndocs = dataset.size();

		int[] doc2cluster = new int[ndocs];
		float[][] costMatrix = new float[ntopics][ntopics];
		for (int i = 0; i < ndocs; i++)
		{
			int label = dataset.getDataAt(i).getLabel();

			double max = 0;
			int cluster = -1;
			for (int j = 0; j < ntopics; j++)
			{
				if (Pz_d[i][j] > max)
				{
					max = Pz_d[i][j];
					cluster = j;
				}
			}

			doc2cluster[i] = cluster;
			costMatrix[label][cluster]++;
		}

		// replacing each cost with the maximum cost subtracted by the cost
		for (int i = 0; i < costMatrix.length; i++)
		{
			for (int j = 0; j < costMatrix[i].length; j++)
			{
				costMatrix[i][j] = ndocs - costMatrix[i][j];
			}
		}

		// matching
		KuhnMunkresAlgorithm km = new KuhnMunkresAlgorithm();
		int[][] matching = km.computeAssignments(costMatrix);

		// do evaluate
		double acc = 0;
		for (int i = 0; i < ndocs; i++)
		{
			int label = dataset.getDataAt(i).getLabel();
			int cluster = matching[doc2cluster[i]][0];

			if (label == cluster)
			{
				acc++;
			}
		}

		return acc / ndocs;
	}
}
