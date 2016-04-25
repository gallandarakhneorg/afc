/* 
  * $Id$
 * 
 * Copyright (C) 2011-13 Stephane GALLAND.
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

package org.arakhne.afc.vmutil;

/**
 * Wrapper to the native functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 11.0
 */
class OperatingSystemUnknownOsWrapper implements OperatingSystemWrapper {

	/**
	 */
	public OperatingSystemUnknownOsWrapper() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OperatingSystemIdentificationType getIdentificationType() {
		return OperatingSystemIdentificationType.OPERATING_SYSTEM;
	}

}
