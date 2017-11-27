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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import fr.utbm.set.jasim.generation.builder.FrustumDescription;

/**
 * Window for entering the frustum parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrustumParameterDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 2316334844118966271L;
	
	private static final String ACTION_VALIDATE = "VALIDATE"; //$NON-NLS-1$
	private static final String ACTION_CANCEL = "CANCEL"; //$NON-NLS-1$
	
	private static final int COLUMN_WIDTH = 200;
	
	private boolean validated;
	
	private final FrustumDescription frustumDescription;
	
	private final JTextField farDistance;
	private final JTextField nearDistance;
	private final JSpinner hAngle;
	private final JSpinner vAngle;
	private final JTextField lateralSize;
	private final JSpinner eyeRatio;
		
	/**
	 * @param parent
	 * @param description is the description to edit
	 */
	public FrustumParameterDialog(JDialog parent, FrustumDescription description) {
		super(parent, "Frustum Parameters"); //$NON-NLS-1$
		assert(description!=null);
		this.frustumDescription = description;
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
		this.validated = false;
		
		JLabel label;
		JButton button;

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		getContentPane().add(BorderLayout.CENTER, mainPanel);
				
		{
			SpinnerNumberModel model;
			
			JPanel terrainGroup = new JPanel();
			terrainGroup.setBorder(BorderFactory.createTitledBorder("Terrain")); //$NON-NLS-1$
			terrainGroup.setLayout(new GridBagLayout());
			mainPanel.add(terrainGroup);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			label = new JLabel("Far distance (m):");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 0;
			this.farDistance = new JTextField(Double.toString(this.frustumDescription.farDistance));
			this.farDistance.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.farDistance,c);

			c.gridx = 0;
			c.gridy = 1;
			label = new JLabel("Near distance (m):");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 1;
			this.nearDistance = new JTextField(Double.toString(this.frustumDescription.nearDistance));
			this.nearDistance.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.nearDistance,c);

			c.gridx = 0;
			c.gridy = 2;
			label = new JLabel("Horizontal angle (r):");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 2;
			this.hAngle = new JSpinner();
			model = new SpinnerNumberModel(
					this.frustumDescription.hOpennessAngle,
					0.,
					Math.PI * 2.,
					Math.PI / 10.);
			this.hAngle.setModel(model);
			this.hAngle.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.hAngle,c);

			c.gridx = 0;
			c.gridy = 3;
			label = new JLabel("Vertical angle (r):");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 3;
			this.vAngle = new JSpinner();
			model = new SpinnerNumberModel(
					this.frustumDescription.vOpennessAngle,
					0.,
					Math.PI * 2.,
					Math.PI / 10.);
			this.vAngle.setModel(model);
			this.vAngle.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.vAngle,c);

			c.gridx = 0;
			c.gridy = 4;
			label = new JLabel("Lateral size (m):");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 4;
			this.lateralSize = new JTextField(Double.toString(this.frustumDescription.lateralSize));
			this.lateralSize.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.lateralSize,c);

			c.gridx = 0;
			c.gridy = 5;
			label = new JLabel("Eye ratio:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 5;
			this.eyeRatio = new JSpinner();
			model = new SpinnerNumberModel(
					this.frustumDescription.eyeRatio,
					0.,
					1.,
					.1);
			this.eyeRatio.setModel(model);
			this.eyeRatio.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			terrainGroup.add(this.eyeRatio,c);
		}

		JPanel btPanel = new JPanel();
		btPanel.setLayout(new BoxLayout(btPanel,BoxLayout.X_AXIS));
		getContentPane().add(BorderLayout.SOUTH, btPanel);
		button = new JButton("OK"); //$NON-NLS-1$
		button.setActionCommand(ACTION_VALIDATE);
		button.addActionListener(this);
		btPanel.add(button);
		button = new JButton("Cancel"); //$NON-NLS-1$
		button.setActionCommand(ACTION_CANCEL);
		button.addActionListener(this);
		btPanel.add(button);
		
		// Change enabling state
		if (this.frustumDescription.isSphere()) {
			this.hAngle.setEnabled(false);
			this.vAngle.setEnabled(false);
			this.nearDistance.setEnabled(false);
			this.lateralSize.setEditable(false);
		}
		else if (this.frustumDescription.isPyramid()) {
			this.lateralSize.setEditable(false);
		}
		else if (this.frustumDescription.isPedestrian()) {
			this.lateralSize.setEditable(false);
		}
		else if (this.frustumDescription.isRectangle()) {
			this.hAngle.setEnabled(false);
			this.vAngle.setEnabled(false);
			this.nearDistance.setEnabled(false);
		}
		
		pack();
	}

	/** Replies if the user validate the parameters.
	 * @return <code>true</code> if the "ok" button was pressed, otherwise <code>false</code>
	 */
	public boolean isValidated() {
		return this.validated;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (ACTION_CANCEL.equals(cmd)) {
			this.validated = false;
			setVisible(false);
		}
		else if (ACTION_VALIDATE.equals(cmd)) {
			
			this.frustumDescription.farDistance = getDouble(this.farDistance);
			this.frustumDescription.nearDistance = getDouble(this.nearDistance);
			this.frustumDescription.hOpennessAngle = getDouble(this.hAngle);
			this.frustumDescription.vOpennessAngle = getDouble(this.vAngle);
			this.frustumDescription.lateralSize = getDouble(this.lateralSize);
			this.frustumDescription.eyeRatio = (float)getDouble(this.eyeRatio);
			
			this.validated = true;
			setVisible(false);
		}
	}
	
	private static double getDouble(JTextField field) {
		try {
			return Double.parseDouble(field.getText());
		}
		catch(Throwable _) {
			return 0.;
		}
	}
	
	private static double getDouble(JSpinner field) {
		try {
			return ((Number)field.getValue()).doubleValue();
		}
		catch(Throwable _) {
			return 0.;
		}
	}

}
