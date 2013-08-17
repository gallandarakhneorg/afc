/* 
 * $Id$
 * 
 * Copyright (C) 2004-2013 Stephane GALLAND.
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

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This is a list of supported operating system.  
 *
 * @author $Author: galland$
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
	private static String osSerialNumber = null;
	private static String osUUID = null;
	private static OperatingSystem currentOSInstance = null;

	/** Replies the type of identification found on this operating system.
	 * 
	 * @return the type of identification found on this operating system.
	 */
	public static OperatingSystemIdentificationType getIdentificationType() {
		if (nativeWrapper==null)
			return OperatingSystemIdentificationType.BIOS;
		return nativeWrapper.getIdentificationType();
	}

	/** Replies if the current OperatingSystem constant is corresponding
	 * to the current operating system.
	 * 
	 * @return <code>true</code> if the current operating system corresponds to this constant,
	 * otherwise <code>false</code>
	 */
	public boolean isCurrentOS() {
		return getCurrentOS()==this;
	}

	/** Replies of this OS is Unix compliant.
	 * 
	 * @return <code>true</code> if this constant corresponds to a Unix-like operating system,
	 * otherwise <code>false</code>
	 */
	public boolean isUnixCompliant() {
		switch(this) {
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
	public static String getCurrentOSName() {
		return System.getProperty("os.name"); //$NON-NLS-1$
	}

	/** Replies the version of the current OS.
	 * 
	 * @return the version of the current operating system.
	 * @see System#getProperty(java.lang.String)
	 */
	public static String getCurrentOSVersion() {
		return System.getProperty("os.version"); //$NON-NLS-1$
	}

	/** Replies the current operating system.
	 * 
	 * @return the current operating system constant.
	 */
	public static OperatingSystem getCurrentOS() {
		if (currentOSInstance!=null) return currentOSInstance;

		String os = System.getProperty("os.name").trim().toLowerCase(); //$NON-NLS-1$

		/* Let's try to figure canonical OS name, just in case some
		 * JVMs use funny values (unlikely)
		 */
		if (os.indexOf("windows") >= 0) { //$NON-NLS-1$
			return currentOSInstance=WIN;
		}
		else if (os.indexOf("linux") >= 0) { //$NON-NLS-1$
			String vmName = System.getProperty("java.vm.name").trim().toLowerCase(); //$NON-NLS-1$
			if (vmName.indexOf("dalvik") >= 0) { //$NON-NLS-1$
				return currentOSInstance=ANDROID;
			}
			String runtimeName = System.getProperty("java.runtime.name").trim().toLowerCase(); //$NON-NLS-1$
			if (runtimeName.indexOf("android") >= 0) { //$NON-NLS-1$
				return currentOSInstance=ANDROID;
			}
			return currentOSInstance=LINUX;
		}
		else if ((os.indexOf("solaris") >= 0)|| //$NON-NLS-1$
				(os.indexOf("sunos") >= 0)) { //$NON-NLS-1$
			return currentOSInstance=SOLARIS;
		}
		else if ((os.indexOf("mac os x") >= 0)|| //$NON-NLS-1$
				(os.indexOf("macosx") >= 0)) { //$NON-NLS-1$
			return currentOSInstance=MACOSX;
		}
		else if (os.indexOf("bsd") >= 0) { //$NON-NLS-1$
			if (os.indexOf("freebsd") >= 0) { //$NON-NLS-1$
				return currentOSInstance=FREEBSD;
			}
			else if (os.indexOf("netbsd") >= 0) { //$NON-NLS-1$
				return currentOSInstance=NETBSD;
			}
			else if (os.indexOf("openbsd") >= 0) { //$NON-NLS-1$
				return currentOSInstance=OPENBSD;
			}
			else { // default
				return currentOSInstance=BSD;
			}
		}
		else if (os.indexOf("aix") >= 0) { //$NON-NLS-1$
			return currentOSInstance=AIX;
		}
		else if (os.indexOf("hp ux") >= 0) { //$NON-NLS-1$
			return currentOSInstance=HPUX;
		}
		else {
			return currentOSInstance=OTHER;
		}
	}

	/** Replies the data model of the current operating system: 32 or 64 bits.
	 * 
	 * @return the integer which is corresponding to the data model, or <code>0</code> if
	 * it could not be determined.
	 */
	public static int getOperatingSystemArchitectureDataModel() {
		return LibraryLoader.getOperatingSystemArchitectureDataModel();
	}

	/** Replies if the current operating system is 64bit.
	 * 
	 * @return <code>true</code> if the operating system is 64bits, othewise
	 * <code>false</code>
	 */
	public static boolean is64BitOperatingSystem() {
		return getOperatingSystemArchitectureDataModel()==64;
	}

	/** Replies if the current operating system is 32bit.
	 * 
	 * @return <code>true</code> if the operating system is 32bits, othewise
	 * <code>false</code>
	 */
	public static boolean is32BitOperatingSystem() {
		int dataModel = getOperatingSystemArchitectureDataModel();
		return dataModel==32 || dataModel==0;
	}

	/** Get the OS serial number.
	 * <p>
	 * This function does not allow to run any system command with
	 * the super-user rights, and it disable any additional GUI.
	 * 
	 * @return the serial number associated to the current operating system.
	 */
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
	public static String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		if (osSerialNumber==null) {
			if (nativeWrapper!=null)
				osSerialNumber = nativeWrapper.getOSSerialNumber(enableSuperUser, enableGUI);
			if (osSerialNumber==null)
				osSerialNumber = NULL;
		}
		return osSerialNumber==NULL ? null : osSerialNumber;
	}

	/** Get the OS UUID.
	 * <p>
	 * This function does not allow to run any system command with
	 * the super-user rights, and it disable any additional GUI.
	 * 
	 * @return an unique identifier for the current operating system.
	 */
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
	public static String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		if (osUUID==null) {
			if (nativeWrapper!=null)
				osUUID = nativeWrapper.getOSUUID(enableSuperUser, enableGUI);
			if (osUUID==null)
				osUUID = NULL;
		}
		return osUUID==NULL ? null : osUUID;
	}

	private static OperatingSystemWrapper nativeWrapper; 

	static {
		OperatingSystemIdentificationType type = OperatingSystemIdentificationType.BIOS;
		nativeWrapper = null;

		switch(getCurrentOS()) {
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

		Throwable error = null;
		if (nativeWrapper==null) {
			try {
				LibraryLoader.loadPlatformDependentLibrary(
						"josuuid", //$NON-NLS-1$
						System.getProperty("os.name").trim().toLowerCase(), //$NON-NLS-1$
						"org/arakhne/vmutil"); //$NON-NLS-1$
			}
			catch (Throwable e) {
				error = e;
			}
			if (error==null) {
				nativeWrapper = new OperatingSystemNativeWrapper(type);
			}
		}

		if (nativeWrapper==null) {
			try {
				String errorMsg = Locale.getString("NATIVE_NOT_SUPPORTED"); //$NON-NLS-1$
				if (errorMsg!=null && !"".equals(errorMsg)) { //$NON-NLS-1$
					System.err.println(errorMsg);
				}
				else {
					if (error!=null) error.printStackTrace();
				}
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
