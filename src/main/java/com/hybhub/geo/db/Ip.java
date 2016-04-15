package com.hybhub.geo.db;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ip implements Comparable<Ip> {

	private String countryIso;
	private InetAddress startAddr;
	private BigInteger start;
	private BigInteger end;
	private boolean ipv6;

	public Ip(final String start) {
		try {
			this.startAddr = InetAddress.getByName(start);
			if (this.startAddr instanceof Inet6Address) {
				ipv6 = true;
			}
			this.start = Ip.ipToBigInteger(this.startAddr);
		} catch (final UnknownHostException e) {
			throw new RuntimeException("Couldn't load the addresses : " + start, e);
		}
	}

	public Ip(final String start, final String end, final String countryIso) {
		try {
			final InetAddress startAdd = InetAddress.getByName(start);
			if (startAdd instanceof Inet6Address) {
				ipv6 = true;
			}
			this.start = Ip.ipToBigInteger(startAdd);
			this.end = Ip.ipToBigInteger(InetAddress.getByName(end));
		} catch (final UnknownHostException e) {
			throw new RuntimeException("Couldn't load the addresses : " + start + ", " + end, e);
		}
		this.countryIso = countryIso;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Ip)) {
			return false;
		}
		return this.inRange(((Ip) obj).start);
	}

	@Override
	public int hashCode() {
		return -1;
	}

	public boolean inRange(final BigInteger addr) {
		return (this.start.compareTo(addr) >= 0) && (this.end.compareTo(addr) <= 0);
	}

	@Override
	public String toString() {
		return "Start long : " + this.start + ", end long : " + this.end + ", country : " + this.countryIso;
	}

	public static BigInteger ipToBigInteger(final InetAddress ip) {
		return new BigInteger(1, ip.getAddress());
	}

	public int compareTo(final Ip o) {
		return this.start.compareTo(o.start);
	}

	public String getCountryIso() {
		return countryIso;
	}

	public BigInteger getStart() {
		return start;
	}

	public boolean isIpv6() {
		return ipv6;
	}

}
