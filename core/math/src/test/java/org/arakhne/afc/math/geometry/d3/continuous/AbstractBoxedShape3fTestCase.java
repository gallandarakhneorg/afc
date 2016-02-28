/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractBoxedShape3fTestCase<T extends AbstractBoxedShape3F> extends AbstractShape3fTestCase<T> {

	@Test
	public abstract void toBoundingBox();
	
	@Test
	public abstract void toBoundingBoxAlignedBox3x();

	@Test
	public abstract void clear();

	@Test
	public abstract void setDoubleDoubleDoubleDoubleDoubleDouble();
	
	@Test
	public abstract void setPoint3xPoint3x();

	@Test
	public abstract void setSizeXDouble();

	@Test
	public abstract void setSizeYDouble();

	@Test
	public abstract void setSizeZDouble();

	@Test
	public abstract void setFromCornersPoint3DPoint3D();

	@Test
	public abstract void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble();
	
	@Test
	public abstract void setFromCenterDoubleDoubleDoubleDoubleDoubleDouble();

	@Test
	public abstract void getMinX();

	@Test
	public abstract void setMinXDouble();

	@Test
	public abstract void getMin();

	@Test
	public abstract void getMax();

	@Test
	public abstract void getCenter();

	@Test
	public abstract void getCenterX();

	@Test
	public abstract void getMaxX();

	@Test
	public abstract void setMaxXDouble();

	@Test
	public abstract void getMinY();

	@Test
	public abstract void setMinYDouble();

	@Test
	public abstract void getCenterY();

	@Test
	public abstract void getMaxY();

	@Test
	public abstract void setMaxYDouble();

	@Test
	public abstract void getMinZ();

	@Test
	public abstract void setMinZDouble();

	@Test
	public abstract void getCenterZ();

	@Test
	public abstract void getMaxZ();
	
	@Test
	public abstract void setMaxZDouble();

	@Test
	public abstract void getSizeX();

	@Test
	public abstract void getSizeY();
	
	@Test
	public abstract void getSizeZ();

	@Test
	public abstract void translateDoubleDoubleDouble();

	@Test
	public abstract void isEmpty();
	
	@Test
	public abstract void inflateDoubleDoubleDoubleDoubleDoubleDouble();

	@Test
	public abstract void translateVector3D();

	@Test
	public abstract void setXDoubleDouble();

	@Test
	public abstract void setYDoubleDouble();

	@Test
	public abstract void setZDoubleDouble();

}