package dao;

import model.Fabricante;
import model.Vacina;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacinaDAO implements DAO<Vacina> {

    @Override
    public void inserir(Vacina obj) throws SQLException {
        String sql = "INSERT INTO vacina (cod_vacina, nome, quantidade, validade, ativa, fabricante) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodVacina());
            ps.setString(2, obj.getNome());
            ps.setInt(3, obj.getQuantidade());
            ps.setDate(4, Date.valueOf(obj.getValidade()));
            ps.setBoolean(5, obj.isAtiva());
            ps.setInt(6, obj.getFabricante() != null ? obj.getFabricante().getCodFabricante() : 0);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Vacina> listarTodos() throws SQLException {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT v.*, f.nome AS nome_fabricante FROM vacina v " +
                     "LEFT JOIN fabricante f ON f.cod_fabricante = v.fabricante ORDER BY v.nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Fabricante fab = new Fabricante(rs.getInt("fabricante"), rs.getString("nome_fabricante"));
                lista.add(new Vacina(
                    rs.getInt("cod_vacina"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    rs.getDate("validade") != null ? rs.getDate("validade").toLocalDate() : null,
                    rs.getBoolean("ativa"),
                    fab
                ));
            }
        }
        return lista;
    }

    public Vacina buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT v.*, f.nome AS nome_fabricante FROM vacina v " +
                     "LEFT JOIN fabricante f ON f.cod_fabricante = v.fabricante WHERE v.cod_vacina = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Fabricante fab = new Fabricante(rs.getInt("fabricante"), rs.getString("nome_fabricante"));
                    return new Vacina(
                        rs.getInt("cod_vacina"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDate("validade") != null ? rs.getDate("validade").toLocalDate() : null,
                        rs.getBoolean("ativa"),
                        fab
                    );
                }
            }
        }
        return null;
    }
}
