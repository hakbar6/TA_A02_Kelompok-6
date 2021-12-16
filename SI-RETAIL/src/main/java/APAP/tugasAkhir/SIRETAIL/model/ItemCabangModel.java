package APAP.tugasAkhir.SIRETAIL.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="uuid_item", nullable = false, unique = true)
    private String uuid;

    @NotNull
    @Size(max=50)
    @Column(name = "nama_item", nullable = false)
    private String namaItem;

    @NotNull
    @Column(name = "harga_item", nullable = false)
    private int hargaItem;

    @NotNull
    @Column(name = "stok_item", nullable = false)
    private int stokItem;

    @NotNull
    @Size(max=100)
    @Column(name = "kategori", nullable = false)
    private String kategori;

    // Relasi dengan cabang model (Many to One)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "noCabang", referencedColumnName = "noCabang", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CabangModel cabang;

    @Column(name = "id_promo")
    private int id_promo;
}
