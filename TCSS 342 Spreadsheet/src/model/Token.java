/*
 * Lisa Taylor
 * Jonah Howard
 * Henry Lai
 * John Bui
 * 
 * TCSS 342 - Spring 2016
 * Assignment 6B
 */

package model;

/**
 * This interface represents a Token. It allows for a generic instantiation of a Token.
 * 
 * @author Lisa Taylor
 * 
 * @version 3 March 2016
 */
public abstract class Token {

	/** Represents the current token. */
    private String token = null;

    /**
     * Returns the current token.
     * 
     * @return the current token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets this token to the passed value.
     * 
     * @param token the new value for this token
     */
    public void setToken(final String token) {
        this.token = token;
    }
    
    @Override
    public String toString() {
        return token;
    }
}

