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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.utbm.set.io.filefilter.PNGFileFilter;
import fr.utbm.set.jasim.generation.builder.JasimSimulationExtendedHeightmapGenerator;
import fr.utbm.set.jasim.generation.builder.JasimSimulationHeightmapCreatorConstants;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * This dialog is displaying the parameters for a potential field generation. 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RepulsiveHeightmapParameterDialog extends JOkCancelBtDialog implements DocumentListener {

	private static final long serialVersionUID = 6221268105622779856L;

	private static final String ACTION_BROWSE_INPUT_FILE = "BROWSE_INPUT_FILE"; //$NON-NLS-1$
	
	private static final int COLUMN_WIDTH = 200;

	private final JTextField inputFile;
	private final JButton browseInputFile;
	private final JTextField maxRepulsion;
	private final JCheckBox blockingBorder;
	private final JCheckBox generateArrowField;
	private final JTextField groundZero;

	/**
	 * @param parent
	 */
	public RepulsiveHeightmapParameterDialog(Component parent) {
		super(parent,"Potential Field Heightmap"); //$NON-NLS-1$

		this.topPane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		this.topPane.add(new JLabel("Terrain 3D model"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 0;
		this.inputFile = new JTextField();
		this.inputFile.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
		this.inputFile.getDocument().addDocumentListener(this);
		this.topPane.add(this.inputFile, c);

		c.gridx = 2;
		c.gridy = 0;
		this.browseInputFile = new JButton("Browse"); //$NON-NLS-1$
		this.browseInputFile.setActionCommand(ACTION_BROWSE_INPUT_FILE);
		this.browseInputFile.addActionListener(this);
		this.topPane.add(this.browseInputFile, c);
		
		c.gridx = 0;
		c.gridy = 1;
		this.topPane.add(new JLabel("Max repulsion force [1;)"),c); //$NON-NLS-1$
		
		c.gridx = 1;
		c.gridy = 1;
		this.maxRepulsion = new JTextField(Integer.toString(JasimSimulationExtendedHeightmapGenerator.MAX_FORCE_WEIGHT));
		this.maxRepulsion.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
		this.topPane.add(this.maxRepulsion,c);
		
		c.gridx = 0;
		c.gridy = 2;
		this.topPane.add(new JLabel("Image borders"),c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 2;
		this.blockingBorder = new JCheckBox("borders are repulsive"); //$NON-NLS-1$
		this.blockingBorder.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
		this.blockingBorder.setSelected(true);
		this.topPane.add(this.blockingBorder,c);
		
		c.gridx = 0;
		c.gridy = 3;
		this.topPane.add(new JLabel("Potiential field with arrows"),c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 3;
		this.generateArrowField = new JCheckBox("generate picture"); //$NON-NLS-1$
		this.generateArrowField.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
		this.generateArrowField.setSelected(false);
		this.topPane.add(this.generateArrowField,c);

		c.gridx = 0;
		c.gridy = 4;
		this.topPane.add(new JLabel("Not traversable color [0;255]"),c); //$NON-NLS-1$
		
		c.gridx = 1;
		c.gridy = 4;
		this.groundZero = new JTextField(Integer.toString(JasimSimulationHeightmapCreatorConstants.DEFAULT_GROUND_ZERO));
		this.groundZero.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
		this.topPane.add(this.groundZero,c);
		
		updateEnableState();
		centerDialog();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_BROWSE_INPUT_FILE.equals(e.getActionCommand())) {
			File file = FrameUtil.getOpenFile(this, "Select heightmap", //$NON-NLS-1$ 
					new PNGFileFilter());
			if (file!=null) {
				this.inputFile.setText(file.getAbsolutePath());
			}
		}
		else {
			super.actionPerformed(e);
		}
	}

	/** Replies the max repulsion force.
	 * @return the max repulsion force.
	 */
	public int getMaxRepulsionForce() {
		String t = this.maxRepulsion.getText();
		return Integer.parseInt(t);
	}

	/** Replies if the image borders are repulsive.
	 * @return <code>true</code> if borders are repulsive, otherwise <code>false</code>.
	 */
	public boolean isRepulsiveBorders() {
		return this.blockingBorder.isSelected();
	}

	/** Replies if the portential field picture with arrows should be generated.
	 * @return <code>true</code> if the picture should be generated, otherwise <code>false</code>.
	 */
	public boolean isArrowPotentialFieldGenerated() {
		return this.generateArrowField.isSelected();
	}

	/** Replies the ground zero color.
	 * @return the ground zero color.
	 */
	public int getGroundZero() {
		String t = this.groundZero.getText();
		return Math.min(0, Math.max(255, Integer.parseInt(t)));
	}

	/** Replies the input file.
	 * 
	 * @return the input file.
	 */
	public File getInputFile() {
		String file = this.inputFile.getText();
		if (file!=null && !"".equals(file)) { //$NON-NLS-1$
			File f = new File(file);
			if (f.exists()) return f;
		}
		return null;
	}

	/** Update enabling state of the components.
	 */
	protected void updateEnableState() {
		String file = this.inputFile.getText();
		boolean hasFile = false;
		if (file!=null && !"".equals(file)) { //$NON-NLS-1$
			File f = new File(file);
			hasFile = f.canRead();
		}
		
		this.blockingBorder.setEnabled(hasFile);
		this.groundZero.setEnabled(hasFile);
		this.generateArrowField.setEnabled(hasFile);
		this.maxRepulsion.setEnabled(hasFile);
		
		setOkButtonEnabled(hasFile);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateEnableState();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateEnableState();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateEnableState();
	}

}
