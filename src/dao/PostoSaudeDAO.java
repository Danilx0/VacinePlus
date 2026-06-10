package dao;

import model.PostoSaude;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostoSaudeDAO implements DAO<PostoSaude> {

    @Override
    public void inserir(PostoSaude obj) throws SQLException {
        String sql = "INSERT INTO posto_saude (cod_posto, nome_posto, endereco, cidade, estado, telefone_posto, funcionamento) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodPosto());
            ps.setString(2, obj.getNome());
            ps.setString(3, obj.getEndereco());
            ps.setString(4, obj.getCidade());
            ps.setString(5, obj.getEstado());
            ps.setString(6, obj.getTelefonePosto());
            ps.setBoolean(7, obj.isFuncionamento());
            ps.executeUpdate();
        }
    }

    @Override
    public List<PostoSaude> listarTodos() throws SQLException {
        List<PostoSaude> lista = new ArrayList<>();
        String sql = "SELECT * FROM posto_saude WHERE funcionamento = true ORDER BY nome_posto";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new PostoSaude(
                    rs.getInt("cod_posto"),
                    rs.getString("nome_posto"),
                    rs.getString("endereco"),
                    rs.getString("cidade"),
                    rs.getString("estado"),
                    rs.getString("telefone_posto"),
                    rs.getBoolean("funcionamento")
                ));
            }
        }
        return lista;
    }

    public int totalAplicacoes(int codPosto) throws SQLException {
        String sql = "SELECT public.total_aplicacoes_posto(?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codPosto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
