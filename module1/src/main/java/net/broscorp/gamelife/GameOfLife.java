package net.broscorp.gamelife;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameOfLife {

  public void game(String fileNameInput, String fileNameOutput) {

    String[][] gameField = createGameFiledFromFile(fileNameInput);
    int iteration = Integer.parseInt(getParametersFromFile(fileNameInput).get(0).split(",")[2]);
    String[][] nextGameField = new String[gameField.length][gameField[0].length];
    while (iteration>0){
      gameIteration(gameField, nextGameField);
      iteration--;
    }
    saveResultToFile(gameField, fileNameOutput);
  }

  private void gameIteration(String[][] gameField, String[][] nextGameField) {
    int high2DArray = gameField.length;
    int width2DArray = gameField[0].length;
    for(int rowIndex = 0; rowIndex < high2DArray; rowIndex++){
      for(int columnIndex = 0; columnIndex < width2DArray; columnIndex++){
        int liveCellCount = calculateLiveCell(gameField, rowIndex, columnIndex);
//        refactor logic operation
        if ((gameField[rowIndex][columnIndex].equals("X")) && (liveCellCount < 2)) {
          nextGameField[rowIndex][columnIndex] = "O";
        } else if ((gameField[rowIndex][columnIndex].equals("X")) && (liveCellCount > 3)) {
          nextGameField[rowIndex][columnIndex] = "O";
        } else if ((gameField[rowIndex][columnIndex].equals("X")) && (liveCellCount == 3)) {
          nextGameField[rowIndex][columnIndex] = "X";
        } else {
          nextGameField[rowIndex][columnIndex] = gameField[rowIndex][columnIndex];
        }
      }
    }
  }

  private int calculateLiveCell(String[][] gameField, int rowIndex, int columnIndex) {
    int liveCellCount = 0;
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        int neighborIndexByRows = (rowIndex + x + gameField.length) % gameField.length;
        int neighborIndexByColumns = (columnIndex + y + gameField[0].length) % gameField[0].length;

        liveCellCount += gameField[neighborIndexByRows][neighborIndexByColumns].equals("X") ? 1 : 0;
      }
    }

    return liveCellCount;
  }

  private int[][] createDirectionField() {
    return new int[][]{{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};
  }

  private void saveResultToFile(String[][] gameField, String fileNameOutput) {
    String filePath = "src/test/resources/" + fileNameOutput;
    File file = new File(filePath);

    try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file))) {
      osw.write(convertArrayToString(gameField));
    } catch (IOException exception) {
      System.out.println("File not found " + exception);
    }
  }

  private String convertArrayToString(String[][] gameField) {
    StringBuilder gameFiledToStr = new StringBuilder();
    for (int row = 0; row < gameField.length; row++) {
      for (int col = 0; col < gameField[row].length; col++) {

        if (col == gameField[row].length - 1) {
          gameFiledToStr.append(gameField[row][col]);

        } else {
          gameFiledToStr.append(gameField[row][col]).append(" ");
        }

      }
      if (row < gameField.length -1) gameFiledToStr.append("\n");
    }
    return gameFiledToStr.toString();
  }


  private String[][] createGameFiledFromFile(String fileNameInput) {
    List<String> lines = getParametersFromFile(fileNameInput);

    String[] parametersFor2DArray = lines.get(0).split(",");
    int high2DArray = Integer.parseInt(parametersFor2DArray[0]);
    int width2DArray = Integer.parseInt(parametersFor2DArray[1]);

    lines.remove(0);

    String[][] field = new String[high2DArray][width2DArray];

    for (int rowIndex = 0; rowIndex < high2DArray; rowIndex++) {
      for (int columnIndex = 0;
          columnIndex < lines.get(rowIndex).split(" ").length; columnIndex++) {
        field[rowIndex][columnIndex] = lines.get(rowIndex)
            .split(" ")[columnIndex];
      }
    }

    return field;
  }

  private List<String> getParametersFromFile(String fileNameInput) {
    String filePath = "src/test/resources/" + fileNameInput;
    List<String> lines = new ArrayList<>();
    try {
      lines = Files.readAllLines(Paths.get(filePath),
          StandardCharsets.UTF_8);
    } catch (IOException exception) {
      System.out.println("IOException! File don't found " + exception);
    }
    return lines;
  }

}
