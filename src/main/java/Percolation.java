import edu.princeton.cs.algorithms.WeightedQuickUnionUF;

public class Percolation {
    private int gridScale;
    private int gridSize;

    private WeightedQuickUnionUF weightedQuickUnionUF;

    private boolean[] openSites;
    private int numOpenSites;

    private int virtualTopSite;
    private int virtualBottomSite;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }

        this.gridScale = n;
        this.gridSize = n * n;

        // Initialize with size = gridSize + 2 to account for virtual sites
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(gridSize + 2);

        // Initialize with size = gridSize + 2 to make indexing easier
        this.openSites = new boolean[gridSize + 2];

        this.numOpenSites = 0;

        this.virtualTopSite = 0;
        this.virtualBottomSite = gridSize + 1;
    }

    public void open(int row, int col) {
        // Will throw exception if row and col do not convert to a valid index
        int index = xyTo1D(row, col);

        openSites[index] = true;
        numOpenSites++;

        // If top row, connect to virtual top site
        if (row == 1) {
            weightedQuickUnionUF.union(virtualTopSite, index);
        }

        // If bottom row, connect to virtual bottom site
        if (row == gridScale) {
            weightedQuickUnionUF.union(virtualBottomSite, index);
        }

        // Connect site to open neighbors
        if (row > 1 && isOpen(row - 1, col)) {
            weightedQuickUnionUF.union(index, xyTo1D(row - 1, col));
        }
        if (row < gridScale && isOpen(row + 1, col)) {
            weightedQuickUnionUF.union(index, xyTo1D(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            weightedQuickUnionUF.union(index, index - 1);
        }
        if (col < gridScale && isOpen(row, col + 1)) {
            weightedQuickUnionUF.union(index, index + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        // Will throw exception if row and col do not convert to a valid index
        int index = xyTo1D(row, col);

        return openSites[index];
    }

    public boolean isFull(int row, int col) {
        // Will throw exception if row and col do not convert to a valid index
        int index = xyTo1D(row, col);

        boolean isOpen = isOpen(row, col);
        boolean isConnected = weightedQuickUnionUF.find(index) == weightedQuickUnionUF.find(virtualTopSite);

        return isOpen && isConnected;
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.find(virtualTopSite) == weightedQuickUnionUF.find(virtualBottomSite);
    }

    private int xyTo1D(int row, int col) throws IndexOutOfBoundsException {
        int index = (row - 1) * gridScale + col;

        if (index < 1 || index > gridSize) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        return index;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);

        percolation.open(1,3);
        percolation.open(2,3);
        percolation.open(3,3);

        percolation.isFull(3, 1);
        percolation.open(3, 1);
        percolation.isFull(3, 1);

    }

}
