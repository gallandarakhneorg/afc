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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.ReflectionUtil;

import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox.PlaneConstraint;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;
import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.io.filefilter.INIFileFilter;
import fr.utbm.set.jasim.environment.semantics.ImmobileObjectType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.generation.builder.BoundType;
import fr.utbm.set.jasim.io.DotTreeFileFilter;
import fr.utbm.set.ui.IconFactory;
import fr.utbm.set.ui.IconSize;
import fr.utbm.set.ui.PredefinedIcon;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * Window for entering the static tree generation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StaticTreeParameterDialog extends JOkCancelBtDialog implements ListSelectionListener, DocumentListener {

	private static final long serialVersionUID = -9027537801955176024L;

	private static String findSemanticSingletonDeclaration(Class<? extends Semantic> clazz) {
		Class<?> type;
		int modifiers;
		for (Field field : clazz.getDeclaredFields()) {
			type = field.getType();
			modifiers = field.getModifiers();

			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getName().endsWith("_SINGLETON") //$NON-NLS-1$
					&& clazz.isAssignableFrom(type)) {
				return clazz.getCanonicalName() + "." + field.getName(); //$NON-NLS-1$
			}
		}
		return null;
	}

	private static final String ACTION_ADD_ENTITY = "ADD_ENTITY"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_3D_MODEL = "BROWSE_3D_MODEL"; //$NON-NLS-1$
	private static final String ACTION_LOAD_CONFIG = "LOAD_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_REMOVE_ENTITY = "REMOVE_ENTITY"; //$NON-NLS-1$
	private static final String ACTION_SAVE_CONFIG = "SAVE_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_SELECT_TYPE = "SELECT_TYPE"; //$NON-NLS-1$
	private static final String ACTION_UNSELECT_TYPE = "UNSELECT_TYPE"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_TREE_FILE = "BROWSE_TREE_FILE"; //$NON-NLS-1$
	private static final String ACTION_SELECT_COORDINATE_SYSTEM = "SELECT_COORDINATE_SYSTEM"; //$NON-NLS-1$
	private static final String ACTION_SELECT_BB = "SELECT_BB"; //$NON-NLS-1$
	private static final String ACTION_ENABLE_ENTRY = "ENABLE_ENTRY"; //$NON-NLS-1$
	private static final String ACTION_SELECT_OBBCONSTRAINT = "SELECT_OBB_CONSTRAINT"; //$NON-NLS-1$
	private static final String ACTION_SELECT_ON_GROUND = "SELECT_ON_GROUND"; //$NON-NLS-1$

	private static final int COLUMN_WIDTH = 200;

	private final List<TypeDescription> entityTypes = new ArrayList<TypeDescription>();

	private final JButton browse3dModel;
	private final JButton addEntity;
	private final JButton removeEntity;
	private final JButton loadConfig;
	private final JButton saveConfig;
	private final DefaultListModel model3dListModel = new DefaultListModel();
	private final JList model3dList;
	private final JCheckBox enableEntry;
	private final JTextField model3dFile;
	private final JComboBox coordinateSystem;
	private final JComboBox boundingBoxType;
	private final DefaultComboBoxModel coordinateSystemModel = new DefaultComboBoxModel();
	private final DefaultComboBoxModel boundingBoxTypeModel = new DefaultComboBoxModel();
	private final JComboBox obbConstraintType;
	private final DefaultComboBoxModel obbConstraintModel = new DefaultComboBoxModel();
	private final JLabel coordinateSystemExample;
	private final JCheckBox onGround;
	private final JTextField treeFile;
	private final JButton browseTreeFile;

	private final DefaultListModel availableEntityTypeModel = new DefaultListModel();
	private final JList availableEntityTypes;
	private final DefaultListModel selectedEntityTypeModel = new DefaultListModel();
	private final JList selectedEntityTypes;
	private final JButton selectType;
	private final JButton unselectType;

	/**
	 * @param parent
	 */
	public StaticTreeParameterDialog(Component parent) {
		super(parent, "Static Tree Parameters"); //$NON-NLS-1$
		setModal(true);
		this.topPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		{
			if (parent != null) {
				parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
			Collection<Class<? extends Semantic>> classes = ReflectionUtil.getSubClasses(Semantic.class);
			String singleton;
			for (Class<? extends Semantic> clazz : classes) {
				if (!clazz.equals(ImmobileObjectType.class)) {
					singleton = findSemanticSingletonDeclaration(clazz);
					if (singleton != null) {
						this.entityTypes.add(new TypeDescription(singleton));
					}
				}
			}
			Collections.sort(this.entityTypes);
			if (parent != null) {
				parent.setCursor(Cursor.getDefaultCursor());
			}
		}

		JPanel entityGroup = new JPanel();
		entityGroup.setBorder(BorderFactory.createTitledBorder("World Content")); //$NON-NLS-1$
		entityGroup.setLayout(new BorderLayout());
		this.topPane.add(BorderLayout.CENTER, entityGroup);

		JPanel leftPanel = new JPanel(new BorderLayout());
		entityGroup.add(leftPanel, BorderLayout.WEST);

		JPanel controlBtPanel = new JPanel();
		leftPanel.add(controlBtPanel, BorderLayout.NORTH);
		this.addEntity = new JButton(PredefinedIcon.ADD.getIcon(IconSize.getButtonSize()));
		this.addEntity.setActionCommand(ACTION_ADD_ENTITY);
		this.addEntity.addActionListener(this);
		controlBtPanel.add(this.addEntity);
		this.removeEntity = new JButton(PredefinedIcon.REMOVE.getIcon(IconSize.getButtonSize()));
		this.removeEntity.setActionCommand(ACTION_REMOVE_ENTITY);
		this.removeEntity.addActionListener(this);
		controlBtPanel.add(this.removeEntity);
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
		this.model3dList.setCellRenderer(new CellRenderer());
		JScrollPane scrollPane = new JScrollPane(this.model3dList);
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new GridBagLayout());
		entityGroup.add(parameterPanel, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 1;
		c.gridy = 0;
		this.enableEntry = new JCheckBox("Enable"); //$NON-NLS-1$
		this.enableEntry.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.enableEntry.setActionCommand(ACTION_ENABLE_ENTRY);
		this.enableEntry.addActionListener(this);
		parameterPanel.add(this.enableEntry, c);

		c.gridx = 0;
		c.gridy = 1;
		JLabel label = new JLabel("3D model:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 1;
		this.model3dFile = new JTextField();
		this.model3dFile.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		this.model3dFile.getDocument().addDocumentListener(this);
		parameterPanel.add(this.model3dFile, c);

		c.gridx = 2;
		c.gridy = 1;
		this.browse3dModel = new JButton("Browse..."); //$NON-NLS-1$
		this.browse3dModel.addActionListener(this);
		this.browse3dModel.setActionCommand(ACTION_BROWSE_3D_MODEL);
		parameterPanel.add(this.browse3dModel, c);

		c.gridx = 0;
		c.gridy = 2;
		label = new JLabel("3D model system:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 2;
		this.coordinateSystem = new JComboBox(this.coordinateSystemModel);
		this.coordinateSystem.setEditable(false);
		parameterPanel.add(this.coordinateSystem, c);
		for (CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			this.coordinateSystemModel.addElement(cs);
		}
		this.coordinateSystem.setActionCommand(ACTION_SELECT_COORDINATE_SYSTEM);
		this.coordinateSystem.addActionListener(this);

		c.gridx = 2;
		c.gridy = 2;
		this.coordinateSystemExample = new JLabel();
		Dimension dim = new Dimension(100, 100);
		this.coordinateSystemExample.setMaximumSize(dim);
		this.coordinateSystemExample.setMinimumSize(dim);
		this.coordinateSystemExample.setPreferredSize(dim);
		parameterPanel.add(this.coordinateSystemExample, c);

		c.gridx = 0;
		c.gridy = 3;
		label = new JLabel("Box Type:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 3;
		this.boundingBoxType = new JComboBox(this.boundingBoxTypeModel);
		this.boundingBoxType.setEditable(false);
		this.boundingBoxType.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		parameterPanel.add(this.boundingBoxType, c);
		for (BoundType type : BoundType.values()) {
			this.boundingBoxTypeModel.addElement(type);
		}
		this.boundingBoxType.setActionCommand(ACTION_SELECT_BB);
		this.boundingBoxType.addActionListener(this);

		c.gridx = 2;
		c.gridy = 3;
		this.obbConstraintType = new JComboBox(this.obbConstraintModel);
		this.obbConstraintType.setEditable(false);
		this.obbConstraintType.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
		parameterPanel.add(this.obbConstraintType, c);
		this.obbConstraintModel.addElement("None"); //$NON-NLS-1$
		for (PlaneConstraint obbConstraint : PlaneConstraint.values()) {
			this.obbConstraintModel.addElement(obbConstraint);
		}
		this.obbConstraintType.setActionCommand(ACTION_SELECT_OBBCONSTRAINT);
		this.obbConstraintType.addActionListener(this);

		c.gridx = 0;
		c.gridy = 4;
		label = new JLabel("3D Coordinates:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 4;
		this.onGround = new JCheckBox("On ground"); //$NON-NLS-1$
		this.onGround.setActionCommand(ACTION_SELECT_ON_GROUND);
		this.onGround.addActionListener(this);
		parameterPanel.add(this.onGround, c);

		c.gridx = 0;
		c.gridy = 5;
		label = new JLabel("Entity type:"); //$NON-NLS-1$
		parameterPanel.add(label, c);

		c.gridx = 1;
		c.gridy = 5;
		this.availableEntityTypes = new JList(this.availableEntityTypeModel);
		this.availableEntityTypes.getSelectionModel().addListSelectionListener(new TypeListUpdater(this.availableEntityTypes));
		scrollPane = new JScrollPane(this.availableEntityTypes);
		scrollPane.setPreferredSize(new Dimension(COLUMN_WIDTH, 100));
		parameterPanel.add(scrollPane, c);
		for (TypeDescription desc : this.entityTypes)
			this.availableEntityTypeModel.addElement(desc);

		c.gridx = 2;
		c.gridy = 5;
		JPanel btPanel = new JPanel();
		btPanel.setLayout(new BoxLayout(btPanel, BoxLayout.Y_AXIS));
		parameterPanel.add(btPanel, c);
		this.selectType = new JButton(">"); //$NON-NLS-1$
		this.selectType.setActionCommand(ACTION_SELECT_TYPE);
		this.selectType.addActionListener(this);
		btPanel.add(this.selectType);
		this.unselectType = new JButton("<"); //$NON-NLS-1$
		this.unselectType.setActionCommand(ACTION_UNSELECT_TYPE);
		this.unselectType.addActionListener(this);
		btPanel.add(this.unselectType);

		c.gridx = 3;
		c.gridy = 5;
		this.selectedEntityTypes = new JList(this.selectedEntityTypeModel);
		this.selectedEntityTypes.getSelectionModel().addListSelectionListener(new TypeListUpdater(this.selectedEntityTypes));
		scrollPane = new JScrollPane(this.selectedEntityTypes);
		scrollPane.setPreferredSize(new Dimension(COLUMN_WIDTH, 100));
		parameterPanel.add(scrollPane, c);

		JPanel treeGroup = new JPanel();
		treeGroup.setBorder(BorderFactory.createTitledBorder("Static Tree")); //$NON-NLS-1$
		treeGroup.setLayout(new GridLayout(1, 3));
		this.topPane.add(BorderLayout.SOUTH, treeGroup);

		c.gridx = 0;
		c.gridy = 0;
		label = new JLabel("Output static tree file:"); //$NON-NLS-1$
		treeGroup.add(label, c);

		c.gridx = 1;
		c.gridy = 0;
		this.treeFile = new JTextField();
		this.treeFile.getDocument().addDocumentListener(this);
		treeGroup.add(this.treeFile, c);

		c.gridx = 2;
		c.gridy = 0;
		this.browseTreeFile = new JButton("Browse"); //$NON-NLS-1$
		this.browseTreeFile.setActionCommand(ACTION_BROWSE_TREE_FILE);
		this.browseTreeFile.addActionListener(this);
		treeGroup.add(this.browseTreeFile, c);

		updateEnableState();
		centerDialog();
	}

	/**
	 */
	protected void updateEnableState() {
		boolean hasEntity = this.model3dListModel.getSize() > 0;
		boolean isEntitySelected = hasEntity && this.model3dList.getSelectedIndex() >= 0;
		boolean isObb = this.boundingBoxType.getSelectedItem()==BoundType.ORIENTED;

		String output = this.treeFile.getText();
		boolean hasOutput = (output != null && !"".equals(output)); //$NON-NLS-1$

		boolean isEnable = this.enableEntry.isSelected();
		
		this.enableEntry.setEnabled(isEntitySelected);
		this.model3dFile.setEnabled(isEnable && isEntitySelected);
		this.coordinateSystem.setEnabled(isEnable && isEntitySelected);
		this.boundingBoxType.setEnabled(isEnable && isEntitySelected);
		this.obbConstraintType.setEnabled(isEnable && isEntitySelected && isObb);
		this.onGround.setEnabled(isEnable && isEntitySelected);
		this.browse3dModel.setEnabled(isEnable && isEntitySelected);
		this.availableEntityTypes.setEnabled(isEnable && isEntitySelected);
		this.selectType.setEnabled(isEnable && isEntitySelected && !this.availableEntityTypes.isSelectionEmpty());
		this.unselectType.setEnabled(isEnable && isEntitySelected && !this.selectedEntityTypes.isSelectionEmpty());
		this.selectedEntityTypes.setEnabled(isEnable && isEntitySelected);

		this.removeEntity.setEnabled(isEntitySelected);
		this.saveConfig.setEnabled(hasEntity);

		setOkButtonEnabled(hasEntity && hasOutput);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = this.model3dList.getSelectedIndex();
		if (index >= 0) {
			Object o = this.model3dListModel.getElementAt(index);
			if (o instanceof StaticEntityDescription) {
				StaticEntityDescription desc = (StaticEntityDescription) o;
				if (desc.allocResource()) {
					this.enableEntry.setSelected(desc.enable);
					this.model3dFile.setText(desc.modelFile == null ? "" : desc.modelFile.toString()); //$NON-NLS-1$
					this.coordinateSystem.setSelectedItem(desc.coordinateSystem);
					this.boundingBoxType.setSelectedItem(desc.boundingBoxType);
					if (desc.obbConstraint!=null) {
						this.obbConstraintType.setSelectedItem(desc.obbConstraint);
					}
					else {
						this.obbConstraintType.setSelectedIndex(0);
					}
					this.onGround.setSelected(desc.onGround);
					this.selectedEntityTypeModel.clear();
					this.availableEntityTypeModel.clear();
					for (TypeDescription type : this.entityTypes) {
						if (desc.types.contains(type)) {
							this.selectedEntityTypeModel.addElement(type);
						} else {
							this.availableEntityTypeModel.addElement(type);
						}
					}
					desc.releaseResource();
				}
			}
		}
		updateEnableState();
	}

	/**
	 */
	protected void updateDescription() {
		updateDescription(getSelectedModel());
	}

	/**
	 * @param desc
	 */
	protected void updateDescription(StaticEntityDescription desc) {
		if (desc != null) {
			if (desc.allocResource()) {
				desc.enable = this.enableEntry.isSelected();
				desc.modelFile = getFile(this.model3dFile);
				desc.coordinateSystem = (CoordinateSystem3D) this.coordinateSystem.getSelectedItem();
				desc.boundingBoxType = (BoundType) this.boundingBoxType.getSelectedItem();
				desc.obbConstraint = (this.obbConstraintType.getSelectedItem() instanceof PlaneConstraint)
							? (PlaneConstraint)this.obbConstraintType.getSelectedItem() : null;
				desc.onGround = this.onGround.isSelected();
				desc.types.clear();
				TypeDescription type;
				for (int i = 0; i < this.selectedEntityTypeModel.size(); ++i) {
					type = (TypeDescription) this.selectedEntityTypeModel.getElementAt(i);
					desc.types.add(type);
				}
				desc.releaseResource();
			}
		}
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

	private void updateTreeFileField() {
		if (getOutputFile() == null) {
			String model3dfile = this.model3dFile.getText();
			if (model3dfile != null && !"".equals(model3dfile)) { //$NON-NLS-1$
				this.treeFile.setText(FileSystem.replaceExtension(new File(model3dfile), ".tree").toString()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @return the selected model or <code>null</code>
	 */
	protected StaticEntityDescription getSelectedModel() {
		Object o = this.model3dList.getSelectedValue();
		if (o instanceof StaticEntityDescription)
			return (StaticEntityDescription) o;
		return null;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
			updateTreeFileField();
		} else if (e.getDocument() == this.treeFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
			updateTreeFileField();
		} else if (e.getDocument() == this.treeFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument() == this.model3dFile.getDocument()) {
			this.model3dList.repaint();
			updateTreeFileField();
		} else if (e.getDocument() == this.treeFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (ACTION_ENABLE_ENTRY.equals(cmd)) {
			updateDescription();
			updateEnableState();
			this.model3dList.repaint();
		}
		if (ACTION_ADD_ENTITY.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this, "Select static object model", //$NON-NLS-1$
					(File) null, new ColladaFileFilter());
			if (file != null) {
				StaticEntityDescription desc = new StaticEntityDescription();
				desc.modelFile = file;
				this.model3dListModel.addElement(desc);
				this.model3dList.setSelectedValue(desc, true);
			}
		} else if (ACTION_REMOVE_ENTITY.equals(cmd)) {
			int index = this.model3dList.getSelectedIndex();
			if (index >= 0) {
				this.model3dListModel.remove(index);
			}
		} else if (ACTION_BROWSE_3D_MODEL.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this, "Select static object model", //$NON-NLS-1$
					new File(this.model3dFile.getText()), new ColladaFileFilter());
			if (file != null) {
				this.model3dFile.setText(file.getAbsolutePath());
			}
		} else if (ACTION_SAVE_CONFIG.equals(cmd)) {
			try {
				saveConfig();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		} else if (ACTION_LOAD_CONFIG.equals(cmd)) {
			try {
				loadConfig();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		} else if (ACTION_SELECT_TYPE.equals(cmd)) {
			selectCurrentType();
		} else if (ACTION_UNSELECT_TYPE.equals(cmd)) {
			unselectCurrentType();
		} else if (ACTION_BROWSE_TREE_FILE.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this, "Select static tree file", //$NON-NLS-1$
					new File(this.model3dFile.getText()), new DotTreeFileFilter());
			if (file != null) {
				this.treeFile.setText(file.getAbsolutePath());
			}
		} else if (ACTION_SELECT_COORDINATE_SYSTEM.equals(cmd)) {
			updateDescription();
			updateCoordinateSystemExample();
		} else if (ACTION_SELECT_BB.equals(cmd)) {
			updateDescription();
			updateEnableState();
		} else if (ACTION_SELECT_OBBCONSTRAINT.equals(cmd)) {
			updateDescription();
		} else {
			super.actionPerformed(e);
		}
	}

	/**
	 */
	protected void selectCurrentType() {
		StaticEntityDescription desc = getSelectedModel();
		if (desc != null) {
			TypeDescription type = (TypeDescription) this.availableEntityTypes.getSelectedValue();
			if (type != null) {
				insertInList(this.selectedEntityTypeModel, type);
				this.availableEntityTypeModel.removeElement(type);
				desc.types.add(type);
			}
		}
	}

	/**
	 */
	protected void unselectCurrentType() {
		StaticEntityDescription desc = getSelectedModel();
		if (desc != null) {
			TypeDescription type = (TypeDescription) this.selectedEntityTypes.getSelectedValue();
			if (type != null) {
				this.selectedEntityTypeModel.removeElement(type);
				insertInList(this.availableEntityTypeModel, type);
				desc.types.remove(type);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> void insertInList(DefaultListModel model, Comparable<T> obj) {
		if (obj == null)
			return;
		int insertionIndex = -1;
		T insideObj;
		for (int i = 0; i < model.size() && insertionIndex == -1; ++i) {
			insideObj = (T) model.getElementAt(i);
			if (insideObj != null && obj.compareTo(insideObj) <= 0)
				insertionIndex = i;
		}
		if (insertionIndex >= 0) {
			model.insertElementAt(obj, insertionIndex);
		} else {
			model.addElement(obj);
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

			out.println("[ENTITIES]"); //$NON-NLS-1$
			out.print("COUNT="); //$NON-NLS-1$
			out.println(Long.toString(count));
			out.print("OUTFILE="); //$NON-NLS-1$
			out.println(this.treeFile.getText());

			File f;
			StaticEntityDescription desc;
			for (int i = 0; i < count; ++i) {
				desc = (StaticEntityDescription) this.model3dListModel.getElementAt(i);

				out.print("[ENTITY#"); //$NON-NLS-1$
				out.print(i + 1);
				out.println("]"); //$NON-NLS-1$
				out.print("ENABLE="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.enable));
				f = desc.modelFile;
				out.print("FILE="); //$NON-NLS-1$
				out.println(f == null ? "" : f.toString()); //$NON-NLS-1$
				out.print("COORDINATES="); //$NON-NLS-1$
				out.println(desc.coordinateSystem.name());
				out.print("BOUNDS="); //$NON-NLS-1$
				out.println(desc.boundingBoxType.name());
				out.print("OBBCONSTRAINT="); //$NON-NLS-1$
				out.println(desc.obbConstraint==null ? "None" : desc.obbConstraint.name()); //$NON-NLS-1$
				out.print("ON_GROUND="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.onGround));
				out.print("TYPES="); //$NON-NLS-1$
				for (int j = 0; j < desc.types.size(); ++j) {
					if (j > 0)
						out.print(File.pathSeparator);
					out.print(desc.types.get(j).fullName);
				}
				out.println();
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
	 * @throws IOException
	 */
	protected void loadConfig() throws IOException {
		File inputFile = FrameUtil.getOpenFile(this, "Loading configuration", new INIFileFilter()); //$NON-NLS-1$
		if (inputFile != null) {
			List<StaticEntityDescription> entities = new ArrayList<StaticEntityDescription>();
			StaticEntityDescription lastEntity = null;
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String uline, line = in.readLine();
			int section = 0;
			StringBuffer value = new StringBuffer();
			String outFile = null;
			while (line != null) {
				uline = line.toUpperCase();
				if (uline.equals("[ENTITIES]")) { //$NON-NLS-1$
					section = 1;
				} else if (uline.startsWith("[ENTITY") && uline.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
					section = 2;
					if (lastEntity != null)
						entities.add(lastEntity);
					lastEntity = null;
				} else if (uline.startsWith("[") && uline.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
					section = 0; // unrecognized section
				} else {
					switch (section) {
					case 2:
						if (lastEntity == null)
							lastEntity = new StaticEntityDescription();
						if (isIniLine(uline, line, "ENABLE", value)) { //$NON-NLS-1$
							lastEntity.enable = Boolean.parseBoolean(value.toString());
						} else if (isIniLine(uline, line, "FILE", value)) { //$NON-NLS-1$
							lastEntity.modelFile = new File(value.toString());
						} else if (isIniLine(uline, line, "COORDINATES", value)) { //$NON-NLS-1$
							lastEntity.coordinateSystem = CoordinateSystem3D.valueOf(value.toString());
						} else if (isIniLine(uline, line, "BOUNDS", value)) { //$NON-NLS-1$
							lastEntity.boundingBoxType = BoundType.valueOf(value.toString());
						} else if (isIniLine(uline, line, "OBBCONSTRAINT", value)) { //$NON-NLS-1$
							PlaneConstraint[] allcons = PlaneConstraint.values();
							lastEntity.obbConstraint = null;
							for(PlaneConstraint cons : allcons) {
								if (cons.name().equalsIgnoreCase(value.toString())) {
									lastEntity.obbConstraint = PlaneConstraint.valueOf(value.toString());
									break;
								}
							}
						} else if (isIniLine(uline, line, "ON_GROUND", value)) { //$NON-NLS-1$
							lastEntity.onGround = Boolean.valueOf(value.toString());
						} else if (isIniLine(uline, line, "TYPES", value)) { //$NON-NLS-1$
							String[] types = value.toString().split(File.pathSeparator);
							lastEntity.types.clear();
							for (String type : types) {
								lastEntity.types.add(new TypeDescription(type));
							}
						}
						break;
					case 1:
						if (isIniLine(uline, line, "OUTFILE", value)) { //$NON-NLS-1$
							outFile = value.toString();
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
			for (StaticEntityDescription desc : entities) {
				this.model3dListModel.addElement(desc);
			}

			if (outFile != null && !"".equals(outFile)) { //$NON-NLS-1$
				this.treeFile.setText(outFile);
			}

			updateEnableState();
		}
	}

	/**
	 * Replies the output file.
	 * 
	 * @return the output file or <code>null</code>.
	 */
	public File getOutputFile() {
		String filename = this.treeFile.getText();
		if (filename != null && !"".equals(filename)) { //$NON-NLS-1$
			return new File(filename);
		}
		return null;
	}

	/**
	 * Replies the descriptions.
	 * 
	 * @return the descriptions.
	 */
	public Iterator<StaticEntityDescription> getDescriptions() {
		return new EntityIterator(this.model3dListModel);
	}

	/**
	 * Update the coordinate system illustration.
	 */
	protected void updateCoordinateSystemExample() {
		updateCoordinateSystemExample(getSelectedModel());
	}

	/**
	 * Update the coordinate system illustration.
	 * 
	 * @param desc
	 */
	protected void updateCoordinateSystemExample(StaticEntityDescription desc) {
		String name = null;
		switch (desc.coordinateSystem) {
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
	 * Describe an entity type.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class EntityIterator implements Iterator<StaticEntityDescription> {

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
		public StaticEntityDescription next() {
			if (this.index >= this.model.getSize())
				throw new NoSuchElementException();

			StaticEntityDescription desc = (StaticEntityDescription) this.model.get(this.index);
			++this.index;

			return desc;
		}

		@Override
		public void remove() {
			//
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
	private static class TypeDescription implements Comparable<TypeDescription> {

		public final String fullName;
		public final String className;
		public final String shortName;

		public TypeDescription(String fn) {
			this.fullName = fn;
			int lastIndex = fn.lastIndexOf("."); //$NON-NLS-1$
			String sn;
			if (lastIndex >= 0)
				sn = this.fullName.substring(lastIndex + 1);
			else
				sn = fn;
			if (sn.endsWith("_SINGLETON")) { //$NON-NLS-1$
				sn = sn.substring(0, sn.length() - 10);
			}
			this.className = fn.substring(0, lastIndex);
			this.shortName = sn;
		}

		@Override
		public String toString() {
			return this.shortName;
		}

		@Override
		public int hashCode() {
			return this.shortName.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof TypeDescription)
				return compareTo((TypeDescription) o) == 0;
			return super.equals(o);
		}

		@Override
		public int compareTo(TypeDescription o) {
			return this.shortName.compareTo(o.shortName);
		}
	}

	/**
	 * Update the type lists.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TypeListUpdater implements ListSelectionListener {

		private final WeakReference<JList> list;

		public TypeListUpdater(JList list) {
			this.list = new WeakReference<JList>(list);
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			JList l = this.list.get();
			String txt = null;
			TypeDescription type = (TypeDescription) l.getSelectedValue();
			if (type != null)
				txt = type.fullName;
			updateEnableState();
			l.setToolTipText(txt);
		}

	}

	/**
	 * Describe an entity.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class StaticEntityDescription {

		private int resource = 0;

		/**
		 */
		protected final List<TypeDescription> types = new ArrayList<TypeDescription>();

		/**
		 */
		protected boolean enable = true;
		
		/**
		 */
		protected File modelFile = null;

		/**
		 */
		protected CoordinateSystem3D coordinateSystem = CoordinateSystemConstants.SIMULATION_3D;

		/**
		 */
		protected BoundType boundingBoxType = BoundType.ALIGNED;
		
		/**
		 */
		protected PlaneConstraint obbConstraint = null;
		
		/**
		 */
		protected boolean onGround = true;

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

		private static Semantic getFirstSemanticSingleton(Class<? extends Semantic> type) {
			Class<?> fieldType;
			int modifiers;
			try {
				for (Field field : type.getDeclaredFields()) {
					fieldType = field.getType();
					modifiers = field.getModifiers();

					if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && field.getName().endsWith("_SINGLETON") //$NON-NLS-1$
							&& type.isAssignableFrom(fieldType)) {
						return (Semantic) field.get(null);
					}
				}
			} catch (Exception _) {
				//
			}
			return null;
		}

		/**
		 * @return the types of the entity exclusion of immobile entity
		 *         semantic.
		 */
		@SuppressWarnings("unchecked")
		public List<Semantic> getTypes() {
			ArrayList<Semantic> l = new ArrayList<Semantic>(this.types.size());
			Class<?> semanticType;
			Semantic semantic;
			for (TypeDescription type : this.types) {
				try {
					semanticType = Class.forName(type.className);
					if (Semantic.class.isAssignableFrom(semanticType)) {
						semantic = getFirstSemanticSingleton((Class<? extends Semantic>) semanticType);
						if (semantic != null && !ImmobileObjectType.class.equals(semantic.getClass())) {
							l.add(semantic);
						}
					}
				} catch (Throwable _) {
					//
				}
			}
			return l;
		}

		/**
		 * @return <code>true</code> if is located on ground,
		 *         otherwhise <code>false</code>.
		 */
		public boolean isOnGround() {
			return this.onGround;
		}

		/**
		 * @return the coordinate system of the 3d model.
		 */
		public CoordinateSystem3D getCoordinateSystem() {
			return this.coordinateSystem;
		}

		/**
		 * @return the type of the bounding boxes.
		 */
		public BoundType getBoundingBoxType() {
			return this.boundingBoxType;
		}

		/**
		 * @return the constraint to aply to OBB.
		 */
		public PlaneConstraint getOBBConstraint() {
			return this.obbConstraint;
		}

		/**
		 * @return the model file
		 */
		public File getModelFile() {
			return this.modelFile;
		}

		/**
		 * @return <code>true</code> if enable, otherwise <code>false</code>
		 */
		public boolean isEnable() {
			return this.enable;
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
	 * List celle renderer
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 2429771124463396373L;

		/**
		 */
		public CellRenderer() {
			//
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getListCellRendererComponent(
		        JList list,
		        Object value,
		        int index,
		        boolean isSelected,
		        boolean cellHasFocus) {
			Component cmp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof StaticEntityDescription) {
				StaticEntityDescription desc = (StaticEntityDescription)value;
				cmp.setEnabled(list.isEnabled() && desc.enable);
			}
			return cmp;
		}
		
	}
	
}
