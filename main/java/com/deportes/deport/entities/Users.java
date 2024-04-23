package com.deportes.deport.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "El username debe contener solo letras y números y tener entre 5 y 20 caracteres")
    private String username;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Formato de correo inválido")
    private String correo;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", 
             message = "La contraseña debe tener al menos 8 caracteres, una letra mayúscula,"+
                       "una letra minúscula, un número y un caractér especial")
    private String password;
    private Rol rol;

    private boolean enabled;

    // --------------------- RELACIONES ---------------------

    

    // --------------------- GETTERS AND SETTERS ---------------------
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((correo == null) ? 0 : correo.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((rol == null) ? 0 : rol.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        return result;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Users other = (Users) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (correo == null) {
            if (other.correo != null)
                return false;
        } else if (!correo.equals(other.correo))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (rol != other.rol)
            return false;
        if (enabled != other.enabled)
            return false;
        return true;
    }



    
    
    
}
