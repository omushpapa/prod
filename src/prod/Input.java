/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

import java.util.Scanner;

/**
 *
 * @author giantas
 */
public class Input {
    
    Scanner scan = new Scanner(System.in);
    
    public Input() {}
    
    public int getInt(String info) {
        System.out.print(info);
        return scan.nextInt();
    }
    
    public String getLine(String info) {
        System.out.print(info);
        return scan.nextLine();
    }
    
}
