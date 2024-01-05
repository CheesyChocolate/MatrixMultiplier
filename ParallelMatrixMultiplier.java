import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class ParallelMatrixMultiplier {
	public static void main(String[] args) {
		int rowsA = 30, colsA = 30;
		int rowsB = 30, colsB = 30;
		int numThreads = 6;

		int[][] matrixA = generateRandomMatrix(rowsA, colsA);
		int[][] matrixB = generateRandomMatrix(rowsB, colsB);

		long startTime = System.currentTimeMillis();

		int[][] resultMatrix = parallelMultiplyMatrices(matrixA, matrixB, numThreads);

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		System.out.println("Matrix A:");
		printMatrix(matrixA);
		System.out.println("Matrix B:");
		printMatrix(matrixB);

		System.out.println("Resultant Matrix:");
		printMatrix(resultMatrix);
		System.out.println("Execution time for parallel multiplication with " + numThreads + " threads: " + executionTime + " milliseconds");
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

	public static int[][] parallelMultiplyMatrices(int[][] matrixA, int[][] matrixB, int numThreads) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int rowsB = matrixB.length;
		int colsB = matrixB[0].length;

		if (colsA != rowsB) {
			throw new IllegalArgumentException("Matrices cannot be multiplied due to incompatible dimensions.");
		}

		int[][] result = new int[rowsA][colsB];
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		int chunkSize = rowsA / numThreads;

		List<MatrixMultiplierThread> threads = new ArrayList<>();

		for (int i = 0; i < numThreads; i++) {
			int start = i * chunkSize;
			int end = (i == numThreads - 1) ? rowsA : start + chunkSize;

			MatrixMultiplierThread thread = new MatrixMultiplierThread(matrixA, matrixB, result, start, end);
			threads.add(thread);
			executor.execute(thread);
		}

		executor.shutdown();

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.err.println("Thread execution interrupted: " + e.getMessage());
		}

		return result;
	}

	static class MatrixMultiplierThread implements Runnable {
		private final int[][] matrixA;
		private final int[][] matrixB;
		private final int[][] result;
		private final int startRow;
		private final int endRow;

		public MatrixMultiplierThread(int[][] matrixA, int[][] matrixB, int[][] result, int startRow, int endRow) {
			this.matrixA = matrixA;
			this.matrixB = matrixB;
			this.result = result;
			this.startRow = startRow;
			this.endRow = endRow;
		}

		@Override
		public void run() {
			for (int i = startRow; i < endRow; i++) {
				for (int j = 0; j < matrixB[0].length; j++) {
					for (int k = 0; k < matrixA[0].length; k++) {
						result[i][j] += matrixA[i][k] * matrixB[k][j];
					}
				}
			}
		}
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
