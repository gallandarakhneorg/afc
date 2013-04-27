/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.util;

/** Exception thrown when an object does not support any 
 * natural order.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnsupportedNaturalOrderException extends RuntimeException {

	private static final long serialVersionUID = 6506767532507115987L;
	
	private final Object element;
	
	/**
	 * @param element is the element which does not provide a natural order.
	 */
	public UnsupportedNaturalOrderException(Object element) {
		super();
		this.element = element;
	}
	
	/** Replies the element which does not provide a natural order.
	 * 
	 * @return the element which does not provide a natural order.
	 */
	public Object getElement() {
		return this.element;
	}

}
