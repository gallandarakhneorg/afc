/* 
  * $Id$
 * 
 * Copyright (C) 2011 Stephane GALLAND.
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
 * Types of identification of the operating system.  
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.3
 */
public enum OperatingSystemIdentificationType {

	/**
	 * Identification comes from the BIOS.
	 */
	BIOS,
	
	/**
	 * Identification comes from the Operating System itself.
	 */
	OPERATING_SYSTEM,
	
	/**
	 * Identification comes from the hard disk.
	 */
	HARD_DISK,

	/**
	 * Identification comes from the network card.
	 */
	NETWORK_CARD,

	/**
	 * Identification comes from a dongle.
	 */
	DONGLE;

}
