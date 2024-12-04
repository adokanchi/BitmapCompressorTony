/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Tony Dokanchi
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */


    private static final int MAX_LEN = 255;
    private static final int BLOCK_LEN = 8;

    public static void compress() {
        // Assume we start with 0's, so if we start with a 1, print that we have no zeros
        boolean target = BinaryStdIn.readBoolean();
        if (target) BinaryStdOut.write(0, BLOCK_LEN);

        // count is set to 1 because we already read the first bit
        int count = 1;
        while (!BinaryStdIn.isEmpty()) {
            boolean nextBit = BinaryStdIn.readBoolean();
            if (nextBit != target) {
                // We have just read the first bit of a new run
                BinaryStdOut.write(count, BLOCK_LEN);
                target = !target;
                // Again, we set count to 1 because the first bit of the run has already been read to detect that the run has ended
                count = 1;
            }
            else {
                // The current run is continuing
                count++;
                if (count == MAX_LEN) {
                    BinaryStdOut.write(count, BLOCK_LEN);
                    // Count is set to 0 because we haven't read the first bit of the next run (and if the last run continues, we want to print a count of 0)
                    count = 0;
                    target = !target;
                }
            }
        }
        // The last run doesn't get written
        BinaryStdOut.write(count, BLOCK_LEN);

        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        boolean val = false;
        while (!BinaryStdIn.isEmpty()) {
            int count = BinaryStdIn.readInt(BLOCK_LEN);
            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(val);
            }
            val = !val;
        }

        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}