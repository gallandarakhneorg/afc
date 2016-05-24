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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * This class stores several information given by
 * the Android operating systems.
 * The stored informations are used by the arakhneVmutil
 * tools to proceed several tasks, such as {@link OperatingSystem}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
public final class Android {

	/** Name of the home directory.
	 */
	public static final String HOME_DIRECTORY = "sdcard"; //$NON-NLS-1$

	/** Name of the system-wide configuration directory.
	 */
	public static final String CONFIGURATION_DIRECTORY = "config"; //$NON-NLS-1$

	/** Name of the system-wide data directory.
	 */
	public static final String DATA_DIRECTORY = "data"; //$NON-NLS-1$

	private static SoftReference<Object> context;

	private static WeakReference<Object> contextResolver;

	private static WeakReference<ClassLoader> contextClassLoader;

	private Android() {
		//
	}

	/** Make a valid android application name from the given application name.
	 * A valid android application name is a package name followed by the name
	 * of the application.
	 *
	 * @param applicationName is the simple application name.
	 * @return the android application name.
	 */
	public static String makeAndroidApplicationName(String applicationName) {
		final String fullName;
		if (applicationName.indexOf('.') >= 0) {
			fullName = applicationName;
		} else {
			fullName = "org.arakhne.partnership." + applicationName; //$NON-NLS-1$
		}
		return fullName;
	}

	/** Replies the class {@code Context} from Android.
	 *
	 * @return the class {@code Context} from Android.
	 * @throws AndroidException when the class cannot be found.
	 */
	public static Class<?> getContextClass() throws AndroidException {
		try {
			final ClassLoader loader = ClassLoaderFinder.findClassLoader();
			return Class.forName("android.content.Context", true, loader); //$NON-NLS-1$
		} catch (Throwable e) {
			throw new AndroidException(e);
		}
	}

	/** Replies the class {@code ContextResolver} from Android.
	 *
	 * @return the class {@code ContextResolver} from Android.
	 * @throws AndroidException when the class cannot be found.
	 */
	public static Class<?> getContextResolverClass() throws AndroidException {
		try {
			final ClassLoader loader = ClassLoaderFinder.findClassLoader();
			return Class.forName("android.content.ContentResolver", true, loader); //$NON-NLS-1$
		} catch (Throwable e) {
			throw new AndroidException(e);
		}
	}

	private static Class<?> getInnerClass(String enclosingClassname, String innerClassname) throws AndroidException {
		final ClassLoader loader = ClassLoaderFinder.findClassLoader();
		Throwable ex = null;
		try {
			return Class.forName(
					enclosingClassname + "$" + innerClassname, //$NON-NLS-1$
					true, loader);
		} catch (Throwable e) {
			ex = e;
		}
		try {
			final Class<?> enclosingClass = Class.forName(enclosingClassname, true, loader);
			for (final Class<?> innerClass : enclosingClass.getClasses()) {
				if (innerClassname.equals(innerClass.getName())) {
					return innerClass;
				}
			}
		} catch (Throwable exception) {
			//
		}
		throw new AndroidException(ex);
	}

	/** Replies the class {@code Secure} from Android.
	 *
	 * @return the class {@code Secure} from Android.
	 * @throws AndroidException when the class cannot be found.
	 */
	public static Class<?> getSecureSettingsClass() throws AndroidException {
		return getInnerClass(
				"android.provider.Settings", //$NON-NLS-1$
				"Secure"); //$NON-NLS-1$
	}

	/** Replies the class {@code Secure} from Android.
	 *
	 * @return the class {@code Secure} from Android.
	 * @throws AndroidException when the class cannot be found.
	 */
	public static Class<?> getSystemSettingsClass() throws AndroidException {
		return getInnerClass(
				"android.provider.Settings", //$NON-NLS-1$
				"System"); //$NON-NLS-1$
	}

	/** Extract informations from the current {@code Context} of the android task.
	 *
	 * @param androidContext is the Android {@code Context}.
	 * @throws AndroidException when the information cannot be extracted.
	 */
	public static void initialize(Object androidContext) throws AndroidException {
		assert androidContext != null;
		try {
			final Class<?> contextType = androidContext.getClass();
			final Class<?> contextClass = getContextClass();
			if (!contextClass.isAssignableFrom(contextType)) {
				throw new AndroidException("not an Android Context class"); //$NON-NLS-1$
			}
			synchronized (Android.class) {
				contextResolver = null;
				context = new SoftReference<>(androidContext);
			}
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e) {
			throw new AndroidException(e);
		}
		ClassLoaderFinder.setPreferredClassLoader(getContextClassLoader());
	}

	/** Replies the current {@code Context} for the android task.
	 *
	 * @return the current {@code Context} for the android task.
	 * @throws AndroidException when the context is <code>null</code>.
	 * @see #initialize(Object)
	 */
	public static Object getContext() throws AndroidException {
		final Object ctx;
		synchronized (Android.class) {
			if (context == null) {
				throw new AndroidException();
			}
			ctx = context.get();
		}
		if (ctx == null) {
			throw new AndroidException();
		}
		return ctx;
	}

	/** Replies the class loader of the current Android context.
	 *
	 * @return class loader used by the current Android context.
	 * @throws AndroidException when the context is <code>null</code>.
	 * @see #initialize(Object)
	 */
	public static ClassLoader getContextClassLoader() throws AndroidException {
		ClassLoader cl;
		synchronized (Android.class) {
			cl = (contextClassLoader == null) ? null : contextClassLoader.get();
		}
		if (cl == null) {
			final Object context = getContext();
			try {
				final Method method = context.getClass().getMethod("getClassLoader"); //$NON-NLS-1$
				final Object classLoader = method.invoke(context);
				try {
					cl = (ClassLoader) classLoader;
					synchronized (Android.class) {
						contextClassLoader = new WeakReference<>(cl);
					}
				} catch (ClassCastException exception) {
					throw new AndroidException(exception);
				}
			} catch (Throwable e) {
				throw new AndroidException(e);
			}
		}
		return cl;
	}

	/** Replies the current {@code ContextResolver} for the android task.
	 *
	 * @return the current {@code ContextResolver} for the android task.
	 * @throws AndroidException when the context is <code>null</code>.
	 * @see #initialize
	 */
	public static Object getContextResolver() throws AndroidException {
		Object resolver;
		synchronized (Android.class) {
			resolver = (contextResolver == null) ? null : contextResolver.get();
		}
		if (resolver == null) {
			final Object context = getContext();
			try {
				final Class<?> resolverType = getContextResolverClass();
				final Class<?> contextType = context.getClass();
				final Method getContextResolverMethod = contextType.getMethod("getContentResolver"); //$NON-NLS-1$
				resolver = getContextResolverMethod.invoke(context);
				resolver = resolverType.cast(resolver);
				synchronized (Android.class) {
					contextResolver = new WeakReference<>(resolver);
				}
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				throw new AndroidException(e);
			}
		}
		return resolver;
	}

	/**
	 * This exception is thrown when the {@link Android} attributes
	 * are not correctly initialized.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 7.0
	 */
	public static class AndroidException extends Exception {

		private static final long serialVersionUID = 1521675695582278476L;

		/** Construct the exception.
		 */
		public AndroidException() {
			//
		}

		/** Construct the exception.
		 *
		 * @param message is the error message.
		 */
		public AndroidException(String message) {
			super(message);
		}

		/** Construct the exception.
		 * @param exception is the cause of this exception.
		 */
		public AndroidException(Throwable exception) {
			super(exception);
		}

		/** Construct the exception.
		 * @param message is the error message.
		 * @param exception is the cause of this exception.
		 */
		public AndroidException(String message, Throwable exception) {
			super(message, exception);
		}

	}

}
