package ui;

import java.util.Scanner;

public class UserInputPrompt {

    public static String prompt(String text) {
        var scanner = new Scanner(System.in);
        System.out.println();
        System.out.println(text);
        return scanner.nextLine();
    }

}
