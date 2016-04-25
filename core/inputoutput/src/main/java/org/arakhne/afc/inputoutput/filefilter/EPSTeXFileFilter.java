/* 
 * $Id$
 * 
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
package org.arakhne.afc.inputoutput.filefilter ;

import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for a TeX part of
 * a EPS figure combined with TeX macros.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EPSTeXFileFilter extends AbstractFileFilter {

	/** Historical extension for TeX part of the EPS documents combined with TeX macros.
	 */
	public static final String EXTENSION1 = "pstex_t"; //$NON-NLS-1$

	/** Modern extension for TeX part of the EPS documents combined with TeX macros.
	 */
	public static final String EXTENSION2 = "ps_tex"; //$NON-NLS-1$

	/**
	 */
	public EPSTeXFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 * permit to this file filter to accept directories;
	 * <code>false</code> if the directories should not
	 * match.
	 */
	public EPSTeXFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(EPSTeXFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION1, EXTENSION2);
	}
	
}
