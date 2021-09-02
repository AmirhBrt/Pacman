package model;

import java.util.Arrays;
import java.util.Random;

public class Map {

    private Cell[][] map;
    private final int [][] savedMap;
    private final int rowNum;
    private final int columnNum;

    public Map(int rowNum, int columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.savedMap = new int[2*columnNum + 1][2*rowNum+1];
        this.map = generateMap(rowNum, columnNum);
    }

    public Cell[][] getMap() {
        if (map == null)
            loadMap();
        return map;
    }

    public void setMap(Cell[][] map) {
        this.map = map;
    }

    private void loadMap() {
        Cell[][] maze = initializeMaze(rowNum , columnNum);
        for (int i = 0; i < columnNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                if (savedMap[2*i+2][2*j+1] == 0){
                    maze[i][j].setHasBottomBorder(false);
                } if (savedMap[2*i][2*j+1] == 0){
                    maze[i][j].setHasTopBorder(false);
                } if (savedMap[2*i+1][2*j+2] == 0){
                    maze[i][j].setHasRightBorder(false);
                } if (savedMap[2*i+1][2*j] == 0){
                    maze[i][j].setHasLeftBorder(false);
                }
            }
        }
        setMap(maze);
    }

    private Cell[][] generateMap(int maxRows, int maxColumns) {
        Cell[][] maze = initializeMaze(maxRows, maxColumns);
        Cell[] visitedCells = new Cell[maxRows * maxColumns];
        visitedCells[0] = maze[0][0];
        visitedCells[0].setVisited(true);
        generateMaze(maze, visitedCells, 0, 0);
        deleteRandomWalls(maxRows, maxColumns, maze);
        saveIntegerMap(maxRows, maxColumns, maze);
        return maze;
    }

    private void saveIntegerMap(int maxRows, int maxColumns, Cell[][] maze) {
        for (Cell[] cells : maze)
            for (int j = 0; j < maze[0].length; j++) {
                cells[j].setVisited(false);
            }
        for (int i = 0; i < 2* maxColumns +1 ; i++){
            for (int j = 0; j < 2* maxRows + 1; j++) {
                savedMap[i][j]  = 1;
            }
        }
        for (int i = 0; i < maxColumns; i++) {
            for (int j = 0; j < maxRows; j++) {
                savedMap[2*i+1][2*j+1] = 0;
                if (!maze[i][j].isHasBottomBorder()){
                    savedMap[2*i+2][2*j+1] = 0;
                } if (!maze[i][j].isHasTopBorder()){
                    savedMap[2*i][2*j+1] = 0;
                } if (!maze[i][j].isHasRightBorder()){
                    savedMap[2*i+1][2*j+2] = 0;
                } if (!maze[i][j].isHasLeftBorder()){
                    savedMap[2*i+1][2*j] = 0;
                }
            }
        }
    }

    private void deleteRandomWalls(int rows, int columns, Cell[][] maze) {
        Random random = new Random();
        int i = random.nextInt(rows * columns);
        i = i / 2;
        for (int j = i; j > 0; j--) {
            deleteWall(rows, columns, maze);
        }
    }

    private void deleteWall(int rows, int columns, Cell[][] maze) {
        Random randomRow = new Random();
        int xPlace = randomRow.nextInt(rows) % rows;
        int yPlace = randomRow.nextInt(columns) % columns;
        if (yPlace == 0) {
            maze[yPlace][xPlace].setHasBottomBorder(false);
            maze[yPlace + 1][xPlace].setHasTopBorder(false);
        } else if (yPlace == columns - 1) {
            maze[yPlace][xPlace].setHasTopBorder(false);
            maze[yPlace - 1][xPlace].setHasBottomBorder(false);
        } else {
            maze[yPlace][xPlace].setHasTopBorder(false);
            maze[yPlace][xPlace].setHasBottomBorder(false);
            maze[yPlace - 1][xPlace].setHasBottomBorder(false);
            maze[yPlace + 1][xPlace].setHasTopBorder(false);
        }
        if (xPlace == 0) {
            maze[yPlace][xPlace].setHasRightBorder(false);
            maze[yPlace][xPlace + 1].setHasLeftBorder(false);
        } else if (xPlace == rows - 1) {
            maze[yPlace][xPlace].setHasLeftBorder(false);
            maze[yPlace][xPlace - 1].setHasRightBorder(false);
        } else {
            maze[yPlace][xPlace].setHasRightBorder(false);
            maze[yPlace][xPlace].setHasLeftBorder(false);
            maze[yPlace][xPlace + 1].setHasLeftBorder(false);
            maze[yPlace][xPlace - 1].setHasRightBorder(false);
        }
    }

    private void generateMaze(Cell[][] map, Cell[] visitedCells, int n, int generateCounter) {
        int currentRow = visitedCells[n].getxPlace();
        int currentColumn = visitedCells[n].getyPlace();
        if (n == 0) {
            if (generateCounter != 0) return;
        }
        int nextCell = findCellToMove(currentRow, currentColumn, map);
        if (nextCell == -1)
            generateMaze(map, visitedCells, n - 1, generateCounter + 1);
        else
            switch (nextCell) {
                case 1://move up
                    moveUp(currentRow, currentColumn, map, visitedCells, n);
                    generateMaze(map, visitedCells, n + 1, generateCounter + 1);
                    break;
                case 2://move right
                    moveRight(currentRow, currentColumn, map, visitedCells, n);
                    generateMaze(map, visitedCells, n + 1, generateCounter + 1);
                    break;
                case 3://move down
                    moveDown(currentRow, currentColumn, map, visitedCells, n);
                    generateMaze(map, visitedCells, n + 1, generateCounter + 1);
                    break;
                case 0://move left
                    moveLeft(currentRow, currentColumn, map, visitedCells, n);
                    generateMaze(map, visitedCells, n + 1, generateCounter + 1);
                    break;
            }
    }

    private int findCellToMove(int currentRow, int currentColumn, Cell[][] map) {
        int[] randomNumbers = new int[]{0, 1, 2, 3};
        shuffle(randomNumbers);
        boolean isOk = false;
        int i;
        for (i = 0; i < randomNumbers.length; i++) {
            if (checkPossible(randomNumbers[i], currentRow, currentColumn, map)) {
                isOk = true;
                break;
            }
        }
        if (!isOk) return -1;
        else return randomNumbers[i];
    }

    private boolean checkPossible(int number, int currentRow, int currentColumn, Cell[][] map) {
        if (number == 0)
            return checkLeftCell(currentRow, currentColumn, map);
        else if (number == 1)
            return checkUpCell(currentRow, currentColumn, map);
        else if (number == 2)
            return checkRightCell(currentRow, currentColumn, map);
        else
            return checkDownCell(currentRow, currentColumn, map);
    }

    private void shuffle(int[] array) {
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < array.length; i++) {
            int change = i + random.nextInt(array.length - i);
            int temp = array[i];
            array[i] = array[change];
            array[change] = temp;
        }
    }

    private Cell[][] initializeMaze(int rowNum, int columnNum) {
        Cell[][] map = new Cell[columnNum][rowNum];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new Cell(i, j);
            }
        return map;
    }

    private boolean checkLeftCell(int currentRow, int currentColumn, Cell[][] map) {
        if (currentColumn == 0)
            return false;
        else
            return map[currentRow][currentColumn - 1].isVisited();
    }

    private void moveLeft(int currentRow, int currentColumn, Cell[][] map, Cell[] visitedCells, int n) {
        map[currentRow][currentColumn].setHasLeftBorder(false);
        map[currentRow][currentColumn - 1].setHasRightBorder(false);
        visitedCells[n + 1] = map[currentRow][currentColumn - 1];
        visitedCells[n + 1].setVisited(true);
    }

    private boolean checkDownCell(int currentRow, int currentColumn, Cell[][] map) {
        if (currentRow == map.length - 1)
            return false;
        else
            return map[currentRow + 1][currentColumn].isVisited();
    }

    private void moveDown(int currentRow, int currentColumn, Cell[][] map, Cell[] visitedCells, int n) {
        map[currentRow][currentColumn].setHasBottomBorder(false);
        map[currentRow + 1][currentColumn].setHasTopBorder(false);
        visitedCells[n + 1] = map[currentRow + 1][currentColumn];
        visitedCells[n + 1].setVisited(true);
    }

    private boolean checkRightCell(int currentRow, int currentColumn, Cell[][] map) {
        if (currentColumn == map[0].length - 1)
            return false;
        else
            return map[currentRow][currentColumn + 1].isVisited();
    }

    private void moveRight(int currentRow, int currentColumn, Cell[][] map, Cell[] visitedCells, int n) {
        map[currentRow][currentColumn].setHasRightBorder(false);
        map[currentRow][currentColumn + 1].setHasLeftBorder(false);
        visitedCells[n + 1] = map[currentRow][currentColumn + 1];
        visitedCells[n + 1].setVisited(true);
    }

    private boolean checkUpCell(int currentRow, int currentColumn, Cell[][] map) {
        if (currentRow == 0)
            return false;
        else
            return map[currentRow - 1][currentColumn].isVisited();
    }

    private void moveUp(int currentRow, int currentColumn, Cell[][] map, Cell[] visitedCells, int n) {
        map[currentRow][currentColumn].setHasTopBorder(false);
        map[currentRow - 1][currentColumn].setHasBottomBorder(false);
        visitedCells[n + 1] = map[currentRow - 1][currentColumn];
        visitedCells[n + 1].setVisited(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Map map1 = (Map) o;
        return Arrays.equals(map, map1.map);
    }
}
