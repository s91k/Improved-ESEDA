import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

//Updated TGI classifier with feature selection
public class LearnArrayFromExamplesUpdated 
{
	int label; //class label
	int fs;
	Instances data;
	int[][] resultOfInference;
	double[] weights;
	double[][] intervalsForFeatures;
	double[] editDistances;
	static double[][] CtableTRAIN;
	final static String TRAIN_FILENAME = "fasAttr_spanishINTERFACE_limited_preprocessed_train.arff";
	final static String TEST_FILENAME = "FAS_data/Patient2_2012_phase4_normalized.arff";
	final static int WEIGHT = 20;
	final static double REDUCTION = 0.0;	//Percentage that the intervals are reduced by

	public LearnArrayFromExamplesUpdated(int label, Instances trainData, Instances testData) 
	{
		this.fs = testData.numAttributes() - 1;
		this.label = label;
		this.resultOfInference = new int[testData.numInstances()][fs];
		this.intervalsForFeatures = new double[2][fs];
		this.editDistances = new double[testData.numInstances()];
		
		data = new Instances(trainData, 0);
		
		for(int i = 0; i < trainData.numInstances(); i++)
		{		
			if(trainData.instance(i).classValue() == this.label)
			{
				data.add(trainData.instance(i));
			}
		}
		
		for (int j = 0; j < this.fs; j++)
		{
			double min = Double.MAX_VALUE;			
			double max = -Double.MAX_VALUE;

			for (int i = 0; i < data.numInstances(); i++)
			{
				if (data.instance(i).value(j) < min)
				{
					min = data.instance(i).value(j);
				}	
			
				if (data.instance(i).value(j) > max)
				{
					max = data.instance(i).value(j);
				}
			}
			
			this.intervalsForFeatures[0][j] = min;		
			this.intervalsForFeatures[1][j] = max;
		}
		
		//Perform feature selection
		this.weights = new double[fs];
		
		for(int i = 0; i < this.weights.length; i++)
		{
			this.weights[i] = 1.0;
		}
		
		Instances tmp = new Instances(trainData);

		for(int j = 0; j < data.numInstances(); j++)
		{
			if(tmp.instance(j).classValue() != label)
			{
				if(j == 0)
				{
					tmp.instance(j).setClassValue(1);
				}
				else
				{
					tmp.instance(j).setClassValue(0);
				}			
			}	
		}
		
		ASEvaluation eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
		AttributeSelection attrSelector = new AttributeSelection();
		int[] selectedAttributes = new int[0];
		
		search.setThreshold(0.0625);
		
		attrSelector.setEvaluator(eval);
		attrSelector.setSearch(search);
		attrSelector.setRanking(true);
		
		try 
		{
			attrSelector.SelectAttributes(tmp);
			selectedAttributes = attrSelector.selectedAttributes();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		for(int j = 0; j < selectedAttributes.length - 1; j++)
		{
			this.weights[selectedAttributes[j]] = WEIGHT;
		}	
	}

	public static void main(String[] args) throws IOException
	{
		Instances trainData;
		Instances testData;
		LearnArrayFromExamplesUpdated[] learners;
		
		try 
		{
			trainData = DataSource.read(TRAIN_FILENAME);
			testData = DataSource.read(TEST_FILENAME);
			
			trainData.setClassIndex(trainData.numAttributes() - 1);
			testData.setClassIndex(testData.numAttributes() - 1);			
		} 
		catch (Exception e) 
		{
			System.out.println("Could not read dataset file");
			e.printStackTrace();
			return;
		}
		
		LearnArrayFromExamplesUpdated.CtableTRAIN = new double[testData.numInstances()][testData.classAttribute().numValues() + 1];		
			
		for(int i = 0; i < testData.numInstances(); i++)
		{
			LearnArrayFromExamplesUpdated.CtableTRAIN[i][LearnArrayFromExamplesUpdated.CtableTRAIN[i].length - 1] = testData.instance(i).classValue();
		}
		
		learners = new LearnArrayFromExamplesUpdated[testData.classAttribute().numValues()];
		
		for(int i = 0; i < learners.length; i++)
		{
			learners[i] = new LearnArrayFromExamplesUpdated(i, trainData, testData);
			System.out.println("HERE-0  " + learners[i].label);   // OK
			
			//learners[i].testAutomatonAsAcceptor(testData);
			learners[i].reduceIntervals();
			learners[i].calculateEditDistanceTEST(testData);
		}

		//Filling in Ctable
		for (int i = 0; i < CtableTRAIN.length; i++) 
		{
			for (int j = 0; j < CtableTRAIN[i].length - 1; j++) 
			{
				CtableTRAIN[i][j] = learners[j].editDistances[i];
			}
		}		

		printTheCtableAsARFF("emotionalDistance_" + TEST_FILENAME, testData);
		printTheCtableAsDist("emotionalDistance_" + TEST_FILENAME + ".txt", testData);
		checkConsistency(testData);
		checkThatClassDistIsZero();
	}
	
	public void reduceIntervals()
	{
		for(int i = 0; i < fs; i++)
		{
			double intervalSize = this.intervalsForFeatures[1][i] - this.intervalsForFeatures[0][i];
			this.intervalsForFeatures[0][i] = this.intervalsForFeatures[0][i] + intervalSize * REDUCTION;
			this.intervalsForFeatures[1][i] = this.intervalsForFeatures[0][i] + intervalSize * (1.0 - REDUCTION);			
		}
	}
	
	public static void checkThatClassDistIsZero()
	{
		int nrOfNotZero = 0;
		
		for(int i = 0; i < CtableTRAIN.length; i++)
		{	
			if(CtableTRAIN[i][(int) CtableTRAIN[i][CtableTRAIN[i].length - 1]] != 0.0)
			{
				nrOfNotZero++;	
			}
		}
		
		System.out.println(nrOfNotZero + " of " + CtableTRAIN.length + " wasn't zero");		
	}
	
	public static void checkConsistency(Instances testData)
	{
		//Check the consistency
		int nrOfInconsistant = 0;
		int frequency[] = new int[CtableTRAIN[0].length - 1];
		int nrOfInconsistantForEmotion[] = new int[CtableTRAIN[0].length - 1];
		
		for(int i = 0; i < CtableTRAIN.length; i++)
		{
			double h = Double.MAX_VALUE;
			
			for(int j = 0; j < CtableTRAIN[i].length - 1; j++)
			{
				if(j != CtableTRAIN[i][CtableTRAIN[i].length - 1])
				{
					h = Math.min(h, CtableTRAIN[i][j]);
				}
			}
			
			frequency[(int) CtableTRAIN[i][CtableTRAIN[i].length - 1]]++;
			
			if(h <= CtableTRAIN[i][(int) CtableTRAIN[i][CtableTRAIN[i].length - 1]])
			{
				nrOfInconsistant++;
				nrOfInconsistantForEmotion[(int) CtableTRAIN[i][CtableTRAIN[i].length - 1]]++;	
			}
		}
		
		System.out.println(nrOfInconsistant + " of " + CtableTRAIN.length + " was inconsistent");
		
		for(int i = 0; i < frequency.length; i++)
		{
			System.out.println("\t" + testData.classAttribute().value(i) + ": " + nrOfInconsistantForEmotion[i] + " of " + frequency[i] + " was inconsistent");
		}
	}

	static public void printTheCtable() 
	{
		for (int i = 0; i < CtableTRAIN.length; i++) 
		{
			System.out.print(i + ":\t");

			for(int j = 0; j < CtableTRAIN[i].length; j++)
			{
				System.out.printf("%.8f,\t", CtableTRAIN[i][j]);
			}

			System.out.println();
		}
	}
	
	static public void printTheCtableAsARFF(String filename, Instances testData) throws IOException
	{
		Instances output;
		FastVector attInfo = new FastVector();
		FastVector classes = new FastVector();
		
		for(int i = 0; i < testData.classAttribute().numValues(); i++)
		{
			attInfo.addElement(new Attribute(testData.classAttribute().value(i)));
			classes.addElement(testData.classAttribute().value(i));
		}
		
		attInfo.addElement(new Attribute("emotion", classes));	
		output = new Instances("ctable", attInfo, CtableTRAIN.length);
		
		for(int i = 0; i < CtableTRAIN.length; i++)
		{
			output.add(new DenseInstance(1.0, CtableTRAIN[i]));
		}
		
		ArffSaver saver = new ArffSaver();
		
		saver.setInstances(output);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
	
	static public void printTheCtableAsDist(String filename, Instances testData) throws IOException
	{
		PrintWriter writer = new PrintWriter(filename);
		double classValue = 9999.0;
		
		for(int i = 0; i < CtableTRAIN.length; i++)
		{
			if(testData.instance(i).classValue() != classValue)
			{
				writer.println();
				writer.println(testData.classAttribute().value((int) testData.instance(i).classValue()));
				classValue = testData.instance(i).classValue();
			}
			
			writer.println(String.format("%.8f",CtableTRAIN[i][(int) testData.instance(i).classValue()]));
		}
		
		writer.close();
	}

	private void testAutomatonAsAcceptor(Instances testData)
	{
		int countAccepts = 0;

		for (int i = 0; i < testData.numInstances(); i++) 
		{
			int acceptRejectScore = 0;
			int acceptReject = 100; // to make sure the value will be changed;
			for (int j = 0; j < fs; j++) 
			{
				double min = intervalsForFeatures[0][j];
				double max = intervalsForFeatures[1][j];

				//Tt seems the intervals have not been learned correctly
				// System.out.println("MIN = " + min);
				// System.out.println("MAX = " + max);
				if(testData.instance(i).value(j) >= min && testData.instance(i).value(j) <= max)
				{
					resultOfInference[i][j] = 1;
				}
				else
				{
					resultOfInference[i][j] = 0;
				}

				acceptRejectScore += resultOfInference[i][j];
			}

			if (acceptRejectScore < fs) 
			{
				acceptReject = 0;
			}
			else if (acceptRejectScore == fs) 
			{
				acceptReject = 1;
			}
			else if (acceptRejectScore > fs) 
			{
				acceptReject = -1;
			}
			
			if (acceptRejectScore == fs) 
			{
				countAccepts++;
			}
		}

		System.out.println("label =   " + label + "  " + "Accepted " + countAccepts + " samples");
	}

	private void calculateEditDistanceTEST(Instances testData)
	{		
		for (int i = 0; i < testData.numInstances(); i++)
		{
			double ED = 0.0;
			
			//cycle over features
			for (int j = 0; j < this.fs; j++) 
			{
				//if the element belongs to the FS[], i.e. the set of selected features,
				//change its weight
				double cost = 0.0;
				
				if (intervalsForFeatures[1][j] == intervalsForFeatures[0][j]) 
				{
					continue;
				}
				if (testData.instance(i).value(j) < intervalsForFeatures[0][j]) 
				{
					cost = (intervalsForFeatures[0][j] - testData.instance(i).value(j))/(intervalsForFeatures[1][j] - intervalsForFeatures[0][j]);
				}
				if (testData.instance(i).value(j) > intervalsForFeatures[1][j]) 
				{
					cost = (testData.instance(i).value(j) - intervalsForFeatures[1][j])/(intervalsForFeatures[1][j] - intervalsForFeatures[0][j]);
				}

				// in the version without weights it was: ED = ED + cost;
				ED += cost * this.weights[j];
			}
			
			editDistances[i] = ED;
		}
	}
} 