package com.hybhub.geo.db;

import org.junit.Assert;
import org.junit.Test;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class TestIpDb {

	@Test
	public void testIpDb() {
		final IpDbImpl ipDbImpl = new IpDbImpl(true);
		Assert.assertEquals(ipDbImpl.getCountryIso("212.23.61.177"), "GB");
		Assert.assertEquals(ipDbImpl.getCountryIso("212.35.107.224"), "BE");
		Assert.assertEquals(ipDbImpl.getCountryIso("50.7.187.194"), "CN");
		Assert.assertEquals(ipDbImpl.getCountryIso("46.229.47.0"), "AT");
		Assert.assertEquals(ipDbImpl.getCountryIso("206.73.224.191"), "KR");

		Assert.assertEquals(ipDbImpl.getCountryIso("2804:2c54:1111::"), "BR");
		Assert.assertEquals(ipDbImpl.getCountryIso("2a00:1261:1111::"), "NL");
		Assert.assertEquals(ipDbImpl.getCountryIso("2a00:1508:1111::"), "ES");
		System.out.println(ObjectSizeCalculator.getObjectSize(ipDbImpl));
	}
}
