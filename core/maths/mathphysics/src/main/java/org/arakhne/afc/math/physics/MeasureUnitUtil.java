/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.physics;

import java.util.concurrent.TimeUnit;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;

/**
 * This class permits to manipulate measure units.
 *
 * <p>A foot is a unit of length, in a number of different systems,
 * including English units, Imperial units, and United States
 * customary units. Its size can vary from system to system,
 * but in each is around a quarter to a third of a meter.
 * The most commonly used foot today is the international foot.
 * There are 3 feet in a yard and 12 inches in a foot.
 *
 * <p>An inch is the name of a unit of length in a number of
 * different systems, including English units, Imperial units,
 * and United States customary units. Its size can vary from system
 * to system. There are 36 inches in a yard and 12 inches in a foot.
 *
 * <p>The fathoms is the name of a unit of length,
 * in a number of different systems, including
 * English units, Imperial units, and United
 * States customary units. The name derives from
 * the Old English word f&aelig;thm (plural) meaning
 * 'outstretched arms' which was the original
 * definition of the unit's measure.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:methodcount", "checkstyle:parametername"})
public final class MeasureUnitUtil {

	private MeasureUnitUtil() {
		//
	}

	/** Translate m/s to km/h.
	 *
	 * @param ms the value
	 * @return {@code ms} * 3.6
	 */
	@Pure
	@Inline(value = "($1) * 3.6")
	public static double ms2kmh(double ms) {
		//return ((ms/1000.0)*3600.0);
		return ms * 3.6;
	}

	/** Translate km/h to m/s.
	 *
	 * @param kmh the value
	 * @return {@code kmh} / 3.6
	 */
	@Pure
	@Inline(value = "($1) / 3.6")
	public static double kmh2ms(double kmh) {
		//return ((kmh/3600.0)*1000.0);
		return kmh / 3.6;
	}

	/** Translate meters to kilometers.
	 *
	 * @param m the value
	 * @return {@code m} / 1000
	 */
	@Pure
	@Inline(value = "($1) / 1000.")
	public static double m2km(double m) {
		return m / 1000.;
	}

	/** Translate kilometers to meters.
	 *
	 * @param km the value
	 * @return {@code km} * 1000
	 */
	@Pure
	@Inline(value = "($1) * 1000.")
	public static double km2m(double km) {
		return km * 1000.;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 *
	 * @param pixelCoord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "(int) $2.round($1)", imported = {Math.class})
	public static int pix2pix(double pixelCoord) {
		return (int) Math.round(pixelCoord);
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 *
	 * @param pixelCoord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "(int) ($1)")
	public static int pix2pix(long pixelCoord) {
		return (int) pixelCoord;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 *
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "$2.round($1)", imported = {Math.class})
	public static int pix2pix(float pixel_coord) {
		return Math.round(pixel_coord);
	}

	/** Translate from unit (10^0) to nano (10^-9).
	 *
	 * @param unit the value
	 * @return {@code unit} / 1e-9
	 */
	@Pure
	@Inline(value = "($1) / 1e-9")
	public static double unit2nano(double unit) {
		return unit / 1e-9;
	}

	/** Translate from nano (10^-9) to unit (10^0).
	 *
	 * @param nano the value
	 * @return {@code nano} * 1e-9
	 */
	@Pure
	@Inline(value = "($1) * 1e-9")
	public static double nano2unit(double nano) {
		return nano * 1e-9;
	}

	/** Translate from unit (10^0) to micro (10^-6).
	 *
	 * @param unit the value
	 * @return {@code unit} / 1e-6
	 */
	@Pure
	@Inline(value = "($1) / 1e-6")
	public static double unit2micro(double unit) {
		return unit / 1e-6;
	}

	/** Translate from micro (10^-6) to unit (10^0).
	 *
	 * @param micro the value
	 * @return {@code micro} * 1e-6
	 */
	@Pure
	@Inline(value = "($1) * 1e-6")
	public static double micro2unit(double micro) {
		return micro * 1e-6;
	}

	/** Translate from unit (10^0) to milli (10^-3).
	 *
	 * @param unit the value
	 * @return {@code unit} / 1e-3
	 */
	@Pure
	@Inline(value = "($1) / 1e-3")
	public static double unit2milli(double unit) {
		return unit / 1e-3;
	}

	/** Translate from milli (10^-3) to unit (10^0).
	 *
	 * @param milli the value
	 * @return {@code milli} * 1e-3
	 */
	@Pure
	@Inline(value = "($1) * 1e-3")
	public static double milli2unit(double milli) {
		return milli * 1e-3;
	}

	/** Translate from milli (10^-3) to micro (10^-6).
	 *
	 * @param milli the value
	 * @return {@code milli} / 1e-3
	 */
	@Pure
	@Inline(value = "($1) / 1e-3")
	public static double milli2micro(double milli) {
		return milli / 1e-3;
	}

	/** Translate from milli (10^-3) to nano (10^-9).
	 *
	 * @param milli the value
	 * @return {@code milli} / 1e-6
	 */
	@Pure
	@Inline(value = "($1) / 1e-6")
	public static double milli2nano(double milli) {
		return milli / 1e-6;
	}

	/** Translate from micro (10^-6) to nano (10^-9).
	 *
	 * @param milli the value
	 * @return {@code milli} / 1e-3
	 */
	@Pure
	@Inline(value = "($1) / 1e-3")
	public static double micro2nano(double milli) {
		return milli / 1e-3;
	}

	/** Translate from micro (10^-6) to milli (10^-3).
	 *
	 * @param micro the value
	 * @return {@code micro} * 1e-3
	 */
	@Pure
	@Inline(value = "($1) * 1e-3")
	public static double micro2milli(double micro) {
		return micro * 1e-3;
	}

	/** Translate from nano (10^-9) to micro (10^-6).
	 *
	 * @param nano the value
	 * @return {@code nano} * 1e-3
	 */
	@Pure
	@Inline(value = "($1) * 1e-3")
	public static double nano2micro(double nano) {
		return nano * 1e-3;
	}

	/** Translate from nano (10^-9) to milli (10^-3).
	 *
	 * @param nano the value
	 * @return {@code nano} * 1e-6
	 */
	@Pure
	@Inline(value = "($1) * 1e-6")
	public static double nano2milli(double nano) {
		return nano * 1e-6;
	}

	/** Translate meters to fathoms.
	 *
	 * @param m the value
	 * @return {@code m} * 0.5468
	 */
	@Pure
	@Inline(value = "($1) * 0.5468")
	public static double m2fh(double m) {
		return m * 0.5468;
	}

	/** Translate feets to fathoms.
	 *
	 * @param ft the value
	 * @return {@code ft} * 0.1667
	 */
	@Pure
	@Inline(value = "($1) * 0.1667")
	public static double ft2fh(double ft) {
		return ft * 0.1667;
	}

	/** Translate inches to fathoms.
	 *
	 * @param in the value
	 * @return {@code in} / 72
	 */
	@Pure
	@Inline(value = "($1) / 72.")
	public static double in2fh(double in) {
		return in / 72;
	}

	/** Translate meters to feets.
	 *
	 * @param m the value
	 * @return {@code m} * 0.3048
	 */
	@Pure
	@Inline(value = "($1) * 0.3048")
	public static double m2ft(double m) {
		return m * 0.3048;
	}

	/** Translate inches to feets.
	 *
	 * @param in the value
	 * @return {@code in} / 12
	 */
	@Pure
	@Inline(value = "($1 / 12)")
	public static double in2ft(double in) {
		return in / 12;
	}

	/** Translate fathoms to feets.
	 *
	 * @param fh the value
	 * @return {@code fh} / 0.1667
	 */
	@Pure
	@Inline(value = "($1) / 0.1667")
	public static double fh2ft(double fh) {
		return fh / 0.1667;
	}

	/** Translate meters to inches.
	 *
	 * @param m the value
	 * @return {@code m} * 0.025
	 */
	@Pure
	@Inline(value = "($1) * 0.025")
	public static double m2in(double m) {
		return m * 0.025;
	}

	/** Translate feets to inches.
	 *
	 * @param ft the value
	 * @return {@code ft} * 12
	 */
	@Pure
	@Inline(value = "($1) * 12.0")
	public static double ft2in(double ft) {
		return ft * 12;
	}

	/** Replies the metrics from inches.
	 *
	 * @param i the inch value
	 * @return a value in centimeters
	 */
	@Pure
	@Inline(value = "($1) / 0.3937")
	public static double inchToMetric(double i) {
		return i / 0.3937;
	}

	/** Replies the inches from metrics.
	 *
	 * @param m the metric value
	 * @return a value in inches
	 */
	@Pure
	@Inline(value = "($1) * 0.3937")
	public static double metricToInch(double m) {
		return m * 0.3937;
	}

	/** Convert the given value expressed in the given unit to seconds.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @return the result of the convertion.
	 * @throws IllegalArgumentException if the given inputUnit is undetermined.
	 */
	@Pure
	public static double toSeconds(double value, TimeUnit inputUnit) {
		switch (inputUnit) {
		case DAYS:
			return value * 86400.;
		case HOURS:
			return value * 3600.;
		case MINUTES:
			return value * 60.;
		case SECONDS:
			break;
		case MILLISECONDS:
			return milli2unit(value);
		case MICROSECONDS:
			return micro2unit(value);
		case NANOSECONDS:
			return nano2unit(value);
		default:
			throw new IllegalArgumentException();
		}
		return value;
	}

	/** Convert the given value expressed in the given unit to meters.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @return the result of the convertion.
	 * @throws IllegalArgumentException if the given inputUnit is undetermined.
	 */
	@Pure
	@SuppressWarnings("checkstyle:returncount")
	public static double toMeters(double value, SpaceUnit inputUnit) {
		switch (inputUnit) {
		case TERAMETER:
			return value * 1e12;
		case GIGAMETER:
			return value * 1e9;
		case MEGAMETER:
			return value * 1e6;
		case KILOMETER:
			return value * 1e3;
		case HECTOMETER:
			return value * 1e2;
		case DECAMETER:
			return value * 1e1;
		case METER:
			break;
		case DECIMETER:
			return value * 1e-1;
		case CENTIMETER:
			return value * 1e-2;
		case MILLIMETER:
			return value * 1e-3;
		case MICROMETER:
			return value * 1e-6;
		case NANOMETER:
			return value * 1e-9;
		case PICOMETER:
			return value * 1e-12;
		case FEMTOMETER:
			return value * 1e-15;
		default:
			throw new IllegalArgumentException();
		}
		return value;
	}

	/** Convert the given value expressed in meters to the given unit.
	 *
	 * @param value is the value to convert
	 * @param outputUnit is the unit of the replied value.
	 * @return the result of the convertion.
	 * @throws IllegalArgumentException if the given outputUnit is undetermined.
	 */
	@Pure
	@SuppressWarnings("checkstyle:returncount")
	public static double fromMeters(double value, SpaceUnit outputUnit) {
		switch (outputUnit) {
		case TERAMETER:
			return value * 1e-12;
		case GIGAMETER:
			return value * 1e-9;
		case MEGAMETER:
			return value * 1e-6;
		case KILOMETER:
			return value * 1e-3;
		case HECTOMETER:
			return value * 1e-2;
		case DECAMETER:
			return value * 1e-1;
		case METER:
			break;
		case DECIMETER:
			return value * 1e1;
		case CENTIMETER:
			return value * 1e2;
		case MILLIMETER:
			return value * 1e3;
		case MICROMETER:
			return value * 1e6;
		case NANOMETER:
			return value * 1e9;
		case PICOMETER:
			return value * 1e12;
		case FEMTOMETER:
			return value * 1e15;
		default:
			throw new IllegalArgumentException();
		}
		return value;
	}

	/** Convert the given value expressed in seconds to the given unit.
	 *
	 * @param value is the value to convert
	 * @param outputUnit is the unit of result.
	 * @return the result of the convertion.
	 * @throws IllegalArgumentException if the given outputUnit is undetermined.
	 */
	@Pure
	public static double fromSeconds(double value, TimeUnit outputUnit) {
		switch (outputUnit) {
		case DAYS:
			return value / 86400.;
		case HOURS:
			return value / 3600.;
		case MINUTES:
			return value / 60.;
		case SECONDS:
			break;
		case MILLISECONDS:
			return unit2milli(value);
		case MICROSECONDS:
			return unit2micro(value);
		case NANOSECONDS:
			return unit2nano(value);
		default:
			throw new IllegalArgumentException();
		}
		return value;
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "$4.fromSeconds(MeasureUnitUtil.toSeconds(($1), ($2)), ($3))",
	        imported = {MeasureUnitUtil.class})
	public static double convert(long value, TimeUnit inputUnit, TimeUnit outputUnit) {
		final double v = toSeconds(value, inputUnit);
		return fromSeconds(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "$4.fromSeconds(MeasureUnitUtil.toSeconds(($1), ($2)), ($3))",
	        imported = {MeasureUnitUtil.class})
	public static double convert(double value, TimeUnit inputUnit, TimeUnit outputUnit) {
		final double v = toSeconds(value, inputUnit);
		return fromSeconds(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "$4.fromMetersPerSecond(MeasureUnitUtil.toMetersPerSecond(($1), ($2)), ($3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, SpeedUnit inputUnit, SpeedUnit outputUnit) {
		final double v = toMetersPerSecond(value, inputUnit);
		return fromMetersPerSecond(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "$4.fromRadiansPerSecond(MeasureUnitUtil.toRadiansPerSecond(($1), ($2)), ($3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, AngularUnit inputUnit, AngularUnit outputUnit) {
		final double v = toRadiansPerSecond(value, inputUnit);
		return fromRadiansPerSecond(v, outputUnit);
	}


	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "$4.fromMeters(MeasureUnitUtil.toMeters(($1), ($2)), ($3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, SpaceUnit inputUnit, SpaceUnit outputUnit) {
		final double v = toMeters(value, inputUnit);
		return fromMeters(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to meters per second.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toMetersPerSecond(double value, SpeedUnit inputUnit) {
		switch (inputUnit) {
		case KILOMETERS_PER_HOUR:
			return 3.6 * value;
		case MILLIMETERS_PER_SECOND:
			return value / 1000.;
		case METERS_PER_SECOND:
		default:
		}
		return value;
	}

	/** Convert the given value expressed in the given unit to radians per second.
	 *
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the {@code value}
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toRadiansPerSecond(double value, AngularUnit inputUnit) {
		switch (inputUnit) {
		case TURNS_PER_SECOND:
			return value * (2. * MathConstants.PI);
		case DEGREES_PER_SECOND:
			return Math.toRadians(value);
		case RADIANS_PER_SECOND:
		default:
		}
		return value;
	}

	/** Convert the given value expressed in meters per second to the given unit.
	 *
	 * @param value is the value to convert
	 * @param outputUnit is the unit of result.
	 * @return the result of the convertion.
	 */
	@Pure
	public static double fromMetersPerSecond(double value, SpeedUnit outputUnit) {
		switch (outputUnit) {
		case KILOMETERS_PER_HOUR:
			return value / 3.6;
		case MILLIMETERS_PER_SECOND:
			return value * 1000.;
		case METERS_PER_SECOND:
		default:
		}
		return value;
	}

	/** Convert the given value expressed in radians per second to the given unit.
	 *
	 * @param value is the value to convert
	 * @param outputUnit is the unit of result.
	 * @return the result of the convertion.
	 */
	@Pure
	public static double fromRadiansPerSecond(double value, AngularUnit outputUnit) {
		switch (outputUnit) {
		case TURNS_PER_SECOND:
			return value / (2. * MathConstants.PI);
		case DEGREES_PER_SECOND:
			return Math.toDegrees(value);
		case RADIANS_PER_SECOND:
		default:
		}
		return value;
	}

	/** Compute the smallest unit that permits to have
	 * a metric value with its integer part positive.
	 *
	 * @param amount is the amount expressed in the given unit.
	 * @param unit is the unit of the given amount.
	 * @return the smallest unit that permits to obtain the smallest
	 *     positive mathematical integer that corresponds to the integer
	 *     part of the given amount after its convertion to the selected
	 *     unit.
	 */
	@Pure
	public static SpaceUnit getSmallestUnit(double amount, SpaceUnit unit) {
		final double meters = toMeters(amount, unit);
		double v;
		final SpaceUnit[] units = SpaceUnit.values();
		SpaceUnit u;
		for (int i = units.length - 1; i >= 0; --i) {
			u = units[i];
			v = Math.floor(fromMeters(meters, u));
			if (v > 0.) {
				return u;
			}
		}
		return unit;
	}

}
