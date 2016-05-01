/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * Copyright (C) 2012-13 Stephane GALLAND, Olivier LAMOTTE.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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

package org.arakhne.afc.vmutil.locale;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * <code>LocaleMessageFormat</code> provides a means to produce concatenated
 * messages in a language-neutral way in the {@link Locale}
 * utility class.
 * <p>
 * <code>LocaleMessageFormat</code> takes a set of objects, formats them, then
 * inserts the formatted strings into the pattern at the appropriate places.
 * <p>
 * In addition to the standard JDK {@link MessageFormat}, <code>LocaleMessageFormat</code>
 * provides the <code>FormatStyle</code> named "raw". This new style does not try
 * to format the given data according to the locale. It simply put the 
 * not-formatted data in the result.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.4
 */
public class LocaleMessageFormat extends MessageFormat {

	private static final long serialVersionUID = 6637824487735941754L;
	
	/** String that corresponds to the raw format style.
	 */
	public static final String RAW_FORMAT_STYLE = "raw"; //$NON-NLS-1$

	/**
	 * Creates a LocaleMessageFormat with the given pattern and uses it
	 * to format the given arguments. This is equivalent to
	 * <blockquote>
	 *     <code>(new {@link #LocaleMessageFormat(String) MessageFormat}(pattern)).{@link #format(java.lang.Object[], java.lang.StringBuffer, java.text.FieldPosition) format}(arguments, new StringBuffer(), null).toString()</code>
	 * </blockquote>
	 *
	 * @exception IllegalArgumentException if the pattern is invalid,
	 *            or if an argument in the <code>arguments</code> array
	 *            is not of the type expected by the format element(s)
	 *            that use it.
	 */
	public static String format(String pattern, Object ... arguments) {
		LocaleMessageFormat temp = new LocaleMessageFormat(pattern);
		return temp.format(arguments);
	}

	/**
	 * @param pattern the pattern for this message format
	 * @exception IllegalArgumentException if the pattern is invalid
	 */
	public LocaleMessageFormat(String pattern) {
		super(pattern);
	}

	/**
	 * @param pattern the pattern for this message format
	 * @param locale the locale for this message format
	 * @exception IllegalArgumentException if the pattern is invalid
	 */
	public LocaleMessageFormat(String pattern, Locale locale) {
		super(pattern, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyPattern(String pattern) {
		super.applyPattern(pattern);
		Format[] formats = getFormats();
		boolean changed = false;
		for(int i=0; i<formats.length; ++i) {
			if (formats[i] instanceof DecimalFormat) {
				DecimalFormat df = (DecimalFormat)formats[i];
				if (RAW_FORMAT_STYLE.equalsIgnoreCase(df.getPositivePrefix())) {
					formats[i] = new RawNumberFormat(
							pattern,
							df.getGroupingSize(),
							df.getMinimumIntegerDigits(),
							df.getMaximumIntegerDigits(),
							df.getMinimumFractionDigits(),
							df.getMaximumFractionDigits(),
							df.getRoundingMode());
					changed = true;
				}
			}
		}
		if (changed) {
			setFormats(formats);
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class RawNumberFormat extends NumberFormat {

		private static final long serialVersionUID = 7091190928835741939L;

		private static final char RAW_NEGATIVE_SIGN = '-';
		private static final char RAW_DECIMAL_SEPARATOR = '.';
		private static final char RAW_ZERO_DIGIT = '0';
		
		private final boolean isUnformatted;
		private final RoundingMode roundingMode;
		
		/**
		 * @param pattern
		 * @param groupSize
		 * @param minInt
		 * @param maxInt
		 * @param minFrac
		 * @param maxFrac
		 * @param roundingMode
		 */
		public RawNumberFormat(String pattern, int groupSize, int minInt, int maxInt, int minFrac, int maxFrac, RoundingMode roundingMode) {
			super();
			this.roundingMode = roundingMode;
			this.isUnformatted = (groupSize==0) && (minInt==0) && (maxInt==Integer.MAX_VALUE) && (minFrac==0) && (maxFrac==0);
			setMinimumIntegerDigits(minInt);
			setMaximumIntegerDigits(maxInt);
			setMinimumFractionDigits(minFrac);
			setMaximumFractionDigits(maxFrac);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
			if (number instanceof BigInteger) {
				return format((BigInteger)number, toAppendTo, pos);
			}
			if (number instanceof BigDecimal) {
				return format((BigDecimal)number, toAppendTo, pos);
			}
			return super.format(number, toAppendTo, pos);
		}
		
		private void formatInteger(boolean negative, String number, StringBuffer toAppendTo) {
			assert(!this.isUnformatted);
			
			if (negative) toAppendTo.append(RAW_NEGATIVE_SIGN);
			
			for(int c=getMinimumIntegerDigits()-number.length(); c>0; --c) {
				toAppendTo.append(RAW_ZERO_DIGIT);
			}
			
			toAppendTo.append(number);

			int n = getMinimumFractionDigits();
			if (n>0) {
				toAppendTo.append(RAW_DECIMAL_SEPARATOR);
				for(int c=0; c<n; ++c) {
					toAppendTo.append(RAW_ZERO_DIGIT);
				}
			}
		}
	    
		private void formatDecimal(BigDecimal number, StringBuffer toAppendTo) {
			assert(!this.isUnformatted);
			
			boolean negative = (number.compareTo(BigDecimal.ZERO)<0);
			int minInt = getMinimumIntegerDigits();
			int minFrac = getMinimumFractionDigits();
			int maxFrac = getMaximumFractionDigits();
			
			BigDecimal n = number.setScale(maxFrac, this.roundingMode);
			
			String rawString = n.abs().toPlainString();
			int decimalPos = rawString.indexOf(RAW_DECIMAL_SEPARATOR);
			String integer, decimal;
			if (decimalPos<0) {
				integer = rawString;
				decimal = ""; //$NON-NLS-1$
			}
			else {
				integer = rawString.substring(0, decimalPos);
				decimal = rawString.substring(decimalPos+1);
			}
			
			if (negative) toAppendTo.append(RAW_NEGATIVE_SIGN);
			
			int c = minInt - integer.length();
			while (c>0) {
				toAppendTo.append(RAW_ZERO_DIGIT);
				--c;
			}
			
			toAppendTo.append(integer);
			
			if (minFrac>0 || (maxFrac>0 && decimal.length()>0)) {
				toAppendTo.append(RAW_DECIMAL_SEPARATOR);
				toAppendTo.append(decimal);
				
				c = minFrac - decimal.length();
				while (c>0) {
					toAppendTo.append(RAW_ZERO_DIGIT);
					--c;
				}
			}
		}
		
		/**
		 * Specialization of format.
		 * 
		 * @param number is the number to format.
		 * @param toAppendTo is the string buffer into which the formatting result may be appended.
		 * @param pos is on input: an alignment field, if desired. On output: the offsets of 
		 * 	the alignment field.
		 * @return the value passed in as <code>toAppendTo</code>
		 * @throws ArithmeticException if rounding is needed with rounding
		 *                   mode being set to RoundingMode.UNNECESSARY
		 * @see java.text.Format#format
		 */
		public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
			if (this.isUnformatted) {
				toAppendTo.append(number.toString());
			}
			else {
				formatInteger(number.signum()<0, number.abs().toString(), toAppendTo);
			}
			return toAppendTo;
		}

		/**
		 * Specialization of format.
		 * 
		 * @param number is the number to format.
		 * @param toAppendTo is the string buffer into which the formatting result may be appended.
		 * @param pos is on input: an alignment field, if desired. On output: the offsets of 
		 * 	the alignment field.
		 * @return the value passed in as <code>toAppendTo</code>
		 * @throws ArithmeticException if rounding is needed with rounding
		 *                   mode being set to RoundingMode.UNNECESSARY
		 * @see java.text.Format#format
		 */
		public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
			if (this.isUnformatted) {
				toAppendTo.append(number.toPlainString());
			}
			else {
				formatDecimal(number, toAppendTo);
			}
			return toAppendTo;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
			if (this.isUnformatted) {
				toAppendTo.append(Double.toString(number));
			}
			else {
				formatDecimal(new BigDecimal(number), toAppendTo);
			}
			return toAppendTo;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
			if (this.isUnformatted) {
				toAppendTo.append(Long.toString(number));
			}
			else {
				formatInteger(number<0, Long.toString(Math.abs(number)), toAppendTo);
			}
			return toAppendTo;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Number parse(String source, ParsePosition parsePosition) {
			throw new UnsupportedOperationException();
		}

	} // class RawNumberFormat

}