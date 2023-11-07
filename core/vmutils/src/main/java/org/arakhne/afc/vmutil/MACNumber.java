/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.vmutil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/** A MACNumber is the unique number associated to a network interface.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.2
 * @see NetworkInterface
 */
public final class MACNumber {

	/**
	 * Character that is used as separator inside MAC addresses.
	 */
	public static final char MACNUMBER_SEPARATOR = '/';

	/** Constant ethernet address object which has the "null address".
	 *
	 * <p>This constant can be used when you want a non-null
	 * MACAddress object reference, but want a invalid (or null)
	 * MAC address contained.
	 *
	 * @see #isNull()
	 */
	public static final MACNumber NULL = new MACNumber();

	/**
	 * Content of the MAC address.
	 */
	private final byte[] bytes;


	/** Constructs object with "null values" (address of "0:0:0:0:0:0").
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public MACNumber() {
		this.bytes = new byte[6];
	}

	/** Build a MACNumber from a set of bytes.
	 * The byte array must contains 6 elements.
	 *
	 * @param bytes is the list of bytes from which the address must be built.
	 * @throws IllegalArgumentException if the byte array does not corresponds to a valid MAC Number.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public MACNumber(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		this.bytes = new byte[6];
		System.arraycopy(bytes, 0, this.bytes, 0, 6);
	}

	/** Build a MACNumber from a string representation.
	 *
	 * @param address is the string representation of a MAC address
	 * @throws IllegalArgumentException if the byte array does not corresponds to a valid MAC Number.
	 * @see #toString()
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public MACNumber(String address) {
		if (address == null) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		final String[] parts = address.split(Pattern.quote(":")); //$NON-NLS-1$
		if (parts.length != 6) {
			throw new IllegalArgumentException(Locale.getString("E1")); //$NON-NLS-1$
		}
		this.bytes = new byte[6];
		try {
			int val;
			for (int i = 0; i < 6; ++i) {
				val = Integer.parseInt(parts[i], 16);
				this.bytes[i] = (byte) val;
			}
		} catch (Exception exception) {
			throw new IllegalArgumentException(Locale.getString("E1"), exception); //$NON-NLS-1$
		}
	}

	/** Parse the specified string an repleis the corresponding MAC numbers.
	 *
	 * @param addresses is the string to parse
	 * @return a list of addresses.
	 * @throws IllegalArgumentException is the argument has not the right syntax.
	 */
	@Pure
	public static MACNumber[] parse(String addresses) {
		if ((addresses == null) || ("".equals(addresses))) { //$NON-NLS-1$
			return new MACNumber[0];
		}
		final String[] adrs = addresses.split(Pattern.quote(Character.toString(MACNUMBER_SEPARATOR)));
		final List<MACNumber> list = new ArrayList<>();
		for (final String adr : adrs) {
			list.add(new MACNumber(adr));
		}
		final MACNumber[] tab = new MACNumber[list.size()];
		list.toArray(tab);
		list.clear();
		return tab;
	}

	/** Parse the specified string an repleis the corresponding MAC numbers.
	 *
	 * @param addresses is the string to parse
	 * @return a list of addresses.
	 * @throws IllegalArgumentException is the argument has not the right syntax.
	 */
	@Pure
	public static String[] parseAsString(String addresses) {
		if ((addresses == null) || ("".equals(addresses))) { //$NON-NLS-1$
			return new String[0];
		}
		final String[] adrs = addresses.split(Pattern.quote(Character.toString(MACNUMBER_SEPARATOR)));
		final List<String> list = new ArrayList<>();
		for (final String adr : adrs) {
			list.add(new MACNumber(adr).toString());
		}
		final String[] tab = new String[list.size()];
		list.toArray(tab);
		list.clear();
		return tab;
	}

	/** Join the specified MAC numbers to reply a string.
	 *
	 * @param addresses is the list of mac addresses to join.
	 * @return the joined string.
	 */
	@Pure
	public static String join(MACNumber... addresses) {
		if ((addresses == null) || (addresses.length == 0)) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		for (final MACNumber number : addresses) {
			if (buf.length() > 0) {
				buf.append(MACNUMBER_SEPARATOR);
			}
			buf.append(number);
		}
		return buf.toString();
	}

	/** Get all of the ethernet addresses associated with the local machine.
	 *
	 * <p>This method will try and find ALL of the ethernet adapters
	 * which are currently available on the system. This is heavily OS
	 * dependent and may not be supported on all platforms. When not
	 * supported, you should still get back a collection with the {@link
	 * #getPrimaryAdapter primary adapter} in it.
	 *
	 * @return the list of MAC numbers associated to the physical devices.
	 * @see #getPrimaryAdapter
	 */
	@Pure
	public static Collection<MACNumber> getAllAdapters() {
		final List<MACNumber> av = new ArrayList<>();
		final Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException exception) {
			return av;
		}
		if (interfaces != null) {
			NetworkInterface inter;
			while (interfaces.hasMoreElements()) {
				inter = interfaces.nextElement();
				try {
					final byte[] addr = inter.getHardwareAddress();
					if (addr != null) {
						av.add(new MACNumber(addr));
					}
				} catch (SocketException exception) {
					// Continue to the next loop.
				}
			}
		}
		return av;
	}

	/** Get all of the internet address and ethernet address mappings
	 * on the local machine.
	 *
	 * <p>This method will try and find ALL of the ethernet adapters
	 * which are currently available on the system. This is heavily OS
	 * dependent and may not be supported on all platforms. When not
	 * supported, you should still get back a collection with the {@link
	 * #getPrimaryAdapterAddresses primary adapter} in it.
	 *
	 * @return the map internet address and ethernet address mapping.
	 * @see #getPrimaryAdapterAddresses
	 */
	@Pure
	public static Map<InetAddress, MACNumber> getAllMappings() {
		final Map<InetAddress, MACNumber> av = new HashMap<>();
		final Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException exception) {
			return av;
		}
		if (interfaces != null) {
			NetworkInterface inter;
			MACNumber mac;
			InetAddress inet;
			while (interfaces.hasMoreElements()) {
				inter = interfaces.nextElement();
				try {
					final byte[] addr = inter.getHardwareAddress();
					if (addr != null) {
						mac = new MACNumber(addr);
						final Enumeration<InetAddress> inets = inter.getInetAddresses();
						while (inets.hasMoreElements()) {
							inet = inets.nextElement();
							av.put(inet, mac);
						}
					}
				} catch (SocketException exception) {
					// Continue to the next loop.
				}
			}
		}
		return av;
	}

	/** Try to determine the primary ethernet address of the machine.
	 *
	 * @return the primary MACNumber or {@code null}
	 */
	@Pure
	public static MACNumber getPrimaryAdapter() {
		final Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException exception) {
			return null;
		}
		if (interfaces != null) {
			NetworkInterface inter;
			while (interfaces.hasMoreElements()) {
				inter = interfaces.nextElement();
				try {
					final byte[] addr = inter.getHardwareAddress();
					if (addr != null) {
						return new MACNumber(addr);
					}
				} catch (SocketException exception) {
					// Continue to the next loop.
				}
			}
		}
		return null;
	}

	/** Try to determine the primary ethernet address of the machine and
	 * replies the associated internet addresses.
	 *
	 * @return the internet addresses of the primary network interface.
	 */
	@Pure
	public static Collection<InetAddress> getPrimaryAdapterAddresses() {
		final Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException exception) {
			return Collections.emptyList();
		}
		if (interfaces != null) {
			NetworkInterface inter;
			while (interfaces.hasMoreElements()) {
				inter = interfaces.nextElement();
				try {
					final byte[] addr = inter.getHardwareAddress();
					if (addr != null) {
						final Collection<InetAddress> inetList = new ArrayList<>();
						final Enumeration<InetAddress> inets = inter.getInetAddresses();
						while (inets.hasMoreElements()) {
							inetList.add(inets.nextElement());
						}
						return inetList;
					}
				} catch (SocketException exception) {
					// Continue to the next loop.
				}
			}
		}
		return Collections.emptyList();
	}

	@Override
	@Pure
	public boolean equals(Object object) {
		if (!(object instanceof MACNumber)) {
			return false;
		}

		final byte[] bao = ((MACNumber) object).bytes;
		if (bao.length != this.bytes.length) {
			return false;
		}

		for (int i = 0; i < bao.length; ++i) {
			if (bao[i] != this.bytes[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Pure
	public int hashCode() {
		final int blen = this.bytes.length;
		int hash = 1;
		for (int i = 0; i < blen; ++i) {
			hash = 31 * hash +  Byte.hashCode(this.bytes[i]);
		}
		return hash ^ (hash >> 31);
	}

	@SuppressWarnings("checkstyle:magicnumber")
	@Override
	@Pure
	public String toString() {
		final int blen = this.bytes.length;
		final StringBuilder sb = new StringBuilder(blen * 3);
		for (int i = 0; i < blen; ++i) {
			int lo = this.bytes[i];
			final int hi = (lo >> 4) & 0xF;
			lo &= 0xF;
			if (i != 0) {
				sb.append(':');
			}
			sb.append(Character.forDigit(hi, 16));
			sb.append(Character.forDigit(lo, 16));
		}
		return sb.toString();
	}

	/** Replies if all the MAC address number are equal to zero.
	 *
	 * @return {@code true} if all the bytes are zero.
	 * @see #NULL
	 */
	@Pure
	public boolean isNull() {
		for (int i = 0; i < this.bytes.length; ++i) {
			if (this.bytes[i] != 0) {
				return false;
			}
		}
		return true;
	}

	/** Replies the bytes that compose this MAC Address.
	 *
	 * @return a copy of the current bytes.
	 */
	@Pure
	public byte[] getBytes() {
		return this.bytes.clone();
	}

}

