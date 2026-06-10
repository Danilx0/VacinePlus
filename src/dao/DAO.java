package dao;

import java.sql.*;
import java.util.List;

public interface DAO<T> {
    void inserir(T obj) throws SQLException;
    List<T> listarTodos() throws SQLException;
}
