import java.util.Random;

public class MatrixMultiplier {
    public static void main(String[] args) {
        int rowsA = 30, colsA = 30;
        int rowsB = 30, colsB = 30;

        int[][] matrixA = generateRandomMatrix(rowsA, colsA);
        int[][] matrixB = generateRandomMatrix(rowsB, colsB);

        long startTime = System.currentTimeMillis();

        int[][] resultMatrix = multiplyMatrices(matrixA, matrixB);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

	System.out.println("Matrix A:");
	printMatrix(matrixA);
	System.out.println("Matrix B:");
	printMatrix(matrixB);

        System.out.println("Resultant Matrix:");
        printMatrix(resultMatrix);
        System.out.println("Execution time for sequential multiplication: " + executionTime + " milliseconds");
    }

    public static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    public static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;

        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
