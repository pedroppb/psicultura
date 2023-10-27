package br.com.psicultura.api.controller;

import br.com.psicultura.model.entity.Cliente;
import br.com.psicultura.model.entity.Tanque;
import br.com.psicultura.service.ClienteService;
import br.com.psicultura.service.TanqueService;
import br.com.psicultura.api.dto.TanqueDTO;

import br.com.psicultura.exception.RegraNegocioException;
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
@RequestMapping("/api/v1/tanques")
@RequiredArgsConstructor
@Api("API de Tanques")
public class TanqueController {

    private final TanqueService service;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity get() {
        List<Tanque> tanques = service.getTanques();
        return ResponseEntity.ok(tanques.stream().map(TanqueDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um tanque")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Tanque encontrado"),
            @ApiResponse(code = 404, message = "Tanque não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") @ApiParam("Id do tanque") Long id) {
        Optional<Tanque> tanque = service.getTanqueById(id);
        if (!tanque.isPresent()) {
            return new ResponseEntity("Tanque não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tanque.map(TanqueDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo tanque")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Tanque salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o tanque")
    })
    public ResponseEntity post(TanqueDTO dto) {
        try {
            Tanque tanque = converter(dto);
            tanque = service.salvar(tanque);
            return new ResponseEntity(tanque, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, TanqueDTO dto) {
        if (!service.getTanqueById(id).isPresent()) {
            return new ResponseEntity("Tanque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Tanque tanque = converter(dto);
            tanque.setId(id);
            service.salvar(tanque);
            return ResponseEntity.ok(tanque);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Tanque> tanque = service.getTanqueById(id);
        if (!tanque.isPresent()) {
            return new ResponseEntity("Tanque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tanque.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Tanque converter(TanqueDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Tanque tanque = modelMapper.map(dto, Tanque.class);
        if (dto.getIdcliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdcliente());
            if (!cliente.isPresent()) {
                tanque.setCliente(null);
            } else {
                tanque.setCliente(cliente.get());
            }
        }
        return tanque;
    }
}