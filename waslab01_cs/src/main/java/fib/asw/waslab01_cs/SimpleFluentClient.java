package fib.asw.waslab01_cs;

import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;

//This code uses the Fluent API

public class SimpleFluentClient {

	private static String URI = "http://localhost:8080/waslab01_ss/";

	public final static void main(String[] args) throws Exception {
  	
  	/* Insert code for Task #4 here */
	String id = Request.post(URI).bodyForm(Form.form().add("action",  "create").add("author",  "Abel").add("tweet_text", "Hola").build())
			.addHeader("Accept", "text/plain").execute().returnContent().asString();
	System.out.println(id);
	
  	System.out.println(Request.get(URI).addHeader("Accept", "text/plain").execute().returnContent());
  	
  	/* Insert code for Task #5 here */
  	
  	System.out.println(String.format("El tweet #%s ha sigut esborrat", Integer.parseInt(id)-1));
  	
  	System.out.println(Request.post(URI).bodyForm(Form.form().add("action",  "delete").add("idDelete", id).build())
  	.addHeader("Accept", "text/plain").execute().returnContent());
  	
  	
 
  	
  }
}