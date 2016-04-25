/* 
 * $Id$
 * 
 * Copyright (C) 2005-2010 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.vmutil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/** A MACNumber is the unique number associated to a network interface.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see NetworkInterface
 * @since 4.2
 */
public final class MACNumber {
	
	/**
	 * Character that is used as separator inside MAC addresses.
	 */
	public static final char MACNUMBER_SEPARATOR = '/';

	/** Parse the specified string an repleis the corresponding MAC numbers.
	 * 
	 * @param addresses is the string to parse
	 * @return a list of addresses.
	 * @throws IllegalArgumentException is the argument has not the right syntax. 
	 */
	public static MACNumber[] parse(String addresses) {
		if ((addresses==null)||("".equals(addresses))) return new MACNumber[0]; //$NON-NLS-1$
		String[] adrs = addresses.split(Pattern.quote(Character.toString(MACNUMBER_SEPARATOR)));
		ArrayList<MACNumber> list = new ArrayList<>();
		for (String adr : adrs) {
			list.add(new MACNumber(adr));
		}
		MACNumber[] tab = new MACNumber[list.size()];
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
	public static String[] parseAsString(String addresses) {
		if ((addresses==null)||("".equals(addresses))) return new String[0]; //$NON-NLS-1$
		String[] adrs = addresses.split(Pattern.quote(Character.toString(MACNUMBER_SEPARATOR)));
		ArrayList<String> list = new ArrayList<>();
		for (String adr : adrs) {
			list.add(new MACNumber(adr).toString());
		}
		String[] tab = new String[list.size()];
		list.toArray(tab);
		list.clear();
		return tab;
	}

	/** Join the specified MAC numbers to reply a string.
	 * 
	 * @param addresses is the list of mac addresses to join.
	 * @return the joined string. 
	 */
	public static String join(MACNumber... addresses) {
		if ((addresses==null)||(addresses.length==0)) return null;
		StringBuilder buf = new StringBuilder();
		for (MACNumber number : addresses) {
			if (buf.length()>0) buf.append(MACNUMBER_SEPARATOR);
			buf.append(number);
		}
		return buf.toString();
	}

	/** Get all of the ethernet addresses associated with the local machine.
	 * <p>
	 * This method will try and find ALL of the ethernet adapters
	 * which are currently available on the system. This is heavily OS
	 * dependent and may not be supported on all platforms. When not
	 * supported, you should still get back a collection with the {@link
	 * #getPrimaryAdapter primary adapter} in it.
	 *
	 * @return the list of MAC numbers associated to the physical devices.
	 * @see #getPrimaryAdapter
	 */
	public static Collection<MACNumber> getAllAdapters() {
		List<MACNumber> av = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces!=null) {
				NetworkInterface inter;
				while (interfaces.hasMoreElements()) {
					inter = interfaces.nextElement();
					try {
						byte[] addr = inter.getHardwareAddress();
						if (addr!=null) av.add(new MACNumber(addr));
					}
					catch(SocketException exception) {
						//
					}
				}
			}
		}
		catch(SocketException exception) {
			//
		}
		return av;
	}

	/** Get all of the internet address and ethernet address mappings
	 * on the local machine.
	 * <p>
	 * This method will try and find ALL of the ethernet adapters
	 * which are currently available on the system. This is heavily OS
	 * dependent and may not be supported on all platforms. When not
	 * supported, you should still get back a collection with the {@link
	 * #getPrimaryAdapterAddresses primary adapter} in it.
	 *
	 * @return the map internet address and ethernet address mapping.
	 * @see #getPrimaryAdapterAddresses
	 */
	public static Map<InetAddress, MACNumber> getAllMappings() {
		Map<InetAddress,MACNumber> av = new HashMap<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces!=null) {
				NetworkInterface inter;
				MACNumber mac;
				InetAddress inet;
				while (interfaces.hasMoreElements()) {
					inter = interfaces.nextElement();
					try {
						byte[] addr = inter.getHardwareAddress();
						if (addr!=null) {
							mac = new MACNumber(addr);
							Enumeration<InetAddress> inets = inter.getInetAddresses();
							while (inets.hasMoreElements()) {
								inet = inets.nextElement();
								av.put(inet, mac);
							}
						}
					}
					catch(SocketException exception) {
						//
					}
				}
			}
		}
		catch(SocketException exception) {
			//
		}
		return av;
	}

	/** Try to determine the primary ethernet address of the machine.
	 * 
	 * @return the primary MACNumber or <code>null</code>
	 */
	public static MACNumber getPrimaryAdapter() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces!=null) {
				NetworkInterface inter;
				while (interfaces.hasMoreElements()) {
					inter = interfaces.nextElement();
					try {
						byte[] addr = inter.getHardwareAddress();
						if (addr!=null) return new MACNumber(addr);
					}
					catch(SocketException exception) {
						//
					}
				}
			}
		}
		catch(SocketException exception) {
			//
		}
		return null;
	}

	/** Try to determine the primary ethernet address of the machine and
	 * replies the associated internet addresses.
	 * 
	 * @return the internet addresses of the primary network interface.
	 */
	public static Collection<InetAddress> getPrimaryAdapterAddresses() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces!=null) {
				NetworkInterface inter;
				while (interfaces.hasMoreElements()) {
					inter = interfaces.nextElement();
					try {
						byte[] addr = inter.getHardwareAddress();
						if (addr!=null) {
							Collection<InetAddress> inetList = new ArrayList<>();
							Enumeration<InetAddress> inets = inter.getInetAddresses();
							while (inets.hasMoreElements()) {
								inetList.add(inets.nextElement());
							}
							
							return inetList;
						}
					}
					catch(SocketException exception) {
						//
					}
				}
			}
		}
		catch(SocketException exception) {
			//
		}
		return null;
	}

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
	public MACNumber() {
		this.bytes = new byte[6];
	}

	/** Build a MACNumber from a set of bytes.
	 * The byte array must contains 6 elements.
	 * 
	 * @param bytes is the list of bytes from which the address must be built.
	 * @throws IllegalArgumentException if the byte array does not corresponds to a valid MAC Number.
	 */
	public MACNumber(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			throw new IllegalArgumentException("mac address not 6 bytes long"); //$NON-NLS-1$
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
	public MACNumber(String address) {
		if (address==null)
			throw new IllegalArgumentException("mac address not 6 bytes long"); //$NON-NLS-1$
		String[] parts = address.split(Pattern.quote(":")); //$NON-NLS-1$
		if (parts.length!=6)
			throw new IllegalArgumentException("mac address not 6 bytes long"); //$NON-NLS-1$
		this.bytes = new byte[6];
		try {
			int val;
			for(int i=0; i<6; i++) {
				val = Integer.parseInt(parts[i],16);
				this.bytes[i] = (byte)val;
			}
		}
		catch(Exception exception) {
			throw new IllegalArgumentException("mac address not 6 bytes long"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MACNumber)) return false;

		byte[] bao = ((MACNumber) o).bytes;
		if (bao.length != this.bytes.length) return false;

		for (int i = 0; i < bao.length; i++) if (bao[i] != this.bytes[i]) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int blen = this.bytes.length;
		if (blen == 0) return 0;

		int hc = this.bytes[0];
		for (int i = 1; i < blen; i++) {
			hc *= 37;
			hc += this.bytes[i];
		}
		return hc;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		int blen = this.bytes.length;
		StringBuilder sb = new StringBuilder(blen*3);
		for (int i = 0; i < blen; i++) {
			int lo = this.bytes[i];
			int hi = ((lo >> 4) & 0xF);
			lo &= 0xF;
			if (i != 0) sb.append(':');
			sb.append(Character.forDigit(hi,16));
			sb.append(Character.forDigit(lo,16));
		}
		return sb.toString();
	}

	/** Replies if all the MAC address number are equal to zero.
	 * 
	 * @return <code>true</code> if all the bytes are zero.
	 * @see #NULL
	 */
	public boolean isNull() {
		for (int i = 0; i < this.bytes.length; i++) {
			if (this.bytes[i]!=0) return false;
		}
		return true;
	}

	/** Replies the bytes that compose this MAC Address.
	 * 
	 * @return a copy of the current bytes.
	 */
	public byte[] getBytes() {
		return this.bytes.clone();
	}
	
}

