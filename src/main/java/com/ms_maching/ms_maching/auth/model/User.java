package com.ms_maching.ms_maching.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column
    private String email;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="empresa_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuarios"})
    private Empresa empresa;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    @JsonIgnoreProperties({"users"})
    private Set<Rol> roles = new HashSet<>();

    public User() {}
    // getters y setters...
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa emp) { this.empresa = emp; }
    public Set<Rol> getRoles() { return roles; }
    public void setRoles(Set<Rol> roles) { this.roles = roles; }

}