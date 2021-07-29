/**
 * 
 */
package guymac;

/**
 * 
 */
public class JumbleSolver extends JumbleGame implements Runnable
{
    /** if running as a thread, indicates that the thread is active */
    protected boolean active = true; // stop is deprecated

    public JumbleSolver(JumbleBoard board, CachingWordList list)
    {
        super(board, list);
        super.init();
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isActive()
    {
        return active;
    }

    public void run()
    {

        for (int i = 0; i < board.rows; i++)
        {
            for (int j = 0; j < board.cols; j++)
            {
                if (active) recurse(new boolean[board.rows][board.cols], true, "", i, j);
            }
        }

        active = false;

    }

    // this is where the action is!
    public boolean recurse(boolean[][] state, boolean more, String strb, int row, int col)
    {
        if (!active) return false;

        if (state[row][col])
        {
            return false;
        }

        String letter = board.getValue(row, col);
        String word = strb + letter;

        if (word.length() > 1) switch (list.has(word))
        {
            case (0):
                return false;
            case (1):
                if (word.length() < 3) break;
            if (!added.containsKey(word)) score(word, false); // don't
            // double
            // check
        }

        state[row][col] = true;

        // corner tiles
        if (row == 0 && col == 0)
        {
            recurse(state, more, word, row + 1, col);
            recurse(state, more, word, row, col + 1);
            recurse(state, more, word, row + 1, col + 1);
        }
        else if (row == 0 && col == board.cols - 1)
        {
            recurse(state, more, word, row, col - 1);
            recurse(state, more, word, row + 1, col - 1);
            recurse(state, more, word, row + 1, col);
        }
        else if (row == board.rows - 1 && col == 0)
        {
            recurse(state, more, word, row - 1, col);
            recurse(state, more, word, row - 1, col + 1);
            recurse(state, more, word, row, col + 1);
        }
        else if (row == board.rows - 1 && col == board.cols - 1)
        {
            recurse(state, more, word, row, col - 1);
            recurse(state, more, word, row - 1, col - 1);
            recurse(state, more, word, row - 1, col);
        } // side tiles
        else if (row == 0)
        {
            recurse(state, more, word, row, col - 1);
            recurse(state, more, word, row, col + 1);
            recurse(state, more, word, row + 1, col - 1);
            recurse(state, more, word, row + 1, col);
            recurse(state, more, word, row + 1, col + 1);
        }
        else if (row == board.rows - 1)
        {
            recurse(state, more, word, row, col - 1);
            recurse(state, more, word, row, col + 1);
            recurse(state, more, word, row - 1, col - 1);
            recurse(state, more, word, row - 1, col);
            recurse(state, more, word, row - 1, col + 1);
        }
        else if (col == 0)
        {
            recurse(state, more, word, row - 1, col);
            recurse(state, more, word, row + 1, col);
            recurse(state, more, word, row - 1, col + 1);
            recurse(state, more, word, row, col + 1);
            recurse(state, more, word, row + 1, col + 1);
        }
        else if (col == board.cols - 1)
        {
            recurse(state, more, word, row - 1, col);
            recurse(state, more, word, row + 1, col);
            recurse(state, more, word, row - 1, col - 1);
            recurse(state, more, word, row, col - 1);
            recurse(state, more, word, row + 1, col - 1);
        } // center tiles
        else
        {
            recurse(state, more, word, row - 1, col - 1); // 0
            recurse(state, more, word, row, col - 1); // 1
            recurse(state, more, word, row + 1, col - 1); // 2
            recurse(state, more, word, row + 1, col); // 3
            recurse(state, more, word, row + 1, col + 1); // 4
            recurse(state, more, word, row, col + 1); // 5
            recurse(state, more, word, row - 1, col + 1); // 6
            recurse(state, more, word, row - 1, col); // 7
        }

        state[row][col] = false;

        return false;

    }

}
