# GeoLocator
Geolocate any IPV4 or IPV6 address for a locally indexed CSV based database.

Usually projects would intensively use external API to locate Ip addresses, this offer a locale and reliable way of doing it locally using a TreeMap (red black tree), once the tree is loaded in memory (~60MB).

## Installation
```gradle
gradle build
```

## Usage
```java
final IpDbImpl ipDbImpl = new IpDbImpl(true);
ipDbImpl.getCountryIso("212.23.61.177");
ipDbImpl.getCountryIso("2a00:1261:1111::");
````

````xml
<bean name="ipDb" class="com.hybhub.geo.db.IpDb">
	<constructor-arg value="true"/>
</bean>
````
