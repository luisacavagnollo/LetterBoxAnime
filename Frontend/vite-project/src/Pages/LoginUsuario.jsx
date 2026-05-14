import { useState } from "react";
import "../style/TelaCadastro.css";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function TelaLogin() {
  const [form, setForm] = useState({
    email: "",
    senha: "",
  });

  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!form.email || !form.senha) {
      setErro("Preencha todos os campos!");
      return;
    }

    try {
      const response = await api.post("/auth/login", form);
      localStorage.setItem("token", response.data);
      navigate("/Feed");
    } catch (error) {
      console.error(error);
      setErro("Email ou senha incorretos");
    }
  }

  return (
    <div className="container">
      <h2>Login</h2>

      <form onSubmit={handleSubmit}>
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

        <button type="submit">Entrar</button>
      </form>

      <p>
        Não tem conta?{" "}
        <span onClick={() => navigate("/TelaCadastro")}>Cadastre-se</span>
      </p>
    </div>
  );
}

export default TelaLogin;
