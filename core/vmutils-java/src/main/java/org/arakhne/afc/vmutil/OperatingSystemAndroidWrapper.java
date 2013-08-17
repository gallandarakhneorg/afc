/* 
 * $Id$
 * 
 * Copyright (C) 2012-13 Stephane GALLAND.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Wrapper to the Android functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class OperatingSystemAndroidWrapper extends AbstractOperatingSystemWrapper {

	private static final String PREFS_FILE = "device_id.xml"; //$NON-NLS-1$
	private static final String PREFS_DEVICE_ID = "device_id"; //$NON-NLS-1$

	/**
	 */
	public OperatingSystemAndroidWrapper() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		String serial = null;
		try {
			Object androidContext = Android.getContext();
			if (androidContext!=null) {
				Class<?> contextClass = androidContext.getClass();
				Method m;
				Object v;

				// Read the prefs
				try {
					m = contextClass.getMethod("getSharedPreferences", String.class, int.class); //$NON-NLS-1$
					Object prefs = m.invoke(androidContext, PREFS_FILE, 0);
					assert(prefs!=null);

					try {

						m = prefs.getClass().getMethod("getString", String.class, String .class); //$NON-NLS-1$
						v = m.invoke(prefs, PREFS_DEVICE_ID, null);
						if (v!=null) {
							serial = v.toString();
						}
					}
					catch (Throwable _) {
						serial = null;
					}

					// Get the id from the Android constant.
					try {
						if (serial==null) {
							Class<?> secureClass = Android.getSecureSettingsClass();

							Method getStringMethod = secureClass.getMethod("getString", Android.getContextResolverClass(), String.class); //$NON-NLS-1$

							Field androidIdField = secureClass.getField("ANDROID_ID"); //$NON-NLS-1$
							Object androidId = androidIdField.get(null);

							serial = (String)getStringMethod.invoke(null, Android.getContextResolver(), androidId);

							// Use the Android ID unless it's broken, in which case fallback on deviceId,
							// unless it's not available, then fallback on the phone id.
							if ("9774d56d682e549c".equalsIgnoreCase(serial)) { //$NON-NLS-1$
								// This id is known as the broken id on 2.2 devices.
								serial = null;
							}
						}
					}
					catch (Throwable _) {
						serial = null;
					}

					// Read the phone id
					try {
						if (serial==null) {
							m = contextClass.getMethod("getSystemService", String.class); //$NON-NLS-1$
							Field field = contextClass.getField("TELEPHONY_SERVICE"); //$NON-NLS-1$
							Object telephonyManager = m.invoke(androidContext, field.get(null));
							if (telephonyManager!=null) {
								m = telephonyManager.getClass().getMethod("getDeviceId"); //$NON-NLS-1$
								Object rawSerial = m.invoke(telephonyManager);
								if (rawSerial!=null) {
									serial = rawSerial.toString();
								}
							}
						}
					}
					catch (Throwable _) {
						serial = null;
					}

					// Compute a random id and put it in  the prefs.
					if (serial==null) {
						serial = UUID.randomUUID().toString();
						m = prefs.getClass().getMethod("edit"); //$NON-NLS-1$
						v = m.invoke(prefs);
						m = v.getClass().getMethod("putString", String.class, String.class); //$NON-NLS-1$
						v = m.invoke(v, PREFS_DEVICE_ID, serial);
						m = v.getClass().getMethod("commit"); //$NON-NLS-1$
						m.invoke(v);
					}
				}
				catch (Throwable _) {
					serial = null;
				}
			}
		}
		catch (Throwable _) {
			serial = null;
		}

		return serial;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		String serial = getOSSerialNumber(enableSuperUser, enableGUI);
		if (serial!=null) {
			try {
				return UUID.fromString(serial).toString();
			}
			catch(Throwable _) {
				//
			}

			try {
				return UUID.nameUUIDFromBytes(serial.getBytes()).toString();
			}
			catch(Throwable _) {
				//
			}
		}
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
