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
                String taxId = resultSet.getString(1);
                String taxName = resultSet.getString(2);
                double taxRate = resultSet.getDouble(3);
                LocalDate dateOfCreation = ConvertHelper.LocalDateConverter(resultSet.getDate(4));
                boolean activate = resultSet.getBoolean(5);

                Tax tax = new Tax(taxId, taxName, taxRate, dateOfCreation, activate);
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
                "\t(?, ?, ?, ?, ?)";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, tax.getTaxID());
            preparedStatement.setString(2, tax.getTaxName());
            preparedStatement.setDouble(3, tax.getTaxRate());
            preparedStatement.setDate(4, Date.valueOf(tax.getDateOfCreation()));
            preparedStatement.setBoolean(5, tax.isActivate());
            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static boolean deleteData(String taxID){
        int n = 0;

        String SQLStatement = "DELETE FROM Tax\n" +
                "WHERE taxID = ?";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, taxID);
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
                SET taxName = ?, taxRate = ?, activate = ?
                WHERE taxName = ?""";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, newTax.getTaxName());
            preparedStatement.setDouble(2, newTax.getTaxRate());
            preparedStatement.setBoolean(3, newTax.isActivate());
            preparedStatement.setString(4, newTax.getTaxName());

            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static Tax getDataByID(String id){
        String SQLStatement = "select * from Tax where taxID = ? ";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
        )
        {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Tax tax = new Tax();
                tax.setTaxID(resultSet.getString(1));
                tax.setTaxName(resultSet.getString(2));
                tax.setTaxRate(resultSet.getDouble(3));
                tax.setDateOfCreation(ConvertHelper.LocalDateConverter(resultSet.getDate(4)));
                tax.setActivate(resultSet.getBoolean(5));
                return tax;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
