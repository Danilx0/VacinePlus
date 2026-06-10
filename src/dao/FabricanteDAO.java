package dao;

import model.Fabricante;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO implements DAO<Fabricante> {

    @Override
    public void inserir(Fabricante obj) throws SQLException {
        String sql = "INSERT INTO fabricante (cod_fabricante, nome) VALUES (?, ?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodFabricante());
            ps.setString(2, obj.getNome());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Fabricante> listarTodos() throws SQLException {
        List<Fabricante> lista = new ArrayList<>();
        String sql = "SELECT * FROM fabricante ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Fabricante(
                    rs.getInt("cod_fabricante"),
                    rs.getString("nome")
                ));
            }
        }
        return lista;
    }
}
