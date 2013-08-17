/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.attrs.attr;

import org.arakhne.vmutil.URISchemeType;

/**
 * This interface contains several constant names.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AttributeConstants {

	/** Default URI and URL scheme used to build URLs and URIs.
	 */
	public static final URISchemeType DEFAULT_SCHEME = URISchemeType.HTTP;

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String TRUE_CONSTANT = "true"; //$NON-NLS-1$
	
	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String YES_CONSTANT = "yes"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String OUI_CONSTANT = "oui"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String T_CONSTANT = "t"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String O_CONSTANT = "o"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String Y_CONSTANT = "y"; //$NON-NLS-1$

	/** String constant for the <code>false</code> boolean constant.
	 */
	public static final String FALSE_CONSTANT = "false"; //$NON-NLS-1$
	
	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String NO_CONSTANT = "no"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String NON_CONSTANT = "non"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String F_CONSTANT = "f"; //$NON-NLS-1$

	/** String constant for the <code>true</code> boolean constant.
	 */
	public static final String N_CONSTANT = "n"; //$NON-NLS-1$

	/** Attribute "name".
	 */
	public static final String ATTR_NAME = "name"; //$NON-NLS-1$
	
	/** Attirbute "id".
	 */
	public static final String ATTR_IDENTIFIER = "id"; //$NON-NLS-1$
	
}