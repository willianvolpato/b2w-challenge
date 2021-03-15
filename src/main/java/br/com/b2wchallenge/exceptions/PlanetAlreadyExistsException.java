package br.com.b2wchallenge.exceptions;

import br.com.b2wchallenge.models.Planet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanetAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 8188426372687168668L;
    private static final String ERROR_MSG = "The %s already exists";
    private Planet planet;

    public PlanetAlreadyExistsException(String mensage, Throwable th) {
        super(mensage, th);
    }

    public PlanetAlreadyExistsException(String mensage) {
        super(mensage);
    }

    public PlanetAlreadyExistsException(Throwable th) {
        super(String.format(ERROR_MSG, ""));
    }

    public PlanetAlreadyExistsException() {
        super();
    }

    @Override
    public String getMessage() {
        if (planet == null)
            return super.getMessage();

        return String.format(ERROR_MSG, planet.toString());
    }
}
