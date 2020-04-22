package servlet;

import exception.DBException;
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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        try {
            if (bankClientService.validateClient(senderName, senderPass)) {
                BankClient clientSender = bankClientService.getClientByName(senderName);
                if (bankClientService.sendMoneyToClient(clientSender, nameTo, count)) {
                    pageVariables.put("message", "The transaction was successful");
                    resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                    resp.setContentType("text/html;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    pageVariables.put("message", "transaction rejected");
                    resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                    resp.setContentType("text/html;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                pageVariables.put("message", "transaction rejected");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (DBException e) {
            pageVariables.put("message", "transaction rejected");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
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
