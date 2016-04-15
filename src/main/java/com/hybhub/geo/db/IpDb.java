package com.hybhub.geo.db;

public interface IpDb {
	/**
	 *
	 * @param ipAddress
	 * @return
	 */
	String getCountryIso(final String ipAddress);
}
