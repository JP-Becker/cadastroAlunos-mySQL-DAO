package dao;

import java.sql.*;
import java.util.ArrayList;
import model.Aluno;

public class AlunoDAO {

    public static ArrayList<Aluno> minhaLista = new ArrayList<>();

    public Connection getConexao() {
        Connection connection = null;

        try {
            // Carregando o JDBC driver, tortnando disponível os métodos SQL para serem usados em java
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);

            // Configurar a conexão
            String server = "localhost";
            String database = "db_alunos";
            String url = "jdbc:mysql://" + server + ":3306/" + database + "?useTimezone=true&serverTimezone=UTC";
            String user = "root";
            String password = "root";

            // Conectando-
            connection = DriverManager.getConnection(url, user, password);

            //Testando-
            if (connection != null) {
                System.out.println("Status- conectado!");
            } else {
                System.out.println("Status- NÃO FOI POSSIVEL CONECTAR!");
            }

            return connection;

        } catch (ClassNotFoundException e) { //Driver não encontrado
            System.out.println("O driver nao foi encontrado.");
            return null;

        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar...");
            return null;
        }
    }

    public ArrayList<Aluno> getMinhaLista() {
        minhaLista.clear();

        try {
            Statement stmt = this.getConexao().createStatement();

            // usando a classe resultSet para utilizar métodos getters do SQL
            ResultSet res = stmt.executeQuery("SELECT * FROM tb_alunos");

            while (res.next()) {
                // vai buscar e retornar a lista com todos os objetos 'Aluno'
                int id = res.getInt("id");
                String nome = res.getString("nome");
                int idade = res.getInt("idade");
                String curso = res.getString("curso");
                int fase = res.getInt("fase");

                Aluno objeto = new Aluno(id, nome, idade, curso, fase);
                minhaLista.add(objeto);

                res.close();
                stmt.close();
            }
        } catch (SQLException e) {
            return null;
        }
        return minhaLista;
    }

    // retorna o aluno procurado pela id
    public Aluno carregaAluno(int id) {
        Aluno objeto = new Aluno();
        objeto.setId(id);

        try {
            Statement stmt = this.getConexao().createStatement();

            // usando a classe resultSet para utilizar métodos getters referentes a tipos de dado do SQL
            ResultSet res = stmt.executeQuery("SELECT * FROM tb_alunos WHERE id = " + id);
            res.next();

            objeto.setNome(res.getString("nome"));
            objeto.setIdade(res.getInt("idade"));
            objeto.setCurso(res.getString("curso"));
            objeto.setFase(res.getInt("fase"));

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }

        return objeto;
    }

    // Método para cadastrar novo aluno
    public boolean insertAlunoBD(Aluno objeto) {
        String sql = "INSERT INTO tb_alunos(id, nome, idade, curso, fase) VALUES (?,?,?,?,?)";

        try {
            //objeto que representa uma instrução SQL a ser executada
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);

            stmt.setInt(1, objeto.getId());
            stmt.setString(2, objeto.getNome());
            stmt.setInt(3, objeto.getIdade());
            stmt.setString(4, objeto.getCurso());
            stmt.setInt(5, objeto.getFase());

            stmt.execute();
            stmt.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            throw new RuntimeException(e);
        }
    }

    public int maiorID() {
        int maiorID = 0;

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id) id FROM tb_alunos");

            res.next();
            maiorID = res.getInt("id");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        }
        
        return maiorID;
    }
    
    public boolean deleteAlunoBD(int id) {
        try {
            Statement stmt = this.getConexao().createStatement();
            stmt.executeUpdate("DELETE FROM tb_alunos WHERE id = " + id);
            stmt.close();
            
            return true;

        } catch (SQLException e) {
            System.out.println("Erro:" + e);
            throw new RuntimeException(e);
        }
      
    }

    public boolean updateAlunoBD (Aluno objeto) {
        String sql = "UPDATE tb_alunos set nome = ? ,idade = ? ,curso = ? ,fase = ? WHERE id = ?";
        
        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);
            
            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCurso());
            stmt.setInt(4, objeto.getFase());
            stmt.setInt(5, objeto.getId());
            stmt.execute();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro:" + e);
        throw new RuntimeException(e);
        }
    }
    
    public static void setMinhaLista(ArrayList<Aluno> minhaLista) {
        AlunoDAO.minhaLista = minhaLista;
    }
}
