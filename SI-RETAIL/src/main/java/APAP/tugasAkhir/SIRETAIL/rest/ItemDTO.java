package APAP.tugasAkhir.SIRETAIL.rest;


public class ItemDTO {
    public String uuid;
    public String nama;
    public int harga;
    public int stok;
    public String kategori;
    public void setStok (Integer stok){
        this.stok = stok;
    }
}


