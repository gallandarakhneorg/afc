/* 
 * $Id$
 * 
 * Copyright (C) 2004-2009 Stephane GALLAND.
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * This utility class permits to get the java command line for the current VM.  
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class VMCommandLine {

	private static String classnameToLaunch = null;
	private static boolean analyzed = false;
	private static SortedMap<String,List<Object>> commandLineOptions = null;
	private static String[] commandLineParameters = null;
	
	/** Replies a binary executable filename depending of the current platform.
	 * 
	 * @param name is the name which must be converted into a binary executable filename.
	 * @return the binary executable filename.
	 */
	public static String getExecutableFilename(String name) {
		if (OperatingSystem.WIN.isCurrentOS()) return name+".exe"; //$NON-NLS-1$
		return name;
	}
	
	/** Replies the current java VM binary.
	 * 
	 * @return the binary executable filename that was used to launch the virtual machine.
	 */
	public static String getVMBinary() {
		String java_home = System.getProperty("java.home"); //$NON-NLS-1$
		File bin_dir = new File(new File(java_home),"bin"); //$NON-NLS-1$
		if (bin_dir.isDirectory()) {
			File exec = new File(bin_dir,getExecutableFilename("javaw")); //$NON-NLS-1$
			if (exec.isFile()) {
				return exec.getAbsolutePath();
			}
			exec = new File(bin_dir,getExecutableFilename("java")); //$NON-NLS-1$
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
	 * @throws IOException
	 * @since 6.2 
	 */
	public static Process launchVMWithClassPath(Class<?> classToLaunch, String classpath, String... additionalParams) throws IOException {
		return launchVMWithClassPath(classToLaunch.getCanonicalName(), classpath, additionalParams);
	}

	/** Run a new VM with the given class path.
	 * 
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException 
	 * @since 6.2
	 */
	public static Process launchVMWithClassPath(String classToLaunch, String classpath, String... additionalParams) throws IOException {
		String java_bin = getVMBinary();
		if (java_bin==null) throw new FileNotFoundException("java"); //$NON-NLS-1$
		long totalMemory = Runtime.getRuntime().maxMemory() / 1024;
		String user_dir = FileSystem.getUserHomeDirectoryName();
		String[] params;
		int nParams;
		if (classpath!=null && !"".equals(classpath)) { //$NON-NLS-1$
			nParams = 5;
			params = new String[additionalParams.length+nParams];
			params[0] = java_bin;
			params[1] = "-Xmx"+totalMemory+"k"; //$NON-NLS-1$ //$NON-NLS-2$
			params[2] = "-classpath"; //$NON-NLS-1$
			params[3] = classpath;
			params[4] = classToLaunch;
		}
		else {
			nParams = 3;
			params = new String[additionalParams.length+nParams];
			params[0] = java_bin;
			params[1] = "-Xmx"+totalMemory+"k"; //$NON-NLS-1$ //$NON-NLS-2$
			params[2] = classToLaunch;
		}
		System.arraycopy(additionalParams,0,params,nParams,additionalParams.length);
		return Runtime.getRuntime().exec(params,null,new File(user_dir));
	}

	/** Run a new VM with the given class path.
	 * 
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException
	 * @since 6.2 
	 */
	public static Process launchVMWithClassPath(Class<?> classToLaunch, File[] classpath, String... additionalParams) throws IOException {
		return launchVMWithClassPath(classToLaunch.getCanonicalName(), classpath, additionalParams);
	}

	/** Run a new VM with the given class path.
	 * 
	 * @param classToLaunch is the class to launch.
	 * @param classpath is the class path to use.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException 
	 * @since 6.2
	 */
	public static Process launchVMWithClassPath(String classToLaunch, File[] classpath, String... additionalParams) throws IOException {
		StringBuilder b = new StringBuilder();
		for(File f : classpath) {
			if (b.length()>0) {
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
	 * @throws IOException
	 * @since 6.2 
	 */
	public static Process launchVMWithJar(File jarFile, String... additionalParams) throws IOException {
		String java_bin = getVMBinary();
		if (java_bin==null) throw new FileNotFoundException("java"); //$NON-NLS-1$
		long totalMemory = Runtime.getRuntime().maxMemory() / 1024;
		String user_dir = FileSystem.getUserHomeDirectoryName();
		int nParams = 4;
		String[] params = new String[additionalParams.length+nParams];
			params[0] = java_bin;
			params[1] = "-Xmx"+totalMemory+"k"; //$NON-NLS-1$ //$NON-NLS-2$
			params[2] = "-jar"; //$NON-NLS-1$
			params[3] = jarFile.getAbsolutePath();
		System.arraycopy(additionalParams,0,params,nParams,additionalParams.length);
		return Runtime.getRuntime().exec(params,null,new File(user_dir));
	}

	/** Run a new VM with the class path of the current VM.
	 * 
	 * @param classToLaunch is the class to launch.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException 
	 */
	public static Process launchVM(Class<?> classToLaunch, String... additionalParams) throws IOException {
		return launchVM(classToLaunch.getCanonicalName(), additionalParams);
	}

	/** Run a new VM with the class path of the current VM.
	 * 
	 * @param classToLaunch is the class to launch.
	 * @param additionalParams is the list of additional parameters
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException 
	 * @since 6.2
	 */
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
	public static void saveVMParameters(Class<?> classToLaunch, String... parameters) {
 		saveVMParameters(
 				(classToLaunch!=null)
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
		if (commandLineOptions!=null) commandLineOptions.clear();
		commandLineOptions = null;
		analyzed = false;
	}

	/** Save parameters that permit to relaunch a VM with
	 * {@link #relaunchVM()}.
	 * 
	 * @param classToLaunch is the class which contains a <code>main</code>.
	 * @param parameters is the parameters to pass to the <code>main</code>.
	 */
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
		if (classnameToLaunch==null || "".equals(classnameToLaunch)) { //$NON-NLS-1$
			saveVMParameters(classToLaunch, parameters);
		}
	}

	/** Launch a VM with the same parameters as ones saved by
	 * {@link #saveVMParameters(Class, String[])}.
	 * 
	 * @return the process that is running the new virtual machine, neither <code>null</code>
	 * @throws IOException 
	 */
	public static Process relaunchVM() throws IOException {
		if (classnameToLaunch==null) return null;		
		return launchVM(classnameToLaunch, getAllCommandLineParameters());
	}

	/** Replies the command line including the options and the standard parameters.
	 * 
	 * @return  the command line.
	 */
	public static String[] getAllCommandLineParameters() {
		int osize = commandLineOptions==null ? 0 : commandLineOptions.size();
		int psize = commandLineParameters==null ? 0 : commandLineParameters.length;
		int tsize = (osize>0 && psize>0) ? 1 : 0;
		List<String> params = new ArrayList<>(osize+tsize);
		if (osize>0) {
			List<Object> values;
			String name, prefix, v;
			for(Entry<String,List<Object>> entry : commandLineOptions.entrySet()) {
				name = entry.getKey();
				prefix = (name.length()>1) ? "--" : "-"; //$NON-NLS-1$ //$NON-NLS-2$
				values = entry.getValue();
				if (values==null || values.isEmpty()) {
					params.add(prefix+name);
				}
				else {
					for(Object value : values) {
						if (value!=null) {
							v = value.toString();
							if (v!=null && v.length()>0) {
								params.add(prefix+name+"="+v); //$NON-NLS-1$
							}
							else {
								params.add(prefix+name);
							}
						}
					}
				}
			}
		}
		if (tsize>0) params.add("--"); //$NON-NLS-1$
		
		String[] tab = new String[params.size()+psize];
		params.toArray(tab);
		params.clear();
		
		if (psize>0)
			System.arraycopy(commandLineParameters, 0, tab, osize+tsize, psize);
		
		return tab;
	}

	/** Replies the command line parameters.
	 * 
	 * @return the list of the parameters on the command line
	 */
	public static String[] getCommandLineParameters() {
		return commandLineParameters==null ? new String[0] : commandLineParameters;
	}
	
	/** Shift the command line parameters by one on the left.
	 * The first parameter is removed from the list.
	 * 
	 * @return the removed element or <code>null</code>
	 */
	public static String shiftCommandLineParameters() {
		String removed = null;
		if (commandLineParameters!=null) {
			if (commandLineParameters.length==0) {
				commandLineParameters = null;
			}
			else if (commandLineParameters.length==1) {
				removed = commandLineParameters[0];
				commandLineParameters = null;
			}
			else {
				removed = commandLineParameters[0];
				String[] newTab = new String[commandLineParameters.length-1];
				System.arraycopy(commandLineParameters,1,newTab,0,commandLineParameters.length-1);
				commandLineParameters = newTab;
			}
		}
		return removed;
	}

	/** Replies the command line options.
	 * 
	 * @return the list of options passed on the command line
	 */
	public static Map<String,List<Object>> getCommandLineOptions() {
		if (commandLineOptions!=null)
			return Collections.unmodifiableSortedMap(commandLineOptions);
		return Collections.emptyMap();
	}
	
	/** Replies one command option.
	 *
	 * @param name is the name of the option
	 * @return the option value or <code>null</code> if the option is not on the command line.
	 */
	public static List<Object> getCommandLineOption(String name) {
		if (commandLineOptions!=null) {
			if (commandLineOptions.containsKey(name)) {
				List<Object> value = commandLineOptions.get(name);
				return value==null ? Collections.emptyList() : value;
			}
		}
		return null;
	}

	/** Replies if an option was specified on the command line.
	 *
	 * @param name is the name of the option
	 * @return <code>true</code> if the option was found on the command line, otherwise <code>false</code>.
	 */
	public static boolean hasCommandLineOption(String name) {
		return (commandLineOptions!=null && commandLineOptions.containsKey(name));
	}

	private static boolean registerOptionValue(SortedMap<String,List<Object>> options, String name, Object value, OptionType type) {
		boolean success = true;
		
		Object optValue = value;
		
		List<Object> values = options.get(name);
		if (values==null) {
			values = new ArrayList<>();
			options.put(name, values);
		}
		switch(type) {
		case AUTO_INCREMENTED:
			{
				long v;
				if (values.isEmpty()) v = -1;
				else {
					optValue = values.get(0);
					if (optValue==null) v = 0;
					else if (!(optValue instanceof Number)) {
						v = Long.parseLong(optValue.toString());
					}
					else {
						v = ((Number)optValue).longValue();
					}
				}
				if (values.isEmpty()) values.add(Long.valueOf(v+1));
				else values.set(0, Long.valueOf(v+1));
			}
			break;
		case FLAG:
			if (optValue==null) {
				if (values.isEmpty())
					optValue = Boolean.TRUE;
				else
					optValue = values.get(0);
			}
			else if (!(optValue instanceof Boolean)) {
				optValue = Boolean.parseBoolean(optValue.toString());
			}
			if (values.isEmpty()) values.add(optValue);
			else values.set(0, optValue);
			break;
		case MANDATORY_BOOLEAN:
		case OPTIONAL_BOOLEAN:
			if (optValue==null) {
				optValue = Boolean.TRUE;
			}
			else if (!(optValue instanceof Boolean)) {
				optValue = Boolean.parseBoolean(optValue.toString());
			}
			values.add(optValue);
			break;
		case MANDATORY_FLOAT:
		case OPTIONAL_FLOAT:
			try {
				if (optValue==null) {
					optValue = Double.valueOf(0.);
				}
				else if (!(optValue instanceof Number)) {
					optValue = Double.parseDouble(optValue.toString());
				}
				else {
					optValue = Double.valueOf(((Number)optValue).doubleValue());
				}
			}
			catch(NumberFormatException e) {
				if (type.isOptional()) {
					success = false;
					optValue = Double.valueOf(0.);
				}
				else throw e;
			}
			values.add(optValue);
			break;
		case MANDATORY_INTEGER:
		case OPTIONAL_INTEGER:
			try {
				if (optValue==null) {
					optValue = Long.valueOf(0);
				}
				else if (!(optValue instanceof Number)) {
					optValue = Long.parseLong(optValue.toString());
				}
				else {
					optValue = Long.valueOf(((Number)optValue).longValue());
				}
			}
			catch(NumberFormatException e) {
				if (type.isOptional()) {
					success = false;
					optValue = Long.valueOf(0);
				}
				else throw e;
			}
			values.add(optValue);
			break;
		case MANDATORY_STRING:
		case OPTIONAL_STRING:
			values.add(optValue==null ? "" : optValue.toString()); //$NON-NLS-1$
			break;
		case SIMPLE:
			values.add(optValue);
			break;
		default:
		}
		
		return success;
	}
	
	/** Analyse the command line to extract the options.
	 * <p>
	 * The options will be recognized thanks to the <var>optionDefinitions</var>.
	 * Each entry of <var>optionDefinitions</var> describes an option. They must
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
	public static void splitOptionsAndParameters(String... optionDefinitions) {
		if (analyzed) return;
		
		List<String> params = new ArrayList<>();
		SortedMap<String,List<Object>> options = new TreeMap<>();
		String opt;

		// Analyze definitions
		Map<String,OptionType> defs = new TreeMap<>();
		for (String def : optionDefinitions) {
			if (def.endsWith("!")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-1);
				defs.put(opt, OptionType.FLAG);
				registerOptionValue(options, opt, Boolean.FALSE, OptionType.FLAG);
			}
			else if (def.endsWith("+")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-1);
				defs.put(opt, OptionType.AUTO_INCREMENTED);
				registerOptionValue(options, opt, Long.valueOf(0), OptionType.AUTO_INCREMENTED);
			}
			else if (def.endsWith("=b")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.MANDATORY_BOOLEAN);
			}
			else if (def.endsWith(":b")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.OPTIONAL_BOOLEAN);
			}
			else if (def.endsWith("=f")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.MANDATORY_FLOAT);
			}
			else if (def.endsWith(":f")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.OPTIONAL_FLOAT);
			}
			else if (def.endsWith("=i")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.MANDATORY_INTEGER);
			}
			else if (def.endsWith(":i")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.OPTIONAL_INTEGER);
			}
			else if (def.endsWith("=s")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.MANDATORY_STRING);
			}
			else if (def.endsWith(":s")) { //$NON-NLS-1$
				opt = def.substring(0, def.length()-2);
				defs.put(opt, OptionType.OPTIONAL_STRING);
			}
			else {
				defs.put(def, OptionType.SIMPLE);
			}
		}
		
		int idx;
		String base, nbase, val;
		OptionType type;
		OptionType waitingValue = null;
		String valueOptionName = null;
		boolean allParameters = false;
		boolean success;
		
		for(String param : commandLineParameters) {
			if (allParameters) {
				params.add(param);
				continue;
			}
			
			if (waitingValue!=null && waitingValue.isMandatory()) {
				// Expect a value as the next parameter
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (success) continue;
			}
			
			if ("--".equals(param)) { //$NON-NLS-1$
				if (waitingValue!=null) {
					registerOptionValue(options, valueOptionName, null, waitingValue);
					waitingValue = null;
					valueOptionName = null;
				}
				allParameters = true;
				continue;
			}
			else if ((File.separatorChar!='/')&&(param.startsWith("/"))) { //$NON-NLS-1$
				opt = param.substring(1);
			}
			else if (param.startsWith("--")) { //$NON-NLS-1$
				opt = param.substring(2);
			}
			else if (param.startsWith("-")) { //$NON-NLS-1$
				opt = param.substring(1);
			}
			else if (waitingValue!=null) {
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (!success) params.add(param);
				continue;
			}
			else {
				params.add(param);
				continue;
			}
			
			if (waitingValue!=null) {
				success = registerOptionValue(options, valueOptionName, param, waitingValue);
				waitingValue = null;
				valueOptionName = null;
				if (success) continue;
			}

			idx = opt.indexOf('=');
			if (idx>0) {
				base = opt.substring(0,idx);
				val = opt.substring(idx+1);
			}
			else {
				base = opt;
				val = null;
			}

			nbase = null;
			type = defs.get(base);
			if (type==null && base.toLowerCase().startsWith("no")) { //$NON-NLS-1$
				nbase = base.substring(2);
				type = defs.get(nbase);
			}
			if (type!=null) {
				switch(type) {
				case FLAG:
					if (nbase==null)
						registerOptionValue(options, base, Boolean.TRUE, type);
					else
						registerOptionValue(options, nbase, Boolean.FALSE, type);
					break;
				case MANDATORY_FLOAT:
				case MANDATORY_BOOLEAN:
				case MANDATORY_INTEGER:
				case MANDATORY_STRING:
				case OPTIONAL_FLOAT:
				case OPTIONAL_BOOLEAN:
				case OPTIONAL_INTEGER:
				case OPTIONAL_STRING:
					if (val!=null) {
						registerOptionValue(options, base, val, type);
					}
					else {
						waitingValue = type;
						valueOptionName = base;
					}
					break;
				default:
					registerOptionValue(options, base, val, type);
				}
			}
			else {
				// Not a recognized option, assuming simple
				registerOptionValue(options, base, val, OptionType.SIMPLE);
			}
		}
		
		if (waitingValue!=null && waitingValue.isMandatory()) {
			throw new IllegalStateException("expected a value for command line option "+valueOptionName); //$NON-NLS-1$
		}
		
		commandLineParameters = new String[params.size()];
		params.toArray(commandLineParameters);
		params.clear();

		commandLineOptions = options;
		
		analyzed = true;
	}
	
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
	 * @see #saveVMParametersIfNotSet(String, String[])
	 * @since 6.2
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
	 * @see #saveVMParametersIfNotSet(String, String[])
	 * @see #splitOptionsAndParameters(String[])
	 * @since 6.2
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
		if (classnameToLaunch==null) {
			throw new IllegalArgumentException("you must call the other constructor previously"); //$NON-NLS-1$
		}
	}
	
	/** Replies if the given option is present on the command line.
	 * 
	 * @param optionLabel is the name of the option
	 * @return <code>true</code> if the option is present, otherwise <code>false</code>
	 */
	@SuppressWarnings("static-method")
	public boolean hasOption(String optionLabel) {
		return hasCommandLineOption(optionLabel);
	}

	/** Replies the first value of the option.
	 * 
	 * @param optionLabel is the name of the option
	 * @return the option value or <code>null</code> if the option is not present or has no value.
	 */
	@SuppressWarnings("static-method")
	public Object getFirstOptionValue(String optionLabel) {
		List<Object> options = getCommandLineOption(optionLabel);
		if (options==null || options.isEmpty()) return null;
		return options.get(0);
	}

	/** Replies the values of the option.
	 * 
	 * @param optionLabel is the name of the option
	 * @return the option values or <code>null</code> if the option is not present.
	 */
	@SuppressWarnings("static-method")
	public List<Object> getOptionValues(String optionLabel) {
		List<Object> options = getCommandLineOption(optionLabel);
		if (options==null) return null;
		return Collections.unmodifiableList(options);
	}

	/** Replies the parameters on the command line that are not options.
	 * 
	 * @return the parameters.
	 */
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
	@SuppressWarnings("static-method")
	public int getParameterCount() {
		return getCommandLineParameters().length;
	}

	/** Replies the parameter at the specified index.
	 * 
	 * @param index
	 * @return the value of the parameter.
	 * @throws IndexOutOfBoundsException
	 */
	@SuppressWarnings("static-method")
	public String getParameterAt(int index) {
		return getCommandLineParameters()[index];
	}

	/** Replies if the given index corresponds to a command line parameter.
	 * 
	 * @param index
	 * @return <code>true</code> if the given index corresponds to a parameter,
	 * otherwise <code>false</code>
	 * @throws IndexOutOfBoundsException
	 */
	@SuppressWarnings("static-method")
	public boolean isParameterExists(int index) {
		String[] params = getCommandLineParameters();
		return index>=0 && index<params.length && params[index]!=null;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static enum OptionType {

		/**
		 * a simple option without value or flag.
		 */
		SIMPLE,
		
		/** an option with a mandatory string value.
		 */
		MANDATORY_STRING,

		/** an option with an optional string value.
		 */
		OPTIONAL_STRING,

		/** an option with a mandatory integer value.
		 */
		MANDATORY_INTEGER,

		/** an option with an optional integer value.
		 */
		OPTIONAL_INTEGER,

		/** an option with a mandatory floating-point value.
		 */
		MANDATORY_FLOAT,

		/** an option with an optional floating-point value.
		 */
		OPTIONAL_FLOAT,

		/** an option with a mandatory boolean value.
		 */
		MANDATORY_BOOLEAN,

		/** an option with an optional boolean value.
		 */
		OPTIONAL_BOOLEAN,

		/** an option with an auto-incremented integer value.
		 */
		AUTO_INCREMENTED,

		/** an option which could be flaged or not: {@code --name} or {@code --noname}.
		 */
		FLAG;
		
		/** Replies if the value is mandatory.
		 * 
		 * @return <code>true</code> if the value is mandatory, otherwise <code>false</code>
		 */
		public boolean isMandatory() {
			return this==MANDATORY_BOOLEAN
			|| this==MANDATORY_FLOAT
			|| this==MANDATORY_STRING
			|| this==MANDATORY_INTEGER;
		}
		
		/** Replies if the value is optional.
		 * 
		 * @return <code>true</code> if the value is not mandatory, otherwise <code>false</code>
		 */
		public boolean isOptional() {
			return this==OPTIONAL_BOOLEAN
			|| this==OPTIONAL_FLOAT
			|| this==OPTIONAL_STRING
			|| this==OPTIONAL_INTEGER;
		}

	}

}
