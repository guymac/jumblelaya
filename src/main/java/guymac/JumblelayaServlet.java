package guymac;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/words", initParams = { 
        @WebInitParam(name = "wordlist", value = "/WEB-INF/classes/words/words.txt"), 
     }) 
@SuppressWarnings("serial")
public class JumblelayaServlet extends HttpServlet
{
    private CachingWordList list;

    /**
     * Startup initialization. Load the word list.
     */
    @Override
    public void init(ServletConfig config) throws ServletException
    {

        super.init(config);

        String param = config.getInitParameter("wordlist");
        
        log("Loading wordlist " + param);
        
        try (InputStream in = getServletContext().getResourceAsStream(param))
        {

            BufferedReader reader = null;

            if (param.endsWith("gz") || param.endsWith("GZ"))
            {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(in)));
            }
            else
            {
                reader = new BufferedReader(new InputStreamReader(in));
            }

            list = new CachingWordList(reader);

        }
        catch (IOException | NullPointerException ex)
        {

            log("Could not load wordlist " + param, ex);

            throw new ServletException("Wordlist " + param + " was not found.");
        }

    }

    /**
     * For POST, return a new shuffled board.
     */
    @Override
    public void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException
    {
        try
        {

            byte[] bytes = new JumbleBoard().shuffle().toString().getBytes();

            rs.setContentType("text/plain");
            rs.setDateHeader("Expires", System.currentTimeMillis());
            rs.setContentLength(bytes.length);
            rs.getOutputStream().write(bytes);

        }
        catch (IOException ex)
        {
            log("Could not send board", ex);
        }
    }

    /**
     * For GET, accept a board and return all the words it contains.
     */
    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException
    {

        String qs = rq.getQueryString();

        if (qs == null || qs.length() != 16)
        {
            throw new ServletException("Board length must be 16 characters");
        }

        rs.setContentType("text/plain");

        long now = System.currentTimeMillis();
        rs.setDateHeader("Expires", now);

        JumbleBoard board = new JumbleBoard(qs, 4, 4);

        JumbleSolver solver = new JumbleSolver(board, list);

        solver.run();

        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(out);

            for (Iterator <String> it = solver.added.keySet().iterator(); it.hasNext();)
            {
                ps.println(it.next());
            }

            rs.setContentLength(out.size());

            out.writeTo(rs.getOutputStream());

        }
        catch (IOException ex)
        {
            log("Error sending response", ex);
        }
    }

}
