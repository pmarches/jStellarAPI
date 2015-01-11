package jstellarapi;

import jstellarapi.connection.StellarDaemonWebsocketConnectionTest;
import jstellarapi.core.DenominatedIssuedCurrencyTest;
import jstellarapi.core.StellarSeedAddressTest;
import jstellarapi.keys.StellarSignerTest;
import jstellarapi.keys.StellarWalletTest;
import jstellarapi.serialization.ReferenceImplementationAmountTests;
import jstellarapi.serialization.StellarBinarySerializerTest;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@ExcludeCategory(SlowTests.class)
@SuiteClasses({
	StellarDaemonWebsocketConnectionTest.class,
	DenominatedIssuedCurrencyTest.class,
	StellarSeedAddressTest.class,
	StellarSignerTest.class,
	StellarWalletTest.class,
	ReferenceImplementationAmountTests.class,
	StellarBinarySerializerTest.class,
	})
public class FastTestSuite {
}
