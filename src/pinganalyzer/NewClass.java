/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinganalyzer;

import java.text.DecimalFormat;

/**
 *
 * @author Michal
 */
public class NewClass {
    public static void main(String[] args) {
        System.out.println("'"+format(5000)+"'");
    }
    
    static DecimalFormat formatter = new DecimalFormat("###0ms ");
    public static String format(int value){
        return String.format("%7s", formatter.format((double)value));
    }
    
}
