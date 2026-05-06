import "./Feed.css";
import api from "./services/api";
import { useState, useEffect, useMemo, useCallback } from "react";
import { useNavigate } from "react-router-dom";

function Feed() {
  const [animes, setAnimes] = useState([]);
  const navigate = useNavigate();
  const [search, setSearch] = useState("");

  const carregarAnimes = useCallback(async () => {
  try {
    const token = localStorage.getItem("token");
    const response = await api.get("/animes/todos", {
      headers: { Authorization: `Bearer ${token}` }
    });
    setAnimes(response.data);
  } catch (error) {
    console.error("Erro ao buscar animes:", error);
  }
}, []);

  useEffect(() => {
    (async () => {
      await carregarAnimes();
    })();
  }, []);

  const animesFiltrados = useMemo(() => {
  return animes.filter((anime) =>
    anime.titulo.toLowerCase().includes(search.toLowerCase())
  );
}, [animes, search]);

  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/TelaCadastro");
  }

  return (
    <div className="feed-page">
      <header className="topbar">
        <h1>LetterboxdeAnime</h1> {/* perdão eu sou pessima com nomes */}
        <div className="top-right">
          <input
            placeholder="Explore titulos..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <button
            className="user-btn"
            onClick={handleLogout} // 
          >
            👤
          </button>
        </div>
      </header>

      <main className="layout">
        <section className="feed-posts">

          {animes.length === 0 ? (
            <p>Nenhum anime cadastrado ainda.</p>
          ) : (
            animesFiltrados.map((anime) => (
              <div className="post-card" key={anime.id}>
                <div className="post-user">
                  <strong>
                    {anime.usuario ? anime.usuario.nome : "Usuário"}
                  </strong>
                </div>

                <div className="post-content">
                  <img
                    src={
                      anime.imagemUrl
                        ? anime.imagemUrl
                        : "https://via.placeholder.com/200x250"
                    }
                    alt={anime.titulo}
                  />

                  <div className="post-info">
                    <h2>{anime.titulo}</h2>
                    <p>{anime.descricao}</p>
                    <div className="tags">
                      <span>{anime.categoria}</span>
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}

        </section>
      </main>

      <button
        className="floating-add"
        onClick={() => navigate("/CadastroAnime")}
      >
        +
      </button>
    </div>
  );
}

export default Feed;