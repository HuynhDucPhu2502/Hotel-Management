package iuh.fit.dao;

import iuh.fit.models.HistoryCheckIn;
import iuh.fit.models.Tax;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class TaxDAO {
    public static List<Tax> getData(){
        List<Tax> data = new ArrayList<>();
        String SQLStatement = "SELECT * FROM Tax";

        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String taxName = resultSet.getString(1);
                double taxRate = resultSet.getDouble(2);
                LocalDate dateOfCreation = ConvertHelper.LocalDateConverter(resultSet.getDate(3));
                boolean activate = resultSet.getBoolean(4);

                Tax tax = new Tax(taxName, taxRate, dateOfCreation, activate);
                data.add(tax);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static boolean insertData(Tax tax){
        int n = 0;
        String SQLStatement = "INSERT INTO Tax\n" +
                "VALUES\n" +
                "\t(?, ?, ?, ?)";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, tax.getTaxName());
            preparedStatement.setDouble(2, tax.getTaxRate());
            preparedStatement.setDate(3, Date.valueOf(tax.getDateOfCreation()));
            preparedStatement.setBoolean(4, tax.isActivate());
            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static boolean deleteData(String taxName){
        int n = 0;

        String SQLStatement = "DELETE FROM Tax\n" +
                "WHERE taxName = ?";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, taxName);
            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static boolean updateData(Tax newTax){
        int n = 0;

        String SQLStatement = """
                UPDATE Tax
                SET taxRate = ?, activate = ?
                WHERE taxName = ?""";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setDouble(1, newTax.getTaxRate());
            preparedStatement.setBoolean(2, newTax.isActivate());
            preparedStatement.setString(3, newTax.getTaxName());

            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static Tax getDataByName(String name){
        String SQLStatement = "";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
