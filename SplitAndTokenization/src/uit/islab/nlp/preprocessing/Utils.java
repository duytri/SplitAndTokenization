package uit.islab.nlp.preprocessing;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {
	public static String[] removeDotToGetWords(String[] wordsTmpArray) {
		if (wordsTmpArray[wordsTmpArray.length - 1].equals(".")) {
			String[] result = new String[wordsTmpArray.length - 1];
			for (int i = 0; i < wordsTmpArray.length - 1; i++) {
				result[i] = wordsTmpArray[i];
			}
			return result;
		}
		return wordsTmpArray;
	}
	
	public static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
