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
        int response = scan.nextInt();
        scan.nextLine();
        return response;
    }
    
    public String getLine(String info) {
        //scan.nextLine();
        System.out.print(info);
        String response = scan.nextLine();
        return response;
    }
    
}
