package br.com.b2wchallenge.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "planets")
public class Planet {

    @Id
    String id;

    @Indexed(unique = true)
    @NotBlank(message = "Name isn't defined")
    @Size(min = 3, message = "The name must be at least 3 characters")
    String name;

    @NotBlank(message = "Weather isn't defined")
    @Size(min = 3, message = "The weather must be at least 3 characters")
    String weather;

    @NotBlank(message = "Terrain isn't defined")
    @Size(min=3, message = "The terrain must be at least 3 characters")
    String terrain;

    @NotNull(message = "The quantity of appearance in films isn't defined")
    @Min(0)
    Integer films;
}
