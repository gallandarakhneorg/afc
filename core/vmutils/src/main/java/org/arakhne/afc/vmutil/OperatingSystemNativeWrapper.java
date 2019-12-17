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
 * @since 6.0
 */
class OperatingSystemNativeWrapper implements OperatingSystemWrapper {

	private final OperatingSystemIdentificationType type;

	/** Constructor.
	 * @param type is the type of identification supported by the wrapper.
	 */
	OperatingSystemNativeWrapper(OperatingSystemIdentificationType type) {
		this.type = type;
	}

	@Override
	public native String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI);

	@Override
	public native String getOSUUID(boolean enableSuperUser, boolean enableGUI);

	@Override
	public OperatingSystemIdentificationType getIdentificationType() {
		return this.type;
	}

}
