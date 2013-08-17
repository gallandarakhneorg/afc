/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math.matrix;



/** Exception for all the singular matrices.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SingularMatrixException extends RuntimeException {

	private static final long serialVersionUID = 2834240107372614319L;

	/**
	 */
	public SingularMatrixException() {
		//
	}

	/**
	 * @param message
	 */
	public SingularMatrixException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SingularMatrixException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SingularMatrixException(String message, Throwable cause) {
		super(message, cause);
	}

}