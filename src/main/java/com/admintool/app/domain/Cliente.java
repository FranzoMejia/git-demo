package com.admintool.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pedido", "cliente" }, allowSetters = true)
    private Set<Domicilio> domicilios = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "domicilio", "productos", "cliente" }, allowSetters = true)
    private Set<Pedido> pedidos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Domicilio> getDomicilios() {
        return this.domicilios;
    }

    public void setDomicilios(Set<Domicilio> domicilios) {
        if (this.domicilios != null) {
            this.domicilios.forEach(i -> i.setCliente(null));
        }
        if (domicilios != null) {
            domicilios.forEach(i -> i.setCliente(this));
        }
        this.domicilios = domicilios;
    }

    public Cliente domicilios(Set<Domicilio> domicilios) {
        this.setDomicilios(domicilios);
        return this;
    }

    public Cliente addDomicilio(Domicilio domicilio) {
        this.domicilios.add(domicilio);
        domicilio.setCliente(this);
        return this;
    }

    public Cliente removeDomicilio(Domicilio domicilio) {
        this.domicilios.remove(domicilio);
        domicilio.setCliente(null);
        return this;
    }

    public Set<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        if (this.pedidos != null) {
            this.pedidos.forEach(i -> i.setCliente(null));
        }
        if (pedidos != null) {
            pedidos.forEach(i -> i.setCliente(this));
        }
        this.pedidos = pedidos;
    }

    public Cliente pedidos(Set<Pedido> pedidos) {
        this.setPedidos(pedidos);
        return this;
    }

    public Cliente addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setCliente(this);
        return this;
    }

    public Cliente removePedido(Pedido pedido) {
        this.pedidos.remove(pedido);
        pedido.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
