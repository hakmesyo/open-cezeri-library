package cezeri.utils;

/**
 *
 * This exception is thrown by Queue class methods to indicate an empty queue.
 *
 * @author Yasser EL-Manzalawy ymelmanz@yahoo.com
 *
 *  This software is provided as is, without representation as to its
 *  fitness for any purpose, and without warranty of any kind, either
 *  express or implied, including without limitation the implied
 *  warranties of merchantability and fitness for a particular purpose.
 *  The author shall not be liable for any damages, including special, 
 *  indirect,incidental, or consequential damages, with respect to any claim
 *  arising out of or in connection with the use of the software, even
 *  if they have been or are hereafter advised of the possibility of
 *  such damages.
 * 
 */


public class EmptyQueueException extends RuntimeException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyQueueException()  { } 
}