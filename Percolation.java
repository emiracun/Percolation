import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSize; // size of the each side of the grid
    private int gridSquared; // size of squared of the grid
    private boolean[][] grid; // a 2D array to represent open or blocked sites in the grid
    private WeightedQuickUnionUF wquFind;
    private int virtualTop;
    private int virtualBottom;


    // constructor
    public Percolation(int a) {
        gridSize = a;
        gridSquared = a * a;
        grid = new boolean[a][a];
        wquFind = new WeightedQuickUnionUF(gridSquared + 2); // additional 2 virtual sites (top and bottom)
        virtualTop = gridSquared;
        virtualBottom = gridSquared + 1;

        // connection virtual top with the top row
        for (int col = 0; col < a; col++) {
            wquFind.union(virtualTop, col);
        }

        // connection virtual bottom with the bottom row
        for (int col = 0; col < a; col++) {
            wquFind.union(virtualBottom, (a - 1) * a + col);
        }
    }

    // openSite method 
    public void openSite(int row, int col) {
        if (!grid[row][col]) {
            grid[row][col] = true;

            
            // connect to virtual top if at the top row
            if (row == 0) {
                wquFind.union(virtualTop, col);
            }

            // connect to virtual bottom if at the bottom row
            if (row == gridSize - 1) {
                wquFind.union(virtualBottom, (gridSize - 1) * gridSize + col);
            }

            if (row > 0 && grid[row - 1][col]) {
                wquFind.union(row * gridSize + col, (row - 1) * gridSize + col);
            }

            if (row < gridSize - 1 && grid[row + 1][col]) {
                wquFind.union(row * gridSize + col, (row + 1) * gridSize + col);
            }

            if (col > 0 && grid[row][col - 1]) {
                wquFind.union(row * gridSize + col, row * gridSize + col - 1);
            }

            if (col < gridSize - 1 && grid[row][col + 1]) {
                wquFind.union(row * gridSize + col, row * gridSize + col + 1);
            }
        }
    }

   // openAllSites method to open each side with probability (p)
    public void openAllSites(double p) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (StdRandom.uniform() < p) {
                    openSite(row, col);
                }
            }
        }
    }

    // percolationCheck method
    public boolean percolationCheck() {
        return wquFind.connected(virtualTop, virtualBottom);
    }


    // displayGrid method
    public void displayGrid(double xOffset, double yOffset, double canvasWidth, double canvasHeight) {
        StdDraw.setXscale(0, canvasWidth);
        StdDraw.setYscale(0, canvasHeight);

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col]) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                } else {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }

                double sSize = canvasWidth / (gridSize * 2);

                double x = xOffset + col * sSize;
                double y = yOffset + (gridSize - 1 - row) * sSize;

                StdDraw.filledSquare(x + sSize / 2, y + sSize / 2, sSize / 2);
            }
        }
        StdDraw.show();
    }


    // main method
    public static void main(String[] args) {
        // parameters for grid layout and visualization
        int gridSize = 5;
        int numOfColumns = 2;
        int numOfRows = 5;
        int panelSize = 300;
        double space = 10; 
        
        // calculate the size of each square in the grid
        double sSize = (panelSize - (numOfColumns - 1) * space) / (numOfColumns * gridSize);

        double totalCanvasWidth = numOfColumns * gridSize * sSize + (numOfColumns - 1) * space;
        double totalCanvasHeight = numOfRows * gridSize * sSize + (numOfRows - 1) * space;
        
        StdDraw.setCanvasSize((int) totalCanvasWidth, (int) totalCanvasHeight);
        StdDraw.setXscale(0, totalCanvasWidth);
        StdDraw.setYscale(0, totalCanvasHeight);
        
        int percolateNumber = 1;
        
        // loop through rows and columns to simulate and display multiple percolation situations
        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfColumns; col++) {
                Percolation percolation = new Percolation(gridSize);
                
                double xOffset = col * (gridSize * sSize + space);
                double yOffset = (numOfRows - 1 - row) * (gridSize * sSize + space);
                
                percolation.openAllSites(0.5);
                percolation.displayGrid(xOffset, yOffset, totalCanvasWidth, totalCanvasHeight);
                System.out.println("Percolate" + percolateNumber + ": " + percolation.percolationCheck());

                percolateNumber++;
            }
        }
    }
}