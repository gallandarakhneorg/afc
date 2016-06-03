/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This is a list of supported operating system.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum OperatingSystem {

	/**
	 * Windows&reg;.
	 */
	WIN,

	/**
	 * Linux distribution.
	 */
	LINUX,

	/**
	 * Android Linux distribution.
	 *
	 * @since 7.0
	 */
	ANDROID,

	/**
	 * Solaris&reg;.
	 */
	SOLARIS,

	/**
	 * Mac OS X&reg;.
	 */
	MACOSX,

	/**
	 * Free BSD.
	 */
	FREEBSD,

	/**
	 * Net BSD.
	 */
	NETBSD,

	/**
	 * Standard BSD.
	 */
	BSD,

	/**
	 * Open BSD.
	 */
	OPENBSD,

	/**
	 * AIX&reg;.
	 */
	AIX,

	/**
	 * HPUX&reg;.
	 */
	HPUX,

	/**
	 * Unknown operating systems.
	 */
	OTHER;

	private static final String NULL = new String();
	private static OperatingSystemWrapper nativeWrapper;
	private static String osSerialNumber;
	private static String osUUID;
	private static OperatingSystem currentOSInstance;

	//@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	static {
		OperatingSystemIdentificationType type = OperatingSystemIdentificationType.BIOS;
		nativeWrapper = null;

		switch (getCurrentOS()) {
		case BSD:
		case FREEBSD:
		case NETBSD:
		case OPENBSD:
		case MACOSX:
			nativeWrapper = new OperatingSystemDiskUtilWrapper();
			break;
		case LINUX:
			nativeWrapper = new OperatingSystemUDevWrapper();
			break;
		case ANDROID:
			nativeWrapper = new OperatingSystemAndroidWrapper();
			break;
		case WIN:
			type = OperatingSystemIdentificationType.OPERATING_SYSTEM;
			break;
		case AIX:
		case HPUX:
		case SOLARIS:
		case OTHER:
		default:
		}
		if (nativeWrapper == null) {
			try {
				LibraryLoader.loadPlatformDependentLibrary(
						"josuuid", //$NON-NLS-1$
						System.getProperty("os.name").trim().toLowerCase(), //$NON-NLS-1$
						"org/arakhne/vmutil"); //$NON-NLS-1$
				nativeWrapper = new OperatingSystemNativeWrapper(type);
			} catch (Throwable e) {
				// Cannot load native wrapper; then use the default one.
				nativeWrapper = new OperatingSystemUnknownOsWrapper();
			}
		}
	}

	/** Replies the type of identification found on this operating system.
	 *
	 * @return the type of identification found on this operating system.
	 */
	@Pure
	public static OperatingSystemIdentificationType getIdentificationType() {
		if (nativeWrapper == null) {
			return OperatingSystemIdentificationType.BIOS;
		}
		return nativeWrapper.getIdentificationType();
	}

	/** Replies if the current OperatingSystem constant is corresponding
	 * to the current operating system.
	 *
	 * @return <code>true</code> if the current operating system corresponds to this constant,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isCurrentOS() {
		return getCurrentOS() == this;
	}

	/** Replies of this OS is Unix compliant.
	 *
	 * @return <code>true</code> if this constant corresponds to a Unix-like operating system,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isUnixCompliant() {
		switch (this) {
		case AIX:
		case BSD:
		case FREEBSD:
		case HPUX:
		case LINUX:
		case ANDROID:
		case MACOSX:
		case NETBSD:
		case OPENBSD:
		case SOLARIS:
			return true;
		case WIN:
		case OTHER:
			return false;
		default:
		}
		return false;
	}


	/** Replies the name of the current OS.
	 *
	 * @return the name of the current operating system.
	 * @see System#getProperty(java.lang.String)
	 */
	@Pure
	@Inline("System.getProperty(\"os.name\")")
	public static String getCurrentOSName() {
		return System.getProperty("os.name"); //$NON-NLS-1$
	}

	/** Replies the version of the current OS.
	 *
	 * @return the version of the current operating system.
	 * @see System#getProperty(java.lang.String)
	 */
	@Pure
	@Inline("System.getProperty(\"os.version\")")
	public static String getCurrentOSVersion() {
		return System.getProperty("os.version"); //$NON-NLS-1$
	}

	/** Force the type of the current OS replied by the functions of
	 * this class.
	 *
	 * <p>If the given OS is <code>null</code>, the current OS will be auto-detected
	 * at the next call of {@link #getCurrentOS()}.
	 *
	 * @param os - the OS to consider for the next calls to {@link #getCurrentOS()}.
	 * @since 12.0
	 */
	public static void setCurrentOS(OperatingSystem os) {
		currentOSInstance = os;
	}

	/** Replies the current operating system.
	 *
	 * @return the current operating system constant.
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public static OperatingSystem getCurrentOS() {
		if (currentOSInstance == null) {
			final String os = System.getProperty("os.name").trim().toLowerCase(); //$NON-NLS-1$
			/* Let's try to figure canonical OS name, just in case some
			 * JVMs use funny values (unlikely)
			 */
			if (os.indexOf("windows") >= 0) { //$NON-NLS-1$
				currentOSInstance = WIN;
			} else if (os.indexOf("linux") >= 0) { //$NON-NLS-1$
				final String vmName = System.getProperty("java.vm.name").trim().toLowerCase(); //$NON-NLS-1$
				if (vmName.indexOf("dalvik") >= 0) { //$NON-NLS-1$
					currentOSInstance = ANDROID;
				} else {
					final String runtimeName = System.getProperty("java.runtime.name").trim().toLowerCase(); //$NON-NLS-1$
					if (runtimeName.indexOf("android") >= 0) { //$NON-NLS-1$
						currentOSInstance = ANDROID;
					} else {
						currentOSInstance = LINUX;
					}
				}
			} else if ((os.indexOf("solaris") >= 0) //$NON-NLS-1$
					|| (os.indexOf("sunos") >= 0)) { //$NON-NLS-1$
				currentOSInstance = SOLARIS;
			} else if ((os.indexOf("mac os x") >= 0) //$NON-NLS-1$
					|| (os.indexOf("macosx") >= 0)) { //$NON-NLS-1$
				currentOSInstance = MACOSX;
			} else if (os.indexOf("bsd") >= 0) { //$NON-NLS-1$
				if (os.indexOf("freebsd") >= 0) { //$NON-NLS-1$
					currentOSInstance = FREEBSD;
				} else if (os.indexOf("netbsd") >= 0) { //$NON-NLS-1$
					currentOSInstance = NETBSD;
				} else if (os.indexOf("openbsd") >= 0) { //$NON-NLS-1$
					currentOSInstance = OPENBSD;
				} else {
					currentOSInstance = BSD;
				}
			} else if (os.indexOf("aix") >= 0) { //$NON-NLS-1$
				currentOSInstance = AIX;
			} else if (os.indexOf("hp ux") >= 0) { //$NON-NLS-1$
				currentOSInstance = HPUX;
			} else {
				currentOSInstance = OTHER;
			}
		}
		return currentOSInstance;
	}

	/** Replies the data model of the current operating system: 32 or 64 bits.
	 *
	 * @return the integer which is corresponding to the data model, or <code>0</code> if
	 *     it could not be determined.
	 */
	@Pure
	public static int getOperatingSystemArchitectureDataModel() {
		return LibraryLoader.getOperatingSystemArchitectureDataModel();
	}

	/** Replies if the current operating system is 64bit.
	 *
	 * @return <code>true</code> if the operating system is 64bits, othewise
	 *     <code>false</code>
	 */
	@Pure
	@Inline(value = "OperatingSystem.getOperatingSystemArchitectureDataModel() == 64", imported = {OperatingSystem.class})
	@SuppressWarnings("checkstyle:magicnumber")
	public static boolean is64BitOperatingSystem() {
		return getOperatingSystemArchitectureDataModel() == 64;
	}

	/** Replies if the current operating system is 32bit.
	 *
	 * @return <code>true</code> if the operating system is 32bits, othewise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static boolean is32BitOperatingSystem() {
		final int dataModel = getOperatingSystemArchitectureDataModel();
		return dataModel == 32 || dataModel == 0;
	}

	/** Get the OS serial number.
	 *
	 * <p>This function does not allow to run any system command with
	 * the super-user rights, and it disable any additional GUI.
	 *
	 * @return the serial number associated to the current operating system.
	 */
	@Pure
	@Inline(value = "OperatingSystem.getOSSerialNumber(false, false)", imported = {OperatingSystem.class})
	public static String getOSSerialNumber() {
		return getOSSerialNumber(false, false);
	}

	/** Get the OS serial number.
	 *
	 * @param enableSuperUser indicates if the super-user commands are enabled or not.
	 * @param enableGUI indicates if any additional GUI could be opened, or not.
	 * @return the serial number associated to the current operating system.
	 * @sine 6.1
	 */
	@Pure
	public static String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		if (osSerialNumber == null) {
			if (nativeWrapper != null) {
				osSerialNumber = nativeWrapper.getOSSerialNumber(enableSuperUser, enableGUI);
			}
			if (osSerialNumber == null) {
				osSerialNumber = NULL;
			}
		}
		return osSerialNumber == NULL ? null : osSerialNumber;
	}

	/** Get the OS UUID.
	 *
	 * <p>This function does not allow to run any system command with
	 * the super-user rights, and it disable any additional GUI.
	 *
	 * @return an unique identifier for the current operating system.
	 */
	@Pure
	@Inline(value = "OperatingSystem.getOSUUID(false, false)", imported = {OperatingSystem.class})
	public static String getOSUUID() {
		return getOSUUID(false, false);
	}

	/** Get the OS UUID.
	 *
	 * @param enableSuperUser indicates if the super-user commands are enabled or not.
	 * @param enableGUI indicates if any additional GUI could be opened, or not.
	 * @return an unique identifier for the current operating system.
	 * @since 6.1
	 */
	@Pure
	public static String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		if (osUUID == null) {
			if (nativeWrapper != null) {
				osUUID = nativeWrapper.getOSUUID(enableSuperUser, enableGUI);
			}
			if (osUUID == null) {
				osUUID = NULL;
			}
		}
		return osUUID == NULL ? null : osUUID;
	}

}
