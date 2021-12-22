package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.ItemCabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.BaseResponse;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import ch.qos.logback.core.util.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
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

    @Override
    public ItemDTO getItem(String uuid) {
        // TODO Auto-generated method stub
        WebClient webClient = WebClient.builder().baseUrl("https://si-item.herokuapp.com").build();
        Mono<BaseResponse<ItemDTO>> response = webClient.get().uri("/api/item/"+uuid)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<ItemDTO>>(){});
        ItemDTO itemDTO = response.block().getResult();
        System.out.println(itemDTO.nama);
        return itemDTO;
    }

    @Override
    public void addItem(ItemCabangModel item) {
        // TODO Auto-generated method stub
        itemCabangDb.save(item);        
    }

    @Override
    public void updateSiItem(String uuid, int newStock) {
        // TODO Auto-generated method stub
        WebClient webClient = WebClient.builder().baseUrl("https://si-item.herokuapp.com").build();
        HashMap rb = new HashMap<>();
        rb.put("stok", newStock);
        HashMap response = webClient.put().uri("/api/item/" + uuid)
        .body(Mono.just(rb), HashMap.class)
        .retrieve()
        .bodyToMono(HashMap.class)
        .block();
        // System.out.println(response);
        
    }

    @Override
    public void requestItem(String uuid, Integer kategori, int stock, Long noCabang) {
        // TODO Auto-generated method stub
        WebClient webClient = WebClient.builder().baseUrl("https://a02-5-sifactory.herokuapp.com").build();
        HashMap rb = new HashMap<>();
        rb.put("uuid", uuid);
        rb.put("stok", stock);
        rb.put("idKategori", Integer.valueOf(kategori));
        rb.put("idCabang", noCabang);
        HashMap response = webClient.post().uri("/api/rui")
        .body(Mono.just(rb), HashMap.class)
        .retrieve()
        .bodyToMono(HashMap.class)
        .block();
        // System.out.println(response);
    }

    @Override
    public ItemCabangModel getItemFromDB(Long id) {
        // TODO Auto-generated method stub
        Optional<ItemCabangModel> item = itemCabangDb.findById(id);
        if (item.isPresent()){
            return item.get();
        }else{
            throw new NoSuchElementException();
        }
    }

    @Override
    public void deleteItemFromDB(Long id){
        ItemCabangModel itm = itemCabangDb.getById(id);
        itemCabangDb.delete(itm);
        // Optional<ItemCabangModel> item = itemCabangDb.findById(id);
        // if (item.isPresent()) {
        //     ItemCabangModel itm = item.get();
        //     itemCabangDb.delete(itm);
        // } else {
        //     throw new NoSuchElementException();
        // }
    }

}
