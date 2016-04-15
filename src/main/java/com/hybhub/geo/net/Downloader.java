package com.hybhub.geo.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

public class Downloader {

	/**
	 * URL to download the IP geographic-location CSV file from.
	 *
	 * @see https://db-ip.com
	 */
	protected final static String URL_DB_IP = "http://download.db-ip.com/free/dbip-country-%1$tY-%1$tm.csv.gz";

	protected final static String URL_FALLBACK = "https://www.dropbox.com/s/mzya3mkh9nqcrtj/dbip-country-2016-04.csv.gz?dl=0";

	protected final static String TMP_PATH = System.getProperty("java.io.tmpdir");

	protected final static Path FILE = Paths.get(TMP_PATH + "dbip-country.csv.gz");

	protected final static int MAX_AGE = 60;

	static final Logger LOG = Logger.getLogger(Downloader.class.getName());

	private final boolean useDisk;

	List<URL> targetedUrls = new ArrayList<>();

	public Downloader(final boolean useDisk, final boolean onlyFallback) {
		this.useDisk = useDisk;
		try {
			if (!onlyFallback) {
				this.targetedUrls.add(new URL(String.format(URL_DB_IP, new Date())));
			}
			this.targetedUrls.add(new URL(URL_FALLBACK));
		} catch (final MalformedURLException e) {
			LOG.log(Level.SEVERE, "Couldn't add potential URLS", e);
			throw new RuntimeException(e);
		}
	}

	public String loadDb() {

		if (this.useDisk && this.fileExists() && !this.fileNeedsRefresh()) {
			try {
				return loadDbFromDisk();
			} catch (final IOException e) {
				LOG.log(Level.WARNING, "Couldn't load the db from the disk, will try to download it now !", e);
			}
		}

		String dbStr = null;
		for (final URL url : targetedUrls) {
			try {
				dbStr = loadDbFromNet(url);
				break;
			} catch (final IOException e) {
				LOG.log(Level.WARNING, "Couldn't load the csv !", e);
			}
		}

		if (dbStr == null || dbStr.equals("")) {
			throw new RuntimeException("Couldn't load any csv file, initialization failed !");
		}

		if (this.useDisk) {
			try {
				saveFileToDisk(dbStr);
			} catch (final IOException e) {
				LOG.log(Level.WARNING, "Couldn't save the csv string to disk", e);
			}
		}
		return dbStr;
	}

	private String loadDbFromNet(final URL url) throws IOException {
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.addRequestProperty("User-Agent", "Wget/1.9.1");

		while (httpURLConnection.getResponseCode() == 302) {
			httpURLConnection = (HttpURLConnection) new URL(httpURLConnection.getHeaderField("Location"))
					.openConnection();
		}

		try (final GZIPInputStream in = new GZIPInputStream(httpURLConnection.getInputStream())) {
			return IOUtils.toString(in);
		} finally {
			httpURLConnection.disconnect();
		}
	}

	private String loadDbFromDisk() throws IOException {
		try (final FileInputStream fin = new FileInputStream(Downloader.FILE.toFile())) {
			return IOUtils.toString(fin);
		}
	}

	private void saveFileToDisk(final String dbStr) throws IOException {
		try (final FileOutputStream fos = new FileOutputStream(Downloader.FILE.toFile())) {
			IOUtils.write(dbStr, fos);
		}
	}

	private boolean fileExists() {
		return Files.exists(Downloader.FILE);
	}

	private boolean fileNeedsRefresh() {
		BasicFileAttributes attributes;
		try {
			attributes = Files.getFileAttributeView(FILE, BasicFileAttributeView.class)
					.readAttributes();
			final long diff = System.currentTimeMillis() - attributes.lastModifiedTime().toMillis();
			return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > Downloader.MAX_AGE);
		} catch (final IOException e) {
			LOG.log(Level.WARNING, "Couldn't read the file attribute will try to download it now !", e);
		}
		return false;
	}
}
