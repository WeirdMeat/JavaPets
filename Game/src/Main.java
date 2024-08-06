import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String drawNumber(int number) {
        if (number == 0) {
            return " ".repeat(5);
        } else {
            int numberLen = String.valueOf(number).length();
            int leftSpace = (5 - numberLen) >> 1;
            int rightSpace = 5 - numberLen - leftSpace;
            return " ".repeat(leftSpace) + number + " ".repeat(rightSpace);
        }
    }

    public static void drawField(int[][] field) {
        String border = "+-----".repeat(field[0].length) + "+\n";
        StringBuilder builder = new StringBuilder();
        builder.append(border);

        for (int[] row : field) {
            for (int element: row) {
                builder.append("|");
                builder.append(drawNumber(element));
            }
            builder.append("|\n");
            builder.append(border);
        }
        System.out.print(builder);
    }

    public static List<int[]> findAllEmpties(int[][] field) {

        List<int[]> empties = new ArrayList<>();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == 0) {
                    empties.add(new int[]{i, j});
                }
            }
        }

        return empties;
    }

    public static boolean moveRowLeft(int[] row) {
        int non_zero = -1;
        int non_zero_index = -1;
        boolean moved = false;

        for (int ind = 0; ind < row.length; ind++) {
            int element = row[ind];
            if (element != 0) {
                row[ind] = 0;
                if (element == non_zero) {
                    row[non_zero_index] = element * 2;
                    non_zero = -1;
                } else {
                    row[non_zero_index+1] = element;
                    non_zero = element;
                    non_zero_index += 1;
                }
                if (row[ind] == 0) moved = true;
            }
        }

        return moved;
    }

    public static boolean move(int[][] field, String direction) {

        direction = direction.toUpperCase();

        Set<String> directions = Set.of("A", "W", "S", "D");
        if (!directions.contains(direction)) {
            System.out.println("wrong direction");
            return false;
        }

        int colLen = field.length;
        int rowLen = field[0].length;

        Map<String, int[][][]> rowsForDirection = Map.of(
                "A", new int[colLen][rowLen][2],
                "W", new int[colLen][rowLen][2],
                "D", new int[colLen][rowLen][2],
                "S", new int[colLen][rowLen][2]
        );

        for (int j = 0; j < rowLen; j++) {
            for (int i = 0; i < colLen; i++) {
                rowsForDirection.get("A")[i][j] = new int[]{i, j};
                rowsForDirection.get("W")[i][j] = new int[]{j, i};
                rowsForDirection.get("D")[i][j] = new int[]{i, rowLen - j - 1};
                rowsForDirection.get("S")[i][j] = new int[]{rowLen - j - 1, i};
            }
        }

        boolean moved = false;
        var indexesForRow = rowsForDirection.get(direction);

        //parallel the shit out of this
        for (var indexesList: indexesForRow) {
            int[] row;
            if (direction.equals("A") || direction.equals("D")) {
                row = new int[rowLen];
            } else {
                row = new int[colLen];
            }
            for (int i = 0; i < indexesList.length; i++) {
                row[i] = field[indexesList[i][0]][indexesList[i][1]];
            }
            if (moveRowLeft(row)) moved = true;
            for (int i = 0; i < indexesList.length; i++) {
                field[indexesList[i][0]][indexesList[i][1]] = row[i];
            }
        }

        return moved;
    }

    public static boolean checkEnd(int[][] field) {

        int colLen = field.length;
        int rowLen = field[0].length;

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < rowLen; j++) {
                int element = field[i][j];
                if (element == 0) return false;
                if (i > 0 && element == field[i-1][j]) return false;
                if (i < colLen-1 && element == field[i+1][j]) return false;
                if (j > 0 && element == field[i][j-1]) return false;
                if (j < rowLen-1 && element == field[i][j+1]) return false;
            }
        }
        return true;
    }

    public static void refreshField(int[][] field) {
        Random random = new Random();
        List<int[]> empties = findAllEmpties(field);
        int index = random.nextInt(empties.size());
        int[] place = empties.get(index);
        int number = random.nextBoolean() ? 2 : 4;
        field[place[0]][place[1]] = number;
    }

    public static void step(int[][] field, String direction) {
        boolean moved = move(field, direction);
        if (moved) refreshField(field);
    }

    public static int[][] startGame(int n) {
        int[][] field = new int[n][n];
        List<int[]> cords = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cords.add(new int[]{i, j});
            }
        }
        Collections.shuffle(cords);
        List<int[]> places = cords.subList(0, 2);
        field[places.get(0)[0]][places.get(0)[1]] = 2;
        field[places.get(1)[0]][places.get(1)[1]] = 2;
        return field;
    }

    public static void game() {
        System.out.println("input grid size");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        reader.nextLine();
        int[][] field = startGame(n);
        drawField(field);

        while (!checkEnd(field)) {
            step(field, reader.nextLine());
            drawField(field);
        }

        System.out.println("you won or whatever");
    }

    public static void main(String[] args) {
        game();
    }
}