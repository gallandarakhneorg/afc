/*
 * $Id$
 * 
 * Copyright (c) 2010, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.generation.builder;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.FileSystem;
import org.w3c.dom.Document;

import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.io.xml.XMLUtil;
import fr.utbm.set.ui.filechooser.FileChooser;

/**
 * Read and validate a Collada file to be readable by Live library.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ColladaValidator {

	/**
	 * Check the specified collada file to verfiy its structure, its version and
	 * so on.
	 * 
	 * @param parent is the Swing parent of any opened dialog box.
	 * @param input is the collada file to test.
	 * @param progress is the task progression indicator to use.
	 * @return the URL of the validated collada file.
	 * @throws IOException
	 */
	public static URL validate(Component parent, URL input, Progression progress) throws IOException {
		OutputParameter<Document> document = new OutputParameter<Document>();
		OutputParameter<Boolean> isValid = new OutputParameter<Boolean>();

		URL url = input;
		
		fr.utbm.set.io.collada.ColladaValidator.validate(input, progress, document, isValid);
		
		if (!isValid.get()) {
			File file = FileSystem.convertURLToFile(url);
			FileChooser chooser = new FileChooser(file);
			chooser.setMultiSelectionEnabled(false);
			if (chooser.showSaveDialog(new ColladaFileFilter(), null)==JFileChooser.APPROVE_OPTION) {
				if (progress!=null) progress.setComment("Writing Collada file..."); //$NON-NLS-1$			
				file = chooser.getSelectedFile();
				try {
					XMLUtil.writeXML(document.get(), file);
					url = file.toURI().toURL();
				}
				catch(ParserConfigurationException e) {
					throw new IOException(e);
				}
			}
		}
		
		return url;
	}

}
