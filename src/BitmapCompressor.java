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
 *  @author YOUR NAME HERE
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */


    private static final int MAX_BLOCK_LEN = 127;

    public static void compress() {
        boolean nextBit = BinaryStdIn.readBoolean();
        boolean target = nextBit;
        while (!BinaryStdIn.isEmpty()) {
            int count = 0;
            while (count < MAX_BLOCK_LEN) {
                if (nextBit != target) {
                    break;
                }
                count++;
                if (BinaryStdIn.isEmpty()) break;
                nextBit = BinaryStdIn.readBoolean();
            }

            BinaryStdOut.write(target);
            BinaryStdOut.write(count, 7);

            if (!BinaryStdIn.isEmpty()) target = nextBit;
        }

        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        while (!BinaryStdIn.isEmpty()) {
            boolean bit = BinaryStdIn.readBoolean();
            int count = BinaryStdIn.readInt(7);
            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(bit);
            }
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