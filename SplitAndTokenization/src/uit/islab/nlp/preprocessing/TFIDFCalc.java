package uit.islab.nlp.preprocessing;

public class TFIDFCalc {
	public double tf(String[] sentence, String term) {
		double termAppear = 0;
		int senSize = 0;
		for (String word : sentence) {
			senSize++;
			if (term.equalsIgnoreCase(word))
				termAppear++;
		}
		return termAppear / sentence.length;
	}

	public double idf(String[][] sentences, String term) {
		double n = 0;
		int docsSize = 0;
		for (String[] sentence : sentences) {
			docsSize++;
			for (String word : sentence) {
				if (term.equalsIgnoreCase(word)) {
					n++;
					break;
				}
			}
		}
		return Math.log10(docsSize / n);
	}

	public double tfIdf(String[] doc, String[][] docs, String term) {
		return tf(doc, term) * idf(docs, term);
	}
}
