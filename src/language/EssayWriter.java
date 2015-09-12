/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.io.File;

/**
 *
 * @author Cylin
 */
public class EssayWriter {
    
    public static void main(String[] args) {
        File resource = new File("src/language/dictionary.txt");
        System.out.println(new Dictionary(resource));
    }
    
}
