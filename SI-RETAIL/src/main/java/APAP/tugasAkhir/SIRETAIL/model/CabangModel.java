package APAP.tugasAkhir.SIRETAIL.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"cabang"}, allowSetters = true)
@Setter 
@Getter
@Entity
@Table(name = "cabang")
public class CabangModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noCabang;

    @NotNull
    @Size(max=30)
    @Column(name = "nama", nullable = false)
    private String namaCabang;

    @NotNull
    @Size(max=100)
    @Column(name = "alamat", nullable = false)
    private String alamatCabang;

    @NotNull
    @Column(name = "ukuran", nullable = false)
    private int ukuranCabang;

    @NotNull
    @Column(name = "status", nullable = false)
    private int statusCabang;

    @NotNull
    @Size(max = 20)
    @Column(name = "no_telp", nullable = false)
    private String noTelpCabang;

    // Many to one dengan user
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "penanggung_jawab", referencedColumnName = "id_user", nullable = false)
    private UserModel user;

    // One to many dengan item
    @OneToMany(mappedBy = "cabang", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemCabangModel> listItem;
}
