/* 
  * $Id$
 * 
 * Copyright (C) 2011-12 Stephane GALLAND.
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

package org.arakhne.vmutil;

/**
 * Wrapper to the native functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 */
class OperatingSystemNativeWrapper implements OperatingSystemWrapper {

	private final OperatingSystemIdentificationType type;
	
	/**
	 * @param type is the type of identification supported by the wrapper.
	 */
	public OperatingSystemNativeWrapper(OperatingSystemIdentificationType type) {
		this.type = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public native String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI);

	/** {@inheritDoc}
	 */
	@Override
	public native String getOSUUID(boolean enableSuperUser, boolean enableGUI);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OperatingSystemIdentificationType getIdentificationType() {
		return this.type;
	}

}
