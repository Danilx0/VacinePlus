package dao;

import model.Fabricante;
import model.Lote;
import model.Vacina;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoteDAO implements DAO<Lote> {

    @Override
    public void inserir(Lote obj) throws SQLException {
        String sql = "INSERT INTO lote (cod_lote, num_lote, data_lote, qtda_enviado, vacina) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodLote());
            ps.setLong(2, obj.getNumLote());
            ps.setDate(3, obj.getDataLote() != null ? Date.valueOf(obj.getDataLote()) : null);
            ps.setInt(4, obj.getQtdaEnviado());
            ps.setInt(5, obj.getVacina().getCodVacina());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Lote> listarTodos() throws SQLException {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.*, v.nome AS nome_vacina, v.quantidade, v.validade, v.ativa, v.fabricante, " +
                     "f.nome AS nome_fabricante FROM lote l " +
                     "JOIN vacina v ON v.cod_vacina = l.vacina " +
                     "LEFT JOIN fabricante f ON f.cod_fabricante = v.fabricante ORDER BY l.num_lote";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Lote> listarPorVacina(int codVacina) throws SQLException {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.*, v.nome AS nome_vacina, v.quantidade, v.validade, v.ativa, v.fabricante, " +
                     "f.nome AS nome_fabricante FROM lote l " +
                     "JOIN vacina v ON v.cod_vacina = l.vacina " +
                     "LEFT JOIN fabricante f ON f.cod_fabricante = v.fabricante " +
                     "WHERE l.vacina = ? ORDER BY l.num_lote";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codVacina);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Lote mapear(ResultSet rs) throws SQLException {
        Fabricante fab = new Fabricante(rs.getInt("fabricante"), rs.getString("nome_fabricante"));
        Vacina vacina = new Vacina(
            rs.getInt("vacina"),
            rs.getString("nome_vacina"),
            rs.getInt("quantidade"),
            rs.getDate("validade") != null ? rs.getDate("validade").toLocalDate() : null,
            rs.getBoolean("ativa"),
            fab
        );
        return new Lote(
            rs.getInt("cod_lote"),
            rs.getLong("num_lote"),
            rs.getDate("data_lote") != null ? rs.getDate("data_lote").toLocalDate() : null,
            rs.getInt("qtda_enviado"),
            vacina
        );
    }
}
