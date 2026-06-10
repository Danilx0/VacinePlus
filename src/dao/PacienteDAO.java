package dao;

import model.Paciente;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements DAO<Paciente> {

    @Override
    public void inserir(Paciente obj) throws SQLException {
        String sql = "INSERT INTO paciente (cod_paciente, cpf, nome, data_nascimento, sexo, endereco, cidade, estado, cep) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, obj.getCodPaciente());
            ps.setString(2, obj.getCpf());
            ps.setString(3, obj.getNome());
            ps.setDate(4, Date.valueOf(obj.getDataNascimento()));
            ps.setString(5, obj.getSexo());
            ps.setString(6, obj.getEndereco());
            ps.setString(7, obj.getCidade());
            ps.setString(8, obj.getEstado());
            ps.setString(9, obj.getCep());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente ORDER BY nome";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE cpf = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        return new Paciente(
            rs.getInt("cod_paciente"),
            rs.getString("cpf").trim(),
            rs.getString("nome"),
            rs.getDate("data_nascimento").toLocalDate(),
            rs.getString("sexo"),
            rs.getString("endereco"),
            rs.getString("cidade"),
            rs.getString("estado"),
            rs.getString("cep") != null ? rs.getString("cep").trim() : ""
        );
    }
}
