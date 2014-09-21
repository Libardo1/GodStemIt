package com.khozzy.stemmer.database;

import com.khozzy.stemmer.domain.Sentence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DAO {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DB = "sentiment_analysis";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static final String URL = String.format(
            "jdbc:mysql://%s:%d/%s?user=%s&password=%s&characterEncoding=utf-8", HOST, PORT, DB, USER, PASSWORD);

    public Set<Sentence> getNotProcessedStatements(int limit) {
        Set<Sentence> sentences = new HashSet<>();

        try {
            final Connection connection = DriverManager.getConnection(URL);
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM sentiment_analysis.entry WHERE processed IS NULL AND error = 0 LIMIT ?");
            statement.setInt(1, limit);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sentences.add(new Sentence(
                        resultSet.getInt("id"),
                        resultSet.getString("original"),
                        resultSet.getInt("class"),
                        resultSet.getBoolean("error")
                ));
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return sentences;
    }

    public int countRemainingToProcess() {

        try {
            final Connection connection = DriverManager.getConnection(URL);
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM sentiment_analysis.entry WHERE processed IS NULL AND error = 0");

            while (resultSet.next()) {
                return resultSet.getInt("COUNT(*)");
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return 0;
    }

    public void updateSentence(int id, String processed, boolean error) {

        try {
            final Connection connection = DriverManager.getConnection(URL);
            final PreparedStatement statement = connection.prepareStatement("UPDATE sentiment_analysis.entry SET processed = ?, error = ? WHERE id = ?");

            statement.setString(1, processed);
            statement.setBoolean(2, error);
            statement.setInt(3, id);

            statement.executeUpdate();
            connection.close();

        } catch (SQLException e) {
            System.err.println(String.format("Błąd podczas aktualizacji zdania o id %d", id));
            System.err.println(e.getMessage());
        }
    }
}
