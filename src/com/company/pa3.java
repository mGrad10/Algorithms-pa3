package com.company;

import java.io.*;
import java.util.Scanner;


public class pa3 {

    public static void main(String[] args) {

        Scanner scanner = null;
        PrintWriter pw = null;

        // Get files to read and write to from cmd line
        File infile = new File(args[0]);
        File outfile = new File(args[1]);

        try {
            scanner = new Scanner(infile);
            pw = new PrintWriter(outfile);

        } catch (FileNotFoundException e) {
            System.out.print(e);
        }

        //Get alphabet
        String s = scanner.nextLine();
        int n = s.length(); //Number of chars in alphabet
        char[] alphabet = s.toCharArray();

        //Get multiplication table
        char[][] table  = new char[n][n];
        for(int i = 0; i < n; i++){
            String temp = scanner.nextLine();
            for(int j = 0; j < n; j++){
                table[i][j] = temp.charAt(j);
            }
        }

        //TODO: Remove me! -- print table
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(table[i][j] + " ");
            }
            System.out.print("\n");
        }

        //Get result string
        String result = scanner.nextLine();

        //Get target result char
        String target = scanner.nextLine();

    }
}
