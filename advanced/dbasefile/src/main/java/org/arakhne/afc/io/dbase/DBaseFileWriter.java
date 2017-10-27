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

package org.arakhne.afc.io.dbase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.inputoutput.stream.LittleEndianDataOutputStream;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This class permits to write a dBase file.
 *
 * <p>This class supports dBASE version 5. Version 7 specification is
 * available at <a href="http://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm">http://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm</a>.
 *
 * <h3>ESRi Limitations</h3>
 *
 * <p>Additionnally, this writer includes the ESRi restrictions on dBASE files.
 * Shapefile feature attributes are stored in an associated .dbf file, and so
 * attributes suffer a number of limitations.
 *
 * <p>Attribute names can only be up
 * to 10 characters long.  Longer names will be silently truncated.
 * This may result in non-unique column names, which will definitely cause
 * problems later. Starting with version 1.7, the OGR Shapefile driver tries
 * to generate unique field names. Successive duplicate field names, including
 * those created by truncation to 10 characters, will be truncated to 8
 * characters and appended with a serial number from 1 to 99.
 *
 * <p>Only Integer, Real, String and Date (not DateTime, just year/month/day)
 * field types are supported.  The various list, and binary field types cannot
 * be created.
 *
 * <p>The field width and precision are directly used to establish storage
 * size in the .dbf file.  This means that strings longer than the field
 * width, or numbers that don't fit into the indicated field format will suffer
 * truncation.
 *
 * <p>Integer fields without an explicit width are treated as width 11.
 *
 * <p>Real (floating point) fields without an explicit width are treated as
 * width 24 with 15 decimal places of precision.
 *
 * <p>String fields without an assigned width are treated as 80 characters.
 *
 * <p>Also, .dbf files are required to have at least one field.  If none are created
 * by the application an "FID" field will be automatically created and populated
 * with the record number.
 *
 * <p>The OGR shapefile driver supports rewriting existing shapes in a shapefile
 * as well as deleting shapes.  Deleted shapes are marked for deletion in
 * the .dbf file, and then ignored by OGR.  To actually remove them permanently
 * (resulting in renumbering of FIDs) invoke the SQL 'REPACK &lt;tablename&gt;' via
 * the datasource ExecuteSQL() method.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DBaseFileWriter implements AutoCloseable {

	private LittleEndianDataOutputStream stream;

	private List<DBaseFileField> columns;

	private DBaseCodePage language = DBaseCodePage.WINDOWS_STANDARD;

	/**
	 * @param stream is the file to write.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public DBaseFileWriter(File stream) throws IOException {
		this(new FileOutputStream(stream));
	}

	/**
	 * @param stream is the file to write.
	 * @throws IOException in case of error.
	 */
	public DBaseFileWriter(URL stream) throws IOException {
		this(stream.openConnection().getOutputStream());
	}

	/**
	 * @param channel is the channel to write in.
	 */
	public DBaseFileWriter(WritableByteChannel channel) {
		this(Channels.newOutputStream(channel));
	}

	/** Constuctor.
	 * @param dbfStream is the output stream to use
	 */
	public DBaseFileWriter(OutputStream dbfStream) {
		assert dbfStream != null;
		if (dbfStream instanceof LittleEndianDataOutputStream) {
			this.stream = (LittleEndianDataOutputStream) dbfStream;
		} else {
			this.stream = new LittleEndianDataOutputStream(dbfStream);
		}
	}

	/** Replies the output language to use in the dBASE file.
	 *
	 * @return the output language to use in the dBASE file.
	 */
	public DBaseCodePage getCodePage() {
		return this.language;
	}

	/** Replies the output language to use in the dBASE file.
	 *
	 * @param code the output language to use in the dBASE file.
	 * @throws IllegalStateException if the header was already written.
	 */
	public void setCodePage(DBaseCodePage code) {
		if (this.columns != null) {
			throw new IllegalStateException();
		}
		if (code != null) {
			this.language = code;
		}
	}

	/** Write a string inside the current <var>stream</var>.
	 * Each character of the string will be written as bytes.
	 * No terminal null character is written. The
	 * string area will be filled with the given byte.
	 *
	 * @param str is the string to write
	 * @param size if the max size of the string to write. If <var>str</var>
	 *     is longer than, it is cut.
	 * @param fillingChar is the character used to fill the string area.
	 * @throws IOException in case of error.
	 */
	private void writeDBFString(String str, int size, byte fillingChar) throws IOException {
		assert this.language != null;
		// Be sure that the encoding will be the right one
		int strSize = 0;
		if (str != null) {
			final Charset encodingCharset = this.language.getChatset();
			if (encodingCharset == null) {
				throw new IOException(Locale.getString("UNKNOWN_CHARSET")); //$NON-NLS-1$
			}
			final byte[] bytes = str.getBytes(encodingCharset);
			if (bytes != null) {
				strSize = bytes.length;
				for (int i = 0; i < size && i < strSize; ++i) {
					this.stream.writeByte(bytes[i]);
				}
			}
		}

		for (int i = strSize; i < size; ++i) {
			this.stream.writeByte(fillingChar);
		}
	}

	/** Write a number inside the current <var>stream</var>.
	 *
	 * <p>A number is composed of the characters <code>[-.0123456789]</code>.
	 * The number is written to have its decimal point matching the given
	 * position. It was filled by space characters (0x20) at the left, and
	 * by <code>0</code> at the right.
	 *
	 * @param number is the number to output
	 * @param numberLength is the length, in digits, of the number to output.
	 * @param decimalLength is the position of the decimal point, <code>0</code>
	 *     means right-most position, ie. no decimal part.
	 * @throws IOException in case of error.
	 */
	private void writeDBFNumber(double number, int numberLength, int decimalLength) throws IOException {
		final StringBuilder buffer = new StringBuilder("{0,number,#"); //$NON-NLS-1$
		if (decimalLength > 0) {
			buffer.append("."); //$NON-NLS-1$
			for (int i = 0; i < decimalLength; ++i) {
				buffer.append("#"); //$NON-NLS-1$
			}
		}
		buffer.append("}"); //$NON-NLS-1$

		final MessageFormat format = new MessageFormat(buffer.toString(), java.util.Locale.ROOT);
		String formattedNumber = format.format(new Object[] {Double.valueOf(number)});

		buffer.setLength(0);
		buffer.append(formattedNumber);
		while (buffer.length() < numberLength) {
			buffer.insert(0, " "); //$NON-NLS-1$
		}
		formattedNumber = buffer.toString();

		// Write the number with the original ASCII code page
		final Charset encodingCharset = Charset.forName("IBM437"); //$NON-NLS-1$
		if (encodingCharset == null) {
			throw new IOException(Locale.getString("UNKNOWN_CHARSET")); //$NON-NLS-1$
		}
		final byte[] bytes = formattedNumber.getBytes(encodingCharset);
		if (bytes != null) {
			this.stream.write(bytes);
		}
	}

	/** Write a number inside the current <var>stream</var>.
	 *
	 * <p>A number is composed of the characters <code>[-.0123456789]</code>.
	 * The number is written to have its decimal point matching the given
	 * position. It was filled by space characters (0x20) at the left, and
	 * by <code>0</code> at the right.
	 *
	 * @param number is the number to output
	 * @param numberLength is the length, in digits, of the number to output.
	 * @param decimalLength is the position of the decimal point, <code>0</code>
	 *     means right-most position, ie. no decimal part.
	 * @throws IOException in case of error.
	 */
	private void writeDBFNumber(long number, int numberLength, int decimalLength) throws IOException {
		final StringBuilder buffer = new StringBuilder("{0,number,#}"); //$NON-NLS-1$

		final MessageFormat format = new MessageFormat(buffer.toString(), java.util.Locale.ROOT);
		String formattedNumber = format.format(new Object[] {Double.valueOf(number)});

		buffer.setLength(0);
		buffer.append(formattedNumber);
		while (buffer.length() < numberLength) {
			buffer.insert(0, " "); //$NON-NLS-1$
		}
		formattedNumber = buffer.toString();

		// Write the number with the original ASCII code page
		final Charset encodingCharset = Charset.forName("IBM437"); //$NON-NLS-1$
		if (encodingCharset == null) {
			throw new IOException(Locale.getString("UNKNOWN_CHARSET")); //$NON-NLS-1$
		}
		final byte[] bytes = formattedNumber.getBytes(encodingCharset);
		if (bytes != null) {
			this.stream.write(bytes);
		}
	}

	/** Write a formated boolean according to the Dbase specifications.
	 *
	 * @param bool is the value to convert
	 * @throws IOException in case of error.
	 */
	private void writeDBFBoolean(boolean bool) throws IOException {
		this.stream.writeByte(bool ? 'T' : 'F');
	}

	/** Write a formated date according to the Dbase specifications.
	 *
	 * @param date is the value to convert
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	private void writeDBFDate(Date date) throws IOException {
		final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
		writeDBFString(format.format(date), 8, (byte) ' ');
	}

	/** Write a formated integer (2 bytes) according to the Dbase specifications.
	 *
	 * @param value is the value to convert
	 * @throws IOException in case of error.
	 */
	private void writeDBFInteger(short value) throws IOException {
		this.stream.writeLEShort(value);
	}

	/** Write a formated long integer (4 bytes) according to the dBASE specifications.
	 *
	 * @param value is the value to convert
	 * @throws IOException in case of error.
	 */
	private void writeDBFLong(int value) throws IOException {
		this.stream.writeLEInt(value);
	}

	/** Write a formated double (8 bytes) according to the dBASE specifications.
	 *
	 * @param value is the value to convert
	 * @throws IOException in case of error.
	 */
	private void writeDBFDouble(double value) throws IOException {
		this.stream.writeLEDouble(value);
	}

	/** Replies the size of the DBase field which must contains the
	 * value of the given attribute value.
	 *
	 * @return the size of the field in bytes
	 * @throws DBaseFileException if the Dbase file cannot be read.
	 * @throws AttributeException  if an attribute is invalid.
	 */
	private static int computeFieldSize(AttributeValue value) throws DBaseFileException, AttributeException {
		final DBaseFieldType dbftype = DBaseFieldType.fromAttributeType(value.getType());
		return dbftype.getFieldSize(value.getString());
	}

	/** Replies the decimal size of the DBase field which must contains the
	 * value of the given attribute value.
	 *
	 * @throws DBaseFileException if the Dbase file cannot be read.
	 * @throws AttributeException  if an attribute is invalid.
	 */
	private static int computeDecimalSize(AttributeValue value) throws DBaseFileException, AttributeException {
		final DBaseFieldType dbftype = DBaseFieldType.fromAttributeType(value.getType());
		return dbftype.getDecimalPointPosition(value.getString());
	}

	/** Replies if the given dBASE type is supported by this writer.
	 *
	 * @param type the type of the field.
	 * @return <code>true</code> if the given type is supported by the writer,
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	public static boolean isSupportedType(DBaseFieldType type) {
		return	(type != DBaseFieldType.BINARY)
				&& (type != DBaseFieldType.GENERAL)
				&& (type != DBaseFieldType.MEMORY)
				&& (type != DBaseFieldType.PICTURE)
				&& (type != DBaseFieldType.VARIABLE);
	}

	/** Extract the attribute's columns from the attributes providers.
	 *
	 * @param providers is the list of attribute providers that will be used for the dbf writing.
	 * @return an map in which each key is the attirbute's name and the corresponding
	 *     value is the size of the field.
	 *
	 * @throws DBaseFileException if the Dbase file cannot be read.
	 * @throws AttributeException  if an attribute is invalid.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	private List<DBaseFileField> extractColumns(List<? extends AttributeProvider> providers)
			throws DBaseFileException, AttributeException {
		final Map<String, DBaseFileField> attributeColumns = new TreeMap<>();
		DBaseFileField oldField;
		String name;
		String kname;
		int fieldSize;
		int decimalSize;

		final Set<String> skippedColumns = new TreeSet<>();

		for (final AttributeProvider provider : providers) {
			for (final Attribute attr : provider.attributes()) {
				final DBaseFieldType dbftype = DBaseFieldType.fromAttributeType(attr.getType());
				// The following types are not yet supported
				if (isSupportedType(dbftype)) {
					name = attr.getName();
					kname = name.toLowerCase();
					oldField = attributeColumns.get(kname);

					// compute the sizes
					fieldSize = computeFieldSize(attr);
					decimalSize = computeDecimalSize(attr);

					if (oldField == null) {
						oldField = new DBaseFileField(
								name,
								dbftype,
								fieldSize,
								decimalSize);
						attributeColumns.put(kname, oldField);
					} else {
						oldField.updateSizes(fieldSize, decimalSize);
					}
				} else {
					skippedColumns.add(attr.getName().toUpperCase());
				}
			}

			// Be sure that the memory was not fully filled by the saving process
			provider.freeMemory();
		}

		//Max of field allowed : 128
		if (attributeColumns.size() > 128) {
			throw new TooManyDBaseColumnException(attributeColumns.size(), 128);
		}

		final List<DBaseFileField> dbColumns = new ArrayList<>();
		dbColumns.addAll(attributeColumns.values());
		attributeColumns.clear();

		for (int idx = 0; idx < dbColumns.size(); ++idx) {
			dbColumns.get(idx).setColumnIndex(idx);
		}

		if (!skippedColumns.isEmpty()) {
			columnsSkipped(skippedColumns);
		}

		return dbColumns;
	}

	/** Invoked each time a column was skipped by the writer because it does
	 * not support its output.
	 *
	 * @param skippedColumns are the skipped columns.
	 * @since 4.0
	 */
	protected void columnsSkipped(Set<String> skippedColumns) {
		//
	}

	/** Write the header of the DBF file on the main <var>stream</var>.
	 *
	 * <p><pre>
	 * -----------------------------------------------------------
	 * 			DBF Header (32 bytes)
	 * -----------------------------------------------------------
	 *  Bytes       Size   Content
	 * -----------------------------------------------------------
	 *       0     1 byte    DBF Format id
	 *                       0x03: FoxBase+, FoxPro, dBASEIII+
	 *                             dBASEIV, no memo
	 *                       0x83: FoxBase+, dBASEIII+ with memo
	 *                       0xF5: FoxPro with memo
	 *                       0x8B: dBASEIV with memo
	 *                       0x8E: dBASEIV with SQL table
	 *     1-3     3 bytes   Date of last update: YMD
	 *     4-7     4 bytes   Number of records in the table
	 *     8-9     2 bytes   Number of bytes in the header
	 *   10-11     2 bytes   Number of bytes in the record
	 *   12-13     2 bytes   Reserved
	 *      14     1 byte    Incomplete transaction
	 *                       0x00: Ignored / Transaction End
	 *                       0x01: Transaction started
	 *      15     1 byte    Encryption flag
	 *                       0x00: Not encrypted
	 *                       0x01: Encrypted
	 *   16-19     4 bytes   Free record thread (reserved for LAN only)
	 *   20-27     8 bytes   Reserved for multi-user dBASE (dBASE III+)
	 *      28     1 byte    MDX flag (dBASE IV)
	 *                       0x00: index upon demand
	 *                       0x01: production index exists
	 *      29     1 byte    Language driver ID
	 *                       See {@link DBaseCodePage} for details.
	 *    30-31    2 bytes   Reserved
	 * ----------------------------------------------------------
	 * </pre>
	 *
	 * @param recordCount is the count of record which will be written.
	 * @param language is the language of the file.
	 * @throws IOException in case of errors.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	private void writeDescriptionHeader(int recordCount) throws IOException {
		assert this.language != null;

		// DBF Format id (0)
		this.stream.writeByte(0x03);

		// Date of last update (1-3)
		final Calendar date = Calendar.getInstance();
		this.stream.writeByte(date.get(Calendar.YEAR) - 1900);
		this.stream.writeByte(date.get(Calendar.MONTH) + 1);
		this.stream.writeByte(date.get(Calendar.DAY_OF_MONTH));

		// Number of records in the table (4-7)
		this.stream.writeLEInt(recordCount);

		// Number of bytes in the header (8-9)
		// Each field description is stored in 32bits (see writeColumnHeaders())
		final int columnHeaderSize = this.columns.size() * 32;
		final int headerSize =
				// size of this header part
				32
				// size of the column headers
				+ columnHeaderSize
				// final character
				+ 1;
		this.stream.writeLEShort((short) headerSize);

		// Number of bytes one record (10-11)
		// deletion flag
		int recordSize = 1;
		for (final DBaseFileField field : this.columns) {
			recordSize += field.getLength();
		}
		this.stream.writeLEShort((short) recordSize);

		// Reserved (12-31)
		//(0Ch) : Reserved
		this.stream.writeByte(0x00);
		//(0Dh) : Reserved
		this.stream.writeByte(0x00);
		//(0Eh) : Incomplete transaction
		this.stream.writeByte(0x00);
		//(0Fh) : Encryption flag
		this.stream.writeByte(0x00);
		//(10h - 13h) : Free record thread (reserved for LAN only)
		this.stream.writeLEInt(0);
		//(14h - 17h) : Reserved for multi-user dBASE (dBASE III+ -)
		this.stream.writeLEInt(0);
		//(18h - 1Bh) : Reserved for multi-user dBASE (dBASE III+ -)
		this.stream.writeLEInt(0);
		//(1Ch) : MDX flag (dBASE IV)
		this.stream.writeByte(0x00);
		//(1Dh) : Language driver
		this.stream.writeByte(this.language.getLanguageCode());
		//(1Eh) : Reserved
		this.stream.writeByte(0x00);
		//(1Fh) : Reserved
		this.stream.writeByte(0x00);
	}

	/** Write the fields definition after the header on the main <var>stream</var>.
	 *
	 * <p>The header must be finished by the character <code>0Dh</code>.
	 *
	 * <p><pre>
	 * -----------------------------------------------------------
	 * 			Field Header (n*32+1 bytes)
	 * -----------------------------------------------------------
	 *  Bytes       Size   Content
	 * -----------------------------------------------------------
	 *  32-m  n*32 bytes   Field descriptors (see bellow)
	 *   m+1     1 byte    terminator character 0x0D
	 * -----------------------------------------------------------
	 * </pre>
	 *
	 * <p><pre>
	 * -----------------------------------------------------------
	 * 			Field Header (32 bytes)
	 * -----------------------------------------------------------
	 *  Bytes       Size   Content
	 * -----------------------------------------------------------
	 *   0-10   11 bytes   Field name, filled with 0x00
	 *     11    1 byte    Field type (see bellow)
	 *  12-15    4 bytes   Field data address, not useful for disk
	 *     16    1 byte    Field length
	 *     17    1 byte    Field decimal count
	 *  18-19    2 bytes   Reserved for dBASE III+ on a Lan
	 *     20    1 byte    Work area ID
	 *  21-22    2 bytes   Reserved for dBASE III+ on a Lan
	 *     23    1 byte    SET FIELDS flag
	 *  24-31    7 bytes   Reserved
	 * -----------------------------------------------------------
	 * </pre>
	 *
	 * <p><pre>
	 * -----------------------------------------------------------
	 * 			Field Type (1 byte)
	 * -----------------------------------------------------------
	 *  Char. Size    Type     Content
	 * -----------------------------------------------------------
	 *  C     1..n    STRING   ASCII fill with spaces (not final
	 *                         \0 character).
	 *                         n=64kb (using deci. counter)
	 *                         n=32kb (using deci. counter)
	 *                         n=254
	 *  D        8    DATE     [0-9]{8} with in format YYYYMMDD
	 *  F     1..n    NUMERIC  [-.0-9]+, variable position
	 *                         of floating position
	 *  N     1..n    NUMERIC  [-.0-9]+, fixed position,
	 *                         no floating position
	 *  L        1    LOGICAL  [TtFfYyNn ?]
	 *  M       10    MEMORY   10 digits representing the start
	 *                         block position in the .dbt file,
	 *                         or 10 spaces if no entry in memo
	 *  V       10    VARIABLE data in .dbv file,
	 *                         4 bytes: start pos in memory
	 *                         4 bytes: block size
	 *                         1 byte:  subtype
	 *                         1 byte:  reserved (0x1A)
	 *                         10 spaces if no entry in .dbv
	 *  P       10    PICTURE  binary data in .ftp file (same
	 *                         structure as M type)
	 *  B       10    BINARY   binary data in .dbt file (same
	 *                         structure as M type)
	 *  G       10    GENERAL  OLE objects (same
	 *                         structure as M type)
	 *  2        2    NUMERIC  short integer +/- 32762
	 *  4        4    NUMERIC  long integer +/- 2147483647
	 *  8        8    NUMERIC  signed double IEEE
	 * -----------------------------------------------------------
	 * </pre>
	 *
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	private void writeColumns() throws IOException {

		for (final DBaseFileField field : this.columns) {
			// Field name, filled with 0x00 (0-10)
			writeDBFString(field.getName(), 10, (byte) 0x00);
			this.stream.writeByte(0x00);

			// Field type (11)
			this.stream.writeByte(field.getType().toByte());

			// Field data address, not useful for disk (12-15)
			this.stream.write(new byte[]{0, 0, 0, 0});

			// Field length (16)
			this.stream.writeByte(field.getLength());

			// Field decimal count (17)
			this.stream.writeByte(field.getDecimalPointPosition());

			// Reserved (18-31)
			for (int i = 18; i <= 31; ++i) {
				this.stream.writeByte(0);
			}
		}

		// Terminator after the field description (value 0Dh)
		this.stream.writeByte(0x0D);
	}

	/** Write the header of the dbf file.
	 *
	 * @param providers is the list of attribute providers that will be used for the dbf writing.
	 * @throws AttributeException if an attribute cannot pre set.
	 * @throws IOException in case of IO error.
	 */
	public void writeHeader(AttributeProvider... providers) throws IOException, AttributeException {
		writeHeader(Arrays.asList(providers));
	}

	/** Write the header of the dbf file.
	 *
	 * @param providers is the list of attribute providers that will be used for the dbf writing.
	 * @throws AttributeException if an attribute cannot pre set.
	 * @throws IOException in case of IO error.
	 */
	public void writeHeader(List<? extends AttributeProvider> providers) throws IOException, AttributeException {
		if (this.columns == null) {
			this.columns = extractColumns(providers);
			writeDescriptionHeader(providers.size());
			writeColumns();
		}
	}

	/** Write a record for attribute provider.
	 *
	 * @param element is the element to write.
	 * @throws AttributeException if an attribute cannot pre set.
	 * @throws IOException in case of IO error.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:magicnumber"})
	public void writeRecord(AttributeProvider element) throws IOException, AttributeException {

		if (this.columns == null) {
			throw new MustCallWriteHeaderFunctionException();
		}

		//Field deleted flag (value : 2Ah (*) => Record is deleted, 20h (blank) => Record is valid)
		this.stream.writeByte(0x20);

		for (final DBaseFileField field : this.columns) {
			// Get attribute
			final DBaseFieldType dbftype = field.getType();
			final String fieldName = field.getName();

			AttributeValue attr = element != null ? element.getAttribute(fieldName) : null;

			if (attr == null) {
				attr = new AttributeValueImpl(dbftype.toAttributeType());
				attr.setToDefault();
			} else {
				if (attr.isAssigned()) {
					attr = new AttributeValueImpl(attr);
					attr.cast(dbftype.toAttributeType());
				} else {
					attr = new AttributeValueImpl(attr);
					attr.setToDefaultIfUninitialized();
				}
			}

			// Write value
			switch (dbftype) {
			case BOOLEAN:
				writeDBFBoolean(attr.getBoolean());
				break;
			case DATE:
				writeDBFDate(attr.getDate());
				break;
			case STRING:
				writeDBFString(attr.getString(), field.getLength(), (byte) ' ');
				break;
			case INTEGER_2BYTES:
				writeDBFInteger((short) attr.getInteger());
				break;
			case INTEGER_4BYTES:
				writeDBFLong((int) attr.getInteger());
				break;
			case DOUBLE:
				writeDBFDouble(attr.getReal());
				break;
			case FLOATING_NUMBER:
			case NUMBER:
				if (attr.getType() == AttributeType.REAL) {
					writeDBFNumber(attr.getReal(), field.getLength(), field.getDecimalPointPosition());
				} else {
					writeDBFNumber(attr.getInteger(), field.getLength(), field.getDecimalPointPosition());
				}
				break;
			case BINARY:
			case GENERAL:
			case MEMORY:
			case PICTURE:
			case VARIABLE:
				// not yet supported
				writeDBFString((String) null, 10, (byte) 0);
				break;
			default:
				throw new IllegalStateException();
			}
		}

		// Be sure that the memory was not fully filled by the saving process
		if (element != null) {
			element.freeMemory();
		}
	}

	/** Write the DBase file.
	 *
	 * <p>A call to this method is equivalent to the sequence of calls:
	 * <ul>
	 * <li>{@link #writeHeader(AttributeProvider[])}</li>
	 * <li>foreach(providers) {@link #writeRecord(AttributeProvider)}</li>
	 * <li>{@link #close()}</li>
	 * </ul>
	 *
	 * @param providers are the attribute container that must be written inside the dBASE file.
	 * @throws AttributeException if an attribute cannot pre set.
	 * @throws IOException in case of IO error.
	 */
	public void write(AttributeProvider... providers) throws IOException, AttributeException {
		writeHeader(providers);

		for (final AttributeProvider provider : providers) {
			writeRecord(provider);
		}

		close();
	}

	/** Write the DBase file.
	 *
	 * <p>A call to this method is equivalent to the sequence of calls:
	 * <ul>
	 * <li>{@link #writeHeader(AttributeProvider[])}</li>
	 * <li>foreach(providers) {@link #writeRecord(AttributeProvider)}</li>
	 * <li>{@link #close()}</li>
	 * </ul>
	 *
	 * @param providers are the attribute container that must be written inside the dBASE file.
	 * @throws AttributeException if an attribute cannot pre set.
	 * @throws IOException in case of IO error.
	 */
	public void write(List<? extends AttributeProvider> providers) throws IOException, AttributeException {
		writeHeader(providers);

		for (final AttributeProvider provider : providers) {
			writeRecord(provider);
		}

		close();
	}

	/** Close the dBASE file.
	 *
	 * <p>This function writes the end of file character (1Ah).
	 *
	 * @throws IOException in case of IO error.
	 */
	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void close() throws IOException {
		if (this.stream != null) {
			//End of file (1Ah)
			this.stream.write(0x1A);
			this.stream.close();
			this.stream = null;
		}

		if (this.columns != null) {
			this.columns.clear();
			this.columns = null;
		}
	}

}
