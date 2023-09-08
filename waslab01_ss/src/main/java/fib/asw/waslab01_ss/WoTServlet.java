package fib.asw.waslab01_ss;

import java.io.*;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/")
public class WoTServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private TweetDAO tweetDAO;
	private Locale currentLocale = new Locale("en");
    private String ENCODING = "ISO-8859-1";

    public void init() {
    	tweetDAO = new TweetDAO((java.sql.Connection) this.getServletContext().getAttribute("connection"));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	
        List<Tweet> tweets = tweetDAO.getAllTweets();
        if (request.getHeader("Accept").equals("text/plain")) {
    		printPLAINresult(response, tweets);
    	}
        else {
        	printHTMLresults(response, tweets);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
        String autor = request.getParameter("author");
        String text = request.getParameter("tweet_text");
        
        response.setContentType ("text/plain");
        
        
        try {
        		long id = tweetDAO.insertTweet(autor, text);
        		PrintWriter out = response.getWriter();
        		out.print(id);
        		
        }
        catch (Exception e) {
        	PrintWriter out = response.getWriter();
        	out.println("Raised exception of type" + e);
        }
        
        	
        if (!request.getHeader("Accept").equals("text/plain")) {
        	response.sendRedirect(request.getContextPath());
        }
    
    }

    private void printHTMLresults (HttpServletResponse response, List<Tweet> tweets) throws IOException {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
        response.setContentType ("text/html");
        response.setCharacterEncoding(ENCODING);

        PrintWriter out = response.getWriter();


        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Wall of Tweets</title>");
        out.println("<link href=\"wot.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("</head>");
        out.println("<body class=\"wallbody\">");
        out.println("<h1>Wall of Tweets</h1>");
        out.println("<div class=\"walltweet\">");
        out.println("<form method=\"post\">");
        out.println("<table border=0 cellpadding=2>");
        out.println("<tr><td>Your name:</td><td><input name=\"author\" type=\"text\" size=70></td><td></td></tr>");
        out.println("<tr><td>Your tweet:</td><td><textarea name=\"tweet_text\" rows=\"2\" cols=\"70\" wrap></textarea></td>");
        out.println("<td><input type=\"submit\" name=\"action\" value=\"Tweet!\"></td></tr>");
        out.println("</table></form></div>");
        String currentDate = "None";
        for (Tweet tweet: tweets) {
            String messDate = dateFormatter.format(tweet.getCreated_at());
            if (!currentDate.equals(messDate)) {
                out.println("<br><h3>...... " + messDate + "</h3>");
                currentDate = messDate;
            }
            out.println("<div class=\"wallitem\">");
            out.println("<h4><em>" + tweet.getAuthor() + "</em> @ "+ timeFormatter.format(tweet.getCreated_at()) +"</h4>");
            out.println("<p>" + tweet.getText() + "</p>");
            out.println("</div>");
        }
        out.println ( "</body></html>" );
    }
    
    private void printPLAINresult(HttpServletResponse response, List<Tweet> tweets) throws IOException{

        response.setContentType ("text/html");
        response.setCharacterEncoding(ENCODING);
        
        PrintWriter out = response.getWriter();
        
        for (Tweet tweet: tweets) {
        	out.print("tweet #" + tweet.getTwid() + ": " 
        			+ tweet.getAuthor()+ ": " + tweet.getText() + ". [" + tweet.getCreated_at() + "]");
        }
    }
}