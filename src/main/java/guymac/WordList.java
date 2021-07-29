/**
 * 
 */
package guymac;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/**
 * Stores a text file of words in an ArrayList, uses a fast binary search
 * algorithm to determine if an input word is in the list. Used as
 * memory-persistent spelling lookup table.
 */
@SuppressWarnings("serial")
public class WordList extends ArrayList <String>
{

    /**
     * Constructs a WordList from a BufferedReader source. One word per line.
     * 
     * @param in Alphabetically ordered word list
     * @throws IOException Error parsing stream
     */
    public WordList(BufferedReader in) throws IOException
    {

        super();

        for (String line = null; (line = in.readLine()) != null;)
        {
            add(line.trim().toLowerCase());
        }

    }

    /**
     * Test from the command-line.
     * 
     * @throws IOException Error parsing stream
     */
    public static void main(String[] args) throws IOException
    {

        if (args.length < 1)
        {
            System.out.println("Usage: WordList dictionary [words]");

            return;
        }

        WordList wordlist = new WordList(new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream(args[0])))));

        for (int i = 1; i < args.length; i++)
        {
            System.out.println(args[i] + " => " + wordlist.has(args[i]));
        }

    }

    /**
     * Binary search algorithm.
     * 
     * @param word
     *            Fragment to lookup
     * @return 1 if word is in list, -1 if word(s) in list start with word, 0 if
     *         no words start with word
     */
    public int has(final String word)
    {

        boolean foundStart = false; // did I find a word that starts with input?

        String compare;

        for (int lo = -1, hi = size(), mid = 0; hi > lo + 1; mid = lo + (hi - lo) / 2)
        {

            compare = get(mid);

            int s = word.compareTo(compare);

            if (s > 0)
            { // word follows compare
                lo = mid;
            }
            else if (s < 0)
            { // word precedes compare
                hi = mid;
            }
            else
            { // found match
                return 1;
            }

            if (!foundStart) foundStart = compare.startsWith(word);
        }

        return (foundStart) ? -1 : 0;
    }
}
