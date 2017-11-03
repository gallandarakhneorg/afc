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

package org.arakhne.afc.gis.coordinate;

import java.io.Serializable;
import java.util.prefs.Preferences;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.locale.Locale;

/** Describes a GPS position in the geographic WSG84 standard.
 *
 * <p>The geographic latitude (abbreviation: Lat., or phi) of a point on
 * the Earth's surface is the angle between the equatorial plane and a
 * line that passes through that point and is normal to the surface of
 * a reference ellipsoid which approximates the shape of the Earth.
 * This line passes a few kilometers away from the center of the Earth
 * except at the poles and the equator where it passes through Earth's
 * center.
 * Lines joining points of the same latitude trace circles on the
 * surface of the Earth called parallels, as they are parallel to the
 * equator and to each other. The north pole is 90&deg; N; the south pole
 * is 90&deg; S. The 0&deg; parallel of latitude is designated the equator,
 * the fundamental plane of all geographic coordinate systems. The equator
 * divides the globe into Northern and Southern Hemispheres.
 *
 * <p>The Longitude (abbreviation: Long., or lambda) of a point on the
 * Earth's surface is the angle east or west from a reference meridian
 * to another meridian that passes through that point. All meridians are
 * halves of great ellipses (often improperly called great circles),
 * which converge at the north and south poles.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public class GeodesicPosition implements Cloneable, Serializable {

	/** Default string representation when not defined by the user.
	 */
	public static final GeodesicPositionStringRepresentation DEFAULT_STRING_REPRESENTATION =
			GeodesicPositionStringRepresentation.DEGREE;

	/** Default usage of the direction symbols in the string representation when not defined by the user.
	 */
	public static final boolean DEFAULT_SYMBOL_IN_STRING_REPRESENTATION = true;

	private static final long serialVersionUID = -9175636969708372806L;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/** Lambda component in the WSG84 standard.
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public final double lambda;

	/** Phi component in the WSG84 standard.
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public final double phi;

	/**
	 * Construct a GSC position from its sexagesimal representation.
	 *
	 * @param latDegree is the latitute degree in [0;180)
	 * @param latMinute is the latitute minute in [0;60)
	 * @param latSecond is the latitute second in [0;60)
	 * @param latAxis is the axis of the given latitude coordinate.
	 * @param longDegree is the longitude degree in [0;180)
	 * @param longMinute is the longitude minute in [0;60)
	 * @param longSecond is the longitude second in [0;60)
	 * @param longAxis is the axis of the given longitude coordinate.
	 */
	public GeodesicPosition(
			int latDegree, int latMinute, double latSecond, SexagesimalLatitudeAxis latAxis,
			int longDegree, int longMinute, double longSecond, SexagesimalLongitudeAxis longAxis) {
		assert latDegree >= 0 && latDegree < 180;
		assert longDegree >= 0 && longDegree < 180;
		assert latMinute >= 0 && latMinute < 60;
		assert longMinute >= 0 && longMinute < 60;
		assert latSecond >= 0. && latSecond < 60.;
		assert longSecond >= 0. && longSecond < 60.;
		this.phi = (latDegree + latMinute / 60. + latSecond / 3600.)
					* (latAxis == SexagesimalLatitudeAxis.NORTH ? 1. : -1.);
		this.lambda = (longDegree + longMinute / 60. + longSecond / 3600.)
					* (longAxis == SexagesimalLongitudeAxis.EAST ? 1. : -1.);
	}

	/** Construct a GSC position.
	 * @param lambda is the longitude in degrees.
	 * @param phi is the latitude in degrees.
	 */
	public GeodesicPosition(double lambda, double phi) {
		this.lambda = lambda;
		this.phi = phi;
	}

	/** Construct a GSC position.
	 * @param position in degrees.
	 */
	public GeodesicPosition(GeodesicPosition position) {
		if (position != null) {
			this.lambda = position.lambda;
			this.phi = position.phi;
		} else {
			this.lambda = Double.NaN;
			this.phi = Double.NaN;
		}
	}

	/** Construct a GSC position.
	 * @param position in degrees.
	 */
	public GeodesicPosition(Tuple2D<?> position) {
		if (position != null) {
			this.lambda = position.getX();
			this.phi = position.getY();
		} else {
			this.lambda = Double.NaN;
			this.phi = Double.NaN;
		}
	}

	/** Replies the latitude phi.
	 *
	 * @return phi in degrees.
	 */
	@Pure
	public double getLatitude() {
		return this.phi;
	}

	/** Replies the degree of the sexagesimal representation of the latitude phi.
	 *
	 * @return the latitude degree in [0;180).
	 */
	@Pure
	public int getLatitudeDegree() {
		return (int) Math.abs(this.phi);
	}

	/** Replies the minute of the sexagesimal representation of the latitude phi.
	 *
	 * @return the latitude minute in [0;60).
	 */
	@SuppressWarnings("checkstyle:localvariablename")
	@Pure
	public int getLatitudeMinute() {
		double p = Math.abs(this.phi);
		p = p - (int) p;
		return (int) (p * 60.);
	}

	/** Replies the second of the sexagesimal representation of the latitude phi.
	 *
	 * @return the latitude second in [0;60).
	 */
	@SuppressWarnings("checkstyle:localvariablename")
	@Pure
	public double getLatitudeSecond() {
		double p = Math.abs(this.phi);
		p = (p - (int) p) * 60.;
		p = p - (int) p;
		return p * 60.;
	}

	/** Replies the axis of the sexagesimal representation of the latitude phi.
	 *
	 * @return the axis of the latitude.
	 */
	@Pure
	public SexagesimalLatitudeAxis getLatitudeAxis() {
		return this.phi < 0. ? SexagesimalLatitudeAxis.SOUTH : SexagesimalLatitudeAxis.NORTH;
	}

	/** Replies the longitude lamda.
	 *
	 * @return lambda in degrees.
	 */
	@Pure
	public double getLongitude() {
		return this.lambda;
	}

	/** Replies the degree of the sexagesimal representation of the longitude phi.
	 *
	 * @return the longitude degree in [0;180).
	 */
	@Pure
	public int getLongitudeDegree() {
		return (int) Math.abs(this.lambda);
	}

	/** Replies the minute of the sexagesimal representation of the longitude phi.
	 *
	 * @return the longitude minute in [0;60).
	 */
	@SuppressWarnings("checkstyle:localvariablename")
	@Pure
	public int getLongitudeMinute() {
		double l = Math.abs(this.lambda);
		l = l - (int) l;
		return (int) (l * 60.);
	}

	/** Replies the second of the sexagesimal representation of the longitude phi.
	 *
	 * @return the longitude second in [0;60).
	 */
	@SuppressWarnings("checkstyle:localvariablename")
	@Pure
	public double getLongitudeSecond() {
		double p = Math.abs(this.lambda);
		p = (p - (int) p) * 60.;
		p = p - (int) p;
		return p * 60.;
	}

	/** Replies the axis of the sexagesimal representation of the longitude phi.
	 *
	 * @return the axis of the latitude.
	 */
	@Pure
	public SexagesimalLongitudeAxis getLongitudeAxis() {
		return this.lambda < 0. ? SexagesimalLongitudeAxis.WEST : SexagesimalLongitudeAxis.EAST;
	}

	/** Replies the GPS position as a point,
	 * where <code>x</code> is lambda and <code>y</code> is phi.
	 *
	 * @return the point representation of this GPS position in degrees..
	 */
	@Pure
	public Point2d toPoint2d() {
		return new Point2d(this.lambda, this.phi);
	}

	/** Replies the string representation of the longitude/latitude coordinates
	 * in the DMS format: coordinate containing degrees (integer), minutes (integer),
	 * and seconds (integer, or real number).
	 *
	 * <p>Example with symbolic direction: <code>40&deg;26'47"N 79&deg;58'36"W</code><br>
	 * Example without symbolic direction: <code>40&deg;26'47" -79&deg;58'36"</code><br>
	 *
	 * @param lambda is the longitude in degrees.
	 * @param phi is the latitude in degrees.
	 * @param useSymbolicDirection indicates if the directions should be output with there
	 *     symbols or if there are represented by the signs of the coordinates.
	 * @return the string representation of the longitude/latitude coordinates.
	 */
	@SuppressWarnings("checkstyle:localvariablename")
	@Pure
	public static String toDMSString(double lambda, double phi, boolean useSymbolicDirection) {
		final StringBuilder b = new StringBuilder();
		final SexagesimalLatitudeAxis latitudeAxis = (phi < 0.) ? SexagesimalLatitudeAxis.SOUTH : SexagesimalLatitudeAxis.NORTH;
		final SexagesimalLongitudeAxis longitudeAxis = (lambda < 0.) ? SexagesimalLongitudeAxis.WEST
				: SexagesimalLongitudeAxis.EAST;
		toDMSString(b, phi, useSymbolicDirection ? Locale.getString(latitudeAxis.name()) : EMPTY_STRING);
		b.append(" "); //$NON-NLS-1$
		toDMSString(b, lambda, useSymbolicDirection ? Locale.getString(longitudeAxis.name()) : EMPTY_STRING);
		return b.toString();
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:parametername", "checkstyle:localvariablename"})
	private static void toDMSString(StringBuilder b, double value, String direction) {
		final double v = Math.abs(value);
		int deg = (int) v;
		final double d = (v - deg) * 60.;
		int min = (int) d;
		double sec = (d - min) * 60.;

		if (deg < 0) {
			deg = 0;
		}
		if (min < 0) {
			min = 0;
		}
		if (sec < 0.) {
			sec = 0.;
		}

		final String s;
		if (deg > 0) {
			if (direction == EMPTY_STRING && value < 0.) {
				deg = -deg;
			}
			if (min > 0) {
				if (sec > 0) {
					s = Locale.getString("DMS_DEG_MIN_SEC", deg, min, sec, direction); //$NON-NLS-1$
				} else {
					s = Locale.getString("DMS_DEG_MIN", deg, min, direction); //$NON-NLS-1$
				}
			} else if (sec > 0) {
				s = Locale.getString("DMS_DEG_SEC", deg, sec, direction); //$NON-NLS-1$
			} else {
				s = Locale.getString("DMS_DEG", deg, direction); //$NON-NLS-1$
			}
		} else if (min > 0) {
			if (direction == EMPTY_STRING && value < 0.) {
				min = -min;
			}
			if (sec > 0) {
				s = Locale.getString("DMS_MIN_SEC", min, sec, direction); //$NON-NLS-1$
			} else {
				s = Locale.getString("DMS_MIN", min, direction); //$NON-NLS-1$
			}
		} else {
			if (direction == EMPTY_STRING && value < 0.) {
				sec = -sec;
			}
			s = Locale.getString("DMS_SEC", sec, direction); //$NON-NLS-1$
		}

		b.append(s);
	}

	/** Replies the string representation of the longitude/latitude coordinates
	 * in the Decimal Degree format: coordinate containing only degrees (integer, or real number).
	 *
	 * <p>Example: <code>40.446195N 79.948862W</code>
	 *
	 * @param lambda is the longitude in degrees.
	 * @param phi is the latitude in degrees.
	 * @param useSymbolicDirection indicates if the directions should be output with there
	 *     symbols or if there are represented by the signs of the coordinates.
	 * @return the string representation of the longitude/latitude coordinates.
	 */
	@Pure
	public static String toDecimalDegreeString(double lambda, double phi, boolean useSymbolicDirection) {
		final StringBuilder b = new StringBuilder();
		final SexagesimalLatitudeAxis latitudeAxis = (phi < 0.) ? SexagesimalLatitudeAxis.SOUTH : SexagesimalLatitudeAxis.NORTH;
		final SexagesimalLongitudeAxis longitudeAxis = (lambda < 0.) ? SexagesimalLongitudeAxis.WEST
				: SexagesimalLongitudeAxis.EAST;
		if (useSymbolicDirection) {
			b.append(Locale.getString("D_DEG", Math.abs(phi), Locale.getString(latitudeAxis.name()))); //$NON-NLS-1$
		} else {
			b.append(Locale.getString("D_DEG", phi, EMPTY_STRING)); //$NON-NLS-1$
		}
		b.append(" "); //$NON-NLS-1$
		if (useSymbolicDirection) {
			b.append(Locale.getString("D_DEG", Math.abs(lambda), Locale.getString(longitudeAxis.name()))); //$NON-NLS-1$
		} else {
			b.append(Locale.getString("D_DEG", lambda, EMPTY_STRING)); //$NON-NLS-1$
		}
		return b.toString();
	}

	/** Replies the string representation of the longitude/latitude coordinates
	 * in the Decimal Degree Minute format: coordinate containing degrees (integer)
	 * and minutes (integer, or real number).
	 *
	 * <p>Example: <code>40° 26.7717N 79° 56.93172W</code>
	 *
	 * @param lambda is the longitude in degrees.
	 * @param phi is the latitude in degrees.
	 * @param useSymbolicDirection indicates if the directions should be output with there
	 *     symbols or if there are represented by the signs of the coordinates.
	 * @return the string representation of the longitude/latitude coordinates.
	 */
	@Pure
	public static String toDecimalDegreeMinuteString(double lambda, double phi, boolean useSymbolicDirection) {
		final StringBuilder b = new StringBuilder();
		final SexagesimalLatitudeAxis latitudeAxis = (phi < 0.) ? SexagesimalLatitudeAxis.SOUTH : SexagesimalLatitudeAxis.NORTH;
		final SexagesimalLongitudeAxis longitudeAxis = lambda < 0. ? SexagesimalLongitudeAxis.WEST
				: SexagesimalLongitudeAxis.EAST;
		toDMString(b, phi, useSymbolicDirection ? Locale.getString(latitudeAxis.name()) : EMPTY_STRING);
		b.append(" "); //$NON-NLS-1$
		toDMString(b, lambda, useSymbolicDirection ? Locale.getString(longitudeAxis.name()) : EMPTY_STRING);
		return b.toString();
	}

	@SuppressWarnings({"checkstyle:parametername", "checkstyle:localvariablename", "checkstyle:magicnumber"})
	private static void toDMString(StringBuilder b, double value, String direction) {
		final double v = Math.abs(value);
		int deg = (int) v;
		final double min = (v - deg) * 60.;

		if (deg < 0) {
			deg = 0;
		}
		if (value < 0. && direction == EMPTY_STRING) {
			deg = -deg;
		}

		final String s;
		if (min > 0) {
			s = Locale.getString("DM_DEG_MIN", deg, min, direction); //$NON-NLS-1$
		} else {
			s = Locale.getString("DM_DEG", deg, direction); //$NON-NLS-1$
		}

		b.append(s);
	}

	/** Set the preferred format of the string representation
	 * of a geodesic position.
	 *
	 * @param representation indicates the preferred string representation, or <code>null</code>
	 *     to use the default.
	 * @param useSymbolicDirection indicates if the directions should be output with there
	 *     symbols or if there are represented by the signs of the coordinates.
	 * @see #getPreferredStringRepresentation()
	 * @see #toString()
	 */
	public static void setPreferredStringRepresentation(GeodesicPositionStringRepresentation representation,
			boolean useSymbolicDirection) {
		final Preferences prefs = Preferences.userNodeForPackage(GeodesicPosition.class);
		if (prefs != null) {
			if (representation == null) {
				prefs.remove("STRING_REPRESENTATION"); //$NON-NLS-1$
				prefs.remove("SYMBOL_IN_STRING_REPRESENTATION"); //$NON-NLS-1$
			} else {
				prefs.put("STRING_REPRESENTATION", representation.toString()); //$NON-NLS-1$
				prefs.putBoolean("SYMBOL_IN_STRING_REPRESENTATION", useSymbolicDirection); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred format of the string representation
	 * of a geodesic position.
	 *
	 * @return the preferred string representation, or <code>null</code>
	 *     to use the default; never <code>null</code>.
	 * @see #setPreferredStringRepresentation(GeodesicPositionStringRepresentation, boolean)
	 * @see #getDirectionSymbolInPreferredStringRepresentation()
	 * @see #toString()
	 */
	@Pure
	public static GeodesicPositionStringRepresentation getPreferredStringRepresentation() {
		return getPreferredStringRepresentation(false);
	}

	/** Replies the preferred format of the string representation
	 * of a geodesic position.
	 *
	 * @param allowNullValue indicates if the <code>null</code> value is enable
	 *     for output in the replied value.
	 * @return the preferred string representation, or <code>null</code>
	 *     to use the default. The <code>null</code> value is replied iff
	 *     <var>allowNullValue</var> is <code>true</code> and if the
	 *     default configuration is currently used.
	 * @see #setPreferredStringRepresentation(GeodesicPositionStringRepresentation, boolean)
	 * @see #getDirectionSymbolInPreferredStringRepresentation()
	 * @see #toString()
	 */
	@Pure
	public static GeodesicPositionStringRepresentation getPreferredStringRepresentation(boolean allowNullValue) {
		final Preferences prefs = Preferences.userNodeForPackage(GeodesicPosition.class);
		if (prefs != null) {
			final String v = prefs.get("STRING_REPRESENTATION", null); //$NON-NLS-1$
			if (v != null && !"".equals(v)) { //$NON-NLS-1$
				try {
					GeodesicPositionStringRepresentation rep;
					try {
						rep = GeodesicPositionStringRepresentation.valueOf(v.toUpperCase());
					} catch (Throwable exception) {
						rep = null;
					}
					if (rep != null) {
						return rep;
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					//
				}
			}
		}
		if (allowNullValue) {
			return null;
		}
		return DEFAULT_STRING_REPRESENTATION;
	}

	/** Replies if the direction symbol should be
	 * output in the preferred string representation
	 * of a geodesic position.
	 *
	 * @return <code>true</code> if the direction symbol should be output,
	 * <code>false</code> if the signs of the coordinates indicate the directions.
	 * @see #setPreferredStringRepresentation(GeodesicPositionStringRepresentation, boolean)
	 * @see #getPreferredStringRepresentation()
	 * @see #toString()
	 */
	@Pure
	public static boolean getDirectionSymbolInPreferredStringRepresentation() {
		final Preferences prefs = Preferences.userNodeForPackage(GeodesicPosition.class);
		if (prefs != null) {
			return prefs.getBoolean("SYMBOL_IN_STRING_REPRESENTATION", DEFAULT_SYMBOL_IN_STRING_REPRESENTATION); //$NON-NLS-1$
		}
		return DEFAULT_SYMBOL_IN_STRING_REPRESENTATION;
	}

	/** Replies the string representation of this geodesic position
	 * according to the preferred string format replied by
	 * {@link #getPreferredStringRepresentation()} and
	 * {@link #getDirectionSymbolInPreferredStringRepresentation()}.
	 *
	 * @return the string representation of this geodesic position.
	 * @see #setPreferredStringRepresentation(GeodesicPositionStringRepresentation, boolean)
	 * @see #getPreferredStringRepresentation()
	 * @see #getDirectionSymbolInPreferredStringRepresentation()
	 */
	@Pure
	@Override
	public String toString() {
		final boolean useDirectionSymbol = getDirectionSymbolInPreferredStringRepresentation();
		switch (getPreferredStringRepresentation()) {
		case DEGREE_MINUTE_SECOND:
			return toDMSString(this.lambda, this.phi, useDirectionSymbol);
		case DEGREE_MINUTE:
			return toDecimalDegreeMinuteString(this.lambda, this.phi, useDirectionSymbol);
		case DEGREE:
			return toDecimalDegreeString(this.lambda, this.phi, useDirectionSymbol);
		default:
		}
		throw new IllegalStateException();
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GeodesicPosition) {
			final GeodesicPosition p = (GeodesicPosition) obj;
			return this.lambda == p.lambda && this.phi == p.phi;
		}
		if (obj instanceof Tuple2D<?>) {
			final Tuple2D<?> p = (Tuple2D<?>) obj;
			return this.lambda == p.getX() && this.phi == p.getY();
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		int hvalue = Double.valueOf(this.lambda).hashCode();
		hvalue = hvalue * 31 + Double.valueOf(this.phi).hashCode();
		return hvalue;
	}

}
