/*
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.generation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;
import org.arakhne.afc.ui.swing.progress.ProgressBarModel;
import org.arakhne.afc.vmutil.FileSystem;

import fr.utbm.set.collection.SizedIterator;
import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds2d.OrientedBoundingRectangle;
import fr.utbm.set.geom.bounds.bounds2d.OrientedBounds2D;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedCombinableBounds3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.io.collada.ColladaReader;
import fr.utbm.set.io.filefilter.ColladaFileFilter;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.model.ground.SimpleHeightmapGround;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.generation.builder.ColladaValidator;
import fr.utbm.set.jasim.generation.builder.FrustumDescription;
import fr.utbm.set.jasim.generation.builder.HeightmapPictureType;
import fr.utbm.set.jasim.generation.builder.JasimEnvironmentBuilder;
import fr.utbm.set.jasim.generation.builder.JasimSimulationExtendedHeightmapGenerator;
import fr.utbm.set.jasim.generation.builder.JasimSimulationHeightmapCreator;
import fr.utbm.set.jasim.generation.builder.JasimSimulationHeightmapCreatorConstants;
import fr.utbm.set.jasim.generation.frame.DynamicTreeParameterDialog;
import fr.utbm.set.jasim.generation.frame.DynamicTreeParameterDialog.DynamicEntityDescription;
import fr.utbm.set.jasim.generation.frame.DynamicTreeParameterDialog.TerrainDescription;
import fr.utbm.set.jasim.generation.frame.FrameUtil;
import fr.utbm.set.jasim.generation.frame.GoalWaypointParameterDialog;
import fr.utbm.set.jasim.generation.frame.GoalWaypointParameterDialog.GoalWaypointDescription;
import fr.utbm.set.jasim.generation.frame.HeightmapParameterDialog;
import fr.utbm.set.jasim.generation.frame.OBRManipulationDialog;
import fr.utbm.set.jasim.generation.frame.OBRParameterDialog;
import fr.utbm.set.jasim.generation.frame.RepulsiveHeightmapParameterDialog;
import fr.utbm.set.jasim.generation.frame.StaticTreeParameterDialog;
import fr.utbm.set.jasim.generation.frame.StaticTreeParameterDialog.StaticEntityDescription;
import fr.utbm.set.live.entity.EntityInterface;
import fr.utbm.set.live.entity.RendableEntityInterface;
import fr.utbm.set.live.entity.factory.LiveFactory;
import fr.utbm.set.live.entity.factory.build.RendableEntityLiveFactory;
import fr.utbm.set.tree.io.DotDotWriter;
import fr.utbm.set.ui.IconFactory;
import fr.utbm.set.ui.window.JOptionPane;
import fr.utbm.set.ui.window.OptionPane.MessageType;
import fr.utbm.set.ui.window.OptionPane.Option;

/**
 * Main class that enables to generate several data structure for the Eurockeennes's demo:
 * <ul>
 * <li>tree of mobile entities.</li>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimSimulationGenerator extends JFrame implements ActionListener, ProgressionListener {

	private static final long serialVersionUID = -8375329790750231755L;

	private static String ACTION_GENERATE_HEIGHTMAP = "Heightmap"; //$NON-NLS-1$	
	private static String ACTION_GENERATE_EXTENDED_HEIGHTMAP = "Extended heightmap"; //$NON-NLS-1$	
	private static String ACTION_GENERATE_STATIC_TREE = "Static Tree"; //$NON-NLS-1$	
	private static String ACTION_GENERATE_DYNAMIC_TREE = "Dynamic Tree"; //$NON-NLS-1$
	private static String ACTION_GENERATE_GOALS_WAYPOINTS = "Goals Waypoints"; //$NON-NLS-1$
	private static String ACTION_VALIDATE_COLLADA = "Validate Collada"; //$NON-NLS-1$
	private static String ACTION_READ_COLLADA = "Read Collada"; //$NON-NLS-1$
	private static String ACTION_TURN_OBR = "Turn OBR"; //$NON-NLS-1$

	private static final Icon HEIGHTMAP_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/heightmap.png"); //$NON-NLS-1$
	private static final Icon EXTENDED_HEIGHTMAP_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/eheightmap.png"); //$NON-NLS-1$
	private static final Icon STATIC_TREE_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/statree.png"); //$NON-NLS-1$
	private static final Icon DYNAMIC_TREE_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/dyntree.png"); //$NON-NLS-1$
	private static final Icon GOAL_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/goals.png"); //$NON-NLS-1$
	private static final Icon COLLADA_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/collada.png"); //$NON-NLS-1$
	private static final Icon OBR_PICTURE = IconFactory.getIcon("/fr/utbm/set/jasim/generation/obr.png"); //$NON-NLS-1$

	private final JLabel progressBarComment;
	private final JProgressBar progressBar;
	private final ProgressBarModel progressBarModel;

	private transient volatile ExecutorService threadService = null;

	/**
	 */
	public JasimSimulationGenerator() {
		super("Jasim Simulation Generator"); //$NON-NLS-1$
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JButton bt;
		
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new GridLayout(4, 2));
		add(BorderLayout.CENTER, mainPanel);

		bt = new JButton("Heightmap", HEIGHTMAP_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_GENERATE_HEIGHTMAP);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Extended heightmap", EXTENDED_HEIGHTMAP_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_GENERATE_EXTENDED_HEIGHTMAP);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Static Tree", STATIC_TREE_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_GENERATE_STATIC_TREE);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Dynamic Tree", DYNAMIC_TREE_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_GENERATE_DYNAMIC_TREE);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Goals and Waypoints", GOAL_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_GENERATE_GOALS_WAYPOINTS);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Validate Collada", COLLADA_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_VALIDATE_COLLADA);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Read Collada", COLLADA_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_READ_COLLADA);
		bt.addActionListener(this);
		mainPanel.add(bt);

		bt = new JButton("Turn OBR", OBR_PICTURE); //$NON-NLS-1$
		bt.setActionCommand(ACTION_TURN_OBR);
		bt.addActionListener(this);
		mainPanel.add(bt);

		JPanel progressPanel = new JPanel(new GridLayout(2, 1));

		this.progressBar = new JProgressBar();
		this.progressBarModel = new ProgressBarModel(this.progressBar, 0, 1000);
		this.progressBar.setEnabled(false);
		progressPanel.add(this.progressBar);

		this.progressBarComment = new JLabel();
		progressPanel.add(this.progressBarComment);

		this.progressBarModel.addProgressionListener(this);

		add(BorderLayout.SOUTH, progressPanel);
		
		pack();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Class path:"); //$NON-NLS-1$
		for (String p : System.getProperty("java.class.path").split(File.pathSeparator)) { //$NON-NLS-1$
			System.out.print("\t"); //$NON-NLS-1$
			System.out.println(p);
		}
		System.out.println("Library path:"); //$NON-NLS-1$
		for (String p : System.getProperty("java.library.path").split(File.pathSeparator)) { //$NON-NLS-1$
			System.out.print("\t"); //$NON-NLS-1$
			System.out.println(p);
		}
		JasimSimulationGenerator window = new JasimSimulationGenerator();
		window.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_GENERATE_HEIGHTMAP.equals(e.getActionCommand()) || ACTION_GENERATE_EXTENDED_HEIGHTMAP.equals(e.getActionCommand()) || ACTION_GENERATE_STATIC_TREE.equals(e.getActionCommand()) || ACTION_GENERATE_DYNAMIC_TREE.equals(e.getActionCommand()) || ACTION_GENERATE_GOALS_WAYPOINTS.equals(e.getActionCommand()) || ACTION_VALIDATE_COLLADA.equals(e.getActionCommand()) || ACTION_READ_COLLADA.equals(e.getActionCommand()) || ACTION_TURN_OBR.equals(e.getActionCommand())) {
			AsynchonousAction action = new AsynchonousAction(e.getActionCommand());
			if (this.threadService == null)
				this.threadService = Executors.newCachedThreadPool();
			this.threadService.submit(action);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if (this.threadService != null) {
			this.threadService.shutdownNow();
			this.threadService = null;
		}
		super.dispose();
	}

	private void displaySuccess(File generatedFile) {
		JOptionPane.showMessageDialog(this, "The file " + generatedFile.getName() + //$NON-NLS-1$
				" was successfully generated.", //$NON-NLS-1$
				"Generation Success", //$NON-NLS-1$
				MessageType.INFORMATION);
	}

	private void displaySuccess() {
		JOptionPane.showMessageDialog(this, "Data was successfully generated.", //$NON-NLS-1$
				"Generation Success", //$NON-NLS-1$
				MessageType.INFORMATION);
	}

	private void displaySuccess(File generatedFile, Bounds<?, ?, ?> bounds) {
		JOptionPane.showMessageDialog(this, "The file " + generatedFile.getName() + //$NON-NLS-1$
				" was successfully generated.\n" + //$NON-NLS-1$
				"Bounds=\n" + //$NON-NLS-1$
				"  " + bounds.getLower().getCoordinate(0) + //$NON-NLS-1$
				" &le; X &le; " + bounds.getUpper().getCoordinate(0) + //$NON-NLS-1$
				"\n  " + bounds.getLower().getCoordinate(1) + //$NON-NLS-1$
				" &le; Y &le; " + bounds.getUpper().getCoordinate(1) + //$NON-NLS-1$
				"\n  " + bounds.getLower().getCoordinate(2) + //$NON-NLS-1$
				" &le; Z &le; " + bounds.getUpper().getCoordinate(2), //$NON-NLS-1$
				"Generation Success", //$NON-NLS-1$
				MessageType.INFORMATION);
	}

	private void displayFailure(File generatedFile) {
		JOptionPane.showMessageDialog(this, "The file " + generatedFile.getName() + //$NON-NLS-1$
				" was not generated.", //$NON-NLS-1$
				"Generation Failure", //$NON-NLS-1$
				MessageType.ERROR);
	}

	private void displayError(File generatedFile, Throwable e) {
		String msg = e.getLocalizedMessage();
		if (msg == null) {
			msg = e.toString();
		}
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, "The file " + generatedFile.getName() + //$NON-NLS-1$
				" was not generate due to an exception:\n" + //$NON-NLS-1$
				msg, "Failure", //$NON-NLS-1$
				MessageType.ERROR);
	}

	private void displayError(Throwable e) {
		String msg = e.getLocalizedMessage();
		if (msg == null) {
			msg = e.toString();
		}
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, "Error is encountered:\n" + //$NON-NLS-1$
				msg, "Failure", //$NON-NLS-1$
				MessageType.ERROR);
	}

	private void startProgressBar() {
		this.progressBar.setEnabled(true);
		this.progressBarModel.setProperties(0, 0, 100, false);
		this.progressBarComment.setEnabled(true);
		this.progressBarComment.setText(null);
	}

	private void stopProgressBar() {
		this.progressBarModel.setProperties(0, 0, 0, true);
		this.progressBarComment.setText("Finished"); //$NON-NLS-1$
		this.progressBar.setEnabled(false);
		this.progressBarComment.setEnabled(false);
	}

	/**
	 * Generate a heightmap.
	 */
	public void generateHeightmap() {
		startProgressBar();

		File inputFile;
		float precisionX, precisionY;
		int groundZero;
		HeightmapPictureType heightmapType;
		{
			HeightmapParameterDialog pd = new HeightmapParameterDialog(this);
			pd.setVisible(true);
			if (pd.getClosingState() != Option.OK)
				return;
			precisionX = pd.getWidthPrecision();
			precisionY = pd.getHeightPrecision();
			groundZero = pd.getGroundZero();
			heightmapType = pd.getHeightmapType();
			inputFile = pd.getInputFile();
		}

		File outputFile = null;
		outputFile = FileSystem.replaceExtension(new File(inputFile.toURI()), ".png"); //$NON-NLS-1$

		JasimSimulationHeightmapCreator creator = new JasimSimulationHeightmapCreator(inputFile, outputFile, precisionX, precisionY, groundZero, heightmapType, JasimSimulationHeightmapCreatorConstants.DEFAULT_HEIGHTMAP_TYPE);

		try {
			if (creator.generate())
				displaySuccess(outputFile);
			else
				displayFailure(outputFile);
		} catch (Throwable e) {
			displayError(outputFile, e);
		}

		stopProgressBar();
	}

	/**
	 * Generate a heightmap with force field.
	 */
	public void generateExtendedHeightmap() {
		startProgressBar();

		File inputFile;
		boolean repulsiveBorders, arrows;
		int force;
		int groundZero;
		{
			RepulsiveHeightmapParameterDialog pd = new RepulsiveHeightmapParameterDialog(this);
			pd.setVisible(true);
			if (pd.getClosingState() == Option.CANCEL)
				return;
			force = pd.getMaxRepulsionForce();
			repulsiveBorders = pd.isRepulsiveBorders();
			arrows = pd.isArrowPotentialFieldGenerated();
			groundZero = pd.getGroundZero();
			inputFile = pd.getInputFile();
		}

		File base = FileSystem.removeExtension(inputFile);
		File outputFile = new File(base.toString() + "#extended.png"); //$NON-NLS-1$
		File arrowFile = new File(base.toString() + "#force_field.png"); //$NON-NLS-1$

		JasimSimulationExtendedHeightmapGenerator creator = new JasimSimulationExtendedHeightmapGenerator(inputFile, null, null, null, arrows ? arrowFile : null, outputFile, groundZero + 1, force, JasimSimulationHeightmapCreatorConstants.DEFAULT_HEIGHTMAP_TYPE, repulsiveBorders);

		try {
			if (creator.generate())
				displaySuccess(outputFile);
			else
				displayFailure(outputFile);
		} catch (Throwable e) {
			displayError(outputFile, e);
		}

		stopProgressBar();
	}

	/**
	 * Generate a dynamic perception tree.
	 */
	public void generateDynamicPerceptionTree() {
		startProgressBar();

		DynamicTreeParameterDialog params = new DynamicTreeParameterDialog(this);
		params.setVisible(true);

		if (params.getClosingState() == Option.OK) {

			TerrainDescription terrain = params.getTerrainDescription();
			File terrainFile = null;
			terrainFile = terrain.getFile();

			File outputFile = FileSystem.replaceExtension(terrainFile, ".dynamic.tree"); //$NON-NLS-1$
			//File outputDotFile = FileSystem.replaceExtension(terrainFile, ".dynamic.dot"); //$NON-NLS-1$

			try {
				System.out.print("Reading ground..."); //$NON-NLS-1$
				SimpleHeightmapGround ground = new SimpleHeightmapGround(terrainFile.toURI().toURL(), UUID.randomUUID(), (byte) (terrain.getNotTraversableColorByte() + 1), terrain.getMinPoint(), terrain.getMaxPoint());
				System.out.println("done"); //$NON-NLS-1$

				JasimEnvironmentBuilder<OrientedBoundingBox, AlignedBoundingBox> builder = new JasimEnvironmentBuilder<OrientedBoundingBox, AlignedBoundingBox>(outputFile, OrientedBoundingBox.class, AlignedBoundingBox.class);
				Random random = new Random();
				double sizex = terrain.getMaxPoint().x - terrain.getMinPoint().x;
				double sizey = terrain.getMaxPoint().y - terrain.getMinPoint().y;
				double sizez = terrain.getMaxPoint().z - terrain.getMinPoint().z;

				List<AlignedBoundingBox> boxes = new ArrayList<AlignedBoundingBox>();

				DynamicEntityDescription entityDescription;

				for (int i = 0; i < params.getEntityDescriptionCount(); ++i) {
					entityDescription = params.getEntityDescriptionAt(i);

					System.out.println("Adding entity model " + (i + 1) + "/" + params.getEntityDescriptionCount() + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					List<Semantic> types = entityDescription.getTypes();
					FrustumDescription frustum = entityDescription.getFrustumDescription();

					for (int idxEntityInstance = 0; idxEntityInstance < entityDescription.getCount(); ++idxEntityInstance) {
						System.out.print("\tcomputing position " + (idxEntityInstance + 1) + "/" + entityDescription.getCount() + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						double px, py, pz;
						double angle;
						int tryCount = 0;
						do {
							if (entityDescription.isRandomPosition()) {
								px = random.nextDouble() * sizex + terrain.getMinPoint().x;
								py = random.nextDouble() * sizey + terrain.getMinPoint().y;
							} else {
								px = entityDescription.getPosition().x;
								py = entityDescription.getPosition().y;
							}
							if (entityDescription.isKeepOnFloor()) {
								pz = ground.getHeightAt(px, py);
							} else if (entityDescription.isRandomPosition()) {
								pz = random.nextDouble() * sizez + terrain.getMinPoint().z;
							} else {
								pz = entityDescription.getPosition().z;
							}
							if (!Double.isNaN(pz)) {
								boolean intersect = false;
								Point3d lower, upper;
								for (AlignedBoundingBox box : boxes) {
									lower = box.getLower();
									upper = box.getUpper();
									if (lower.x <= px && px <= upper.x && lower.y <= py && py <= upper.y) {
										intersect = true;
										break;
									}
								}
								if (intersect)
									pz = Double.NaN;
							}
							++tryCount;
						} while (Double.isNaN(pz) && tryCount < 1000);
						if (Double.isNaN(pz)) {
							System.out.println("failed"); //$NON-NLS-1$
						} else {
							System.out.println("done"); //$NON-NLS-1$

							System.out.print("\tcomputing orientation " + (idxEntityInstance + 1) + "/" + entityDescription.getCount() + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (entityDescription.isRandomOrientation()) {
								angle = random.nextDouble() * Math.PI * 2.;
							} else {
								angle = entityDescription.getRotation().angle;
							}
							System.out.println("done"); //$NON-NLS-1$

							if (frustum == null) {
								// Mobile entity, but not an agent.
								System.out.print("\tcreating mobile entity " + (idxEntityInstance + 1) + "/" + entityDescription.getCount() + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								MobileEntity3D<AlignedBoundingBox> entity = builder.extractDynamicEntity(this, entityDescription.getModelFile(), entityDescription.getCoordinateSystem(), entityDescription.getHeight(), new Point3d(px, py, pz), new AxisAngle4d(entityDescription.getRotation().x, entityDescription.getRotation().y, entityDescription.getRotation().z, angle), types, entityDescription.isKeepOnFloor());
								if (entity != null) {
									boxes.add(entity.getBounds());
								}
								System.out.println("done"); //$NON-NLS-1$
							} else {
								// Agent
								System.out.print("\tcreating agent " + (idxEntityInstance + 1) + "/" + entityDescription.getCount() + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								AgentBody3D<Influence3D, Perception3D, AlignedBoundingBox> agent = builder.extractDynamicAgent(this, entityDescription.getModelFile(), entityDescription.getCoordinateSystem(), entityDescription.getHeight(), new Point3d(px, py, pz), new AxisAngle4d(entityDescription.getRotation().x, entityDescription.getRotation().y, entityDescription.getRotation().z, angle), types, entityDescription.isKeepOnFloor(), frustum);
								if (agent != null) {
									boxes.add(agent.getBounds());
								}
								System.out.println("done"); //$NON-NLS-1$
							}
						}

					}

				}

				System.out.print("Writing dynamic tree..."); //$NON-NLS-1$
				PerceptionTree<?, ?, ?, ?, ?> tree = builder.writeDynamicTree();

				System.out.println("done"); //$NON-NLS-1$

				System.out.print("Writing dynamic tree picture..."); //$NON-NLS-1$
				File outputDotFile = FileSystem.replaceExtension(outputFile, ".dot"); //$NON-NLS-1$
				DotDotWriter writer = new DotDotWriter(new FileOutputStream(outputDotFile));
				writer.write(tree);
				writer.close();
				System.out.println("done"); //$NON-NLS-1$
				displaySuccess(outputFile, tree.getRoot().getBounds());
			} catch (Throwable e) {
				displayError(outputFile, e);
			}
		}

		stopProgressBar();
	}

	/**
	 * Generate a static perception tree.
	 */
	public void generateStaticPerceptionTree() {
		startProgressBar();

		this.progressBarModel.setValue(10, "Launching configuration window"); //$NON-NLS-1$

		StaticTreeParameterDialog params = new StaticTreeParameterDialog(this);
		params.setVisible(true);

		if (params.getClosingState() == Option.OK) {

			this.progressBarModel.setValue(100, "Extracting entities"); //$NON-NLS-1$

			File outputFile = params.getOutputFile();

			if (outputFile != null) {

				try {
					Iterator<StaticEntityDescription> iterator = params.getDescriptions();
					StaticEntityDescription description;

					JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox> builder = new JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox>(outputFile, OrientedCombinableBounds3D.class, AlignedBoundingBox.class);

					while (iterator.hasNext()) {
						description = iterator.next();
						if (description.isEnable()) {
							this.progressBarModel.setComment("Extracting entities from " //$NON-NLS-1$
									+ description.getModelFile().getName());
							builder.extractStaticEntities(this, description.getModelFile(), description.getTypes(), description.getCoordinateSystem(), description.getBoundingBoxType(), description.getOBBConstraint(), description.isOnGround());
							System.err.flush();
						}
					}

					this.progressBarModel.setValue(500, "Building and writing tree"); //$NON-NLS-1$

					PerceptionTree<CombinableBounds3D, OrientedCombinableBounds3D, ? extends Entity3D<OrientedCombinableBounds3D>, AlignedBoundingBox, ?> tree = builder.writeStaticTree();

					this.progressBarModel.setValue(925, "Writing static tree picture"); //$NON-NLS-1$

					File outputDotFile = FileSystem.replaceExtension(outputFile, ".dot"); //$NON-NLS-1$
					DotDotWriter writer = new DotDotWriter(new FileOutputStream(outputDotFile));
					writer.write(tree);
					writer.close();

					this.progressBarModel.setValue(950, "Writing planar projection from tree"); //$NON-NLS-1$

					File outputPngFile = FileSystem.replaceExtension(outputFile, "_tree_projection.png"); //$NON-NLS-1$
					writePngPicture(tree, outputPngFile);

					this.progressBarModel.setValue(970, "Writing planar projection from collection"); //$NON-NLS-1$

					outputPngFile = FileSystem.replaceExtension(outputFile, "_collection_projection.png"); //$NON-NLS-1$
					writePngPicture(builder.getStaticEntities(), outputPngFile);

					this.progressBarModel.setValue(995, "Writing informations"); //$NON-NLS-1$

					File outputTxtFile = FileSystem.replaceExtension(outputFile, ".txt"); //$NON-NLS-1$
					writeTreeInformations(tree, builder.getStaticEntities(), outputTxtFile);

					this.progressBarModel.setValue(1000);

					AlignedBoundingBox bounds = tree.getRoot().getBounds();
					displaySuccess(outputFile, bounds);
				} catch (Throwable e) {
					displayError(outputFile, e);
				}

			}
		}

		stopProgressBar();
	}

	/**
	 * Generate a goals and waypoints SFG entries.
	 */
	public void generateGoalsWaypoints() {
		startProgressBar();

		this.progressBarModel.setComment("Launching configuration window"); //$NON-NLS-1$

		GoalWaypointParameterDialog params = new GoalWaypointParameterDialog(this);
		params.setVisible(true);

		if (params.getClosingState() == Option.OK) {

			GoalWaypointDescription description;
			File modelFile;
			SizedIterator<GoalWaypointDescription> iterator = params.getDescriptions();
			int index = 0;

			this.progressBarModel.setProperties(0, 0, iterator.size() + 10, false, "Extracting entities"); //$NON-NLS-1$

			StringBuffer goalCode = new StringBuffer();
			StringBuffer waypointCode = new StringBuffer();

			JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox> builder;

			if (params.getOutputFile() != null)
				builder = new JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox>(params.getOutputFile(), OrientedCombinableBounds3D.class, AlignedBoundingBox.class);
			else
				builder = new JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox>(OrientedCombinableBounds3D.class, AlignedBoundingBox.class);

			while (iterator.hasNext()) {
				description = iterator.next();
				try {
					modelFile = description.get3DModel();

					if (description.isGoal()) {
						builder.extractGoalSFCContent(this, modelFile, goalCode);
					} else {
						builder.extractWaypointSFCContent(this, modelFile, waypointCode);
					}
				} catch (IOException e) {
					displayError(e);
				}

				++index;
				this.progressBarModel.setValue(index);
			}

			if (goalCode.length() > 0) {
				goalCode.insert(0, "<goals>\n"); //$NON-NLS-1$
				goalCode.append("</goals>\n"); //$NON-NLS-1$
			}
			if (waypointCode.length() > 0) {
				waypointCode.insert(0, "<waypoints>\n"); //$NON-NLS-1$
				waypointCode.append("</waypoints>\n"); //$NON-NLS-1$
			}

			this.progressBarModel.setValue(iterator.size(), "Saving entities"); //$NON-NLS-1$

			File outputFile = params.getOutputFile();
			if (outputFile != null) {
				try {
					FileWriter fos = new FileWriter(outputFile);
					fos.write(goalCode.toString());
					fos.write(waypointCode.toString());
					fos.close();

					this.progressBarModel.setValue(iterator.size() + 10);
					displaySuccess(outputFile);
				} catch (IOException e) {
					displayError(outputFile, e);
				}
			} else {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringBuffer fullCode = new StringBuffer();
				fullCode.append(goalCode.toString());
				fullCode.append(waypointCode.toString());
				Transferable transferable = new StringSelection(fullCode.toString());
				clipboard.setContents(transferable, null);

				this.progressBarModel.setValue(iterator.size() + 10);
				displaySuccess();
			}
		}

		stopProgressBar();
	}

	/**
	 * Validate a collada file.
	 */
	public void validateColladaFile() {
		startProgressBar();

		this.progressBarModel.setComment("Validating collada file"); //$NON-NLS-1$

		File file = FrameUtil.getOpenFile(this, "Select collada file", //$NON-NLS-1$
				(File) null, new ColladaFileFilter());
		if (file != null) {
			try {
				ColladaValidator.validate(this, file.toURI().toURL(), this.progressBarModel);
			} catch (Throwable e) {
				displayError(e);
			}
		}

		stopProgressBar();
	}

	/**
	 * Read a collada file and extract bounds.
	 */
	public void readColladaFile() {
		startProgressBar();

		this.progressBarModel.setComment("Reading collada file"); //$NON-NLS-1$

		File file = FrameUtil.getOpenFile(this, "Select collada file", //$NON-NLS-1$
				(File) null, new ColladaFileFilter());
		if (file != null) {
			try {
				File output;
				FileWriter writer;
				URL url = ColladaValidator.validate(this, file.toURI().toURL(), this.progressBarModel);

				ColladaReader colladaReader = new ColladaReader();
				LiveFactory factory = new LiveFactory(new RendableEntityLiveFactory(), false, false);
				factory.clear();

				colladaReader.setURL(url);

				colladaReader.addMeshListener(factory);
				colladaReader.addNodeListener(factory);
				colladaReader.addMeshAttachmentListener(factory);
				colladaReader.addErrorListener(factory);

				colladaReader.process();

				colladaReader.removeMeshListener(factory);
				colladaReader.removeNodeListener(factory);
				colladaReader.removeMeshAttachmentListener(factory);
				colladaReader.removeErrorListener(factory);

				List<EntityInterface> roots = factory.getRoots();

				List<Bounds3D> bounds = new ArrayList<Bounds3D>(roots.size());
				output = FileSystem.convertURLToFile(FileSystem.replaceExtension(url, "_collada_AABB.csv")); //$NON-NLS-1$
				writer = new FileWriter(output);
				writer.write("Entity Name"); //$NON-NLS-1$
				writer.write("\tObject Id"); //$NON-NLS-1$
				writer.write("\tMin X"); //$NON-NLS-1$
				writer.write("\tMin Y"); //$NON-NLS-1$
				writer.write("\tMin Z"); //$NON-NLS-1$
				writer.write("\tMax X"); //$NON-NLS-1$
				writer.write("\tMax Y"); //$NON-NLS-1$
				writer.write("\tMax Z"); //$NON-NLS-1$
				writer.write("\tCenter X"); //$NON-NLS-1$
				writer.write("\tCenter Y"); //$NON-NLS-1$
				writer.write("\tCenter Z\n"); //$NON-NLS-1$
				AlignedBoundingBox aabb;
				for (EntityInterface entity : roots) {
					if (entity instanceof RendableEntityInterface) {
						aabb = ((RendableEntityInterface) entity).getGlobalAABB(true);
						bounds.add(aabb);
						writer.write(entity.getName());
						writer.write("\t0x"); //$NON-NLS-1$
						writer.write(Integer.toHexString(System.identityHashCode(aabb)));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMinX()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMinY()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMinZ()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMaxX()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMaxY()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getMaxZ()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getCenterX()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getCenterY()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(aabb.getCenterZ()));
						writer.write("\n"); //$NON-NLS-1$
					}
				}
				writer.close();
				aabb = null;

				output = FileSystem.convertURLToFile(FileSystem.replaceExtension(url, "_collada_AABB.png")); //$NON-NLS-1$
				writePngPicture(output, bounds);

				bounds.clear();
				output = FileSystem.convertURLToFile(FileSystem.replaceExtension(url, "_collada_OBB.csv")); //$NON-NLS-1$
				writer = new FileWriter(output);
				writer.write("Entity Name"); //$NON-NLS-1$
				writer.write("\tObject Id"); //$NON-NLS-1$
				writer.write("\tCenter X"); //$NON-NLS-1$
				writer.write("\tCenter Y"); //$NON-NLS-1$
				writer.write("\tCenter Z"); //$NON-NLS-1$
				writer.write("\tR"); //$NON-NLS-1$
				writer.write("\tS"); //$NON-NLS-1$
				writer.write("\tT"); //$NON-NLS-1$
				writer.write("\tR Extent"); //$NON-NLS-1$
				writer.write("\tS Extent"); //$NON-NLS-1$
				writer.write("\tS Extent\n"); //$NON-NLS-1$
				OrientedBoundingBox obb;
				for (EntityInterface entity : roots) {
					if (entity instanceof RendableEntityInterface) {
						obb = ((RendableEntityInterface) entity).getGlobalOBB(true);
						bounds.add(obb);
						writer.write(entity.getName());
						writer.write("\t0x"); //$NON-NLS-1$
						writer.write(Integer.toHexString(System.identityHashCode(obb)));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getCenterX()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getCenterY()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getCenterZ()));
						writer.write("\t["); //$NON-NLS-1$
						writer.write(Double.toString(obb.getR().getX()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getR().getY()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getR().getZ()));
						writer.write("]\t["); //$NON-NLS-1$
						writer.write(Double.toString(obb.getS().getX()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getS().getY()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getS().getZ()));
						writer.write("]\t["); //$NON-NLS-1$
						writer.write(Double.toString(obb.getT().getX()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getT().getY()));
						writer.write(";"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getT().getZ()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getRExtent()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getSExtent()));
						writer.write("\t"); //$NON-NLS-1$
						writer.write(Double.toString(obb.getTExtent()));
						writer.write("\n"); //$NON-NLS-1$
					}
				}
				writer.close();

				output = FileSystem.convertURLToFile(FileSystem.replaceExtension(url, "_collada_OBB.png")); //$NON-NLS-1$
				writePngPicture(output, bounds);
			} catch (Throwable e) {
				displayError(e);
			}
		}

		stopProgressBar();
	}

	/**
	 * Turn OBR.
	 */
	public void turnOBR() {
		startProgressBar();

		this.progressBarModel.setValue(10, "Launching configuration window"); //$NON-NLS-1$

		OBRParameterDialog params = new OBRParameterDialog(this);
		params.setVisible(true);

		if (params.getClosingState() == Option.OK) {

			this.progressBarModel.setValue(100, "Extracting entities"); //$NON-NLS-1$

			File outputFile = params.getOutputFile();

			if (outputFile != null) {

				try {
					Iterator<OBRParameterDialog.StaticEntityDescription> iterator = params.getDescriptions();
					OBRParameterDialog.StaticEntityDescription description;

					JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox> builder = new JasimEnvironmentBuilder<OrientedCombinableBounds3D, AlignedBoundingBox>(outputFile, OrientedCombinableBounds3D.class, AlignedBoundingBox.class);

					while (iterator.hasNext()) {
						description = iterator.next();
						if (description.isEnable()) {
							this.progressBarModel.setComment("Extracting entities from " //$NON-NLS-1$
									+ description.getModelFile().getName());
							builder.extractStaticEntities(this, description.getModelFile(), description.getCoordinateSystem(), description.getBoundingBoxType(), description.getOBBConstraint(), description.isOnGround());
							System.err.flush();
						}
					}

					this.progressBarModel.setValue(1000);

					OBRManipulationDialog manipulationDialog = new OBRManipulationDialog(this, builder.getStaticEntities());
					manipulationDialog.setVisible(true);

					if (manipulationDialog.getClosingState() == Option.OK) {
						writeTurnedOBRInformations(manipulationDialog.getOBR(), outputFile);
					}

				} catch (Throwable e) {
					displayError(outputFile, e);
				}

			}
		}

		stopProgressBar();
	}

	private static void writeTreeInformations(PerceptionTree<CombinableBounds3D, ?, ?, AlignedBoundingBox, ?> tree, Collection<? extends Entity3D<? extends OrientedCombinableBounds3D>> entities, File output) throws IOException {
		output.delete();

		PrintStream ps = new PrintStream(new FileOutputStream(output));

		tree.invalidateBounds();
		AlignedBoundingBox bounds = tree.getBounds();

		if (bounds.isInit()) {
			if (bounds.isEmpty()) {
				ps.println("Bounds from tree: EMPTY"); //$NON-NLS-1$
			} else {
				ps.println("Bounds from tree:"); //$NON-NLS-1$
				ps.print("\tmin x = "); //$NON-NLS-1$
				ps.println(bounds.getMinX());
				ps.print("\tmin y = "); //$NON-NLS-1$
				ps.println(bounds.getMinY());
				ps.print("\tmin z = "); //$NON-NLS-1$
				ps.println(bounds.getMinZ());
				ps.print("\tmax x = "); //$NON-NLS-1$
				ps.println(bounds.getMaxX());
				ps.print("\tmax y = "); //$NON-NLS-1$
				ps.println(bounds.getMaxY());
				ps.print("\tmax z = "); //$NON-NLS-1$
				ps.println(bounds.getMaxZ());
			}
		} else {
			ps.println("Bounds from tree: NOT INITIALIZED"); //$NON-NLS-1$
		}

		bounds = new AlignedBoundingBox();
		OrientedCombinableBounds3D bb;
		for (Entity3D<? extends OrientedCombinableBounds3D> entity : entities) {
			bb = entity.getBounds();
			if (bb != null)
				bounds.combine(bb);
		}

		if (bounds.isInit()) {
			if (bounds.isEmpty()) {
				ps.println("Bounds from collection: EMPTY"); //$NON-NLS-1$
			} else {
				ps.println("Bounds from collection:"); //$NON-NLS-1$
				ps.print("\tmin x = "); //$NON-NLS-1$
				ps.println(bounds.getMinX());
				ps.print("\tmin y = "); //$NON-NLS-1$
				ps.println(bounds.getMinY());
				ps.print("\tmin z = "); //$NON-NLS-1$
				ps.println(bounds.getMinZ());
				ps.print("\tmax x = "); //$NON-NLS-1$
				ps.println(bounds.getMaxX());
				ps.print("\tmax y = "); //$NON-NLS-1$
				ps.println(bounds.getMaxY());
				ps.print("\tmax z = "); //$NON-NLS-1$
				ps.println(bounds.getMaxZ());
			}
		} else {
			ps.println("Bounds from collection: NOT INITIALIZED"); //$NON-NLS-1$
		}

		Collection<Bounds2D> boundsList2d = new ArrayList<Bounds2D>(entities.size());
		for (Entity3D<? extends OrientedCombinableBounds3D> entity : entities) {
			boundsList2d.add(entity.getBounds().toBounds2D());
		}

		OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
		obr.combine(boundsList2d);
		if (obr.isInit()) {
			if (obr.isEmpty()) {
				ps.println("Oriented Bounds 2D: EMPTY"); //$NON-NLS-1$
			} else {
				ps.println("Oriented Bounds 2D:"); //$NON-NLS-1$
				ps.print("\tcenter x = "); //$NON-NLS-1$
				ps.println(obr.getCenterX());
				ps.print("\tcenter y = "); //$NON-NLS-1$
				ps.println(obr.getCenterY());
				ps.print("\tR = ("); //$NON-NLS-1$
				ps.print(obr.getR().x * obr.getRExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obr.getR().y * obr.getRExtent());
				ps.println(")"); //$NON-NLS-1$
				ps.print("\tS = ("); //$NON-NLS-1$
				ps.print(obr.getS().x * obr.getSExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obr.getS().y * obr.getSExtent());
				ps.println(")"); //$NON-NLS-1$
			}
		} else {
			ps.println("Oriented Bounds 2D: NOT INITIALIZED"); //$NON-NLS-1$
		}

		Collection<Bounds3D> boundsList3d = new ArrayList<Bounds3D>(entities.size());
		for (Entity3D<? extends OrientedCombinableBounds3D> entity : entities) {
			boundsList3d.add(entity.getBounds());
		}

		OrientedBoundingBox obb = new OrientedBoundingBox();
		obb.combine(boundsList3d);
		if (obb.isInit()) {
			if (obb.isEmpty()) {
				ps.println("Oriented Bounds 3D: EMPTY"); //$NON-NLS-1$
			} else {
				ps.println("Oriented Bounds 3D:"); //$NON-NLS-1$
				ps.print("\tcenter x = "); //$NON-NLS-1$
				ps.println(obb.getCenterX());
				ps.print("\tcenter y = "); //$NON-NLS-1$
				ps.println(obb.getCenterY());
				ps.print("\tcenter z = "); //$NON-NLS-1$
				ps.println(obb.getCenterZ());
				ps.print("\tR = ("); //$NON-NLS-1$
				ps.print(obb.getR().x * obb.getRExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getR().y * obb.getRExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getR().z * obb.getRExtent());
				ps.println(")"); //$NON-NLS-1$
				ps.print("\tS = ("); //$NON-NLS-1$
				ps.print(obb.getS().x * obb.getSExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getS().y * obb.getSExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getS().z * obb.getSExtent());
				ps.println(")"); //$NON-NLS-1$
				ps.print("\tT = ("); //$NON-NLS-1$
				ps.print(obb.getT().x * obb.getTExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getT().y * obb.getTExtent());
				ps.print(";"); //$NON-NLS-1$
				ps.print(obb.getT().z * obb.getTExtent());
				ps.println(")"); //$NON-NLS-1$
			}
		} else {
			ps.println("Oriented Bounds 3D: NOT INITIALIZED"); //$NON-NLS-1$
		}

		ps.print("Type type: "); //$NON-NLS-1$
		ps.println(tree.getClass().getCanonicalName());

		ps.print("Node count: "); //$NON-NLS-1$
		ps.println(tree.getNodeCount());

		ps.print("Object count: "); //$NON-NLS-1$
		ps.println(tree.getUserDataCount());

		ps.close();
	}

	private static void writeTurnedOBRInformations(OrientedBoundingRectangle obr, File output) throws IOException {
		output.delete();

		PrintStream ps = new PrintStream(new FileOutputStream(output));

		ps.print("Center = ("); //$NON-NLS-1$
		ps.print(Double.toString(obr.getCenterX()));
		ps.print(";"); //$NON-NLS-1$
		ps.print(Double.toString(obr.getCenterY()));
		ps.println(")"); //$NON-NLS-1$

		Vector2d R = new Vector2d(obr.getR());

		ps.print("R = ("); //$NON-NLS-1$
		ps.print(Double.toString(R.x));
		ps.print(";"); //$NON-NLS-1$
		ps.print(Double.toString(R.y));
		ps.println(")"); //$NON-NLS-1$

		ps.print("Re = "); //$NON-NLS-1$
		ps.println(Double.toString(obr.getRExtent()));

		R.scale(obr.getRExtent());

		ps.print("R * Re = ("); //$NON-NLS-1$
		ps.print(Double.toString(R.x));
		ps.print(";"); //$NON-NLS-1$
		ps.print(Double.toString(R.y));
		ps.println(")"); //$NON-NLS-1$

		Vector2d S = new Vector2d(obr.getS());

		ps.print("S = ("); //$NON-NLS-1$
		ps.print(Double.toString(S.x));
		ps.print(";"); //$NON-NLS-1$
		ps.print(Double.toString(S.y));
		ps.println(")"); //$NON-NLS-1$

		ps.print("Se = "); //$NON-NLS-1$
		ps.println(Double.toString(obr.getSExtent()));

		S.scale(obr.getSExtent());

		ps.print("S * Se = ("); //$NON-NLS-1$
		ps.print(Double.toString(S.x));
		ps.print(";"); //$NON-NLS-1$
		ps.print(Double.toString(S.y));
		ps.println(")"); //$NON-NLS-1$

		ps.close();
	}

	private static final Color[] COLORS = new Color[] { Color.CYAN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK };

	private static <B extends OrientedCombinableBounds3D> void writePngPicture(PerceptionTree<CombinableBounds3D, B, ? extends Entity3D<B>, AlignedBoundingBox, ?> tree, File output) throws IOException {
		output.delete();

		// Compute bounds
		AlignedBoundingBox worldBounds = tree.getRoot().getBounds().clone();

		int color = 0;
		double scale;
		int sx, sy;
		double dx, dy;

		dx = -worldBounds.getMinX();
		dy = worldBounds.getMaxY();

		if (worldBounds.getSizeX() >= worldBounds.getSizeY()) {
			scale = worldBounds.getSizeY() / worldBounds.getSizeX();
			sx = 1024;
			sy = (int) (scale * 1024.);
			scale = 1024. / worldBounds.getSizeX();
		} else {
			scale = (worldBounds.getSizeX()) / worldBounds.getSizeY();
			sy = 1024;
			sx = (int) (scale * 1024.);
			scale = 1024. / worldBounds.getSizeY();
		}

		BufferedImage image = new BufferedImage(sx + 1, sy + 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, sx, sy);

		Iterator<? extends Entity3D<B>> dataIterator = tree.dataDepthFirstIterator();
		Entity3D<B> entity;
		OrientedCombinableBounds3D bb;

		while (dataIterator.hasNext()) {
			entity = dataIterator.next();
			bb = entity.getBounds();
			if (bb != null) {
				g2d.setColor(COLORS[color]);
				color = (color + 1) % COLORS.length;
				g2d.fill(toShape(bb, dx, dy, scale));
			}
		}

		ImageIO.write(image, "png", output); //$NON-NLS-1$
	}

	private static void toPictureCS(Point3d p, double dx, double dy, double scale) {
		double x = (int) ((p.x + dx) * scale);
		double y = (int) ((dy - p.y) * scale);
		p.set(x, y, p.z);
	}

	private static Shape toShape(Bounds2D bb, double dx, double dy, double scale) {
		GeneralPath path = new GeneralPath();

		if (bb instanceof OrientedBounds2D) {
			OrientedBounds2D obb = (OrientedBounds2D) bb;
			Point2d c = obb.getCenter();
			Vector2d R = obb.getR();
			Vector2d S = obb.getS();
			// Top
			drawFace(path, new EuclidianPoint3D(c.x, c.y, 0.), new Vector3d(R.x, R.y, 0.), new Vector3d(S.x, S.y, 0.), new Vector3d(0., 0., 1.), obb.getRExtent(), obb.getSExtent(), 0., dx, dy, scale);
		} else {
			Point2d c = bb.getCenter();
			Vector3d R = new Vector3d(1., 0., 0.);
			Vector3d S = new Vector3d(0., 1., 0.);
			Vector3d T = new Vector3d(0., 0., 1.);
			double Re = bb.getSizeX() / 2.;
			double Se = bb.getSizeY() / 2.;
			double Te = 0.;

			// Top
			drawFace(path, new EuclidianPoint3D(c.x, c.y, 0.), R, S, T, Re, Se, Te, dx, dy, scale);
		}

		return path;
	}

	private static Shape toShape(Bounds3D bb, double dx, double dy, double scale) {
		GeneralPath path = new GeneralPath();

		if (bb instanceof OrientedBounds3D) {
			OrientedBounds3D obb = (OrientedBounds3D) bb;
			// Top
			drawFace(path, obb.getCenter(), obb.getR(), obb.getS(), obb.getT(), obb.getRExtent(), obb.getSExtent(), obb.getTExtent(), dx, dy, scale);

			// Bottom
			drawFace(path, obb.getCenter(), obb.getR(), obb.getS(), obb.getT(), obb.getRExtent(), obb.getSExtent(), -obb.getTExtent(), dx, dy, scale);

			// Front
			drawFace(path, obb.getCenter(), obb.getS(), obb.getT(), obb.getR(), obb.getSExtent(), obb.getTExtent(), obb.getRExtent(), dx, dy, scale);

			// back
			drawFace(path, obb.getCenter(), obb.getS(), obb.getT(), obb.getR(), obb.getSExtent(), obb.getTExtent(), -obb.getRExtent(), dx, dy, scale);

			// Left
			drawFace(path, obb.getCenter(), obb.getR(), obb.getT(), obb.getS(), obb.getRExtent(), obb.getTExtent(), obb.getSExtent(), dx, dy, scale);

			// Right
			drawFace(path, obb.getCenter(), obb.getR(), obb.getT(), obb.getS(), obb.getRExtent(), obb.getTExtent(), -obb.getSExtent(), dx, dy, scale);
		} else {

			Vector3d R = new Vector3d(1., 0., 0.);
			Vector3d S = new Vector3d(0., 1., 0.);
			Vector3d T = new Vector3d(0., 0., 1.);
			double Re = bb.getSizeX() / 2.;
			double Se = bb.getSizeY() / 2.;
			double Te = bb.getSizeZ() / 2.;

			// Top
			drawFace(path, bb.getCenter(), R, S, T, Re, Se, Te, dx, dy, scale);

			// Bottom
			drawFace(path, bb.getCenter(), R, S, T, Se, Se, -Te, dx, dy, scale);

			// Front
			drawFace(path, bb.getCenter(), S, T, R, Se, Te, Re, dx, dy, scale);

			// back
			drawFace(path, bb.getCenter(), S, T, R, Se, Te, -Re, dx, dy, scale);

			// Left
			drawFace(path, bb.getCenter(), R, T, S, Re, Te, Se, dx, dy, scale);

			// Right
			drawFace(path, bb.getCenter(), R, T, S, Re, Te, -Se, dx, dy, scale);
		}

		return path;
	}

	private static void drawFace(GeneralPath path, EuclidianPoint3D center, Vector3d v1, Vector3d v2, Vector3d v3, double e1, double e2, double e3, double dx, double dy, double scale) {
		Vector3d v = new Vector3d();
		Point3d p = new Point3d();
		Point3d p1 = new Point3d();

		p.set(center);
		v.set(v1);
		v.scale(e1);
		p.add(v);
		v.set(v2);
		v.scale(e2);
		p.add(v);
		v.set(v3);
		v.scale(e3);
		p.add(v);
		toPictureCS(p, dx, dy, scale);
		p1.set(p);
		path.moveTo(p.x, p.y);

		p.set(center);
		v.set(v1);
		v.scale(e1);
		p.add(v);
		v.set(v2);
		v.scale(-e2);
		p.add(v);
		v.set(v3);
		v.scale(e3);
		p.add(v);
		toPictureCS(p, dx, dy, scale);
		path.lineTo(p.x, p.y);

		p.set(center);
		v.set(v1);
		v.scale(-e1);
		p.add(v);
		v.set(v2);
		v.scale(-e2);
		p.add(v);
		v.set(v3);
		v.scale(e3);
		p.add(v);
		toPictureCS(p, dx, dy, scale);
		path.lineTo(p.x, p.y);

		p.set(center);
		v.set(v1);
		v.scale(-e1);
		p.add(v);
		v.set(v2);
		v.scale(e2);
		p.add(v);
		v.set(v3);
		v.scale(e3);
		p.add(v);
		toPictureCS(p, dx, dy, scale);
		path.lineTo(p.x, p.y);

		path.lineTo(p1.x, p1.y);
	}

	private static void writePngPicture(Collection<? extends Entity3D<? extends OrientedCombinableBounds3D>> entities, File output) throws IOException {
		output.delete();

		// Compute bounds
		AlignedBoundingBox worldBounds = new AlignedBoundingBox();
		Collection<Bounds2D> bounds2d = new ArrayList<Bounds2D>(entities.size());
		OrientedCombinableBounds3D bb;
		for (Entity3D<? extends OrientedCombinableBounds3D> entity : entities) {
			bb = entity.getBounds();
			if (bb != null) {
				worldBounds.combine(bb);
				bounds2d.add(bb.toBounds2D());
			}
		}
		OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
		obr.combine(bounds2d);

		double wmxx = Math.max(worldBounds.getMaxX(), obr.getMaxX());
		double wmnx = Math.min(worldBounds.getMinX(), obr.getMinX());
		double wmxy = Math.max(worldBounds.getMaxY(), obr.getMaxY());
		double wmny = Math.min(worldBounds.getMinY(), obr.getMinY());
		double wsx = wmxx - wmnx;
		double wsy = wmxy - wmny;

		double scale;
		int sx, sy;
		double dx, dy;

		dx = -wmnx;
		dy = wmxy;

		if (wsx >= wsy) {
			scale = wsy / wsx;
			sx = 1024;
			sy = (int) (scale * 1024.);
			scale = 1024. / wsx;
		} else {
			scale = wsx / wsy;
			sy = 1024;
			sx = (int) (scale * 1024.);
			scale = 1024. / wsy;
		}

		BufferedImage image = new BufferedImage(sx + 1, sy + 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, sx, sy);

		g2d.setColor(Color.BLUE);
		g2d.draw(toShape(worldBounds, dx, dy, scale));

		g2d.setColor(Color.RED);
		g2d.draw(toShape(obr, dx, dy, scale));

		g2d.setColor(Color.GREEN);

		for (Entity3D<? extends OrientedCombinableBounds3D> entity : entities) {
			bb = entity.getBounds();
			if (bb != null) {
				g2d.draw(toShape(bb, dx, dy, scale));
			}
		}

		ImageIO.write(image, "png", output); //$NON-NLS-1$
	}

	private static void writePngPicture(File output, Collection<? extends Bounds3D> bounds) throws IOException {
		output.delete();

		// Compute bounds
		AlignedBoundingBox worldBounds = new AlignedBoundingBox();
		Collection<Bounds2D> bounds2d = new ArrayList<Bounds2D>(bounds.size());
		for (Bounds3D bb : bounds) {
			if (bb != null) {
				worldBounds.combine(bb);
				bounds2d.add(bb.toBounds2D());
			}
		}
		OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
		obr.combine(bounds2d);

		double scale;
		int sx, sy;
		double dx, dy;

		double wmxx = Math.max(worldBounds.getMaxX(), obr.getMaxX());
		double wmnx = Math.min(worldBounds.getMinX(), obr.getMinX());
		double wmxy = Math.max(worldBounds.getMaxY(), obr.getMaxY());
		double wmny = Math.min(worldBounds.getMinY(), obr.getMinY());
		double wsx = wmxx - wmnx;
		double wsy = wmxy - wmny;

		dx = -wmnx;
		dy = wmxy;

		if (wsx >= wsy) {
			scale = wsy / wsx;
			sx = 1024;
			sy = (int) (scale * 1024.);
			scale = 1024. / wsx;
		} else {
			scale = wsx / wsy;
			sy = 1024;
			sx = (int) (scale * 1024.);
			scale = 1024. / wsy;
		}

		BufferedImage image = new BufferedImage(sx + 1, sy + 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, sx, sy);

		g2d.setColor(Color.RED);
		g2d.draw(toShape(obr, dx, dy, scale));

		g2d.setColor(Color.GREEN);

		for (Bounds3D bb : bounds) {
			if (bb != null) {
				g2d.draw(toShape(bb, dx, dy, scale));
			}
		}

		ImageIO.write(image, "png", output); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProgressionStateChanged(ProgressionEvent event) {
		this.progressBarComment.setText(event.getComment());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		this.progressBarComment.setText(event.getComment());
	}

	/**
	 * Main class that enables to generate several data structure for the Eurockeennes's demo:
	 * <ul>
	 * <li>tree of mobile entities.</li>
	 * </ul>
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AsynchonousAction implements Runnable {

		private final String cmd;

		/**
		 * @param c
		 */
		public AsynchonousAction(String c) {
			this.cmd = c;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			if (ACTION_GENERATE_HEIGHTMAP.equals(this.cmd)) {
				generateHeightmap();
			} else if (ACTION_GENERATE_EXTENDED_HEIGHTMAP.equals(this.cmd)) {
				generateExtendedHeightmap();
			} else if (ACTION_GENERATE_STATIC_TREE.equals(this.cmd)) {
				generateStaticPerceptionTree();
			} else if (ACTION_GENERATE_DYNAMIC_TREE.equals(this.cmd)) {
				generateDynamicPerceptionTree();
			} else if (ACTION_GENERATE_GOALS_WAYPOINTS.equals(this.cmd)) {
				generateGoalsWaypoints();
			} else if (ACTION_VALIDATE_COLLADA.equals(this.cmd)) {
				validateColladaFile();
			} else if (ACTION_READ_COLLADA.equals(this.cmd)) {
				readColladaFile();
			} else if (ACTION_TURN_OBR.equals(this.cmd)) {
				turnOBR();
			}
		}

	}

}
