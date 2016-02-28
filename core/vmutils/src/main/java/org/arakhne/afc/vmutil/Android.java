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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * This class stores several information given by
 * the Android operating systems.
 * The stored informations are used by the arakhneVmutil
 * tools to proceed several tasks, such as {@link OperatingSystem}. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
public class Android {

	/** Name of the home directory.
	 */
	public static final String HOME_DIRECTORY = "sdcard"; //$NON-NLS-1$

	/** Name of the system-wide configuration directory.
	 */
	public static final String CONFIGURATION_DIRECTORY = "config"; //$NON-NLS-1$

	/** Name of the system-wide data directory.
	 */
	public static final String DATA_DIRECTORY = "data"; //$NON-NLS-1$

	private static SoftReference<Object> context = null;
	private static WeakReference<Object> contextResolver = null;
	private static WeakReference<ClassLoader> contextClassLoader = null;

	/**
	 */
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
		String fullName;
		if (applicationName.indexOf('.')>=0) {
			fullName = applicationName;
		}
		else {
			fullName = "org.arakhne.partnership."+applicationName; //$NON-NLS-1$
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
			ClassLoader loader = ClassLoaderFinder.findClassLoader();
			return Class.forName("android.content.Context", true, loader); //$NON-NLS-1$
		}
		catch(Throwable e) {
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
			ClassLoader loader = ClassLoaderFinder.findClassLoader();
			return Class.forName("android.content.ContentResolver", true, loader); //$NON-NLS-1$
		}
		catch(Throwable e) {
			throw new AndroidException(e);
		}
	}

	private static Class<?> getInnerClass(String enclosingClassname, String innerClassname) throws AndroidException {
		ClassLoader loader = ClassLoaderFinder.findClassLoader();
		Throwable ex = null;
		try {
			Class<?> innerClass = Class.forName(
					enclosingClassname + "$" + innerClassname, //$NON-NLS-1$
					true, loader);
			return innerClass;
		}
		catch(Throwable e) {
			ex = e;
		}
		try {
			Class<?> enclosingClass = Class.forName(enclosingClassname, true, loader);
			for(Class<?> innerClass : enclosingClass.getClasses()) {
				if (innerClassname.equals(innerClass.getName())) {
					return innerClass;
				}
			}
		}
		catch(Throwable exception) {
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
		assert(androidContext!=null);
		try {
			Class<?> contextType = androidContext.getClass();
			Class<?> contextClass = getContextClass();
			if (!contextClass.isAssignableFrom(contextType))
				throw new AndroidException("not an Android Context class"); //$NON-NLS-1$
			synchronized(Android.class) {
				contextResolver = null;
				context = new SoftReference<>(androidContext);
			}
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable e) {
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
		Object c;
		synchronized(Android.class) {
			if (context==null) throw new AndroidException();
			c = context.get();
		}
		if (c==null) throw new AndroidException();
		return c;
	}

	/** Replies the class loader of the current Android context.
	 * 
	 * @return class loader used by the current Android context.
	 * @throws AndroidException when the context is <code>null</code>.
	 * @see #initialize(Object)
	 */
	public static ClassLoader getContextClassLoader() throws AndroidException {
		ClassLoader cl;
		synchronized(Android.class) {
			cl = (contextClassLoader==null) ? null : contextClassLoader.get();
		}
		if (cl==null) {
			Object context = getContext();
			try {
				Method method = context.getClass().getMethod("getClassLoader"); //$NON-NLS-1$
				Object classLoader = method.invoke(context);
				if (classLoader instanceof ClassLoader) {
					cl = (ClassLoader)classLoader;
					synchronized(Android.class) {
						contextClassLoader = new WeakReference<>(cl);
					}
				}
				else {
					throw new AndroidException();
				}
			}
			catch (Throwable e) {
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
		synchronized(Android.class) {
			resolver = (contextResolver==null) ? null : contextResolver.get();
		}
		if (resolver==null) {
			Object context = getContext();
			try {
				Class<?> resolverType = getContextResolverClass();
				Class<?> contextType = context.getClass();
				Method getContextResolverMethod = contextType.getMethod("getContentResolver"); //$NON-NLS-1$
				resolver = getContextResolverMethod.invoke(context);
				resolver = resolverType.cast(resolver);
				synchronized(Android.class) {
					contextResolver = new WeakReference<>(resolver);
				}
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable e) {
				throw new AndroidException(e);
			}
		}
		return resolver;
	}

	/**
	 * This exception is thrown when the {@link Android} attributes
	 * are not correctly initialized.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 7.0
	 */
	public static class AndroidException extends Exception {

		private static final long serialVersionUID = 1521675695582278476L;

		/**
		 */
		public AndroidException() {
			//
		}

		/**
		 * @param message is the error message.
		 */
		public AndroidException(String message) {
			super(message);
		}

		/**
		 * @param exception is the cause of this exception.
		 */
		public AndroidException(Throwable exception) {
			super(exception);
		}

		/**
		 * @param message is the error message.
		 * @param exception is the cause of this exception.
		 */
		public AndroidException(String message, Throwable exception) {
			super(message, exception);
		}

	} // class AndroidException

}
