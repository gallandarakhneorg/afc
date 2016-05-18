/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;

import org.arakhne.afc.math.geometry.d2.ImmutablePoint2D;
import org.eclipse.xtext.xbase.lib.Pure;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Immutable point 3D.
 * 
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ImmutablePoint3D implements UnmodifiablePoint3D<ImmutablePoint3D, ImmutableVector3D> {

	private static final long serialVersionUID = 407348048685709808L;
	
	private final double x;
	
	private final double y;
	
	private final double z;
	
	
	/**
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public ImmutablePoint3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x x coordinate 
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public ImmutablePoint3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Pure
	@Override
	public boolean equals(Object obj) {
		try {
			Tuple3D<?> tuple = (Tuple3D<?>) obj;
			return tuple.getX() == getX() && tuple.getY() == getY() && tuple.getZ() == getZ();
		} catch (AssertionError e){
			throw e;
		} catch (Throwable e1){
			return false;
		}
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x
				+";" //$NON-NLS-1$
				+this.y
				+";" //$NON-NLS-1$
				+this.z
				+")"; //$NON-NLS-1$
	}
	
	@Pure
	@Override
	public ImmutablePoint3D clone() {
		try {
			return (ImmutablePoint3D) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#getX()
	 */
	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) this.y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return (int) this.z;
	}


//	/* (non-Javadoc)
//	 * @see org.arakhne.afc.math.geometry.d3.Point3D#getGeomFactory()
//	 */
//	@Override
//	public ImmutableGeomFactory3D getGeomFactory() {
//		return ImmutableGeomFactory3D.INSTANCE;
//	}

	

}
