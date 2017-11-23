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

package org.arakhne.afc.io.shape;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.vmutil.FileSystem;

/**
 * Constants and utility functions for ESRI shape files.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodname")
public final class ESRIFileUtil {

	/** Code which is identifying the ESRI shape files.
	 */
	public static final int SHAPE_FILE_CODE = 9994;

	/** Version of the ESRI shape file syntax.
	 */
	public static final int SHAPE_FILE_VERSION = 1000;

	/** NaN value for floating point numbers in ESRI files.
	 */
	public static final double ESRI_NAN = -10e38;

	/** Size (in bytes) of the standard shape file header.
	 */
	public static final int HEADER_BYTES = 100;

	private ESRIFileUtil() {
		//
	}

	/** Replies the count of words to put inside an ESRI file and
	 * which is corresponding to the given byte amount.
	 *
	 * @param byteCount the number of bytes
	 * @return the count of words (ie. {@code byteCount/2}).
	 */
	protected static int toESRIWords(int byteCount) {
		return byteCount / 2;
	}

	/** Replies the count of bytes from an ESRI file and
	 * which is corresponding to the given 16-bit word amount.
	 *
	 * @param wordCount number of words.
	 * @return the count of bytes (ie. {@code byteCount*2}).
	 */
	protected static int fromESRIWords(int wordCount) {
		return wordCount * 2;
	}

	/** Translate a x-coordinate from Java standard
	 * to ESRI standard.
	 *
	 * @param x is the Java x-coordinate
	 * @return the ESRI x-coordinate
	 * @throws IOException when invalid conversion.
	 */
	@Pure
	public static double toESRI_x(double x) throws IOException {
		if (Double.isInfinite(x) || Double.isNaN(x)) {
			throw new InvalidNumericValueException(x);
		}
		return x;
	}

	/** Translate a x-coordinate from ESRI standard
	 * to Java standard.
	 *
	 * @param x is the Java x-coordinate
	 * @return the ESRI x-coordinate
	 * @throws IOException when invalid conversion.
	 */
	@Pure
	public static double fromESRI_x(double x) throws IOException {
		if (Double.isInfinite(x) || Double.isNaN(x)) {
			throw new InvalidNumericValueException(x);
		}
		return x;
	}

	/** Translate a y-coordinate from Java standard
	 * to ESRI standard.
	 *
	 * @param y is the Java y-coordinate
	 * @return the ESRI y-coordinate
	 * @throws IOException when invalid conversion.
	 */
	@Pure
	public static double toESRI_y(double y) throws IOException {
		if (Double.isInfinite(y) || Double.isNaN(y)) {
			throw new InvalidNumericValueException(y);
		}
		return y;
	}

	/** Translate a y-coordinate from ESRI standard
	 * to Java standard.
	 *
	 * @param y is the Java y-coordinate
	 * @return the ESRI y-coordinate
	 * @throws IOException when invalid conversion.
	 */
	@Pure
	public static double fromESRI_y(double y) throws IOException {
		if (Double.isInfinite(y) || Double.isNaN(y)) {
			throw new InvalidNumericValueException(y);
		}
		return y;
	}

	/** Translate a z-coordinate from Java standard
	 * to ESRI standard.
	 *
	 * @param z is the Java z-coordinate
	 * @return the ESRI z-coordinate
	 */
	@Pure
	public static double toESRI_z(double z) {
		if (Double.isInfinite(z) || Double.isNaN(z)) {
			return ESRI_NAN;
		}
		return z;
	}

	/** Translate a z-coordinate from ESRI standard
	 * to Java standard.
	 *
	 * @param z is the Java z-coordinate
	 * @return the ESRI z-coordinate
	 */
	@Pure
	public static double fromESRI_z(double z) {
		if (isESRINaN(z)) {
			return Double.NaN;
		}
		return z;
	}

	/** Translate a M-coordinate from Java standard
	 * to ESRI standard.
	 *
	 * @param measure is the Java z-coordinate
	 * @return the ESRI m-coordinate
	 */
	@Pure
	public static double toESRI_m(double measure) {
		if (Double.isInfinite(measure) || Double.isNaN(measure)) {
			return ESRI_NAN;
		}
		return measure;
	}

	/** Translate a m-coordinate from ESRI standard
	 * to Java standard.
	 *
	 * @param meeasure is the Java m-coordinate
	 * @return the ESRI m-coordinate
	 */
	@Pure
	public static double fromESRI_m(double meeasure) {
		if (isESRINaN(meeasure)) {
			return Double.NaN;
		}
		return meeasure;
	}

	/** Replies if the given value is assumed to be NaN according to the
	 * ESRI specifications.
	 *
	 * @param value the value.
	 * @return <code>true</code> if the value corresponds to NaN, otherwise <code>false</code>.
	 */
	@Pure
	public static boolean isESRINaN(double value) {
		return Double.isInfinite(value) || Double.isNaN(value) || value <= ESRI_NAN;
	}

	/** Replies if the given value is assumed to be NaN according to the
	 * ESRI specifications.
	 *
	 * @param value the value.
	 * @return <code>true</code> if the value corresponds to NaN, otherwise <code>false</code>.
	 */
	@Pure
	public static boolean isESRINaN(float value) {
		return Float.isInfinite(value) || Float.isNaN(value) || value <= ESRI_NAN;
	}

	/** Translate a floating point value into ESRI standard.
	 *
	 * <p>This function translate the Java NaN and infinites values
	 * into the ESRI equivalent value.
	 *
	 * @param value the value.
	 * @return the ESRI value
	 */
	@Pure
	public static double toESRI(double value) {
		return (Double.isInfinite(value) || Double.isNaN(value)) ? ESRI_NAN : value;
	}

	/** Translate a floating point value into ESRI standard.
	 *
	 * <p>This function translate the Java NaN and infinites values
	 * into the ESRI equivalent value.
	 *
	 * @param value the value.
	 * @return the ESRI value
	 */
	@Pure
	public static double toESRI(float value) {
		return (Float.isInfinite(value) || Float.isNaN(value)) ? ESRI_NAN : value;
	}

	/** Translate a floating point value from ESRI standard.
	 *
	 * <p>This function translate the ESRI "no data" value into
	 * the NaN value.
	 *
	 * @param value the value.
	 * @return the Java value
	 */
	@Pure
	public static double fromESRI(double value) {
		return isESRINaN(value) ? Double.NaN : value;
	}

	/** Translate a floating point value from ESRI standard.
	 *
	 * <p>This function translate the ESRI "no data" value into
	 * the NaN value.
	 *
	 * @param value the value.
	 * @return the Java value
	 */
	@Pure
	public static float fromESRI(float value) {
		return isESRINaN(value) ? Float.NaN : value;
	}

	/**
	 * Generate a shape file index (.shx) from an ESRI shape file (.shp).
	 *
	 * @param shapeFile is the ESRI shape file.
	 * @return the index filename.
	 * @throws IOException when invalid conversion.
	 */
	@Pure
	public static URL generateShapeFileIndexFromShapeFile(File shapeFile) throws IOException {
		final File shxFile = FileSystem.replaceExtension(shapeFile, "." + ShapeFileIndexFilter.EXTENSION_SHX); //$NON-NLS-1$

		try (ShapeFileReader<Object> shpReader = new ShapeFileReader<>(shapeFile, new NullFactory())) {

			try (ShapeFileIndexWriter shxWriter = new ShapeFileIndexWriter(
					shxFile,
					shpReader.getShapeElementType(),
					shpReader.getBoundsFromHeader())) {
				int offset = shpReader.getFileReadingPosition();
				int newOffset;
				int shpLength;

				while (shpReader.read() != null) {
					newOffset = shpReader.getFileReadingPosition();
					// byte length - file header length - record header length
					shpLength = newOffset - offset;
					shxWriter.write(shpLength);
					offset = newOffset;
				}
			}
		}

		return shxFile.toURI().toURL();
	}

	/** A factory that creates nothing.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public static class NullFactory implements ElementFactory<Object> {

		/** Object replied by this factory.
		 */
		private static final Object INSTANCE = new Object();

		/** Constructor.
		 */
		NullFactory() {
			//
		}

		@Override
		public Object createPolyline(AttributeCollection provider, int shapeIndex,
				int[] parts, ESRIPoint[] points, boolean hasZ) {
			return INSTANCE;
		}

		@Override
		public Object createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
			return INSTANCE;
		}

		@Override
		public Object createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
			return INSTANCE;
		}

		@Override
		public Object createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
			return INSTANCE;
		}

		@Override
		public Object createMultiPatch(AttributeCollection provider, int shapeIndex,
				int[] parts, ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
			return INSTANCE;
		}

	}

}
