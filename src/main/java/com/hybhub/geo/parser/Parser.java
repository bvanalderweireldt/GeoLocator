package com.hybhub.geo.parser;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

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

	private Pair<Boolean, Ip> next() {
		String line = this.scanner.nextLine();
		line = line.replaceAll("\"", "");
		final String[] tokens = line.split(",");
		return Pair.of(line.contains(":"), new Ip(tokens[0], tokens[2]));
	}

	public void parseAll() {
		ipv4Addr = new TreeMap<BigInteger, Ip>();
		ipv6Addr = new TreeMap<BigInteger, Ip>();

		while (this.hasNext()) {
			final Pair<Boolean, Ip> pair = this.next();
			if (pair.getKey()) {
				this.ipv6Addr.put(pair.getValue().getStart(), pair.getValue());
			} else {
				this.ipv4Addr.put(pair.getValue().getStart(), pair.getValue());
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
