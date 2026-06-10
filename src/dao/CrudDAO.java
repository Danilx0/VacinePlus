package dao;

import model.Paciente;
import model.Vacina;
import util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrudDAO {

    public void inserirViaProc(int codFabricante, String nomeFabricante,
                                int codVacina, String nomeVacina,
                                int quantidade, LocalDate validade, boolean ativa) throws SQLException {
        String sql = "CALL public.inserir_vacina_fabricante(?, ?, ?, ?, ?, ?, ?)";
        try (CallableStatement cs = Conexao.getConexao().prepareCall(sql)) {
            cs.setInt(1, codFabricante);
            cs.setString(2, nomeFabricante);
            cs.setInt(3, codVacina);
            cs.setString(4, nomeVacina);
            cs.setInt(5, quantidade);
            cs.setDate(6, Date.valueOf(validade));
            cs.setBoolean(7, ativa);
            cs.execute();
        }
    }

    public void excluirVacina(int codVacina) throws SQLException {
        String sql = "DELETE FROM vacina WHERE cod_vacina = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codVacina);
            ps.executeUpdate();
        }
    }

    public void excluirPaciente(int codPaciente) throws SQLException {
        String sql = "DELETE FROM paciente WHERE cod_paciente = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codPaciente);
            ps.executeUpdate();
        }
    }

    public int proximoCodigoVacina() throws SQLException {
        String sql = "SELECT COALESCE(MAX(cod_vacina), 0) + 1 FROM vacina";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    public int proximoCodigoFabricante() throws SQLException {
        String sql = "SELECT COALESCE(MAX(cod_fabricante), 0) + 1 FROM fabricante";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    public int proximoCodigoPaciente() throws SQLException {
        String sql = "SELECT COALESCE(MAX(cod_paciente), 0) + 1 FROM paciente";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }
    
    public void atualizarVacina(int codVacina, String nome, int quantidade, LocalDate validade, boolean ativa, String nomeFabricante ) throws SQLException {
        String sql = "UPDATE vacina SET nome = ?, quantidade = ?, validade = ?, ativa = ? WHERE cod_vacina = ?";
        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, quantidade);
            ps.setDate(3, Date.valueOf(validade));
            ps.setBoolean(4, ativa);
            ps.setInt(5, codVacina);
            ps.executeUpdate();
        }
        
        String sqlFabricante = "UPDATE fabricante SET nome = ? WHERE cod_fabricante = " +
                "(SELECT fabricante FROM vacina WHERE cod_vacina = ?)";
		try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sqlFabricante)) {
		ps.setString(1, nomeFabricante);
		ps.setInt(2, codVacina);
		ps.executeUpdate();
		}
    }
    public void atualizarPaciente(int codPaciente, String nome, String cpf, LocalDate dataNascimento,
            String sexo, String endereco, String cidade, String estado, String cep) throws SQLException {
			String sql = "UPDATE paciente SET nome = ?, cpf = ?, data_nascimento = ?, sexo = ?, endereco = ?, cidade = ?, estado = ?, cep = ? WHERE cod_paciente = ?";
			try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
			ps.setString(1, nome);
			ps.setString(2, cpf);
			ps.setDate(3, Date.valueOf(dataNascimento));
			ps.setString(4, sexo);
			ps.setString(5, endereco);
			ps.setString(6, cidade);
			ps.setString(7, estado);
			ps.setString(8, cep);
			ps.setInt(9, codPaciente);
			ps.executeUpdate();
		}
    }

    public List<Vacina> listarVacinas() throws SQLException {
        return new VacinaDAO().listarTodos();
    }

    public List<Paciente> listarPacientes() throws SQLException {
        return new PacienteDAO().listarTodos();
    }

    public void inserirPaciente(Paciente p) throws SQLException {
        new PacienteDAO().inserir(p);
    }
}
