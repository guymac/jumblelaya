/**
 * 
 */
package guymac;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class CachingWordList extends WordList
{
    private final Map <String, Integer> cache = new HashMap <> ();

    public CachingWordList(BufferedReader in) throws IOException
    {
        super(in);
    }

    /**
     * 
     * Look the word up in the cache, or look it up then put it in the cache.
     * 
     * @param word Fragment to lookup
     * @return 1 if word is in list, -1 if word(s) in list start with word,
     *         0 if no words start with word
     */
    @Override
    public int has(String word)
    {
        // Set<Map.Entry<String, Integer>> set = cache.entrySet();

        if (cache.containsKey(word)) return cache.get(word);
        int value = super.has(word);
        cache.put(word, value);
        return value;
    }
}
