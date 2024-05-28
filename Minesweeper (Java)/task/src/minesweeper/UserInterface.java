package minesweeper;

import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void printBoard(String board) {
        System.out.println(board);
    }

    public void printCongratulations() {
        System.out.println("Congratulations! You found all the mines!");
    }

    public void printCellIsUncovered() {
        System.out.println("You cannot mark an uncovered cell!");
    }

    public void printInvalidClaim() {
        System.out.println("You cannot claim an uncovered/marked cell!");
    }

    public void printLoss() {
        System.out.println("You stepped on a mine and failed!");
    }

    public String getUserInput() {
        String input;
        while (true) {
            System.out.println("Set/unset mine marks or claim a cell as free:");
            input = this.scanner.nextLine();
            if (!input.matches("[1-9] [1-9] (free|mine)")) {
                System.out.println("Incorrect input!");
            } else {
                break;
            }
        }
        return input;
    }

    public int getMineNumber() {
        while (true) {
            System.out.println("How many mines do you want on the field?");
            String input = this.scanner.nextLine();
            if (!isNumeric(input)) {
                System.out.println("Input a number!");
                continue;
            }
            int mines = Integer.valueOf(input);
            if (mines > 81) {
                System.out.println("Too many mines!");
                continue;
            }
            return mines;
        }
    }

    public boolean isNumeric(String value) {
        if (value == null) {
            return false;
        }
        try {
            int num = Integer.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
