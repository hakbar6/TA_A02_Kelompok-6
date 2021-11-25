package APAP.tugasAkhir.SIRETAIL.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter 
@Getter
@Entity
@Table(name = "item")

public class ItemCabangModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;

    @NotNull
    @Size(max=50)
    @Column(name = "nama_item", nullable = false)
    private String namaItem;

    @NotNull
    @Column(name = "harga_item", nullable = false)
    private Integer hargaItem;

    @NotNull
    @Column(name = "stok_item", nullable = false)
    private Integer stokItem;

    @NotNull
    @Size(max=100)
    @Column(name = "kategori", nullable = false)
    private String kategori;

    
}
