/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.bounds;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.graph.GraphSegment;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;

/**
 * Stub for GraphSegment class.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GraphSegmentStub implements GraphSegment<GraphSegmentStub, GraphPointStub>, Segment1D {

	/**
	 */
	final GraphPointStub entry;
	/**
	 */
	final GraphPointStub exit;
	/**
	 */
	final float length;
	
	/**
	 * @param entry
	 * @param exit
	 * @param length
	 */
	public GraphSegmentStub(GraphPointStub entry, GraphPointStub exit, float length) {
		this.length = length;
		this.entry = entry;
		this.exit = exit;
	}
	
	@Override
	public GraphPointStub getEndPoint() {
		return this.exit;
	}

	@Override
	public float getLength() {
		return this.length;
	}

	@Override
	public GraphPointStub getOtherSidePoint(GraphPointStub arg0) {
		if (arg0==this.entry) return this.exit;
		return this.entry;
	}

	@Override
	public GraphPointStub getBeginPoint() {
		return this.entry;
	}

	@Override
	public Point2f getFirstPoint() {
		return this.entry.getPosition();
	}

	@Override
	public Point2f getLastPoint() {
		return this.exit.getPosition();
	}

	@Override
	public boolean isFirstPointConnectedTo(Segment1D otherSegment) {
		if (otherSegment instanceof GraphSegmentStub) {
			return this.entry.isConnectedSegment((GraphSegmentStub)otherSegment);
		}
		return false;
	}

	@Override
	public boolean isLastPointConnectedTo(Segment1D otherSegment) {
		if (otherSegment instanceof GraphSegmentStub) {
			return this.exit.isConnectedSegment((GraphSegmentStub)otherSegment);
		}
		return false;
	}

	@Override
	public Vector2f getTangentAt(float positionOnSegment) {
		Vector2f v = new Vector2f();
		v.sub(getLastPoint(), getFirstPoint());
		return v;
	}

	@Override
	public void projectsOnPlane(float positionOnSegment, Point2f position,
			Vector2f tangent, CoordinateSystem2D system) {
		tangent.sub(getLastPoint(), getFirstPoint());
		tangent.normalize();
		tangent.scale(positionOnSegment);
		position.set(getFirstPoint());
		position.add(tangent);
		tangent.normalize();
	}

	@Override
	public void projectsOnPlane(float positionOnSegment, float shiftDistance,
			Point2f position, Vector2f tangent, CoordinateSystem2D system) {
		tangent.sub(getLastPoint(), getFirstPoint());
		tangent.normalize();
		Vector2f perp = MathUtil.perpendicularVector(tangent, system);
		perp.scale(shiftDistance);
		tangent.scale(positionOnSegment);
		position.set(getFirstPoint());
		position.add(tangent);
		position.add(perp);
		tangent.normalize();
	}

}
