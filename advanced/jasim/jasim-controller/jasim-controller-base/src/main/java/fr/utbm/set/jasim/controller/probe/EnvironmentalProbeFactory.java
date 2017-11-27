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
package fr.utbm.set.jasim.controller.probe;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.UUID;

import fr.utbm.set.jasim.controller.SimulationController;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbe;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;

/** This class permits to instance probes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EnvironmentalProbeFactory {

	/**
	 * Try to create an instance of an environmental probe.
	 * <p>
	 * This function creates an probe instance by invocation to one of
	 * the following constructors (in prefered order, where {@code ProbeClass}
	 * is the name of the probe class):
	 * <ul>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SimulationController controller, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SimulationController controller, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SimulationController controller)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SimulationController controller)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SituatedEnvironment environment, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SituatedEnvironment environment, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SituatedEnvironment environment)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SituatedEnvironment environment)}</li>
	 * </ul>
	 * 
	 * @param <P> is the type of the probe to instance
	 * @param probeType is the type of the probe to instance
	 * @param probeId is the identifier of the new probe.
	 * @param placeId is the identifier of the place in which the probe must be put.
	 * @param controller is the simulation controller.
	 * @param parameters is the parameters to pass to the probe.
	 * @return a probe, never <code>null</code>.
	 * @throws IllegalArgumentException if the probe could not be constructed.
	 */
	public static <P extends EnvironmentalProbe> P newProbe(Class<P> probeType,
			String probeId, UUID placeId,
			SimulationController<?,?,?> controller,
			Serializable... parameters) {
		Constructor<P> cons;
		try {
			cons = probeType.getConstructor(String.class, UUID.class, SimulationController.class, Serializable[].class);
			return cons.newInstance(probeId, placeId, controller, parameters);
		}
		catch(Throwable _) {
			//
		}
		
		try {
			cons = probeType.getConstructor(String.class, String.class, SimulationController.class, Serializable[].class);
			return cons.newInstance(probeId, placeId.toString(), controller, parameters);
		}
		catch(Throwable _) {
			//
		}

		try {
			cons = probeType.getConstructor(String.class, UUID.class, SimulationController.class);
			return cons.newInstance(probeId, placeId, controller);
		}
		catch(Throwable _) {
			//
		}
		
		try {
			cons = probeType.getConstructor(String.class, String.class, SimulationController.class);
			return cons.newInstance(probeId, placeId.toString(), controller);
		}
		catch(Throwable _) {
			//
		}

		try {
			cons = probeType.getConstructor(String.class, UUID.class, SituatedEnvironment.class, Serializable[].class);
			return cons.newInstance(probeId, placeId, controller.getEnvironment(), parameters);
		}
		catch(Throwable _) {
			//
		}

		try {
			cons = probeType.getConstructor(String.class, String.class, SituatedEnvironment.class, Serializable[].class);
			return cons.newInstance(probeId, placeId.toString(), controller.getEnvironment(), parameters);
		}
		catch(Throwable _) {
			//
		}

		try {
			cons = probeType.getConstructor(String.class, UUID.class, SituatedEnvironment.class);
			return cons.newInstance(probeId, placeId, controller.getEnvironment());
		}
		catch(Throwable e) {
			//
		}

		try {
			cons = probeType.getConstructor(String.class, String.class, SituatedEnvironment.class);
			return cons.newInstance(probeId, placeId.toString(), controller.getEnvironment());
		}
		catch(Throwable e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Try to create an instance of an environmental probe.
	 * <p>
	 * This function creates an probe instance by invocation to one of
	 * the following constructors (in prefered order, where {@code ProbeClass}
	 * is the name of the probe class):
	 * <ul>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SimulationController controller, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SimulationController controller, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SimulationController controller)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SimulationController controller)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SituatedEnvironment environment, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SituatedEnvironment environment, Serializable... parameters)}</li>
	 * <li><{@code ProbeClass(String probeId, UUID placeId, SituatedEnvironment environment)}</li>
	 * <li><{@code ProbeClass(String probeId, String placeId, SituatedEnvironment environment)}</li>
	 * </ul>
	 * 
	 * @param probeType is the type of the probe to instance
	 * @param probeId is the identifier of the new probe.
	 * @param placeId is the identifier of the place in which the probe must be put.
	 * @param controller is the simulation controller.
	 * @param parameters is the parameters to pass to the probe.
	 * @return a probe, never <code>null</code>.
	 * @throws IllegalArgumentException if the probe could not be constructed.
	 * @throws ClassNotFoundException if the given type is unknown.
	 */
	@SuppressWarnings("unchecked")
	public static EnvironmentalProbe newProbe(String probeType,
			String probeId, UUID placeId,
			SimulationController<?,?,?> controller,
			Serializable... parameters) throws ClassNotFoundException {
		Class<?> probeClass = Class.forName(probeType);
		if (EnvironmentalProbe.class.isAssignableFrom(probeClass)) {
			Class<? extends EnvironmentalProbe> type = (Class<? extends EnvironmentalProbe>)probeClass;
			return newProbe(type, probeId, placeId, controller, parameters);
		}
		throw new IllegalArgumentException("cannot cast to EnvironmentalProbe"); //$NON-NLS-1$
	}

}