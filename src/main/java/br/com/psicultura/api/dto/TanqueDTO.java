package br.com.psicultura.api.dto;

import br.com.psicultura.model.entity.Tanque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TanqueDTO {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ultimaLavagem;

    private Long idcliente;

    public static TanqueDTO create(Tanque tanque) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tanque, TanqueDTO.class);
    }
}