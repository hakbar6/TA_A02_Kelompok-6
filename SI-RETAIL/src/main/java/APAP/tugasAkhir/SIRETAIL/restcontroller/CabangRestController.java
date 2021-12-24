package APAP.tugasAkhir.SIRETAIL.restcontroller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.AlamatDTO;
import APAP.tugasAkhir.SIRETAIL.rest.BaseResponse;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;
import APAP.tugasAkhir.SIRETAIL.restservice.CabangRestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cabang")
public class CabangRestController {

    @Qualifier("cabangRestServiceImpl")
    @Autowired
    private CabangRestService cabangRestService;

    @Autowired
    private ModelMapper modelMapper;

    // pekerjaan harakan
    @PostMapping()
    private BaseResponse<CabangModel> createCabang(
            @Valid @RequestBody CabangDTO cabangDTO,
            BindingResult bindingResult) throws ParseException {
        BaseResponse<CabangModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                CabangModel newCabang = cabangRestService.createCabangAPI(cabangDTO);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(newCabang);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage("perhatikan data yang kamu kirimkan");
                response.setResult(null);
            }
            return response;
        }
    }
    // pekerjaan harakan tutup

    @GetMapping("/list-alamat")
    private BaseResponse<List<AlamatDTO>> getListAlamatCabang() {
        BaseResponse<List<AlamatDTO>> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        List<CabangModel> listCabang = cabangRestService.retrieveListCabang();
        response.setResult(
                listCabang.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
        return response;
    }

    private AlamatDTO convertToDto(CabangModel cabang) {
        AlamatDTO alamatDTO = modelMapper.map(cabang, AlamatDTO.class);
        return alamatDTO;
    }
}
