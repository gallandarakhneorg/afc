/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.vmutil;

import java.io.FileNotFoundException;


/**
 * This class print on the standard output several informations
 * about your operating system.
 * These informations are extracted by the Java or the native
 * libraries from <code>arakhneVmutils</code>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:regexp")
public final class OperatingSystemInfo {

	private OperatingSystemInfo() {
		//
	}

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
		System.out.println((value == null) ? null : value.toString());
	}

	/** Main program.
	 *
	 * @param args command line arguments.
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		showTitle("OperatingSystem"); //$NON-NLS-1$

		showValue("getCurrentOSName()", OperatingSystem.getCurrentOSName()); //$NON-NLS-1$
		showValue("getCurrentOSVersion()", OperatingSystem.getCurrentOSVersion()); //$NON-NLS-1$
		showValue("getOSSerialNumber()", OperatingSystem.getOSSerialNumber()); //$NON-NLS-1$
		showValue("getOSUUID()", OperatingSystem.getOSUUID()); //$NON-NLS-1$
		showValue("getIdentificationType()", OperatingSystem.getIdentificationType().name()); //$NON-NLS-1$
		showValue("getOperatingSystemArchitectureDataModel()", //$NON-NLS-1$
				Integer.toString(OperatingSystem.getOperatingSystemArchitectureDataModel()));
		showValue("getCurrentOS()", OperatingSystem.getCurrentOS().name()); //$NON-NLS-1$
		showValue("is32BitOperatingSystem()", Boolean.toString(OperatingSystem.is32BitOperatingSystem())); //$NON-NLS-1$
		showValue("is64BitOperatingSystem()", Boolean.toString(OperatingSystem.is64BitOperatingSystem())); //$NON-NLS-1$
	}

}
