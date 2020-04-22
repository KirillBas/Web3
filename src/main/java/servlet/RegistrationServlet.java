package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        String money = req.getParameter("money");
        BankClient client = new BankClient();
        client.setName(name);
        client.setPassword(pass);
        client.setMoney(Long.valueOf(money));
        if (name.equals("") || pass.equals("") || money.equals("")) {
            pageVariables.put("message", "Client not add");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            if (bankClientService.addClient(client)) {
                pageVariables.put("message", "Add client successful");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                pageVariables.put("message", "Client not add");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        pageVariables.put("contextPath", request.getContextPath());
        return pageVariables;
    }
}
