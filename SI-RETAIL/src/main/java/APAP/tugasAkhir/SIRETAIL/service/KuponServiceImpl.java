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
        WebClient webClient = WebClient.builder().baseUrl("https://a02-4-sibusiness.herokuapp.com").build();
        Mono<List<CouponDTO>> response = webClient.get().uri("/api/v1/coupon")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CouponDTO>>() {});

        List<CouponDTO> couponDTOS = response.block().stream().collect(Collectors.toList());
        System.out.println("halo");
        return couponDTOS;
    }

    @Override
    public ItemCabangModel kuponApply(Long idItem, int idKupon, float discountAmount){
        ItemCabangModel itemInCabang = itemCabangDb.getById(idItem);
        int discount = Math.round(discountAmount) * itemInCabang.getHargaItem() / 100;
        int hargaItem = itemInCabang.getHargaItem();
        int hargaAkhir = hargaItem - discount;
        itemInCabang.setHargaItem(hargaAkhir);
        itemInCabang.setId_promo(idKupon);
        itemCabangDb.save(itemInCabang);
        System.out.println(discount);
        System.out.println(discountAmount);
        return itemInCabang;
    }
}
