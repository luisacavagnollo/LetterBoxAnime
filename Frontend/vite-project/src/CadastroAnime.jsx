import { useState } from "react";
import "./CadastroAnime.css";
import api from "./services/api";
import { uploadImagem } from "./services/cloudinary";
import { useNavigate } from "react-router-dom";

function CadastroAnime() {
  const [form, setForm] = useState({
    titulo: "",
    categoria: "",
    imagemUrl: "",
    descricao: ""
  });

  const [preview, setPreview] = useState("");
  const [carregando, setCarregando] = useState(false);
  const navigate = useNavigate();

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  }

  async function handleImagem(e) {
    const file = e.target.files[0];
    if (file) {
      setCarregando(true);
      try {
        const url = await uploadImagem(file); 
        setPreview(url);
        setForm({ ...form, imagemUrl: url }); 
      } catch (error) {
        alert("Erro ao fazer upload da imagem");
        console.error(error);
      } finally {
        setCarregando(false);
      }
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!form.titulo || !form.categoria || !form.imagemUrl || !form.descricao) {
      alert("Preencha todos os campos!");
      return;
    }

    try {
      const token = localStorage.getItem("token");

      await api.post("/animes", form, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      alert("Anime cadastrado com sucesso!");
      navigate("/Feed");

    } catch (error) {
      console.error(error);
      alert("Erro ao cadastrar anime");
    }
  }

  return (
    <div className="container">
      <h2>Cadastro de Anime</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-fields">

          <div className="form-group">
            <label>Título:</label>
            <input
              type="text"
              name="titulo"
              value={form.titulo}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>Categoria:</label>
            <select
              name="categoria"
              value={form.categoria}
              onChange={handleChange}
            >
              <option value="">Selecione</option>
              <option value="Ação">Ação</option>
              <option value="Comedia">Comédia</option>
              <option value="Drama">Drama</option>
              <option value="Romance">Romance</option>
              <option value="Terror">Terror</option>
            </select>
          </div>

          <div className="form-group">
            <label>Descrição:</label>
            <textarea
              name="descricao"
              value={form.descricao}
              onChange={handleChange}
              placeholder="Digite a descrição do anime..."
            />
          </div>

          <div className="image-box">
            {carregando ? (
              <span>Enviando imagem...</span>
            ) : preview ? (
              <img src={preview} alt="Preview" className="preview-img" />
            ) : (
              <span>Selecione uma imagem de capa</span>
            )}
            <input
              type="file"
              accept="image/*"
              onChange={handleImagem}
            />
          </div>

          <button type="submit" disabled={carregando}>
            {carregando ? "Aguarde..." : "Cadastrar"}
          </button>

        </div>
      </form>
    </div>
  );
}

export default CadastroAnime;