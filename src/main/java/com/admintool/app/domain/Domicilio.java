package com.admintool.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Domicilio.
 */
@Entity
@Table(name = "domicilio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "ubicacion")
    private String ubicacion;

    @JsonIgnoreProperties(value = { "domicilio", "productos", "cliente" }, allowSetters = true)
    @OneToOne(mappedBy = "domicilio")
    private Pedido pedido;

    @ManyToOne
    @JsonIgnoreProperties(value = { "domicilios", "pedidos" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Domicilio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Domicilio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Domicilio ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        if (this.pedido != null) {
            this.pedido.setDomicilio(null);
        }
        if (pedido != null) {
            pedido.setDomicilio(this);
        }
        this.pedido = pedido;
    }

    public Domicilio pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Domicilio cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domicilio)) {
            return false;
        }
        return id != null && id.equals(((Domicilio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Domicilio{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            "}";
    }
}
