/*
 * MorePaperLib
 * Copyright © 2023 Anand Beh
 *
 * MorePaperLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MorePaperLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MorePaperLib. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.morepaperlib;

/**
 * Unchecked exception thrown when a feature cannot be used
 * 
 * @author A248
 *
 */
public class FeatureFailedException extends RuntimeException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 5038819479673612108L;
	
	/**
	 * Creates the exception
	 * 
	 */
	public FeatureFailedException() {
		
	}
	
	/**
	 * Creates the exception with the given cause
	 * 
	 * @param cause the cause
	 */
	public FeatureFailedException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates the exception with the given message
	 * 
	 * @param message the message
	 */
	public FeatureFailedException(String message) {
		super(message);
	}
	
	/**
	 * Creates the exception with the given message and cause
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public FeatureFailedException(String message, Throwable cause) {
		super(cause);
	}

}
