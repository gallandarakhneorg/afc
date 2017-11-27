/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.jasim.environment;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.physics.AngularUnit;
import org.arakhne.afc.math.physics.SpaceUnit;
import org.arakhne.afc.math.physics.SpeedUnit;

/**
 * Constants for a Jasim simulation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class JasimConstants {

	/**
	 * Default view vector.
	 */
	public static final double DEFAULT_VIEW_VECTOR_X = CoordinateSystem3D.getDefaultCoordinateSystem().getViewVector().x;

	/**
	 * Default view vector.
	 */
	public static final double DEFAULT_VIEW_VECTOR_Y = CoordinateSystem3D.getDefaultCoordinateSystem().getViewVector().y;

	/**
	 * Default view vector.
	 */
	public static final double DEFAULT_VIEW_VECTOR_Z = CoordinateSystem3D.getDefaultCoordinateSystem().getViewVector().z;

	/**
	 * Default up vector.
	 */
	public static final double DEFAULT_UP_VECTOR_X = CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector().x;

	/**
	 * Default up vector.
	 */
	public static final double DEFAULT_UP_VECTOR_Y = CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector().y;

	/**
	 * Default up vector.
	 */
	public static final double DEFAULT_UP_VECTOR_Z = CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector().z;

	/**
	 * Default left vector.
	 */
	public static final double DEFAULT_LEFT_VECTOR_X = CoordinateSystem3D.getDefaultCoordinateSystem().getLeftVector().x;

	/**
	 * Default left vector.
	 */
	public static final double DEFAULT_LEFT_VECTOR_Y = CoordinateSystem3D.getDefaultCoordinateSystem().getLeftVector().y;

	/**
	 * Default left vector.
	 */
	public static final double DEFAULT_LEFT_VECTOR_Z = CoordinateSystem3D.getDefaultCoordinateSystem().getLeftVector().z;

	/** Default space unit.
	 */
	public static final SpaceUnit DEFAULT_SPACE_UNIT = SpaceUnit.METER;

	/** Default speed unit.
	 */
	public static final SpeedUnit DEFAULT_SPEED_UNIT = SpeedUnit.METERS_PER_SECOND;

	/** Default rotation unit.
	 */
	public static final AngularUnit DEFAULT_ROTATION_UNIT = AngularUnit.RADIANS_PER_SECOND;

	/** Default heights for human pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_HEIGHT = 1.8;

	/** Default distance between the ground and the eye for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_EYE_POSITION = 1.6;

	/** Default lateral size for human pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_SIZE = .5;

	/** Default near perception distance for pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_NEAR_PERCEPTION_DISTANCE = 2.;

	/** Default far perception distance for pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_FAR_PERCEPTION_DISTANCE = 50.;

	/** Default horizontal angle of perception for pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_HORIZONTAL_PERCEPTION_ANGLE = Math.PI / 4.;

	/** Default vertical angle of perception for pedestrians.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_VERTICAL_PERCEPTION_ANGLE = Math.PI / 4.;

	/** Default maximal linear forward speed for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 *
	 * <p>Walking speeds can vary greatly depending on factors such as height,
	 * weight, age, terrain, surface, load, culture, effort, and fitness,
	 * the average human walking speed is about 3 miles per hour.
	 * Specific studies have found pedestrian walking speeds ranging from
	 * 4.11 to 4.33 feet per second (2.8 mph ~ 2.95 mph | 4.51 km/h ~ 4.75 km/h)
	 * for older individuals to 4.85 to 4.95 fps (3.3 mph ~ 3.38 mph |
	 * 5.32 km/h ~ 5.43 km/h) for younger individuals.<br>
	 * Source 1: "Study Compares Older and Younger Pedestrian Walking Speeds".
	 * TranSafety, Inc. 1997-10-01. Retrieved
	 * <a href="http://www.usroads.com/journals/p/rej/9710/re971001.htm">2009-08-24</a>.<br>
	 * Source 2: Aspelin, Karen (2005-05-25). "Establishing Pedestrian Walking
	 * Speeds". Portland State University. Retrieved
	 * <a href="http://www.westernite.org/datacollectionfund/2005/psu_ped_summary.pdf">2009-08-24</a>.
	 *
	 * <p>Average adult walking speed on level surfaces is approximately 80 m/min
	 * (4.8 km/h, 12.5 min/km), or about 3 miles per hour. For men, it is
	 * about 82 m/min, and for women, about 79 m/min.<br>
	 * Source: Burnfield, JM, and Powers, CM. Normal and Pathologic Gait, in
	 * Orthopaedic Physical Therapy Secrets edited by Jeffrey D. Placzek
	 * and David A. Boyce, Hanley & Belfus; 2 edition (June 6, 2006),
	 * chap. 16 [ISBN 1560537086; ISBN 978-1560537083]
	 *
	 * <p>Assuming average speed: 5.09 km/h = 5090 m/h = 1.4138 m/s.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_FORWARD_SPEED = 1.4138;

	/** Default maximal linear backward speed for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 *
	 * <p>Walking speeds can vary greatly depending on factors such as height,
	 * weight, age, terrain, surface, load, culture, effort, and fitness,
	 * the average human walking speed is about 3 miles per hour.
	 * Specific studies have found pedestrian walking speeds ranging from
	 * 4.11 to 4.33 feet per second (2.8 mph ~ 2.95 mph | 4.51 km/h ~ 4.75 km/h)
	 * for older individuals to 4.85 to 4.95 fps (3.3 mph ~ 3.38 mph |
	 * 5.32 km/h ~ 5.43 km/h) for younger individuals.<br>
	 * Source 1: "Study Compares Older and Younger Pedestrian Walking Speeds".
	 * TranSafety, Inc. 1997-10-01. Retrieved
	 * <a href="http://www.usroads.com/journals/p/rej/9710/re971001.htm">2009-08-24</a>.<br>
	 * Source 2: Aspelin, Karen (2005-05-25). "Establishing Pedestrian Walking
	 * Speeds". Portland State University. Retrieved
	 * <a href="http://www.westernite.org/datacollectionfund/2005/psu_ped_summary.pdf">2009-08-24</a>.
	 *
	 * <p>Average adult walking speed on level surfaces is approximately 80 m/min
	 * (4.8 km/h, 12.5 min/km), or about 3 miles per hour. For men, it is
	 * about 82 m/min, and for women, about 79 m/min.<br>
	 * Source: Burnfield, JM, and Powers, CM. Normal and Pathologic Gait, in
	 * Orthopaedic Physical Therapy Secrets edited by Jeffrey D. Placzek
	 * and David A. Boyce, Hanley & Belfus; 2 edition (June 6, 2006),
	 * chap. 16 [ISBN 1560537086; ISBN 978-1560537083]
	 *
	 * <p>Assuming average speed: 5.09 km/h = 5090 m/h = 1.4138 m/s.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_BACKWARD_SPEED = 1.3194;

	/** Default maximal angular speed for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_ANGULAR_SPEED = Math.PI;

	/** Default maximal linear acceleration for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_LINEAR_ACCELERATION = 1.4138;

	/** Default maximal linear deceleration for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_LINEAR_DECELERATION = 1.4138;

	/** Default maximal angular acceleration for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_ANGULAR_ACCELERATION = Math.PI;

	/** Default maximal angular deceleration for a pedestrian.
	 * A pedestrian  is a person who is walking on a road, sidewalk  or path.
	 */
	public static final double DEFAULT_PEDESTRIAN_MAX_ANGULAR_DECELERATION = Math.PI;

	/**
	 * Default length of a vehicle in meters, from back to front.
	 * Dimension of a Peugeot 407 Berline.
	 * <img src="./doc-files/defaultVehicleSizes.png">
	 */
	public static final double DEFAULT_VEHICLE_LENGTH = 4.691;

	/**
	 * Default width of a vehicle in meters, from left to right.
	 * Dimension of a Peugeot 407 Berline without external side-mirrors.
	 * <img src="./doc-files/defaultVehicleSizes.png">
	 */
	public static final double DEFAULT_VEHICLE_WIDTH = 1.811;

	/**
	 * Default width of a vehicle in meters, from road to roof.
	 * Dimension of a Peugeot 407 Berline.
	 * <img src="./doc-files/defaultVehicleSizes.png">
	 */
	public static final double DEFAULT_VEHICLE_HEIGHT = 1.455;

	/** Default maximal linear forward speed for a vehicle.
	 * Specification of a Peugeot 407 1.6 HDi.
	 */
	public static final double DEFAULT_VEHICLE_MAX_FORWARD_SPEED = 53;

	/** Default maximal linear backward speed for a vehicle.
	 * Specification of a Peugeot 407 1.6 HDi.
	 */
	public static final double DEFAULT_VEHICLE_MAX_BACKWARD_SPEED = 5;

	/** Default maximal angular speed for a vehicle.
	 */
	public static final double DEFAULT_VEHICLE_MAX_ANGULAR_SPEED = Math.PI;

	/** Default maximal linear acceleration for a vehicle.
	 */
	public static final double DEFAULT_VEHICLE_MAX_LINEAR_ACCELERATION = 2.39;

	/** Default maximal linear deceleration for a vehicle.
	 */
	public static final double DEFAULT_VEHICLE_MAX_LINEAR_DECELERATION = 12;

	/** Default maximal angular acceleration for a vehicle.
	 */
	public static final double DEFAULT_VEHICLE_MAX_ANGULAR_ACCELERATION = Math.PI;

	/** Default maximal angular deceleration for a vehicle.
	 */
	public static final double DEFAULT_VEHICLE_MAX_ANGULAR_DECELERATION = Math.PI;

	/**
	 * Default forward perception distance for a vehicle in meters.
	 */
	public static final double DEFAULT_VEHICLE_FORWARD_PERCEPTION_DISTANCE = 150d;

	/**
	 * Default backward perception distance for a vehicle in meters.
	 */
	public static final double DEFAULT_VEHICLE_BACKWARD_PERCEPTION_DISTANCE = 150d;

	/**
	 * Default reaction time of a vehicle.
	 *
	 * <p>Cf <a href="http://fr.wikipedia.org/wiki/Distance_de_perception-r%C3%A9action">http://fr.wikipedia.org/wiki/Distance_de_perception-r%C3%A9action [fr]</a>
	 */
	public static final double DEFAULT_HUMAN_REACTION_TIME = 2.0d;

	/**
	 * Minimal distance between vehicles.
	 */
	public static final double DEFAULT_MINIMAL_DISTANCE_BETWEEN_VEHICLES = 0.7d;

	/** Default split count in tree nodes.
	 */
	public static final int DEFAULT_TREE_SPLIT_COUNT = 5;

	private JasimConstants() {
		//
	}

}
