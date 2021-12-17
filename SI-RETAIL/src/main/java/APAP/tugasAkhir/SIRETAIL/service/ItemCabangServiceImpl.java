package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.repository.ItemCabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.BaseResponse;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ItemCabangServiceImpl implements ItemCabangService{
    private final WebClient webClient;

    @Autowired
    private ItemCabangDb itemCabangDb;

    @Autowired
    private CabangDb cabangDb;

    public ItemCabangServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("https://si-item.herokuapp.com").build();
    }

    // fungsi untuk retrive item dari si-item
    @Override
    public List<ItemDTO> getAllItem(){
        WebClient webClient = WebClient.builder().baseUrl("https://si-item.herokuapp.com").build();
        Mono<BaseResponse<List<ItemDTO>>> response = webClient.get().uri("/api/item")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<ItemDTO>>>() {});

        BaseResponse<List<ItemDTO>> itemDTOS = response.block();
        return itemDTOS.getResult().stream().collect(Collectors.toList());
    }

    //Not used, refer to getItemByUuidImprv
    @Override
    public Optional<ItemCabangModel> getItemByUuid(String uuid){
        return itemCabangDb.findByUuid(uuid);
    }
    //Not used, refer to getItemByUuidImprv
    @Override
    public ItemCabangModel getItemByUuid2 (String uuid){
        Optional<ItemCabangModel> item = itemCabangDb.findByUuid(uuid);
        return item.get();
    }

    @Override
    public void addItemCabang (ItemCabangModel item){
        itemCabangDb.save(item);
    }

    @Override
    public ItemCabangModel getItemInCabang (CabangModel cabang, String uuid){
        List<ItemCabangModel> listItemCabang = cabang.getListItem();
        for(ItemCabangModel item : listItemCabang){
            if(item.getUuid().equals(uuid)){
                return item;
            }
        }
        return null;
    }

    @Override
    public ItemCabangModel getItemByUuidImprv (String UUID){
        Optional<ItemCabangModel> itemInCabang = itemCabangDb.findByUuid(UUID);
        if (itemInCabang.isPresent()){
            return itemInCabang.get();
        }
        else{
            throw new NoSuchElementException();
        }
    }

    @Override
    public ItemCabangModel getItemById (Long id){
        Optional<ItemCabangModel> itemInCabang = itemCabangDb.findById(id);
        if (itemInCabang.isPresent()){
            return itemInCabang.get();
        }
        else{
            throw new NoSuchElementException();
        }
    }

    @Override// Cek kecukupan barang, sekalian update
    public boolean penggunaanBarang (String uuid, int penggunaan, Long noCabang){
        WebClient webClient = WebClient.builder().baseUrl("https://si-item.herokuapp.com").build();
        Mono<BaseResponse<ItemDTO>> response = webClient.get().uri("/api/item/" + uuid)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<ItemDTO>>() {});

        BaseResponse<ItemDTO> output = response.block();
        ItemDTO fokus = output.getResult();

        if(penggunaan > fokus.stok){
            return false;
        }
        else{
            int hasil = 0;
            hasil = fokus.stok -= penggunaan;

            ItemCabangModel item = new ItemCabangModel();
            item.setCabang(cabangDb.getById(noCabang));
            item.setUuid(uuid);
            item.setNamaItem(fokus.nama);
            item.setStokItem(penggunaan);
            item.setKategori(fokus.kategori);
            item.setHargaItem(fokus.harga);

            //--------------------------------------------------------------------------------//
            Map<String, Object> bodyMap = new HashMap<String, Object>();
            bodyMap.put("stok",hasil);

            Mono<BaseResponse<ItemDTO>> responseCallBack = webClient.put().uri("/api/item/" + uuid)
            .body(BodyInserters.fromValue(bodyMap))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponse<ItemDTO>>() {});
            return true;
        }
    }
    @Override
    public List<ItemCabangModel> getListItem(){
        return itemCabangDb.findAll();
    }
}
