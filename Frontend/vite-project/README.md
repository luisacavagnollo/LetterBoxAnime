# LetterboxdeAnime

Uma aplicação React para cadastro e visualização de animes, inspirada no Letterboxd. Permite que usuários se cadastrem, façam login, cadastrem animes com imagens e visualizem um feed de animes.

## Funcionalidades

- **Cadastro de Usuário**: Registre-se com nome, email e senha.
- **Login**: Faça login para acessar recursos protegidos.
- **Feed de Animes**: Visualize animes cadastrados por usuários, com busca por título.
- **Cadastro de Anime**: Adicione novos animes com título, categoria, descrição e imagem de capa (upload via Cloudinary).
- **Rotas Protegidas**: Acesso restrito a usuários autenticados.

## Tecnologias Utilizadas

- **Frontend**: React 19, React Router DOM, Vite
- **Estilos**: CSS personalizado
- **API**: Axios para comunicação com backend (localhost:8081)
- **Upload de Imagens**: Cloudinary
- **Ferramentas de Desenvolvimento**: ESLint, Vite

## Instalação

1. Clone o repositório:
   ```sh
   git clone <url-do-repositorio>
   cd vite-project
   ```

2. Instale as dependências:
   ```sh
   npm install
   ```

3. Certifique-se de que o backend esteja rodando em `http://localhost:8081` (não incluído neste projeto).

4. Configure o preset do Cloudinary em [`vite-project/src/services/cloudinary.js`](vite-project/src/services/cloudinary.js) se necessário.

## Uso

1. Inicie o servidor de desenvolvimento:
   ```sh
   npm run dev
   ```

2. Abra o navegador em `http://localhost:5173` (porta padrão do Vite).

3. Navegue pelas rotas:
   - `/`: Página de cadastro.
   - `/TelaCadastro`: Cadastro de usuário.
   - `/TelaLogin`: Login.
   - `/Feed`: Feed de animes (protegido).
   - `/CadastroAnime`: Cadastro de anime (protegido).

## Scripts Disponíveis

- `npm run dev`: Inicia o servidor de desenvolvimento.
- `npm run build`: Constrói a aplicação para produção.
- `npm run lint`: Executa o ESLint.
- `npm run preview`: Visualiza a build de produção.

## Estrutura do Projeto

- [`vite-project/src/main.jsx`](vite-project/src/main.jsx): Ponto de entrada e configuração de rotas.
- [`vite-project/src/services/api.js`](vite-project/src/services/api.js): Configuração do Axios para API.
- [`vite-project/src/services/cloudinary.js`](vite-project/src/services/cloudinary.js): Função de upload de imagens.
- Componentes principais: [`Feed.jsx`](vite-project/src/Feed.jsx), [`CadastroAnime.jsx`](vite-project/src/CadastroAnime.jsx), [`TelaCadastro.jsx`](vite-project/src/TelaCadastro.jsx), [`LoginUsuario.jsx`](vite-project/src/LoginUsuario.jsx).
- Estilos: [`Feed.css`](vite-project/src/Feed.css), [`CadastroAnime.css`](vite-project/src/CadastroAnime.css), [`TelaCadastro.css`](vite-project/src/TelaCadastro.css).

## Contribuição

Contribuições são bem-vindas! Abra issues ou pull requests no repositório.

## Licença

Este projeto é privado e não possui licença específica.