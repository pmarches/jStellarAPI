jRippleAPI
==========

This library is an API for the [Ripple](http://ripple.com/)  network. It uses websocket to provide synchronous and asynchronous access. Currently requires eclipse to build.

This is very much a work in progress, contributions are welcomed.

Features
==
* Pure Java implementation, from scratch
* Websocket
* Full binary read/write of the ripple wire format
* Offline signing
* HTTP REST support
* Address generation


Quick start
==
How to send 10 XRP to the JRippleAPI project, the simplest way possible.
```java
File testWalletFile = new File("myUnEncryptedWallet.wallet");
RippleSeedAddress seed = new RippleSeedAddress("sXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
RippleWallet wallet = RippleWallet.createWallet(seed, testWalletFile);
wallet.sendXRP(BigInteger.TEN, new RippleAddress("r32fLio1qkmYqFFYkwdnsaVN7cxBwkW4cT"));
```

Libraries required
==
*  [Jetty](http://www.eclipse.org/jetty/) as the websocket client
*  [JSONSimple](https://code.google.com/p/json-simple/) for JSON handling
*  [BouncyCastle](http://www.bouncycastle.org/) for crypto
