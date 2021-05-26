package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.shib.java.lib.diction.DictionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    private static final DictionService dictionService = new DictionService();
    private static final List<String> validWord = new ArrayList<String>();
    private static final int boardSize = 4;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //generate random board
        char[][] boggle = generateBoard(boardSize);
//		char boggle[][] = { { 'G', 'I', 'Z' ,'Y','O'},
//							{ 'U', 'E', 'K','Y','O' },
//							{ 'Q', 'S', 'E' ,'Y','O'},
//							{ 'G', 'I', 'Z','K','O' },
//							{ 'G', 'I', 'Z','Y','Y' }};
        System.out.println("Following words of dictionary are present");
        findWords(boggle);

    }

    private static char[][] generateBoard(int size) {
        System.out.println("Lets play Boggle!!!!!");
        Random rnd = new Random();
        char[][] board = new char[size][size];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = (char) ('a' + rnd.nextInt(26)); //Whatever value you want to set them to
                System.out.print(board[row][col] + " ");
            }
            System.out.println();

        }
        return board;
    }

    // Prints all words present in dictionary.
    private static void findWords(char[][] boggle) {
        int boggleSize = boggle.length;
        // Mark all characters as not visited
        boolean[][] visited = new boolean[boggleSize][boggleSize];

        // Initialize current string
        String str = "";

        // Consider every character and look for all words
        // starting with this character
        for (int i = 0; i < boggleSize; i++)
            for (int j = 0; j < boggleSize; j++)
                findWordsUtil(boggle, visited, i, j, str);
    }

    // A recursive function to print all words present on boggle
    private static void findWordsUtil(char[][] boggle, boolean[][] visited, int i,
                                      int j, String str) {
        // Mark current cell as visited and append current character
        // to str
        visited[i][j] = true;
        str = str + boggle[i][j];

        // If str is present in dictionary, then print it
        isWord(str);

        // Traverse adjacent cells of boggle[i][j]
        for (int row = i - 1; row <= i + 1 && row < boggle.length - 1; row++) {
            for (int col = j - 1; col <= j + 1 && col < boggle.length - 1; col++) {
                if (row >= 0 && col >= 0 && !visited[row][col])
                    findWordsUtil(boggle, visited, row, col, str);
            }
        }

        // Erase current character from string and mark visited
        // of current cell as false
        str.charAt(str.length() - 1);
        visited[i][j] = false;
    }

    // function to check if a given string is present in
    // dictionary.
    private static void isWord(String str) {
        if (!validWord.contains(str) && dictionService.getDictionWord(str) != null) {
            // considering verbs as valid words
            if (dictionService.getDictionWord(str).getDescriptions().get(0).getWordType().equalsIgnoreCase("verb" + "")) {
                validWord.add(str);
                System.out.println("Valid Word --> " + str + ", Def: " + dictionService.getDictionWord(str).getDescriptions().get(0).getDescription());
            }
        }
    }
}
