package unit;

import java.util.Date;
import language.Dictionary;
import language.Difficulty;

/**
 * Contains some unit testing for the {@link language.Dictionary} class.
 * 
 * @author Oliver Abdulrahim
 */
public class DictionaryUnitTest {
    
    private int easyCount = 0, mediumCount = 0, hardCount = 0;
    
    private Date start, end;
    
    /**
     * The test subject!
     */
    private Dictionary dictionary;
    
    /**
     * Tests the instantiation process of the dictionary.
     */
    void instantiate() {
        dictionary = new Dictionary();
    }
    
    /**
     * Counts each word in the dictionary. Prints out each key in the structure 
     * unless {@code quietMode} is {@code true}.
     */
    void countWords(boolean quietMode) {
        System.out.println("Working...");
        start = new Date();
        if (!quietMode) {
            System.out.println(dictionary.getListOf(Difficulty.EASY));
            System.out.println(dictionary.getListOf(Difficulty.MEDIUM));
            System.out.println(dictionary.getListOf(Difficulty.HARD));
        }
        end = new Date();
        easyCount = dictionary.getListOf(Difficulty.EASY).size();
        mediumCount = dictionary.getListOf(Difficulty.MEDIUM).size();
        hardCount = dictionary.getListOf(Difficulty.HARD).size();
        System.out.println("Done!");
    }
    
    /**
     * Prints the results of the unit test.
     */
    void printResults() {
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
        System.out.println("Expected/Actual total...: " + dictionary.size() + '/' + sum);
    }
    
    void reset() {
        easyCount = mediumCount = hardCount = 0;
        start = end = null;
    }
    
    public static void main(String[] args) {
        DictionaryUnitTest test = new DictionaryUnitTest();
        test.instantiate();
        test.countWords(false);
        test.printResults();
//        test.reset();
    }
    
}
