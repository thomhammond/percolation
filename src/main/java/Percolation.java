import edu.princeton.cs.algorithms.WeightedQuickUnionUF;

public class Percolation {
   private int scale;
   private int size;

   private WeightedQuickUnionUF wquuf;
   private byte[] status;

   private int numberOfOpenSites;
   private boolean percolates;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }

        scale = n;
        size = scale * scale;

        wquuf = new WeightedQuickUnionUF(size);
        status = new byte[size];

        for (int i = 0; i < size; i++) {
            if (i < scale) status[i] = 2;
            else if (i >= size - scale) status[i] = 1;
            else status[i] = 0;
        }

        numberOfOpenSites = 0;
        percolates = false;
    }

    public void open(int row, int col) {
        int index = xyToIndex(row, col);

        // Set site status to open
        status[index] = (byte) (4 | status[index]);
        numberOfOpenSites++;

        byte currentStatus = status[index];

        // Update neighbors
        if (row > 1 && isOpen(row - 1, col)) {
            int neighbor = xyToIndex(row - 1, col);

            currentStatus = updateStatus(neighbor, currentStatus);

            wquuf.union(index, neighbor);
        }
        if (row < scale && isOpen(row + 1, col)) {
            int neighbor = xyToIndex(row + 1, col);

            currentStatus = updateStatus(neighbor, currentStatus);

            wquuf.union(index, neighbor);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            int neighbor = xyToIndex(row, col - 1);

            currentStatus = updateStatus(neighbor, currentStatus);

            wquuf.union(index, neighbor);
        }
        if (col < scale && isOpen(row, col + 1)) {
            int neighbor = xyToIndex(row, col + 1);

            currentStatus = updateStatus(neighbor, currentStatus);

            wquuf.union(index, neighbor);
        }

        // Update status
        int root = wquuf.find(index);
        status[root] = (byte) (currentStatus | status[root]);

        if (status[root] == 7) {
            percolates = true;
        }
    }

    public boolean isOpen(int row, int col) {
        int index = xyToIndex(row, col);
        int root = wquuf.find(index);

        return status[root] >= 4;
    }

    public boolean isFull(int row, int col) {
        int index = xyToIndex(row, col);
        int root = wquuf.find(index);

        return status[root] >= 6;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return percolates;
    }

    private int xyToIndex(int row, int col) throws IndexOutOfBoundsException {
        int index = ((row - 1) * scale + col) - 1;

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        return index;
    }

    public int getSize() {
        return size;
    }

    private synchronized byte updateStatus(int site, byte currentStatus) {
        int root = wquuf.find(site);
        return (byte) (currentStatus | status[root]);
    }
}
