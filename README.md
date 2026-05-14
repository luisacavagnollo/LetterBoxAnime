# LetterBoxAnime

Um aplicativo full-stack para cadastro e gerenciamento de animes, inspirado no Letterboxd, mas focado em animes.

## Tecnologias Utilizadas

### Backend

- **Java 17+**
- **Spring Boot** - Framework para desenvolvimento de aplicações Java
- **Maven** - Gerenciamento de dependências
- **JWT** - Autenticação e autorização
- **H2 Database** - Banco de dados em memória para desenvolvimento

### Frontend

- **React** - Biblioteca JavaScript para interfaces de usuário
- **Vite** - Ferramenta de build rápida para projetos modernos
- **Axios** - Cliente HTTP para fazer requisições à API
- **Cloudinary** - Serviço para upload e gerenciamento de imagens

## Pré-requisitos

- **Java 17 ou superior**
- **Node.js 16 ou superior**
- **Maven 3.6+**
- **Git**

## Instalação e Execução

### Backend

1. Navegue até a pasta `Backend`:

   ```bash
   cd Backend
   ```

2. Execute o aplicativo Spring Boot:

   ```bash
   ./mvnw spring-boot:run
   ```

   O backend estará rodando em `http://localhost:8081`.

### Frontend

1. Navegue até a pasta `Frontend/vite-project`:

   ```bash
   cd Frontend/vite-project
   ```

2. Instale as dependências:

   ```bash
   npm install
   ```

3. Execute o servidor de desenvolvimento:

   ```bash
   npm run dev
   ```

   O frontend estará rodando em `http://localhost:5173`.

## Estrutura do Projeto

```
LetterBoxAnime/
├── Backend/                 # Código do backend em Java/Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/cadastro_usuario/
│   │   │   │   ├── business/       # Lógica de negócio
│   │   │   │   ├── controller/     # Controladores REST
│   │   │   │   ├── infrastructure/ # Entidades e repositórios
│   │   │   │   └── security/       # Configurações de segurança
│   │   │   └── resources/          # Arquivos de configuração
│   │   └── test/                   # Testes
│   └── pom.xml                     # Dependências Maven
├── Frontend/               # Código do frontend em React
│   └── vite-project/
│       ├── src/
│       │   ├── components/         # Componentes React
│       │   ├── services/           # Serviços para API
│       │   └── assets/             # Recursos estáticos
│       ├── package.json            # Dependências Node.js
│       └── vite.config.js          # Configuração Vite
└── .gitignore              # Arquivos ignorados pelo Git
```

## Funcionalidades

- **Cadastro de Usuários**: Registro e login de usuários
- **Gerenciamento de Animes**: Adicionar, editar e visualizar animes
- **Autenticação JWT**: Segurança nas rotas da API
- **Upload de Imagens**: Integração com Cloudinary para imagens de animes

## API Endpoints

### Autenticação

- `POST /auth/login` - Login de usuário
- `POST /auth/register` - Registro de usuário

### Usuários

- `GET /usuarios` - Listar usuários
- `GET /usuarios/{id}` - Obter usuário por ID
- `PUT /usuarios/{id}` - Atualizar usuário
- `DELETE /usuarios/{id}` - Deletar usuário

### Animes

- `GET /animes` - Listar animes
- `GET /animes/{id}` - Obter anime por ID
- `POST /animes` - Criar novo anime
- `PUT /animes/{id}` - Atualizar anime
- `DELETE /animes/{id}` - Deletar anime
