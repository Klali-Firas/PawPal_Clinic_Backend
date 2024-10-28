package com.PawPal_Clinic.Backend.controller;

import com.PawPal_Clinic.Backend.dto.RappelDto;
import com.PawPal_Clinic.Backend.service.RappelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/rappels")
public class RappelController {

    @Autowired
    private RappelService rappelService;

    @GetMapping
    public ResponseEntity<List<RappelDto>> getAllRappels() {
        List<RappelDto> rappels = rappelService.getAllRappels();
        return ResponseEntity.ok(rappels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RappelDto> getRappelById(@PathVariable Integer id) {
        Optional<RappelDto> rappel = rappelService.getRappelById(id);
        return rappel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RappelDto> createRappel(@RequestBody RappelDto rappelDto) {
        RappelDto createdRappel = rappelService.createRappel(rappelDto);
        return ResponseEntity.ok(createdRappel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RappelDto> updateRappel(@PathVariable Integer id, @RequestBody RappelDto rappelDto) {
        Optional<RappelDto> updatedRappel = rappelService.updateRappel(id, rappelDto);
        return updatedRappel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRappel(@PathVariable Integer id) {
        boolean deleted = rappelService.deleteRappel(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}