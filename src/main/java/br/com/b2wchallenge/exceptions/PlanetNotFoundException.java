package br.com.b2wchallenge.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanetNotFoundException extends Exception {

    private static final long serialVersionUID = -9212762337031342684L;
    private static final String ERROR_MSG = "Planet %s not found";
    private String id;

    public PlanetNotFoundException(String message, Throwable th) {
        super(message, th);
    }

    public PlanetNotFoundException(String message) {
        super(message);
    }

    public PlanetNotFoundException(Throwable th) {
        super(String.format(ERROR_MSG, ""));
    }

    public PlanetNotFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        if (id == null)
            return super.getMessage();

        return String.format(ERROR_MSG, id);
    }
}
