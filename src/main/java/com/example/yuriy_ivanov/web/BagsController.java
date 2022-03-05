package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.BagsDto.BagRequest;
import com.example.yuriy_ivanov.dto.BagsDto.BagResponse;
import com.example.yuriy_ivanov.services.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bags")
public class BagsController {
    private final BagService bagService;

    @PostMapping
    public ResponseEntity<BagResponse> create(@RequestBody BagRequest bagRequest) {
        BagResponse bag = bagService.create(bagRequest);

        return ResponseEntity.ok(bag);
    }

    @GetMapping
    public ResponseEntity<List<BagResponse>> index(@PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        List<BagResponse> bags = bagService.all(pageable);

        return ResponseEntity.ok(bags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BagResponse> show(@PathVariable Long id) {
        BagResponse bagResponse = bagService.findById(id);

        return ResponseEntity.ok(bagResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BagResponse> update(@PathVariable Long id, @RequestBody BagRequest bagRequest) {
        BagResponse bagResponse = bagService.update(id, bagRequest);

        return ResponseEntity.ok(bagResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bagService.delete(id);

        return ResponseEntity.ok("Bag was successfully deleted.");
    }
}
