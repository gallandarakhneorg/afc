/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.text;

import java.net.InetSocketAddress;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.crypto.provider.SunJCE;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Tools for encryption.
 *
 * <p>Theoretically, MD5 and SHA1 are algorithms for computing a
 * 'condensed representation' of a message or a data file.
 * The 'condensed representation' is of fixed length and is known
 * as a 'message digest' or 'fingerprint'.
 *
 * <p>In both cases of MD5 and SHA, the fingerprint (message digest) is also
 * non-reversable.... your data cannot be retrieved from
 * the message digest, yet as stated earlier,
 * the digest uniquely identifies the data.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("restriction")
public final class Encryption {

	private static final Key CRYPT_KEY;

	static {
		// Be sure that the cryptographical algorithms are loaded
		loadDefaultEncryptionModule();

		// Create the crypting key
		Key kkey;
		try {
			final Properties p = System.getProperties();
			final String seed =	p.getProperty("user.name") //$NON-NLS-1$
					+ '@'
					+ new InetSocketAddress(0).getHostName()
					+ ":jdk_" //$NON-NLS-1$
					+ p.getProperty("java.version") //$NON-NLS-1$
					+ ":os_" //$NON-NLS-1$
					+ p.getProperty("os.name") //$NON-NLS-1$
					+ '-'
					+ p.getProperty("os.version"); //$NON-NLS-1$
			final byte[] original = md5(seed).getBytes("UTF8"); //$NON-NLS-1$

			final byte[] bkey = new byte[DESKeySpec.DES_KEY_LEN];
			for (int i = 0; i < DESKeySpec.DES_KEY_LEN; ++i) {
				bkey[i] = original[i % original.length];
			}

			kkey = new SecretKeySpec(
					bkey,
					"DES"); //$NON-NLS-1$
		} catch (Exception e) {
			throw new Error(e);
		}
		CRYPT_KEY = kkey;
		kkey = null;
	}

	private Encryption() {
		//
	}

	/** Load the default encryption module.
	 *
	 * <p>By default this is the SunJCE module.
	 */
	public static void loadDefaultEncryptionModule() {
		// Be sure that the cryptographical algorithms are loaded
		final Provider[] providers = Security.getProviders();
		boolean found = false;
		for (final Provider provider : providers) {
			if (provider instanceof SunJCE) {
				found = true;
				break;
			}
		}
		if (!found) {
			Security.addProvider(new SunJCE());
		}
	}

	/**
	 * Replies a MD5 key.
	 *
	 * <p>MD5 was developed by Professor Ronald L. Rivest in 1994.
	 * Its 128 bit (16 byte) message digest makes it a
	 * faster implementation than SHA-1.
	 *
	 * <p>The fingerprint (message digest) is
	 * non-reversable.... your data cannot be retrieved from
	 * the message digest, yet as stated earlier,
	 * the digest uniquely identifies the data.
	 *
	 * @param str is the string to encrypt.
	 * @return the MD5 encryption of the string <var>str</var>
	 */
	public static String md5(String str) {
		if (str == null) {
			return "";  //$NON-NLS-1$
		}
		final byte[] uniqueKey = str.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new Error(Locale.getString("NO_MD5")); //$NON-NLS-1$
		}

		final StringBuilder hashString = new StringBuilder();

		for (int i = 0; i < hash.length; ++i) {
			final String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}

	/**
	 * Replies a SHA key.
	 *
	 * <p>The Secure Hash Algorithm (SHA) was developed by NIST and is
	 * specified in the Secure Hash Standard (SHS, FIPS 180).
	 * SHA-1 is a revision to this version and was published in 1994.
	 * It is also described in the ANSI X9.30 (part 2) standard.
	 * SHA-1 produces a 160-bit (20 byte) message digest. Although
	 * slower than MD5, this larger digest size makes it stronger
	 * against brute force attacks.
	 *
	 * <p>The fingerprint (message digest) is
	 * non-reversable.... your data cannot be retrieved from
	 * the message digest, yet as stated earlier,
	 * the digest uniquely identifies the data.
	 *
	 * @param str is the string to encrypt.
	 * @return the SHA encryption of the string <var>str</var>
	 */
	public static String sha(String str) {
		if (str == null) {
			return "";  //$NON-NLS-1$
		}
		final byte[] uniqueKey = str.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("SHA").digest(uniqueKey); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new Error(Locale.getString("NO_SHA")); //$NON-NLS-1$
		}

		final StringBuilder hashString = new StringBuilder();

		for (int i = 0; i < hash.length; ++i) {
			final String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}

	/** Encrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to encrypt
	 * @return the encryption result
	 */
	public static String encrypt(String str) {
		return encrypt(str, CRYPT_KEY);
	}

	/** Encrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to encrypt
	 * @param encrypting_key is the key used for encryption.
	 * @return the encryption result or the empty string
	 */
	public static String encrypt(String str, Key encrypting_key) {
		try {
			// Get the data to encrypt
			final byte[] inputBytes = str.getBytes("UTF8"); //$NON-NLS-1$

			// Encode
			final byte[] outputBytes = encrypt(inputBytes, encrypting_key);

			// Recode on base 64
			final String base64 = new String(Base64.getEncoder().encode(outputBytes));

			return base64;
		} catch (Exception e) {
			return ""; //$NON-NLS-1$
		}
	}

	/** Encrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to encrypt
	 * @return the encryption result
	 */
	public static byte[] encrypt(byte[] str) {
		return encrypt(str, CRYPT_KEY);
	}

	/** Encrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to encrypt
	 * @param encrypting_key is the key used for encryption.
	 * @return the encryption result or the empty string
	 */
	public static byte[] encrypt(byte[] str, Key encrypting_key) {
		return encrypt(str, encrypting_key, "DES"); //$NON-NLS-1$
	}

	/** Encrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to encrypt
	 * @param encrypting_key is the key used for encryption.
	 * @param algorithm is the name of the encryption algorithm.
	 * @return the encryption result or the empty string
	 */
	public static byte[] encrypt(byte[] str, Key encrypting_key, String algorithm) {
		try {
			// Initialize the encoder
			final Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, encrypting_key);

			// Encode
			return cipher.doFinal(str);
		} catch (Exception e) {
			return null;
		}
	}

	/** Decrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to decrypt
	 * @return the decryption result
	 */
	public static String decrypt(String str) {
		return decrypt(str, CRYPT_KEY);
	}

	/** Decrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to decrypt
	 * @param decrypting_key is the key used for decryption.
	 * @return the decryption result or the empty string
	 */
	public static String decrypt(String str, Key decrypting_key) {
		try {
			// Decode on base 64
			final byte[] inputBytes = Base64.getDecoder().decode(str);

			// Decode
			final byte[] outputBytes = decrypt(inputBytes, decrypting_key);

			return new String(outputBytes, "UTF8"); //$NON-NLS-1$
		} catch (Exception e) {
			return ""; //$NON-NLS-1$
		}
	}

	/** Decrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to decrypt
	 * @return the decryption result
	 */
	public static byte[] decrypt(byte[] str) {
		return decrypt(str, CRYPT_KEY);
	}

	/** Decrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to decrypt
	 * @param decrypting_key is the key used for decryption.
	 * @return the decryption result or the empty string
	 */
	public static byte[] decrypt(byte[] str, Key decrypting_key) {
		return decrypt(str, decrypting_key, "DES"); //$NON-NLS-1$
	}

	/** Decrypt the specified string according to the given key.
	 *
	 * <p>Caution: this function use some Sun's JDK classes.
	 *
	 * <p>The DES algorithm is used.
	 *
	 * @param str is the string to decrypt
	 * @param decrypting_key is the key used for decryption.
	 * @param algorithm is the name of the decryption algorithm
	 * @return the decryption result or the empty string
	 */
	public static byte[] decrypt(byte[] str, Key decrypting_key, String algorithm) {
		try {
			// Initialize the decoder
			final Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, decrypting_key);

			// Decode
			return cipher.doFinal(str);
		} catch (Exception e) {
			return null;
		}
	}

}
