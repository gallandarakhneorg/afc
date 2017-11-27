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
package org.arakhne.afc.jasim.environment.model.place;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.intersection.IntersectionUtil;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.math.MathUtil;

/** This classes describes a portal position in a {@link Place place}.
 * <p>
 * A portal is a segment on the ground described by two points.
 * The portal is traversable by on of its side only.
 * When the portal was traversed, the traversing object must lies
 * with specific position and orientation on the target place.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SegmentPortalPosition implements PortalPosition {

	private final double p1x, p1y, p1z, p2x, p2y, p2z;
	private final boolean entryOnLeft;
	private final double tx, ty, tz, vx, vy, vz;
	
	/**
	 * @param x1 is the position of the portal on the start place.
	 * @param y1 is the position of the portal on the start place.
	 * @param x2 is the position of the portal on the start place.
	 * @param y2 is the position of the portal on the start place.
	 * @param entryOnLeft indicates if the entry side is on the left of the given segment.
	 * @param tx is the arriving point on the end place.
	 * @param ty is the arriving point on the end place.
	 * @param vx is the arriving direction on the end place.
	 * @param vy is the arriving direction on the end place.
	 */
	public SegmentPortalPosition(double x1, double y1, double x2, double y2, boolean entryOnLeft, double tx, double ty, double vx, double vy) {
		this(x1,y1,0,x2,y2,0,entryOnLeft,tx,ty,0,vx,vy,0);
	}

	/**
	 * @param x1 is the position of the portal on the start place.
	 * @param y1 is the position of the portal on the start place.
	 * @param z1 is the position of the portal on the start place.
	 * @param x2 is the position of the portal on the start place.
	 * @param y2 is the position of the portal on the start place.
	 * @param z2 is the position of the portal on the start place.
	 * @param entryOnLeft indicates if the entry side is on the left of the given segment.
	 * @param tx is the arriving point on the end place.
	 * @param ty is the arriving point on the end place.
	 * @param tz is the arriving point on the end place.
	 * @param vx is the arriving direction on the end place.
	 * @param vy is the arriving direction on the end place.
	 * @param vz is the arriving direction on the end place.
	 */
	public SegmentPortalPosition(double x1, double y1, double z1, double x2, double y2, double z2, boolean entryOnLeft, double tx, double ty, double tz, double vx, double vy, double vz) {
		this.p1x = x1;
		this.p1y = y1;
		this.p1z = z1;
		this.p2x = x2;
		this.p2y = y2;
		this.p2z = z2;
		this.entryOnLeft = entryOnLeft;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2d getExitPoint2D() {
		return new Point2d(this.tx, this.ty);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Point3d getExitPoint3D() {
		return new Point3d(this.tx, this.ty, this.tz);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getExitVector2D() {
		return new Vector2d(this.vx, this.vy);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3d getExitVector3D() {
		return new Vector3d(this.vx, this.vy, this.vz);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2d getEntryPoint2D() {
		return new Point2d(
				(this.p2x+this.p1x)/2.,
				(this.p2y+this.p1y)/2.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3d getEntryPoint3D() {
		return new Point3d(
				(this.p2x+this.p1x)/2.,
				(this.p2y+this.p1y)/2.,
				(this.p2z+this.p1z)/2.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getEntryVector2D() {
		Vector2d v;
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(this.p2x-this.p1x, this.p2y-this.p1y, cs);
		if (this.entryOnLeft!=cs.isRightHanded()) v.negate();
		return v;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3d getEntryVector3D() {
		Vector2d v;
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(this.p2x-this.p1x, this.p2y-this.p1y, cs);
		if (this.entryOnLeft!=cs.isRightHanded()) v.negate();
		return new Vector3d(v.x,v.y,0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double startX, double startY, double endX, double endY) {
		if (IntersectionUtil.intersectsSegments(this.p1x, this.p1y, this.p2x, this.p2y, startX, startY, endX, endY)) {
			Vector2d entry = getEntryVector2D();
			double dp = MathUtil.dotProduct(endX-startX, endY-startY, 0, entry.x, entry.y, 0);
			return (dp<0.);
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double startX, double startY, double startZ, double endX, double endY, double endZ) {
		if (IntersectionUtil.intersectsSegments(this.p1x, this.p1y, this.p2x, this.p2y, startX, startY, endX, endY)) {
			Vector3d entry = getEntryVector3D();
			double dp = MathUtil.dotProduct(endX-startX, endY-startY, endZ-startZ, entry.x, entry.y, entry.z);
			return (dp<0.);
		}
		return false;
	}
	
}