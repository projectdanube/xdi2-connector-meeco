package xdi2.connector.meeco.mapping;

import xdi2.core.syntax.XDIAddress;

public class MeecoMapping {

	public static XDIAddress meecoToXDI(String meeco) {

		if (meeco.equals("dob")) return XDIAddress.create("<#dob>");
		if (meeco.equals("full_name")) return XDIAddress.create("<#dob>");
		if (meeco.equals("dob")) return XDIAddress.create("<#dob>");
		if (meeco.equals("dob")) return XDIAddress.create("<#dob>");
		if (meeco.equals("dob")) return XDIAddress.create("<#dob>");

		return null;
	}
}
