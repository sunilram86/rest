<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="com.test.ItemStore" %>
<%@ page import="com.test.CartStore" %>
<%@ page import="java.util.Set" %>

<html>
<head><title>Shopping Cart</title></head>
<body>
  Here are the items in your cart:
  <form action="post" method="post">
    <ul>
      <%
        Set<Long> ids = CartStore.getItems();
        for(Object obj : ItemStore.getItems()) {
          JSONObject item = (JSONObject)obj;
          if(ids.contains((Long)item.get("id"))) {
      %>
          <li>
            <%=item.get("name")%>&nbsp;$<%=item.get("price")%>
      <%
          }
        }
      %>
    </ul>
    <input type="submit" value="Checkout" />
  </form>
</body>
</html>
