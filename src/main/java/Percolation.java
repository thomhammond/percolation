import edu.princeton.cs.algorithms.WeightedQuickUnionUF;

public class Percolation {
    private int gridScale;
    private int gridSize;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private boolean[] openSites;
    private int numOpenSites;


    public Percolation(int n) {
        this.gridScale = n;
        this.gridSize = n * n;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(gridSize + 2);
        this.openSites = new boolean[gridSize + 2];
        this.numOpenSites = 0;

        // Open and connect virtual sites
        this.openSites[0] = true;
        for (int i = 1; i <= gridScale; i++) {
            weightedQuickUnionUF.union(0, i);
        }
        this.openSites[gridSize + 1] = true;
        for (int j = gridSize - gridScale + 1; j <= gridSize; j++) {
            weightedQuickUnionUF.union(gridSize + 1, j);
        }

    }

    public void open(int row, int col) throws  IndexOutOfBoundsException {
        int index = xyTo1D(row, col);

        // Should this be thrown in xyTo1D method call?
        if (index < 1 || index > gridSize) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        openSites[index] = true;
        numOpenSites++;

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

    private int xyTo1D(int row, int col) {
        return (row - 1) * gridScale + col;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 1);
        percolation.open(3, 3);
    }

}
