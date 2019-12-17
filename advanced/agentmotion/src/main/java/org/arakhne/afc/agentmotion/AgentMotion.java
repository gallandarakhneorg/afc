/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.agentmotion;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Describes the motion of an agent.
 *
 * <p>This class is typically an output of the agent motion algorithms.
 *
 * <p>The units of the values depend on the agent motion algorithm that is creating this object:
 * <ul>
 * <li>kinematic: linear motion is in meter per second (or km/s, etc.); angular motion is in radians per second (or d/s).</li>
 * <li>steering: linear motion is in meter per squared second (or km/(s*s), etc.); angular motion is in radians
 * per squared second (or d/(s*s)).</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class AgentMotion implements Serializable, Cloneable {

	private static final long serialVersionUID = 8733139573585094484L;

	private Vector2d linear = new Vector2d();

	private double angular;

	/** Construct a zero motion.
	 */
	public AgentMotion() {
		//
	}

	/** Constructor by copy.
	 *
	 * @param outputToCopy is the output to copy.
	 */
	public AgentMotion(AgentMotion outputToCopy) {
		assert outputToCopy != null : AssertMessages.notNullParameter();
		this.linear.set(outputToCopy.getLinear());
		this.angular = outputToCopy.getAngular();
	}

	/** Construct a motion.
	 *
	 * @param angularMotion the angular motion.
	 */
	public AgentMotion(double angularMotion) {
		this.angular = angularMotion;
	}

	/** Construct a motion.
	 *
	 * @param linearx the motion along the x axis.
	 * @param lineary the motion along the y axis.
	 */
	public AgentMotion(double linearx, double lineary) {
		this.linear.set(linearx, lineary);
	}

	/** Construct a motion.
	 *
	 * @param linearx the motion along the x axis.
	 * @param lineary the motion along the y axis.
	 * @param angularMotion the angular motion.
	 */
	public AgentMotion(double linearx, double lineary, double angularMotion) {
		this.linear.set(linearx, lineary);
		this.angular = angularMotion;
	}

	/** Construct a motion.
	 *
	 * @param linearMotion the linear motion.
	 */
	public AgentMotion(Vector2D<?, ?> linearMotion) {
		assert linearMotion != null : AssertMessages.notNullParameter();
		this.linear.set(linearMotion);
	}

	/** Construct a motion.
	 *
	 * @param linearMotion the linear motion.
	 * @param angularMotion the angular motion.
	 */
	public AgentMotion(Vector2D<?, ?> linearMotion, double angularMotion) {
		assert linearMotion != null : AssertMessages.notNullParameter();
		this.linear.set(linearMotion);
		this.angular = angularMotion;
	}

	/** Replies the subtraction of the given linear and angular motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@ScalaOperator("-")
	public AgentMotion $minus(AgentMotion motion) {
		return operator_minus(motion);
	}

	/** Replies the subtraction of the given angular and angular motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@ScalaOperator("-")
	public AgentMotion $minus(double motion) {
		return operator_minus(motion);
	}

	/** Replies the subtraction of the given linear motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@ScalaOperator("-")
	public AgentMotion $minus(Vector2D<?, ?> motion) {
		return operator_minus(motion);
	}

	/** Replies the addition of the given linear and angular motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@ScalaOperator("+")
	public AgentMotion $plus(AgentMotion motion) {
		return operator_plus(motion);
	}

	/** Replies the addition of the given angular motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@ScalaOperator("+")
	public AgentMotion $plus(double motion) {
		return operator_plus(motion);
	}

	/** Replies the addition of the given linear motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@ScalaOperator("+")
	public AgentMotion $plus(Vector2D<?, ?> motion) {
		return operator_plus(motion);
	}

	@Pure
	@Override
	public AgentMotion clone() {
		try {
			final AgentMotion clone = (AgentMotion) super.clone();
			clone.linear = this.linear.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj.getClass() == getClass()) {
			final AgentMotion other = (AgentMotion) obj;
			return other.getAngular() == getAngular()
					&& other.getLinear().equals(getLinear());
		}
		return false;
	}

	/** Replies the rotation.
	 *
	 * @return the angular motion.
	 */
	@Pure
	public double getAngular() {
		return this.angular;
	}

	/** Replies the motion on the plane.
	 *
	 * @return the linear motion.
	 */
	@Pure
	public Vector2D<?, ?> getLinear() {
		return this.linear.toUnmodifiable();
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.linear.hashCode();
		bits = 31 * bits + Double.hashCode(this.angular);
		return bits ^ (bits >> 31);
	}

	/** Add the given linear and angular motion to this motion: {@code this += motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 */
	@Pure
	@XtextOperator("+=")
	public void operator_add(AgentMotion motion) {
		assert motion != null : AssertMessages.notNullParameter();
		this.linear.add(motion.getLinear());
		this.angular += motion.getAngular();
	}

	/** Add the given angular motion to this motion: {@code this += motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 */
	@Pure
	@XtextOperator("+=")
	public void operator_add(double motion) {
		this.angular += motion;
	}

	/** Add the given linear motion to this motion: {@code this += motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 */
	@Pure
	@XtextOperator("+=")
	public void operator_add(Vector2D<?, ?> motion) {
		assert motion != null : AssertMessages.notNullParameter();
		this.linear.add(motion);
	}

	/** Replies the subtraction of the given linear and angular motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@XtextOperator("-")
	public AgentMotion operator_minus(AgentMotion motion) {
		assert motion != null : AssertMessages.notNullParameter();
		final Vector2D<?, ?> mymotion = getLinear();
		final Vector2D<?, ?> othermotion = motion.getLinear();
		return new AgentMotion(
				mymotion.getX() - othermotion.getX(),
				mymotion.getY() - othermotion.getY(),
				getAngular() - motion.getAngular());
	}

	/** Replies the subtraction of the given angular motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@XtextOperator("-")
	public AgentMotion operator_minus(double motion) {
		return new AgentMotion(
				getLinear(),
				getAngular() - motion);
	}

	/** Replies the subtraction of the given linear motion and this motion: {@code this - motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the subtraction.
	 */
	@Pure
	@XtextOperator("-")
	public AgentMotion operator_minus(Vector2D<?, ?> motion) {
		assert motion != null : AssertMessages.notNullParameter();
		final Vector2D<?, ?> mymotion = getLinear();
		return new AgentMotion(
				mymotion.getX() - motion.getX(),
				mymotion.getY() - motion.getY(),
				getAngular());
	}

	/** Replies the addition of the given linear and angular motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@XtextOperator("+")
	public AgentMotion operator_plus(AgentMotion motion) {
		assert motion != null : AssertMessages.notNullParameter();
		final Vector2D<?, ?> mymotion = getLinear();
		final Vector2D<?, ?> othermotion = motion.getLinear();
		return new AgentMotion(
				mymotion.getX() + othermotion.getX(),
				mymotion.getY() + othermotion.getY(),
				getAngular() + motion.getAngular());
	}

	/** Replies the addition of the given angular motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@XtextOperator("+")
	public AgentMotion operator_plus(double motion) {
		return new AgentMotion(
				getLinear(),
				getAngular() + motion);
	}

	/** Replies the addition of the given linear motion and this motion: {@code this + motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to add.
	 * @return the sum.
	 */
	@Pure
	@XtextOperator("+")
	public AgentMotion operator_plus(Vector2D<?, ?> motion) {
		assert motion != null : AssertMessages.notNullParameter();
		final Vector2D<?, ?> mymotion = getLinear();
		return new AgentMotion(
				mymotion.getX() + motion.getX(),
				mymotion.getY() + motion.getY(),
				getAngular());
	}

	/** Subtract the given linear and angular motion to this motion: {@code this -= motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to subtract.
	 */
	@Pure
	@XtextOperator("-=")
	public void operator_remove(AgentMotion motion) {
		assert motion != null : AssertMessages.notNullParameter();
		this.linear.sub(motion.getLinear());
		this.angular -= motion.getAngular();
	}

	/** Subtract the given angular motion to this motion: {@code this -= motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to subtract.
	 */
	@Pure
	@XtextOperator("-=")
	public void operator_remove(double motion) {
		this.angular -= motion;
	}

	/** Subtract the given linear motion to this motion: {@code this -= motion}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param motion the motion to subtract.
	 */
	@Pure
	@XtextOperator("-=")
	public void operator_remove(Vector2D<?, ?> motion) {
		assert motion != null : AssertMessages.notNullParameter();
		this.linear.sub(motion);
	}

	/** Set the linear and angular motion by copying the motion from the given object.
	 *
	 * @param motionToCopy the motion to copy.
	 */
	public void set(AgentMotion motionToCopy) {
		assert motionToCopy != null : AssertMessages.notNullParameter();
		this.linear.set(motionToCopy.getLinear());
		this.angular = motionToCopy.getAngular();
	}

	/** Set the angular motion by copying the motion from the given object.
	 *
	 * @param motionToCopy the object to copy.
	 */
	public void setAngular(AgentMotion motionToCopy) {
		assert motionToCopy != null : AssertMessages.notNullParameter();
		this.angular = motionToCopy.getAngular();
	}

	/** Set the angular motion.
	 *
	 * @param angular the rotation.
	 */
	public void setAngular(double angular) {
		this.angular = angular;
	}

	/** Set the linear motion by copying the motion from the given object.
	 *
	 * @param motionToCopy the object to copy.
	 */
	public void setLinear(AgentMotion motionToCopy) {
		assert motionToCopy != null : AssertMessages.notNullParameter();
		this.linear.set(motionToCopy.getLinear());
	}

	/** Set the linear motion.
	 *
	 * @param dx the motion along the x axis.
	 * @param dy the motion along the y axis.
	 */
	public void setLinear(double dx, double dy) {
		this.linear.set(dx, dy);
	}

	/** Set the linear motion.
	 *
	 * @param linear the motion vector.
	 */
	public void setLinear(Vector2D<?, ?> linear) {
		assert linear != null : AssertMessages.notNullParameter();
		this.linear.set(linear);
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

}
