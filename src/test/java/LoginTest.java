
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 

public class LoginTest {
	
	private Login login;
	
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
		login = new Login();
		MockitoAnnotations.initMocks(this);
	}
		
	@Test
	public void doPost_NonEmptyInput_GoToHomePage() throws ServletException, IOException {
		when(request.getParameter("username")).thenReturn("admin");
		when(request.getParameter("password")).thenReturn("Password123!");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		login.doPost(request, response);
		
		verify(request).getRequestDispatcher("homepage.jsp");
        verify(session).setAttribute("username", "admin");
	}

	@Test
	public void doPost_OneEmptyInput_StaySamePage() throws ServletException, IOException {
		when(request.getParameter("username")).thenReturn("xyfj");
		when(request.getParameter("password")).thenReturn("");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		login.doPost(request, response);
		
		verify(request).getRequestDispatcher("login.jsp");
        verify(session).setAttribute("username", "xyfj");
	}
	
	@Test
	public void doPost_AllEmptyInput_StaySamePage() throws ServletException, IOException {
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		
		login.doPost(request, response);
		
		verify(request).getRequestDispatcher("login.jsp");
        verify(session).setAttribute("username", "");
	}
}
