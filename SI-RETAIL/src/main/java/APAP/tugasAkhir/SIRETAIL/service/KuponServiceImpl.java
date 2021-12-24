package APAP.tugasAkhir.SIRETAIL.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.ItemCabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.BaseResponse;
import APAP.tugasAkhir.SIRETAIL.rest.CouponDTO;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class KuponServiceImpl implements KuponService{
    @Autowired
    ItemCabangDb itemCabangDb;

    public List<CouponDTO> getAllKupon(){
        WebClient webClient = WebClient.builder().baseUrl("https://si-business.herokuapp.com").build();
        Mono<BaseResponse<List<CouponDTO>>> response = webClient.get().uri("/api/list-couponToday")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<CouponDTO>>>() {});

        BaseResponse<List<CouponDTO>> couponDTOS = response.block();
        System.out.println("halo");
        System.out.println(couponDTOS.getResult().get(0).couponName);
        return couponDTOS.getResult().stream().collect(Collectors.toList());
    }

    @Override
    public ItemCabangModel kuponApply(Long idItem, int idKupon, float discountAmount){
        ItemCabangModel itemInCabang = itemCabangDb.getById(idItem);
        int discount = Math.round(discountAmount / 100) * itemInCabang.getHargaItem();
        int hargaItem = itemInCabang.getHargaItem();
        itemInCabang.setHargaItem(hargaItem - discount);
        itemInCabang.setId_promo(idKupon);
        itemCabangDb.save(itemInCabang);
        return itemInCabang;
    }
}
