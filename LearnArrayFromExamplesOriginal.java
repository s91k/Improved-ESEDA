import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

//Original TGI classifier
public class LearnArrayFromExamplesOriginal 
{
	int label; //class label
	int numberOfLines;
	int[] fs; //size of the fs vector, by default 117
	double[][] valueOfFeatures;
	static double[][] valueOfFeaturesTEST;
	int[][] resultOfInference;
	double[][] intervalsForFeatures;
	double[] editDistances;
	static double[][] CtableTRAIN;
	//the line below is reserved for the perfect training mode:
	//final static int LINES_IN_TEST_FILE = 316; //number of samples in 60_TRAIN_berlin_SHUFFLED.txt
	final static int LINES_IN_TEST_FILE = 534; //number of samples in berlinForJava.txt

	public LearnArrayFromExamplesOriginal(ArrayList<Integer> vector, int label) 
	{

		int LINES_IN_TEST_FILE = LearnArrayFromExamplesOriginal.LINES_IN_TEST_FILE;
		int size = vector.size();
		
		//redefine all VARS here
		fs = new int[size];
		
		for (int i = 0; i < vector.size(); i++) 
		{
			fs[i] = ((Integer) vector.get(i)).intValue();
		}

		valueOfFeatures = new double[LINES_IN_TEST_FILE][size];
		valueOfFeaturesTEST = new double[LINES_IN_TEST_FILE][size];
		resultOfInference = new int[LINES_IN_TEST_FILE][size];
		//resultOfInference = new int[200][size];
		intervalsForFeatures = new double[2][size];
		//editDistances = new double[200];
		editDistances = new double[LINES_IN_TEST_FILE];
		//CtableTRAIN = new double[200][8];
		CtableTRAIN = new double[LINES_IN_TEST_FILE][8];
	}


	public static void main(String[] args) throws IOException 
	{
		//Fill in arrays with features for FEAR
		ArrayList<Integer> vectorFEAR = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorFEAR.add(new Integer(i));
		}

		//Analogously, for all other classes
		ArrayList<Integer> vectorDISGUST = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorDISGUST.add(new Integer(i));
		}

		ArrayList<Integer> vectorHAPPINESS = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorHAPPINESS.add(new Integer(i));
		}

		ArrayList<Integer> vectorBOREDOM = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorBOREDOM.add(new Integer(i));
		}

		ArrayList<Integer> vectorNEUTRAL = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorNEUTRAL.add(new Integer(i));
		}

		ArrayList<Integer> vectorSADNESS = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorSADNESS.add(new Integer(i));
		}

		ArrayList<Integer> vectorANGER = new ArrayList<Integer>();

		for (int i = 1; i < 118; i++) 
		{
			vectorANGER.add(new Integer(i));
		}

		//call FEAR: activate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesFEAR = new LearnArrayFromExamplesOriginal(vectorFEAR, 1);
		examplesFEAR.label = 1;
		//examplesFEAR.numberOfLines = 25;
		examplesFEAR.numberOfLines = 34;
		System.out.println("HERE-0  " + examplesFEAR.label);   // OK
		examplesFEAR.run();

		//call DISGUST; DISactivate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesDISGUST = new LearnArrayFromExamplesOriginal(vectorDISGUST, 2);
		examplesDISGUST.label = 2;
		//examplesDISGUST.numberOfLines = 14;
		examplesDISGUST.numberOfLines = 23;
		System.out.println("HERE-0  " + examplesDISGUST.label);   // OK
		examplesDISGUST.run();

		// call BOREDOM activate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesBOREDOM = new LearnArrayFromExamplesOriginal(vectorBOREDOM, 4);
		examplesBOREDOM.label = 4;
		//examplesBOREDOM.numberOfLines = 30;
		examplesBOREDOM.numberOfLines = 40;
		System.out.println("HERE-0  " + examplesBOREDOM.label);   // OK
		examplesBOREDOM.run();

		// call HAPPINESS activate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesHAPPINESS = new LearnArrayFromExamplesOriginal(vectorHAPPINESS, 3);
		examplesHAPPINESS.label = 3;
		//examplesHAPPINESS.numberOfLines = 18;
		examplesHAPPINESS.numberOfLines = 35;
		System.out.println("HERE-0  " + examplesHAPPINESS.label);   // OK
		examplesHAPPINESS.run();


		// call NEUTRAL activate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesNEUTRAL = new LearnArrayFromExamplesOriginal(vectorNEUTRAL, 5);
		examplesNEUTRAL.label = 5;
		//examplesNEUTRAL.numberOfLines = 18;
		examplesNEUTRAL.numberOfLines = 39;
		System.out.println("HERE-0  " + examplesNEUTRAL.label);   // OK
		examplesNEUTRAL.run();


		// call SADNESS activate the following 4 lines if necessary
		LearnArrayFromExamplesOriginal examplesSADNESS = new LearnArrayFromExamplesOriginal(vectorSADNESS, 6);
		examplesSADNESS.label = 6;
		//examplesSADNESS.numberOfLines = 18;
		examplesSADNESS.numberOfLines = 30;
		System.out.println("HERE-0  " + examplesSADNESS.label);   // OK
		examplesSADNESS.run();

		LearnArrayFromExamplesOriginal examplesANGER = new LearnArrayFromExamplesOriginal(vectorANGER, 7);
		examplesANGER.label = 7;
		//examplesANGER.numberOfLines = 33;
		examplesANGER.numberOfLines = 63;
		//System.out.println("HERE-0  " + examplesANGER.label);   // OK
		examplesANGER.run();


		//filling in Ctable = new double[200][8]

		// column in CtableTRAIN that corresponds to fear
		for (int i = 0; i < LINES_IN_TEST_FILE; i++) {
			//for (int i = 0; i < 159; i++) {
			CtableTRAIN[i][0] = examplesFEAR.editDistances[i];
			CtableTRAIN[i][1] = examplesDISGUST.editDistances[i];
			CtableTRAIN[i][2] = examplesHAPPINESS.editDistances[i];
			CtableTRAIN[i][3] = examplesBOREDOM.editDistances[i];
			CtableTRAIN[i][4] = examplesNEUTRAL.editDistances[i];
			CtableTRAIN[i][5] = examplesSADNESS.editDistances[i];
			CtableTRAIN[i][6] = examplesANGER.editDistances[i];
			CtableTRAIN[i][7] = valueOfFeaturesTEST[i][116];


			//System.out.print(CtableTRAIN[i][0] + ", ");
			//System.out.print(examplesFEAR.editDistances[i] + ", ");
			/*System.out.print(CtableTRAIN[i][0] + ", ");
          	System.out.print(CtableTRAIN[i][1] + ", ");
          	System.out.print(CtableTRAIN[i][2] + ", ");
          	System.out.print(CtableTRAIN[i][3] + ", ");
          	System.out.print(CtableTRAIN[i][4] + ", ");
          	System.out.print(CtableTRAIN[i][5] + ", ");
          	System.out.print(CtableTRAIN[i][6] + ", ");
          	System.out.print(CtableTRAIN[i][7] + ", ");
          	System.out.print("END"); */

			System.out.println();
		}

		//last is the real label, i.e. valueOfFeatures-FDHBNSA = new double[200][size];
		/* for (int i = 0; i < 199; i++) {
           CtableTRAIN[i][7] = valueOfFeaturesTEST[i][117];
           System.out.println(CtableTRAIN[i][6]);
          //System.out.println(examplesANGER.editDistancesTEST[i]);
        } */

		printTheCtable(); 

	} //end of main()

	static public void  printTheCtable() 
	{
		for (int i = 0; i < LINES_IN_TEST_FILE; i++) 
		{
			//for (int i = 0; i < 159; i++) {

			//System.out.print(CtableTRAIN[i][0] + ", ");
			//System.out.print(examplesFEAR.editDistances[i] + ", ");
			System.out.print(CtableTRAIN[i][0] + ", ");
			System.out.print(CtableTRAIN[i][1] + ", ");
			System.out.print(CtableTRAIN[i][2] + ", ");
			System.out.print(CtableTRAIN[i][3] + ", ");
			System.out.print(CtableTRAIN[i][4] + ", ");
			System.out.print(CtableTRAIN[i][5] + ", ");
			System.out.print(CtableTRAIN[i][6] + ", ");
			System.out.print(CtableTRAIN[i][7] + ", ");
			//Two times there will be 0 instead of LABEL -- simply cut out 
			System.out.println();
		}
	}

	public void run() throws IOException 
	{
		extractVectorOfFeaturesFromTRAIN(fs, label);
		printIntervals(fs, numberOfLines);
		extractVectorFeaturesTEST(fs);
		printResults(fs);
		testAutomatonAsAcceptor(fs);
		//printResultsAutomatonAsAcceptor(fs);
		calculateEditDistanceTEST (fs, label, valueOfFeaturesTEST, intervalsForFeatures);
	}

	//private void extractVectorOfFeaturesFromTRAIN() throws IOException {
	private void extractVectorOfFeaturesFromTRAIN(int[] fs, int label) throws IOException 
	{
		String nameOfTRAIN = "noSuchTRAIN";
		//System.out.println ("HERE-1   "+ label);
		if (label == 1)
		{
			//nameOfTRAIN = "trainTA-FEAR";
			nameOfTRAIN = "50_berlinFear";
			// System.out.println(nameOfTRAIN);
		}

		if (label == 2)
		{
			//nameOfTRAIN = "trainTA-DISGUST";
			nameOfTRAIN = "50_berlinDisgust";
			// System.out.println(nameOfTRAIN);
		}

		if (label == 3)
		{
			//nameOfTRAIN = "trainTA-HAPPINESS";
			nameOfTRAIN = "50_berlinHappiness";
			System.out.println(nameOfTRAIN);
		}

		if (label == 4)
		{
			//nameOfTRAIN = "trainTA-BOREDOM";
			nameOfTRAIN = "50_berlinBoredom";
			System.out.println(nameOfTRAIN);
		}

		if (label == 5)
		{
			//nameOfTRAIN = "trainTA-NEUTRAL";
			nameOfTRAIN = "50_berlinNeutral";
			System.out.println(nameOfTRAIN);
		}

		if (label == 6)
		{
			//nameOfTRAIN = "trainTA-SADNESS";
			nameOfTRAIN = "50_berlinSadness";
			System.out.println(nameOfTRAIN);
		}

		if (label == 7)
		{
			//nameOfTRAIN = "trainTA-ANGER";
			nameOfTRAIN = "50_berlinAnger";
			System.out.println(nameOfTRAIN);
		}

		BufferedReader inputStream = null;
		PrintWriter outputStream = null;
		int countLines = 0;

		try 
		{
			inputStream =  new BufferedReader(new FileReader(nameOfTRAIN));
			outputStream = new PrintWriter(new FileWriter("characteroutput.txt"));

			String l;
			while ((l = inputStream.readLine()) != null) 
			{

				for (int i = 0; i < fs.length; i++) 
				{

					int featureNumber = fs[i];
					valueOfFeatures[countLines][i] = getValueOfFeature(featureNumber, l);
					//test System.out
					//System.out.println("valueOfFeatures[" + countLines + "][" + i + "]" + valueOfFeatures[countLines][i]);
					//TEST CORRECT
				}

				countLines++;
				//System.out.println("Current line's number is " + countLines);
			}

		} finally {
			if (inputStream != null) 
			{
				inputStream.close();
			}
			if (outputStream != null) 
			{
				outputStream.close();
			}
		}
	}

	private void printIntervals(int[] fs, int numberOfLines) 
	{
		intervalsForFeatures = getMinMax(fs, valueOfFeatures, numberOfLines);

		for (int i = 0; i < 2; i++) 
		{
			for (int j = 0; j < fs.length; j++) 
			{
				//for testing purposes
				//System.out.println("intervalsForFeatures[" + i + "][" + j + "]    = " + intervalsForFeatures[i][j]);
			}
		}
	}

	private void extractVectorFeaturesTEST(int[] fs) throws IOException 
	{
		PrintWriter outputStream = null;
		BufferedReader inputStream = null;

		//ACCURACY OF THE ACCEPTOR WITH TEXT FILE PROCESSING
		//int countLines1 = 0; IS EQUIVALENT TO sampleId
		int sampleId = 0;

		try 
		{
			inputStream = new BufferedReader(new FileReader("berlinForJava.txt"));
			//new BufferedReader(new FileReader("learnED-FDHBNSA"));     

			String l1;
			while ((l1 = inputStream.readLine()) != null) 
			{
				// GET valueOfFeatures for TEST  BEGIN

				for (int i = 0; i < fs.length; i++) 
				{
					int featureNumber = fs[i];
					valueOfFeaturesTEST[sampleId][i] = getValueOfFeature(featureNumber, l1);
					//for testing purposes
					//System.out.print(valueOfFeaturesTEST[sampleId][i] + " ~ ");

					//THE LAST COLUMN IN CtableTRAIN is filed in here
					if (i == 117) 
					{
						CtableTRAIN[sampleId][7] = valueOfFeaturesTEST[i][117];
					}
				}

				sampleId++;
				//for testing purposes
				//System.out.println();

				// GET valueOfFeatures for TEST  END
			}
		} finally {
			if (inputStream != null) 
			{
				inputStream.close();
			}

			if (outputStream != null) 
			{
				outputStream.close();
			}
		}

	}


	private void printResults(int[] fs) 
	{
		for (int i = 0; i < LINES_IN_TEST_FILE; i++) 
		{
			for (int j = 0; j < fs.length; j++) 
			{
				//   System.out.print(valueOfFeaturesTEST[i][j] + "  ");
			}
			// System.out.println();
		}
	}


	private void testAutomatonAsAcceptor(int[] fs)
	{

		// System.out.println("fs.length =  " + fs.length);
		int countAccepts = 0;
		//for (int i = 0; i < 160; i++) {
		for (int i = 0; i < LINES_IN_TEST_FILE; i++) 
		{
			int acceptRejectScore = 0;
			int acceptReject = 100; // to make sure the value will be changed;
			for (int j = 0; j < fs.length; j++) 
			{
				// double feature = valueOfFeatures[i][j];
				//int featureNumber = j;
				double min = intervalsForFeatures[0][j];
				double max = intervalsForFeatures[1][j];

				//DEBUG: it seems the intervals have not been learned correctly
				// System.out.println("MIN = " + min);
				// System.out.println("MAX = " + max);
				resultOfInference[i][j] = doesFeatureFitInterval(valueOfFeaturesTEST[i][j], min, max);
				//for testing purposes
				// System.out.println("featureValue[sampleId  " +  i + " , feature number  " + j + "] i.e. " + valueOfFeaturesTEST[i][j] + "  resultOfInference  " + resultOfInference[i][j] + " ~ ");
				acceptRejectScore = acceptRejectScore + resultOfInference[i][j];

			}

			if (acceptRejectScore < fs.length) 
			{
				acceptReject = 0;
			}
			if (acceptRejectScore == fs.length) 
			{
				acceptReject = 1;
			}
			if (acceptRejectScore > fs.length) 
			{
				acceptReject = -1;
			}
			if (acceptReject == 1) 
			{
				countAccepts = countAccepts + 1;
			}

			// System.out.println("acceptReject = " + acceptReject + "  number Of Correctly Accepted  " + countAccepts);
			//System.out.println("acceptRejectScore = " + acceptRejectScore);

		}

		System.out.println("label =   " + label + "  " + "Accepted " + countAccepts + " samples");
	}


	/*  private void printResultsAutomatonAsAcceptor(int[] fs) {
       // System.out.println("Here comes inference testing");

    } */

	private void calculateEditDistanceTEST(int[] fs, int label, double[][] valueOfFeatureTEST, double[][] intervalsForFeatures){

		//To put on weights on the branches
		//result of feature selection in the mode "one against all"

		ArrayList<Integer> selectedFeatures = new ArrayList<Integer>();

		//fear
		if (label == 1) {
			selectedFeatures.add(new Integer(11));
			selectedFeatures.add(new Integer(17));
			selectedFeatures.add(new Integer(64));
			selectedFeatures.add(new Integer(65));
			selectedFeatures.add(new Integer(68));
			selectedFeatures.add(new Integer(73));
		}
		//disgust
		else if (label == 2) {
			selectedFeatures.add(new Integer(31));
			selectedFeatures.add(new Integer(43));
			selectedFeatures.add(new Integer(49));
			selectedFeatures.add(new Integer(50));
			selectedFeatures.add(new Integer(61));
			selectedFeatures.add(new Integer(65));
			selectedFeatures.add(new Integer(75));
			selectedFeatures.add(new Integer(78));
			selectedFeatures.add(new Integer(96));
			selectedFeatures.add(new Integer(97));
		}
		//happiness
		else if (label == 3) {
			selectedFeatures.add(new Integer(12));
			selectedFeatures.add(new Integer(18));
			selectedFeatures.add(new Integer(22));
			selectedFeatures.add(new Integer(28));
			selectedFeatures.add(new Integer(35));
			selectedFeatures.add(new Integer(48));
			selectedFeatures.add(new Integer(51));
			selectedFeatures.add(new Integer(57));
			selectedFeatures.add(new Integer(60));
			selectedFeatures.add(new Integer(64));
			selectedFeatures.add(new Integer(71));
			selectedFeatures.add(new Integer(72));
			selectedFeatures.add(new Integer(73));
			selectedFeatures.add(new Integer(79));
			selectedFeatures.add(new Integer(80));
			selectedFeatures.add(new Integer(83));
			selectedFeatures.add(new Integer(85));
			selectedFeatures.add(new Integer(91));
			selectedFeatures.add(new Integer(93));
			selectedFeatures.add(new Integer(97));
			selectedFeatures.add(new Integer(99));
			selectedFeatures.add(new Integer(100));
			selectedFeatures.add(new Integer(102));
			selectedFeatures.add(new Integer(103));
			selectedFeatures.add(new Integer(104));
			selectedFeatures.add(new Integer(106));
		}
		//boredom
		else if (label == 4) {
			selectedFeatures.add(new Integer(11));
			selectedFeatures.add(new Integer(24));
			selectedFeatures.add(new Integer(25));
			selectedFeatures.add(new Integer(30));
			selectedFeatures.add(new Integer(34));
			selectedFeatures.add(new Integer(35));
			selectedFeatures.add(new Integer(43));
			selectedFeatures.add(new Integer(48));
			selectedFeatures.add(new Integer(63));
			selectedFeatures.add(new Integer(73));
			selectedFeatures.add(new Integer(74));
			selectedFeatures.add(new Integer(80));
			selectedFeatures.add(new Integer(81));
			selectedFeatures.add(new Integer(94));
			selectedFeatures.add(new Integer(97));
			selectedFeatures.add(new Integer(104));
			selectedFeatures.add(new Integer(107));
			selectedFeatures.add(new Integer(109));
		}
		//neutral
		else if (label == 5) {
			selectedFeatures.add(new Integer(13));
			selectedFeatures.add(new Integer(18));
			selectedFeatures.add(new Integer(24));
			selectedFeatures.add(new Integer(26));
			selectedFeatures.add(new Integer(68));
			selectedFeatures.add(new Integer(74));
			selectedFeatures.add(new Integer(76));
			selectedFeatures.add(new Integer(79));
			selectedFeatures.add(new Integer(86));
			selectedFeatures.add(new Integer(93));
			selectedFeatures.add(new Integer(97));
			selectedFeatures.add(new Integer(98));
			selectedFeatures.add(new Integer(102));
			selectedFeatures.add(new Integer(103));
			selectedFeatures.add(new Integer(104));
			selectedFeatures.add(new Integer(106));
			selectedFeatures.add(new Integer(109));
		}
		//sadness
		else if (label == 6) {
			selectedFeatures.add(new Integer(20));
			selectedFeatures.add(new Integer(24));
			selectedFeatures.add(new Integer(25));
			selectedFeatures.add(new Integer(26));
			selectedFeatures.add(new Integer(44));
			selectedFeatures.add(new Integer(45));
			selectedFeatures.add(new Integer(48));
			selectedFeatures.add(new Integer(59));
			selectedFeatures.add(new Integer(61));
			selectedFeatures.add(new Integer(64));
			selectedFeatures.add(new Integer(72));
			selectedFeatures.add(new Integer(73));
			selectedFeatures.add(new Integer(74));
			selectedFeatures.add(new Integer(75));
			selectedFeatures.add(new Integer(81));
			selectedFeatures.add(new Integer(91));
			selectedFeatures.add(new Integer(96));
			selectedFeatures.add(new Integer(100));
			selectedFeatures.add(new Integer(102));
			selectedFeatures.add(new Integer(109));
		}

		//anger
		else {
			selectedFeatures.add(new Integer(19));
			selectedFeatures.add(new Integer(21));
			selectedFeatures.add(new Integer(34));
			selectedFeatures.add(new Integer(45));
			selectedFeatures.add(new Integer(48));
			selectedFeatures.add(new Integer(59));
			selectedFeatures.add(new Integer(64));
			selectedFeatures.add(new Integer(69));
			selectedFeatures.add(new Integer(74));
			selectedFeatures.add(new Integer(98));
			selectedFeatures.add(new Integer(20));
		}

		//convert Array list to array

		int size = selectedFeatures.size();
		//redefine all VARS here
		int FS[] = new int[size];
		
		for (int i = 0; i < selectedFeatures.size(); i++) 
		{
			FS[i] = ((Integer) selectedFeatures.get(i)).intValue();
			System.out.println("FS[i]  " + FS[i]);
		}


		for (int i = 0; i < LINES_IN_TEST_FILE; i++)
		{
			//for (int i = 0; i < 200; i++){
			double ED = 0.0;
			
			//cycle over features
			for (int j = 0; j < fs.length; j++) 
			{
				//if the element belongs to the FS[], i.e. the set of selected features,
				//change its weight
				double weight = 1;
				
				for (int k = 0; k < FS.length; k++)
				{
					if (j == FS[k])
					{
						weight = 1.5;
					}
				}

				double cost = 0.0;
				
				if (intervalsForFeatures[1][j] == intervalsForFeatures[0][j]) 
				{
					continue;	// SK:????
				}
				if (valueOfFeatureTEST[i][j] < intervalsForFeatures[0][j]) 
				{
					cost = (intervalsForFeatures[0][j] - valueOfFeatureTEST[i][j])/(intervalsForFeatures[1][j] - intervalsForFeatures[0][j]);
				}
				if (valueOfFeatureTEST[i][j] > intervalsForFeatures[1][j]) 
				{
					cost = (valueOfFeatureTEST[i][j] - intervalsForFeatures[1][j])/(intervalsForFeatures[1][j] - intervalsForFeatures[0][j]);
				}
				// in the ver without weights it was: ED = ED + cost;
				ED = ED + cost * weight;
				//ED = ED + cost;
				//  System.out.println(valueOfFeatureTEST[i][j] + "  " + intervalsForFeatures[1][j]+ " " + intervalsForFeatures[0][j] + "  " +cost + "  ");


			}
			
			editDistances[i] = ED;
			// System.out.println("editDistancesTEST[" + i + "]  =  " + editDistancesTEST[i]);
			//System.out.println();
			// System.out.println(/*editDistancesTEST[i]*/);

		}


	}

	//this method finds min in columns of valueOfFeatures
	public static double[][] getMinMax(int fs[], double[][] valueOfFeatures, int numberOfLines) 
	{

		// System.out.println("Check: fs.length =" + fs.length);
		double[][] intervalsForFeatures1 = new double[2][fs.length];

		//MAX
		for (int j = 0; j < fs.length; j++) 
		{
			double max = -200.99;
			
			for (int i = 0; i < numberOfLines; i++) 
			{
				//System.out.println("valueOfFeature   " + valueOfFeatures[i][j]);
				if (valueOfFeatures[i][j] >= max) 
				{
					max = valueOfFeatures[i][j];
				}
			}
			
			intervalsForFeatures1[1][j] = max;
		}


		//MIN
		for (int j = 0; j < fs.length; j++) 
		{
			double min = 200.99;
			
			for (int i = 0; i < numberOfLines; i++) 
			{
				if (valueOfFeatures[i][j] <= min) 
				{
					min = valueOfFeatures[i][j];
				}
			}
			
			intervalsForFeatures1[0][j] = min;
		}

		return (intervalsForFeatures1);
	}



	//this method takes an array of numbers of features to be extracted,
	//parses the database file and returns the required values

	public static double getValueOfFeature(int arg, String firstLine) 
	{
		int countSpaces = 0;
		int positionSpace1 = 0;
		int positionSpace2 = 0;

		for (int i = 0; i < firstLine.length(); i++) 
		{
			if (firstLine.charAt(i) == ' ') 
			{
				countSpaces++;
			}
			
			if (countSpaces == arg - 2) 
			{
				// if (countSpaces == arg - 1) {
				positionSpace1 = i;
			}
			
			if (countSpaces == arg - 1) 
			{
				// if (countSpaces == arg) {
				positionSpace2 = i;
			}
		}

		String stringFeatureX = "";
		
		for (int i = positionSpace1 + 2; i < positionSpace2; i++) 
		{
			//System.out.print(firstLine.charAt(i));
			stringFeatureX = stringFeatureX + firstLine.charAt(i);
		}

		//System.out.println(stringFeatureX);
		double doubleFeatureX = Double.parseDouble(stringFeatureX);
		//System.out.println(doubleFeatureX);
		return (doubleFeatureX);
		//RETURN  doubleFeatureX
	}


	//the methods returns 1 if the sample fits into the interval
	//and 0 otherwise



	public static int doesFeatureFitInterval(/*double[][] intervalsForFeatures,*/ double feature, double min, double max) 
	{

		int answer = 100; //answer is 0 if not in the interval, 1 if in the interval

		if ((feature >= min) && (feature <= max)) 
		{
			answer = 1;
		} 
		else 
		{
			answer = 0;
		}

		return answer;
	}


}  //end of LearnArrayFromExamples