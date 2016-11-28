import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.attributeSelection.*;

//Try to do individual feature selection (feature selection for each possible class attribute)
public class IndividualFeatureSelection 
{
	final static String FILE_NAME = "spanishINTERFACE_preprocessed.arff";
	final static int NUM_ATTR = 30;

	public static void main(String[] args) throws Exception 
	{
		Instances data;
		
		data = DataSource.read(FILE_NAME);
		data.setClassIndex(data.numAttributes() - 1);			
		
		for(int i = 0; i < data.classAttribute().numValues(); i++)
		{
			Instances tmp = new Instances(data);
			
			tmp.classAttribute().addStringValue("unknown");
			tmp.setClassIndex(tmp.numAttributes() - 1);	
			
			for(int j = 0; j < data.numInstances(); j++)
			{
				if(tmp.instance(j).classValue() != i)
				{
					tmp.instance(j).setClassValue("unknown");
				}	
			}
			
			ASEvaluation eval = new InfoGainAttributeEval();
			Ranker search = new Ranker();
			AttributeSelection attrSelector = new AttributeSelection();
			
			search.setThreshold(0.0625);
			
			attrSelector.setEvaluator(eval);
			attrSelector.setSearch(search);
			attrSelector.setRanking(true);
			attrSelector.SelectAttributes(tmp);
			
			int[] selectedAttributes = attrSelector.selectedAttributes();
			
			System.out.println("if(label == " + i + "){");
			
			for(int j = 0; j < selectedAttributes.length - 1; j++)
			{
				System.out.println("\tfeatures[" + selectedAttributes[j] + "] = 1.5;");
			}
			
			System.out.println("}");
		}
	}
}
