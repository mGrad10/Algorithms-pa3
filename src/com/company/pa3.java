/* Algorithms PA3
 * Taylor Coury and Melinda Grad
 * This program finds all ways to parenthesize a string
 * such that it equates to a target character based off of
 * a given multiplication table
 */

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
        char[][] multTable  = new char[n][n];
        for(int i = 0; i < n; i++){
            String temp = scanner.nextLine();
            for(int j = 0; j < n; j++){
                multTable[i][j] = temp.charAt(j);
            }
        }
        //Get result string
        String ogString = scanner.nextLine();

        //Get target result char
        String temp = scanner.nextLine();
        char target = temp.charAt(0);

        //Table of subproblems
        ArrayList<ParenVal>[][] memoTable =
                (ArrayList<ParenVal>[][]) new ArrayList[ogString.length()][ogString.length()];

        //Initialize the memoization table
        initBaseCases(alphabet, multTable, memoTable, ogString.length() , ogString);

        //Dynamically fill the memoization table
        dynamicFill(ogString,alphabet,multTable,memoTable);

        ArrayList<String> solution = new ArrayList<String>();

        //Checking if string can be parenthesized to equal target
        solution = checkTarget(memoTable, target, ogString.length());

        //Print to file
        if(solution.size() == 0)
            pw.println("Not possible");
        else{
            for(int i = 0; i < solution.size(); i++)
                pw.println(solution.get(i));
        }

        pw.close();
    }

    /* Method to check if string can be parenthesized to equal target string
    * @param memoTable the memoization table to fill
    * @param target the target string
    * @param n the length of the original string
    * @return checkTarget the list of parenthesized strings that equal the target
    */
    public static ArrayList<String> checkTarget(ArrayList<ParenVal>[][] memoTable, char target, int n){
        ArrayList<String> hit = new ArrayList<String>();

        for(int i = 0; i < memoTable[0][n-1].size(); i++){
            if(memoTable[0][n-1].get(i).getValueStr() == target){
                //Add the first parenthesized string
                if(hit.size() == 0){
                    hit.add(memoTable[0][n-1].get(i).getParenStr());
                }
                //If more than one -- find index for lexo sort
                else{
                    int index = -1; //placement of new paren
                    for(int j = 0; j < hit.size();j++){
                        int temp = memoTable[0][n-1].get(i).getParenStr().compareTo(hit.get(j));

                        if(temp > 0){ //Found index
                            index = j;
                            break;
                        }
                    }
                    if(index == -1){ //Put at end of sorted list
                        hit.add(memoTable[0][n-1].get(i).getParenStr());
                    }
                    else{ //Put at index found
                        hit.add(index, memoTable[0][n-1].get(i).getParenStr());
                    }
                }
            }
        }
        return hit;
    }

    /* Method to dynamically fill the memoization table
    * @param ogString original string to process
    * @param alphabet array with the alphabet
    * @param multTable array used to lookup multiplication value
    * @param memoTable the memoization table to fill
    */
    public static void dynamicFill(String ogString, char[] alphabet, char[][]multTable,
                                   ArrayList<ParenVal>[][] memoTable){
        //dist = end - start
        for(int dist = 2; dist < ogString.length(); dist++){
            for(int start = 0; start < ogString.length()-dist; start++){
                int end = dist + start;

                //Create new entry for table
                ArrayList<ParenVal> currProb = new ArrayList<ParenVal>();

                //Iterate over all possible ways to parenthesize the string
                for(int k = 1; k < dist + 1; k++){

                    //break into 2 subproblems based off k
                    ArrayList<ParenVal> firstSubprob = memoTable[start][start + k-1];
                    ArrayList<ParenVal> secondSubprob = memoTable[start + k][end];

                    for(int l = 0 ; l < firstSubprob.size(); l++){
                        for(int m = 0; m < secondSubprob.size(); m++){

                            String parenStr;
                            char valueStr;

                            //First letter by itself
                            if(k == 1){
                                parenStr = firstSubprob.get(l).getParenStr() + "(" +
                                        secondSubprob.get(m).getParenStr() + ")";

                            }
                            //Last letter by itself
                            else if(k == dist){
                                parenStr = "(" + firstSubprob.get(l).getParenStr() + ")"
                                        + secondSubprob.get(m).getParenStr();
                            }
                            else{
                                parenStr = "(" + firstSubprob.get(l).getParenStr() + ")("
                                        + secondSubprob.get(m).getParenStr() + ")";
                            }
                            valueStr = multiply(alphabet, multTable, firstSubprob.get(l).getValueStr(),
                                    secondSubprob.get(m).getValueStr());

                            //Create new entry into list of possible parens
                            ParenVal newParenVal = new ParenVal(parenStr, valueStr);
                            currProb.add(newParenVal);
                        }
                    }
                }
                memoTable[start][end] = currProb; //put element into memo table
            }
        }
    }

    /*Method to get value result of multiplication from multiplication table
    * @param alphabet array used to lookup value
    * @param multTable array used to lookup multiplication value
    * @param firstChar first char in multiplication
    * @param SecondChar second char in multiplication
    * @return char value of result of multiplication
    */
    public static char multiply(char[] alphabet, char[][] multTable, char firstChar, char secondChar) {

        int firstIndex = -1;
        int secondIndex = -1;
        //Find index corresponding to first letter
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == firstChar){
                firstIndex = i;
                break;
            }
        }
        //Find index corresponding to second letter
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == secondChar){
                secondIndex = i;
                break;
            }
        }
        return multTable[firstIndex][secondIndex];
    }
    /* Method to init the memoization table
    * @param memoTable the memoization table to init
    * @param n number of characters in the alphabet
    * @param result String to add parens to
    */
    public static void initBaseCases(char[] alphabet, char[][]multTable, ArrayList<ParenVal>[][] memoTable, int n, String ogString){
        //Init the memo table with Opts
        for(int i = 0; i < n ; i++){
            for(int j = 0; j < n; j++){
                memoTable[i][j] = new ArrayList<ParenVal>();
            }
        }
        //Init base cases when subproblem has 1 letter
        for(int i = 0; i < n; i++){
            ParenVal parenVal = new ParenVal(Character.toString(ogString.charAt(i)), ogString.charAt(i));
            memoTable[i][i].add(parenVal);

        }
        //Init base cases when subproblem has 2 letters
        for(int i = 0; i < n-1; i++){
            int j = i + 1;

            ParenVal parenVal = new ParenVal(ogString.substring(i, j+1),
                    multiply(alphabet, multTable,ogString.charAt(i),ogString.charAt(j)));
            memoTable[i][j].add(parenVal);
        }
    }

    /* Class to represent sub problems */
    public static class ParenVal{
        String parenStr;
        char valueStr;

        public ParenVal(String parenStr, char targetStr) {
            this.parenStr = parenStr;
            this.valueStr = targetStr;
        }
        public String getParenStr() {return parenStr;}
        public void setParenStr(String parenStr) { this.parenStr = parenStr;}
        public char getValueStr() { return valueStr;}
        public void setValueStr(char targetStr) { this.valueStr = targetStr;}
    }
}