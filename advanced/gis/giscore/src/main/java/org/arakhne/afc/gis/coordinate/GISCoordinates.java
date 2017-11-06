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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Utilities to convert GIS coordinates.
 *
 * <p>Details on the algorithms are in <a href="./doc-files/NTG_71.pdf">NTG_71.pdf</a>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see "./doc-files/NTG_71.pdf"
 */
@SuppressWarnings("checkstyle:methodname")
public final class GISCoordinates {

	/** Epsilon constant from the IGN specifications.
	 */
	public static final double EPSILON = 1e-10;

	/** The value of the e constant in the NTF specification.
	 */
	public static final double NTF_E = 0.08248325676;

	/** Demi {@link #NTF_E}.
	 */
	public static final double DEMI_NTF_E = NTF_E / 2.;

	/** The value of the e constant in the WSG94 specification.
	 */
	public static final double WSG84_E = 0.08181919106;

	/** France Lambert I Constant n.
	 */
	public static final double LAMBERT_1_N = 0.7604059656;

	/** France Lambert I Constant c.
	 */
	public static final double LAMBERT_1_C = 11603796.98;

	/** France Lambert I Constant Xs.
	 */
	public static final double LAMBERT_1_XS = 600000.0;

	/** France Lambert I Constant Ys.
	 */
	public static final double LAMBERT_1_YS = 5657616.674;

	/** France Lambert II Constant n.
	 */
	public static final double LAMBERT_2_N = 0.7289686274;

	/** France Lambert II Constant c.
	 */
	public static final double LAMBERT_2_C = 11745793.39;

	/** France Lambert II Constant Xs.
	 */
	public static final double LAMBERT_2_XS = 600000.0;

	/** France Lambert II Constant Ys.
	 */
	public static final double LAMBERT_2_YS = 6199695.768;

	/** Extended France Lambert II Constant n.
	 */
	public static final double LAMBERT_2E_N = 0.7289686274;

	/** Extended France Lambert II Constant c.
	 */
	public static final double LAMBERT_2E_C = 11745793.39;

	/** Extended France Lambert II Constant Xs.
	 */
	public static final double LAMBERT_2E_XS = 600000.0;

	/** Extended France Lambert II Constant Ys.
	 */
	public static final double LAMBERT_2E_YS = 8199695.768;

	/** France Lambert III Constant n.
	 */
	public static final double LAMBERT_3_N = 0.6959127966;

	/** France Lambert III Constant c.
	 */
	public static final double LAMBERT_3_C = 11947992.52;

	/** France Lambert III Constant Xs.
	 */
	public static final double LAMBERT_3_XS = 600000.0;

	/** France Lambert III Constant Ys.
	 */
	public static final double LAMBERT_3_YS = 6791905.085;

	/** France Lambert IV Constant n.
	 */
	public static final double LAMBERT_4_N = 0.6712679322;

	/** France Lambert IV Constant c.
	 */
	public static final double LAMBERT_4_C = 12136281.99;

	/** France Lambert IV Constant Xs.
	 */
	public static final double LAMBERT_4_XS = 234.358;

	/** France Lambert IV Constant Ys.
	 */
	public static final double LAMBERT_4_YS = 7239161.542;

	/** France Lambert 93 Constant n.
	 */
	public static final double LAMBERT_93_N = 0.7256077650;

	/** France Lambert 93 Constant c.
	 */
	public static final double LAMBERT_93_C = 11754255.426;

	/** France Lambert 93 Constant Xs.
	 */
	public static final double LAMBERT_93_XS = 700000.0;

	/** France Lambert 93 Constant Ys.
	 */
	public static final double LAMBERT_93_YS = 12655612.050;

	private GISCoordinates() {
		//
	}

	/**
	 * This function convert extended France Lambert II coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition EL2_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert extended France Lambert II coordinate to
	 * France Lambert I coordinate.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d EL2_L1(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert extended France Lambert II coordinate to
	 * France Lambert II coordinate.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return the France Lambert II coordinate.
	 */
	@Pure
	public static Point2d EL2_L2(double x, double y) {
		return new Point2d(x, y + (LAMBERT_2E_YS - LAMBERT_2_YS));
	}

	/**
	 * This function convert extended France Lambert II coordinate to
	 * France Lambert III coordinate.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return the France Lambert III coordinate.
	 */
	@Pure
	public static Point2d EL2_L3(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert extended France Lambert II coordinate to
	 * France Lambert IV coordinate.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return the France Lambert IV coordinate.
	 */
	@Pure
	public static Point2d EL2_L4(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert extended France Lambert II coordinate to
	 * France Lambert 93 coordinate.
	 *
	 * @param x is the coordinate in extended France Lambert II
	 * @param y is the coordinate in extended France Lambert II
	 * @return the France Lambert 93 coordinate.
	 */
	@Pure
	public static Point2d EL2_L93(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert France Lambert I coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition L1_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert France Lambert I coordinate to
	 * extended France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return the extended France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L1_EL2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
	}

	/**
	 * This function convert France Lambert I coordinate to
	 * France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return the France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L1_L2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
	}

	/**
	 * This function convert France Lambert I coordinate to
	 * France Lambert III coordinate.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return the France Lambert III coordinate.
	 */
	@Pure
	public static Point2d L1_L3(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert France Lambert I coordinate to
	 * France Lambert IV coordinate.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return the France Lambert IV coordinate.
	 */
	@Pure
	public static Point2d L1_L4(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert France Lambert I coordinate to
	 * France Lambert 93 coordinate.
	 *
	 * @param x is the coordinate in France Lambert I
	 * @param y is the coordinate in France Lambert I
	 * @return the France Lambert 93 coordinate.
	 */
	@Pure
	public static Point2d L1_L93(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert France Lambert II coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition L2_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert France Lambert II coordinate to
	 * extended France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return the extended France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L2_EL2(double x, double y) {
		return new Point2d(x, y - (LAMBERT_2E_YS - LAMBERT_2_YS));
	}

	/**
	 * This function convert France Lambert II coordinate to
	 * France Lambert I coordinate.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d L2_L1(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert France Lambert II coordinate to
	 * France Lambert III coordinate.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return the France Lambert III coordinate.
	 */
	@Pure
	public static Point2d L2_L3(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert France Lambert II coordinate to
	 * France Lambert IV coordinate.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return the France Lambert IV coordinate.
	 */
	@Pure
	public static Point2d L2_L4(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert France Lambert II coordinate to
	 * France Lambert 93 coordinate.
	 *
	 * @param x is the coordinate in France Lambert II
	 * @param y is the coordinate in France Lambert II
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d L2_L93(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert France Lambert III coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition L3_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert France Lambert III coordinate to
	 * extended France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return the extended France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L3_EL2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
	}

	/**
	 * This function convert France Lambert III coordinate to
	 * France Lambert I coordinate.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d L3_L1(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert France Lambert III coordinate to
	 * France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return the France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L3_L2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
	}

	/**
	 * This function convert France Lambert III coordinate to
	 * France Lambert IV coordinate.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return the France Lambert IV coordinate.
	 */
	@Pure
	public static Point2d L3_L4(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert France Lambert III coordinate to
	 * France Lambert 93 coordinate.
	 *
	 * @param x is the coordinate in France Lambert III
	 * @param y is the coordinate in France Lambert III
	 * @return the France Lambert 93 coordinate.
	 */
	@Pure
	public static Point2d L3_L93(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert France Lambert IV coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition L4_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert France Lambert IV coordinate to
	 * extended France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return the extended France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L4_EL2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
	}

	/**
	 * This function convert France Lambert IV coordinate to
	 * France Lambert I coordinate.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d L4_L1(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert France Lambert IV coordinate to
	 * France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return the France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L4_L2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
	}

	/**
	 * This function convert France Lambert IV coordinate to
	 * France Lambert III coordinate.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return the France Lambert III coordinate.
	 */
	@Pure
	public static Point2d L4_L3(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert France Lambert IV coordinate to
	 * France Lambert 93 coordinate.
	 *
	 * @param x is the coordinate in France Lambert IV
	 * @param y is the coordinate in France Lambert IV
	 * @return the France Lambert 93 coordinate.
	 */
	@Pure
	public static Point2d L4_L93(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert France Lambert 93 coordinate to geographic WSG84 Data.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@Pure
	public static GeodesicPosition L93_WSG84(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_WSG84(ntfLambdaPhi.getX(), ntfLambdaPhi.getY());
	}

	/**
	 * This function convert France Lambert 93 coordinate to
	 * extended France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return the extended France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L93_EL2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
	}

	/**
	 * This function convert France Lambert 93 coordinate to
	 * France Lambert I coordinate.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return the France Lambert I coordinate.
	 */
	@Pure
	public static Point2d L93_L1(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert France Lambert 93 coordinate to
	 * France Lambert II coordinate.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return the France Lambert II coordinate.
	 */
	@Pure
	public static Point2d L93_L2(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
	}

	/**
	 * This function convert France Lambert 93 coordinate to
	 * France Lambert III coordinate.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return the France Lambert III coordinate.
	 */
	@Pure
	public static Point2d L93_L3(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert France Lambert 93 coordinate to
	 * France Lambert IV coordinate.
	 *
	 * @param x is the coordinate in France Lambert 93
	 * @param y is the coordinate in France Lambert 93
	 * @return the France Lambert IV coordinate.
	 */
	@Pure
	public static Point2d L93_L4(double x, double y) {
		final Point2d ntfLambdaPhi = NTFLambert_NTFLambdaPhi(x, y,
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert extended France NTF Lambert coordinate to Angular NTF coordinate.
	 *
	 * @param x is the coordinate in extended France NTF Lambert
	 * @param y is the coordinate in extended France NTF Lambert
	 * @param n is the exponential of the Lambert projection.
	 * @param c is the constant of projection.
	 * @param Xs is the x coordinate of the origine of the Lambert projection.
	 * @param Ys is the y coordinate of the origine of the Lambert projection.
	 * @return lambda and phi in NTF.
	 */
	@SuppressWarnings({"checkstyle:parametername", "checkstyle:localfinalvariablename"})
	private static Point2d NTFLambert_NTFLambdaPhi(double x, double y, double n, double c, double Xs, double Ys) {
		// Several constants from the IGN specifications
		//Longitude in radians of Paris (2°20'14.025" E) from Greenwich
		final double lambda_0 = 0.;

		// Extended Lambert II (x,y) -> graphical coordinate NTF (lambda_ntf,phi_ntf)
		// ALG0004
		final double R = Math.hypot(x - Xs, y - Ys);
		final double g = Math.atan((x - Xs) / (Ys - y));
		final double lamdda_ntf = lambda_0 + (g / n);
		final double L = -(1 / n) * Math.log(Math.abs(R / c));
		final double phi0 = 2 * Math.atan(Math.exp(L)) - (Math.PI / 2.0);
		double phiprec = phi0;
		double phii = compute1(phiprec, L);

		while (Math.abs(phii - phiprec) >= EPSILON) {
			phiprec = phii;
			phii = compute1(phiprec, L);
		}

		final double phi_ntf = phii;

		return new Point2d(lamdda_ntf, phi_ntf);
	}

	@SuppressWarnings({"checkstyle:parametername"})
	private static double compute1(double phiprec, double L) {
		final double pow = Math.pow(
				(1. + NTF_E * Math.sin(phiprec)) / (1. - NTF_E * Math.sin(phiprec)),
				DEMI_NTF_E);
		return 2. * Math.atan(pow * Math.exp(L)) - MathConstants.DEMI_PI;
	}

	/**
	 * This function convert extended France NTF Lambert coordinate to geographic WSG84 Data.
	 *
	 * @param lambda_ntf is the lambda coordinate in NTF
	 * @param lambda_ntf is the phi coordinate in NTF
	 * @return lambda and phi in geographic WSG84 in degrees.
	 */
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localfinalvariablename", "checkstyle:localvariablename"})
	private static GeodesicPosition NTFLambdaPhi_WSG84(double lambda_ntf, double phi_ntf) {
		// Geographical coordinate NTF (lamda_ntf,phi_ntf)
		// -> Cartesian coordinate NTF (x_ntf,y_ntf,z_ntf)
		// ALG0009
		double a = 6378249.2;
		// 100 meters
		final double h = 100;
		final double N = a / Math.pow(1. - (NTF_E * NTF_E) * (Math.sin(phi_ntf) * Math.sin(phi_ntf)), .5);
		final double x_ntf = (N + h) * Math.cos(phi_ntf) * Math.cos(lambda_ntf);
		final double y_ntf = (N + h) * Math.cos(phi_ntf) * Math.sin(lambda_ntf);
		final double z_ntf = ((N * (1. - (NTF_E * NTF_E))) + h) * Math.sin(phi_ntf);

		// Cartesian coordinate NTF (x_ntf,y_ntf,z_ntf)
		// -> Cartesian coordinate WGS84 (x_w,y_w,z_w)
		// ALG0013

		// This is a simple translation.
		final double x_w = x_ntf - 168.;
		final double y_w = y_ntf - 60.;
		final double z_w = z_ntf + 320;

		// Cartesian coordinate WGS84 (x_w,y_w,z_w)
		// -> Geographic coordinate WGS84 (lamda_w,phi_w)
		// ALG0012

		// 0.04079234433 to use the Greenwich meridian, 0 else
		final double l840 = 0.04079234433;
		a = 6378137.0;

		final double P = Math.hypot(x_w, y_w);

		double lambda_w = l840 + Math.atan(y_w / x_w);

		double phi0_w = Math.atan(z_w / (P * (1 - ((a * WSG84_E * WSG84_E))
				/ Math.sqrt((x_w * x_w) + (y_w * y_w) + (z_w * z_w)))));

		double phi_w = Math.atan((z_w / P) / (1 - ((a * WSG84_E * WSG84_E * Math.cos(phi0_w))
				/ (P * Math.sqrt(1 - NTF_E * NTF_E * (Math.sin(phi0_w) * Math.sin(phi0_w)))))));

		while (Math.abs(phi_w - phi0_w) >= EPSILON) {
			phi0_w = phi_w;
			phi_w = Math.atan((z_w / P) / (1 - ((a * WSG84_E * WSG84_E * Math.cos(phi0_w))
					/ (P * Math.sqrt(1 - ((WSG84_E * WSG84_E) * (Math.sin(phi0_w) * Math.sin(phi0_w))))))));

		}

		// Convert radians to degrees.
		lambda_w = Math.toDegrees(lambda_w);
		phi_w = Math.toDegrees(phi_w);

		return new GeodesicPosition(lambda_w, phi_w);
	}

	/**
	 * This function convert WSG84 GPS coordinate to extended France Lambert II coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the extended France Lambert II coordinates.
	 */
	@Pure
	public static Point2d WSG84_EL2(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2E_N,
				LAMBERT_2E_C,
				LAMBERT_2E_XS,
				LAMBERT_2E_YS);
	}

	/**
	 * This function convert WSG84 GPS coordinate to France Lambert I coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the France Lambert I coordinates.
	 */
	@Pure
	public static Point2d WSG84_L1(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_1_N,
				LAMBERT_1_C,
				LAMBERT_1_XS,
				LAMBERT_1_YS);
	}

	/**
	 * This function convert WSG84 GPS coordinate to France Lambert II coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the France Lambert II coordinates.
	 */
	@Pure
	public static Point2d WSG84_L2(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_2_N,
				LAMBERT_2_C,
				LAMBERT_2_XS,
				LAMBERT_2_YS);
	}

	/**
	 * This function convert WSG84 GPS coordinate to France Lambert III coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the France Lambert III coordinates.
	 */
	@Pure
	public static Point2d WSG84_L3(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_3_N,
				LAMBERT_3_C,
				LAMBERT_3_XS,
				LAMBERT_3_YS);
	}

	/**
	 * This function convert WSG84 GPS coordinate to France Lambert IV coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the France Lambert IV coordinates.
	 */
	public static Point2d WSG84_L4(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_4_N,
				LAMBERT_4_C,
				LAMBERT_4_XS,
				LAMBERT_4_YS);
	}

	/**
	 * This function convert WSG84 GPS coordinate to France Lambert 93 coordinate.
	 *
	 * @param lambda  in degrees.
	 * @param phi  in degrees.
	 * @return the France Lambert 93 coordinates.
	 */
	@Pure
	public static Point2d WSG84_L93(double lambda, double phi) {
		final Point2d ntfLambdaPhi = WSG84_NTFLamdaPhi(lambda, phi);
		return NTFLambdaPhi_NTFLambert(
				ntfLambdaPhi.getX(), ntfLambdaPhi.getY(),
				LAMBERT_93_N,
				LAMBERT_93_C,
				LAMBERT_93_XS,
				LAMBERT_93_YS);
	}

	/**
	 * This function convert the geographical NTF Lambda-Phi
	 * coordinate to one of the France NTF standard coordinate.
	 *
	 * @param lambda is the NTF coordinate.
	 * @param phi is the NTF coordinate.
	 * @param n is the exponential of the Lambert projection.
	 * @param c is the constant of projection.
	 * @param Xs is the x coordinate of the origine of the Lambert projection.
	 * @param Ys is the y coordinate of the origine of the Lambert projection.
	 * @return the extended France Lambert II coordinates.
	 */
	@SuppressWarnings({"checkstyle:parametername", "checkstyle:magicnumber",
		"checkstyle:localfinalvariablename", "checkstyle:localvariablename"})
	private static Point2d NTFLambdaPhi_NTFLambert(double lambda, double phi, double n, double c, double Xs, double Ys) {
		//---------------------------------------------------------
		// 3) cartesian coordinate NTF (X_n,Y_n,Z_n)
		//    -> geographical coordinate NTF (phi_n,lambda_n)

		// One formula is given by the IGN, and two constants about
		// the ellipsoide are from the NTF system specification of Clarke 1880.
		// Ref:
		//		http://www.ign.fr/telechargement/MPro/geodesie/CIRCE/NTG_80.pdf
		//		http://support.esrifrance.fr/Documents/Generalites/Projections/Generalites/Generalites.htm#2
		final double a_n = 6378249.2;
		final double b_n = 6356515.0;
		// then
		final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);

		//---------------------------------------------------------
		// 4) Geographical coordinate NTF (phi_n,lambda_n)
		//    -> Extended Lambert II coordinate (X_l2e, Y_l2e)

		// Formula are given by the IGN from another specification
		// Ref:
		//		http://www.ign.fr/telechargement/MPro/geodesie/CIRCE/NTG_71.pdf

		final double e_n = Math.sqrt(e2_n);
		// Let the longitude in radians of Paris (2°20'14.025" E) from the Greenwich meridian
		final double lambda0 = 0.04079234433198;

		// Compute the isometric latitude
		final double L = Math.log(Math.tan(Math.PI / 4. + phi / 2.)
				* Math.pow((1. - e_n * Math.sin(phi)) / (1. + e_n * Math.sin(phi)),
						e_n / 2.));

		// Then do the projection according to extended Lambert II
		final double X_l2e = Xs + c * Math.exp(-n * L) * Math.sin(n * (lambda - lambda0));
		final double Y_l2e = Ys - c * Math.exp(-n * L) * Math.cos(n * (lambda - lambda0));

		return new Point2d(X_l2e, Y_l2e);
	}

	/**
	 * This function convert WSG84 GPS coordinate to one of the NTF Lambda-Phi coordinate.
	 *
	 * @param lambda is the WSG94 coordinate in decimal degrees.
	 * @param phi is the WSG84 coordinate is decimal in degrees.
	 * @param n is the exponential of the Lambert projection.
	 * @param c is the constant of projection.
	 * @param Xs is the x coordinate of the origine of the Lambert projection.
	 * @param Ys is the y coordinate of the origine of the Lambert projection.
	 * @return the NTF Lambda-Phi
	 */
	@SuppressWarnings({"checkstyle:parametername", "checkstyle:magicnumber",
		"checkstyle:localfinalvariablename", "checkstyle:localvariablename"})
	private static Point2d WSG84_NTFLamdaPhi(double lambda, double phi) {
		//---------------------------------------------------------
		// 0) degree -> radian
		final double lambda_w = Math.toRadians(lambda);
		final double phi_w = Math.toRadians(phi);

		//---------------------------------------------------------
		// 1) geographical coordinates WGS84 (phi_w,lambda_w)
		//    -> cartesian coordinate WGS84 (x_w,y_w,z_w)

		// Formula from IGN are used from the official downloadable document, and
		// the two constants, one for each demi-axis, are given by the WGS84 specification
		// of the ellipsoide.
		// Ref:
		// 	http://www.ign.fr/telechargement/MPro/geodesie/CIRCE/NTG_80.pdf
		//  http://de.wikipedia.org/wiki/WGS84
		final double a_w = 6378137.0;
		final double b_w = 6356752.314;
		// then
		final double e2_w = (a_w * a_w - b_w * b_w) / (a_w * a_w);
		// then let the big normal of the WGS84 ellipsoide
		final double N = a_w / Math.sqrt(1. - e2_w * Math.pow(Math.sin(phi_w), 2.));
		// let the WGS84 cartesian coordinates:
		final double X_w = N * Math.cos(phi_w) * Math.cos(lambda_w);
		final double Y_w = N * Math.cos(phi_w) * Math.sin(lambda_w);
		final double Z_w = N * (1 - e2_w) * Math.sin(phi_w);

		//---------------------------------------------------------
		// 2) cartesian coordinate WGS84 (X_w,Y_w,Z_w)
		//    -> cartesian coordinate NTF (X_n,Y_n,Z_n)
		//    Ref: http://support.esrifrance.fr/Documents/Generalites/Projections/Generalites/Generalites.htm#2
		//    No convertion to be done.
		final double dX = 168.0;
		final double dY = 60.0;
		final double dZ = -320.0;

		final double X_n = X_w + dX;
		final double Y_n = Y_w + dY;
		final double Z_n = Z_w + dZ;

		//---------------------------------------------------------
		// 3) cartesian coordinate NTF (X_n,Y_n,Z_n)
		//    -> geographical coordinate NTF (phi_n,lambda_n)

		// One formula is given by the IGN, and two constants about
		// the ellipsoide are from the NTF system specification of Clarke 1880.
		// Ref:
		//		http://www.ign.fr/telechargement/MPro/geodesie/CIRCE/NTG_80.pdf
		//		http://support.esrifrance.fr/Documents/Generalites/Projections/Generalites/Generalites.htm#2
		final double a_n = 6378249.2;
		final double b_n = 6356515.0;
		// then
		final double e2_n = (a_n * a_n - b_n * b_n) / (a_n * a_n);
		// let the convergence epsilon
		final double epsilon = 1e-10;
		// Then try to converge
		double p0 = Math.atan(Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n) * (1 - (a_n * e2_n) / (Math.sqrt(X_n * X_n + Y_n
				* Y_n + Z_n * Z_n))));
		double p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n
				* Math.cos(p0)) / (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));
		while (Math.abs(p1 - p0) >= epsilon) {
			p0 = p1;
			p1 = Math.atan((Z_n / Math.sqrt(X_n * X_n + Y_n * Y_n)) / (1 - (a_n * e2_n * Math.cos(p0))
					/ (Math.sqrt((X_n * X_n + Y_n * Y_n) * (1 - e2_n * Math.pow(Math.sin(p0), 2))))));
		}

		final double phi_n = p1;
		final double lambda_n = Math.atan(Y_n / X_n);

		return new Point2d(lambda_n, phi_n);
	}

}
