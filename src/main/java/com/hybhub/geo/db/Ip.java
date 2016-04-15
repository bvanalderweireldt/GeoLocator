package com.hybhub.geo.db;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ip implements Comparable<Ip> {

	private final byte[] countryIso;
	private BigInteger start;

	public Ip(final String start, final String countryIso) {
		try {
			this.start = Ip.ipToBigInteger(InetAddress.getByName(start));
		} catch (final UnknownHostException e) {
			throw new RuntimeException("Couldn't load the addresses : " + start, e);
		}
		this.countryIso = countryIso.getBytes();
	}

	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public String toString() {
		return "Start long : " + this.start + ", country : " + this.countryIso;
	}

	public static BigInteger ipToBigInteger(final InetAddress ip) {
		return new BigInteger(1, ip.getAddress());
	}

	@Override
	public int compareTo(final Ip o) {
		return this.start.compareTo(o.start);
	}

	public String getCountryIso() {
		return new String(this.countryIso);
	}

	public BigInteger getStart() {
		return start;
	}

}
