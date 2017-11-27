/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.environment.model.world;

import java.lang.reflect.Constructor;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.BoundingSphere;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;

/** This class contains a spherical mesh.
 * 
 * <h2>Compute the mesh points</h2>
 * 
 * <h3>Defining Evenly Distributed</h3>
 * 
 * Some would argue that for points to be evenly distributed on a sphere
 * the resulting polygonal object defined by the points needs to have
 * faces that are equal as well as an equal number of faces leading into
 * every vertex. These perfect shapes are known as
 * <a href="http://en.wikipedia.org/wiki/Platonic_solid">Platonic Solids</a>.
 * <p>
 * There are unfortunately only five platonic solids: the tetrahedron, cube,
 * octahedron, dodecahedron and icosahedron each having 4, 8, 6, 20 and 12
 * vertices.
 * <p>
 * So unless that is exactly the number of points you wish to have around
 * your sphere we must somehow redefine evenly distributed. There is a great
 * discussion on this titled
 * "<a href="http://www.math.niu.edu/~rusin/known-math/95/sphere.faq">Topics
 * On Sphere Distributions</a>" by Dave Rusin.
 * <p>
 * In our case, let's ignore how the points would combine to create a solid
 * and concentrate on the distance relationship to its neighbors considering
 * the whole set. For any given number of points what we want is for the
 * minimum distance between any two points to be as large as possible.
 * Makes sense? If any two closest points in the whole set are as far apart
 * as possible, all points should be equally distant from their closest
 * neighbor. That is what we will define as evenly distributed.
 * 
 * <h3>How to achieve even distribution</h3>
 * 
 * One of the most precise ways to organize points on a sphere, given our
 * definition, would be to simulate them repelling themselves with equal
 * force until they settled (see
 * <a href="http://www.chiark.greenend.org.uk/~sgtatham/polyhedra/">this
 * document</a> by Simon Tatham). It would be exact but wouldn't necessarily
 * be quick. Then there are three algorithms that approximate the same
 * result all the while requiring less computational power.
 * <p>
 * The first one is
 * <a href="http://www.math.niu.edu/~rusin/known-math/95/equispace.elect">Dave
 * Rusin's Disco Ball</a>. Although it will wield a 
 * very precise pattern the algorithm does not allow to specify an exact
 * number of points which are then distributed.
 * <p>
 * The second one is the method of
 * <a href="http://sitemason.vanderbilt.edu/page/hmbADS">Saff and Kuijlaars</a>.
 * This second one packs the points much less tightly than the Disco Ball but
 * manages to do so with any arbitrary number of points.
 * <p>
 * Finally there is an algorithm based on the greek golden ratio, the
 * <a href="http://cgafaq.info/wiki/Evenly_distributed_points_on_sphere">Golden
 * Section spiral</a>. This last stab at the problem can generate 
 * a set packed more evenly than Saff and Kuijlaars while being able to
 * specify any number of points.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SphereMesh3D extends Mesh3D {

	private static final long serialVersionUID = 1965268218006026632L;
	
	/** Default count of points on the sphere.
	 */
	public static final int DEFAULT_POINT_COUNT_ON_SPHERE = 20;
	
	private final double radius;
	private final boolean onGround;
	private final int pointOnSphere;
	private BoundingSphere boundingSphere = null; 

	/**
	 * Constructs a local sphere.
	 * 
	 * @param radius is the radius of the sphere
	 * @param onGround indicates if the sphere may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the sphere center will be at the origin.
	 * @param pointOnSphere is the count of points on the sphere.
	 */ 
	public SphereMesh3D(double radius, boolean onGround, int pointOnSphere) {
		super(true);
		this.radius = radius;
		this.onGround = onGround;
		this.pointOnSphere = pointOnSphere;
	}

	/**
	 * Constructs a local sphere.
	 * 
	 * @param radius is the radius of the sphere
	 * @param onGround indicates if the sphere may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the sphere center will be at the origin.
	 */ 
	public SphereMesh3D(double radius, boolean onGround) {
		this(radius,onGround,DEFAULT_POINT_COUNT_ON_SPHERE);
	}

	/** Clone this mesh.
	 * 
	 * @return a clone.
	 */
	@Override
	public SphereMesh3D clone() {
			return (SphereMesh3D)super.clone();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3d[] getPoints() {
		Point3d[] pts = super.getPoints();
		if (pts==null || pts.length==0) {
			Vector3d center = new Vector3d();
			if (this.onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				assert(cs!=null);
				cs.setHeight(center, this.radius);
			}

			Matrix4d m = new Matrix4d();
			m.setScale(this.radius);
			m.setTranslation(center);
			
			//
			// Gloden Section Spiral Algorithm
			//
			double y, r, phi;
			
			double inc = Math.PI * (3. - Math.sqrt(5.));
			double off = 2. / this.pointOnSphere;
			
			pts = new Point3d[this.pointOnSphere];
			
			for(int k=0; k<this.pointOnSphere; ++k) {
				y = k * off - 1. + (off / 2.);
		        r = Math.sqrt(1. - y*y);
		        phi = k * inc;
		        pts[k] = new Point3d(
		        		Math.cos(phi) * r,
		        		y,
		        		Math.sin(phi) * r);
		        m.transform(pts[k]);
			}

			setPoints(pts);
		}
		return pts;
	}
	
	/** Replies the bounding sphere of this mesh.
	 * 
	 * @return the bounding sphere.
	 */
	public BoundingSphere getBoundingSphere() {
		if (this.boundingSphere==null) {
			Point3d center = new Point3d();
			if (this.onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				assert(cs!=null);
				cs.setHeight(center, this.radius);
			}
			this.boundingSphere = new BoundingSphere(center, this.radius);
		}
		return this.boundingSphere;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public <B extends CombinableBounds3D> B toBounds(Class<? extends B> boundClass) {
		BoundingSphere bb = getBoundingSphere();
		assert(bb!=null);
		try {
			Constructor<? extends B> cons = boundClass.getConstructor(CombinableBounds3D[].class);
			return cons.newInstance((Object)(new CombinableBounds3D[]{bb}));
		}
		catch(Exception _) {
			//
		}
		try {
			Constructor<? extends B> cons = boundClass.getConstructor();
			B bounds = cons.newInstance();
			bounds.set(bb);
			return bounds;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getBounds(CombinableBounds3D bounds, Quat4d rotation, Tuple3d pivot) {
		Matrix4d m = GeometryUtil.rotateAround(rotation, pivot);
		Point3d center = new Point3d();		
		m.transform(center);		
		bounds.set(new BoundingSphere(center, this.radius));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getBounds(CombinableBounds3D bounds, Quat4d rotation) {
		Matrix4d m = new Matrix4d();
		m.set(rotation);
		Point3d center = new Point3d();		
		m.transform(center);		
		bounds.set(new BoundingSphere(center, this.radius));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void makeLocal(Point3d localReference) {
		super.makeLocal(localReference);
		this.boundingSphere = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void makeGlobal(Point3d localReference) {
		super.makeGlobal(localReference);
		this.boundingSphere = null;
	}
	
}