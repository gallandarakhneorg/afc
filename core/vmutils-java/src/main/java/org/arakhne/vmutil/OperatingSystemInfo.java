/* 
 * $Id$
 * 
 * Copyright (C) 2009, 2011, 2013 Stephane GALLAND.
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

package org.arakhne.vmutil;

import java.io.FileNotFoundException;


/**
 * This class print on the standard output several informations
 * about your operating system.
 * These informations are extracted by the Java or the native
 * libraries from <code>arakhneVmutils</code>.  
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OperatingSystemInfo {

	private static void showTitle(String title) {
		System.out.print("#######################################\n# "); //$NON-NLS-1$
		System.out.println(title);
		System.out.println("#######################################"); //$NON-NLS-1$
	}
	
	private static void showPropertyValue(String name) {
		showValue(name, System.getProperty(name));
	}
	
	private static void showValue(String name, Object value) {
		System.out.print(name);
		System.out.print(" = "); //$NON-NLS-1$
		System.out.println((value==null) ? null : value.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		showTitle("JRE Properties"); //$NON-NLS-1$
		
		showPropertyValue("java.class.path"); //$NON-NLS-1$
		showPropertyValue("java.library.path"); //$NON-NLS-1$
		showPropertyValue("java.home"); //$NON-NLS-1$
		showPropertyValue("os.name"); //$NON-NLS-1$
		showPropertyValue("os.version"); //$NON-NLS-1$
		showPropertyValue("file.separator"); //$NON-NLS-1$
		showPropertyValue("path.separator"); //$NON-NLS-1$
		showPropertyValue("sun.arch.data.model"); //$NON-NLS-1$
		showPropertyValue("user.dir"); //$NON-NLS-1$
		showPropertyValue("user.name"); //$NON-NLS-1$
		showPropertyValue("user.home"); //$NON-NLS-1$
		
		showTitle("FileSystem"); //$NON-NLS-1$

		showValue("getUserHomeDirectoryName()", FileSystem.getUserHomeDirectoryName()); //$NON-NLS-1$
		try {
			showValue("getUserHomeDirectory()", FileSystem.getUserHomeDirectory()); //$NON-NLS-1$
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		showTitle("OperatingSystem"); //$NON-NLS-1$
		
		showValue("getCurrentOSName()", OperatingSystem.getCurrentOSName()); //$NON-NLS-1$
		showValue("getCurrentOSVersion()", OperatingSystem.getCurrentOSVersion()); //$NON-NLS-1$
		showValue("getOSSerialNumber()", OperatingSystem.getOSSerialNumber()); //$NON-NLS-1$
		showValue("getOSUUID()", OperatingSystem.getOSUUID()); //$NON-NLS-1$
		showValue("getIdentificationType()", OperatingSystem.getIdentificationType().name()); //$NON-NLS-1$
		showValue("getOperatingSystemArchitectureDataModel()", Integer.toString(OperatingSystem.getOperatingSystemArchitectureDataModel())); //$NON-NLS-1$
		showValue("getCurrentOS()", OperatingSystem.getCurrentOS().name()); //$NON-NLS-1$
		showValue("is32BitOperatingSystem()", Boolean.toString(OperatingSystem.is32BitOperatingSystem())); //$NON-NLS-1$
		showValue("is64BitOperatingSystem()", Boolean.toString(OperatingSystem.is64BitOperatingSystem())); //$NON-NLS-1$
	}
	
}
