package utils;

import java.sql.Timestamp;

/**
 *
 * @author Josh Shepherd
 */

/** This is a functional interface used for creating getMessage lambda expressions
 * on the Login Menu. The justification for this lambda interface is to provide
 * an easy means for generating simple messages.  It could be used throughout
 * the application.  However, I only used this interface in the LoginMenuController
 * to feed messages into my logger. */
public interface MessageInterface {
    
    /** An abstract getMessage method that accepts two parameter. 
     * @param s String
     * @param t Timestamp
     * @return String A message that is to be defined by lambda expression.*/
    String getMessage(String s, Timestamp t);
    
}
