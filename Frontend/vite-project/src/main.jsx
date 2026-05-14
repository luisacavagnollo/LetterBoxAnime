import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import Feed from "./Pages/Feed.jsx";
import CadastroAnime from "./Pages/CadastroAnime.jsx";
import TelaCadastro from "./Pages/TelaCadastro.jsx";
import RotaProtegida from "./components/RotaProtegida.jsx";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import TelaLogin from "./Pages/LoginUsuario.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <TelaCadastro />,
  },
  {
    path: "/TelaCadastro",
    element: <TelaCadastro />,
  },
  {
    path: "/Feed",
    element: (
      <RotaProtegida>
        <Feed />
      </RotaProtegida>
    ),
  },
  {
    path: "/CadastroAnime",
    element: (
      <RotaProtegida>
        <CadastroAnime />
      </RotaProtegida>
    ),
  },
  {
    path: "/TelaLogin",
    element: <TelaLogin />,
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
);
