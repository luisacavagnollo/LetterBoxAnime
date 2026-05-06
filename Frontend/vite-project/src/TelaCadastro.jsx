import { useState } from "react";
import "./TelaCadastro.css";
import api from "./services/api";
import { useNavigate } from "react-router-dom";

function Cadastro() {
  const [form, setForm] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!form.nome || !form.email || !form.senha) {
      setErro("Preencha todos os campos!");
      return;
    }

    try {
      await api.post("/usuarios", form);

      const login = await api.post("/auth/login", {
        email: form.email,
        senha: form.senha,
      });

      localStorage.setItem("token", login.data);

      navigate("/Feed");
    } catch (error) {
      console.error(error);
      setErro("Erro ao cadastrar usuário");
    }
  }

  return (
    <div className="container">
      <h2>Cadastro</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Nome:</label>
          <input
            type="text"
            name="nome"
            value={form.nome}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Senha:</label>
          <input
            type="password"
            name="senha"
            value={form.senha}
            onChange={handleChange}
          />
        </div>

        {erro && <p className="erro">{erro}</p>}

        <button type="submit">Cadastrar</button>
      </form>
      <p>
        Já tem conta?{" "}
        <span onClick={() => navigate("/TelaLogin")}>Fazer login</span>
      </p>
    </div>
  );
}

export default Cadastro;