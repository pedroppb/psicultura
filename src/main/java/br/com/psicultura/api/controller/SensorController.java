package br.com.psicultura.api.controller;


import br.com.psicultura.model.entity.Cliente;
import br.com.psicultura.model.entity.Sensor;
import br.com.psicultura.model.entity.Tanque;
import br.com.psicultura.service.SensorService;
import br.com.psicultura.api.dto.SensorDTO;

import br.com.psicultura.exception.RegraNegocioException;
import br.com.psicultura.service.TanqueService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
@Api("API de Sensors")
public class SensorController {

    private final SensorService service;
    private final TanqueService tanqueService;
    @GetMapping()
    public ResponseEntity get() {
        List<Sensor> sensors = service.getSensors();
        return ResponseEntity.ok(sensors.stream().map(SensorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um sensor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sensor encontrado"),
            @ApiResponse(code = 404, message = "Sensor não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do sensor") Long id) {
        Optional<Sensor> sensor = service.getSensorById(id);
        if (!sensor.isPresent()) {
            return new ResponseEntity("Sensor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sensor.map(SensorDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo sensor")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Sensor salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o sensor")
    })
    public ResponseEntity post(SensorDTO dto) {
        try {
            Sensor sensor = converter(dto);
            sensor = service.salvar(sensor);
            return new ResponseEntity(sensor, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, SensorDTO dto) {
        if (!service.getSensorById(id).isPresent()) {
            return new ResponseEntity("Sensor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Sensor sensor = converter(dto);
            sensor.setId(id);
            service.salvar(sensor);
            return ResponseEntity.ok(sensor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Sensor> sensor = service.getSensorById(id);
        if (!sensor.isPresent()) {
            return new ResponseEntity("Sensor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(sensor.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Sensor converter(SensorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Sensor sensor = modelMapper.map(dto, Sensor.class);
        if (dto.getIdTanque() != null) {
            Optional<Tanque> tanque = tanqueService.getTanqueById(dto.getIdTanque());
            if (!tanque.isPresent()) {
                sensor.setTanque(null);
            } else {
                sensor.setTanque(tanque.get());
            }
        }
        return sensor;
    }
}