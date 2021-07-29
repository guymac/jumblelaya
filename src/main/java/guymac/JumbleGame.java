/**
 * 
 */
package guymac;

import java.text.ChoiceFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class JumbleGame extends Observable
{
    /** Reference to the current board */
    protected JumbleBoard board;

    /** Score in the current round */
    protected int score = 0;

    /** Total score since the game began */
    protected int total = 0;

    /** Number of words available on the board */
    protected int wordcount = 0;

    /** Maps words to their point value */
    protected Map <String, Integer> added = new HashMap <> ();

    /** Our look-up table */
    protected CachingWordList list;

    /** Message formatter */
    protected final ChoiceFormat fmt = new ChoiceFormat(" 1# point.|1< points.");

    /**
     * Message object, contains string, or if points scored, the point value.
     * 
     * 
     */
    public static class GameMessage
    {
        public int pts = 0;
        String msg, word = null;

        public GameMessage(String msg)
        {
            this.msg = msg;
        }

        public GameMessage(String msg, String word, int pts)
        {
            this.msg = msg;
            this.word = word;
            this.pts = pts;
        }

        @Override
        public String toString()
        {
            return msg;
        }
    }

    /**
     * Given a board and a WordList, construct a game instance.
     * A game will have multiple matches.
     */
    public JumbleGame(final JumbleBoard board, final CachingWordList list)
    {
        this.board = board;
        this.list = list;
    }

    public void zero()
    {
        total = 0;
        init();
    }

    /**
     * Start a new match.
     */
    public void init()
    {
        score = 0;
        added.clear();
    }

    /**
     * Add up the points for the recently completed match.
     */
    public void total()
    {
        total += score;
        wordcount += added.size();
        setChanged();
        notifyObservers(new GameMessage("Matching words eliminated."));
    }

    /**
     * Score the points for a guess. Check to see if it is on the board.
     */
    public int score(String word)
    {
        return score(word, true);
    }

    /**
     * Score the points for a guess.
     * 
     * @param word Input guess word.
     * @param check True if the board should be checked to see if it contains
     *            the word.
     */
    public int score(String word, boolean check)
    {
        setChanged(); // somethings gonna happen, I can feel it!

        if (word.length() < 3)
        {
            notifyObservers(new GameMessage("Word must be at least three letters long.", word, 0));
            return 0;
        }
        if (check)
        {
            if (added.containsKey(word))
            {
                notifyObservers(new GameMessage("\"" + word + "\" already used.", word, 0));
                return 0;
            }
            if (!board.has(word))
            {
                notifyObservers(new GameMessage("\"" + word + "\" not found on the board.", word, 0));
                return 0;
            }
            if (list.has(word) != 1)
            {
                notifyObservers(new GameMessage("\"" + word + "\" not found in dictionary.", word,
                        0));
                return 0;
            }
        }

        int pts = getPoints(word);
        // total += pts;

        score += pts;
        added.put(word, Integer.valueOf(pts));
        notifyObservers(new GameMessage("\"" + word + "\" scored " + pts + " " + fmt.format(pts),
                word, pts));

        return pts;
    }

    /**
     * The word scoring algorithm.
     */
    public static int getPoints(final String word)
    {
        if (word.length() < 3) return 0;

        switch (word.length())
        {
            case (3):
                return 1;
            case (4):
                return 1;
            case (5):
                return 2;
            case (6):
                return 3;
            case (7):
                return 5;
            default: // 8 or more
                return 11;
        }
    }
}
