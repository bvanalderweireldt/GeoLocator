package com.hybhub.geo.parser;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.TreeMap;

import com.hybhub.geo.db.Ip;

public class Parser {

	private final Scanner scanner;

	TreeMap<BigInteger, Ip> ipv4Addr;
	TreeMap<BigInteger, Ip> ipv6Addr;

	public Parser(final String csvFileStr) {
		this.scanner = new Scanner(csvFileStr);
	}

	private boolean hasNext() {
		return this.scanner.hasNextLine();
	}

	private Ip next() {
		String line = this.scanner.nextLine();
		line = line.replaceAll("\"", "");
		final String[] tokens = line.split(",");
		return new Ip(tokens[0], tokens[1], tokens[2]);
	}

	public void parseAll() {
		ipv4Addr = new TreeMap<BigInteger, Ip>();
		ipv6Addr = new TreeMap<BigInteger, Ip>();

		while (this.hasNext()) {
			final Ip ipRange = this.next();
			if (ipRange.isIpv6()) {
				this.ipv6Addr.put(ipRange.getStart(), ipRange);
			} else {
				this.ipv4Addr.put(ipRange.getStart(), ipRange);
			}
		}
	}

	public TreeMap<BigInteger, Ip> getIpv4Addr() {
		return ipv4Addr;
	}

	public TreeMap<BigInteger, Ip> getIpv6Addr() {
		return ipv6Addr;
	}
}
