/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.generation.frame;

import java.awt.Component;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

import fr.utbm.set.io.filefilter.FileFilter;

/**
 * Utilities for the parameter frames. 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrameUtil {

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @return the file ro <code>null</code>
	 */
	public static File getOpenFile(Component parent, String text, FileFilter... filters) {
		return getOpenFile(parent, text, null, filters);
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @param selectedFile is the file to select when the dialog box is opened.
	 * @return the file ro <code>null</code>
	 */
	public static File getOpenFile(Component parent, String text, File selectedFile, FileFilter... filters) {
		return getOpenFile(parent, text, selectedFile, new FileFilter(filters));
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @return the file ro <code>null</code>
	 */
	public static File getOpenFile(Component parent, String text, FileFilter filters) {
		return getOpenFile(parent, text, null, filters);
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @param selectedFile is the file to select when the dialog box is opened.
	 * @return the file ro <code>null</code>
	 */
	public static File getOpenFile(Component parent, String text, File selectedFile, FileFilter filters) {
		File currentDirectory = null;
		Preferences prefs = Preferences.userNodeForPackage(FrameUtil.class);
		{ 
			String f = prefs.get("OPEN_FILE", System.getProperty("user.home"));  //$NON-NLS-1$//$NON-NLS-2$
			currentDirectory = new File(f);
		}
		JFileChooser jfc = new JFileChooser(currentDirectory);
		jfc.setFileFilter(filters);
		jfc.setMultiSelectionEnabled(false);
		if (selectedFile!=null) jfc.setSelectedFile(selectedFile);
		jfc.setDialogTitle(text);
		if (jfc.showOpenDialog(parent)==JFileChooser.APPROVE_OPTION) {
			File selFile = jfc.getSelectedFile();
			File directory = selFile.getParentFile();
			prefs.put("OPEN_FILE", directory.getAbsolutePath()); //$NON-NLS-1$
			return selFile;
		}
		return null;
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @return the file ro <code>null</code>
	 */
	public static File getSaveFile(Component parent, String text, FileFilter... filters) {
		return getSaveFile(parent, text, null, filters);
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @param selectedFile is the file to select when the dialog box is opened.
	 * @return the file ro <code>null</code>
	 */
	public static File getSaveFile(Component parent, String text, File selectedFile, FileFilter... filters) {
		return getSaveFile(parent, text, selectedFile, new FileFilter(filters));
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @return the file ro <code>null</code>
	 */
	public static File getSaveFile(Component parent, String text, FileFilter filters) {
		return getSaveFile(parent, text, null, filters);
	}

	/**
	 * Open a dialog box to enter a filename.
	 * 
	 * @param parent is the windows owner
	 * @param text is the title of the dialog box.
	 * @param filters are the file filters to use.
	 * @param selectedFile is the file to select when the dialog box is opened.
	 * @return the file ro <code>null</code>
	 */
	public static File getSaveFile(Component parent, String text, File selectedFile, FileFilter filters) {
		File currentDirectory = null;
		Preferences prefs = Preferences.userNodeForPackage(FrameUtil.class);
		{ 
			String f = prefs.get("OPEN_FILE", System.getProperty("user.home"));  //$NON-NLS-1$//$NON-NLS-2$
			currentDirectory = new File(f);
		}
		JFileChooser jfc = new JFileChooser(currentDirectory);
		jfc.setFileFilter(filters);
		jfc.setMultiSelectionEnabled(false);
		if (selectedFile!=null) jfc.setSelectedFile(selectedFile);
		jfc.setDialogTitle(text);
		if (jfc.showSaveDialog(parent)==JFileChooser.APPROVE_OPTION) {
			File selFile = jfc.getSelectedFile();
			File directory = selFile.getParentFile();
			prefs.put("OPEN_FILE", directory.getAbsolutePath()); //$NON-NLS-1$
			return selFile;
		}
		return null;
	}

}
