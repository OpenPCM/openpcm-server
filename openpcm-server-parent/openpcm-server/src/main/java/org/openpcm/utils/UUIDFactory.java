package org.openpcm.utils;

import java.util.UUID;

public final class UUIDFactory {

	public static String opId() {
		return UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
	}
}
