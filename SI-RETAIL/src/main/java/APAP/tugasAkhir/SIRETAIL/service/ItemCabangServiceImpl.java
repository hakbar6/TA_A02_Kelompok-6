package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
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
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ItemCabangServiceImpl implements ItemCabangService{

    @Autowired
    private ItemCabangDb itemCabangDb;

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
        
    }




}
