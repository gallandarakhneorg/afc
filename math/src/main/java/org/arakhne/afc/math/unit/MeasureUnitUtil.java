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
package org.arakhne.afc.math.unit;

import java.util.concurrent.TimeUnit;

import org.arakhne.afc.math.MathConstants;

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
 */
public class MeasureUnitUtil {

	/** Translate m/s to km/h.
	 * 
	 * @param ms
	 * @return <var>ms</var> * 3.6
	 */
	public static float ms2kmh(float ms) {
		//return ((ms/1000.0)*3600.0);
		return ms*3.6f;
	}

	/** Translate km/h to m/s.
	 * 
	 * @param kmh
	 * @return <var>kmh</var> / 3.6
	 */
	public static float kmh2ms(float kmh) {
		//return ((kmh/3600.0)*1000.0);
		return kmh / 3.6f;
	}

	/** Translate meters to kilometers.
	 * 
	 * @param m
	 * @return <var>m</var> / 1000
	 */
	public static float m2km(float m) {
		return m / 1000.f;
	}
	
	/** Translate kilometers to meters.
	 * 
	 * @param km
	 * @return <var>km</var> * 1000
	 */
	public static float km2m(float km) {
		return km * 1000.f;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	public static int pix2pix(double pixel_coord) {
		return (int)Math.round(pixel_coord);
	}
	
	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	public static int pix2pix(long pixel_coord) {
		return (int)pixel_coord;
	}

	/** Translate from "long" pixel coordinate to "system" pixel coordinate.
	 * 
	 * @param pixel_coord is the pixel coordinate to translate.
	 * @return the given value rounded to the nearest integer.
	 */
	public static int pix2pix(float pixel_coord) {
		return Math.round(pixel_coord);
	}

	/** Translate from unit (10^0) to nano (10^-9).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-9
	 */
	public static float unit2nano(float unit) {
		return unit / 1e-9f;
	}
	
	/** Translate from nano (10^-9) to unit (10^0).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-9
	 */
	public static float nano2unit(float nano) {
		return nano * 1e-9f;
	}

	/** Translate from unit (10^0) to micro (10^-6).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-6
	 */
	public static float unit2micro(float unit) {
		return unit / 1e-6f;
	}
	
	/** Translate from micro (10^-6) to unit (10^0).
	 * 
	 * @param micro
	 * @return <var>micro</var> * 1e-6
	 */
	public static float micro2unit(float micro) {
		return micro * 1e-6f;
	}

	/** Translate from unit (10^0) to milli (10^-3).
	 * 
	 * @param unit
	 * @return <var>unit</var> / 1e-3
	 */
	public static float unit2milli(float unit) {
		return unit / 1e-3f;
	}
	
	/** Translate from milli (10^-3) to unit (10^0).
	 * 
	 * @param milli
	 * @return <var>milli</var> * 1e-3
	 */
	public static float milli2unit(float milli) {
		return milli * 1e-3f;
	}
	
	/** Translate from milli (10^-3) to micro (10^-6).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-3
	 */
	public static float milli2micro(float milli) {
		return milli / 1e-3f;
	}

	/** Translate from milli (10^-3) to nano (10^-9).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-6
	 */
	public static float milli2nano(float milli) {
		return milli / 1e-6f;
	}

	/** Translate from micro (10^-6) to nano (10^-9).
	 * 
	 * @param milli
	 * @return <var>milli</var> / 1e-3
	 */
	public static float micro2nano(float milli) {
		return milli / 1e-3f;
	}

	/** Translate from micro (10^-6) to milli (10^-3).
	 * 
	 * @param micro
	 * @return <var>micro</var> * 1e-3
	 */
	public static float micro2milli(float micro) {
		return micro * 1e-3f;
	}

	/** Translate from nano (10^-9) to micro (10^-6).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-3
	 */
	public static float nano2micro(float nano) {
		return nano * 1e-3f;
	}

	/** Translate from nano (10^-9) to milli (10^-3).
	 * 
	 * @param nano
	 * @return <var>nano</var> * 1e-6
	 */
	public static float nano2milli(float nano) {
		return nano * 1e-6f;
	}
	
	/** Translate meters to fathoms.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.5468
	 */
	public static float m2fh(float m) {
		return m * 0.5468f;
	}

	/** Translate feets to fathoms.
	 * 
	 * @param ft
	 * @return <var>ft</var> * 0.1667
	 */
	public static float ft2fh(float ft) {
		return ft * 0.1667f;
	}

	/** Translate inches to fathoms.
	 * 
	 * @param in
	 * @return <var>in</var> / 72
	 */
	public static float in2fh(float in) {
		return in / 72f;
	}

	/** Translate meters to feets.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.3048
	 */
	public static float m2ft(float m) {
		return m * 0.3048f;
	}

	/** Translate inches to feets.
	 * 
	 * @param in
	 * @return <var>in</var> / 12
	 */
	public static float in2ft(float in) {
		return in / 12f;
	}

	/** Translate fathoms to feets.
	 * 
	 * @param fh
	 * @return <var>fh</var> / 0.1667
	 */
	public static float fh2ft(float fh) {
		return fh / 0.1667f;
	}
	
	/** Translate meters to inches.
	 * 
	 * @param m
	 * @return <var>m</var> * 0.025
	 */
	public static float m2in(float m) {
		return m * 0.025f;
	}
	
	/** Translate feets to inches.
	 * 
	 * @param ft
	 * @return <var>ft</var> * 12
	 */
	public static float ft2in(float ft) {
		return ft * 12f;
	}

	/** Convert the given value expressed in the given unit to seconds.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	public static float toSeconds(float value, TimeUnit inputUnit) {
		switch(inputUnit) {
		case DAYS:
			return value * 86400.f;
		case HOURS:
			return value * 3600.f;
		case MINUTES:
			return value * 60.f;
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
	public static float toMeters(float value, SpaceUnit inputUnit) {
		switch(inputUnit) {
		case TERAMETER:
			return value * 1e12f;
		case GIGAMETER:
			return value * 1e9f;
		case MEGAMETER:
			return value * 1e6f;
		case KILOMETER:
			return value * 1e3f;
		case HECTOMETER:
			return value * 1e2f;
		case DECAMETER:
			return value * 1e1f;
		case METER:
			break;
		case DECIMETER:
			return value * 1e-1f;
		case CENTIMETER:
			return value * 1e-2f;
		case MILLIMETER:
			return value * 1e-3f;
		case MICROMETER:
			return value * 1e-6f;
		case NANOMETER:
			return value * 1e-9f;
		case PICOMETER:
			return value * 1e-12f;
		case FEMTOMETER:
			return value * 1e-15f;
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
	public static float fromMeters(float value, SpaceUnit outputUnit) {
		switch(outputUnit) {
		case TERAMETER:
			return value * 1e-12f;
		case GIGAMETER:
			return value * 1e-9f;
		case MEGAMETER:
			return value * 1e-6f;
		case KILOMETER:
			return value * 1e-3f;
		case HECTOMETER:
			return value * 1e-2f;
		case DECAMETER:
			return value * 1e-1f;
		case METER:
			break;
		case DECIMETER:
			return value * 1e1f;
		case CENTIMETER:
			return value * 1e2f;
		case MILLIMETER:
			return value * 1e3f;
		case MICROMETER:
			return value * 1e6f;
		case NANOMETER:
			return value * 1e9f;
		case PICOMETER:
			return value * 1e12f;
		case FEMTOMETER:
			return value * 1e15f;
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
	public static float fromSeconds(float value, TimeUnit outputUnit) {
		switch(outputUnit) {
		case DAYS:
			return value / 86400.f;
		case HOURS:
			return value / 3600.f;
		case MINUTES:
			return value / 60.f;
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
	public static float convert(long value, TimeUnit inputUnit, TimeUnit outputUnit) {
		float v = toSeconds(value, inputUnit);
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
	public static float convert(float value, TimeUnit inputUnit, TimeUnit outputUnit) {
		float v = toSeconds(value, inputUnit);
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
	public static float convert(float value, SpeedUnit inputUnit, SpeedUnit outputUnit) {
		float v = toMetersPerSecond(value, inputUnit);
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
	public static float convert(float value, AngularUnit inputUnit, AngularUnit outputUnit) {
		float v = toRadiansPerSecond(value, inputUnit);
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
	public static float convert(float value, SpaceUnit inputUnit, SpaceUnit outputUnit) {
		float v = toMeters(value, inputUnit);
		return fromMeters(v, outputUnit);
	}

	/** Convert the given value expressed in the given unit to meters per second.
	 * 
	 * @param value is the value to convert
	 * @param inputUnit is the unit of the <var>value</var>
	 * @return the result of the convertion.
	 */
	public static float toMetersPerSecond(float value, SpeedUnit inputUnit) {
		switch(inputUnit) {
		case KILOMETERS_PER_HOUR:
			return 3.6f * value;
		case MILLIMETERS_PER_SECOND:
			return value / 1000.f;
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
	public static float toRadiansPerSecond(float value, AngularUnit inputUnit) {
		switch(inputUnit) {
		case TURNS_PER_SECOND:
			return value * (2*MathConstants.PI);
		case DEGREES_PER_SECOND:
			return (float) Math.toRadians(value);
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
	public static float fromMetersPerSecond(float value, SpeedUnit outputUnit) {
		switch(outputUnit) {
		case KILOMETERS_PER_HOUR:
			return value / 3.6f;
		case MILLIMETERS_PER_SECOND:
			return value * 1000.f;
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
	public static float fromRadiansPerSecond(float value, AngularUnit outputUnit) {
		switch(outputUnit) {
		case TURNS_PER_SECOND:
			return value / (2.f*MathConstants.PI);
		case DEGREES_PER_SECOND:
			return (float) Math.toDegrees(value);
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
	public static SpaceUnit getSmallestUnit(float amount, SpaceUnit unit) {
		float meters = toMeters(amount, unit);
		float v;
		SpaceUnit[] units = SpaceUnit.values();
		SpaceUnit u;
		for(int i=units.length-1; i>=0; --i) {
			u = units[i];
			v = (float) Math.floor(fromMeters(meters, u));
			if (v>0.) return u;
		}
		return unit;
	}
	
}