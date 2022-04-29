package com.admintool.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Catalogo.
 */
@Entity
@Table(name = "catalogo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "producto_desc")
    private String productoDesc;

    @Column(name = "precio_sugerido")
    private Float precioSugerido;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductoDesc() {
        return this.productoDesc;
    }

    public Catalogo productoDesc(String productoDesc) {
        this.setProductoDesc(productoDesc);
        return this;
    }

    public void setProductoDesc(String productoDesc) {
        this.productoDesc = productoDesc;
    }

    public Float getPrecioSugerido() {
        return this.precioSugerido;
    }

    public Catalogo precioSugerido(Float precioSugerido) {
        this.setPrecioSugerido(precioSugerido);
        return this;
    }

    public void setPrecioSugerido(Float precioSugerido) {
        this.precioSugerido = precioSugerido;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogo)) {
            return false;
        }
        return id != null && id.equals(((Catalogo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogo{" +
            "id=" + getId() +
            ", productoDesc='" + getProductoDesc() + "'" +
            ", precioSugerido=" + getPrecioSugerido() +
            "}";
    }
}
