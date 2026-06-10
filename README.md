# VacinePlus

Projeto desenvolvido para receber um banco dados onde foi integrado para verificar persistência de dados e manipulação de dados.
O foco principal era conseguir consultar o hitórico de vacinação de diversos pacientes.

Padrão DAO (Data Access Object).
Padrão de projeto utilizado para separar a lógica de acesso ao banco da lógica de negócio. Cada entidade possui seu próprio DAO, todos implementando a interface.

Orientação a Objetos — Herança e Polimorfismo
Herança: todas as entidades do domínio estendem a classe abstrata.
Polimorfismo: a interface DAO<T> permite tratar qualquer DAO de forma uniforme.

Java Swing
Biblioteca nativa do Java para construção de interfaces gráficas desktop.

## 🚀 Tecnologias
- Java
- Sql
- Java Swing

## ▶️ Uso
No teriminal certifique de estar no diretório correto e execute o comando:

java --module-path "VacinePlus/bin;VacinePlus/postgresql-42.7.11.jar" --module VacinePlus/view.MainFrame

## 📸 Preview
<img width="1917" height="1032" alt="image" src="https://github.com/user-attachments/assets/2022a7b8-926c-4fab-9e99-05be191a87e7" />

