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
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String count = req.getParameter("count");
        String nameTo = req.getParameter("nameTo");
        try {
            BankClient clientSender = new BankClientService().getClientByName(senderName);
            BankClient clientTo = new BankClientService().getClientByName(nameTo);
            if (bankClientService.sendMoneyToClient(clientSender, clientTo.getName(), Long.valueOf(count))) {
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                pageVariables.put("message", "The transaction was successful");
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                pageVariables.put("message", "transaction rejected");
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (DBException e) {
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
            pageVariables.put("message", "transaction rejected");
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
