package com.company;

import java.io.*;
import java.util.ArrayList;
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

        //Get result string
        String result = scanner.nextLine();

        //Get target result char
        String target = scanner.nextLine();

        //To add parens to string
        StringBuilder str = new StringBuilder(result);
        str.insert(3, ")");
        System.out.println(str);

        //Table of subproblems
        ArrayList<Opt>[][] memoTable = (ArrayList<Opt>[][]) new ArrayList[n][n];

        initMemoTable(memoTable, n, result);

    }
    /* Method to init the memoization table
     * @param memoTable the memoization table to init
     * @param n number of characters in the alphabet
     * @param result String to add parens to
     */
    public static void initMemoTable(ArrayList<Opt>[][] memoTable, int n, String result){
        //Init the memo table with Opts
        for(int i = 0; i < n ; i++){
            for(int j = 0; j < n; j++){
                memoTable[i][j] = new ArrayList<Opt>();
                memoTable[i][j].add(new Opt(" ", " "));
            }
        }

        //Init base cases
        for(int i = 0; i < n; i++){
            memoTable[i][i].get(0).setParenStr(Character.toString(result.charAt(0)));
            memoTable[i][i].get(0).setTargetStr(Character.toString(result.charAt(0)));

        }

        //TODO: Print statement -- remove me
        for(int i = 0; i < n ; i++){
            for(int j = 0; j < n; j++){
                System.out.print(memoTable[i][j].get(0).getParenStr());
            }
            System.out.print("\n");
        }
    }
    /* Method to add parens to a string
     * @param result String to add parens to
     */
    public static void addParens(String result){
        //To add parens to string
        StringBuilder str = new StringBuilder(result);
        str.insert(3, ")");
        System.out.println(str);

    }

    /* Class to represent sub problems */
    public static class Opt{

        String parenStr;
        String targetStr;

        public Opt(String parenStr, String targetStr) {
            this.parenStr = parenStr;
            this.targetStr = targetStr;
        }

        public String getParenStr() {
            return parenStr;
        }

        public void setParenStr(String parenStr) {
            this.parenStr = parenStr;
        }

        public String getTargetStr() {
            return targetStr;
        }

        public void setTargetStr(String targetStr) {
            this.targetStr = targetStr;
        }
    }
}