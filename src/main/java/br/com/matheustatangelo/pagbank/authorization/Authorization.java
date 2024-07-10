package br.com.matheustatangelo.pagbank.authorization;

public record Authorization(
        String message
) {
    public boolean isAuthorized() {
        return message.equals("Autorizado");
    }
}
