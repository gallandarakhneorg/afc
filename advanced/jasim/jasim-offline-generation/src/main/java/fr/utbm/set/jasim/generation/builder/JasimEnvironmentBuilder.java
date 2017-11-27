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
package fr.utbm.set.jasim.generation.builder;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;

import fr.utbm.set.collection.ArrayUtil;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox.PlaneConstraint;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;
import fr.utbm.set.io.collada.ColladaReader;
import fr.utbm.set.io.factory3d.event.NodeEvent;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.KinematicAgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.PedestrianFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum;
import fr.utbm.set.jasim.environment.model.perception.frustum.RectangularFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BoundCenterPartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.DynamicIcosepQuadTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.DynamicIcosepQuadTreeBuilder3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.IcosepQuadTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.StaticIcosepQuadTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.StaticIcosepQuadTreeBuilder3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.semantics.ImmobileObjectType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.live.entity.EntityInterface;
import fr.utbm.set.live.entity.RendableEntityInterface;
import fr.utbm.set.live.entity.factory.LiveFactory;
import fr.utbm.set.live.entity.factory.build.RendableEntityLiveFactory;
import fr.utbm.set.live.scenegraph.SceneGraphElement;
import fr.utbm.set.tree.builder.TreeBuilderException;

/**
 * This class permits to create the pre-build tree resources required by Jasim simulation.
 * 
 * @param <SB>
 *            static bounds
 * @param <MB>
 *            mobile bounds
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimEnvironmentBuilder<SB extends CombinableBounds3D, MB extends AlignedBoundingBox> {

	private final Class<SB> staticBoundsType;
	private final Class<MB> mobileBoundsType;
	private final File outFile;
	private final List<Entity3D<SB>> entities = new LinkedList<Entity3D<SB>>();
	private final List<MobileEntity3D<MB>> mobileEntities = new LinkedList<MobileEntity3D<MB>>();

	/**
	 * Processor used to read collada file
	 */
	private ColladaReader colladaReader = null;

	/**
	 * Factory used to build a scenegraph from a reader of a given 3D file format
	 */
	private LiveJasimFactory object3dFactory = null;

	/**
	 * @param outFile
	 *            is the file to write.
	 * @param staticBounds
	 *            type for static bounds
	 * @param mobileBounds
	 *            type for mobile bounds
	 */
	public JasimEnvironmentBuilder(File outFile, Class<SB> staticBounds, Class<MB> mobileBounds) {
		this.outFile = outFile;
		this.staticBoundsType = staticBounds;
		this.mobileBoundsType = mobileBounds;
	}

	/**
	 * @param staticBounds
	 *            type for static bounds
	 * @param mobileBounds
	 *            type for mobile bounds
	 */
	public JasimEnvironmentBuilder(Class<SB> staticBounds, Class<MB> mobileBounds) {
		this.outFile = null;
		this.staticBoundsType = staticBounds;
		this.mobileBoundsType = mobileBounds;
	}

	/**
	 * Replies 3D entities extracted from a Collada file.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param filePath
	 *            is the path to the Collada file.
	 * @param multipleEntity
	 *            is <code>true</code> to enable multiple entities, otherwise <code>false</code>
	 * @return the entities in the Collada file.
	 * @throws IOException
	 */
	private ColladaContent readColladaFile(Component parent, URL filePath, boolean multipleEntity) throws IOException {
		URL url = ColladaValidator.validate(parent, filePath, null);

		if (this.colladaReader == null) {
			this.colladaReader = new ColladaReader();
		}

		if (this.object3dFactory == null) {
			this.object3dFactory = new LiveJasimFactory(!multipleEntity);
		}
		this.object3dFactory.clear();

		this.colladaReader.setURL(url);

		this.colladaReader.addMeshListener(this.object3dFactory);
		this.colladaReader.addNodeListener(this.object3dFactory);
		this.colladaReader.addMeshAttachmentListener(this.object3dFactory);
		this.colladaReader.addErrorListener(this.object3dFactory);

		this.colladaReader.process();

		this.colladaReader.removeMeshListener(this.object3dFactory);
		this.colladaReader.removeNodeListener(this.object3dFactory);
		this.colladaReader.removeMeshAttachmentListener(this.object3dFactory);
		this.colladaReader.removeErrorListener(this.object3dFactory);

		return new ColladaContent(this.object3dFactory.getUserData(), this.object3dFactory.getRoots());
	}

	/**
	 * Create not initialized bounds.
	 * 
	 * @param boundType
	 *            is the type of bounds to generate.
	 * @return a not-initialized static bounds.
	 */
	protected SB createStaticBounds(BoundType boundType) {
		try {
			Bounds3D bb = boundType.type().newInstance();
			if (this.staticBoundsType.isInstance(bb)) {
				return this.staticBoundsType.cast(bb);
			}
		} catch (Throwable _) {
			//
		}
		try {
			return this.staticBoundsType.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create not initialized bounds.
	 * 
	 * @return a not-initialized mobile bounds.
	 */
	protected MB createMobileBounds() {
		try {
			return this.mobileBoundsType.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Replies the static entities.
	 * 
	 * @return an iterator on static entities.
	 */
	public Collection<Entity3D<SB>> getStaticEntities() {
		return Collections.unmodifiableCollection(this.entities);
	}

	/**
	 * Replies the mobile entities.
	 * 
	 * @return an iterator on mobile entities.
	 */
	public Collection<MobileEntity3D<MB>> getMobileEntities() {
		return Collections.unmodifiableCollection(this.mobileEntities);
	}

	/**
	 * Extract static ground entities from the given file and store them inside the list of entities.
	 * 
	 * @param inFile
	 *            is the file to read.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @throws IOException
	 */
	public final void extractStaticEntities(File inFile, double attemptedHeight) throws IOException {
		extractStaticEntities(inFile, attemptedHeight, null, null, true, true);
	}

	/**
	 * Extract static ground entities from the given file and store them inside the list of entities.
	 * 
	 * @param inFile
	 *            is the file to read.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param semantic
	 *            is the semantic that soulhd be associated to the red entities.
	 * @throws IOException
	 */
	public final void extractStaticEntities(File inFile, double attemptedHeight, List<Semantic> semantic) throws IOException {
		extractStaticEntities(inFile, attemptedHeight, null, semantic, true, true);
	}

	/**
	 * Extract static entities from the given file and store them inside the list of entities.
	 * 
	 * @param inFile
	 *            is the file to read.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param semantic
	 *            is the semantic that soulhd be associated to the red entities.
	 * @param onGround
	 *            indicates if the extracted entities are on ground or not.
	 * @throws IOException
	 */
	public final void extractStaticEntities(File inFile, double attemptedHeight, List<Semantic> semantic, boolean onGround) throws IOException {
		extractStaticEntities(inFile, attemptedHeight, null, semantic, onGround, true);
	}

	/**
	 * Extract static entities from the given file and store them inside the list of entities.
	 * 
	 * @param inFile
	 *            is the file to read.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param position
	 *            is the global position of the sceneGraphElement.
	 * @throws IOException
	 */
	public final void extractStaticEntities(File inFile, double attemptedHeight, Point3d position) throws IOException {
		extractStaticEntities(inFile, attemptedHeight, position, null, true, true);
	}

	/**
	 * Extract static entities from the given file and store them inside the list of entities.
	 * <p>
	 * This function scales the sceneGraphElement extracted from the given file to have a height corresponding to the given value.
	 * 
	 * @param inFile
	 *            is the file to read.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param position
	 *            is the global position of the sceneGraphElement.
	 * @param semantic
	 *            is the semantic that soulhd be associated to the red entities.
	 * @param onGround
	 *            indicates if the extracted entities are on ground or not.
	 * @param fixCoordinates
	 *            indicates if coordinates may be dynamically re-computed.
	 * @throws IOException
	 */
	@SuppressWarnings("static-method")
	public void extractStaticEntities(File inFile, double attemptedHeight, Point3d position, List<Semantic> semantic, boolean onGround, boolean fixCoordinates) throws IOException {
		throw new RuntimeException("Reimplement!!!!"); //$NON-NLS-1$
	}

	/**
	 * Extract static entities from the given file and store them inside the list of entities.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param cs
	 *            is the coordinate system used by objects in the given file.
	 * @param boundType
	 *            is the type of bounds to generate.
	 * @param obbConstraint
	 *            indicates the type of constraint to apply to OBB.
	 * @param onGround
	 *            indicates if objects are located on ground or not.
	 * @throws IOException
	 */
	public final void extractStaticEntities(Component parent, File inFile, CoordinateSystem3D cs, BoundType boundType, PlaneConstraint obbConstraint, boolean onGround) throws IOException {
		extractStaticEntities(parent, inFile, null, cs, boundType, obbConstraint, onGround);
	}

	/**
	 * Extract static entities from the given file and store them inside the list of entities.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param semantic
	 *            is the semantic to asociate to the entities.
	 * @param cs
	 *            is the coordinate system used by objects in the given file.
	 * @param boundType
	 *            is the type of bounds to generate.
	 * @param obbConstraint
	 *            indicates the type of constraint to apply to OBB.
	 * @param onGround
	 *            indicates if objects are located on ground or not.
	 * @throws IOException
	 */
	public void extractStaticEntities(Component parent, File inFile, List<Semantic> semantic, CoordinateSystem3D cs, BoundType boundType, PlaneConstraint obbConstraint, boolean onGround) throws IOException {
		ColladaContent colladaContent = readColladaFile(parent, inFile.toURI().toURL(), false);
		CoordinateSystem3D defaultCs = CoordinateSystemConstants.SIMULATION_3D;

		for (EntityInterface entity : colladaContent.entities) {
			SB bounds = createStaticBounds(boundType);

			if (entity instanceof RendableEntityInterface) {
				RendableEntityInterface rendableEntity = (RendableEntityInterface) entity;

				List<Point3d> vertices = rendableEntity.getGlobalVertices(true);
				List<Point3d> nVertices = new ArrayList<Point3d>(vertices.size());
				for (Point3d p : vertices) {
					Point3d np = new Point3d(p);
					cs.toSystem(np, defaultCs);
					nVertices.add(np);
				}

				if (bounds instanceof OrientedBoundingBox && obbConstraint != null) {
					((OrientedBoundingBox) bounds).set(obbConstraint, nVertices);
				} else {
					bounds.set(nVertices);
				}

				nVertices.clear();

				if (bounds.isEmpty()) {
					System.err.println("Bounds(" + entity.getName() + ": EMPTY BOX"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					Entity3D<SB> jentity = new Entity3D<SB>(entity.getUid(), bounds, ImmobileObjectType.IMMOBILEOBJECTTYPE_SINGLETON, onGround);

					if (semantic != null && !semantic.isEmpty()) {
						jentity.addSemantics(semantic);
					}
					
					jentity.setUserData(colladaContent.userData.get(entity.getUid()));

					this.entities.add(jentity);
				}
			}
		}
	}

	/**
	 * Extract dynamic/mobile agent from the given file and store them inside the list of entities.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param cs
	 *            is the coordinate system of the 3D model.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param position
	 *            is the global position of the sceneGraphElement.
	 * @param rotation
	 *            is the initial rotation to apply.
	 * @param semantics
	 *            are the semantics that should be associated to the red entities.
	 * @param onGround
	 *            indicates if the extracted entities are on ground or not.
	 * @param frustum
	 *            indicates the type of frustum to generate
	 * @return the created agent
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public AgentBody3D<Influence3D, Perception3D, MB> extractDynamicAgent(Component parent, File inFile, CoordinateSystem3D cs, double attemptedHeight, Point3d position, AxisAngle4d rotation, List<Semantic> semantics, boolean onGround, FrustumDescription frustum) throws IOException {
		ColladaContent colladaContent = readColladaFile(parent, inFile.toURI().toURL(), false);// We load a unique entity
		if (colladaContent.entities.size() > 0) {
			EntityInterface entity = colladaContent.entities.get(0);
			if (entity instanceof RendableEntityInterface) {
				RendableEntityInterface rendableEntity = (RendableEntityInterface) entity;
				Mesh3D mesh = new Mesh3D(false, rendableEntity.getGlobalVertices(true));

				AlignedBoundingBox bounds = new AlignedBoundingBox();
				mesh.getBounds(bounds, null);
				bounds.setTranslation(position);

				AgentBody3D<Influence3D, Perception3D, MB> jentity;

				List<ObjectType> objectTypes = new ArrayList<ObjectType>();
				if (semantics != null && !semantics.isEmpty()) {
					for (Semantic semantic : semantics) {
						if (semantic instanceof ObjectType) {
							objectTypes.add((ObjectType) semantic);
						}
					}
				}

				if (!objectTypes.isEmpty()) {
					jentity = new KinematicAgentBody3D<Influence3D, Perception3D, MB>(
							entity.getUid(), Perception3D.class, 
							(MB)bounds, 
							ArrayUtil.toArray(objectTypes, ObjectType.class), 
							mesh, 
							JasimConstants.DEFAULT_PEDESTRIAN_MAX_FORWARD_SPEED, 
							JasimConstants.DEFAULT_PEDESTRIAN_MAX_ANGULAR_SPEED);
				} else {
					jentity = new KinematicAgentBody3D<Influence3D, Perception3D, MB>(
							entity.getUid(), Perception3D.class,(MB)bounds,
							mesh,
							JasimConstants.DEFAULT_PEDESTRIAN_MAX_FORWARD_SPEED,
							JasimConstants.DEFAULT_PEDESTRIAN_MAX_ANGULAR_SPEED);
				}
				if (semantics != null && !semantics.isEmpty()) {
					for (Semantic semantic : semantics) {
						if (!(semantic instanceof ObjectType)) {
							jentity.addSemantic(semantic);
						}
					}
				}
				
				jentity.setUserData(colladaContent.userData.get(entity.getUid()));

				Frustum3D frustumInstance = createFrustum(jentity.getPosition3D(), attemptedHeight, frustum);

				if (frustumInstance != null) {
					jentity.setFrustums(frustumInstance);
				}

				jentity.rotate(rotation);

				this.mobileEntities.add(jentity);

				return jentity;
			}
		}
		return null;
	}

	/**
	 * Create and replies an instance of frustum which is described by thegiven description.
	 * 
	 * @param entityPosition
	 *            is the position of the sceneGraphElement.
	 * @param entityHeight
	 *            is the height of the sceneGraphElement.
	 * @param description
	 *            describes the frustum to create
	 * @return a new frustum instance.
	 */
	@SuppressWarnings("static-method")
	protected Frustum3D createFrustum(Point3d entityPosition, double entityHeight, FrustumDescription description) {
		Frustum3D frustum = null;

		if (description != null) {

			Point3d eyePos = new Point3d(entityPosition.x, entityPosition.y, entityPosition.z + entityHeight * description.eyeRatio);

			Direction3D viewDirection = new Direction3D(JasimConstants.DEFAULT_VIEW_VECTOR_X, JasimConstants.DEFAULT_VIEW_VECTOR_Y, JasimConstants.DEFAULT_VIEW_VECTOR_Z, 0.);

			if (description.isSphere()) {
				frustum = new SphericalFrustum(UUID.randomUUID(), eyePos, description.farDistance);
			} else if (description.isPyramid()) {
				frustum = new PyramidalFrustum(UUID.randomUUID(), eyePos, viewDirection, description.nearDistance, description.farDistance, description.hOpennessAngle, description.vOpennessAngle);
			} else if (description.isPedestrian()) {
				frustum = new PedestrianFrustum3D(UUID.randomUUID(), eyePos, description.nearDistance, // near and radius
						viewDirection, description.farDistance, description.hOpennessAngle, description.vOpennessAngle);
			} else if (description.isRectangle()) {
				frustum = new RectangularFrustum3D(UUID.randomUUID(), eyePos, viewDirection, description.lateralSize, // left right
						entityHeight * 2., // top bottom
						description.farDistance); // front back
			} else {
				throw new IllegalArgumentException("Don't know how to create an instance of " + description.fullName.getName()); //$NON-NLS-1$
			}
		}

		return frustum;
	}

	/**
	 * Extract dynamic/mobile sceneGraphElement (not an agent) from the given file and store them inside the list of entities.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param cs
	 *            is the coordinate system of the 3D model.
	 * @param attemptedHeight
	 *            is the attempted height of the object.
	 * @param position
	 *            is the global position of the sceneGraphElement.
	 * @param rotation
	 *            is the initial rotation to apply.
	 * @param semantics
	 *            are the semantics that should be associated to the red entities.
	 * @param onGround
	 *            indicates if the extracted entities are on ground or not.
	 * @return the created sceneGraphElement
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public MobileEntity3D<MB> extractDynamicEntity(Component parent, File inFile, CoordinateSystem3D cs, double attemptedHeight, Point3d position, AxisAngle4d rotation, List<Semantic> semantics, boolean onGround) throws IOException {

		ColladaContent colladaContent = readColladaFile(parent, inFile.toURI().toURL(), false);

		if (colladaContent.entities.size() > 0) {

			EntityInterface entity = colladaContent.entities.get(0);
			if (entity instanceof RendableEntityInterface) {
				RendableEntityInterface rendableEntity = (RendableEntityInterface) entity;
				Mesh3D mesh = new Mesh3D(false, rendableEntity.getGlobalVertices(true));

				AlignedBoundingBox bounds = new AlignedBoundingBox();
				mesh.getBounds(bounds, null);
				bounds.setTranslation(position);

				MobileEntity3D<MB> jentity;
				Semantic objectType = null;
				if (semantics != null && !semantics.isEmpty()) {
					for (Semantic semantic : semantics) {
						if (semantic instanceof ObjectType) {
							objectType = semantic;
						}
					}
				}

				jentity = new MobileEntity3D<MB>(entity.getUid(), (MB)bounds, objectType, onGround, mesh);

				if (semantics != null && !semantics.isEmpty()) {
					for (Semantic semantic : semantics) {
						if (objectType != semantic) {
							jentity.addSemantic(semantic);
						}
					}
				}
				
				jentity.setUserData(colladaContent.userData.get(entity.getUid()));

				jentity.rotate(rotation);

				this.mobileEntities.add(jentity);

				return jentity;
			}
		}
		return null;
	}

	/**
	 * Write a static perception tree in the output file.
	 * 
	 * @return the written tree.
	 * @throws IOException
	 * @throws TreeBuilderException
	 */
	public final PerceptionTree<CombinableBounds3D, SB, ? extends Entity3D<SB>, AlignedBoundingBox, ?> writeStaticTree() throws IOException, TreeBuilderException {
		return writeStaticTree(JasimConstants.DEFAULT_TREE_SPLIT_COUNT);
	}

	/**
	 * Write a static perception tree in the output file.
	 * 
	 * @param splitCount
	 *            is the maximal count of elements per node.
	 * @return the written tree.
	 * @throws IOException
	 * @throws TreeBuilderException
	 */
	public PerceptionTree<CombinableBounds3D, SB, ? extends Entity3D<SB>, AlignedBoundingBox, ?> writeStaticTree(int splitCount) throws IOException, TreeBuilderException {
		PerceptionTree<CombinableBounds3D, SB, ? extends Entity3D<SB>, AlignedBoundingBox, ?> tree;
		if (this.entities.isEmpty()) {
			tree = new StaticIcosepQuadTree3D<SB>();
		} else {
			StaticIcosepQuadTreeBuilder3D<SB> builder = new StaticIcosepQuadTreeBuilder3D<SB>(splitCount, BoundCenterPartitionPolicy.SINGLETON);
			tree = builder.buildTree(this.entities);
		}
		assert (tree != null);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.outFile));
		oos.writeObject(tree);
		oos.close();
		return tree;
	}

	/**
	 * Write a dynamic perception tree in the output file.
	 * 
	 * @return the written tree.
	 * @throws IOException
	 * @throws TreeBuilderException
	 */
	public final PerceptionTree<CombinableBounds3D, ?, ?, AlignedBoundingBox, ?> writeDynamicTree() throws IOException, TreeBuilderException {
		return writeDynamicTree(JasimConstants.DEFAULT_TREE_SPLIT_COUNT);
	}

	/**
	 * Write a dynamic perception tree in the output file.
	 * 
	 * @param splitCount
	 *            is the maximal count of elements per node.
	 * @return the written tree.
	 * @throws IOException
	 * @throws TreeBuilderException
	 */
	public PerceptionTree<CombinableBounds3D, ?, ?, AlignedBoundingBox, ?> writeDynamicTree(int splitCount) throws IOException, TreeBuilderException {
		PerceptionTree<CombinableBounds3D, ?, ?, AlignedBoundingBox, ?> tree;
		if (this.mobileEntities.isEmpty()) {
			tree = new DynamicIcosepQuadTree3D<AlignedBoundingBox>(new IcosepQuadTreeManipulator3D<AlignedBoundingBox>(splitCount, BoundCenterPartitionPolicy.SINGLETON, new AlignedBoundingBox()));
		} else {
			DynamicIcosepQuadTreeBuilder3D<MB> builder = new DynamicIcosepQuadTreeBuilder3D<MB>(splitCount, BoundCenterPartitionPolicy.SINGLETON);
			tree = builder.buildTree(this.mobileEntities);
		}
		assert (tree != null);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.outFile));
		oos.writeObject(tree);
		oos.close();
		return tree;
	}

	/**
	 * Extract entities from the given file and store them inside the list of goals.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param output
	 *            is the parameter to fill with SFG configuration.
	 * @throws IOException
	 */
	public void extractGoalSFCContent(Component parent, File inFile, StringBuffer output) throws IOException {
		ColladaContent colladaContent = readColladaFile(parent, inFile.toURI().toURL(), true);
		Bounds3D bounds;

		for (EntityInterface entity : colladaContent.entities) {
			if (entity instanceof RendableEntityInterface) {
				bounds = ((RendableEntityInterface) entity).getGlobalAABB(true);

				output.append("  <goal activate=\""); //$NON-NLS-1$
				output.append(Boolean.TRUE.toString());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("        name=\""); //$NON-NLS-1$
				output.append(entity.getName());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("        place=\"CHANGE ME\"\n"); //$NON-NLS-1$
				output.append("        x=\""); //$NON-NLS-1$
				output.append(bounds.getCenterX());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("        y=\""); //$NON-NLS-1$
				output.append(bounds.getCenterY());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("        z=\""); //$NON-NLS-1$
				output.append(bounds.getCenterZ());
				output.append("\" />\n"); //$NON-NLS-1$
			}

		}
	}

	/**
	 * Extract entities from the given file and store them inside the list of goals.
	 * 
	 * @param parent is the swing component of any opened dialog box.
	 * @param inFile
	 *            is the file to read.
	 * @param output
	 *            is the parameter to fill with SFG configuration.
	 * @throws IOException
	 */
	public void extractWaypointSFCContent(Component parent, File inFile, StringBuffer output) throws IOException {
		ColladaContent colladaContent = readColladaFile(parent, inFile.toURI().toURL(), true);
		Bounds3D bounds;

		for (EntityInterface entity : colladaContent.entities) {
			if (entity instanceof RendableEntityInterface) {
				bounds = ((RendableEntityInterface) entity).getGlobalAABB(true);

				output.append("  <waypoint activate=\""); //$NON-NLS-1$
				output.append(Boolean.TRUE.toString());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("            name=\""); //$NON-NLS-1$
				output.append(entity.getName());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("            place=\"CHANGE ME\"\n"); //$NON-NLS-1$
				output.append("            x=\""); //$NON-NLS-1$
				output.append(bounds.getCenterX());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("            y=\""); //$NON-NLS-1$
				output.append(bounds.getCenterY());
				output.append("\"\n"); //$NON-NLS-1$
				output.append("            z=\""); //$NON-NLS-1$
				output.append(bounds.getCenterZ());
				output.append("\" />\n"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Content of a collada file.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ColladaContent {
		
		/** */
		public final Map<UUID, Map<String,Object>> userData;
		/** */
		public final List<EntityInterface> entities;
		
		/**
		 * @param userData
		 * @param entities
		 */
		public ColladaContent(Map<UUID, Map<String,Object>> userData, List<EntityInterface> entities) {
			this.userData = userData;
			this.entities = entities;
		}
		
	}

	/**
	 * Factory of 3D elements related to the Jasim simulator.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class LiveJasimFactory extends LiveFactory {

		private final Map<UUID, Map<String,Object>> userData = new TreeMap<UUID, Map<String,Object>>();
		
		/**
		 * @param unique
		 */
		public LiveJasimFactory(boolean unique) {
			super(new RendableEntityLiveFactory(), unique, false);
			this.elements = new ElementSmartList();
		}
		
		@Override
		public void clear() {
			super.clear();
			this.userData.clear();
		}
		
		@Override
		public void nodeRead(NodeEvent event) {
			((ElementSmartList)this.elements).lastId = null;
			super.nodeRead(event);
			UUID id = ((ElementSmartList)this.elements).lastId;
			if (id!=null) {
				Map<String,Object> ud = event.getUserData();
				if (ud!=null && !ud.isEmpty()) {
					this.userData.put(id, ud);
				}
			}
		}
		
		/**
		 * @return the user data per object.
		 */
		public Map<UUID, Map<String,Object>> getUserData() {
			return this.userData;
		}
		
	}
	
	/**
	 * Smart list of elements.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ElementSmartList extends ArrayList<SceneGraphElement> {

		private static final long serialVersionUID = 8045273573470703847L;
		
		/** */
		public UUID lastId = null;
		
		/**
		 */
		public ElementSmartList() {
			//
		}
		
		@Override
		public boolean add(SceneGraphElement e) {
			boolean a = super.add(e);
			if (a) {
				this.lastId = e.getUid();
			}
			return a;
		}
		
	}

}
