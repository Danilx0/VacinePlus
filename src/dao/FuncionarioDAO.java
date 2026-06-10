package dao;

import model.Funcionario;
import model.PostoSaude;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO implements DAO<Funcionario> {

    @Override
    public void inserir(Funcionario obj) throws SQLException {
        String sql = "INSERT INTO funcionario (cod_funcionario, nome, cargo, unidade_posto) VALUES (?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodFuncionario());
            ps.setString(2, obj.getNome());
            ps.setString(3, obj.getCargo());
            ps.setInt(4, obj.getUnidadePosto().getCodPosto());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT f.*, ps.nome_posto, ps.endereco, ps.cidade, ps.estado, ps.telefone_posto, ps.funcionamento " +
                     "FROM funcionario f JOIN posto_saude ps ON ps.cod_posto = f.unidade_posto ORDER BY f.nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PostoSaude posto = new PostoSaude(
                    rs.getInt("unidade_posto"),
                    rs.getString("nome_posto"),
                    rs.getString("endereco"),
                    rs.getString("cidade"),
                    rs.getString("estado"),
                    rs.getString("telefone_posto"),
                    rs.getBoolean("funcionamento")
                );
                lista.add(new Funcionario(
                    rs.getInt("cod_funcionario"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    posto
                ));
            }
        }
        return lista;
    }
}
