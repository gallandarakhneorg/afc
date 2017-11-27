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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.arakhne.afc.vmutil.FileSystem;

import fr.utbm.set.collection.SizedIterator;
import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.io.filefilter.INIFileFilter;
import fr.utbm.set.io.filefilter.XMLFileFilter;
import fr.utbm.set.ui.IconFactory;
import fr.utbm.set.ui.IconSize;
import fr.utbm.set.ui.PredefinedIcon;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * Window for entering the goal and waypoint parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GoalWaypointParameterDialog
extends JOkCancelBtDialog
implements ListSelectionListener,
           DocumentListener {

	private static final long serialVersionUID = -6218707911606827433L;

	private static final String ACTION_ADD_3D_FILE = "ADD_3D_FILE"; //$NON-NLS-1$
	private static final String ACTION_LOAD_CONFIG = "LOAD_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_REMOVE_3D_FILE = "REMOVE_3D_FILE"; //$NON-NLS-1$
	private static final String ACTION_SAVE_CONFIG = "SAVE_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_3D_MODEL = "BROWSE_3D_MODEL"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_OUTPUT_FILE = "BROWSE_OUTPUT_FILE"; //$NON-NLS-1$
	private static final String ACTION_SELECT_GOAL = "SELECT_GOAL"; //$NON-NLS-1$
	private static final String ACTION_SELECT_WAYPOINT = "SELECT_WAYPOINT"; //$NON-NLS-1$
	private static final String ACTION_SELECT_OUTPUT_CLIPBOARD = "SELECT_OUTPUT_CLIPBOARD"; //$NON-NLS-1$
	private static final String ACTION_SELECT_OUTPUT_FILE = "SELECT_OUTPUT_FILE"; //$NON-NLS-1$
	
	private static final Icon ICON_GOAL = IconFactory.getIcon("/fr/utbm/set/jasim/generation/frame/goal.png"); //$NON-NLS-1$
	private static final Icon ICON_WAYPOINT = IconFactory.getIcon("/fr/utbm/set/jasim/generation/frame/waypoint.png"); //$NON-NLS-1$

	private static final int COLUMN_WIDTH = 200;

	private final JButton add3DFile;
	private final JButton remove3DFile;
	private final JButton loadConfig;
	private final JButton saveConfig;
	private final DefaultListModel model3dListModel = new DefaultListModel();
	private final JList model3dList;
	private final JTextField model3dFile;
	private final JButton browse3dModel;
	private final JRadioButton goalType;
	private final JRadioButton waypointType;
	private final JRadioButton outputClipboardType;
	private final JRadioButton outputFileType;
	private final JTextField outputFile;
	private final JButton browseOutputFile;

	/**
	 * @param parent
	 */
	public GoalWaypointParameterDialog(Component parent) {
		super(parent, "Goal and Waypoint Parameters"); //$NON-NLS-1$
		setModal(true);
		this.topPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel entityGroup = new JPanel();
		entityGroup.setBorder(BorderFactory.createTitledBorder("World Content")); //$NON-NLS-1$
		entityGroup.setLayout(new BorderLayout());
		this.topPane.add(BorderLayout.CENTER, entityGroup);

		JPanel leftPanel = new JPanel(new BorderLayout());
		entityGroup.add(leftPanel, BorderLayout.WEST);

		JPanel controlBtPanel = new JPanel();
		leftPanel.add(controlBtPanel, BorderLayout.NORTH);
		this.add3DFile = new JButton(PredefinedIcon.ADD.getIcon(IconSize.getButtonSize()));
		this.add3DFile.setActionCommand(ACTION_ADD_3D_FILE);
		this.add3DFile.addActionListener(this);
		controlBtPanel.add(this.add3DFile);
		this.remove3DFile = new JButton(PredefinedIcon.REMOVE.getIcon(IconSize.getButtonSize()));
		this.remove3DFile.setActionCommand(ACTION_REMOVE_3D_FILE);
		this.remove3DFile.addActionListener(this);
		controlBtPanel.add(this.remove3DFile);
		this.loadConfig = new JButton(PredefinedIcon.LOAD.getIcon(IconSize.getButtonSize()));
		this.loadConfig.setActionCommand(ACTION_LOAD_CONFIG);
		this.loadConfig.addActionListener(this);
		controlBtPanel.add(this.loadConfig);
		this.saveConfig = new JButton(PredefinedIcon.SAVE.getIcon(IconSize.getButtonSize()));
		this.saveConfig.setActionCommand(ACTION_SAVE_CONFIG);
		this.saveConfig.addActionListener(this);
		controlBtPanel.add(this.saveConfig);

		this.model3dList = new JList(this.model3dListModel);
		this.model3dList.getSelectionModel().addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(this.model3dList);
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new GridBagLayout());
		entityGroup.add(parameterPanel, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		JLabel label = new JLabel("3D model:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 0;
		this.model3dFile = new JTextField();
		this.model3dFile.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.model3dFile.getDocument().addDocumentListener(this);
		parameterPanel.add(this.model3dFile, c);

		c.gridx = 2;
		c.gridy = 0;
		this.browse3dModel = new JButton("Browse..."); //$NON-NLS-1$
		this.browse3dModel.addActionListener(this);
		this.browse3dModel.setActionCommand(ACTION_BROWSE_3D_MODEL);
		parameterPanel.add(this.browse3dModel, c);

		ButtonGroup typeGroup = new ButtonGroup();
		
		c.gridx = 0;
		c.gridy = 1;
		this.goalType = new JRadioButton("Goal"); //$NON-NLS-1$
		this.goalType.addActionListener(this);
		this.goalType.setActionCommand(ACTION_SELECT_GOAL);
		typeGroup.add(this.goalType);
		parameterPanel.add(this.goalType, c);

		c.gridx = 1;
		c.gridy = 1;
		label = new JLabel(ICON_GOAL);
		parameterPanel.add(label, c);
		
		c.gridx = 0;
		c.gridy = 2;
		this.waypointType = new JRadioButton("Waypoint"); //$NON-NLS-1$
		this.waypointType.addActionListener(this);
		this.waypointType.setActionCommand(ACTION_SELECT_WAYPOINT);
		typeGroup.add(this.waypointType);
		parameterPanel.add(this.waypointType, c);
		
		c.gridx = 1;
		c.gridy = 2;
		label = new JLabel(ICON_WAYPOINT);
		parameterPanel.add(label, c);

		this.goalType.setSelected(true);

		JPanel treeGroup = new JPanel();
		treeGroup.setBorder(BorderFactory.createTitledBorder("Output")); //$NON-NLS-1$
		treeGroup.setLayout(new GridBagLayout());
		this.topPane.add(BorderLayout.SOUTH, treeGroup);

		ButtonGroup outputGroup = new ButtonGroup();
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		this.outputClipboardType = new JRadioButton("Copy into clipboard"); //$NON-NLS-1$
		this.outputClipboardType.addActionListener(this);
		this.outputClipboardType.setActionCommand(ACTION_SELECT_OUTPUT_CLIPBOARD);
		outputGroup.add(this.outputClipboardType);
		treeGroup.add(this.outputClipboardType, c);

		c.gridx = 0;
		c.gridy = 1;
		this.outputFileType = new JRadioButton("Save in file"); //$NON-NLS-1$
		this.outputFileType.addActionListener(this);
		this.outputFileType.setActionCommand(ACTION_SELECT_OUTPUT_FILE);
		outputGroup.add(this.outputFileType);
		treeGroup.add(this.outputFileType, c);
		
		this.outputClipboardType.setSelected(true);

		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.outputFile = new JTextField();
		this.outputFile.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.outputFile.getDocument().addDocumentListener(this);
		treeGroup.add(this.outputFile, c);

		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		this.browseOutputFile = new JButton("Browse"); //$NON-NLS-1$
		this.browseOutputFile.setActionCommand(ACTION_BROWSE_OUTPUT_FILE);
		this.browseOutputFile.addActionListener(this);
		treeGroup.add(this.browseOutputFile, c);
		
		updateEnableState();
		centerDialog();
	}

	/**
	 * Replies the descriptions.
	 * 
	 * @return the descriptions.
	 */
	public SizedIterator<GoalWaypointDescription> getDescriptions() {
		return new EntityIterator(this.model3dListModel);
	}

	/**
	 * Replies the output file.
	 * 
	 * @return the output file or <code>null</code>.
	 */
	public File getOutputFile() {
		if (this.outputFileType.isSelected()) {
			String filename = this.outputFile.getText();
			if (filename != null && !"".equals(filename)) { //$NON-NLS-1$
				return new File(filename);
			}
		}
		return null;
	}

	/**
	 * Replies if the output may be put inside clipboard.
	 * 
	 * @return <code>true</code> if the output may be put inside
	 * the clipboard, otherwise <code>false</code>
	 */
	public boolean isOutputToClipboard() {
		return (this.outputClipboardType.isSelected());
	}

	/**
	 */
	protected void updateEnableState() {
		boolean hasModel = !this.model3dListModel.isEmpty();
		boolean hasSelectedModel = this.model3dList.getSelectedIndex()>=0;
	
		boolean hasOutput =
			this.outputClipboardType.isSelected()
			|| (this.outputFileType.isSelected()
					&& !"".equals(this.outputFile.getText())); //$NON-NLS-1$
		
		boolean isFileOutput = this.outputFileType.isSelected();
		
		this.add3DFile.setEnabled(true);
		this.remove3DFile.setEnabled(hasSelectedModel);
		this.loadConfig.setEnabled(true);
		this.saveConfig.setEnabled(hasModel);
		this.model3dList.setEnabled(true);
				
		this.model3dFile.setEnabled(hasSelectedModel);
		this.browse3dModel.setEnabled(hasSelectedModel);
		this.goalType.setEnabled(hasSelectedModel);
		this.waypointType.setEnabled(hasSelectedModel);
		
		this.outputClipboardType.setEnabled(true);
		this.outputFileType.setEnabled(true);
		this.outputFile.setEnabled(isFileOutput);
		this.browseOutputFile.setEnabled(isFileOutput);		
		
		setOkButtonEnabled(hasModel && hasOutput);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (ACTION_ADD_3D_FILE.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this, "Select 3D model", //$NON-NLS-1$
					(File) null, new ColladaFileFilter());
			if (file != null) {
				GoalWaypointDescription desc = new GoalWaypointDescription();
				desc.modelFile = file;
				this.model3dListModel.addElement(desc);
				this.model3dList.setSelectedValue(desc, true);
			}
		}
		else if (ACTION_REMOVE_3D_FILE.equals(cmd)) {
			int index = this.model3dList.getSelectedIndex();
			if (index >= 0) {
				this.model3dListModel.remove(index);
			}
		}
		else if (ACTION_LOAD_CONFIG.equals(cmd)) {
			try {
				loadConfig();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		}
		else if (ACTION_SAVE_CONFIG.equals(cmd)) {
			try {
				saveConfig();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		}
		else if (ACTION_BROWSE_3D_MODEL.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this, "Select object model", //$NON-NLS-1$
					new File(this.model3dFile.getText()), new ColladaFileFilter());
			if (file != null) {
				this.model3dFile.setText(file.getAbsolutePath());
			}
		}
		else if (ACTION_BROWSE_OUTPUT_FILE.equals(cmd)) {
			File file = FrameUtil.getSaveFile(this, "Select ouput file", //$NON-NLS-1$
					new File(this.model3dFile.getText()), new XMLFileFilter("SFG Snipset")); //$NON-NLS-1$
			if (file != null) {
				this.outputFile.setText(file.getAbsolutePath());
			}
		}
		else if (ACTION_SELECT_GOAL.equals(cmd) || ACTION_SELECT_WAYPOINT.equals(cmd)) {
			updateDescription();
			updateEnableState();
		}
		else if (ACTION_SELECT_OUTPUT_CLIPBOARD.equals(cmd) || ACTION_SELECT_OUTPUT_FILE.equals(cmd)) {
			updateEnableState();
		}
		else {
			super.actionPerformed(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = this.model3dList.getSelectedIndex();
		if (index >= 0) {
			Object o = this.model3dListModel.getElementAt(index);
			if (o instanceof GoalWaypointDescription) {
				GoalWaypointDescription desc = (GoalWaypointDescription) o;
				if (desc.allocResource()) {
					this.model3dFile.setText(desc.modelFile == null ? "" : desc.modelFile.toString()); //$NON-NLS-1$
					if (desc.goal)
						this.goalType.setSelected(true);
					else
						this.waypointType.setSelected(true);
					desc.releaseResource();
				}
			}
		}
		updateEnableState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
		}
		else if (e.getDocument() == this.outputFile.getDocument()) {
			updateEnableState();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
		}
		else if (e.getDocument() == this.outputFile.getDocument()) {
			updateEnableState();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
		}
		else if (e.getDocument() == this.outputFile.getDocument()) {
			updateEnableState();
		}
	}

	/**
	 * @throws IOException
	 */
	protected void loadConfig() throws IOException {
		File inputFile = FrameUtil.getOpenFile(this, "Loading configuration", new INIFileFilter()); //$NON-NLS-1$
		if (inputFile != null) {
			List<GoalWaypointDescription> entities = new ArrayList<GoalWaypointDescription>();
			GoalWaypointDescription lastEntity = null;
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String uline, line = in.readLine();
			int section = 0;
			StringBuffer value = new StringBuffer();
			String outFile = null;
			boolean isOutClipboard = true;
			while (line != null) {
				uline = line.toUpperCase();
				if (uline.equals("[GENERAL]")) { //$NON-NLS-1$
					section = 1;
					if (lastEntity != null)
						entities.add(lastEntity);
					lastEntity = null;
				}
				else if (uline.equals("[GOAL]")) { //$NON-NLS-1$
					section = 2;
					if (lastEntity != null)
						entities.add(lastEntity);
					lastEntity = null;
				}
				else if (uline.equals("[WAYPOINT]")) { //$NON-NLS-1$
					section = 3;
					if (lastEntity != null)
						entities.add(lastEntity);
					lastEntity = null;
				}
				else if (uline.startsWith("[") && uline.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
					section = 0; // unrecognized section
					if (lastEntity != null)
						entities.add(lastEntity);
					lastEntity = null;
				}
				else {
					switch (section) {
					case 3:
						if (lastEntity == null) {
							lastEntity = new GoalWaypointDescription();
							lastEntity.goal = false;
						}
						if (isIniLine(uline, line, "FILE", value)) { //$NON-NLS-1$
							lastEntity.modelFile = new File(value.toString());
						}
						break;
					case 2:
						if (lastEntity == null) {
							lastEntity = new GoalWaypointDescription();
							lastEntity.goal = true;
						}
						if (isIniLine(uline, line, "FILE", value)) { //$NON-NLS-1$
							lastEntity.modelFile = new File(value.toString());
						}
						break;
					case 1:
						if (isIniLine(uline, line, "OUTFILE", value)) { //$NON-NLS-1$
							outFile = value.toString();
							isOutClipboard = false;
						}
						else if (isIniLine(uline, line, "OUTCLIPBOARD", value)) { //$NON-NLS-1$
							isOutClipboard = Boolean.valueOf(value.toString());
						}
						break;
					default:
						//
					}
				}
				line = in.readLine();
			}
			if (lastEntity != null)
				entities.add(lastEntity);
			in.close();

			this.model3dListModel.clear();
			for (GoalWaypointDescription desc : entities) {
				this.model3dListModel.addElement(desc);
			}

			if (isOutClipboard) {
				this.outputClipboardType.setSelected(true);
			}
			else {
				this.outputFileType.setSelected(true);
				if (outFile != null && !"".equals(outFile)) { //$NON-NLS-1$
					this.outputFile.setText(outFile);
				}
			}

			updateEnableState();
		}
	}

	/**
	 * @throws IOException
	 */
	protected void saveConfig() throws IOException {
		File outputFile = FrameUtil.getSaveFile(this, "Saving configuration", new INIFileFilter()); //$NON-NLS-1$
		if (outputFile != null) {
			PrintWriter out = new PrintWriter(new FileOutputStream(outputFile));

			int count = this.model3dListModel.getSize();

			out.println("[GENERAL]"); //$NON-NLS-1$
			out.print("OUTCLIPBOARD="); //$NON-NLS-1$
			out.println(Boolean.toString(this.outputClipboardType.isSelected()));
			if (this.outputFileType.isSelected()) {
				out.print("OUTFILE="); //$NON-NLS-1$
				out.println(this.outputFile.getText());
			}

			File f;
			GoalWaypointDescription desc;
			for (int i = 0; i < count; ++i) {
				desc = (GoalWaypointDescription) this.model3dListModel.getElementAt(i);

				if (desc.isGoal()) {
					out.println("[GOAL]"); //$NON-NLS-1$
				}
				else {
					out.println("[WAYPOINT]"); //$NON-NLS-1$
				}
				
				f = desc.modelFile;
				out.print("FILE="); //$NON-NLS-1$
				out.println(f == null ? "" : f.toString()); //$NON-NLS-1$
			}
			out.close();
		}
	}

	private static boolean isIniLine(String ucase, String line, String header, StringBuffer value) {
		if (ucase.startsWith(header + "=")) { //$NON-NLS-1$
			String v = line.substring(header.length() + 1);
			if (v != null && !"".equals(v)) { //$NON-NLS-1$
				value.setLength(0);
				value.append(v);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the selected model or <code>null</code>
	 */
	protected GoalWaypointDescription getSelectedModel() {
		Object o = this.model3dList.getSelectedValue();
		if (o instanceof GoalWaypointDescription)
			return (GoalWaypointDescription) o;
		return null;
	}

	/**
	 */
	protected void updateDescription() {
		updateDescription(getSelectedModel());
	}

	private static File getFile(JTextField field) {
		try {
			String txt = field.getText();
			if ((txt != null) && (!"".equals(txt))) { //$NON-NLS-1$
				return new File(txt);
			}
		} catch (Throwable _) {
			//
		}
		return null;
	}

	/**
	 * @param desc
	 */
	protected void updateDescription(GoalWaypointDescription desc) {
		if (desc != null) {
			if (desc.allocResource()) {
				desc.modelFile = getFile(this.model3dFile);
				desc.goal = this.goalType.isSelected();
				desc.releaseResource();
			}
		}
	}

	/**
	 * Description a goal/waypoint.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public class GoalWaypointDescription {
	
		private int resource = 0;

		/** File of the 3D model to load.
		 */
		File modelFile = null;
		
		/** Indicates if this description is for a goal.
		 */
		boolean goal = false;
		
		/**
		 */
		public GoalWaypointDescription() {
			//
		}

		/**
		 * @return <code>true</code> if the resource is available, otherwise
		 *         <code>false</code>
		 */
		protected synchronized boolean allocResource() {
			if (this.resource <= 0) {
				++this.resource;
				return true;
			}
			return false;
		}

		/**
		 */
		protected synchronized void releaseResource() {
			if (this.resource > 0) {
				--this.resource;
			}
		}

		/** Replies the 3D model to load.
		 * 
		 * @return the 3D model to load.
		 */
		public File get3DModel() {
			return this.modelFile;
		}
		
		/** Replies if this description is for a goal.
		 *
		 * @return <code>true</code> if this description is
		 * for a goal, otherwise <code>false</code>.
		 */
		public boolean isGoal() {
			return this.goal;
		}
		
		/** Replies if this description is for a waypoint.
		 *
		 * @return <code>true</code> if this description is
		 * for a waypoint, otherwise <code>false</code>.
		 */
		public boolean isWaypoint() {
			return !this.goal;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return FileSystem.basename(this.modelFile);
		}

	}

	/**
	 * Describe an entity type.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class EntityIterator implements SizedIterator<GoalWaypointDescription> {

		private final DefaultListModel model;
		private int index;

		/**
		 * @param model
		 */
		public EntityIterator(DefaultListModel model) {
			this.model = model;
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.model.getSize();
		}

		@Override
		public GoalWaypointDescription next() {
			if (this.index >= this.model.getSize())
				throw new NoSuchElementException();

			GoalWaypointDescription desc = (GoalWaypointDescription) this.model.get(this.index);
			++this.index;

			return desc;
		}

		@Override
		public void remove() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.index - 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return this.model.size();
		}

	}

}
