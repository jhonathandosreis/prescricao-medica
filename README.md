# 💙 Sistema de Prescrição Médica

<div align="center">

![JavaEE](https://img.shields.io/badge/JavaEE-8-blue?style=for-the-badge&logo=java)
![PrimeFaces](https://img.shields.io/badge/PrimeFaces-15.0.8-green?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-336791?style=for-the-badge&logo=postgresql)
![WildFly](https://img.shields.io/badge/WildFly-26+-red?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**Sistema web completo para gerenciamento de prescrições médicas desenvolvido com JavaEE**

[Sobre](#-sobre) • [Funcionalidades](#-funcionalidades) • [Arquitetura](#-arquitetura)

</div>

---

## 📋 Sobre

Sistema de Prescrição Médica é uma aplicação web enterprise desenvolvida como teste técnico, implementando um CRUD completo para gerenciamento de pacientes, medicamentos e receitas médicas. O projeto segue as melhores práticas de arquitetura em camadas JavaEE, com interface moderna e responsiva.

### 🎯 Propósito

Facilitar o gerenciamento de prescrições médicas através de uma interface intuitiva, permitindo:

- Cadastro completo de pacientes e medicamentos
- Criação de receitas com múltiplos medicamentos
- Consultas avançadas e relatórios estatísticos
- Exportação de dados em Excel e PDF

---

## ✨ Funcionalidades

### 🏥 Gestão de Pacientes

- CRUD completo (Create, Read, Update, Delete)
- Validação de CPF com máscara automática
- Busca por nome e CPF
- Paginação com lazy loading
- Validação de CPF único no sistema

### 💊 Gestão de Medicamentos

- CRUD completo
- Cadastro com nome e descrição
- Busca por nome com filtros
- Paginação server-side
- Base com 100+ medicamentos pré-cadastrados

### 📝 Gestão de Receitas

- Associação de múltiplos medicamentos por receita
- Seleção de paciente via dropdown
- Adição/remoção dinâmica de medicamentos
- Campo de posologia para cada medicamento
- Visualização detalhada de receitas
- Data automática de prescrição

### 🔍 Consultas Avançadas

- Filtro por paciente
- Filtro por medicamento
- Filtros combinados
- Exibição de total de medicamentos por receita
- Badges visuais para quantidade
- Paginação com lazy loading

### 📊 Relatórios Estatísticos

- Top 2 medicamentos mais prescritos
- Top 2 pacientes com mais medicamentos
- Lista completa de pacientes com totais
- Exportação para Excel (.xlsx)
- Exportação para PDF
- Formatação profissional dos relatórios
- Atualização em tempo real

---

### Bibliotecas Adicionais

- **Apache POI 5.4.1** - Geração de arquivos Excel
- **iText 5.5.13.3** - Geração de arquivos PDF
- **OmniFaces 4.6.5** - Utilitários JSF

### Build & Deployment

- **Maven 3.6+** - Gerenciamento de dependências e build

---

## 🏗 Arquitetura

O sistema segue o padrão de arquitetura em camadas (Layered Architecture), garantindo separação de responsabilidades e manutenibilidade:

---

### Passo a Passo

#### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/prescricao-medica.git
cd prescricao-medica
```

#### 2. Configurar Banco de Dados

```bash
# Criar banco de dados
createdb -U postgres prescricao_medica

# Restaurar dump com dados de exemplo
psql -U postgres -d prescricao_medica -f prescricao_medica_dump.sql
```

#### 3. Configurar DataSource no WildFly

Consulte o arquivo `datasource-config.txt` para instruções detalhadas.

**Resumo:**

```bash
# Iniciar WildFly
cd $WILDFLY_HOME/bin
./standalone.sh  # Linux/Mac
# ou
standalone.bat   # Windows

# Em outro terminal, configurar datasource via CLI
./jboss-cli.sh --connect

# Executar comandos do datasource-config.txt
```

#### 4. Build da Aplicação

```bash
mvn clean package
```

#### 5. Deploy no WildFly

```bash
# Copiar WAR para deployment
cp target/prescricao-medica.war $WILDFLY_HOME/standalone/deployments/

# Ou via console administrativo em http://localhost:9990
```

#### 6. Acessar Aplicação

Abra seu navegador e acesse:

```
http://localhost:8080/prescricao-medica/
```

## 👨‍💻 Autor

Desenvolvido como teste técnico para demonstração de conhecimentos em JavaEE.
