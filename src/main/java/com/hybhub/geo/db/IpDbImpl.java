package com.hybhub.geo.db;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TreeMap;

import com.hybhub.geo.net.Downloader;
import com.hybhub.geo.parser.Parser;

public class IpDbImpl implements IpDb {

	private final boolean saveToDisk;

	private TreeMap<BigInteger, Ip> ipv6Db;

	private TreeMap<BigInteger, Ip> ipv4Db;

	public IpDbImpl() {
		this(false);
	}

	public IpDbImpl(final boolean saveToDisk) {
		this.saveToDisk = saveToDisk;
		initDb();
	}

	private void initDb() {
		final Downloader downloader = new Downloader(saveToDisk, false);
		final Parser parser = new Parser(downloader.loadDb());
		parser.parseAll();
		this.ipv4Db = parser.getIpv4Addr();
		this.ipv6Db = parser.getIpv6Addr();
	}

	@Override
	public String getCountryIso(final String ipAddress) {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(ipAddress);
		} catch (final UnknownHostException e) {
			throw new RuntimeException(e);
		}
		final TreeMap<BigInteger, Ip> targettedMap = (inetAddress instanceof Inet6Address) ? this.ipv6Db : this.ipv4Db;

		return targettedMap.floorEntry(Ip.ipToBigInteger(inetAddress)).getValue().getCountryIso();
	}

}
