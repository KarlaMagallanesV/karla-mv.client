package pe.edu.vallegrande.karla_mv.client.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("clientes")
public class Cliente {
    @Id
    private Long id;

    @Column("dni")
    private String dni;

    @Column("nombres")
    private String nombres;

    @Column("apellidos")
    private String apellidos;

    @Column("celular")
    private String celular;

    @Column("correo")
    private String correo;

    @Column("licencia")
    private String licencia;

    @Column("estado")
    private String estado;
}
