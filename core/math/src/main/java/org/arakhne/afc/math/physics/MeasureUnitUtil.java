/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math.physics;

import java.util.concurrent.TimeUnit;

import org.arakhne.afc.math.MathConstants;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class permits to manipulate measure units.
 * <p>
 * A foot is a unit of length, in a number of different systems, 
 * including English units, Imperial units, and United States
 * customary units. Its size can vary from system to system,
 * but in each is around a quarter to a third of a meter.
 * The most commonly used foot today is the international foot.
 * There are 3 feet in a yard and 12 inches in a foot.
 * <p>
 * An inch is the name of a unit of length in a number of
 * different systems, including English units, Imperial units,
 * and United States customary units. Its size can vary from system
 * to system. There are 36 inches in a yard and 12 inches in a foot.
 * <p>
 * The fathoms is the name of a unit of length,
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
public class MeasureUnitUtil {

	/** Translate m/s to km/h.
	 * 
	 * @param ms
	 * @return <var>ms</var> * 3.6
	 */
	@Pure
	@Inline(value = "($1 * 3.6)")
	public static double ms2kmh(double ms) {
		//return ((ms/1000.0)*3600.0);
		return ms*3.6;
	}

	/** Translate km/h to m/s.
	 * 
	 * @param kmh
	 * @return <var>kmh</var> / 3.6
	 */
	@Pure
	@Inline(value = "($1 / 3.6)")
	public static double kmh2ms(double kmh) {
		//return ((kmh/3600.0)*1000.0);
		return kmh / 3.6;
	}

	/** Translate meters to kilometers.
	 * 
	 * @param m
	 * @return <var>m</var> / 1000
	 */
	@Pure
	@Inline(value = "($1 / 1000.0)")
	public static double m2km(double m) {
		return m / 1000.;
	}
	
	/** Translate kilometers to meters.
	 * 
	 * @param km
	 * @return <var>km</var> * 1000
	 */
	@Pure
	@Inline(value = "($1 * 1000)")
	public static double km2m(double km) {
		return km * 1000.;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "((int) Math.round($1))", imported = {Math.class})
	public static int pix2pix(double pixel_coord) {
		return (int)Math.round(pixel_coord);
	}
	
	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "((int) ($1))")
	public static int pix2pix(long pixel_coord) {
		return (int)pixel_coord;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	@Pure
	@Inline(value = "(Math.round($1))", imported = {Math.class})
	public static int pix2pix(float pixel_coord) {
		return Math.round(pixel_coord);
	}

	/** Translate from unit (10^0) to nano (10^-9).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-9
	 */
	@Pure
	@Inline(value = "($1 / 1e-9)")
	public static double unit2nano(double unit) {
		return unit / 1e-9;
	}
	
	/** Translate from nano (10^-9) to unit (10^0).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-9
	 */
	@Pure
	@Inline(value = "($1 * 1e-9)")
	public static double nano2unit(double nano) {
		return nano * 1e-9;
	}

	/** Translate from unit (10^0) to micro (10^-6).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-6
	 */
	@Pure
	@Inline(value = "($1 / 1e-6)")
	public static double unit2micro(double unit) {
		return unit / 1e-6;
	}
	
	/** Translate from micro (10^-6) to unit (10^0).
	 * 
	 * @param micro
	 * @return <var>micro</var> * 1e-6
	 */
	@Pure
	@Inline(value = "($1 * 1e-6)")
	public static double micro2unit(double micro) {
		return micro * 1e-6;
	}

	/** Translate from unit (10^0) to milli (10^-3).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-3
	 */
	@Pure
	@Inline(value = "($1 / 1e-3)")
	public static double unit2milli(double unit) {
		return unit / 1e-3;
	}
	
	/** Translate from milli (10^-3) to unit (10^0).
	 * 
	 * @param milli
	 * @return <var>milli</var> * 1e-3
	 */
	@Pure
	@Inline(value = "($1 * 1e-3)")
	public static double milli2unit(double milli) {
		return milli * 1e-3;
	}
	
	/** Translate from milli (10^-3) to micro (10^-6).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-3
	 */
	@Pure
	@Inline(value = "($1 / 1e-3)")
	public static double milli2micro(double milli) {
		return milli / 1e-3;
	}

	/** Translate from milli (10^-3) to nano (10^-9).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-6
	 */
	@Pure
	@Inline(value = "($1 / 1e-6)")
	public static double milli2nano(double milli) {
		return milli / 1e-6;
	}

	/** Translate from micro (10^-6) to nano (10^-9).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-3
	 */
	@Pure
	@Inline(value = "($1 / 1e-3)")
	public static double micro2nano(double milli) {
		return milli / 1e-3;
	}

	/** Translate from micro (10^-6) to milli (10^-3).
	 * 
	 * @param micro
	 * @return <var>micro</var> * 1e-3
	 */
	@Pure
	@Inline(value = "($1 * 1e-3)")
	public static double micro2milli(double micro) {
		return micro * 1e-3;
	}

	/** Translate from nano (10^-9) to micro (10^-6).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-3
	 */
	@Pure
	@Inline(value = "($1 * 1e-3)")
	public static double nano2micro(double nano) {
		return nano * 1e-3;
	}

	/** Translate from nano (10^-9) to milli (10^-3).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-6
	 */
	@Pure
	@Inline(value = "($1 * 1e-6)")
	public static double nano2milli(double nano) {
		return nano * 1e-6;
	}
	
	/** Translate meters to fathoms.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.5468
	 */
	@Pure
	@Inline(value = "($1 * 0.5468)")
	public static double m2fh(double m) {
		return m * 0.5468;
	}

	/** Translate feets to fathoms.
	 * 
	 * @param ft
	 * @return <var>ft</var> * 0.1667
	 */
	@Pure
	@Inline(value = "($1 * 0.1667)")
	public static double ft2fh(double ft) {
		return ft * 0.1667;
	}

	/** Translate inches to fathoms.
	 * 
	 * @param in
	 * @return <var>in</var> / 72
	 */
	@Pure
	@Inline(value = "($1 / 72)")
	public static double in2fh(double in) {
		return in / 72;
	}

	/** Translate meters to feets.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.3048
	 */
	@Pure
	@Inline(value = "($1 * 0.3048)")
	public static double m2ft(double m) {
		return m * 0.3048;
	}

	/** Translate inches to feets.
	 * 
	 * @param in
	 * @return <var>in</var> / 12
	 */
	@Pure
	@Inline(value = "($1 / 12)")
	public static double in2ft(double in) {
		return in / 12;
	}

	/** Translate fathoms to feets.
	 * 
	 * @param fh
	 * @return <var>fh</var> / 0.1667
	 */
	@Pure
	@Inline(value = "($1 / 0.1667)")
	public static double fh2ft(double fh) {
		return fh / 0.1667;
	}
	
	/** Translate meters to inches.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.025
	 */
	@Pure
	@Inline(value = "($1 * 0.025)")
	public static double m2in(double m) {
		return m * 0.025;
	}
	
	/** Translate feets to inches.
	 * 
	 * @param ft
	 * @return <var>ft</var> * 12
	 */
	@Pure
	@Inline(value = "($1 * 12)")
	public static double ft2in(double ft) {
		return ft * 12;
	}
	
	/** Replies the metrics from inches.
	 *
	 * @param i the inch value
	 * @return a value in centimeters
	 */
	@Pure
	@Inline(value = "($1 / 0.3937)")
	public static double inchToMetric( double i ) {
		return i / 0.3937;
	}

	/** Replies the inches from metrics.
	 *
	 * @param m the metric value
	 * @return a value in inches
	 */
	@Pure
	@Inline(value = "($1 * 0.3937)")
	public static double metricToInch( double m ) {
		return m * 0.3937;
	}

	/** Convert the given value expressed in the given unit to seconds.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toSeconds(double value, TimeUnit inputUnit) {
		switch(inputUnit) {
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
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toMeters(double value, SpaceUnit inputUnit) {
		switch(inputUnit) {
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
	 */
	@Pure
	public static double fromMeters(double value, SpaceUnit outputUnit) {
		switch(outputUnit) {
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
	 */
	@Pure
	public static double fromSeconds(double value, TimeUnit outputUnit) {
		switch(outputUnit) {
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
	 * @param inputUnit is the unit of the <var>value</var>
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "(MeasureUnitUtil.fromSeconds(MeasureUnitUtil.toSeconds($1, $2), $3))", imported = {MeasureUnitUtil.class})
	public static double convert(long value, TimeUnit inputUnit, TimeUnit outputUnit) {
		double v = toSeconds(value, inputUnit);
		return fromSeconds(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "(MeasureUnitUtil.fromSeconds(MeasureUnitUtil.toSeconds($1, $2), $3))", imported = {MeasureUnitUtil.class})
	public static double convert(double value, TimeUnit inputUnit, TimeUnit outputUnit) {
		double v = toSeconds(value, inputUnit);
		return fromSeconds(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "(MeasureUnitUtil.fromMetersPerSecond(MeasureUnitUtil.toMetersPerSecond($1, $2), $3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, SpeedUnit inputUnit, SpeedUnit outputUnit) {
		double v = toMetersPerSecond(value, inputUnit);
		return fromMetersPerSecond(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "(MeasureUnitUtil.fromRadiansPerSecond(MeasureUnitUtil.toRadiansPerSecond($1, $2), $3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, AngularUnit inputUnit, AngularUnit outputUnit) {
		double v = toRadiansPerSecond(value, inputUnit);
		return fromRadiansPerSecond(v, outputUnit);
	}


	/** Convert the given value expressed in the given unit to the
	 * second given unit.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @param outputUnit is the unit for the replied value.
	 * @return the result of the convertion.
	 */
	@Pure
	@Inline(value = "(MeasureUnitUtil.fromMeters(MeasureUnitUtil.toMeters($1, $2), $3))",
			imported = {MeasureUnitUtil.class})
	public static double convert(double value, SpaceUnit inputUnit, SpaceUnit outputUnit) {
		double v = toMeters(value, inputUnit);
		return fromMeters(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to meters per second.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toMetersPerSecond(double value, SpeedUnit inputUnit) {
		switch(inputUnit) {
		case KILOMETERS_PER_HOUR:
			return 3.6 * value;
		case MILLIMETERS_PER_SECOND:
			return value / 1000.;
		default:
		}
		return value;
	}

	/** Convert the given value expressed in the given unit to radians per second.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	@Pure
	public static double toRadiansPerSecond(double value, AngularUnit inputUnit) {
		switch(inputUnit) {
		case TURNS_PER_SECOND:
			return value * (2.*MathConstants.PI);
		case DEGREES_PER_SECOND:
			return Math.toRadians(value);
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
		switch(outputUnit) {
		case KILOMETERS_PER_HOUR:
			return value / 3.6;
		case MILLIMETERS_PER_SECOND:
			return value * 1000.;
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
		switch(outputUnit) {
		case TURNS_PER_SECOND:
			return value / (2.*MathConstants.PI);
		case DEGREES_PER_SECOND:
			return Math.toDegrees(value);
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
	 * positive mathematical integer that corresponds to the integer
	 * part of the given amount after its convertion to the selected
	 * unit.
	 */
	@Pure
	public static SpaceUnit getSmallestUnit(double amount, SpaceUnit unit) {
		double meters = toMeters(amount, unit);
		double v;
		SpaceUnit[] units = SpaceUnit.values();
		SpaceUnit u;
		for(int i=units.length-1; i>=0; --i) {
			u = units[i];
			v = Math.floor(fromMeters(meters, u));
			if (v>0.) return u;
		}
		return unit;
	}
	
}