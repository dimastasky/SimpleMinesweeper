package minesweeper;

        import java.util.Random;
        import java.util.Scanner;

//Класс для хранения координат
class Pair {
    private int x;
    private int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}

public class Main {
    public static void main(String[] args) {
        char[][] field = new char[9][9];
        field = createBaseField(field);
        playStandartMode(field);
    }

    static Scanner scanner = new Scanner(System.in);

    public static void playStandartMode(char[][] field) {
        int mines = setMinesCount();
        char[][] unmarkedField = createBaseField(9,9);
        printField(unmarkedField);
        int counter = mines;
        boolean gameOver = false;
        boolean isFirstMove = true;

        while (counter != 0 && gameOver != true) {
            System.out.println("Set/unset mines marks or claim a cell as free:");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            String dotStatus = scanner.next();
            if (isFirstMove==true) {
                if ((x >= 1) && (x <= unmarkedField.length + 1) && (y >= 1) && (y <= unmarkedField[0].length + 1)) {
                    field = setFieldWithMines(field, mines, x, y);
                    field = makeFieldWithHints(field);
                    //printField(field); //вывод поля с расположением мин
                    isFirstMove = false;
                }
            }
            switch (dotStatus) {
                case "free":
                    if ((x >= 1) && (x <= unmarkedField.length + 1) && (y >= 1) && (y <= unmarkedField[0].length + 1)) {
                        if(field[y - 1][x - 1] == '.') {
                            if (unmarkedField[y - 1][x - 1] == '.' || unmarkedField[y - 1][x - 1] == '*') {
                                unmarkedField[y - 1][x - 1] = '/';
                                checkField(unmarkedField,field,y-1,x-1);
                            }
                        } else if(field[y - 1][x - 1] == 'X') {
                            System.out.println("BOOM");
                            gameOver = true;
                        }
                        else {
                            checkField(unmarkedField,field,y-1,x-1);
                        }
                        break;
                    }

                case "mine":
                    if ((x >= 1) && (x <= unmarkedField.length + 1) && (y >= 1) && (y <= unmarkedField[0].length + 1)) {
                        if (unmarkedField[y - 1][x - 1] == '.') {
                            unmarkedField[y - 1][x - 1] = '*';
                            if (field[y - 1][x - 1] == 'X') {
                                --counter;
                                System.out.println("X!!!");
                            } else {
                                ++counter;
                            }
                        } else if (unmarkedField[y - 1][x - 1] == '*') {
                            unmarkedField[y - 1][x - 1] = '.';
                            if (field[y - 1][x - 1] == 'X') {
                                ++counter;
                            } else {
                                --counter;
                            }
                        } else {
                            System.out.println("There is a number here!");
                        }
                    }
                    break;
                default:
                    System.out.println("claim a cell as 'mine' or 'free'");
                    break;
            }
            printField(unmarkedField);
            System.out.println(counter);
        }
        if (counter == 0) {
            System.out.println("Congratulations! You found all the mines!");
        } else if (gameOver == true) {
            System.out.println("You stepped on a mine and failed!");
        }


    }

    private static char[][] checkField(char[][] playerField,char[][] realField, int i, int j) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x + i >= 0 && x + i < playerField.length && y + j >= 0 && y + j < playerField[0].length) {
                        if (realField[x + i][y + j] == '.' && (playerField[x + i][y + j] == '.' || playerField[x + i][y + j] == '*')) {
                            playerField[x + i][y + j] = '/';
                            checkField(playerField, realField, x + i, y + j);
                        } else if (playerField[x + i][y + j] != '/' && realField[x+i][y+j] != 'X'){
                            playerField[x + i][y + j] = realField[x + i][y + j];
                        }
                    }
                }
            }
            return playerField;
    }

    public static void playWithHints(char[][] field, int mines) {
        char[][] unmarkedField = new char[field.length][field[0].length];
        unmarkedField = makeFieldCopy(field);
        doNotShowMines(unmarkedField, mines);
        int counter = mines;
        while (counter != 0) {
            printField(unmarkedField);
            System.out.println("Set/delete mines marks (x and y coordinates): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if ((x >= 1) && (x <= unmarkedField.length + 1) && (y >= 1) && (y <= unmarkedField[0].length + 1)) {
                System.out.println("____");
                printField(field);
                System.out.println("____");
                if (unmarkedField[y - 1][x - 1] == '.') {
                    unmarkedField[y - 1][x - 1] = '*';
                    if (field[y - 1][x - 1] == 'X') {
                        --counter;
                        //System.out.println("X!!!");
                    } else {
                        ++counter;
                    }
                } else if (unmarkedField[y - 1][x - 1] == '*') {
                    unmarkedField[y - 1][x - 1] = '.';
                    if (field[y - 1][x - 1] == 'X') {
                        ++counter;
                    } else {
                        --counter;
                    }
                } else {
                    System.out.println("There is a number here!");
                }
            }
        }
        System.out.println("Congratulations! You found all the mines!");
    }

    private static char[][] doNotShowMines(char[][] field, int mines) {
        char[][] copyField = field.clone();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (copyField[i][j] == 'X') {
                    copyField[i][j] = '.';
                }
            }
        }
        return copyField;
    }

    private static Pair[] randomiseMinesCoordinates(char[][] field, int mines) {
        int counter = 0;
        Pair[] mineCoordinate = new Pair[mines];
        Random random = new Random();
        char[][] copyField = field;
        for (int i = 0; i < mines; i++) {
            int x = random.nextInt(field.length);
            int y = random.nextInt(field[0].length);
            while (copyField[x][y] == 'X') {
                x = random.nextInt(field.length);
                y = random.nextInt(field[0].length);
            }
            copyField[x][y] = 'X';
            mineCoordinate[counter++] = new Pair(x, y);
        }
        return mineCoordinate;
    }
    private static Pair[] randomiseMinesCoordinates(char[][] field, int mines, int notX, int notY) {
        int counter = 0;
        Pair[] mineCoordinate = new Pair[mines];
        Random random = new Random();
        char[][] copyField = field;
        for (int i = 0; i < mines; i++) {
            int x = random.nextInt(field.length);
            int y = random.nextInt(field[0].length);
            while (copyField[x][y] == 'X' || (x == notX && y == notY) ) {
                x = random.nextInt(field.length);
                y = random.nextInt(field[0].length);
            }
            copyField[x][y] = 'X';
            mineCoordinate[counter++] = new Pair(x, y);
        }
        return mineCoordinate;
    }

    public static char[][] setFieldWithMines(char[][] field, int mines) {
        Pair[] mineCoords = randomiseMinesCoordinates(field, mines);
        for (int i = 0; i < mines; i++) {
            field[mineCoords[i].getX()][mineCoords[i].getY()] = 'X';
        }
        return field;
    }
    public static char[][] setFieldWithMines(char[][] field, int mines, int notX, int notY) {
        Pair[] mineCoords = randomiseMinesCoordinates(field, mines, notX, notY);
        for (int i = 0; i < mines; i++) {
            field[mineCoords[i].getX()][mineCoords[i].getY()] = 'X';
        }
        return field;
    }

    public static void printField(char[][] field) {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < field.length; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < field[0].length; j++) {
                System.out.print(field[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    private static char[][] createBaseField(int x, int y) {
        char[][] field = new char[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                field[i][j] = '.';
            }
        }
        return field;
    }
    private static char[][] createBaseField(char[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = '.';
            }
        }
        return field;
    }

    private static char[][] makeFieldWithHints(char[][] field) {
        char[][] fieldWithHints = field;
        int[][] minesAroundCounter = new int[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                minesAroundCounter[i][j] = countMinesNear(fieldWithHints, i, j);
                if (field[i][j] != 'X' && minesAroundCounter[i][j] != 0) {
                    fieldWithHints[i][j] = (char) (minesAroundCounter[i][j] + '0');
                }
            }
        }
        return fieldWithHints;
    }

    private static int countMinesNear(char[][] field, int i, int j) {
        int mines = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x + i >= 0 && x + i < field.length && y + j >= 0 && y + j < field[0].length) {
                    if (field[x + i][y + j] == 'X') {
                        mines++;
                    }
                }
            }
        }
        return mines;
    }

    private static char[][] makeFieldCopy(char[][] field) {
        char[][] copyField = new char[9][9];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                copyField[i][j] = field[i][j];
            }
        }
        return copyField;
    }

    private static int setMinesCount() {
        System.out.print("How many mines do you want on the field?  ");
        int mines = scanner.nextInt();
        return mines;
    }
}