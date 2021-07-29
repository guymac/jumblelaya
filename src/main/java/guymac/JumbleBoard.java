/**
 * 
 */
package guymac;

import java.util.Random;

/**
 * Keeps track of the board.
 */
public class JumbleBoard
{
    /** Default number of rows */
    protected int rows = 4;

    /** Default number of columns */
    protected int cols = 4;

    /** RNG */
    protected Random random = new Random();

    /** Dice */
    private char[][] dice =
    {
            {
                    'a', 'a', 'e', 'e', 'n', 'g'
            },
            {
                    'e', 'l', 'd', 'r', 'x', 'i'
            },
            {
                    'e', 'e', 'u', 's', 'i', 'n'
            },
            {
                    's', 'e', 'i', 't', 's', 'o'
            },
            {
                    'a', 'c', 's', 'o', 'p', 'h'
            },
            {
                    'h', 't', 'u', 'r', 'w', 'e'
            },
            {
                    'w', 'o', 'o', 't', 'a', 't'
            },
            {
                    'n', 'e', 'e', 'h', 'w', 'g'
            },
            {
                    'n', 'h', 'n', 'l', 'r', 'z'
            },
            {
                    'c', 'o', 'u', 'm', 't', 'i'
            },
            {
                    't', 'r', 'e', 't', 'y', 'l'
            },
            {
                    'y', 'e', 'd', 'l', 'r', 'v'
            },
            {
                    'b', 'b', 'a', 'o', 'j', 'o'
            },
            {
                    's', 'y', 'i', 'd', 't', 't'
            },
            {
                    'f', 'a', 'k', 'p', 'f', 's'
            },
            {
                    'q', 'u', 'm', 'h', 'n', 'i'
            }
    };

    /**
     * Create a new board with defaults
     * 
     */
    public JumbleBoard()
    {
    }

    /**
     * Initialize with an X by Y array of dice
     * 
     * @param dice Unrandomized set of JoggleDie
     * @param rows number of rows
     * @param cols number of cols
     */
    public JumbleBoard(char[][] dice, int rows, int cols)
    {
        this.dice = dice;
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Initialize with a string, which must have rows x cols number of chars.
     * 
     * @param values
     * @param rows
     * @param cols
     */
    public JumbleBoard(String values, int rows, int cols)
    {
        if (values.length() != rows * cols)
        {
            throw new IllegalArgumentException(String.format(
                    "'%s' must have %d x %d = %d characters", values, rows, cols, rows * cols));
        }

        this.rows = rows;
        this.cols = cols;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                dice[i * cols + j][0] = values.charAt(i * cols + j);
            }
        }
    }

    /**
     * Roll each die and swap randomly,
     * 
     */
    public JumbleBoard shuffle()
    {

        for (int i = 0; i < dice.length; i++)
        {
            for (int j = 0; j < dice[i].length; j++)
            {
                int a = random.nextInt(dice.length);
                int b = random.nextInt(dice[i].length);

                char c = dice[i][j];
                dice[i][j] = dice[a][b];
                dice[a][b] = c;

            }
        }

        return this;

    }

    /**
     * Get the board value at a particular row and column
     * 
     * @param row Row number (starting from zero)
     * @param col Col number (starting from zero)
     * 
     * @return board value at row, column
     */
    public String getValue(int row, int col)
    {
        char value = dice[row * cols + col][0];
        return (value == 'q') ? "qu" : "" + value;
    }


    /**
     * Snapshot of the board.
     * 
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < dice.length; i++)
        {
            sb.append(dice[i][0]);
        }

        return sb.toString();
    }

    public static void main(String[] args)
    {
        if (args.length < 3)
        {
            System.out.println("Usage: board rows cols\n");
            return;
        }

        JumbleBoard board = new JumbleBoard(args[0], Integer.parseInt(args[1]), Integer
                .parseInt(args[2]));
        System.out.println(board);

        board.shuffle();
        System.out.println(board);
    }
}
