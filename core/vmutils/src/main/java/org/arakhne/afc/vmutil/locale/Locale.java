/* 
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * Copyright (C) 2012-13 Stephane GALLAND, Olivier LAMOTTE.
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

package org.arakhne.afc.vmutil.locale;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.arakhne.afc.vmutil.Caller;
import org.arakhne.afc.vmutil.ClassLoaderFinder;

/**
 * This utility class permits a easier use of localized strings.
 * <code>Locale</code> provides a means to retreive
 * messages in the default language. Use this to construct messages
 * displayed for end users.
 * <p>
 * <code>Locale</code> takes a string from a properties resource, 
 * then inserts the parameter strings into the extracted strings
 * at the appropriate places.
 * The pattern matching is proceeded with {@link LocaleMessageFormat}
 * formatter. Note that <code>''</code> may represent a single quote
 * in strings (see {@link LocaleMessageFormat} for details).
 * 
 * FIXME: Does java.text.Normalizer may replace decodeString functions?
 *
 * @author $Author: galland$
 * @author $Author: lamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.4
 */
public class Locale {
	
	private static Class<?> detectResourceClass(Class<?> resource) {
    	if (resource==null) {
        	// Parameter value:
        	// 0: is always Locale.class (ie. this PRIVATE function)
        	// 1: is always Locale.class (ie. the caller of this PRIVATE function)
        	// 2: is the third top of the trace stack ie, the first caller outside Locale.class
    		return Caller.getCallerClass(2);
    	}
    	return resource;
	}
    
    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * The <code>resourcePath</code> argument should be a fully 
     * qualified class name. However, for compatibility with earlier 
     * versions, Sun's Java SE Runtime Environments do not verify this,
     * and so it is possible to access <code>PropertyResourceBundle</code>s
     * by specifying a path name (using "/") instead of a fully 
     * qualified class name (using ".").
     * 
     * @param resourcePath is the name (path) of the resource file, a fully qualified class name
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefaultFrom(String resourcePath, String key, String defaultValue, Object... params) {
        // This method try to use the plugin manager class loader
        // if it exists, otherwhise, it use the default class loader        
    	return getStringWithDefaultFrom(
    			ClassLoaderFinder.findClassLoader(),
    			resourcePath, key, defaultValue, params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * The <code>resourcePath</code> argument should be a fully 
     * qualified class name. However, for compatibility with earlier 
     * versions, Sun's Java SE Runtime Environments do not verify this,
     * and so it is possible to access <code>PropertyResourceBundle</code>s
     * by specifying a path name (using "/") instead of a fully 
     * qualified class name (using ".").
     * 
     * @param classLoader is the class loader to use, a fully qualified class name
     * @param resourcePath is the name (path) of the resource file
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefaultFrom(ClassLoader classLoader, String resourcePath, String key, String defaultValue, Object... params) {

        if (resourcePath == null) return defaultValue;
        
        // Get the resource file
        ResourceBundle resource = null;
        try {
        	resource = ResourceBundle.getBundle(resourcePath,
            		java.util.Locale.getDefault(),
            		classLoader);
        }
        catch (MissingResourceException exep) {
            return defaultValue;
        }
        
        // get the resource string
        String result;
        
        try {
            result = resource.getString(key);
        }
        catch (Exception e) {
            return defaultValue;
        }

        // replace the \n and \r by a real new line character
        result = result.replaceAll("[\\n\\r]","\n"); //$NON-NLS-1$ //$NON-NLS-2$
        result = result.replaceAll("\\t","\t"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // replace the parameter values
        assert(params!=null);
 		return LocaleMessageFormat.format(result, params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * The <code>resourcePath</code> argument should be a fully 
     * qualified class name. However, for compatibility with earlier 
     * versions, Sun's Java SE Runtime Environments do not verify this,
     * and so it is possible to access <code>PropertyResourceBundle</code>s
     * by specifying a path name (using "/") instead of a fully 
     * qualified class name (using ".").
     * 
     * @param resourcePath is the name (path) of the resource file, a fully qualified class name
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringFrom(String resourcePath, String key, Object... params) {
    	return getStringWithDefaultFrom(resourcePath, key, key, params);
    }
    
    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * The <code>resourcePath</code> argument should be a fully 
     * qualified class name. However, for compatibility with earlier 
     * versions, Sun's Java SE Runtime Environments do not verify this,
     * and so it is possible to access <code>PropertyResourceBundle</code>s
     * by specifying a path name (using "/") instead of a fully 
     * qualified class name (using ".").
     *  
     * @param classLoader is the classLoader to use.
     * @param resourcePath is the name (path) of the resource file, a fully qualified class name
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringFrom(ClassLoader classLoader, String resourcePath, String key, Object... params) {
    	return getStringWithDefaultFrom(classLoader,resourcePath, key, key, params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param resource is the name of the resource file
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getString(Class<?> resource, String key, Object... params) {
    	Class<?> res = detectResourceClass(resource);
    	return getString(ClassLoaderFinder.findClassLoader(),res,key,params);
    }
    
    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param classLoader is the class loader to use.
     * @param resource is the name of the resource file
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getString(ClassLoader classLoader, Class<?> resource, String key, Object... params) {
    	Class<?> res = detectResourceClass(resource);
    	if (res==null) return key;
    	String val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    	if (val==null && classLoader!=resource.getClassLoader()) {
    		val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    	}
    	while ((res!=null)&&(val==null)) {
    		res = res.getSuperclass();
    		if (res!=null) {
    			val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    		}
    	}    	
    	if (val==null) return key;
    	return val;
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param resource is the name of the resource file
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefault(Class<?> resource, String key, String defaultValue, Object... params) {
    	Class<? >res = detectResourceClass(resource);
    	return getStringWithDefault(ClassLoaderFinder.findClassLoader(),res,key,defaultValue,params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param classLoader is the class loader to use.
     * @param resource is the name of the resource file
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefault(ClassLoader classLoader, Class<?> resource, String key, String defaultValue, Object... params) {
    	Class<?> res = detectResourceClass(resource);
    	if (res==null) return defaultValue;
    	String val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    	if (val==null && classLoader!=resource.getClassLoader()) {
    		val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    	}
    	while ((res!=null)&&(val==null)) {
    		res = res.getSuperclass();
    		if (res!=null) {
    			val = getStringWithDefaultFrom(classLoader,res.getCanonicalName(),key,null,params);
    		}
    	}    	
    	if (val==null) return defaultValue;
    	return val;
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * This function assumes the classname of the caller as the
     * resource provider. 
     * 
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getString(String key, Object... params) {
    	Class<?> resource = detectResourceClass(null);
    	return getString(ClassLoaderFinder.findClassLoader(),resource,key,params);
    }
    
    /**
     * Replies the text that corresponds to the specified resource.
     * <p>
     * This function assumes the classname of the caller as the
     * resource provider. 
     * 
     * @param classLoader is the classLoader to use.
     * @param key is the name of the resource into the specified file
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getString(ClassLoader classLoader, String key, Object... params) {
    	Class<?> resource = detectResourceClass(null);
    	return getString(classLoader,resource,key,params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefault(String key, String defaultValue, Object... params) {
    	Class<?> resource = detectResourceClass(null);
    	return getStringWithDefault(ClassLoaderFinder.findClassLoader(),resource,key,defaultValue,params);
    }

    /**
     * Replies the text that corresponds to the specified resource.
     * 
     * @param classLoader is the class loader to use.
     * @param key is the name of the resource into the specified file
     * @param defaultValue is the default value to replies if the resource does not contain the specified key. 
     * @param params is the the list of parameters which will
     * replaces the <code>#1</code>, <code>#2</code>... into the string.
     * @return the text that corresponds to the specified resource
     */
    public static String getStringWithDefault(ClassLoader classLoader, String key, String defaultValue, Object... params) {
    	Class<?> resource = detectResourceClass(null);
    	return getStringWithDefault(classLoader,resource,key,defaultValue,params);
    }

    /** Decode the specified array of bytes according to
     * a charset selection. This function tries
     * to decode a string from the given byte array
     * with the following charsets (in preferred order):
     * <ul>
     * <li>the current charset returned by {@link Charset#defaultCharset()},</li>
     * <li>OEM United States: IBM437,</li>
     * <li>West European: ISO-8859-1,</li>
     * <li>one of the charst returned by {@link Charset#availableCharsets()}.</li>
     * </ul>
     * <p>
     * The IBM437 charset was added to support several specific files (Dbase files)
     * generated from a GIS.
     * 
     * @param bytes is the array of bytes to decode.
     * @return the decoded string with the appropriate charset set.
     */
    public static String decodeString(byte[] bytes) {
    	Charset default_charset = Charset.defaultCharset();
    	Charset west_european = Charset.forName("ISO-8859-1"); //$NON-NLS-1$
    	Charset utf = Charset.forName("UTF-8"); //$NON-NLS-1$
    	
    	String refBuffer = new String(bytes);
    	
		CharBuffer buffer = decodeString(bytes,default_charset,refBuffer.length());

		if ((buffer==null)&&(!default_charset.equals(west_european))) {
			buffer = decodeString(bytes,west_european,refBuffer.length());
		}
    	
		if ((buffer==null)&&(!default_charset.equals(utf))) {
			buffer = decodeString(bytes,utf,refBuffer.length());
		}

		if (buffer==null) {
    		// Decode until one of the available charset replied a value
	    	for (Charset charset : Charset.availableCharsets().values()) {
				buffer = decodeString(bytes,charset,refBuffer.length());
				if (buffer!=null) break;
	    	}
    	}
    	// Use the default encoding
    	if (buffer==null) return refBuffer;
    	return buffer.toString();
    }
   
    /** Decode the specified array of bytes with the specified charset.
     * 
     * @param bytes is the array of bytes to decode.
     * @param charset is the charset to use for decoding
     * @param referenceLength is the length of the attempted result. If negative, this parameter is ignored.
     * @return the decoded string with the appropriate charset set,
     * or <code>null</code> if the specified charset cannot be
     * used to decode all the characters inside the byte array.
     */
    private static CharBuffer decodeString(byte[] bytes, Charset charset, int referenceLength) {
    	try {
    		Charset autodetected_charset = charset;
        	CharsetDecoder decoder = charset.newDecoder();
			CharBuffer buffer = decoder.decode(ByteBuffer.wrap(bytes));
			if ((decoder.isAutoDetecting())&&
				(decoder.isCharsetDetected())) {
				autodetected_charset = decoder.detectedCharset();
				if (charset.contains(autodetected_charset)) {
					buffer.position(0);
					if ((referenceLength>=0)&&(buffer.remaining()==referenceLength))
						return buffer;
					return null;
				}
			}
			// Apply a proprietaty detection
			buffer.position(0);
			char c;
			int type;
			while (buffer.hasRemaining()) {
				c = buffer.get();
				type = Character.getType(c);
	        	switch(type) {
	        	case Character.UNASSIGNED:
	        	case Character.CONTROL:
	        	case Character.FORMAT:
	        	case Character.PRIVATE_USE:
	        	case Character.SURROGATE:
		        	// Character not supported?
	        		return null;
	        	default:
	        	}
			}
			buffer.position(0);
			if ((referenceLength>=0)&&(buffer.remaining()==referenceLength))
				return buffer;
		}
    	catch (CharacterCodingException e) {
			//
		}
    	return null;
    }
    
    /** Decode the bytes from the specified input stream
     * according to a charset selection. This function tries
     * to decode a string from the given byte array
     * with the following charsets (in preferred order):
     * <ul>
     * <li>the current charset returned by {@link Charset#defaultCharset()},</li>
     * <li>OEM United States: IBM437,</li>
     * <li>West European: ISO-8859-1,</li>
     * <li>one of the charst returned by {@link Charset#availableCharsets()}.</li>
     * </ul>
     * <p>
     * The IBM437 charset was added to support several specific files (Dbase files)
     * generated from a GIS.
     * 
     * @param stream is the stream to decode.
     * @return the decoded string with the appropriate charset set.
     * @throws IOException 
     */
    public static String decodeString(InputStream stream) throws IOException {
    	byte[] complete_buffer = new byte[0];
    	byte[] buffer = new byte[2048];
    	int red;
    	
    	while ((red=stream.read(buffer))>0) {
    		byte[] tmp = new byte[complete_buffer.length+red];
    		System.arraycopy(complete_buffer,0,tmp,0,complete_buffer.length);
    		System.arraycopy(buffer,0,tmp,complete_buffer.length,red);
    		complete_buffer = tmp;
    		tmp = null;
    	}
    	
    	String s = decodeString(complete_buffer);
    	complete_buffer = null;
    	buffer = null;
    	return s;
    }

    /** Decode the bytes from the specified input stream
     * according to a charset selection. This function tries
     * to decode a string from the given byte array
     * with the following charsets (in preferred order):
     * <ul>
     * <li>the current charset returned by {@link Charset#defaultCharset()},</li>
     * <li>OEM United States: IBM437,</li>
     * <li>West European: ISO-8859-1,</li>
     * <li>one of the charst returned by {@link Charset#availableCharsets()}.</li>
     * </ul>
     * <p>
     * The IBM437 charset was added to support several specific files (Dbase files)
     * generated from a GIS.
     * <p>
     * This function read the input stream line by line.
     * 
     * @param stream is the stream to decode.
     * @param lineArray is the array of lines to fill
     * @return <code>true</code> is the decoding was successful,
     * otherwhise <code>false</code>
     * @throws IOException 
     */
    public static boolean decodeString(InputStream stream, List<String> lineArray) throws IOException {
    	// Read the complete file
    	byte[] complete_buffer = new byte[0];
    	byte[] buffer = new byte[2048];
    	int red;
    	
    	while ((red=stream.read(buffer))>0) {
    		byte[] tmp = new byte[complete_buffer.length+red];
    		System.arraycopy(complete_buffer,0,tmp,0,complete_buffer.length);
    		System.arraycopy(buffer,0,tmp,complete_buffer.length,red);
    		complete_buffer = tmp;
    		tmp = null;
    	}

    	buffer = null;
    	
    	// Get the two default charsets
    	//Charset oem_us = Charset.forName("IBM437"); //$NON-NLS-1$
    	Charset west_european = Charset.forName("ISO-8859-1"); //$NON-NLS-1$
    	Charset default_charset = Charset.defaultCharset();
    	
    	// Decode with the current charset
    	boolean ok = decodeString(new ByteArrayInputStream(complete_buffer),lineArray,default_charset);
    	
    	// Decode with the default oem US charset
    	/*if ((!ok)&&(!default_charset.equals(oem_us))) {
    		ok = decodeString(new ByteArrayInputStream(complete_buffer),lineArray,oem_us); 
    	}*/

    	// Decode with the default west european charset
    	if ((!ok)&&(!default_charset.equals(west_european))) {
    		ok = decodeString(new ByteArrayInputStream(complete_buffer),lineArray,west_european); 
    	}
    	
		// Decode until one of the available charset replied a value
    	if (!ok) {
	    	for (Entry<String,Charset> charset : Charset.availableCharsets().entrySet()) {
				if (decodeString(new ByteArrayInputStream(complete_buffer),lineArray,charset.getValue())) {
					complete_buffer = null;
					return true;
				}
			}
    	}

		complete_buffer = null;
    	return ok;
    }

    /** Decode the bytes from the specified input stream
     * according to a charset selection. This function tries
     * to decode a string from the given byte array
     * with the following charsets (in preferred order):
     * <p>
     * This function read the input stream line by line.
     * 
     * @param stream is the stream to decode.
     * @param lineArray is the array of lines to fill.
     * @param charset is the charset to use.
     * @return <code>true</code> is the decoding was successful,
     * otherwhise <code>false</code>
     * @throws IOException 
     */
    private static boolean decodeString(InputStream stream, List<String> lineArray, Charset charset) throws IOException {
    	try {
    		BufferedReader breader = new BufferedReader(
    				new InputStreamReader(stream,charset.newDecoder()));
    		
    		lineArray.clear();
    		
    		String line;
    		while ((line=breader.readLine())!=null) {
    			lineArray.add(line);
    		}
    		
    		return true;
    	}
    	catch(CharacterCodingException exception) {
    		//
    	}
    	return false;
    }
    
}