package exercicios;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // A FORMA DE CONEXÃO UTILIZADA BEM COMO USUÁRIO E SENHA HARDCODED
        // FOI CONSTRUÍDO SOMENTE PARA FINS DIDÁTICOS COMO TAREFA
        // DA PÓS DESENV. JAVA DA UNYLEYA - by Wagner Pires

        Scanner ler = new Scanner(System.in);
        char insert = 0;
        try {
            System.out.printf("Deseja inserir valores (S/N):\n ");
            insert = (char) System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Double valor = 0.00;
        String data = null;

        if (Character.toUpperCase(insert) == 'S') {
            System.out.printf("Digite o valor (0,00):\n ");
            valor = ler.nextDouble();
            ler.nextLine();
            System.out.printf("Digite uma data (YYYY-MM-DD):\n ");
            data = ler.nextLine();
        }
        String url = "jdbc:postgresql://ec2-34-194-171-47.compute-1.amazonaws.com/d53pt4g95nkpa4a";
        Properties props = new Properties();
        props.setProperty("user", "bzjcqmdvrqcdoia");
        props.setProperty("password", "14b11a83502db54c41eb8dc6951afeac09a9d29399ce9f9bd2550943cb9eced0a");
        props.setProperty("sslmode", "require");

        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("FALHA NA CONEXÃO");
                e.printStackTrace();
            }
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println("CONECTADO AO SGBD UNYLEYA NA NUVEM - HEROKU\n ");

            if (Character.toUpperCase(insert) == 'S') {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO log (DATA, VALOR) VALUES (?, ?)");
                ps.setDate(1, java.sql.Date.valueOf(data));
                ps.setDouble(2, valor);
                ps.executeUpdate();
                ps.close();
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, TO_CHAR(data, 'dd/mm/yyyy') AS data_f, " +
                    " REPLACE(REPLACE(REPLACE(REPLACE(((valor)::money)::text,'$','R$ '),',','|'),'.',','),'|','.')" +
                    " FROM log ORDER BY data");
            while (rs.next()) {
                System.out.print(" -> ");
                System.out.print(((ResultSet) rs).getString(1) + "   ");
                System.out.print(((ResultSet) rs).getString(2) + "   ");
                System.out.println(((ResultSet) rs).getString(3));
            }

            st.close();
            rs.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("FALHA NA CONEXÃO");
            e.printStackTrace();
        }
    }
}
