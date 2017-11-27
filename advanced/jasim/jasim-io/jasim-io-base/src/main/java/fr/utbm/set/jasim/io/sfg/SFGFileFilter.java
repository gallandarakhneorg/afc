/* 
 * $Id$
 * 
 * Copyright (c) 2005-07, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */

package fr.utbm.set.jasim.io.sfg;

import java.io.File;
import java.net.URL;

import javax.activation.MimeTypeParseException;

import org.arakhne.afc.vmutil.locale.Locale;

import fr.utbm.set.io.filefilter.FileFilter;
import fr.utbm.set.io.magic.DTDBasedXMLMagicNumber;
import fr.utbm.set.io.magic.FileType;
import fr.utbm.set.util.MIMEConstants;

/**
 * This class permits to filter the files to show only
 * the Simulation Configuration File. It could be used by a {@link javax.swing.JFileChooser}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SFGFileFilter extends FileFilter implements XMLSimulationConfigConstants {
	
	/** This is the filename extension associated to a SFG file.
	 */
	public static String SFG_EXTENSION = "sfg"; //$NON-NLS-1$

	static {
		//Register MIME file contents
		for(SFGFileType type : SFGFileType.values()) {
			try {
				FileType.addContentType(
						new DTDBasedXMLMagicNumber(
								MIMEConstants.MIME_JASIM_CONFIGURATION.toMIME(),
								type.getSystemIdentifier(),
								type.getPublicIdentifier()));
			}
			catch (MimeTypeParseException _) {
				//
			}
		}
	}
	
	/** Replies if the specified file contains a SFG project
	 * 
	 * @param file
	 * @return <code>true</code> if the given file contains compressed SFG data,
	 * otherwise false.
	 */
	public static boolean isSFGFile(String file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_CONFIGURATION.getMimeConstant());
	}

	/** Replies if the specified file contains a SFG project
	 * 
	 * @param file
	 * @return <code>true</code> if the given file contains compressed SFG data,
	 * otherwise false.
	 */
	public static boolean isSFGFile(File file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_CONFIGURATION.getMimeConstant());
	}

	/** Replies if the specified file contains a SFG project
	 * 
	 * @param file
	 * @return <code>true</code> if the given file contains compressed SFG data,
	 * otherwise false.
	 */
	public static boolean isSFGFile(URL file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_CONFIGURATION.getMimeConstant());
	}

	/**
	 */
	public SFGFileFilter() {
		super(Locale.getString("FILE_DESCRIPTION"), //$NON-NLS-1$
			  SFG_EXTENSION);
	}
	
}