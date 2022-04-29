package com.admintool.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total")
    private Float total;

    @Column(name = "fecha_generado")
    private LocalDate fechaGenerado;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "preparado")
    private Boolean preparado;

    @Column(name = "entregado")
    private Boolean entregado;

    @Column(name = "foraneo")
    private Boolean foraneo;

    @JsonIgnoreProperties(value = { "pedido", "cliente" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Domicilio domicilio;

    @OneToMany(mappedBy = "pedido")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pedido" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "domicilios", "pedidos" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pedido id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTotal() {
        return this.total;
    }

    public Pedido total(Float total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public LocalDate getFechaGenerado() {
        return this.fechaGenerado;
    }

    public Pedido fechaGenerado(LocalDate fechaGenerado) {
        this.setFechaGenerado(fechaGenerado);
        return this;
    }

    public void setFechaGenerado(LocalDate fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }

    public LocalDate getFechaEntrega() {
        return this.fechaEntrega;
    }

    public Pedido fechaEntrega(LocalDate fechaEntrega) {
        this.setFechaEntrega(fechaEntrega);
        return this;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Boolean getPreparado() {
        return this.preparado;
    }

    public Pedido preparado(Boolean preparado) {
        this.setPreparado(preparado);
        return this;
    }

    public void setPreparado(Boolean preparado) {
        this.preparado = preparado;
    }

    public Boolean getEntregado() {
        return this.entregado;
    }

    public Pedido entregado(Boolean entregado) {
        this.setEntregado(entregado);
        return this;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getForaneo() {
        return this.foraneo;
    }

    public Pedido foraneo(Boolean foraneo) {
        this.setForaneo(foraneo);
        return this;
    }

    public void setForaneo(Boolean foraneo) {
        this.foraneo = foraneo;
    }

    public Domicilio getDomicilio() {
        return this.domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Pedido domicilio(Domicilio domicilio) {
        this.setDomicilio(domicilio);
        return this;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setPedido(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setPedido(this));
        }
        this.productos = productos;
    }

    public Pedido productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Pedido addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setPedido(this);
        return this;
    }

    public Pedido removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setPedido(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return id != null && id.equals(((Pedido) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", fechaGenerado='" + getFechaGenerado() + "'" +
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", preparado='" + getPreparado() + "'" +
            ", entregado='" + getEntregado() + "'" +
            ", foraneo='" + getForaneo() + "'" +
            "}";
    }
}
