import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, describe, expect, it, vi } from 'vitest';
import TelaLogin from './LoginUsuario';
import api from '../services/api';

vi.mock('../services/api', () => ({
  default: {
    post: vi.fn(),
  },
}));

function renderLogin() {
  return render(
    <MemoryRouter>
      <TelaLogin />
    </MemoryRouter>,
  );
}

describe('TelaLogin', () => {
  afterEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('mostra mensagem quando o formulario esta vazio', async () => {
    const user = userEvent.setup();

    renderLogin();

    await user.click(screen.getByRole('button', { name: /entrar/i }));

    expect(screen.getByText('Preencha todos os campos!')).toBeInTheDocument();
    expect(api.post).not.toHaveBeenCalled();
  });

  it('envia email e senha para a api ao fazer login', async () => {
    const user = userEvent.setup();
    api.post.mockResolvedValue({ data: 'token-teste' });

    const { container } = renderLogin();

    await user.type(container.querySelector('input[name="email"]'), 'ana@email.com');
    await user.type(container.querySelector('input[name="senha"]'), 'senha123');
    await user.click(screen.getByRole('button', { name: /entrar/i }));

    expect(api.post).toHaveBeenCalledWith('/auth/login', {
      email: 'ana@email.com',
      senha: 'senha123',
    });
  });
});
