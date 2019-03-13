package softuni.exodia.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    private static final String ID = "id";
    private static final String UUID_STRING = "uuid-string";
    private static final String GENERATOR_STRAT = "org.hibernate.id.UUIDGenerator";

    private String id;

    @Id
    @GeneratedValue(generator = UUID_STRING)
    @GenericGenerator(name = UUID_STRING, strategy = GENERATOR_STRAT)
    @Column(name = ID, unique = true, updatable = false, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
