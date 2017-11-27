/*
 * $Id$
 * 
 * Copyright (c) 2009, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.io;

import java.io.File;
import java.io.ObjectInputStream;
import java.net.URL;

import javax.activation.MimeTypeParseException;

import org.arakhne.afc.vmutil.locale.Locale;

import fr.utbm.set.io.filefilter.FileFilter;
import fr.utbm.set.io.magic.FileType;
import fr.utbm.set.io.magic.MagicNumber;
import fr.utbm.set.io.magic.MagicNumberStream;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.util.MIMEConstants;

/**
 * File filter for serialized JaSIM Tree models.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DotTreeFileFilter extends FileFilter {

	static {
		//Register MIME file contents
		try {
			FileType.addContentType(new DotTreeMagicNumber());
		}
		catch (MimeTypeParseException _) {
			//
		}
	}

	/** Replies if the specified file contains <code>.tree</code> data.
	 * 
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.tree</code> data,
	 * otherwise <code>false</code>
	 */
	public static boolean isDotTreeFile(File file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_SERIALIZED_TREE.getMimeConstant());
	}
	
	/** Replies if the specified file contains <code>.tree</code> data.
	 * 
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.tree</code> data,
	 * otherwise <code>false</code>
	 */
	public static boolean isDotTreeFile(URL file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_SERIALIZED_TREE.getMimeConstant());
	}

	/** Replies if the specified file contains <code>.tree</code> data.
	 * 
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.tree</code> data,
	 * otherwise <code>false</code>
	 */
	public static boolean isDotTreeFile(String file) {
		return FileType.isContentType(
				file,
				MIMEConstants.MIME_JASIM_SERIALIZED_TREE.getMimeConstant());
	}
	
	/**
	 */
	public DotTreeFileFilter() {
		super(Locale.getString("DESCRIPTION"), //$NON-NLS-1$
				"tree" //$NON-NLS-1$
			);
	}

	/** This clas defines a set of informations that could distinguish
	 * a file content from another one. It is also known as Magic Number
	 * on several operating systems.
	 * <p>
	 * This magic number supports the text content.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class DotTreeMagicNumber extends MagicNumber {

		/**
		 * @throws MimeTypeParseException
		 */
		public DotTreeMagicNumber() throws MimeTypeParseException {
			super(
					MIMEConstants.MIME_JASIM_SERIALIZED_TREE,
					MIMEConstants.MIME_SERIALIZED_OBJECT);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		protected final boolean isContentType(MagicNumberStream stream) {
			try {
				ObjectInputStream ois = new ObjectInputStream(stream);
				Object obj = ois.readObject();
				if (obj!=null) {
					return (obj instanceof PerceptionTree<?,?,?,?,?>);
				}
			}
			catch(Exception _) {
				//
			}
			return false;
		}
		
	}

}
