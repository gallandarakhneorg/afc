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
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;

import org.arakhne.afc.vmutil.ReflectionUtil;

import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;
import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.io.filefilter.INIFileFilter;
import fr.utbm.set.io.filefilter.PNGFileFilter;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.semantics.ImmobileObjectType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.generation.builder.FrustumDescription;
import fr.utbm.set.jasim.io.sfg.SFGFileFilter;
import fr.utbm.set.ui.IconFactory;
import fr.utbm.set.ui.IconSize;
import fr.utbm.set.ui.PredefinedIcon;
import fr.utbm.set.ui.window.JOkCancelBtDialog;

/**
 * Window for entering the dynamic tree generation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicTreeParameterDialog extends JOkCancelBtDialog implements ListSelectionListener, DocumentListener, ChangeListener {
	
	private static final long serialVersionUID = -8882454178311580746L;
	
	private static final String NONE_STR = "--- Not an agent ---"; //$NON-NLS-1$

	private static final String ACTION_ADD_ENTITY = "ADD_ENTITY"; //$NON-NLS-1$
	private static final String ACTION_LOAD_CONFIG = "LOAD_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_REMOVE_ENTITY = "REMOVE_ENTITY"; //$NON-NLS-1$
	private static final String ACTION_SAVE_CONFIG = "SAVE_CONFIG"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_ENTITY_MODEL = "BROWSE_ENTITY_MODEL"; //$NON-NLS-1$
	private static final String ACTION_SELECT_COORDINATE_SYSTEM = "SELECT_COORDINATE_SYSTEM"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_TERRAIN_MODEL = "BROWSE_TERRAIN_MODEL"; //$NON-NLS-1$
	private static final String ACTION_SELECT_TYPE = "SELECT_TYPE"; //$NON-NLS-1$
	private static final String ACTION_UNSELECT_TYPE = "UNSELECT_TYPE"; //$NON-NLS-1$
	private static final String ACTION_FRUSTUM_CHANGE = "FRUSTUM_CHANGE"; //$NON-NLS-1$
	private static final String ACTION_EDIT_FRUSTUM_PARAMETERS = "EDIT_FRUSTUM_PARAMETERS"; //$NON-NLS-1$
	private static final String ACTION_BROWSE_SFG_FILE = "BROWSE_SFG_FILE"; //$NON-NLS-1$
	private static final String ACTION_CLEAR_SFG_FILE = "CLEAR_SFG_FILE"; //$NON-NLS-1$
	
	private static final int COLUMN_WIDTH = 200;
	
	private final List<TypeDescription> entityTypes = new ArrayList<TypeDescription>();
	
	private final DefaultListModel entityListModel = new DefaultListModel();
	private final JList entityList;
	private final JTextField entityName;
	private final JTextField entityFile;
	private final JSpinner entityHeight;
	private final JSpinner entityCount;
	private final JCheckBox entityOnGround;
	private final JComboBox entityFrustum;
	private final JCheckBox entityPosRandom;
	private final JCheckBox entityPosOnSpawner;
	private final JCheckBox entityRotRandom;
	private final JTextField entityPosX;
	private final JTextField entityPosY;
	private final JTextField entityPosZ;
	private final JTextField entityRotation;
	private final DefaultListModel availableEntityTypeModel = new DefaultListModel();
	private final JList availableEntityTypes;
	private final DefaultListModel selectedEntityTypeModel = new DefaultListModel();
	private final JList selectedEntityTypes;
	private final DefaultComboBoxModel coordinateSystemModel = new DefaultComboBoxModel();
	private final JComboBox coordinateSystem;
	private final JLabel coordinateSystemExample;
	
	private final JButton browseEntityModel;
	private final JButton addEntity;
	private final JButton removeEntity;
	private final JButton loadConfig;
	private final JButton saveConfig;
	private final JButton selectType;
	private final JButton unselectType;
	private final JButton editFrustum;
	
	private final JTextField terrainFile;
	private final JSpinner terrainMinX;
	private final JSpinner terrainMaxX;
	private final JSpinner terrainMinY;
	private final JSpinner terrainMaxY;
	private final JSpinner terrainMinZ;
	private final JSpinner terrainMaxZ;
	private final JSpinner terrainNotTraversable;
	
	private final JTextField sfgFile;

	/**
	 * @param parent
	 */
	public DynamicTreeParameterDialog(Component parent) {
		super(parent, "Dynamic Tree Parameters"); //$NON-NLS-1$

		this.topPane.setLayout(new BorderLayout());
				
		JLabel label;
		JButton button;

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		this.topPane.add(BorderLayout.CENTER, mainPanel);
		
		{
			if (parent!=null) {
				parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
			Collection<Class<? extends Semantic>> classes = ReflectionUtil.getSubClasses(Semantic.class);
			String singleton;
			for(Class<? extends Semantic> clazz : classes) {
				singleton = findSemanticSingletonDeclaration(clazz);
				if (singleton!=null) {
					this.entityTypes.add(new TypeDescription(singleton));
				}
			}
			Collections.sort(this.entityTypes);
			if (parent!=null) {
				parent.setCursor(Cursor.getDefaultCursor());
			}
		}

		{
			JPanel entityGroup = new JPanel();
			entityGroup.setBorder(BorderFactory.createTitledBorder("Dynamic Entities")); //$NON-NLS-1$
			entityGroup.setLayout(new BorderLayout());
			mainPanel.add(entityGroup);
			
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
			
			this.entityList = new JList(this.entityListModel);
			this.entityList.getSelectionModel().addListSelectionListener(this);
			JScrollPane scrollPane = new JScrollPane(this.entityList);
			leftPanel.add(scrollPane, BorderLayout.CENTER);
			

			JPanel parameterPanel = new JPanel();
			parameterPanel.setLayout(new GridBagLayout());
			entityGroup.add(parameterPanel, BorderLayout.CENTER);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			label = new JLabel("Name:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 0;
			this.entityName = new JTextField();
			this.entityName.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityName.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityName,c);

			c.gridx = 0;
			c.gridy = 1;
			label = new JLabel("3D model:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 1;
			this.entityFile = new JTextField();
			this.entityFile.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityFile.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityFile,c);

			c.gridx = 2;
			c.gridy = 1;
			this.browseEntityModel = new JButton("Browse..."); //$NON-NLS-1$
			this.browseEntityModel.addActionListener(this);
			this.browseEntityModel.setActionCommand(ACTION_BROWSE_ENTITY_MODEL);
			parameterPanel.add(this.browseEntityModel,c);

			c.gridx = 0;
			c.gridy = 2;
			label = new JLabel("3D Coordinate System:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 2;
			this.coordinateSystem = new JComboBox(this.coordinateSystemModel);
			this.coordinateSystem.setEditable(false);
			for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
				this.coordinateSystemModel.addElement(cs);
			}
			this.coordinateSystem.setActionCommand(ACTION_SELECT_COORDINATE_SYSTEM);
			this.coordinateSystem.addActionListener(this);
			parameterPanel.add(this.coordinateSystem,c);

			c.gridx = 2;
			c.gridy = 2;
			this.coordinateSystemExample = new JLabel();
			Dimension dim = new Dimension(100,100);
			this.coordinateSystemExample.setMinimumSize(dim);
			this.coordinateSystemExample.setMaximumSize(dim);
			this.coordinateSystemExample.setPreferredSize(dim);
			parameterPanel.add(this.coordinateSystemExample,c);

			c.gridx = 0;
			c.gridy = 3;
			label = new JLabel("Entity height (meters):");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 3;
			this.entityHeight = new JSpinner();
			this.entityHeight.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			SpinnerNumberModel model = new SpinnerNumberModel(
					1.8,
					0.,
					3.,
					.1);
			this.entityHeight.setModel(model);
			model.addChangeListener(this);
			parameterPanel.add(this.entityHeight,c);

			c.gridx = 0;
			c.gridy = 4;
			label = new JLabel("Entity count:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 4;
			this.entityCount = new JSpinner();
			this.entityCount.setPreferredSize(new Dimension(COLUMN_WIDTH, 20));
			model = new SpinnerNumberModel(
					100,
					0.,
					100000.,
					10);
			this.entityCount.setModel(model);
			model.addChangeListener(this);
			parameterPanel.add(this.entityCount,c);

			c.gridx = 0;
			c.gridy = 5;
			label = new JLabel("Ground reaction:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 5;
			this.entityOnGround = new JCheckBox("keep on floor"); //$NON-NLS-1$
			this.entityOnGround.setSelected(true);
			this.entityOnGround.getModel().addChangeListener(this);
			parameterPanel.add(this.entityOnGround,c);

			c.gridx = 0;
			c.gridy = 6;
			label = new JLabel("Frustum type:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 6;
			this.entityFrustum = new JComboBox();
			this.entityFrustum.setEditable(false);
			{
				if (parent!=null) {
					parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}
				SortedSet<Class<? extends Frustum3D>> ltypes = new TreeSet<Class<? extends Frustum3D>>(new Comparator<Class<? extends Frustum3D>>() {
					@Override
					public int compare(Class<? extends Frustum3D> o1,
							Class<? extends Frustum3D> o2) {
						return o1.getSimpleName().compareTo(o2.getSimpleName());
					}
				});
				ReflectionUtil.getSubClasses(Frustum3D.class, false, false, false, ltypes);
				this.entityFrustum.addItem(NONE_STR);
				for(Class<? extends Frustum3D> clazz : ltypes) {
					this.entityFrustum.addItem(new FrustumDescription(clazz));
				}
				if (!ltypes.isEmpty())
					this.entityFrustum.setSelectedIndex(0);
				ltypes.clear();
				if (parent!=null) {
					parent.setCursor(Cursor.getDefaultCursor());
				}
			}
			this.entityFrustum.setActionCommand(ACTION_FRUSTUM_CHANGE);
			this.entityFrustum.addActionListener(this);
			parameterPanel.add(this.entityFrustum,c);

			c.gridx = 2;
			c.gridy = 6;
			this.editFrustum = new JButton("Edit frustum...");  //$NON-NLS-1$
			this.editFrustum.setActionCommand(ACTION_EDIT_FRUSTUM_PARAMETERS);
			this.editFrustum.addActionListener(this);
			parameterPanel.add(this.editFrustum,c);

			c.gridx = 0;
			c.gridy = 7;
			label = new JLabel("Position:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 7;
			this.entityPosX = new JTextField(Double.toString(0.));
			this.entityPosX.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityPosX.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityPosX,c);

			c.gridx = 2;
			c.gridy = 7;
			this.entityPosY = new JTextField(Double.toString(0.));
			this.entityPosY.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityPosY.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityPosY,c);

			c.gridx = 3;
			c.gridy = 7;
			this.entityPosZ = new JTextField(Double.toString(0.));
			this.entityPosZ.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityPosZ.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityPosZ,c);

			c.gridx = 1;
			c.gridy = 8;
			this.entityPosRandom = new JCheckBox("Random position"); //$NON-NLS-1$
			this.entityPosRandom.setSelected(true);
			this.entityPosRandom.getModel().addChangeListener(this);
			parameterPanel.add(this.entityPosRandom,c);

			c.gridx = 2;
			c.gridy = 8;
			this.entityPosOnSpawner = new JCheckBox("Restrict to spawners"); //$NON-NLS-1$
			this.entityPosOnSpawner.setSelected(false);
			this.entityPosOnSpawner.getModel().addChangeListener(this);
			parameterPanel.add(this.entityPosOnSpawner,c);

			c.gridx = 0;
			c.gridy = 9;
			label = new JLabel("Rotation angle:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 9;
			this.entityRotation = new JTextField(Double.toString(0.));
			this.entityRotation.setPreferredSize(new Dimension(COLUMN_WIDTH,20));
			this.entityRotation.getDocument().addDocumentListener(this);
			parameterPanel.add(this.entityRotation,c);

			c.gridx = 1;
			c.gridy = 10;
			this.entityRotRandom = new JCheckBox("Random orientation"); //$NON-NLS-1$
			this.entityRotRandom.setSelected(true);
			this.entityRotRandom.getModel().addChangeListener(this);
			parameterPanel.add(this.entityRotRandom,c);

			c.gridx = 0;
			c.gridy = 11;
			label = new JLabel("Entity type:");  //$NON-NLS-1$
			parameterPanel.add(label,c);

			c.gridx = 1;
			c.gridy = 11;
			this.availableEntityTypes = new JList(this.availableEntityTypeModel);
			this.availableEntityTypes.getSelectionModel().addListSelectionListener(new TypeListUpdater(this.availableEntityTypes));
			scrollPane = new JScrollPane(this.availableEntityTypes);
			scrollPane.setPreferredSize(new Dimension(COLUMN_WIDTH,100));
			parameterPanel.add(scrollPane,c);
			for(TypeDescription desc : this.entityTypes)
				this.availableEntityTypeModel.addElement(desc);
			
			c.gridx = 2;
			c.gridy = 11;
			JPanel btPanel = new JPanel();
			btPanel.setLayout(new BoxLayout(btPanel, BoxLayout.Y_AXIS));
			parameterPanel.add(btPanel,c);
			this.selectType = new JButton(">"); //$NON-NLS-1$
			this.selectType.setActionCommand(ACTION_SELECT_TYPE);
			this.selectType.addActionListener(this);
			btPanel.add(this.selectType);
			this.unselectType = new JButton("<"); //$NON-NLS-1$
			this.unselectType.setActionCommand(ACTION_UNSELECT_TYPE);
			this.unselectType.addActionListener(this);
			btPanel.add(this.unselectType);
			
			c.gridx = 3;
			c.gridy = 11;
			this.selectedEntityTypes = new JList(this.selectedEntityTypeModel);
			this.selectedEntityTypes.getSelectionModel().addListSelectionListener(new TypeListUpdater(this.selectedEntityTypes));
			scrollPane = new JScrollPane(this.selectedEntityTypes);
			scrollPane.setPreferredSize(new Dimension(COLUMN_WIDTH,100));
			parameterPanel.add(scrollPane,c);
			
		}
		
		{
			JPanel terrainGroup = new JPanel();
			terrainGroup.setBorder(BorderFactory.createTitledBorder("Terrain")); //$NON-NLS-1$
			terrainGroup.setLayout(new GridBagLayout());
			mainPanel.add(terrainGroup);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			label = new JLabel("Height map:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 0;
			this.terrainFile = new JTextField();
			this.terrainFile.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainFile,c);

			c.gridx = 2;
			c.gridy = 0;
			button = new JButton("Browse..."); //$NON-NLS-1$
			button.addActionListener(this);
			button.setActionCommand(ACTION_BROWSE_TERRAIN_MODEL);
			terrainGroup.add(button,c);

			c.gridx = 0;
			c.gridy = 1;
			label = new JLabel("Min X:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 1;
			this.terrainMinX = new JSpinner();
			SpinnerNumberModel model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMinX.setModel(model);
			this.terrainMinX.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMinX,c);

			c.gridx = 0;
			c.gridy = 2;
			label = new JLabel("Min Y:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 2;
			this.terrainMinY = new JSpinner();
			model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMinY.setModel(model);
			this.terrainMinY.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMinY,c);

			c.gridx = 0;
			c.gridy = 3;
			label = new JLabel("Min Z:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 3;
			this.terrainMinZ = new JSpinner();
			model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMinZ.setModel(model);
			this.terrainMinZ.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMinZ,c);

			c.gridx = 0;
			c.gridy = 4;
			label = new JLabel("Max X:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 4;
			this.terrainMaxX = new JSpinner();
			model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMaxX.setModel(model);
			this.terrainMaxX.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMaxX,c);

			c.gridx = 0;
			c.gridy = 5;
			label = new JLabel("Max Y:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 5;
			this.terrainMaxY = new JSpinner();
			model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMaxY.setModel(model);
			this.terrainMaxY.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMaxY,c);

			c.gridx = 0;
			c.gridy = 6;
			label = new JLabel("Max Z:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 6;
			this.terrainMaxZ = new JSpinner();
			model = new SpinnerNumberModel(
					0,
					-1000000,
					1000000,
					10);
			this.terrainMaxZ.setModel(model);
			this.terrainMaxZ.setPreferredSize(new Dimension(150,20));
			terrainGroup.add(this.terrainMaxZ,c);

			c.gridx = 0;
			c.gridy = 7;
			label = new JLabel("Not traversable color:");  //$NON-NLS-1$
			terrainGroup.add(label,c);

			c.gridx = 1;
			c.gridy = 7;
			this.terrainNotTraversable = new JSpinner();
			this.terrainNotTraversable.setPreferredSize(new Dimension(150,20));
			model = new SpinnerNumberModel(
					0,
					0,
					255,
					1);
			this.terrainNotTraversable.setModel(model);
			terrainGroup.add(this.terrainNotTraversable,c);

			c.gridx = 3;
			c.gridy = 0;
			label = new JLabel("Simulation Conf:");  //$NON-NLS-1$
			label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
			terrainGroup.add(label,c);

			c.gridx = 4;
			c.gridy = 0;
			this.sfgFile = new JTextField();
			this.sfgFile.setPreferredSize(new Dimension(150,20));
			this.sfgFile.getDocument().addDocumentListener(this);
			terrainGroup.add(this.sfgFile,c);

			c.gridx = 5;
			c.gridy = 0;
			button = new JButton("Browse..."); //$NON-NLS-1$
			button.addActionListener(this);
			button.setActionCommand(ACTION_BROWSE_SFG_FILE);
			terrainGroup.add(button,c);
			
			c.gridx = 6;
			c.gridy = 0;
			button = new JButton(PredefinedIcon.DELETE.getIcon(IconSize.getButtonSize()));
			button.addActionListener(this);
			button.setActionCommand(ACTION_CLEAR_SFG_FILE);
			terrainGroup.add(button,c);
		}

		updateEnableState();
		centerDialog();
	}

	private static String findSemanticSingletonDeclaration(Class<? extends Semantic> clazz) {
		Class<?> type;
		int modifiers;
		for(Field field : clazz.getDeclaredFields()) {
			type = field.getType();
			modifiers = field.getModifiers();
			
			if (Modifier.isPublic(modifiers)
				&& Modifier.isStatic(modifiers)
				&& field.getName().endsWith("_SINGLETON") //$NON-NLS-1$
				&& clazz.isAssignableFrom(type)) {
				return clazz.getCanonicalName()+"."+field.getName(); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	/**
	 */
	protected void updateEnableState() {
		boolean hasEntity = this.entityListModel.getSize()>0;
		boolean isEntitySelected = hasEntity && this.entityList.getSelectedIndex()>=0;
		
		CoordinateSystem3D cs = (CoordinateSystem3D)this.coordinateSystem.getSelectedItem();
		
		boolean isZup = cs.isZOnUp();
		boolean isManualPos = isEntitySelected && !this.entityPosRandom.isSelected();
		boolean isNotOnGround = isManualPos && !this.entityOnGround.isSelected();
		
		boolean hasSFGFile = false;
		String sfgFilename = this.sfgFile.getText();
		if (sfgFilename!=null && !"".equals(sfgFilename)) { //$NON-NLS-1$
			File file = new File(sfgFilename);
			hasSFGFile = file.canRead();
		}
		
		this.entityName.setEnabled(isEntitySelected);
		this.removeEntity.setEnabled(isEntitySelected);
		this.entityFile.setEnabled(isEntitySelected);
		this.coordinateSystem.setEnabled(isEntitySelected);
		this.browseEntityModel.setEnabled(isEntitySelected);
		this.entityHeight.setEnabled(isEntitySelected);
		this.entityCount.setEnabled(isEntitySelected);
		this.entityOnGround.setEnabled(isEntitySelected);
		this.entityFrustum.setEnabled(isEntitySelected);
		this.editFrustum.setEnabled(isEntitySelected && this.entityFrustum.getSelectedIndex()>0);
		this.entityPosRandom.setEnabled(isEntitySelected);
		this.entityPosOnSpawner.setEnabled(isEntitySelected && this.entityPosRandom.isSelected() && hasSFGFile);
		this.entityPosX.setEnabled(isManualPos);
		this.entityPosY.setEnabled(isManualPos && (isZup || isNotOnGround));
		this.entityPosZ.setEnabled(isManualPos && (!isZup || isNotOnGround));
		this.entityRotRandom.setEnabled(isEntitySelected);
		this.entityRotation.setEnabled(isEntitySelected && !this.entityRotRandom.isSelected());
		this.availableEntityTypes.setEnabled(isEntitySelected);
		this.selectType.setEnabled(isEntitySelected && !this.availableEntityTypes.isSelectionEmpty());
		this.unselectType.setEnabled(isEntitySelected && !this.selectedEntityTypes.isSelectionEmpty());
		this.selectedEntityTypes.setEnabled(isEntitySelected);
		
		setOkButtonEnabled(hasEntity);
	}

	/** Replies the count of entity descriptions.
	 * 
	 * @return the count of entity descriptions
	 */
	public int getEntityDescriptionCount() {
		return this.entityListModel.getSize();
	}
	
	/** Replies the count of entity descriptions.
	 * 
	 * @param index
	 * @return the entity descriptions
	 */
	public DynamicEntityDescription getEntityDescriptionAt(int index) {
		return (DynamicEntityDescription)this.entityListModel.get(index);
	}
	
	/** Replies the terrain description.
	 *
	 * @return the terrain description.
	 */
	public TerrainDescription getTerrainDescription() {
		return new TerrainDescription(
				getFile(this.terrainFile),
				getDouble(this.terrainMinX),
				getDouble(this.terrainMinY),
				getDouble(this.terrainMinZ),
				getDouble(this.terrainMaxX),
				getDouble(this.terrainMaxY),
				getDouble(this.terrainMaxZ),
				(int)getLong(this.terrainNotTraversable));
	}

	/**
	 * @return the selected entity or <code>null</code>
	 */
	protected DynamicEntityDescription getSelectedEntity() {
		Object o = this.entityList.getSelectedValue();
		if (o instanceof DynamicEntityDescription)
			return (DynamicEntityDescription)o;
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (ACTION_ADD_ENTITY.equals(cmd)) {
			DynamicEntityDescription desc = new DynamicEntityDescription();
			desc.name = "Entity #"+(this.entityList.getModel().getSize()+1); //$NON-NLS-1$
			this.entityListModel.addElement(desc);
			this.entityList.setSelectedValue(desc, true);
		}
		else if (ACTION_REMOVE_ENTITY.equals(cmd)) {
			int index = this.entityList.getSelectedIndex();
			if (index>=0) {
				this.entityListModel.remove(index);
			}
		}
		else if (ACTION_BROWSE_ENTITY_MODEL.equals(cmd)) {
			File file = FrameUtil.getOpenFile(this,"Select pedestrian model", //$NON-NLS-1$
					new File(this.entityFile.getText()),
					new ColladaFileFilter()); 
			if (file!=null) {
				this.entityFile.setText(file.getAbsolutePath());
			}
		}
		else if (ACTION_SELECT_COORDINATE_SYSTEM.equals(cmd)) {
			updateDescription();
			updateCoordinateSystemExample();
			updateEnableState();
		}
		else if (ACTION_BROWSE_TERRAIN_MODEL.equals(e.getActionCommand())) {
			File file = FrameUtil.getOpenFile(this,"Select terrain model", //$NON-NLS-1$
									new File(this.terrainFile.getText()),
									new PNGFileFilter()); 
			if (file!=null) {
				this.terrainFile.setText(file.getAbsolutePath());
			}
		}
		else if (ACTION_SAVE_CONFIG.equals(cmd)) {
			try {
				saveConfig();
			}
			catch(IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		}
		else if (ACTION_LOAD_CONFIG.equals(cmd)) {
			try {
				loadConfig();
			}
			catch(IOException ex) {
				JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		}
		else if (ACTION_SELECT_TYPE.equals(cmd)) {
			selectCurrentType();
		}
		else if (ACTION_UNSELECT_TYPE.equals(cmd)) {
			unselectCurrentType();
		}
		else if (ACTION_FRUSTUM_CHANGE.equals(cmd)) {
			DynamicEntityDescription desc = getSelectedEntity();
			if (desc!=null && desc.allocResource()) {
				FrustumDescription orig = castFrustum(this.entityFrustum.getSelectedItem());
				FrustumDescription d;
				if (desc.frustum!=null) {
					d = orig.clone(desc.frustum);
				}
				else {
					d = orig.clone();
				}
				desc.frustum = d;
				desc.releaseResource();
				updateEnableState();
			}
		}
		else if (ACTION_EDIT_FRUSTUM_PARAMETERS.equals(cmd)) {
			DynamicEntityDescription desc = getSelectedEntity();
			if (desc!=null && desc.allocResource()) {
				
				if (desc.frustum!=null) {
					FrustumParameterDialog frustumDialog = new FrustumParameterDialog(this,desc.frustum);
					frustumDialog.setVisible(true);
				}
				
				desc.releaseResource();
				updateEnableState();
			}
		}
		else if (ACTION_BROWSE_SFG_FILE.equals(e.getActionCommand())) {
			File file = FrameUtil.getOpenFile(this,"Select SFG model", //$NON-NLS-1$
									new File(this.sfgFile.getText()),
									new SFGFileFilter()); 
			if (file!=null) {
				this.sfgFile.setText(file.getAbsolutePath());
			}
		}
		else if (ACTION_CLEAR_SFG_FILE.equals(e.getActionCommand())) {
			this.sfgFile.setText(null);
		}
		else {
			super.actionPerformed(e);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = this.entityList.getSelectedIndex();
		if (index>=0) {
			Object o = this.entityListModel.getElementAt(index);
			if (o instanceof DynamicEntityDescription) {
				DynamicEntityDescription desc = (DynamicEntityDescription)o;
				if (desc.allocResource()) {
					this.entityName.setText(desc.name==null ? "" : desc.name); //$NON-NLS-1$
					this.entityFile.setText(desc.modelFile==null ? "" : desc.modelFile.toString()); //$NON-NLS-1$
					this.coordinateSystem.setSelectedItem(desc.coordinateSystem);
					this.entityHeight.setValue(desc.height);
					this.entityCount.setValue(desc.count);
					this.entityOnGround.setSelected(desc.isKeepOnFloor);
					if (desc.frustum!=null)
						this.entityFrustum.setSelectedItem(desc.frustum);
					// Force the frustum type to be selected
					// and update the entity description with an change event.
					if (desc.frustum==null || this.entityFrustum.getSelectedIndex()<0) {
						this.entityFrustum.setSelectedIndex(0);
						desc.frustum = null;
					}
					this.entityPosX.setText(Double.toString(desc.position.x));
					this.entityPosY.setText(Double.toString(desc.position.y));
					this.entityPosZ.setText(Double.toString(desc.position.z));
					this.entityPosRandom.setSelected(desc.hasRandomPosition);
					this.entityPosOnSpawner.setSelected(desc.isRectrictToSpawners);
					this.entityRotation.setText(Double.toString(desc.rotation.angle));
					this.entityRotRandom.setSelected(desc.hasRandomOrientation);
					this.selectedEntityTypeModel.clear();
					this.availableEntityTypeModel.clear();
					for(TypeDescription type : this.entityTypes) {
						if (desc.types.contains(type)) {
							this.selectedEntityTypeModel.addElement(type);
						}
						else {
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
		updateDescription(getSelectedEntity());
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
	
	private static double getDouble(String field) {
		try {
			return Double.parseDouble(field);
		}
		catch(Throwable _) {
			return 0.;
		}
	}

	private static long getLong(String field) {
		try {
			return Long.parseLong(field);
		}
		catch(Throwable _) {
			return 0;
		}
	}

	private static long getLong(String field, long min, long max) {
		long v;
		try {
			v = Long.parseLong(field);
		}
		catch(Throwable _) {
			v = 0;
		}
		if (v<min) v = min;
		if (v>max) v = max;
		return v;
	}

	private static boolean getBoolean(String field) {
		try {
			return Boolean.parseBoolean(field);
		}
		catch(Throwable _) {
			return false;
		}
	}

	private static long getLong(JSpinner field) {
		try {
			return ((Number)field.getValue()).longValue();
		}
		catch(Throwable _) {
			return 0l;
		}
	}

	private static File getFile(JTextField field) {
		try {
			String txt = field.getText();
			if ((txt!=null)&&(!"".equals(txt))) { //$NON-NLS-1$
				return new File(txt);
			}
		}
		catch(Throwable _) {
			//
		}
		return null;
	}
	
	private static FrustumDescription castFrustum(Object obj) {
		if (obj instanceof FrustumDescription)
			return (FrustumDescription)obj;
		return null;
	}

	/**
	 * @param desc
	 */
	protected void updateDescription(DynamicEntityDescription desc) {
		if (desc!=null) {
			if (desc.allocResource()) {
				desc.name = this.entityName.getText();
				desc.count = getLong(this.entityCount);
				desc.frustum = castFrustum(this.entityFrustum.getSelectedItem());
				desc.hasRandomPosition = this.entityPosRandom.isSelected();
				desc.isRectrictToSpawners = this.entityPosOnSpawner.isSelected();
				desc.hasRandomOrientation = this.entityRotRandom.isSelected();
				desc.height = getDouble(this.entityHeight);
				desc.isKeepOnFloor = this.entityOnGround.isSelected();
				desc.modelFile = getFile(this.entityFile);
				desc.coordinateSystem = (CoordinateSystem3D)this.coordinateSystem.getSelectedItem();
				desc.position.set(
						getDouble(this.entityPosX),
						getDouble(this.entityPosY),
						getDouble(this.entityPosZ));
				desc.rotation.angle = getDouble(this.entityRotation);
				desc.types.clear();
				TypeDescription type;
				for(int i=0; i<this.selectedEntityTypeModel.size(); ++i) {
					type = (TypeDescription)this.selectedEntityTypeModel.getElementAt(i);
					desc.types.add(type);
				}
				desc.releaseResource();
			}
		}
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument()==this.entityName.getDocument()) {
			this.entityList.repaint();
		}
		else if (e.getDocument()==this.sfgFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument()==this.entityName.getDocument()) {
			this.entityList.repaint();
		}
		else if (e.getDocument()==this.sfgFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateDescription();
		if (e.getDocument()==this.entityName.getDocument()) {
			this.entityList.repaint();
		}
		else if (e.getDocument()==this.sfgFile.getDocument()) {
			updateEnableState();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		updateDescription();
		updateEnableState();
	}
	
	/**
	 * @throws IOException 
	 */
	protected void saveConfig() throws IOException {
		File outputFile = FrameUtil.getSaveFile(this, "Saving configuration", new INIFileFilter()); //$NON-NLS-1$
		if (outputFile!=null) {
			PrintWriter out = new PrintWriter(new FileOutputStream(outputFile));
			out.println("[TERRAIN]"); //$NON-NLS-1$
			out.print("FILE="); //$NON-NLS-1$
			out.println(this.terrainFile.getText());
			out.print("MINX="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMinX));
			out.print("MINY="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMinY));
			out.print("MINZ="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMinZ));
			out.print("MAXX="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMaxX));
			out.print("MAXY="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMaxY));
			out.print("MAXZ="); //$NON-NLS-1$
			out.println(getDouble(this.terrainMaxZ));
			out.print("NOTTRAVERSABLE="); //$NON-NLS-1$
			out.println(getLong(this.terrainNotTraversable));

			out.println("[SFG]"); //$NON-NLS-1$
			out.print("FILE="); //$NON-NLS-1$
			out.println(this.sfgFile.getText());

			int count = this.entityListModel.getSize();
			
			out.println("[ENTITIES]"); //$NON-NLS-1$
			out.print("COUNT="); //$NON-NLS-1$
			out.println(Long.toString(count));

			File f;
			DynamicEntityDescription desc;
			for(int i=0; i<count; ++i) {
				desc = (DynamicEntityDescription)this.entityListModel.getElementAt(i);
				
				out.print("[ENTITY#"); //$NON-NLS-1$
				out.print(i+1);
				out.println("]"); //$NON-NLS-1$
				out.print("COUNT="); //$NON-NLS-1$
				out.println(desc.count);
				out.print("HEIGHT="); //$NON-NLS-1$
				out.println(desc.height);
				f = desc.modelFile;
				out.print("FILE="); //$NON-NLS-1$
				out.println(f==null ? "" : f.toString()); //$NON-NLS-1$
				out.print("COORDINATES="); //$NON-NLS-1$
				out.println(desc.coordinateSystem.name());
				out.print("NAME="); //$NON-NLS-1$
				out.println(desc.name);
				out.print("ONGROUND="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.isKeepOnFloor));
				out.print("RANDOMPOS="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.hasRandomPosition));
				out.print("SPAWNERPOS="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.isRectrictToSpawners));
				out.print("RANDOMROT="); //$NON-NLS-1$
				out.println(Boolean.toString(desc.hasRandomOrientation));
				out.print("FRUSTUM_TYPE="); //$NON-NLS-1$
				if (desc.frustum!=null)
					out.println(desc.frustum.fullName.getName());
				else {
					out.println();
					out.print("FRUSTUM_EYERATIO="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.eyeRatio));
					out.print("FRUSTUM_FARDISTANCE="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.farDistance));
					out.print("FRUSTUM_HOPENNESSANGLE="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.hOpennessAngle));
					out.print("FRUSTUM_LATERALSIZE="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.lateralSize));
					out.print("FRUSTUM_NEARDISTANCE="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.nearDistance));
					out.print("FRUSTUM_VOPENNESSANGLE="); //$NON-NLS-1$
					out.println(Double.toString(desc.frustum.vOpennessAngle));
				}
				out.print("X="); //$NON-NLS-1$
				out.println(desc.position.x);
				out.print("Y="); //$NON-NLS-1$
				out.println(desc.position.y);
				out.print("Z="); //$NON-NLS-1$
				out.println(desc.position.z);
				out.print("ROTATION="); //$NON-NLS-1$
				out.println(desc.rotation);
				out.print("TYPES="); //$NON-NLS-1$
				for(int j=0; j<desc.types.size(); ++j) {
					if (j>0) out.print(File.pathSeparator);
					out.print(desc.types.get(j).fullName);
				}
				out.println();
			}		
			out.close();
		}
	}

	private static boolean isIniLine(String ucase, String line, String header, StringBuffer value) {
		if (ucase.startsWith(header+"=")) { //$NON-NLS-1$
			String v = line.substring(header.length()+1);
			if (v!=null && !"".equals(v)) { //$NON-NLS-1$
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
	@SuppressWarnings("unchecked")
	protected void loadConfig() throws IOException {
		File inputFile = FrameUtil.getOpenFile(this, "Loading configuration", new INIFileFilter()); //$NON-NLS-1$
		if (inputFile!=null) {
			List<DynamicEntityDescription> entities = new ArrayList<DynamicEntityDescription>();
			DynamicEntityDescription lastEntity = null;
			TerrainDescription terrain = new TerrainDescription();
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String uline, line = in.readLine();
			File sfg = null;
			int section = 0;
			StringBuffer value = new StringBuffer();
			while (line!=null) {
				uline = line.toUpperCase();
				if (uline.equals("[TERRAIN]")) { //$NON-NLS-1$
					section = 1;
				}                                     
				else if (uline.equals("[ENTITIES]")) { //$NON-NLS-1$
					section = 2;
				}                                     
				else if (uline.startsWith("[ENTITY")&&uline.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
					section = 3;
					if (lastEntity!=null) entities.add(lastEntity);
					lastEntity = null;
				}        
				else if (uline.equals("[SFG]")) { //$NON-NLS-1$
					section = 4;
				}                                     
				else if (uline.startsWith("[") && uline.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
					section = 0; // unrecognized section
				}                                     
				else {
					switch(section) {
					case 1:
						if (isIniLine(uline,line,"FILE",value)) { //$NON-NLS-1$
							terrain.file = new File(value.toString());
						}
						else if (isIniLine(uline,line,"MINX",value)) { //$NON-NLS-1$
							terrain.minPoint.x = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"MINY",value)) { //$NON-NLS-1$
							terrain.minPoint.y = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"MINZ",value)) { //$NON-NLS-1$
							terrain.minPoint.z = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"MAXX",value)) { //$NON-NLS-1$
							terrain.maxPoint.x = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"MAXY",value)) { //$NON-NLS-1$
							terrain.maxPoint.y = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"MAXZ",value)) { //$NON-NLS-1$
							terrain.maxPoint.z = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"NOTTRAVERSABLE",value)) { //$NON-NLS-1$
							terrain.notTraversableColor = (int)getLong(value.toString(),0,255);
						}
						break;
					case 3:
						if (lastEntity==null) lastEntity = new DynamicEntityDescription();
						if (isIniLine(uline,line,"FILE",value)) { //$NON-NLS-1$
							lastEntity.modelFile = new File(value.toString());
						}
						else if (isIniLine(uline,line,"COORDINATES",value)) { //$NON-NLS-1$
							lastEntity.coordinateSystem = CoordinateSystem3D.valueOf(value.toString());
						}
						else if (isIniLine(uline,line,"COUNT",value)) { //$NON-NLS-1$
							lastEntity.count = getLong(value.toString());
						}
						else if (isIniLine(uline,line,"HEIGHT",value)) { //$NON-NLS-1$
							lastEntity.height = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"NAME",value)) { //$NON-NLS-1$
							lastEntity.name = value.toString();
						}
						else if (isIniLine(uline,line,"ONGROUND",value)) { //$NON-NLS-1$
							lastEntity.isKeepOnFloor = getBoolean(value.toString());
						}
						else if (isIniLine(uline,line,"RANDOMPOS",value)) { //$NON-NLS-1$
							lastEntity.hasRandomPosition = getBoolean(value.toString());
						}
						else if (isIniLine(uline,line,"SPAWNERPOS",value)) { //$NON-NLS-1$
							lastEntity.isRectrictToSpawners = getBoolean(value.toString());
						}
						else if (isIniLine(uline,line,"RANDOMROT",value)) { //$NON-NLS-1$
							lastEntity.hasRandomOrientation = getBoolean(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_TYPE",value)) { //$NON-NLS-1$
							try {
								Class<?> clazz = Class.forName(value.toString());
								if (Frustum3D.class.isAssignableFrom(clazz)) {
									lastEntity.frustum = new FrustumDescription((Class<? extends Frustum3D>)clazz);
								}
								else {
									lastEntity.frustum = null;
								}
							}
							catch(Throwable _) {
								//
							}
						}
						else if (isIniLine(uline,line,"FRUSTUM_EYERATIO",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.eyeRatio = (float)getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_FARDISTANCE",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.farDistance = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_HOPENNESSANGLE",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.hOpennessAngle = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_LATERALSIZE",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.lateralSize = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_NEARDISTANCE",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.nearDistance = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"FRUSTUM_VOPENNESSANGLE",value)) { //$NON-NLS-1$
							if (lastEntity.frustum!=null)
								lastEntity.frustum.vOpennessAngle = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"X",value)) { //$NON-NLS-1$
							lastEntity.position.x = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"Y",value)) { //$NON-NLS-1$
							lastEntity.position.x = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"Z",value)) { //$NON-NLS-1$
							lastEntity.position.x = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"ROTATION",value)) { //$NON-NLS-1$
							lastEntity.rotation.angle = getDouble(value.toString());
						}
						else if (isIniLine(uline,line,"TYPES",value)) { //$NON-NLS-1$
							String[] types = value.toString().split(File.pathSeparator);
							lastEntity.types.clear();
							for(String type : types) {
								lastEntity.types.add(new TypeDescription(type));
							}
						}
						break;
					case 4:
						if (isIniLine(uline,line,"FILE",value)) { //$NON-NLS-1$
							String filename = value.toString();
							if (filename!=null && !"".equals(filename)) { //$NON-NLS-1$
								sfg = new File(filename);
							}
						}
						break;
					default:
						//
					}
				}
				line = in.readLine();
			}
			if (lastEntity!=null) entities.add(lastEntity);
			in.close();
			
			this.terrainFile.setText((terrain.file==null) ? "" : terrain.file.toString()); //$NON-NLS-1$
			this.terrainMinX.setValue(terrain.minPoint.x);
			this.terrainMinY.setValue(terrain.minPoint.y);
			this.terrainMinZ.setValue(terrain.minPoint.z);
			this.terrainMaxX.setValue(terrain.maxPoint.x);
			this.terrainMaxY.setValue(terrain.maxPoint.y);
			this.terrainMaxZ.setValue(terrain.maxPoint.z);
			
			if (sfg!=null) {
				this.sfgFile.setText(sfg.getAbsolutePath());
			}
			
			this.entityListModel.clear();
			for(DynamicEntityDescription desc : entities) {
				this.entityListModel.addElement(desc);
			}
			
			updateEnableState();
		}
	}

	/**
	 */
	protected void selectCurrentType() {
		DynamicEntityDescription desc = getSelectedEntity();
		if (desc!=null) {
			TypeDescription type = (TypeDescription)this.availableEntityTypes.getSelectedValue();
			if (type!=null) {
				insertInList(this.selectedEntityTypeModel, type);
				this.availableEntityTypeModel.removeElement(type);
				desc.types.add(type);
			}
		}
	}
	
	/**
	 */
	protected void unselectCurrentType() {
		DynamicEntityDescription desc = getSelectedEntity();
		if (desc!=null) {
			TypeDescription type = (TypeDescription)this.selectedEntityTypes.getSelectedValue();
			if (type!=null) {
				this.selectedEntityTypeModel.removeElement(type);
				insertInList(this.availableEntityTypeModel, type);
				desc.types.remove(type);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> void insertInList(DefaultListModel model, Comparable<T> obj) {
		if (obj==null) return;
		int insertionIndex = -1;
		T insideObj;
		for(int i=0; i<model.size() && insertionIndex==-1; ++i) {
			insideObj = (T)model.getElementAt(i);
			if (insideObj!=null && obj.compareTo(insideObj)<=0)
				insertionIndex = i;
		}
		if (insertionIndex>=0) {
			model.insertElementAt(obj, insertionIndex);
		}
		else {
			model.addElement(obj);
		}
	}

	/** Update the coordinate system illustration.
	 */
	protected void updateCoordinateSystemExample() {
		updateCoordinateSystemExample(getSelectedEntity());
	}
	
	/** Update the coordinate system illustration.
	 * 
	 * @param desc
	 */
	protected void updateCoordinateSystemExample(DynamicEntityDescription desc) {
		String name = null;
		switch(desc.coordinateSystem) {
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
		if (name!=null) {
			Icon ic = IconFactory.getIcon(name);
			this.coordinateSystemExample.setIcon(ic);
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
	public static class DynamicEntityDescription {
	
		private int resource = 0;
		
		/**
		 */
		protected String name;
		
		/**
		 */
		protected final List<TypeDescription> types = new ArrayList<TypeDescription>();
		
		/**
		 */
		protected File modelFile = null;
		
		/**
		 */
		protected CoordinateSystem3D coordinateSystem = CoordinateSystemConstants.SIMULATION_3D;

		/**
		 */
		protected double height = 1.8;
		
		/**
		 */
		protected long count = 1;
		
		/**
		 */
		protected boolean isKeepOnFloor = true;
		
		/**
		 */
		protected FrustumDescription frustum = null;

		/**
		 */
		protected boolean  hasRandomPosition = true;

		/**
		 */
		protected boolean isRectrictToSpawners = false;
		
		/**
		 */
		protected boolean  hasRandomOrientation = true;

		/**
		 */
		protected final Point3d position = new Point3d();

		/**
		 */
		protected final AxisAngle4d rotation = new AxisAngle4d(
				JasimConstants.DEFAULT_UP_VECTOR_X,
				JasimConstants.DEFAULT_UP_VECTOR_Y,
				JasimConstants.DEFAULT_UP_VECTOR_Z,
				0.);

		/**
		 * @return <code>true</code> if the resource is available, otherwise <code>false</code>
		 */
		protected synchronized boolean allocResource() {
			if (this.resource<=0) {
				++this.resource;
				return true;
			}
			return false;
		}
		
		/**
		 */
		protected synchronized void releaseResource() {
			if (this.resource>0) {
				--this.resource;
			}
		}

		/**
		 * @return the name of the entity
		 */
		public String getName() {
			return this.name;
		}

		private static Semantic getFirstSemanticSingleton(Class<? extends Semantic> type) {
			Class<?> fieldType;
			int modifiers;
			try {
				for(Field field : type.getDeclaredFields()) {
					fieldType = field.getType();
					modifiers = field.getModifiers();
					
					if (Modifier.isPublic(modifiers)
						&& Modifier.isStatic(modifiers)
						&& field.getName().endsWith("_SINGLETON") //$NON-NLS-1$
						&& type.isAssignableFrom(fieldType)) {
						return (Semantic)field.get(null);
					}
				}
			}
			catch(Exception _) {
				//
			}
			return null;
		}

		/**
		 * @return the types of the entity.
		 */
		@SuppressWarnings("unchecked")
		public List<Semantic> getTypes() {
			ArrayList<Semantic> l = new ArrayList<Semantic>(this.types.size());
			Class<?> semanticType;
			Semantic semantic;
			for(TypeDescription type : this.types) {
				try {
					semanticType = Class.forName(type.className);
					if (Semantic.class.isAssignableFrom(semanticType)) {
						semantic = getFirstSemanticSingleton((Class<? extends Semantic>)semanticType);
						if (semantic!=null && !ImmobileObjectType.class.equals(semantic.getClass())) {
							l.add(semantic);
						}
					}
				}
				catch(Throwable _) {
					//
				}
			}
			return l;
		}

		/**
		 * @return the model file
		 */
		public File getModelFile() {
			return this.modelFile;
		}

		/**
		 * @return the coordinate system of the 3d model.
		 */
		public CoordinateSystem3D getCoordinateSystem() {
			return this.coordinateSystem;
		}

		/**
		 * @return the entity height
		 */
		public double getHeight() {
			return this.height;
		}

		/**
		 * @return the count of entities
		 */
		public long getCount() {
			return this.count;
		}

		/**
		 * @return <code>true</code> if the entity is on ground, otherwise <code>false</code>
		 */
		public boolean isKeepOnFloor() {
			return this.isKeepOnFloor;
		}

		/**
		 * @return the type of frustum.
		 */
		public FrustumDescription getFrustumDescription() {
			return this.frustum==null ? null : this.frustum;
		}

		/**
		 * @return <code>true</code> if the position must be randomly selected,
		 * otherwise <code>false</code>
		 */
		public boolean isRandomPosition() {
			return this.hasRandomPosition;
		}

		/**
		 * @return <code>true</code> if the position must be limited to spawners,
		 * otherwise <code>false</code>
		 */
		public boolean isPositionLimitedToSpawners() {
			return this.isRectrictToSpawners;
		}

		/**
		 * @return the entity position
		 */
		public Point3d getPosition() {
			Point3d p = new Point3d(this.position);
			this.coordinateSystem.toSystem(this.position, CoordinateSystemConstants.SIMULATION_3D);
			return p;
		}

		/**
		 * @return <code>true</code> if the orientation must be randomly selected,
		 * otherwise <code>false</code>
		 */
		public boolean isRandomOrientation() {
			return this.hasRandomOrientation;
		}

		/**
		 * @return the entity rotation.
		 */
		public AxisAngle4d getRotation() {
			return this.rotation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.name;
		}

	}

	/**
	 * Describe a terrain.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class TerrainDescription {

		/**
		 */
		protected File file;
		
		/**
		 */
		protected Point3d minPoint;
		
		/**
		 */
		protected Point3d maxPoint;
		
		/**
		 */
		protected int notTraversableColor;

		/**
		 * @param url
		 * @param ix
		 * @param iy
		 * @param iz
		 * @param ax
		 * @param ay
		 * @param az
		 * @param notTraversableColor
		 */
		TerrainDescription(File url, double ix, double iy, double iz, double ax, double ay, double az, int notTraversableColor) {
			this.file = url;
			this.minPoint = new Point3d(ix,iy,iz);
			this.maxPoint = new Point3d(ax,ay,az);
			this.notTraversableColor = notTraversableColor;
		}

		/**
		 */
		TerrainDescription() {
			this.file = null;
			this.minPoint = new Point3d();
			this.maxPoint = new Point3d();
			this.notTraversableColor = 0;
		}

		/**
		 * @return the terrain file
		 */
		public File getFile() {
			return this.file;
		}

		/**
		 * @return the min point
		 */
		public Point3d getMinPoint() {
			return this.minPoint;
		}

		/**
		 * @return the max point
		 */
		public Point3d getMaxPoint() {
			return this.maxPoint;
		}

		/**
		 * @return the color under which the terrain is not traversable (inclusive),
		 * between <code>0</code> and <code>255</code>
		 */
		public int getNotTraversableColor() {
			return this.notTraversableColor;
		}
		
		/**
		 * @return the color under which the terrain is not traversable (inclusive),
		 * between <code>-128</code> and <code>127</code>
		 */
		public byte getNotTraversableColorByte() {
			return (byte)(this.notTraversableColor-128);
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
		public final String shortName;
		public final String className;
		
		public TypeDescription(String fn) {
			this.fullName = fn;
			int lastIndex = fn.lastIndexOf("."); //$NON-NLS-1$
			String sn;
			if (lastIndex>=0) 
				sn = this.fullName.substring(lastIndex+1);
			else
				sn = fn;
			if (sn.endsWith("_SINGLETON")) { //$NON-NLS-1$
				sn = sn.substring(0, sn.length()-10);
			}
			this.className = this.fullName.substring(0, lastIndex);
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
				return compareTo((TypeDescription)o)==0;
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
			TypeDescription type = (TypeDescription)l.getSelectedValue();
			if (type!=null) txt = type.fullName;
			updateEnableState();
			l.setToolTipText(txt);
		}
		
	}
	
}
