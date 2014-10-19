package com.khozzy.stemmer.database.mysql;

import com.khozzy.stemmer.database.DAO;
import com.khozzy.stemmer.database.DataSource;
import com.khozzy.stemmer.domain.Sentence;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MysqlDAO implements DAO {
    private static final Logger logger = Logger.getLogger(MysqlDAO.class);

    private DataSource datasource;

    public MysqlDAO() {
        try {
            datasource = DataSource.getInstance();
        } catch (IOException | SQLException | PropertyVetoException e) {
            logger.error("Nie mozna uzyskac dostepu do zrodla danych", e);
        }
    }

    public Set<Sentence> getNotProcessedStatements() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Set<Sentence> sentences = new HashSet<>();

        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement("SELECT * FROM sentiment_analysis.entry WHERE processed IS NULL;");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sentences.add(new Sentence(
                        resultSet.getInt("id"),
                        resultSet.getString("original"),
                        resultSet.getInt("class")
                ));
            }
        } catch (SQLException e) {
            logger.error("Blad podczas pobierania zdan do przetworzenia", e);
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {}
        }

        return sentences;
    }

    public void updateSentence(Sentence sentence) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = datasource.getConnection();
            statement = connection.prepareStatement("UPDATE sentiment_analysis.entry SET processed = ? WHERE id = ?");

            statement.setString(1, sentence.getProcessed());
            statement.setInt(2, sentence.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Blad podczas akutalizacji zdania o id " + sentence.getId(), e);
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (Exception e) {}
        }
    }
}
