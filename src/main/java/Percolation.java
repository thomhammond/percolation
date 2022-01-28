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
        this.openSites = new boolean[gridSize + 2];

        this.numOpenSites = 0;

        this.virtualTopSite = 0;
        this.virtualBottomSite = gridSize + 1;

        // Open and connect virtual top site
        this.openSites[virtualTopSite] = true;
        for (int i = 1; i <= gridScale; i++) {
            weightedQuickUnionUF.union(virtualTopSite, i);
        }

        // Open and connect virtual bottom site
        this.openSites[virtualBottomSite] = true;
        for (int j = gridSize - gridScale + 1; j <= gridSize; j++) {
            weightedQuickUnionUF.union(virtualBottomSite, j);
        }

    }

    public void open(int row, int col) {
        // Will throw exception if row and col do not convert to a valid index
        int index = xyTo1D(row, col);

        openSites[index] = true;
        numOpenSites++;

        // If site is on the border of the grid, ignore neighbor indices outside of grid bounds
        int above = index <= gridScale ? -1 : xyTo1D(row - 1, col);
        int below = index > gridSize - gridScale ? -1 : xyTo1D(row + 1, col);
        int left = index % gridScale == 1 ? -1 : index - 1;
        int right = index % gridScale == 0 ? -1 : index + 1;

        if (above > 0 && openSites[above]) {
            weightedQuickUnionUF.union(above, index);
        }
        if (below > 0 && openSites[below]) {
            weightedQuickUnionUF.union(below, index);
        }
        if (left > 0 && openSites[left]) {
            weightedQuickUnionUF.union(index, left);
        }
        if (right > 0 && openSites[right]) {
            weightedQuickUnionUF.union(index, right);
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

        System.out.println();
        System.out.println("System should NOT percolate");
        System.out.println("System percolates: " + percolation.percolates());

        System.out.println();
        System.out.println("Row 1, Col 1 should be closed");
        System.out.println("Row 1, Col 1 is open: " + percolation.isOpen(1, 1));

        percolation.open(1, 1);
        System.out.println();
        System.out.println("Row 1, Col 1 should be open");
        System.out.println("Row 1, Col 1 is open: " + percolation.isOpen(1, 1));

        System.out.println();
        System.out.println("Row 2, Col 2 should be closed");
        System.out.println("Row 2, Col 2 is open: " + percolation.isOpen(2, 2));

        percolation.open(2, 2);
        System.out.println();
        System.out.println("Row 2, Col 2 should be open");
        System.out.println("Row 2, Col 2 is open: " + percolation.isOpen(2, 2));

        System.out.println();
        System.out.println("System should NOT percolate");
        System.out.println("System percolates: " + percolation.percolates());

        System.out.println();
        System.out.println("Row 3, Col 2 should be closed");
        System.out.println("Row 3, Col 2 is open: " + percolation.isOpen(3, 2));

        percolation.open(3, 2);
        System.out.println();
        System.out.println("Row 3, Col 2 should be open");
        System.out.println("Row 3, Col 2 is open: " + percolation.isOpen(3, 2));

        System.out.println();
        System.out.println("Row 2, Col 1 should be closed");
        System.out.println("Row 2, Col 1 is open: " + percolation.isOpen(2, 1));

        percolation.open(2, 1);
        System.out.println();
        System.out.println("Row 2, Col 1 should be open");
        System.out.println("Row 2, Col 1 is open: " + percolation.isOpen(2, 1));

        System.out.println();
        System.out.println("System should percolate");
        System.out.println("System percolates: " + percolation.percolates());
    }

}
