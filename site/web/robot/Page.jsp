<%@page import="jease.cms.domain.*,jease.site.*" contentType="text/html; charset=UTF-8"%>
<% Content content = (Content) request.getAttribute("Node"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%@include file="/site/service/Pagebase.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%= request.getAttribute("Page.Title") %></title>
<link rel="stylesheet" type="text/css" href="<%=request.getAttribute("Page.Root") %>site/web/robot/style/screen.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<%=request.getAttribute("Page.Root") %>site/web/robot/style/print.css" media="<%= request.getParameter("print") == null ? "print" : "print,screen" %>" />
<%@include file="/site/service/Jquery.jsp"%>
<%@include file="/site/service/Lightbox.jsp"%>
<%@include file="/site/service/Prettify.jsp"%>
<script type="text/javascript" src="<%=request.getAttribute("Page.Root") %>site/web/robot/js/maxheight.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("Page.Root") %>site/web/robot/js/cufon-yui.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("Page.Root") %>site/web/robot/js/Myriad_Pro_300.font.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("Page.Root") %>site/web/robot/js/Myriad_Pro_400.font.js"></script>
<script type="text/javascript">//<![CDATA[
Cufon.replace("#tabs", { fontFamily: 'Myriad Pro', hover:true });
Cufon.replace("#logo", { fontFamily: 'Myriad Pro', hover:true });
$(document).ready(function(){
	new ElementMaxHeight();
	Cufon.now();
});
//]]></script>
</head>
<body>
	<div id="header">
		<div class="bg">
			<div class="container">
				<div id="top">
					<div class="wrapper">
						<div class="fleft">							
							<div id="logo">
								<img src="<%=request.getAttribute("Page.Root") %>site/web/robot/style/img/logo.jpg" alt="" />
								<a href="<%=request.getAttribute("Page.Root") %>">								
									&nbsp;j<span style="color: white;">ease</span> &raquo; <span style="color: white;">java</span>&nbsp;with <span style="color: white;">ease</span>
								</a>
							</div>
						</div>
						<div class="fright">
							<form action="." id="search-form">
							<fieldset>
								<input type="text" class="text" name="query" value="<%=request.getParameter("query") != null ? request.getParameter("query") : ""%>" />
								<input type="hidden" name="page" value="/site/service/Search.jsp" />
								<input type="submit" class="submit" value="" />
							</fieldset>
							</form>
						</div>
					</div>
				</div>
				<div id="tabs">					
					<ul>
					<% for (Content tab : Navigations.getTabs()) { %>
						<li><a href="<%=tab.getPath()%>"><%=tab.getTitle()%></a></li>
					<% } %>
					</ul>
				</div>
				<div id="news">
					<ul>
					<% for (News item : Navigations.getNews((Content) content.getParent())) { %>						
						<li>
							<a href="<%=item.getPath()%>?print">
								<% if (item.getDate() != null) { %><%=String.format("%1$td %1$tb %1$tY", item.getDate())%> - <% } %>
								<%=item.getTitle()%>
							</a>
							<br />
							<%= jfix.util.Validations.isNotEmpty(item.getTeaser()) ? item.getTeaser() : item.getStory()%>
						</li>	
					<% } %>
					</ul>				
				</div>
			</div>
		</div>
	</div>
	<div id="content">
		<div class="container">
			<div class="wrapper">
				<div class="aside maxheight">
					<div class="indent">
						<div id="breadcrumb">
							<% for (Content parent : content.getParents(Content.class)) { %>
								<% if(parent != content.getParent()) { %>
									&raquo; <a href="<%=parent.getPath()%>"><%=parent.getTitle()%></a> 
								<% } %>
							<% } %>
						</div>
						<h1><%=((Content) content.getParent()).getTitle()%></h1>
						<ul>
						<% for (Content item : Navigations.getItems((Content) content.getParent())) { %>
							<% if (item instanceof Topic) { %>
								</ul><h2><%=item.getTitle()%></h2><ul>
							<% } else { %>
								<li><a href="<%=item.getPath()%>" <%=item == content ? " class=\"current\"" : ""%>><%=item.getTitle()%></a></li>
							<% } %>
						<% } %>
						</ul>
					</div>
				</div>
				<div class="mainContent maxheight">
					<div class="indent">
						<div class="section">
							<% pageContext.include((String) request.getAttribute("Page.Template")); %>
							<p class="editorial">
								Last modified on <%=String.format("%tF", content.getLastModified())%>
								<% if (content.getEditor() != null) { %>
									by <%=content.getEditor().getName()%>
								<% }%>
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<div class="bg">
			<div class="container">
				<div class="indent">
					&copy; 2011 <a href="http://www.jease.org/">jease.org</a> | Design by <a href="http://www.templates.com/">templates.com</a>
					<br />
  					<%@include file="/site/service/Designswitch.jsp" %>	  			  								
				</div>	
			</div>
		</div>
	</div>
</body>
</html>