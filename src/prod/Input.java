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
        Integer response = null;
        boolean validInput = false;
        while (!validInput) {
            try {
                response = scan.nextInt();
                validInput = true;
            } catch (java.util.InputMismatchException i) {
                System.out.println("\nInvalid input. Try again\n" + info);
            }
            scan.nextLine();
        }
        return response;
    }
    
    public String getLine(String info) {
        System.out.print(info);
        String response = scan.nextLine();
        return response;
    }
    
}
