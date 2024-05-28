package minesweeper;

public class GameController {
    static Field field = new Field();
    static UserInterface ui = new UserInterface();

    public static void run() {
        int mines = ui.getMineNumber();
        field.generate(mines);
        while (true) {
            ui.printBoard(field.toString());
            if (!processUserInput()) {
                break;
            }
            if (field.calcWin()) {
                ui.printBoard(field.toString());
                ui.printCongratulations();
                break;
            }
        }
    }

    private static boolean processUserInput() {
        String input = ui.getUserInput();
        String[] parts = input.split(" ");
        int x = Integer.valueOf(parts[0]);
        int y = Integer.valueOf(parts[1]);
        if (parts[2].equals("mine")) {
            if (!sendMineMark(x, y)) {
                ui.printCellIsUncovered();
                processUserInput();
            }
        } else {
            String result = field.claimCell(x, y);
            switch (result) {
                case "true":
                    return true;
                case "false":
                    ui.printInvalidClaim();
                    processUserInput();
                    break;
                default:
                    ui.printBoard(field.toString());
                    ui.printLoss();
                    return false;
            }
        }
        return true;
    }


    private static boolean sendMineMark(int x, int y) {
        if (!field.setMark(x, y)) {
            return false;
        }
        return true;
    }
}
