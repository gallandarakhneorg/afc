/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.io.File;

/**
 * Wrapper to the MacOS functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.3
 */
class OperatingSystemUDevWrapper extends AbstractOperatingSystemWrapper {

	/** Construct the wrapper.
	 */
	OperatingSystemUDevWrapper() {
		//
	}

	private static String runUdev(File file, String key) {
		final String result = runCommand(
				"udevadm", //$NON-NLS-1$
				"info", //$NON-NLS-1$
				"-q", //$NON-NLS-1$
				"property", //$NON-NLS-1$
				"-n", //$NON-NLS-1$
				file.toString());
		return cut("=", 1, grep(key + "=", result)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		File file = new File("/dev/sda"); //$NON-NLS-1$
		if (file.exists()) {
			return runUdev(file, "ID_SERIAL"); //$NON-NLS-1$
		}

		file = new File("/dev/hda"); //$NON-NLS-1$
		if (file.exists()) {
			return runUdev(file, "ID_SERIAL"); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		File file = new File("/dev/sda"); //$NON-NLS-1$
		if (file.exists()) {
			return runUdev(file, "ID_SERIAL_SHORT"); //$NON-NLS-1$
		}

		file = new File("/dev/hda"); //$NON-NLS-1$
		if (file.exists()) {
			return runUdev(file, "ID_SERIAL_SHORT"); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public OperatingSystemIdentificationType getIdentificationType() {
		return OperatingSystemIdentificationType.HARD_DISK;
	}

}
