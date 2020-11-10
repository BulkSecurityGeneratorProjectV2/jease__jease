<%@page import="jease.cms.domain.*,jease.site.*,jease.*"%>
<%
	Composite composite = (Composite) request.getAttribute("Node");

	for (Content child : Navigations.getVisibleContent(composite)) {
		if (child instanceof Reference) {
			child = ((Reference) child).getDestination();
		}
		if (child.isPage()) {
			request.setAttribute("Node", child);
			pageContext.include(Registry.getView(child));
			request.setAttribute("Node", composite);
		} else if (child instanceof Topic) {
%>
<h1><%=child.getTitle()%></h1>
<%
	} else {
%>
<ul>
	<li><a href="<%=child.getPath()%>"><%=child.getTitle()%></a></li>
</ul>
<%
	}
	}
%>