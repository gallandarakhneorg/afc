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
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;
import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.jasim.generation.builder.HeightmapPictureType;
import fr.utbm.set.jasim.generation.builder.JasimSimulationHeightmapCreatorConstants;
import fr.utbm.set.ui.IconFactory;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * This dialog is displaying the parameters for a basic heightmap generation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HeightmapParameterDialog extends JOkCancelBtDialog implements DocumentListener {

	private static final long serialVersionUID = -1685679703084971350L;

	private static final String ACTION_SELECT_COORDINATE_SYSTEM = "SELECT_COORDINATE_SYSTEM"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_INPUT_FILE = "BROWSE_INPUT_FILE"; //$NON-NLS-1$

	private static final int COLUMN_WIDTH = 200;

	private final JTextField inputFile;
	private final JButton browseInputFile;
	private final JTextField width;
	private final JTextField height;
	private final JTextField groundZero;
	private final JComboBox coordinateSystem;
	private final JLabel coordinateSystemExample;
	private final JComboBox heightmapType;

	/**
	 * @param parent
	 */
	public HeightmapParameterDialog(Component parent) {
		super(parent, "Heightmap Parameters"); //$NON-NLS-1$
		Preferences prefs = Preferences.userNodeForPackage(HeightmapParameterDialog.class);

		this.topPane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		this.topPane.add(new JLabel("Terrain 3D model"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 0;
		this.inputFile = new JTextField();
		this.inputFile.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
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
		this.topPane.add(new JLabel("Width precision (pixels/meter)"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 1;
		this.width = new JTextField(Double.toString(prefs.getDouble("HEIGHTMAP_WIDTH_PRECISION", //$NON-NLS-1$
				JasimSimulationHeightmapCreatorConstants.DEFAULT_PPM_X)));
		this.width.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.topPane.add(this.width, c);

		c.gridx = 0;
		c.gridy = 2;
		this.topPane.add(new JLabel("Height precision (pixels/meter)"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 2;
		this.height = new JTextField(Double.toString(prefs.getDouble("HEIGHTMAP_HEIGHT_PRECISION", //$NON-NLS-1$
				JasimSimulationHeightmapCreatorConstants.DEFAULT_PPM_Y)));
		this.height.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.topPane.add(this.height, c);

		c.gridx = 0;
		c.gridy = 3;
		this.topPane.add(new JLabel("Not traversable color [0;255]"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 3;
		this.groundZero = new JTextField(Integer.toString(prefs.getInt("HEIGHTMAP_GROUNDZERO_COLOR", //$NON-NLS-1$
				JasimSimulationHeightmapCreatorConstants.DEFAULT_GROUND_ZERO)));
		this.groundZero.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.topPane.add(this.groundZero, c);

		c.gridx = 0;
		c.gridy = 4;
		this.topPane.add(new JLabel("3DS coordinate system"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 4;
		this.coordinateSystem = new JComboBox();
		this.coordinateSystem.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.coordinateSystem.setEditable(false);
		for (CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			this.coordinateSystem.addItem(cs);
		}
		CoordinateSystem3D defaultCS = CoordinateSystem3D.valueOf(prefs.get("3DS_COORDINATE_SYSTEM", CoordinateSystemConstants.SIMULATION_3D.name())); //$NON-NLS-1$
		this.coordinateSystem.setSelectedItem(defaultCS);
		this.coordinateSystem.setActionCommand(ACTION_SELECT_COORDINATE_SYSTEM);
		this.coordinateSystem.addActionListener(this);
		this.topPane.add(this.coordinateSystem, c);

		c.gridx = 2;
		c.gridy = 4;
		this.coordinateSystemExample = new JLabel();
		Dimension dim = new Dimension(100, 100);
		this.coordinateSystemExample.setMinimumSize(dim);
		this.coordinateSystemExample.setMaximumSize(dim);
		this.coordinateSystemExample.setPreferredSize(dim);
		this.topPane.add(this.coordinateSystemExample, c);

		c.gridx = 0;
		c.gridy = 5;
		this.topPane.add(new JLabel("Heightmap type"), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 5;
		this.heightmapType = new JComboBox();
		this.heightmapType.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.heightmapType.setEditable(false);
		for (HeightmapPictureType cs : HeightmapPictureType.values()) {
			this.heightmapType.addItem(cs);
		}
		HeightmapPictureType defaultHPT = HeightmapPictureType.valueOf(prefs.get("HEIGHTMAP_TYPE", HeightmapPictureType.GRAYSCALE.name())); //$NON-NLS-1$
		this.heightmapType.setSelectedItem(defaultHPT);
		this.topPane.add(this.heightmapType, c);

		updateEnableState();
		centerDialog();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_BROWSE_INPUT_FILE.equals(e.getActionCommand())) {
			File file = FrameUtil.getOpenFile(this, "Select heightmap", //$NON-NLS-1$ 
					new ColladaFileFilter());
			if (file != null) {
				this.inputFile.setText(file.getAbsolutePath());
			}
		} else if (ACTION_SELECT_COORDINATE_SYSTEM.equals(e.getActionCommand())) {
			updateCoordinateSystemExample();
		} else {
			super.actionPerformed(e);
		}
	}

	/**
	 * Update the coordinate system illustration.
	 */
	protected void updateCoordinateSystemExample() {
		String name = null;
		switch ((CoordinateSystem3D) this.coordinateSystem.getSelectedItem()) {
		case XYZ_LEFT_HAND:
			name = "/fr/utbm/set/jasim/generation/frame/xyz_left.png"; //$NON-NLS-1$
			break;
		case XYZ_RIGHT_HAND:
			name = "/fr/utbm/set/jasim/generation/frame/xyz_right.png"; //$NON-NLS-1$
			break;
		case XZY_LEFT_HAND:
			name = "/fr/utbm/set/jasim/generation/frame/xzy_left.png"; //$NON-NLS-1$
			break;
		case XZY_RIGHT_HAND:
			name = "/fr/utbm/set/jasim/generation/frame/xzy_right.png"; //$NON-NLS-1$
			break;
		default:
		}
		if (name != null) {
			Icon ic = IconFactory.getIcon(name);
			this.coordinateSystemExample.setIcon(ic);
		}
	}

	/**
	 * Replies the input file.
	 * 
	 * @return the input file.
	 */
	public File getInputFile() {
		String file = this.inputFile.getText();
		if (file != null && !"".equals(file)) { //$NON-NLS-1$
			return new File(file);
		}
		return null;
	}

	/**
	 * Update enabling state of the components.
	 */
	protected void updateEnableState() {
		String file = this.inputFile.getText();
		boolean hasFile = false;
		if (file != null && !"".equals(file)) { //$NON-NLS-1$
			File f = new File(file);
			hasFile = f.canRead();
		}

		this.coordinateSystem.setEnabled(hasFile);
		this.groundZero.setEnabled(hasFile);
		this.height.setEnabled(hasFile);
		this.heightmapType.setEnabled(hasFile);
		this.width.setEnabled(hasFile);
		this.coordinateSystemExample.setEnabled(hasFile);

		setOkButtonEnabled(hasFile);
	}

	/**
	 * Replies the coordinate system.
	 * 
	 * @return the coordinate system.
	 */
	public CoordinateSystem3D getCoordinateSystem() {
		return (CoordinateSystem3D) this.coordinateSystem.getSelectedItem();
	}

	/**
	 * Replies the heightmap type.
	 * 
	 * @return the heightmap type.
	 */
	public HeightmapPictureType getHeightmapType() {
		return (HeightmapPictureType) this.heightmapType.getSelectedItem();
	}

	/**
	 * Replies the width precision.
	 * 
	 * @return the width precision.
	 */
	public float getWidthPrecision() {
		String t = this.width.getText();
		return Float.parseFloat(t);
	}

	/**
	 * Replies the height precision.
	 * 
	 * @return the height precision.
	 */
	public float getHeightPrecision() {
		String t = this.height.getText();
		return Float.parseFloat(t);
	}

	/**
	 * Replies the ground zero color.
	 * 
	 * @return the ground zero color.
	 */
	public int getGroundZero() {
		String t = this.groundZero.getText();
		return Math.min(0, Math.max(255, Integer.parseInt(t)));
	}

	@Override
	protected boolean onOkButtonClicked() {
		Preferences prefs = Preferences.userNodeForPackage(HeightmapParameterDialog.class);
		prefs.putDouble("HEIGHTMAP_WIDTH_PRECISION", getWidthPrecision()); //$NON-NLS-1$
		prefs.putDouble("HEIGHTMAP_HEIGHT_PRECISION", getHeightPrecision()); //$NON-NLS-1$
		prefs.putInt("HEIGHTMAP_GROUNDZERO_COLOR", getGroundZero()); //$NON-NLS-1$
		prefs.put("3DS_COORDINATE_SYSTEM", getCoordinateSystem().name()); //$NON-NLS-1$
		prefs.put("HEIGHTMAP_TYPE", getHeightmapType().name()); //$NON-NLS-1$
		return super.onOkButtonClicked();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateCoordinateSystemExample();
		updateEnableState();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateCoordinateSystemExample();
		updateEnableState();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateCoordinateSystemExample();
		updateEnableState();
	}

}
