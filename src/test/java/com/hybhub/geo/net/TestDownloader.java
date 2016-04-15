package com.hybhub.geo.net;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class TestDownloader {

	@Test
	public void testLoadDb() throws IOException {
		final Downloader downloader = new Downloader(true, true);
		final String dbStr = downloader.loadDb();
		Assert.assertNotNull(dbStr);
		Assert.assertNotEquals("", dbStr);

		Assert.assertTrue(Files.exists(Downloader.FILE));

		Files.delete(Downloader.FILE);
		Assert.assertFalse(Files.exists(Downloader.FILE));
	}

	@Test
	public void testFileGetRefreshed() throws IOException {
		final Downloader downloader = new Downloader(true, true);
		downloader.loadDb();

		Assert.assertTrue(Files.exists(Downloader.FILE));
		final long oldTime = new Calendar.Builder().set(Calendar.YEAR, 2014).build().getTimeInMillis();
		Files.setLastModifiedTime(Downloader.FILE, FileTime.fromMillis(oldTime));

		downloader.loadDb();
		Assert.assertTrue(Files.getLastModifiedTime(Downloader.FILE).toMillis() > oldTime);

		Files.delete(Downloader.FILE);
		Assert.assertFalse(Files.exists(Downloader.FILE));
	}
}
