<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>xdi2-connector-meeco</title>
<link rel="stylesheet" target="_blank" href="style.css" TYPE="text/css" MEDIA="screen">
<link rel="shortcut icon" href="favicon.ico" />
<style type="text/css">
div.header { padding-top: 5px; padding-bottom: 35px; }
</style>
</head>
<body>
	<div id="imgtop"><img id="imgtopleft" src="images/xdi2-topleft.png"><img id="imgtopright" src="images/xdi2-topright.png"></div>
	<div id="main">
	<div class="header">
	<table cellpadding="5"><tr><td><img src="images/arrow.png" align="middle"></td><td><img src="images/meeco-logo.png" align="middle"></td><td><span id="appname">xdi2-connector-meeco</span></td></tr></table>
	</div>

	<% if (request.getAttribute("error") != null) { %>

		<p style="font-family: monospace; white-space: pre; color: red;"><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>

	<% } %>

	<% if (request.getAttribute("feedback") != null) { %>

		<p style="font-family: monospace; white-space: pre; color: #5e1bda;"><%= request.getAttribute("feedback") != null ? request.getAttribute("feedback") : "" %></p>

	<% } %>

	<div class="line"></div>

	<p class="subheader">XDI Server</p>
	<p>
	<table cellpadding="5"><tr>
	<td>Click here to access the local XDI server: <a href="<%= request.getRequestURL().substring(0, request.getRequestURL().toString().lastIndexOf('/')) %>/xdi/"><%= request.getRequestURL().substring(0, request.getRequestURL().toString().lastIndexOf('/')) %>/xdi/</a></td>
	</tr></table>
	</p>
	
	<div class="line"></div>

	<p class="subheader">Send a Message to my XDI Endpoint</p>

	<p>Make sure you have a Meeco Email and Password in the graph, before sending this XDI message.</p>

	<form action="client" method="post" accept-charset="UTF-8">

		<textarea class="input" name="input" style="width: 100%" rows="12"><%= request.getAttribute("input") != null ? request.getAttribute("input") : "" %></textarea><br>

		<% String resultFormat = (String) request.getAttribute("resultFormat"); if (resultFormat == null) resultFormat = ""; %>
		<% String writeImplied = (String) request.getAttribute("writeImplied"); if (writeImplied == null) writeImplied = ""; %>
		<% String writeOrdered = (String) request.getAttribute("writeOrdered"); if (writeOrdered == null) writeOrdered = ""; %>
		<% String writePretty = (String) request.getAttribute("writePretty"); if (writePretty == null) writePretty = ""; %>
		<% String endpoint = (String) request.getAttribute("endpoint"); if (endpoint == null) endpoint = ""; %>

		<p>
		Send to endpoint: 
		<input type="text" name="endpoint" size="80" value="<%= endpoint %>">
		</p>

		Result Format:
		<select name="resultFormat">
		<option value="XDI/JSON" <%= resultFormat.equals("XDI/JSON") ? "selected" : "" %>>XDI/JSON</option>
		<option value="XDI DISPLAY" <%= resultFormat.equals("XDI DISPLAY") ? "selected" : "" %>>XDI DISPLAY</option>
		</select>
		&nbsp;

		<input name="writeImplied" type="checkbox" <%= writeImplied.equals("on") ? "checked" : "" %>>implied=1

		<input name="writeOrdered" type="checkbox" <%= writeOrdered.equals("on") ? "checked" : "" %>>ordered=1

		<input name="writePretty" type="checkbox" <%= writePretty.equals("on") ? "checked" : "" %>>pretty=1

		<input type="submit" value="Go!">

	</form>

	<% if (request.getAttribute("stats") != null) { %>
		<p>
		<%= request.getAttribute("stats") %>

		<% if (request.getAttribute("output") != null) { %>
			Copy&amp;Paste: <textarea style="width: 100px; height: 1.2em; overflow: hidden"><%= request.getAttribute("output") != null ? request.getAttribute("output") : "" %></textarea>
		<% } %>
		</p>
	<% } %>

	<% if (request.getAttribute("output") != null) { %>
		<div class="result"><pre><%= request.getAttribute("output") != null ? request.getAttribute("output") : "" %></pre></div><br>
	<% } %>

	</div>
</body>
</html>
