/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.continous.object3d;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.util.HashCodeUtil;


/**
 * Represents Euler angles.
 * <p>
 * The term "Euler Angle" is used for any representation of 3 dimensional 
 * rotations where the rotation is decomposed into 3 separate angles. 
 * <p>
 * There is no single set of conventions and standards in this area, 
 * therefore the following conventions was choosen:<ul>
 * <li>angle applied first:	heading;</li>
 * <li>angle applied second: attitude;</li>
 * <li>angle applied last: bank</li>
 * </ul>
 * <p>
 * Examples: NASA aircraft standard and telescope standard
 * <img src="doc-files/euler_plane.gif" alt="[NASA Aircraft Standard]">
 * <img src="doc-files/euler_telescop.gif" alt="[Telescope Standard]">
 * <p>
 * You must see {@link CoordinateSystem3D} for more details on rotations
 * in a specific 3D coordinate system.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EulerAngle {

	/**
	 * Rotation angle around the top vector.
	 */
	public float heading;

	/**
	 * Rotation angle around the left vector.
	 */
	public float attitude;

	/**
	 * Rotation angle around the front vector.
	 */
	public float bank;
	
	/**
	 * 
	 */
	public EulerAngle() {
		this.heading = this.attitude = this.bank = 0f;
	}

	/**
	 * @param eulers
	 */
	public EulerAngle(EulerAngle eulers) {
		this.heading = eulers.heading;
		this.attitude = eulers.attitude;
		this.bank = eulers.bank;
	}

	/**
	 * @param heading is the rotation around top vector.
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 */
	public EulerAngle(float heading, float attitude, float bank) {
		this.heading = heading;
		this.attitude = attitude;
		this.bank = bank;
	}
	
	/**
	 * @param quaternion
	 */
	public EulerAngle(Quaternion quaternion) {
		MathUtil.quat2euler(quaternion, this);
	}

	/**
	 * @param rotation
	 */
	public EulerAngle(AxisAngle4f rotation) {
		Quaternion q = new Quaternion();
		q.set(rotation);
		MathUtil.quat2euler(q, this);
	}

	/** Set the heading angle.
	 *
	 * @param heading is the rotation around top vector.
	 */
	public final void setHeading(float heading) {
		this.heading = heading;
	}

	/** Get the heading angle.
	 *
	 * @return the rotation around top vector.
	 */
	public final float getHeading() {
		return this.heading;
	}

	/** Set the attitude angle.
	 *
	 * @param attitude is the rotation around left vector.
	 */
	public final void setAttitude(float attitude) {
		this.attitude = attitude;
	}

	/** Get the attitude angle.
	 *
	 * @return the rotation around left vector.
	 */
	public final float getAttitude() {
		return this.attitude;
	}

	/** Set the bank angle.
	 *
	 * @param bank is the rotation around front vector.
	 */
	public final void setBank(float bank) {
		this.bank = bank;
	}

	/** Get the bank angle.
	 *
	 * @return the rotation around front vector.
	 */
	public final float getBank() {
		return this.bank;
	}

	/** Set the Euler angles.
	 *
	 * @param heading is the rotation around top vector.
	 * @param attitude is the rotation around left vector.
	 * @param bank is the rotation around front vector.
	 */
	public final void set(float heading, float attitude, float bank) {
		this.heading = heading;
		this.attitude = attitude;
		this.bank = bank;
	}

	/** Set the Euler angles.
	 *
	 * @param eulers
	 */
	public final void set(EulerAngle eulers) {
		this.heading = eulers.heading;
		this.attitude = eulers.attitude;
		this.bank = eulers.bank;
	}
	
	/** Set the Euler angles from a quaternion.
	 *
	 * @param quaternion
	 * @see MathUtil#quat2euler(Quaternion, EulerAngle)
	 */
	public final void set(Quaternion quaternion) {
		MathUtil.quat2euler(quaternion, this);
	}

	/** Set the Euler angles from a quaternion.
	 *
	 * @param rotation
	 * @see MathUtil#quat2euler(Quaternion, EulerAngle)
	 */
	public final void set(AxisAngle4f rotation) {
		Quaternion q = new Quaternion();
		q.set(rotation);
		MathUtil.quat2euler(q, this);
	}

	/** Set the Euler angles from a quaternion.
	 *
	 * @param quaternion
	 * @param system is the coordinate system to use for the conversion
	 * @see MathUtil#quat2euler(Quaternion, EulerAngle, CoordinateSystem3D)
	 */
	public final void set(Quaternion quaternion, CoordinateSystem3D system) {
		MathUtil.quat2euler(quaternion, this, system);
	}

	/** Set the Euler angles from a quaternion.
	 *
	 * @param rotation
	 * @param system is the coordinate system to use for the conversion
	 * @see MathUtil#quat2euler(Quaternion, EulerAngle, CoordinateSystem3D)
	 */
	public final void set(AxisAngle4f rotation, CoordinateSystem3D system) {
		Quaternion q = new Quaternion();
		q.set(rotation);
		MathUtil.quat2euler(q, this, system);
	}

	/** Replies the quaternion that is corresponding to this eulers.
	 * 
	 * @return the quaternion
	 * @see MathUtil#euler2quat(float, float, float)
	 */
	public final Quaternion toQuaternion() {
		return MathUtil.euler2quat(this.heading, this.attitude, this.bank);
	}
	
	/** Replies the quaternion that is corresponding to this eulers.
	 * 
	 * @param system is the coordinate system to use for the conversion
	 * @return the quaternion
	 * @see MathUtil#euler2quat(float, float, float, CoordinateSystem3D)
	 */
	public final Quaternion toQuaternion(CoordinateSystem3D system) {
		return MathUtil.euler2quat(this.heading, this.attitude, this.bank, system);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("heading="); //$NON-NLS-1$
		buffer.append(this.heading);
		buffer.append("; attitude="); //$NON-NLS-1$
		buffer.append(this.attitude);
		buffer.append("; bank="); //$NON-NLS-1$
		buffer.append(this.bank);
		return buffer.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		EulerAngle a = null;
		Quaternion q = null;
		if (obj instanceof EulerAngle) {
			a = (EulerAngle)obj;
		}
		else if (obj instanceof Quaternion) {
			q = (Quaternion)obj;
		}
		else if (obj instanceof AxisAngle4f) {
			q = new Quaternion();
			q.set((AxisAngle4f)obj);
		}
		if (a==null && q!=null) {
			a = MathUtil.quat2euler(q);
		}
		if (a!=null) {
			return this.attitude==a.attitude
				&& this.bank==a.bank
				&& this.heading==a.heading;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int h = HashCodeUtil.hash(this.attitude);
		h = HashCodeUtil.hash(h, this.bank);
		h = HashCodeUtil.hash(h, this.heading);
		return h;
	}

}
