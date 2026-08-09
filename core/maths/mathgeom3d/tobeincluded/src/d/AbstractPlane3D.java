/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D plane.
 *
 * @param <PT> is the type of the plane.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlane3D<PT extends AbstractPlane3D<? super PT>> implements Plane3D<PT> {
	
	private static final long serialVersionUID = -8226008101273829655L;

	@Pure
	@Override
	public boolean intersects(AbstractOrientedBox3F box) {
		// Compute the effective radius of the obb and
		// compare it with the distance between the obb center
		// and the plane; source MGPCG pp.235
		Vector3f n = (Vector3f) getNormal();

		double dist = Math.abs(distanceTo(box.getCenter()));

		double effectiveRadius;

		effectiveRadius = Math.abs(
				FunctionalVector3D.dotProduct(
						box.getFirstAxisX() * box.getFirstAxisExtent(), 
						box.getFirstAxisY() * box.getFirstAxisExtent(),
						box.getFirstAxisZ() * box.getFirstAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		effectiveRadius += Math.abs(
				FunctionalVector3D.dotProduct(
						box.getSecondAxisX() * box.getSecondAxisExtent(), 
						box.getSecondAxisY() * box.getSecondAxisExtent(),
						box.getSecondAxisZ() * box.getSecondAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		effectiveRadius += Math.abs(
				FunctionalVector3D.dotProduct(
						box.getThirdAxisX() * box.getThirdAxisExtent(), 
						box.getThirdAxisY() * box.getThirdAxisExtent(),
						box.getThirdAxisZ() * box.getThirdAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		return MathUtil.compareEpsilon(dist, effectiveRadius) <= 0;
	}
    
}