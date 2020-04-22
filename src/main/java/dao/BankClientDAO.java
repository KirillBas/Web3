package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;
    private PreparedStatement preparedStatement = null;
    private String insertSQL = "insert into bank_client (name , password, money) values(?, ?, ?);";
    private String updateMoney = "update bank_client set money = ? where id = ?;";

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client");
        ResultSet result = statement.getResultSet();
        List<BankClient> resultList = new ArrayList<>();
        BankClient bankClient = new BankClient();
        while (result.next()) {
            bankClient.setId(result.getLong(1));
            bankClient.setName(result.getString(2));
            bankClient.setPassword(result.getString(3));
            bankClient.setMoney(result.getLong(4));
            resultList.add(bankClient);
        }
        return resultList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        List<BankClient> validateClient = getAllBankClient();
        for (BankClient client: validateClient) {
            System.out.println(client.getName() + " " + client.getPassword());
            if (client.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            BankClient currentClient = getClientByName(name);
            Long updateMoneys = currentClient.getMoney() + transactValue;
            preparedStatement = connection.prepareStatement(updateMoney);
            preparedStatement.setString(1, String.valueOf(updateMoneys));
            preparedStatement.setString(2, String.valueOf(currentClient.getId()));
            preparedStatement.execute();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where id = '" + id + "'");
        ResultSet result = statement.getResultSet();
        result.next();
        BankClient bankClient = new BankClient();
        bankClient.setId(result.getLong(1));
        bankClient.setName(result.getString(2));
        bankClient.setPassword(result.getString(3));
        bankClient.setMoney(result.getLong(4));
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException{
        BankClient tempClient = getClientByName(name);
        return tempClient.getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where name = '" + name + "'");
        ResultSet result = statement.getResultSet();
        result.next();
        BankClient bankClient = new BankClient();
        bankClient.setId(result.getLong(1));
        bankClient.setName(result.getString(2));
        bankClient.setPassword(result.getString(3));
        bankClient.setMoney(result.getLong(4));
        return bankClient;
    }

    public boolean addClient(BankClient client) throws SQLException {
        boolean check = validateClient(client.getName(), client.getPassword());
        System.out.println(check);
        if (!check) {
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPassword());
            preparedStatement.setString(3, String.valueOf(client.getMoney()));
            preparedStatement.execute();
            return true;
        }
        return false;
    }

    public boolean deleteClient(BankClient client) throws SQLException {
        Statement statement = connection.createStatement();
        if (validateClient(client.getName(), client.getPassword())) {
            statement.execute("delete from bank_client where bank_client.id = '" + client.getName() +"'");
            return true;
        }
        return false;
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
