package xdi2.connector.meeco.util;

import xdi2.core.Graph;
import xdi2.core.syntax.XDIAddress;

public class GraphUtil {

	private GraphUtil() { }

	public static String retrieveMeecoEmail(Graph graph, XDIAddress meecoUserXDIAddress) {

/*		XDIAddress contextNodeXDIAddress = XDIAddress.create("" + MeecoMapping.XDI_ADD_MEECO_CONTEXT + meecoUserXDIAddress + XDISecurityConstants.XDI_ADD_OAUTH_TOKEN);

		LiteralNode literalNode = graph.getDeepLiteralNode(contextNodeXDIAddress);

		return literalNode == null ? null : literalNode.getLiteralDataString();*/
		return "markus@projectdanube.org";
	}

	public static String retrieveMeecoPassword(Graph graph, XDIAddress meecoUserXDIAddress) {

	/*	XDIAddress contextNodeXDIAddress = XDIAddress.create("" + MeecoMapping.XDI_ADD_MEECO_CONTEXT + meecoUserXDIAddress + XDISecurityConstants.XDI_ADD_OAUTH_TOKEN);

		LiteralNode literalNode = graph.getDeepLiteralNode(contextNodeXDIAddress);

		return literalNode == null ? null : literalNode.getLiteralDataString();*/
		return "4aa1-9d$90";
	}
}
