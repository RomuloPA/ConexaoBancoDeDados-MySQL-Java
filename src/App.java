import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir pessoa");
            System.out.println("2 - Excluir pessoa");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Digite o nome da pessoa: ");
                String nome = scanner.nextLine();

                System.out.print("Digite a idade da pessoa: ");
                int idade = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Digite o sexo da pessoa: ");
                String sexo = scanner.nextLine();

                String insertQuery = "INSERT INTO pessoas (nome, idade, sexo) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, idade);
                preparedStatement.setString(3, sexo);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Pessoa inserida com sucesso!");
                }

                preparedStatement.close();
            } else if (choice == 2) {
                String selectQuery = "SELECT id, nome, idade, sexo FROM pessoas";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = selectStatement.executeQuery();

                System.out.println("Lista de pessoas cadastradas:");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    int idade = resultSet.getInt("idade");
                    String sexo = resultSet.getString("sexo");
                    System.out.println("ID: " + id + ", Nome: " + nome + ", Idade: " + idade + ", Sexo: " + sexo);
                }

                System.out.print("Digite o ID da pessoa que deseja excluir: ");
                int idToDelete = scanner.nextInt();

                String deleteQuery = "DELETE FROM pessoas WHERE id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, idToDelete);
                int rowsDeleted = deleteStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Pessoa excluída com sucesso!");
                }

                deleteStatement.close();
            }

            connection.close();
            scanner.close();
        } catch (SQLException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }
    }
}