package utils;

/**
 *
 * @author Josh Shepherd
 */

/** This is a functional interface to provide an abstract boolean method
 * for creating one lambda expression in AddCustomerController, AddAppointment Controller,
 * ModifyCustomerController, and MofidyAppointmentController classes for a total of four
 * lambda expressions.  The justification for these lambdas is that I had a private boolean
 * aFieldIsEmpty() method located in each of those four classes and instead they were all
 * able to utilize this interface to check for empty fields.*/
public interface EmptyFieldInterface {
    
    /** An abstract boolean method that accepts no parameters.
     * @return  boolean A boolean that is to be defined by lambda expression. */
    public abstract boolean aFieldIsEmpty();
    
}
