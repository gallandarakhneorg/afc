/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * Wrapper to the Android functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
class OperatingSystemAndroidWrapper extends AbstractOperatingSystemWrapper {

	private static final String PREFS_FILE = "device_id.xml"; //$NON-NLS-1$

	private static final String PREFS_DEVICE_ID = "device_id"; //$NON-NLS-1$

	/** Construct a wrapper.
	 */
	OperatingSystemAndroidWrapper() {
		//
	}

	private static Object getPreferences(Object context) {
		assert context != null;
		try {
			final Method method = context.getClass().getMethod("getSharedPreferences", String.class, int.class); //$NON-NLS-1$
			final Object prefs = method.invoke(context, PREFS_FILE, 0);
			assert prefs != null;
			return prefs;
		} catch (Exception exception) {
			return null;
		}
	}

	private static String getSerialFromPreferences(Object prefs) {
		try {
			final Method method = prefs.getClass().getMethod("getString", String.class, String .class); //$NON-NLS-1$
			final Object v = method.invoke(prefs, PREFS_DEVICE_ID, null);
			final String label = Objects.toString(v, null);
			if (label != null && !label.isEmpty()) {
				return label;
			}
		} catch (Exception exception) {
			//
		}
		return null;
	}

	private static String getAndroidID() {
		try {
			final Class<?> secureClass = Android.getSecureSettingsClass();
			final Method getStringMethod = secureClass.getMethod("getString", //$NON-NLS-1$
					Android.getContextResolverClass(), String.class);
			final Field androidIdField = secureClass.getField("ANDROID_ID"); //$NON-NLS-1$
			final Object androidId = androidIdField.get(null);
			final Object v = getStringMethod.invoke(null, Android.getContextResolver(), androidId);
			final String label = Objects.toString(v, null);
			// Use the Android ID unless it's broken, in which case fallback on deviceId,
			// unless it's not available, then fallback on the phone id.
			if (label != null && !label.isEmpty() && !"9774d56d682e549c".equalsIgnoreCase(label)) { //$NON-NLS-1$
				// The id above is known as the broken id on 2.2 devices.
				return label;
			}
		} catch (Exception exception) {
			//
		}
		return null;
	}

	private static String getDeviceID(Object context) {
		try {
			final Class<?> contextClass = context.getClass();
			Method method = contextClass.getMethod("getSystemService", String.class); //$NON-NLS-1$
			final Field field = contextClass.getField("TELEPHONY_SERVICE"); //$NON-NLS-1$
			final Object telephonyManager = method.invoke(context, field.get(null));
			if (telephonyManager != null) {
				method = telephonyManager.getClass().getMethod("getDeviceId"); //$NON-NLS-1$
				final Object v = method.invoke(telephonyManager);
				final String label = Objects.toString(v, null);
				if (label != null && !label.isEmpty()) {
					return label;
				}
			}
		} catch (Exception exception) {
			//
		}
		return null;
	}

	private static String randomID(Object prefs) {
		// Compute a random id and put it in  the prefs.
		try {
			final String serial = UUID.randomUUID().toString();
			Method method = prefs.getClass().getMethod("edit"); //$NON-NLS-1$
			Object v = method.invoke(prefs);
			method = v.getClass().getMethod("putString", String.class, String.class); //$NON-NLS-1$
			v = method.invoke(v, PREFS_DEVICE_ID, serial);
			method = v.getClass().getMethod("commit"); //$NON-NLS-1$
			method.invoke(v);
			return serial;
		} catch (Exception exception) {
			//
		}
		return null;
	}

	@SuppressWarnings("checkstyle:npathcomplexity")
	@Override
	public String getOSSerialNumber(boolean enableSuperUser, boolean enableGUI) {
		final Object androidContext;
		try {
			androidContext = Android.getContext();
		} catch (Throwable exception) {
			return null;
		}
		if (androidContext != null) {
			// Read the prefs
			final Object prefs = getPreferences(androidContext);
			if (prefs == null) {
				return null;
			}

			String serial = getSerialFromPreferences(prefs);
			if (serial != null) {
				return serial;
			}

			serial = getAndroidID();
			if (serial != null) {
				return serial;
			}

			serial = getDeviceID(androidContext);
			if (serial != null) {
				return serial;
			}

			return randomID(prefs);
		}

		return null;
	}

	@Override
	public String getOSUUID(boolean enableSuperUser, boolean enableGUI) {
		final String serial = getOSSerialNumber(enableSuperUser, enableGUI);
		if (serial != null) {
			try {
				return UUID.fromString(serial).toString();
			} catch (Throwable exception) {
				//
			}

			try {
				return UUID.nameUUIDFromBytes(serial.getBytes()).toString();
			} catch (Throwable exception) {
				//
			}
		}
		return null;
	}

	@Override
	public OperatingSystemIdentificationType getIdentificationType() {
		return OperatingSystemIdentificationType.OPERATING_SYSTEM;
	}

}
