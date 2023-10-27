package br.com.psicultura.api.dto;


import br.com.psicultura.model.entity.Sensor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime manutencao;

    private Long idTanque;

    public static SensorDTO create(Sensor sensor) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(sensor, SensorDTO.class);
    }
}