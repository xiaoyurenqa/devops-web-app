
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 

public class RegisterTest {
	
	private Register register;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private RequestDispatcher requestDispatcher;
	
	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() throws IOException {
		register = new Register();
		MockitoAnnotations.initMocks(this);
	}
		
	@Test
	public void doPost_NonEmptyInput_GoToLoginPage() throws ServletException, IOException {
		when(request.getParameter("first_name")).thenReturn("John");
		when(request.getParameter("last_name")).thenReturn("Doe");
		when(request.getParameter("username")).thenReturn("admin");
		when(request.getParameter("password")).thenReturn("Password123!");
		when(request.getParameter("address")).thenReturn("Howe Street");
		when(request.getParameter("contact")).thenReturn("12345678");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		register.doPost(request, response);
		
		verify(request).getRequestDispatcher("login.jsp");
		verify(session).setAttribute("username", "admin");
		verify(session).setAttribute("password", "Password123!");
	}
	
	@Test 
	public void doPost_OneEmptyInput_StaySamePage() throws ServletException, IOException {
		when(request.getParameter("first_name")).thenReturn("John");
		when(request.getParameter("last_name")).thenReturn("Doe");
		when(request.getParameter("username")).thenReturn("admin");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("address")).thenReturn("");
		when(request.getParameter("contact")).thenReturn("");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		register.doPost(request, response);
		
		verify(request).getRequestDispatcher("register.jsp");
		verify(session).setAttribute("username", "admin");
		verify(session).setAttribute("password", "");
		verify(session).setAttribute("first_name", "John");
		verify(session).setAttribute("last_name", "Doe");
		verify(session).setAttribute("address", "");
		verify(session).setAttribute("contact", "");
	}
	
	@Test 
	public void doPost_AllEmptyInput_StaySamePage() throws ServletException, IOException {
		when(request.getParameter("first_name")).thenReturn("");
		when(request.getParameter("last_name")).thenReturn("");
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("address")).thenReturn("");
		when(request.getParameter("contact")).thenReturn("");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		register.doPost(request, response);
		
		verify(request).getRequestDispatcher("register.jsp");
		verify(session).setAttribute("username", "");
		verify(session).setAttribute("password", "");
		verify(session).setAttribute("first_name", "");
		verify(session).setAttribute("last_name", "");
		verify(session).setAttribute("address", "");
		verify(session).setAttribute("contact", "");
	}
	
	@Ignore
	@Test
	public void doPost_NonEmptyInput_SpecialUsername_GoToLoginPage() throws ServletException, IOException {
		when(request.getParameter("first_name")).thenReturn("John");
		when(request.getParameter("last_name")).thenReturn("Doe");
		when(request.getParameter("username")).thenReturn("Denver");
		when(request.getParameter("password")).thenReturn("Password123!");
		when(request.getParameter("address")).thenReturn("Howe Street");
		when(request.getParameter("contact")).thenReturn("12345678");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		register.doPost(request, response);
		
		verify(request).getRequestDispatcher("login.jsp");
		verify(session).setAttribute("username", "admin");
		verify(session).setAttribute("password", "Password123!");
	}


}