package br.com.psicultura.api.controller;

import br.com.psicultura.model.entity.Dado;
import br.com.psicultura.model.entity.Sensor;
import br.com.psicultura.service.DadoService;
import br.com.psicultura.api.dto.DadoDTO;

import br.com.psicultura.exception.RegraNegocioException;
import br.com.psicultura.service.SensorService;
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
@RequestMapping("/api/v1/dados")
@RequiredArgsConstructor
@Api("API de Dados")
public class DadoController {

    private final DadoService service;
    private final SensorService sensorService;
    @GetMapping()
    public ResponseEntity get() {
        List<Dado> dados = service.getDados();
        return ResponseEntity.ok(dados.stream().map(DadoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um dado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Dado encontrado"),
            @ApiResponse(code = 404, message = "Dado não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do dado") Long id) {
        Optional<Dado> dado = service.getDadoById(id);
        if (!dado.isPresent()) {
            return new ResponseEntity("Dado não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(dado.map(DadoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo dado")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Dado salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o dado")
    })
    public ResponseEntity post(DadoDTO dto) {
        try {
            Dado dado = converter(dto);
            dado = service.salvar(dado);
            return new ResponseEntity(dado, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, DadoDTO dto) {
        if (!service.getDadoById(id).isPresent()) {
            return new ResponseEntity("Dado não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Dado dado = converter(dto);
            dado.setId(id);
            service.salvar(dado);
            return ResponseEntity.ok(dado);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Dado> dado = service.getDadoById(id);
        if (!dado.isPresent()) {
            return new ResponseEntity("Dado não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(dado.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Dado converter(DadoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Dado dado = modelMapper.map(dto, Dado.class);
        if (dto.getIdSensor() != null) {
            Optional<Sensor> sensor = sensorService.getSensorById(dto.getIdSensor());
            if (!sensor.isPresent()) {
                dado.setSensor(null);
            } else {
                dado.setSensor(sensor.get());
            }
        }
        return dado;
    }
}