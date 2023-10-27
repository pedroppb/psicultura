package br.com.psicultura.api.dto;


import br.com.psicultura.model.entity.Dado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadoDTO {

    private Long id;
    private Long idSensor;
    private Float valor;

    public static DadoDTO create(Dado dado) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dado, DadoDTO.class);
    }
}