import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, describe, expect, it, vi } from 'vitest';
import Feed from './Feed';
import api from '../services/api';

vi.mock('../services/api', () => ({
  default: {
    get: vi.fn(),
  },
}));

function renderFeed() {
  return render(
    <MemoryRouter>
      <Feed />
    </MemoryRouter>,
  );
}

describe('Feed', () => {
  afterEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('lista os animes retornados pela api', async () => {
    localStorage.setItem('token', 'token-teste');
    api.get.mockResolvedValue({
      data: [
        {
          id: 1,
          titulo: 'Naruto',
          descricao: 'Ninja que quer ser Hokage',
          categoria: 'Acao',
          usuario: { nome: 'Ana' },
        },
      ],
    });

    renderFeed();

    expect(await screen.findByText('Naruto')).toBeInTheDocument();
    expect(screen.getByText('Ninja que quer ser Hokage')).toBeInTheDocument();
    expect(screen.getByText('Ana')).toBeInTheDocument();
    expect(api.get).toHaveBeenCalledWith('/animes/todos', {
      headers: { Authorization: 'Bearer token-teste' },
    });
  });

  it('filtra os animes pelo campo de busca', async () => {
    const user = userEvent.setup();
    api.get.mockResolvedValue({
      data: [
        { id: 1, titulo: 'Naruto', descricao: 'Ninja', categoria: 'Acao' },
        { id: 2, titulo: 'One Piece', descricao: 'Pirata', categoria: 'Aventura' },
      ],
    });

    renderFeed();

    expect(await screen.findByText('Naruto')).toBeInTheDocument();
    expect(screen.getByText('One Piece')).toBeInTheDocument();

    await user.type(screen.getByPlaceholderText('Explore titulos...'), 'one');

    await waitFor(() => {
      expect(screen.queryByText('Naruto')).not.toBeInTheDocument();
      expect(screen.getByText('One Piece')).toBeInTheDocument();
    });
  });
});
