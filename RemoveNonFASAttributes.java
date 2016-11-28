import java.io.File;
import java.io.IOException;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

//Remove non-FAS attributes from a training- and a test-file
public class RemoveNonFASAttributes 
{
	final static String TRAIN_FILENAME = "spanishINTERFACE_limited_preprocessed_train.arff";
	final static String TEST_FILENAME = "spanishINTERFACE_limited_preprocessed_test.arff";	
	
	public static void main(String[] args) throws IOException 
	{
		Instances trainData;
		Instances testData;
		ArffSaver saver;
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

		for(int i = 0; i < trainData.numInstances(); i++)
		{
			if(trainData.instance(i).classValue() == 2.0 || trainData.instance(i).classValue() == 3.0 || trainData.instance(i).classValue() == 6.0)
			{
				trainData.delete(i);
				i--;
			}
		}
		
		for(int i = 0; i < testData.numInstances(); i++)
		{
			if(testData.instance(i).classValue() == 2.0 || testData.instance(i).classValue() == 3.0 || testData.instance(i).classValue() == 6.0)
			{
				testData.delete(i);
				i--;
			}			
		}
		
		saver = new ArffSaver();
	
		saver.setInstances(trainData);
		saver.setFile(new File("fasAttr_" + TRAIN_FILENAME));
		saver.writeBatch();	
		
		saver.setInstances(testData);
		saver.setFile(new File("fasAttr_" + TEST_FILENAME));
		saver.writeBatch();				
	}

}
