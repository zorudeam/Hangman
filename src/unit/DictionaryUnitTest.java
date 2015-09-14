package unit;

import java.io.File;
import java.util.Date;
import java.util.Map;
import language.Dictionary;
import language.Word;
import language.WordProperties;

/**
 * Contains some unit testing for the {@link language.Dictionary} class.
 * 
 * @author Oliver Abdulrahim
 */
public class DictionaryUnitTest {
    
    /**
     * Stores the default testing file. Contains 234371 words - good for 
     * distribution testing.
     */
    static final File TARGET_FILE = new File("resources/dictionary.txt");
    
    private int easyCount = 0, mediumCount = 0, hardCount = 0;
    
    /**
     * The test subject!
     */
    private Dictionary dictionary;
    
    /**
     * Tests the instantiation process of the dictionary. Prints out each key in
     * the structure unless {@code quietMode} is {@code true}.
     * 
     * @param quietMode 
     */
    void instantiate(boolean quietMode) {
        System.out.println("Working...");
        dictionary = new Dictionary(TARGET_FILE);    
        Date start = new Date();
        for (Map.Entry<Word, WordProperties> entry : dictionary.entrySet()) {
            if (Dictionary.isEasyWord(entry.getKey())) {
                easyCount++;
            }
            else if (Dictionary.isMediumWord(entry.getKey())) {
                mediumCount++;
            }
            else if (Dictionary.isHardWord(entry.getKey())) {
                hardCount++;
            }
            if (!quietMode) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
        }
        System.out.println("Done!");
        printResults(start, new Date());
        reset();
    }
    
    /**
     * Prints the results of the unit test using the given dates.
     * 
     * @param start The start time of the test.
     * @param end The end time of the test.
     */
    void printResults(Date start, Date end) {
        System.out.println("\n\n                      __...--~~~~~-._   _.-~~~~~--...__\n" +
                           "                    //               `V'               \\\\ \n" +
                           "                   //    Dictionary   |  Unit Test       \\\\ \n" +
                           "                  //__...--~~~~~~-._  |  _.-~~~~~~--...__\\\\ \n" +
                           "                 //__.....----~~~~._\\ | /_.~~~~----.....__\\\\\n" +
                           "                ====================\\\\|//====================\n" +
                           "                                    `---`  ");
        System.out.println("█████████████████ █ █ █ Results █ █ █ █████████████████");
        System.out.println("Start time............: " + start);
        System.out.println("End time..............: " + end);
        System.out.println("Easy count............: " + easyCount);
        System.out.println("Medium count..........: " + mediumCount);
        System.out.println("Hard count............: " + hardCount);
        int sum = easyCount + mediumCount + hardCount;
        System.out.println("Target/Actual total...: " + dictionary.entrySet().size() + '/' + sum);
    }
    
    void reset() {
        easyCount = mediumCount = hardCount = 0;
        dictionary = null;
    }
    
    public static void main(String[] args) {
        DictionaryUnitTest test = new DictionaryUnitTest();
        test.instantiate(false);
    }
    
}
