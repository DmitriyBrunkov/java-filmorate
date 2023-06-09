package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Objects;

@Data
public class Genre implements Comparable<Genre> {
    private Integer id;
    @Size(max = 255)
    private String name;

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(this.getId(), o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
