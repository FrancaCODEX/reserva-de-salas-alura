package br.com.francacodex.reservas.error;
public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) { super(mensagem); }
}
