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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This utility class permits to get the java command line for the current VM.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class VMCommandLine {

	private static String classnameToLaunch;

	private static boolean analyzed;

	private static SortedMap<String, List<Object>> commandLineOptions;

	private static String[] commandLineParameters;

	/**
	 * Create a interface to the command line options.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @see #saveVMParametersIfNotSet(Class, String[])
	 */
	public VMCommandLine(Class<?> classToLaunch, String... parameters) {
		saveVMParametersIfNotSet(classToLaunch, parameters);
	}

	/**
	 * Create a interface to the command line options.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @since 6.2
	 * @see #saveVMParametersIfNotSet(String, String[])
	 */
	public VMCommandLine(String classToLaunch, String... parameters) {
		saveVMParametersIfNotSet(classToLaunch, parameters);
	}

	/**
	 * Create a interface to the command line options.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param optionDefinitions is the list of definitions of the available command line options.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @see #saveVMParametersIfNotSet(Class, String[])
	 * @see #splitOptionsAndParameters(String[])
	 */
	public VMCommandLine(Class<?> classToLaunch, String[] optionDefinitions, String... parameters) {
		saveVMParametersIfNotSet(classToLaunch, parameters);
		splitOptionsAndParameters(optionDefinitions);
	}

	/**
	 * Create a interface to the command line options.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param optionDefinitions is the list of definitions of the available command line options.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @since 6.2
	 * @see #saveVMParametersIfNotSet(String, String[])
	 * @see #splitOptionsAndParameters(String[])
	 */
	public VMCommandLine(String classToLaunch, String[] optionDefinitions, String... parameters) {
		saveVMParametersIfNotSet(classToLaunch, parameters);
		splitOptionsAndParameters(optionDefinitions);
	}

	/**
	 * Create a interface to the command line options.
	 *
	 * @see #VMCommandLine(Class, String[], String[])
	 * @see #VMCommandLine(Class, String[])
	 */
	public VMCommandLine() {
		if (classnameToLaunch == null) {
			throw new IllegalArgumentException("you must call the other constructor previously"); //$NON-NLS-1$
		}
	}

	/** Replies a binary executable filename depending of the current platform.
	 *
	 * @param name is the name which must be converted into a binary executable filename.
	 * @return the binary executable filename.
	 */
	@Pure
	public static String getExecutableFilename(String name) {
		if (OperatingSystem.WIN.isCurrentOS()) {
			return name + ".exe"; //$NON-NLS-1$
		}
		return name;
	}

	/** Replies the current java VM binary.
	 *
	 * @return the binary executable filename that was used to launch the virtual machine.
	 */
	@Pure
	public static String getVMBinary() {
		final String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		final File binDir = new File(new File(javaHome), "bin"); //$NON-NLS-1$
		if (binDir.isDirectory()) {
			File exec = new File(binDir, getExecutableFilename("javaw")); //$NON-NLS-1$
			if (exec.isFile()) {
				return exec.getAbsolutePath();
			}
			exec = new File(binDir, getExecutableFilename("java")); //$NON-NLS-1$
			if (exec.isFile()) {
				return exec.getAbsolutePath();
			}
		}
		return null;
	}

	/** Run a new VM with the given class path.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Pure
	@Inline(value = "VMCommandLine.launchVMWithClassPath(($1).getCanonicalName(), ($2), ($3))",
			imported = {VMCommandLine.class})
	public static Process launchVMWithClassPath(Class<?> classToLaunch, String classpath,
			String... additionalParams) throws IOException {
		return launchVMWithClassPath(classToLaunch.getCanonicalName(), classpath, additionalParams);
	}

	/** Run a new VM with the given class path.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Pure
	@SuppressWarnings({"checkstyle:magicnumber"})
	public static Process launchVMWithClassPath(String classToLaunch, String classpath,
			String... additionalParams) throws IOException {
		final String javaBin = getVMBinary();
		if (javaBin == null) {
			throw new FileNotFoundException("java"); //$NON-NLS-1$
		}
		final long totalMemory = Runtime.getRuntime().maxMemory() / 1024;
		final String userDir = FileSystem.getUserHomeDirectoryName();
		final String[] params;
		final int nParams;
		if (classpath != null && !"".equals(classpath)) { //$NON-NLS-1$
			nParams = 5;
			params = new String[additionalParams.length + nParams];
			params[0] = javaBin;
			params[1] = "-Xmx" + totalMemory + "k"; //$NON-NLS-1$ //$NON-NLS-2$
			params[2] = "-classpath"; //$NON-NLS-1$
			params[3] = classpath;
			params[4] = classToLaunch;
		} else {
			nParams = 3;
			params = new String[additionalParams.length + nParams];
			params[0] = javaBin;
			params[1] = "-Xmx" + totalMemory + "k"; //$NON-NLS-1$ //$NON-NLS-2$
			params[2] = classToLaunch;
		}
		System.arraycopy(additionalParams, 0, params, nParams, additionalParams.length);
		return Runtime.getRuntime().exec(params, null, new File(userDir));
	}

	/** Run a new VM with the given class path.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Pure
	@Inline(value = "VMCommandLine.launchVMWithClassPath(($1).getCanonicalName(), ($2), ($3))",
			imported = {VMCommandLine.class})
	public static Process launchVMWithClassPath(Class<?> classToLaunch, File[] classpath,
			String... additionalParams) throws IOException {
		return launchVMWithClassPath(classToLaunch.getCanonicalName(), classpath, additionalParams);
	}

	/** Run a new VM with the given class path.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Pure
	public static Process launchVMWithClassPath(String classToLaunch, File[] classpath,
			String... additionalParams) throws IOException {
		final StringBuilder b = new StringBuilder();
		for (final File f : classpath) {
			if (b.length() > 0) {
				b.append(File.pathSeparator);
			}
			b.append(f.getAbsolutePath());
		}
		return launchVMWithClassPath(classToLaunch, b.toString(), additionalParams);
	}

	/** Run a jar file inside a new VM.
	 *
	 * @param jarFile is the jar file to launch.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static Process launchVMWithJar(File jarFile, String... additionalParams) throws IOException {
		final String javaBin = getVMBinary();
		if (javaBin == null) {
			throw new FileNotFoundException("java"); //$NON-NLS-1$
		}
		final long totalMemory = Runtime.getRuntime().maxMemory() / 1024;
		final String userDir = FileSystem.getUserHomeDirectoryName();
		final int nParams = 4;
		final String[] params = new String[additionalParams.length + nParams];
		params[0] = javaBin;
		params[1] = "-Xmx" + totalMemory + "k"; //$NON-NLS-1$ //$NON-NLS-2$
		params[2] = "-jar"; //$NON-NLS-1$
		params[3] = jarFile.getAbsolutePath();
		System.arraycopy(additionalParams, 0, params, nParams, additionalParams.length);
		return Runtime.getRuntime().exec(params, null, new File(userDir));
	}

	/** Run a new VM with the class path of the current VM.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 */
	@Pure
	@Inline(value = "VMCommandLine.launchVM(($1).getCanonicalName(), ($2))", imported = {VMCommandLine.class})
	public static Process launchVM(Class<?> classToLaunch, String... additionalParams) throws IOException {
		return launchVM(classToLaunch.getCanonicalName(), additionalParams);
	}

	/** Run a new VM with the class path of the current VM.
	 *
	 * @param classToLaunch is the class to launch.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 * @since 6.2
	 */
	@Inline(value = "VMCommandLine.launchVMWithClassPath(($1), System.getProperty(\"java.class.path\"), ($2))",
			imported = {VMCommandLine.class})
	public static Process launchVM(String classToLaunch, String... additionalParams) throws IOException {
		return launchVMWithClassPath(
				classToLaunch,
				System.getProperty("java.class.path"), //$NON-NLS-1$
				additionalParams);
	}

	/** Save parameters that permit to relaunch a VM with
	 * {@link #relaunchVM()}.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 */
	@Inline(value = "VMCommandLine.saveVMParameters((($1) != null) ? ($1).getCanonicalName() : null, ($2))",
			imported = {VMCommandLine.class})
	public static void saveVMParameters(Class<?> classToLaunch, String... parameters) {
 		saveVMParameters(
 				(classToLaunch != null)
 				? classToLaunch.getCanonicalName()
 				: null, parameters);
	}

	/** Save parameters that permit to relaunch a VM with
	 * {@link #relaunchVM()}.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @since 6.2
	 */
	public static void saveVMParameters(String classToLaunch, String... parameters) {
		classnameToLaunch = classToLaunch;
		commandLineParameters = parameters;
		if (commandLineOptions != null) {
			commandLineOptions.clear();
		}
		commandLineOptions = null;
		analyzed = false;
	}

	/** Save parameters that permit to relaunch a VM with
	 * {@link #relaunchVM()}.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 */
	@Inline(value = "VMCommandLine.saveVMParametersIfNotSet(($1).getCanonicalName(), ($2))",
			imported = {VMCommandLine.class})
	public static void saveVMParametersIfNotSet(Class<?> classToLaunch, String... parameters) {
		saveVMParametersIfNotSet(classToLaunch.getCanonicalName(), parameters);
	}

	/** Save parameters that permit to relaunch a VM with
	 * {@link #relaunchVM()}.
	 *
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 * @since 6.2
	 */
	public static void saveVMParametersIfNotSet(String classToLaunch, String... parameters) {
		if (classnameToLaunch == null || "".equals(classnameToLaunch)) { //$NON-NLS-1$
			saveVMParameters(classToLaunch, parameters);
		}
	}

	/** Launch a VM with the same parameters as ones saved by
	 * {@link #saveVMParameters(Class, String[])}.
	 *
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException when a IO error occurs.
	 */
	public static Process relaunchVM() throws IOException {
		if (classnameToLaunch == null) {
			return null;
		}
		return launchVM(classnameToLaunch, getAllCommandLineParameters());
	}

	/** Replies the command line including the options and the standard parameters.
	 *
	 * @return  the command line.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static String[] getAllCommandLineParameters() {
		final int osize = commandLineOptions == null ? 0 : commandLineOptions.size();
		final int psize = commandLineParameters == null ? 0 : commandLineParameters.length;
		final int tsize = (osize > 0 && psize > 0) ? 1 : 0;
		final List<String> params = new ArrayList<>(osize + tsize);
		if (osize > 0) {
			List<Object> values;
			String name;
			String prefix;
			String v;
			for (final Entry<String, List<Object>> entry : commandLineOptions.entrySet()) {
				name = entry.getKey();
				prefix = (name.length() > 1) ? "--" : "-"; //$NON-NLS-1$ //$NON-NLS-2$
				values = entry.getValue();
				if (values == null || values.isEmpty()) {
					params.add(prefix + name);
				} else {
					for (final Object value : values) {
						if (value != null) {
							v = value.toString();
							if (v != null && v.length() > 0) {
								params.add(prefix + name + "=" + v); //$NON-NLS-1$
							} else {
								params.add(prefix + name);
							}
						}
					}
				}
			}
		}
		if (tsize > 0) {
			params.add("--"); //$NON-NLS-1$
		}

		final String[] tab = new String[params.size() + psize];
		params.toArray(tab);
		params.clear();

		if (psize > 0) {
			System.arraycopy(commandLineParameters, 0, tab, osize + tsize, psize);
		}
		return tab;
	}

	/** Replies the command line parameters.
	 *
	 * @return the list of the parameters on the command line
	 */
	@Pure
	public static String[] getCommandLineParameters() {
		return commandLineParameters == null ? new String[0] : commandLineParameters;
	}

	/** Shift the command line parameters by one on the left.
	 * The first parameter is removed from the list.
	 *
	 * @return the removed element or <code>null</code>
	 */
	public static String shiftCommandLineParameters() {
		String removed = null;
		if (commandLineParameters != null) {
			if (commandLineParameters.length == 0) {
				commandLineParameters = null;
			} else if (commandLineParameters.length == 1) {
				removed = commandLineParameters[0];
				commandLineParameters = null;
			} else {
				removed = commandLineParameters[0];
				final String[] newTab = new String[commandLineParameters.length - 1];
				System.arraycopy(commandLineParameters, 1, newTab, 0, commandLineParameters.length - 1);
				commandLineParameters = newTab;
			}
		}
		return removed;
	}

	/** Replies the command line options.
	 *
	 * @return the list of options passed on the command line
	 */
	@Pure
	public static Map<String, List<Object>> getCommandLineOptions() {
		if (commandLineOptions != null) {
			return Collections.unmodifiableSortedMap(commandLineOptions);
		}
		return Collections.emptyMap();
	}

	/** Replies one command option.
	 *
	 * @param name is the name of the option
	 * @return the option value or <code>null</code> if the option is not on the command line.
	 */
	@Pure
	public static List<Object> getCommandLineOption(String name) {
		if (commandLineOptions != null) {
			if (commandLineOptions.containsKey(name)) {
				final List<Object> value = commandLineOptions.get(name);
				return value == null ? Collections.emptyList() : value;
			}
		}
		return null;
	}

	/** Replies if an option was specified on the command line.
	 *
	 * @param name is the name of the option
	 * @return <code>true</code> if the option was found on the command line, otherwise <code>false</code>.
	 */
	@Pure
	public static boolean hasCommandLineOption(String name) {
		return commandLineOptions != null && commandLineOptions.containsKey(name);
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private static boolean registerOptionValue(SortedMap<String, List<Object>> options, String name, Object value,
			OptionType type) {
		boolean success = true;

		Object optValue = value;

		List<Object> values = options.get(name);
		if (values == null) {
			values = new ArrayList<>();
			options.put(name, values);
		}
		switch (type) {
		case AUTO_INCREMENTED:
			final long v;
			if (values.isEmpty()) {
				v = -1;
			} else {
				optValue = values.get(0);
				if (optValue == null) {
					v = 0;
				} else if (!(optValue instanceof Number)) {
					v = Long.parseLong(optValue.toString());
				} else {
					v = ((Number) optValue).longValue();
				}
			}
			if (values.isEmpty()) {
				values.add(Long.valueOf(v + 1));
			} else {
				values.set(0, Long.valueOf(v + 1));
			}
			break;
		case FLAG:
			if (optValue == null) {
				if (values.isEmpty()) {
					optValue = Boolean.TRUE;
				} else {
					optValue = values.get(0);
				}
			} else if (!(optValue instanceof Boolean)) {
				optValue = Boolean.parseBoolean(optValue.toString());
			}
			if (values.isEmpty()) {
				values.add(optValue);
			} else {
				values.set(0, optValue);
			}
			break;
		case MANDATORY_BOOLEAN:
		case OPTIONAL_BOOLEAN:
			if (optValue == null) {
				optValue = Boolean.TRUE;
			} else if (!(optValue instanceof Boolean)) {
				optValue = Boolean.parseBoolean(optValue.toString());
			}
			values.add(optValue);
			break;
		case MANDATORY_FLOAT:
		case OPTIONAL_FLOAT:
			try {
				if (optValue == null) {
					optValue = Double.valueOf(0.);
				} else if (!(optValue instanceof Number)) {
					optValue = Double.parseDouble(optValue.toString());
				} else {
					optValue = Double.valueOf(((Number) optValue).doubleValue());
				}
			} catch (NumberFormatException e) {
				if (type.isOptional()) {
					success = false;
					optValue = Double.valueOf(0.);
				} else {
					throw e;
				}
			}
			values.add(optValue);
			break;
		case MANDATORY_INTEGER:
		case OPTIONAL_INTEGER:
			try {
				if (optValue == null) {
					optValue = Long.valueOf(0);
				} else if (!(optValue instanceof Number)) {
					optValue = Long.parseLong(optValue.toString());
				} else {
					optValue = Long.valueOf(((Number) optValue).longValue());
				}
			} catch (NumberFormatException e) {
				if (type.isOptional()) {
					success = false;
					optValue = Long.valueOf(0);
				} else {
					throw e;
				}
			}
			values.add(optValue);
			break;
		case MANDATORY_STRING:
		case OPTIONAL_STRING:
			values.add(optValue == null ? "" : optValue.toString()); //$NON-NLS-1$
			break;
		case SIMPLE:
			values.add(optValue);
			break;
		default:
		}

		return success;
	}

	/** Analyse the command line to extract the options.
	 *
	 * <p>The options will be recognized thanks to the {@code optionDefinitions}.
	 * Each entry of {@code optionDefinitions} describes an option. They must
	 * have one of the following formats:
	 * <ul>
	 * <li>{@code name}: a simple option without value or flag,</li>
	 * <li>{@code name=s}: an option with a mandatory string value,</li>
	 * <li>{@code name:s}: an option with an optional string value,</li>
	 * <li>{@code name=i}: an option with a mandatory integer value,</li>
	 * <li>{@code name:i}: an option with an optional integer value,</li>
	 * <li>{@code name=f}: an option with a mandatory floating-point value,</li>
	 * <li>{@code name:f}: an option with an optional floating-point value,</li>
	 * <li>{@code name=b}: an option with a mandatory boolean value,</li>
	 * <li>{@code name:b}: an option with an optional boolean value,</li>
	 * <li>{@code name+}: an option with an autoincremented integer value,</li>
	 * <li>{@code name!}: an option which could be flaged or not: {@code --name} or {@code --noname}.</li>
	 * </ul>
	 *
	 * @param optionDefinitions is the list of definitions of the available command line options.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "npathcomplexity"})
	public static void splitOptionsAndParameters(String... optionDefinitions) {
		if (analyzed) {
			return;
		}

		final List<String> params = new ArrayList<>();
		final SortedMap<String, List<Object>> options = new TreeMap<>();
		String opt;

		// Analyze definitions
		final Map<String, OptionType> defs = new TreeMap<>();
		for (final String def : optionDefinitions) {
			if (def.endsWith("!")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 1);
				defs.put(opt, OptionType.FLAG);
				registerOptionValue(options, opt, Boolean.FALSE, OptionType.FLAG);
			} else if (def.endsWith("+")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 1);
				defs.put(opt, OptionType.AUTO_INCREMENTED);
				registerOptionValue(options, opt, Long.valueOf(0), OptionType.AUTO_INCREMENTED);
			} else if (def.endsWith("=b")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.MANDATORY_BOOLEAN);
			} else if (def.endsWith(":b")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.OPTIONAL_BOOLEAN);
			} else if (def.endsWith("=f")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.MANDATORY_FLOAT);
			} else if (def.endsWith(":f")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.OPTIONAL_FLOAT);
			} else if (def.endsWith("=i")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.MANDATORY_INTEGER);
			} else if (def.endsWith(":i")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.OPTIONAL_INTEGER);
			} else if (def.endsWith("=s")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.MANDATORY_STRING);
			} else if (def.endsWith(":s")) { //$NON-NLS-1$
				opt = def.substring(0, def.length() - 2);
				defs.put(opt, OptionType.OPTIONAL_STRING);
			} else {
				defs.put(def, OptionType.SIMPLE);
			}
		}

		int idx;
		String base;
		String nbase;
		String val;
		OptionType type;
		OptionType waitingValue = null;
		String valueOptionName = null;
		boolean allParameters = false;
		boolean success;

		for (final String param : commandLineParameters) {
			if (allParameters) {
				params.add(param);
				continue;
			}

			if (waitingValue != null && waitingValue.isMandatory()) {
				// Expect a value as the next parameter
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (success) {
					continue;
				}
			}

			if ("--".equals(param)) { //$NON-NLS-1$
				if (waitingValue != null) {
					registerOptionValue(options, valueOptionName, null, waitingValue);
					waitingValue = null;
					valueOptionName = null;
				}
				allParameters = true;
				continue;
			} else if ((File.separatorChar != '/') && (param.startsWith("/"))) { //$NON-NLS-1$
				opt = param.substring(1);
			} else if (param.startsWith("--")) { //$NON-NLS-1$
				opt = param.substring(2);
			} else if (param.startsWith("-")) { //$NON-NLS-1$
				opt = param.substring(1);
			} else if (waitingValue != null) {
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (!success) {
					params.add(param);
				}
				continue;
			} else {
				params.add(param);
				continue;
			}

			if (waitingValue != null) {
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (success) {
					continue;
				}
			}

			idx = opt.indexOf('=');
			if (idx > 0) {
				base = opt.substring(0, idx);
				val = opt.substring(idx + 1);
			} else {
				base = opt;
				val = null;
			}

			nbase = null;
			type = defs.get(base);
			if (type == null && base.toLowerCase().startsWith("no")) { //$NON-NLS-1$
				nbase = base.substring(2);
				type = defs.get(nbase);
			}
			if (type != null) {
				switch (type) {
				case FLAG:
					if (nbase == null) {
						registerOptionValue(options, base, Boolean.TRUE, type);
					} else {
						registerOptionValue(options, nbase, Boolean.FALSE, type);
					}
					break;
				case MANDATORY_FLOAT:
				case MANDATORY_BOOLEAN:
				case MANDATORY_INTEGER:
				case MANDATORY_STRING:
				case OPTIONAL_FLOAT:
				case OPTIONAL_BOOLEAN:
				case OPTIONAL_INTEGER:
				case OPTIONAL_STRING:
					if (val != null) {
						registerOptionValue(options, base, val, type);
					} else {
						waitingValue = type;
						valueOptionName = base;
					}
					break;
				case AUTO_INCREMENTED:
				case SIMPLE:
				default:
					registerOptionValue(options, base, val, type);
				}
			} else {
				// Not a recognized option, assuming simple
				registerOptionValue(options, base, val, OptionType.SIMPLE);
			}
		}

		if (waitingValue != null && waitingValue.isMandatory()) {
			throw new IllegalStateException("expected a value for command line option " + valueOptionName); //$NON-NLS-1$
		}

		commandLineParameters = new String[params.size()];
		params.toArray(commandLineParameters);
		params.clear();

		commandLineOptions = options;

		analyzed = true;
	}

	/** Replies if the given option is present on the command line.
	 *
	 * @param optionLabel is the name of the option
	 * @return <code>true</code> if the option is present, otherwise <code>false</code>
	 */
	@Pure
	@SuppressWarnings("static-method")
	public boolean hasOption(String optionLabel) {
		return hasCommandLineOption(optionLabel);
	}

	/** Replies the first value of the option.
	 *
	 * @param optionLabel is the name of the option
	 * @return the option value or <code>null</code> if the option is not present or has no value.
	 */
	@Pure
	@SuppressWarnings("static-method")
	public Object getFirstOptionValue(String optionLabel) {
		final List<Object> options = getCommandLineOption(optionLabel);
		if (options == null || options.isEmpty()) {
			return null;
		}
		return options.get(0);
	}

	/** Replies the values of the option.
	 *
	 * @param optionLabel is the name of the option
	 * @return the option values or <code>null</code> if the option is not present.
	 */
	@Pure
	@SuppressWarnings("static-method")
	public List<Object> getOptionValues(String optionLabel) {
		final List<Object> options = getCommandLineOption(optionLabel);
		if (options == null) {
			return null;
		}
		return Collections.unmodifiableList(options);
	}

	/** Replies the parameters on the command line that are not options.
	 *
	 * @return the parameters.
	 */
	@Pure
	@SuppressWarnings("static-method")
	public String[] getParameters() {
		return getCommandLineParameters();
	}

	/** Shift the command line parameters by one on the left.
	 * The first parameter is removed from the list.
	 *
	 * @return the removed element or <code>null</code>
	 */
	@SuppressWarnings("static-method")
	public String shiftParameters() {
		return shiftCommandLineParameters();
	}

	/** Replies the count of parameters on the command line that are not options.
	 *
	 * @return the count of parameters
	 */
	@Pure
	@SuppressWarnings("static-method")
	public int getParameterCount() {
		return getCommandLineParameters().length;
	}

	/** Replies the parameter at the specified index.
	 *
	 * @param index position of the parameter to reply.
	 * @return the value of the parameter.
	 * @throws IndexOutOfBoundsException if the given index is out of bounds.
	 */
	@Pure
	@SuppressWarnings("static-method")
	public String getParameterAt(int index) {
		return getCommandLineParameters()[index];
	}

	/** Replies if the given index corresponds to a command line parameter.
	 *
	 * @param index position of the parameter to test.
	 * @return <code>true</code> if the given index corresponds to a parameter,
	 *     otherwise <code>false</code>
	 * @throws IndexOutOfBoundsException if the given index is out of bounds.
	 */
	@Pure
	@SuppressWarnings("static-method")
	public boolean isParameterExists(int index) {
		final String[] params = getCommandLineParameters();
		return index >= 0 && index < params.length && params[index] != null;
	}

	/** Type of command line option.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private enum OptionType {

		/**
		 * A simple option without value or flag.
		 */
		SIMPLE,

		/** An option with a mandatory string value.
		 */
		MANDATORY_STRING,

		/** An option with an optional string value.
		 */
		OPTIONAL_STRING,

		/** An option with a mandatory integer value.
		 */
		MANDATORY_INTEGER,

		/** An option with an optional integer value.
		 */
		OPTIONAL_INTEGER,

		/** An option with a mandatory floating-point value.
		 */
		MANDATORY_FLOAT,

		/** An option with an optional floating-point value.
		 */
		OPTIONAL_FLOAT,

		/** An option with a mandatory boolean value.
		 */
		MANDATORY_BOOLEAN,

		/** An option with an optional boolean value.
		 */
		OPTIONAL_BOOLEAN,

		/** An option with an auto-incremented integer value.
		 */
		AUTO_INCREMENTED,

		/** An option which could be flaged or not: {@code --name} or {@code --noname}.
		 */
		FLAG;

		/** Replies if the value is mandatory.
		 *
		 * @return <code>true</code> if the value is mandatory, otherwise <code>false</code>
		 */
		@Pure
		public boolean isMandatory() {
			return this == MANDATORY_BOOLEAN
					|| this == MANDATORY_FLOAT
					|| this == MANDATORY_STRING
					|| this == MANDATORY_INTEGER;
		}

		/** Replies if the value is optional.
		 *
		 * @return <code>true</code> if the value is not mandatory, otherwise <code>false</code>
		 */
		@Pure
		public boolean isOptional() {
			return this == OPTIONAL_BOOLEAN
					|| this == OPTIONAL_FLOAT
					|| this == OPTIONAL_STRING
					|| this == OPTIONAL_INTEGER;
		}

	}

}
