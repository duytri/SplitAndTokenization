package uit.islab.nlp.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import uit.islab.classifier.svm.svm_train;
import vn.hus.nlp.sd.SentenceDetector;
import vn.hus.nlp.sd.SentenceDetectorFactory;
import vn.hus.nlp.tokenizer.TokenizerOptions;
import vn.hus.nlp.tokenizer.VietTokenizer;
import vn.hus.nlp.utils.FileIterator;
import vn.hus.nlp.utils.TextFileFilter;

public class SplitAndTokenization {

	public static void main(String[] args) throws IOException {
		String inputFolName = "input";
		String outputFolName = "output";
		int nTokens = 0;
		SentenceDetector senDetector = SentenceDetectorFactory.create("vietnamese");

		// get the current dir
		String currentDir = new File("").getAbsolutePath();
		String inputDirPath = currentDir + File.separator + inputFolName;
		String outputDirPath = currentDir + File.separator + outputFolName;

		File inputDirFile = new File(inputDirPath);

		Properties property = new Properties();
		property.setProperty("sentDetectionModel", currentDir + File.separator + "models" + File.separator
				+ "sentDetection" + File.separator + "VietnameseSD.bin.gz");
		property.setProperty("lexiconDFA", currentDir + File.separator + "models" + File.separator + "tokenization"
				+ File.separator + "automata" + File.separator + "dfaLexicon.xml");
		property.setProperty("unigramModel", currentDir + File.separator + "models" + File.separator + "tokenization"
				+ File.separator + "bigram" + File.separator + "unigram.xml");
		property.setProperty("bigramModel", currentDir + File.separator + "models" + File.separator + "tokenization"
				+ File.separator + "bigram" + File.separator + "bigram.xml");
		property.setProperty("externalLexicon", currentDir + File.separator + "models" + File.separator + "tokenization"
				+ File.separator + "automata" + File.separator + "externalLexicon.xml");
		property.setProperty("normalizationRules", currentDir + File.separator + "models" + File.separator
				+ "tokenization" + File.separator + "normalization" + File.separator + "rules.txt");
		property.setProperty("lexers", currentDir + File.separator + "models" + File.separator + "tokenization"
				+ File.separator + "lexers" + File.separator + "lexers.xml");
		property.setProperty("namedEntityPrefix", currentDir + File.separator + "models" + File.separator
				+ "tokenization" + File.separator + "prefix" + File.separator + "namedEntityPrefix.xml");

		VietTokenizer tokenizer = new VietTokenizer(property);
		tokenizer.turnOffSentenceDetection();

		// get all input files
		File[] inputFiles = FileIterator.listFiles(inputDirFile,
				new TextFileFilter(TokenizerOptions.TEXT_FILE_EXTENSION));
		System.out.println("Tokenizing all files in the directory, please wait...");
		long startTime = System.currentTimeMillis();
		for (File aFile : inputFiles) {
			// get the simple name of the file
			String input = aFile.getName();
			// the output file have the same name with the automatic file
			String output = outputDirPath + File.separator + input;
			System.out.println(aFile.getAbsolutePath() + "\n" + output);
			// tokenize the content of file
			String[] sentences = senDetector.detectSentences(aFile.getAbsolutePath());
			String[][] matrixWords = new String[sentences.length][];
			for (int i = 0; i < sentences.length; i++) {
				String[] words = tokenizer.tokenize(sentences[i]);
				String[] wordsTmpArr = words[0].split(" ");
				matrixWords[i] = Utils.removeDotToGetWords(wordsTmpArr);
			}

			// Calculate TFIDF
			TFIDFCalc tCalc = new TFIDFCalc();
			HashMap<String, Double> tfidfResultSet = new HashMap<String, Double>();
			for (int i = 0; i < matrixWords.length; i++) {
				for (int j = 0; j < matrixWords[i].length; j++) {
					nTokens++;
					tfidfResultSet.put(matrixWords[i][j]+"["+i+"]", tCalc.tfIdf(matrixWords[i], matrixWords, matrixWords[i][j]));
				}
			}
			
			// Sort descending
			tfidfResultSet = (HashMap<String, Double>) Utils.sortByComparator(tfidfResultSet, false);
			
			// Show result
			Set<String> keys = tfidfResultSet.keySet();
			for (String key : keys) {
				System.out.println(key + "-----" + tfidfResultSet.get(key));
			}

		}
		long endTime = System.currentTimeMillis();
		float duration = (float) (endTime - startTime) / 1000;
		System.out.println(
				"Tokenized " + nTokens + " words of " + inputFiles.length + " files in " + duration + " (s).\n");
		
		//svm_train train = new svm_train();
		//t.run();
	}

}
